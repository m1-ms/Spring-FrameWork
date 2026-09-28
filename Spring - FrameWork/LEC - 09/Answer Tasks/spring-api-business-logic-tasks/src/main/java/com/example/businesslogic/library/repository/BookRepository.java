package com.example.businesslogic.library.repository;

import com.example.businesslogic.library.entity.Book;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookRepository extends JpaRepository<Book, Long> {
}
