package com.ticketseller.backend.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

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

}
