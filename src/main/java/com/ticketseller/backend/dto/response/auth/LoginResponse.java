package com.ticketseller.backend.dto.response.auth;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@Builder
@AllArgsConstructor
public class LoginResponse {
    private Long userId;
    private String accessToken;
}
