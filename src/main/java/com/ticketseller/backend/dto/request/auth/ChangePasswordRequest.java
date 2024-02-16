package com.ticketseller.backend.dto.request.auth;


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
public class ChangePasswordRequest {

    private String oldPassword;

    private String newPassword;
}
