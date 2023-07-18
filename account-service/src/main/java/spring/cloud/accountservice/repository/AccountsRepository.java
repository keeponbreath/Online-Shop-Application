package spring.cloud.accountservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import spring.cloud.accountservice.entity.Account;

public interface AccountsRepository extends JpaRepository<Account, Long> {
    Account findAccountByOwner(String owner);
}
