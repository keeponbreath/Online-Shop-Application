package spring.cloud.purchaseservice.service;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import spring.cloud.purchaseservice.entity.Purchase;
import spring.cloud.purchaseservice.enums.PurchaseStatus;
import spring.cloud.purchaseservice.exception.PurchaseException;
import spring.cloud.purchaseservice.feign.AccountClient;
import spring.cloud.purchaseservice.repository.PurchaseRepository;

import java.time.LocalDateTime;
import java.util.*;

@Service
public class PurchaseService {
    private final PurchaseRepository repository;
    private final AccountClient accountClient;
    private final RabbitTemplate template;

    @Autowired
    public PurchaseService(PurchaseRepository repository, AccountClient accountClient,
                           RabbitTemplate template) {
        this.repository = repository;
        this.accountClient = accountClient;
        this.template = template;
    }

    @PreAuthorize("hasRole('ROLE_MODERATOR') or hasRole('ROLE_ADMIN')")
    public List<Purchase> getAllPurchases() {
        return repository.findAll();
    }

    public Purchase getPurchaseById(Long id, Authentication auth) throws IllegalAccessException {
        Optional<Purchase> purchaseOptional = repository.findById(id);
        if(purchaseOptional.isPresent()) {
            if(accessCheck(purchaseOptional.get().getOwnerId(), auth)) {
                return purchaseOptional.get();
            } else {
                throw new IllegalAccessException("Access denied! Please contact an administrator");
            }
        } else {
            throw new NoSuchElementException("Purchase with id" + id + " not found!");
        }
    }

    public List<Purchase> getPurchasesByOwnerId(Long id, Authentication auth) throws IllegalAccessException {
        if(accessCheck(id, auth)) {
            List<Purchase> purchases = repository.getPurchasesByOwnerId(id);
            if(!purchases.isEmpty()) {
                return purchases;
            } else {
                throw new NoSuchElementException("Purchases with Owner ID " + id + " not found");
            }
        } else {
            throw new IllegalAccessException("Access denied! Please contact an administrator");
        }
    }

    @PreAuthorize("hasRole('ROLE_MODERATOR') or hasRole('ROLE_ADMIN')")
    public List<Purchase> getPurchasesByItemId(Long id) {
        List<Purchase> purchases = repository.getPurchasesByItemId(id);
        if(!purchases.isEmpty()) {
            return purchases;
        } else {
            throw new NoSuchElementException("Purchases with Item ID " + id + " not found");
        }
    }

    public List<Purchase> getPurchasesByItemIdAndOwnerId(Long itemId, Long ownerId,
                                                         Authentication auth) throws IllegalAccessException {
        if(accessCheck(ownerId, auth)) {
            List<Purchase> purchases = repository.getPurchasesByItemIdAndOwnerId(itemId, ownerId);
            if(!purchases.isEmpty()) {
                return purchases;
            } else {
                throw new NoSuchElementException("Purchases with Item ID " + itemId +
                        " and Owner ID " + ownerId + " not found");
            }
        } else {
            throw new IllegalAccessException("Access denied! Please contact an administrator");
        }
    }

    @RabbitListener(queues = "purchase_create")
    public void createPurchase(Map<String, String> info) {
        Purchase purchase = Purchase.builder()
                .ownerId(Long.valueOf(info.get("accountId")))
                .itemId(Long.valueOf(info.get("itemId")))
                .orgId(Long.valueOf(info.get("orgId")))
                .barCode(Long.valueOf(info.get("barCode")))
                .storageId(Long.valueOf(info.get("storageId")))
                .total(Double.valueOf(info.get("total")))
                .status(PurchaseStatus.CREATED)
                .createdAt(LocalDateTime.now())
                        .build();
        repository.save(purchase);
    }

