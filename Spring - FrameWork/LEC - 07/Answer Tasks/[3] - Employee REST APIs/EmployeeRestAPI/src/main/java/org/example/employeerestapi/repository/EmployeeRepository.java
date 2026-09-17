package org.example.employeerestapi.repository;

import org.example.employeerestapi.entity.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Long> {

    List<Employee> findByIdIn(List<Long> ids);

    // 1) Function Name
    List<Employee> findByNameStartingWithIgnoreCase(String name);

    // 2) Native Query
    @Query(value = "SELECT * FROM EMPLOYEE WHERE UPPER(NAME) LIKE UPPER(:name)", nativeQuery = true)
    List<Employee> searchByNameNative(@Param("name") String name);

    // 3) Non-Native Query
    @Query("SELECT e FROM Employee e WHERE UPPER(e.name) LIKE UPPER(:name)")
    List<Employee> searchByNameJPQL(@Param("name") String name);
}