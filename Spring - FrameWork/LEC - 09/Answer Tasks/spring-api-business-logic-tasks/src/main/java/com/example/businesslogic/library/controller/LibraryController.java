package com.example.businesslogic.library.controller;

import com.example.businesslogic.library.dto.BorrowRequest;
import com.example.businesslogic.library.dto.BorrowingResponse;
import com.example.businesslogic.library.service.BorrowingService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/library")
public class LibraryController {

    private final BorrowingService borrowingService;

    public LibraryController(BorrowingService borrowingService) {
        this.borrowingService = borrowingService;
    }

    @PostMapping("/borrow")
    public ResponseEntity<BorrowingResponse> borrow(@Valid @RequestBody BorrowRequest request) {
        return ResponseEntity.ok(borrowingService.borrowBook(request));
    }

    @PostMapping("/return/{borrowingId}")
    public ResponseEntity<BorrowingResponse> returnBook(@PathVariable Long borrowingId) {
        return ResponseEntity.ok(borrowingService.returnBook(borrowingId));
    }
}
