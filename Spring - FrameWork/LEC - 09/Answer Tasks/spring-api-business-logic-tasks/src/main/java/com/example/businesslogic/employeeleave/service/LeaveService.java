package com.example.businesslogic.employeeleave.service;

import com.example.businesslogic.common.exception.BusinessException;
import com.example.businesslogic.common.exception.ResourceNotFoundException;
import com.example.businesslogic.employeeleave.dto.CreateLeaveRequest;
import com.example.businesslogic.employeeleave.dto.LeaveResponse;
import com.example.businesslogic.employeeleave.entity.Employee;
import com.example.businesslogic.employeeleave.entity.LeaveRequest;
import com.example.businesslogic.employeeleave.enums.LeaveStatus;
import com.example.businesslogic.employeeleave.repository.EmployeeRepository;
import com.example.businesslogic.employeeleave.repository.LeaveRequestRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.List;
import java.util.Set;

@Service
public class LeaveService {

    // Rule 3: weekend days are not counted as leave days
    private static final Set<DayOfWeek> WEEKEND_DAYS = Set.of(DayOfWeek.FRIDAY, DayOfWeek.SATURDAY);

    private static final List<LeaveStatus> ACTIVE_STATUSES =
            List.of(LeaveStatus.PENDING, LeaveStatus.APPROVED);

    private final EmployeeRepository employeeRepository;
    private final LeaveRequestRepository leaveRequestRepository;

    public LeaveService(EmployeeRepository employeeRepository,
                        LeaveRequestRepository leaveRequestRepository) {
        this.employeeRepository = employeeRepository;
        this.leaveRequestRepository = leaveRequestRepository;
    }

    @Transactional
    public LeaveResponse submit(CreateLeaveRequest request) {

        Employee employee = employeeRepository.findById(request.getEmployeeId())
                .orElseThrow(() -> new ResourceNotFoundException("Employee not found"));

        LocalDate start = request.getStartDate();
        LocalDate end = request.getEndDate();

        // Rule 1: the start date cannot be before today
        if (start.isBefore(LocalDate.now())) {
            throw new BusinessException("Start date cannot be before today");
        }
        if (end.isBefore(start)) {
            throw new BusinessException("End date cannot be before start date");
        }
        if (start.getYear() != end.getYear()) {
            throw new BusinessException("Leave request must be inside a single year");
        }

        // Rule 3: weekends are not counted
        int leaveDays = countWorkingDays(start, end);
        if (leaveDays == 0) {
            throw new BusinessException("Leave request contains no working days");
        }

        // Rule 2: an employee cannot request overlapping leave
        long overlapping = leaveRequestRepository.countOverlapping(
                employee.getId(), ACTIVE_STATUSES, start, end);
        if (overlapping > 0) {
            throw new BusinessException(
                    "Employee already has a leave request in this period", HttpStatus.CONFLICT);
        }

        // Rule 4: the yearly limit depends on the employee type
        // Rule 6: a request exceeding the remaining balance is rejected
        int remaining = remainingBalance(employee, start.getYear());
        if (leaveDays > remaining) {
            throw new BusinessException("Leave balance is not enough. Remaining: "
                    + remaining + ", requested: " + leaveDays, HttpStatus.CONFLICT);
        }

        LeaveRequest leaveRequest = new LeaveRequest();
        leaveRequest.setEmployee(employee);
        leaveRequest.setStartDate(start);
        leaveRequest.setEndDate(end);
        leaveRequest.setLeaveDays(leaveDays);
        leaveRequest.setStatus(LeaveStatus.PENDING);

        LeaveRequest saved = leaveRequestRepository.save(leaveRequest);
        return toResponse(saved, "Leave request submitted and waiting for manager approval");
    }

    // Rule 5: manager approval changes the request status
    @Transactional
    public LeaveResponse approve(Long leaveRequestId) {
        LeaveRequest leaveRequest = findLeaveRequest(leaveRequestId);

        if (!leaveRequest.getStatus().canTransitionTo(LeaveStatus.APPROVED)) {
            throw new BusinessException(
                    "Only a pending request can be approved", HttpStatus.CONFLICT);
        }

        leaveRequest.setStatus(LeaveStatus.APPROVED);
        return toResponse(leaveRequestRepository.save(leaveRequest), "Leave request approved");
    }

    // Rule 7: a rejected request restores the balance (it is no longer active)
    @Transactional
    public LeaveResponse reject(Long leaveRequestId) {
        LeaveRequest leaveRequest = findLeaveRequest(leaveRequestId);

        if (!leaveRequest.getStatus().canTransitionTo(LeaveStatus.REJECTED)) {
            throw new BusinessException(
                    "Only a pending request can be rejected", HttpStatus.CONFLICT);
        }

        leaveRequest.setStatus(LeaveStatus.REJECTED);
        return toResponse(leaveRequestRepository.save(leaveRequest),
                "Leave request rejected. The days were returned to the balance");
    }

    // Rule 7: a canceled request restores the balance (it is no longer active)
    @Transactional
    public LeaveResponse cancel(Long leaveRequestId) {
        LeaveRequest leaveRequest = findLeaveRequest(leaveRequestId);

        if (!leaveRequest.getStatus().canTransitionTo(LeaveStatus.CANCELED)) {
            throw new BusinessException(
                    "Only a pending or approved request can be canceled", HttpStatus.CONFLICT);
        }

        leaveRequest.setStatus(LeaveStatus.CANCELED);
        return toResponse(leaveRequestRepository.save(leaveRequest),
                "Leave request canceled. The days were returned to the balance");
    }

    // Yearly limit of the employee type minus the days held by active requests
    private int remainingBalance(Employee employee, int year) {
        LocalDate yearStart = LocalDate.of(year, 1, 1);
        LocalDate yearEnd = LocalDate.of(year, 12, 31);
        long used = leaveRequestRepository.sumLeaveDaysBetween(
                employee.getId(), ACTIVE_STATUSES, yearStart, yearEnd);
        return employee.getType().getYearlyLeaveDays() - (int) used;
    }

    private int countWorkingDays(LocalDate start, LocalDate end) {
        int days = 0;
        for (LocalDate date = start; !date.isAfter(end); date = date.plusDays(1)) {
            if (!WEEKEND_DAYS.contains(date.getDayOfWeek())) {
                days++;
            }
        }
        return days;
    }

    private LeaveRequest findLeaveRequest(Long leaveRequestId) {
        return leaveRequestRepository.findById(leaveRequestId)
                .orElseThrow(() -> new ResourceNotFoundException("Leave request not found"));
    }

    private LeaveResponse toResponse(LeaveRequest leaveRequest, String message) {
        Employee employee = leaveRequest.getEmployee();

        LeaveResponse response = new LeaveResponse();
        response.setLeaveRequestId(leaveRequest.getId());
        response.setEmployeeId(employee.getId());
        response.setEmployeeName(employee.getName());
        response.setStartDate(leaveRequest.getStartDate());
        response.setEndDate(leaveRequest.getEndDate());
        response.setLeaveDays(leaveRequest.getLeaveDays());
        response.setStatus(leaveRequest.getStatus());
        response.setRemainingBalance(remainingBalance(employee, leaveRequest.getStartDate().getYear()));
        response.setMessage(message);
        return response;
    }
}
