package org.bank.managementsystem.service;

import lombok.RequiredArgsConstructor;
import org.bank.managementsystem.dto.CreatePermissionRequestDto;
import org.bank.managementsystem.dto.CreateRoleRequestDto;
import org.bank.managementsystem.dto.PermissionDto;
import org.bank.managementsystem.dto.RoleDto;
import org.bank.managementsystem.dto.UpdateRolePermissionsRequestDto;
import org.bank.managementsystem.entity.Permission;
import org.bank.managementsystem.entity.Role;
import org.bank.managementsystem.mapper.DtoMapper;
import org.bank.managementsystem.repository.PermissionRepository;
import org.bank.managementsystem.repository.RoleRepository;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class AdminCatalogService {

    private static final Set<String> SYSTEM_ROLES = Set.of("ADMIN", "BANK_MANAGER", "CUSTOMER", "AUDITOR");

    private static final String RESERVED_PREFIX = "ROLE_";

    private final RoleRepository roleRepository;
    private final PermissionRepository permissionRepository;
    private final DtoMapper mapper;

    @PreAuthorize("hasRole('ADMIN')")
    @Transactional(readOnly = true)
    public List<RoleDto> listRoles() {
        return roleRepository.findAll().stream()
                .map(mapper::toRoleDto)
                .toList();
    }

    @PreAuthorize("hasRole('ADMIN')")
    @Transactional(readOnly = true)
    public List<PermissionDto> listPermissions() {
        return permissionRepository.findAll().stream()
                .map(mapper::toPermissionDto)
                .toList();
    }

    @PreAuthorize("hasRole('ADMIN')")
    @Transactional
    public PermissionDto createPermission(CreatePermissionRequestDto request) {
        String name = request.getName();
        rejectReservedPrefix(name);

        if (permissionRepository.findByName(name).isPresent()) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Permission already exists");
        }

        Permission permission = permissionRepository.save(Permission.builder().name(name).build());
        return mapper.toPermissionDto(permission);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @Transactional
    public RoleDto createRole(CreateRoleRequestDto request) {
        String name = request.getName();
        rejectReservedPrefix(name);

        if (roleRepository.findByName(name).isPresent()) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Role already exists");
        }

        Role role = Role.builder().name(name).build();
        role.setPermissions(resolvePermissions(request.getPermissions()));

        return mapper.toRoleDto(roleRepository.save(role));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @Transactional
    public RoleDto updateRolePermissions(Long id, UpdateRolePermissionsRequestDto request) {
        Role role = roleRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Role not found"));

        if (SYSTEM_ROLES.contains(role.getName())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "System roles cannot be modified");
        }

        role.getPermissions().clear();
        role.getPermissions().addAll(resolvePermissions(request.getPermissions()));

        return mapper.toRoleDto(roleRepository.save(role));
    }

    private void rejectReservedPrefix(String name) {
        if (name.startsWith(RESERVED_PREFIX)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Names starting with ROLE_ are reserved");
        }
    }

    private Set<Permission> resolvePermissions(Set<String> names) {
        Set<Permission> permissions = new HashSet<>();
        for (String raw : names) {
            String name = raw.trim().toUpperCase();
            Permission permission = permissionRepository.findByName(name)
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST,
                            "Unknown permission: " + name));
            permissions.add(permission);
        }
        return permissions;
    }
}