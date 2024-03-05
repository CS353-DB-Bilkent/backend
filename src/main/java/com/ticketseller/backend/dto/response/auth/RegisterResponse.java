package com.ticketseller.backend.dto.response.auth;

import lombok.*;

@Getter
@Setter
@ToString
@Builder
@AllArgsConstructor
public class RegisterResponse {
    private String accessToken;
}
