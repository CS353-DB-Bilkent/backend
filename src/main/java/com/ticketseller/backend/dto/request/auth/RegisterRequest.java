package com.ticketseller.backend.dto.request.auth;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDateTime;

@Builder
@Getter
@Setter
@ToString
@AllArgsConstructor
@RequiredArgsConstructor
public class RegisterRequest {

    @NotBlank @NotNull @Email
    private String email;

    @NotBlank @NotNull
    private String password;

    @NotBlank @NotNull
    private String name;

    @NotBlank @NotNull
    private String phone;

    @NotNull
    private LocalDateTime birthDate;

    private String IBAN;

    private String companyName;
}
