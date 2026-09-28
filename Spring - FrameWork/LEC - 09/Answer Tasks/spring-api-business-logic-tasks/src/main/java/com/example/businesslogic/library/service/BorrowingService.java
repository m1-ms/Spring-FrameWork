package com.example.businesslogic.library.service;

import com.example.businesslogic.common.exception.BusinessException;
import com.example.businesslogic.common.exception.ResourceNotFoundException;
import com.example.businesslogic.library.dto.BorrowRequest;
import com.example.businesslogic.library.dto.BorrowingResponse;
import com.example.businesslogic.library.entity.Book;
import com.example.businesslogic.library.entity.Borrowing;
import com.example.businesslogic.library.entity.LibraryMember;
import com.example.businesslogic.library.enums.BorrowingStatus;
import com.example.businesslogic.library.repository.BookRepository;
import com.example.businesslogic.library.repository.BorrowingRepository;
import com.example.businesslogic.library.repository.LibraryMemberRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

@Service
public class BorrowingService {

    private static final int MAX_ACTIVE_BORROWINGS = 3;
    private static final BigDecimal FINE_PER_DAY = new BigDecimal("5");

    private final BookRepository bookRepository;
    private final LibraryMemberRepository memberRepository;
    private final BorrowingRepository borrowingRepository;

    public BorrowingService(BookRepository bookRepository,
                            LibraryMemberRepository memberRepository,
                            BorrowingRepository borrowingRepository) {
        this.bookRepository = bookRepository;
        this.memberRepository = memberRepository;
        this.borrowingRepository = borrowingRepository;
    }

    @Transactional
    public BorrowingResponse borrowBook(BorrowRequest request) {

        LibraryMember member = memberRepository.findById(request.getMemberId())
                .orElseThrow(() -> new ResourceNotFoundException("Member not found"));
        Book book = bookRepository.findById(request.getBookId())
                .orElseThrow(() -> new ResourceNotFoundException("Book not found"));

        // Rule 1: the book must have an available copy
        if (book.getAvailableCopies() <= 0) {
            throw new BusinessException("No copies available for this book", HttpStatus.CONFLICT);
        }

        // Rule 2: a member can borrow a maximum of 3 books at the same time
        long activeCount = borrowingRepository
                .countByMemberIdAndStatus(member.getId(), BorrowingStatus.BORROWED);
        if (activeCount >= MAX_ACTIVE_BORROWINGS) {
            throw new BusinessException("Member cannot borrow more than "
                    + MAX_ACTIVE_BORROWINGS + " books", HttpStatus.CONFLICT);
        }

        // Rule 3: a member with an overdue book cannot borrow another one
        LocalDate today = LocalDate.now();
        boolean hasOverdue = borrowingRepository.existsByMemberIdAndStatusAndDueDateBefore(
                member.getId(), BorrowingStatus.BORROWED, today);
        if (hasOverdue) {
            throw new BusinessException(
                    "Member has overdue books and cannot borrow another book", HttpStatus.CONFLICT);
        }

        // Rule 4: loan period depends on the membership type (14 or 30 days)
        Borrowing borrowing = new Borrowing();
        borrowing.setMember(member);
        borrowing.setBook(book);
        borrowing.setBorrowDate(today);
        borrowing.setDueDate(today.plusDays(member.getMembershipType().getLoanDays()));
        borrowing.setFineAmount(BigDecimal.ZERO);
        borrowing.setStatus(BorrowingStatus.BORROWED);

        book.setAvailableCopies(book.getAvailableCopies() - 1);
        bookRepository.save(book);

        Borrowing saved = borrowingRepository.save(borrowing);
        return toResponse(saved, "Book borrowed successfully");
    }

    @Transactional
    public BorrowingResponse returnBook(Long borrowingId) {

        Borrowing borrowing = borrowingRepository.findById(borrowingId)
                .orElseThrow(() -> new ResourceNotFoundException("Borrowing not found"));

        if (borrowing.getStatus() == BorrowingStatus.RETURNED) {
            throw new BusinessException("Book was already returned", HttpStatus.CONFLICT);
        }

        LocalDate today = LocalDate.now();
        long overdueDays = ChronoUnit.DAYS.between(borrowing.getDueDate(), today);

        String message;
        if (overdueDays > 0) {
            // Rule 5: late return generates a fine based on the overdue days
            BigDecimal fine = FINE_PER_DAY.multiply(BigDecimal.valueOf(overdueDays));
            borrowing.setFineAmount(fine);
            message = "Book returned late by " + overdueDays + " day(s). Fine: " + fine;
        } else {
            borrowing.setFineAmount(BigDecimal.ZERO);
            message = "Book returned on time";
        }

        borrowing.setReturnDate(today);
        borrowing.setStatus(BorrowingStatus.RETURNED);

        Book book = borrowing.getBook();
        book.setAvailableCopies(book.getAvailableCopies() + 1);
        bookRepository.save(book);

        Borrowing saved = borrowingRepository.save(borrowing);
        return toResponse(saved, message);
    }

    private BorrowingResponse toResponse(Borrowing borrowing, String message) {
        BorrowingResponse response = new BorrowingResponse();
        response.setBorrowingId(borrowing.getId());
        response.setMemberId(borrowing.getMember().getId());
        response.setBookId(borrowing.getBook().getId());
        response.setBookTitle(borrowing.getBook().getTitle());
        response.setBorrowDate(borrowing.getBorrowDate());
        response.setDueDate(borrowing.getDueDate());
        response.setReturnDate(borrowing.getReturnDate());
        response.setFineAmount(borrowing.getFineAmount());
        response.setStatus(borrowing.getStatus());
        response.setMessage(message);
        return response;
    }
}
