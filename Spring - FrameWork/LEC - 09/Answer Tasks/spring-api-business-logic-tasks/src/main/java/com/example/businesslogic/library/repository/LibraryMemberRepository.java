package com.example.businesslogic.library.repository;

import com.example.businesslogic.library.entity.LibraryMember;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LibraryMemberRepository extends JpaRepository<LibraryMember, Long> {
}
