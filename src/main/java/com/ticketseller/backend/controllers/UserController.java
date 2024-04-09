package com.ticketseller.backend.controllers;

import com.ticketseller.backend.annotations.RequiredRole;
import com.ticketseller.backend.dto.response.ApiResponse;
import com.ticketseller.backend.entity.Event;
import com.ticketseller.backend.entity.User;
import com.ticketseller.backend.enums.Role;
import com.ticketseller.backend.services.UserService;
import jakarta.servlet.http.HttpServletRequest;
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
    @GetMapping("/updateInfo/{userId}")
    @RequiredRole({Role.USER})
    public ResponseEntity<ApiResponse<User>> updateInfo(@PathVariable Long userId, @RequestBody User request) {
        String newName = request.getName();
        String newEmail = request.getEmail();
        String newPhone = request.getPhone();
        User user = userService.getUserById(userId);
        return userService.updateUser(userId, newName, newEmail, newPhone) ? ResponseEntity.ok(ApiResponse.<User>builder()
                .operationResultData(user)
                .build()): ResponseEntity.internalServerError().build();
    }
}
