package com.ticketseller.backend.dto.request.transaction;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

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
