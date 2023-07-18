package spring.cloud.discountservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import spring.cloud.discountservice.entity.Discount;

public interface DiscountsRepository extends JpaRepository<Discount, Long> {
}
