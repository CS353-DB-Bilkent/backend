package com.ticketseller.backend.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@Builder
@Getter
@Setter
@ToString
public class UserDto {
    private Long userId;

    @NotNull @NotBlank @Email
    private String email;

    @NotNull @NotBlank
    private String name;

    @NotNull @NotBlank
    private String role;

    @NotNull @NotBlank
    private String phone;

    @NotNull
    private LocalDateTime registeredDate;

    @NotNull
    private LocalDateTime birthDate;

    // Can be null
    private String IBAN;

    // Can be null
    private String companyName;


}
