package com.ticketseller.backend.dto.request.wallet;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDateTime;

@Builder
@Getter
@Setter
@ToString
@AllArgsConstructor
@RequiredArgsConstructor
public class AddTransactionRequest {

    @NotNull
    private Long transactionAmount;

    @NotBlank
    private String transactionType;

}
