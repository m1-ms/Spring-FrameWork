package com.example.businesslogic.employeeleave.repository;

import com.example.businesslogic.employeeleave.entity.LeaveRequest;
import com.example.businesslogic.employeeleave.enums.LeaveStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.Collection;

public interface LeaveRequestRepository extends JpaRepository<LeaveRequest, Long> {

    @Query("SELECT COUNT(l) FROM LeaveRequest l " +
            "WHERE l.employee.id = :employeeId AND l.status IN :statuses " +
            "AND l.startDate <= :newEnd AND l.endDate >= :newStart")
    long countOverlapping(@Param("employeeId") Long employeeId,
                          @Param("statuses") Collection<LeaveStatus> statuses,
                          @Param("newStart") LocalDate newStart,
                          @Param("newEnd") LocalDate newEnd);

    // Total leave days held by the employee inside one year
    @Query("SELECT COALESCE(SUM(l.leaveDays), 0L) FROM LeaveRequest l " +
            "WHERE l.employee.id = :employeeId AND l.status IN :statuses " +
            "AND l.startDate >= :yearStart AND l.startDate <= :yearEnd")
    long sumLeaveDaysBetween(@Param("employeeId") Long employeeId,
                             @Param("statuses") Collection<LeaveStatus> statuses,
                             @Param("yearStart") LocalDate yearStart,
                             @Param("yearEnd") LocalDate yearEnd);
}
