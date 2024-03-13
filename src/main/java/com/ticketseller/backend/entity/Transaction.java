package com.ticketseller.backend.entity;

import com.ticketseller.backend.dto.WalletDto;
import com.ticketseller.backend.enums.TransactionType;
import jakarta.validation.constraints.NotNull;
import lombok.*;


@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Transaction extends BaseEntity {
    private Long transactionId;

    @NotNull
    private Long walletId;

    @NotNull
    private Long transactionAmount;

    @NotNull
    private TransactionType transactionType;

    @NotNull
    private Long transactionDate;

}

