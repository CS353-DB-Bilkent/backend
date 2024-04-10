package com.ticketseller.backend.dto.request.user;

import jakarta.validation.constraints.Email;
import lombok.*;

import java.time.LocalDateTime;

@Builder
@Getter
@Setter
@ToString
@AllArgsConstructor
@RequiredArgsConstructor
public class UpdateInfoRequest {
    private String name;
    @Email
    private String email;
    private String phone;
}
