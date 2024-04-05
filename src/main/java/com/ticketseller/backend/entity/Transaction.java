package com.ticketseller.backend.entity;

import com.ticketseller.backend.enums.TransactionType;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDateTime;


@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Transaction extends BaseEntity {
    private Long transactionId;

    @NotNull
    private Long eventId;

    @NotNull
    private Long userId;

    @NotNull
    private Long transactionAmount;

    @NotNull
    private TransactionType transactionType;

    private LocalDateTime transactionDate;
}

