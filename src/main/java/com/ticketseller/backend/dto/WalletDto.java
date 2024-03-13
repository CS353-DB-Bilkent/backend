package com.ticketseller.backend.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;

@Builder
@Getter
@Setter
@ToString
public class WalletDto {
    private Long walletId;

    @NotNull
    private Long balance;

    @NotNull
    private Long userId;

    // May be null
    private UserDto userDto;
}
