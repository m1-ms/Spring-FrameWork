package org.bank.managementsystem.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.bank.managementsystem.dto.CreatePermissionRequestDto;
import org.bank.managementsystem.dto.CreateRoleRequestDto;
import org.bank.managementsystem.dto.PermissionDto;
import org.bank.managementsystem.dto.RoleDto;
import org.bank.managementsystem.dto.UpdateRolePermissionsRequestDto;
import org.bank.managementsystem.service.AdminCatalogService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminCatalogController {

    private final AdminCatalogService adminCatalogService;

    @GetMapping("/roles")
    public ResponseEntity<List<RoleDto>> roles() {
        return ResponseEntity.ok(adminCatalogService.listRoles());
    }

    @GetMapping("/permissions")
    public ResponseEntity<List<PermissionDto>> permissions() {
        return ResponseEntity.ok(adminCatalogService.listPermissions());
    }

    @PostMapping("/permissions")
    public ResponseEntity<PermissionDto> createPermission(
            @Valid @RequestBody CreatePermissionRequestDto request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(adminCatalogService.createPermission(request));
    }

    @PostMapping("/roles")
    public ResponseEntity<RoleDto> createRole(@Valid @RequestBody CreateRoleRequestDto request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(adminCatalogService.createRole(request));
    }

    @PutMapping("/roles/{id}/permissions")
    public ResponseEntity<RoleDto> updateRolePermissions(
            @PathVariable Long id,
            @Valid @RequestBody UpdateRolePermissionsRequestDto request) {
        return ResponseEntity.ok(adminCatalogService.updateRolePermissions(id, request));
    }
}