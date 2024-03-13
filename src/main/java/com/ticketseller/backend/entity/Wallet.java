package com.ticketseller.backend.entity;

import com.ticketseller.backend.dto.UserDto;
import com.ticketseller.backend.dto.WalletDto;
import com.ticketseller.backend.enums.Role;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDateTime;


@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Wallet extends BaseEntity {
    private Long walletId;

    @NotNull
    private Long balance;

    @NotNull
    private Long userId;

    public WalletDto toWalletDto() {
        return WalletDto.builder()
                .walletId(walletId)
                .balance(balance)
                .userId(userId)
                .build();
    }
}

