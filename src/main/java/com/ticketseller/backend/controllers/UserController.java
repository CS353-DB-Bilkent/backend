package com.ticketseller.backend.controllers;

import com.ticketseller.backend.annotations.RequiredRole;
import com.ticketseller.backend.dto.UserDto;
import com.ticketseller.backend.dto.response.ApiResponse;
import com.ticketseller.backend.entity.User;
import com.ticketseller.backend.enums.Role;
import com.ticketseller.backend.services.UserService;
import com.ticketseller.backend.utils.FileUtils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RequiredArgsConstructor
@RestController
@RequestMapping("/user")
public class UserController {

    private final UserService userService;

    @GetMapping("/me")
    public ResponseEntity<ApiResponse<UserDto>> getMe(HttpServletRequest request) {
        User user = (User) request.getAttribute("user");

        return ResponseEntity.ok(
                ApiResponse.<UserDto>builder()
                        .operationResultData(user.toUserDto())
                        .build()
        );
    }

}
