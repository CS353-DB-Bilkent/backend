package com.ticketseller.backend.dto.request.auth;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.validator.constraints.UUID;

@Builder
@Getter
@Setter
@ToString
@AllArgsConstructor
@RequiredArgsConstructor
public class ChangePasswordCodeRequest {

    @NotBlank
    @UUID
    private String code;

    @NotBlank
    @Size(min = 3, max = 30)
    private String newPassword;
}
