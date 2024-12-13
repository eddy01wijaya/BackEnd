package com.eddywijaya.recruitmentbcaf.repo;

import com.eddywijaya.recruitmentbcaf.model.Account;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface AccountRepo extends JpaRepository<Account,Long> {
    Optional<Account> findTopByOrderByIdDesc();

    Page<Account> findByEmailContainingIgnoreCase(Pageable pageable, String value);
    Page<Account> findByFullNameContainingIgnoreCase(Pageable pageable, String value);
    Page<Account> findByUsernameContainingIgnoreCase(Pageable pageable, String value);

    List<Account> findByEmailContainingIgnoreCase(String value);
    List<Account> findByFullNameContainingIgnoreCase(String value);
    List<Account> findByUsernameContainingIgnoreCase(String value);

    Optional<Account> findByUsername(String value);

}
