package com.ticketseller.backend.controllers;

import com.ticketseller.backend.dto.request.auth.*;
import com.ticketseller.backend.dto.response.ApiResponse;
import com.ticketseller.backend.dto.response.auth.LoginResponse;
import com.ticketseller.backend.dto.response.auth.RegisterResponse;
import com.ticketseller.backend.enums.Role;
import com.ticketseller.backend.services.AuthService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.ticketseller.backend.entity.User;


@RequiredArgsConstructor
@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<LoginResponse>> login(@Valid @RequestBody LoginRequest loginRequest) {
        LoginResponse loginResponse = authService.login(loginRequest.getEmail(), loginRequest.getPassword());

        return ResponseEntity.ok(
                ApiResponse.<LoginResponse>builder()
                        .operationResultData(loginResponse)
                        .build());

    }

    @PostMapping("/register/event-organizer")
    public ResponseEntity<ApiResponse<RegisterResponse>> registerEventOrganizer(@Valid @RequestBody RegisterRequest registerRequest) {
        RegisterResponse registerResponse = authService.register(registerRequest.getEmail(), registerRequest.getPassword(),
                Role.EVENT_ORGANIZER, registerRequest.getName(), registerRequest.getPhone(), registerRequest.getIBAN(),
                registerRequest.getCompanyName(), registerRequest.getBirthDate(), null);

        return ResponseEntity.ok(
                ApiResponse.<RegisterResponse>builder()
                        .operationResultData(registerResponse)
                        .build());
    }

    @PostMapping("/register/user")
    public ResponseEntity<ApiResponse<RegisterResponse>> registerNormalUser(@Valid @RequestBody RegisterRequest registerRequest) {
        RegisterResponse registerResponse = authService.register(registerRequest.getEmail(), registerRequest.getPassword(),
                Role.USER, registerRequest.getName(), registerRequest.getPhone(), null,
                null, registerRequest.getBirthDate(), null);

        return ResponseEntity.ok(
                ApiResponse.<RegisterResponse>builder()
                        .operationResultData(registerResponse)
                        .build());
    }

    @PostMapping("/change-password")
    public ResponseEntity<ApiResponse<?>> changePassword(HttpServletRequest request, @Valid @RequestBody ChangePasswordRequest changePasswordRequest) {
        User user = (User) request.getAttribute("user");

        authService.changePassword(user, changePasswordRequest.getOldPassword(), changePasswordRequest.getNewPassword());

        return ResponseEntity.ok(
                ApiResponse.builder()
                        .operationResultData(null)
                        .build()
        );
    }

//    @PostMapping("/logout")
//    public ResponseEntity<ApiResponse<?>> logout(HttpServletRequest request) {
//        String accessToken = (String) request.getAttribute("accessToken");
//        authService.logout(accessToken);
//
//        return ResponseEntity.ok(
//                ApiResponse.builder()
//                        .operationResultData(null)
//                        .build());
//
//    }
//

//
//    @PostMapping("/email-password-change-code")
//    public ResponseEntity<ApiResponse<?>> emailPasswordChange(@Valid @RequestBody EmailPasswordChangeRequest emailPasswordChangeRequest) {
//        authService.emailPasswordChange(emailPasswordChangeRequest.getEmail());
//
//        return ResponseEntity.ok(
//                ApiResponse.builder()
//                        .operationResultData(null)
//                        .build()
//        );
//    }
//
//    @PostMapping("/change-password-code")
//    public ResponseEntity<ApiResponse<?>> changePasswordWithCode(@Valid @RequestBody ChangePasswordCodeRequest changePasswordCodeRequest) {
//        authService.changePasswordWithCode(changePasswordCodeRequest.getCode(), changePasswordCodeRequest.getNewPassword());
//
//        return ResponseEntity.ok(
//                ApiResponse.builder()
//                        .operationResultData(null)
//                        .build()
//        );
//    }
}
