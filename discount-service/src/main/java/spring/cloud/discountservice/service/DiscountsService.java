package spring.cloud.discountservice.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import spring.cloud.discountservice.entity.Discount;
import spring.cloud.discountservice.repository.DiscountsRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
public class DiscountsService {
    private final DiscountsRepository repository;

    @Autowired
    public DiscountsService(DiscountsRepository repository) {
        this.repository = repository;
    }

    public List<Discount> getAllDiscounts() {
        return repository.findAll();
    }

    public Discount getDiscountById(Long id) {
        Optional<Discount> discountOptional = repository.findById(id);
        return discountOptional.orElseThrow(() ->
                new NoSuchElementException("Discount with id " + id + " not found"));
    }

    public Boolean deleteDiscount(Long id) {
        repository.deleteById(id);
        return Boolean.TRUE;
    }

    public Discount createDiscount(LocalDateTime exp, Short value) {
        Discount discount = new Discount();
        discount.setValue(value);
        discount.setExpiration(exp);
        return repository.save(discount);
    }

    public Boolean editDiscount(Long id, LocalDateTime exp, Short value) {
        Discount discount = getDiscountById(id);
        discount.setExpiration(exp);
        discount.setValue(value);
        repository.save(discount);
        return Boolean.TRUE;
    }

    public boolean checkInfo(Long id) {
        return getDiscountById(id).getExpiration().isAfter(LocalDateTime.now());
    }
}