    public Boolean payForPurchase(Long purchaseId, Authentication auth) throws IllegalAccessException {
        Purchase purchase = getPurchaseById(purchaseId, auth);
        if(!purchase.getStatus().equals(PurchaseStatus.CREATED)) {
            throw new PurchaseException("Purchase has been already paid, overdue or returned!");
        } else {
            if(purchase.getCreatedAt().isBefore(LocalDateTime.now().minusMinutes(30))) {
                purchase.setStatus(PurchaseStatus.OVERDUE);
                repository.save(purchase);
                return Boolean.FALSE;
            }
            ResponseEntity<Double> accountRequest = accountClient.checkBalance();
            Double balance = null;
            if(accountRequest.getStatusCode().isSameCodeAs(HttpStatusCode.valueOf(200))) {
                balance = accountRequest.getBody();
            }
            assert balance != null;
            Long orgId = purchase.getOrgId();
            if(balance > purchase.getTotal()) {
                Map<String, String> info = new HashMap<>();
                info.put("ownerId", purchase.getOwnerId().toString());
                info.put("orgId", orgId.toString());
                info.put("value", purchase.getTotal().toString());
                info.put("account_action", "outcome");
                info.put("org_action", "income");
                purchase.setStatus(PurchaseStatus.PAID);
                purchase.setPayedAt(LocalDateTime.now());
                repository.save(purchase);
                template.convertAndSend("purchase_action", "purchase_pay", info);
            } else {
                throw new PurchaseException("Not enough money to pay!");
            }
            return Boolean.TRUE;
        }
    }

    public Boolean returnPurchase(Long purchaseId, Authentication auth) throws IllegalAccessException {
        Purchase purchase = getPurchaseById(purchaseId, auth);
        if(!purchase.getStatus().equals(PurchaseStatus.PAID) &&
                purchase.getPayedAt().isBefore(LocalDateTime.now().minusDays(1L))) {
            throw new PurchaseException("Purchase is not paid or has been overdue");
        } else {
            Map<String, String> info = new HashMap<>();
            info.put("ownerId", purchase.getOwnerId().toString());
            info.put("orgId", purchase.getOrgId().toString());
            info.put("value", purchase.getTotal().toString());
            info.put("storageId", purchase.getStorageId().toString());
            info.put("barCode", purchase.getBarCode().toString());
            info.put("account_action", "income");
            info.put("org_action", "outcome");
            info.put("storage_action", "income");
            purchase.setStatus(PurchaseStatus.RETURNED);
            purchase.setUpdatedAt(LocalDateTime.now());
            repository.save(purchase);
            template.convertAndSend("purchase_action", "purchase_return", info);
            return Boolean.TRUE;
        }
    }

    @Scheduled(fixedDelay = 1000*60*30)
    public void overduePurchases() {
        List<Purchase> purchases = repository.getPurchasesByStatus(PurchaseStatus.CREATED);
        purchases.parallelStream()
                .filter(purchase -> purchase.getCreatedAt()
                        .isBefore(LocalDateTime.now().minusMinutes(30)))
                .forEach(purchase -> {
            purchase.setStatus(PurchaseStatus.OVERDUE);
            repository.save(purchase);
            Map<String, String> info = new HashMap<>();
            Long barCode = purchase.getBarCode();
            Long storageId = purchase.getStorageId();
            info.put("barCode", barCode.toString());
            info.put("storageId", storageId.toString());
            info.put("storage_action", "income");
            template.convertAndSend("purchase_action", "purchase_overdue", info);
        });
    }

    public boolean commentCheck(Long itemId, Long ownerId) {
        return !repository
                .getPurchasesByItemIdAndOwnerIdAndStatus(itemId, ownerId, PurchaseStatus.PAID).isEmpty()
                || !repository
                .getPurchasesByItemIdAndOwnerIdAndStatus(itemId, ownerId, PurchaseStatus.RETURNED).isEmpty();
    }

    private boolean accessCheck(Long id, Authentication auth) {
        if(auth.getAuthorities().toString().contains("ROLE_ADMIN")) {
            return true;
        } else {
            ResponseEntity<Long> accountRequest = accountClient.check();
            return accountRequest.getStatusCode().isSameCodeAs(HttpStatusCode.valueOf(200)) &&
                    Objects.equals(accountRequest.getBody(), id);
        }
    }
}