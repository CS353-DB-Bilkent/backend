package com.ticketseller.backend.controllers;

import com.ticketseller.backend.annotations.RequiredRole;
import com.ticketseller.backend.dto.request.user.UpdateInfoRequest;
import com.ticketseller.backend.dto.response.ApiResponse;
import com.ticketseller.backend.entity.Event;
import com.ticketseller.backend.entity.User;
import com.ticketseller.backend.enums.Role;
import com.ticketseller.backend.services.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RequiredArgsConstructor
@RestController
@RequestMapping("/user")
public class UserController {

    private final UserService userService;

    @GetMapping("/me")
    public ResponseEntity<ApiResponse<User>> getMe(HttpServletRequest request) {
        User user = (User) request.getAttribute("user");

        return ResponseEntity.ok(
                ApiResponse.<User>builder()
                        .operationResultData(user)
                        .build()
        );
    }
    @PostMapping("/{userId}/updateInfo")
    @RequiredRole({Role.USER})
    public ResponseEntity<ApiResponse<User>> updateInfo(@Valid @RequestBody UpdateInfoRequest request, HttpServletRequest requesthttp) {
        String newName = request.getName();
        String newEmail = request.getEmail();
        String newPhone = request.getPhone();
        User user = (User) requesthttp.getAttribute("user");
        return userService.updateUser(user.getUserId(), newName, newEmail, newPhone) ? ResponseEntity.ok(ApiResponse.<User>builder()
                .operationResultData(user)
                .build()): ResponseEntity.internalServerError().build();
    }
}
