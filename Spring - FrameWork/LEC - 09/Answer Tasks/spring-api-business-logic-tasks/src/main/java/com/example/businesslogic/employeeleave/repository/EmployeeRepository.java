package com.example.businesslogic.employeeleave.repository;

import com.example.businesslogic.employeeleave.entity.Employee;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EmployeeRepository extends JpaRepository<Employee, Long> {
}