package com.ticketseller.backend.controllers;

import com.ticketseller.backend.annotations.RequiredRole;
import com.ticketseller.backend.dto.request.superadmin.CreateAdminRequest;
import com.ticketseller.backend.dto.response.ApiResponse;
import com.ticketseller.backend.enums.Role;
import com.ticketseller.backend.services.SuperAdminService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/super-admin")
@RequiredRole({ Role.SUPER_ADMIN })
public class SuperAdminController {

    private final SuperAdminService superAdminService;

    @PostMapping("/create-admin")
    public ResponseEntity<ApiResponse<?>> addUserToSemester(@Valid @RequestBody CreateAdminRequest createAdminRequest) {
        // superAdminService.createAdmin(createAdminRequest.getBilkentId(), createAdminRequest.getEmail(), createAdminRequest.getName(), createAdminRequest.getDepartment());

        return ResponseEntity.ok(
                ApiResponse.<Void>builder()
                        .operationResultData(null)
                        .build());
    }
}
