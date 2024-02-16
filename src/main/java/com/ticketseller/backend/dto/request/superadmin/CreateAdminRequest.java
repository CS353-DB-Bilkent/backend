package com.ticketseller.backend.dto.request.superadmin;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Builder
@Getter
@Setter
@ToString
@AllArgsConstructor
@RequiredArgsConstructor
public class CreateAdminRequest {
    @NotNull @NotBlank
    private String email;

    @NotNull @NotBlank
    private String name;
}
