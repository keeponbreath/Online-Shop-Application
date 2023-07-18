package spring.cloud.accountservice.service;

import org.springframework.amqp.rabbit.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import spring.cloud.accountservice.dto.AccountRequest;
import spring.cloud.accountservice.entity.Account;
import spring.cloud.accountservice.repository.AccountsRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

@Service
public class AccountsService {
    private final AccountsRepository repository;

    @Autowired
    public AccountsService(AccountsRepository accountsRepository) {
        this.repository = accountsRepository;
    }

    public Account showAccount(Long id) {
        return repository.findById(id).orElseThrow(() ->
                new NoSuchElementException("Account with " + id + " not found!"));
    }

    @PreAuthorize("hasRole('ADMIN')")
    public List<Account> showAccounts() {
        return repository.findAll();
    }

    @RabbitListener(queues = "account_create")
    public void createAccount(Map<String, String> info) {
        Account account = new Account();
        String owner = info.get("username");
        account.setOwner(owner);
        account.setIsActive(true);
        account.setBalance(5000.0);
        account.setCreatedAt(LocalDateTime.now());
        repository.save(account);
    }

//    @RabbitListener(queues = "account_edit")
    //todo edit owner

    public Account editAccount(Long id, AccountRequest account, Authentication auth) throws IllegalAccessException {
        if(accessCheck(id, auth)) {
            Account accountToUpdate = showAccount(id);
            accountToUpdate.setId(id);
            accountToUpdate.setUpdatedAt(LocalDateTime.now());
            accountToUpdate.setUpdatedBy(auth.getName());
            accountToUpdate.setFirstName(account.getFirstName());
            accountToUpdate.setLastName(account.getLastName());
            accountToUpdate.setPhoneNumber(account.getPhoneNumber());
            accountToUpdate.setEmail(account.getEmail());
            return repository.save(accountToUpdate);
        } else {
            throw new IllegalAccessException("Access denied");
        }
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public Boolean freezeAccount(Long id) {
        Account account = showAccount(id);
        account.setIsActive(false);
        repository.save(account);
        return Boolean.TRUE;
    }

    public Boolean deleteAccount(Long id, Authentication auth) throws IllegalAccessException {
        if(accessCheck(id, auth)) {
            repository.delete(showAccount(id));
            return Boolean.TRUE;
        } else {
            throw new IllegalAccessException("Access denied");
        }
    }

    public boolean checkInfo(String owner) {
        return repository.findAccountByOwner(owner).getIsActive();
    }

    private boolean accessCheck(Long id, Authentication auth) {
        if(auth.getAuthorities().toString().contains("ROLE_ADMIN")) {
            return true;
        } else {
            return showAccount(id).getOwner().equals(auth.getName());
        }
    }

    public Double checkBalance(String owner) {
        return repository.findAccountByOwner(owner).getBalance();
    }

    public Long getAccountId(String owner) {
        return repository.findAccountByOwner(owner).getId();
    }

    @RabbitListener(queues = "account_payment")
    public void changeBalance(Map<String, String> info) {
        Long accountId = Long.valueOf(info.get("ownerId"));
        Account account = showAccount(accountId);
        Double value = Double.valueOf(info.get("value"));
        String action = info.get("account_action");
        if(action.equals("income")) {
            account.setBalance(account.getBalance() + value);
        } else {
            account.setBalance(account.getBalance() - value);
        }
        repository.save(account);
    }
}