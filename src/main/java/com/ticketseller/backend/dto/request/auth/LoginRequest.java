package com.ticketseller.backend.dto.request.auth;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Builder
@Getter
@Setter
@ToString
@AllArgsConstructor
@RequiredArgsConstructor
public class LoginRequest {
    @NotBlank
    private String email;

    @NotBlank
    private String password;
}
