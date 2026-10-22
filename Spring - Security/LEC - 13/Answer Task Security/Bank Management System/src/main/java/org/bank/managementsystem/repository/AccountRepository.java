package org.bank.managementsystem.repository;

import jakarta.persistence.LockModeType;
import org.bank.managementsystem.entity.Account;
import org.bank.managementsystem.enums.AccountStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface AccountRepository extends JpaRepository<Account, Long> {

    boolean existsByAccountNumber(String accountNumber);

    List<Account> findByCustomerId(Long customerId);

    long countByStatus(AccountStatus status);

    @Query("select a.currency as currency, sum(a.balance) as total from Account a group by a.currency")
    List<CurrencyTotalView> sumBalanceByCurrency();

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select a from Account a where a.id = :id")
    Optional<Account> findByIdForUpdate(@Param("id") Long id);
}