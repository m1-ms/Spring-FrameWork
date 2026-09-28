package org.example.employee.email.repository;

import org.example.employee.email.entity.Employee;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EmployeeRepository extends JpaRepository<Employee, Long> {

    List<Employee> findByNameIn(List<String> names);
}