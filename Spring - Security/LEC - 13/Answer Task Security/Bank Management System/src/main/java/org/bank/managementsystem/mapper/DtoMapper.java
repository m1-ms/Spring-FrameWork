package org.bank.managementsystem.mapper;

import org.bank.managementsystem.dto.AccountDto;
import org.bank.managementsystem.dto.CustomerDto;
import org.bank.managementsystem.dto.PermissionDto;
import org.bank.managementsystem.dto.RoleDto;
import org.bank.managementsystem.dto.TransferDto;
import org.bank.managementsystem.entity.Account;
import org.bank.managementsystem.entity.Customer;
import org.bank.managementsystem.entity.Permission;
import org.bank.managementsystem.entity.Role;
import org.bank.managementsystem.entity.Transfer;
import org.springframework.stereotype.Component;

import java.util.Set;
import java.util.stream.Collectors;

@Component
public class DtoMapper {

    public PermissionDto toPermissionDto(Permission permission) {
        return PermissionDto.builder()
                .id(permission.getId())
                .name(permission.getName())
                .build();
    }

    public RoleDto toRoleDto(Role role) {
        Set<PermissionDto> permissions = role.getPermissions().stream()
                .map(this::toPermissionDto)
                .collect(Collectors.toSet());

        return RoleDto.builder()
                .id(role.getId())
                .name(role.getName())
                .permissions(permissions)
                .build();
    }

    public CustomerDto toCustomerDto(Customer customer) {
        Set<String> roleNames = customer.getRoles().stream()
                .map(Role::getName)
                .collect(Collectors.toSet());

        return CustomerDto.builder()
                .id(customer.getId())
                .fullName(customer.getFullName())
                .email(customer.getEmail())
                .phone(customer.getPhone())
                .enabled(customer.isEnabled())
                .blocked(customer.isBlocked())
                .createdAt(customer.getCreatedAt())
                .roles(roleNames)
                .build();
    }

    public AccountDto toAccountDto(Account account) {
        return AccountDto.builder()
                .id(account.getId())
                .accountNumber(account.getAccountNumber())
                .balance(account.getBalance())
                .currency(account.getCurrency())
                .status(account.getStatus())
                .customerId(account.getCustomer().getId())
                .build();
    }

    public TransferDto toTransferDto(Transfer transfer) {
        return TransferDto.builder()
                .id(transfer.getId())
                .fromAccountId(transfer.getFromAccount().getId())
                .toAccountId(transfer.getToAccount().getId())
                .amount(transfer.getAmount())
                .status(transfer.getStatus())
                .createdAt(transfer.getCreatedAt())
                .createdById(transfer.getCreatedBy().getId())
                .build();
    }
}