package com.ticketseller.backend.entity;

import com.ticketseller.backend.enums.Role;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;


@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class User extends BaseEntity {
    private Long userId;

    @NotNull
    private String name;

    @NotNull @Email
    private String email;

    @NotNull
    private String password;

    private Role role;

    @NotNull
    private String phone;

    @NotNull
    private LocalDateTime registeredDate;

    @NotNull
    private LocalDateTime birthDate;

    private String IBAN;

    private String companyName;

    private Double salary;
}

