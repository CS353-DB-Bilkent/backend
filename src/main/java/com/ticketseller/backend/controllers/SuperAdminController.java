package com.ticketseller.backend.controllers;

import com.ticketseller.backend.annotations.RequiredRole;
import com.ticketseller.backend.dto.request.auth.RegisterRequest;
import com.ticketseller.backend.dto.request.superadmin.CreateAdminRequest;
import com.ticketseller.backend.dto.response.ApiResponse;
import com.ticketseller.backend.dto.response.auth.RegisterResponse;
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
    public ResponseEntity<ApiResponse<?>> addUserToSemester(@Valid @RequestBody RegisterRequest registerRequest) {
        superAdminService.createAdmin(registerRequest.getEmail(), registerRequest.getPassword(),
                Role.ADMIN, registerRequest.getName(), registerRequest.getPhone(), registerRequest.getIBAN(),
                registerRequest.getCompanyName(), registerRequest.getBirthDate(), registerRequest.getSalary());

        return ResponseEntity.ok(
                ApiResponse.<Void>builder()
                        .operationResultData(null)
                        .build());
    }
}
