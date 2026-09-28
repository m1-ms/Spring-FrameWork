package com.example.businesslogic.library.repository;

import com.example.businesslogic.library.entity.Borrowing;
import com.example.businesslogic.library.enums.BorrowingStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;

public interface BorrowingRepository extends JpaRepository<Borrowing, Long> {

    long countByMemberIdAndStatus(Long memberId, BorrowingStatus status);

    boolean existsByMemberIdAndStatusAndDueDateBefore(Long memberId, BorrowingStatus status, LocalDate date);
}
