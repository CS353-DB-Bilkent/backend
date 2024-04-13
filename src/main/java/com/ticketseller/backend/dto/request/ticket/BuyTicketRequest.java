package com.ticketseller.backend.dto.request.ticket;

import com.ticketseller.backend.enums.TicketStatus;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDateTime;

@Builder
@Getter
@Setter
@ToString
@AllArgsConstructor
@RequiredArgsConstructor
public class BuyTicketRequest {
    @NotNull
    private Long ticketId;

    @NotNull
    private Long userId;

    @NotNull
    private Long eventId;

    @NotNull
    private LocalDateTime purchaseDate;

    @NotNull
    private double price;

    @NotNull
    private TicketStatus ticketStatus;

    @NotNull
    private String qrCode;

    @NotNull
    private boolean buyerVisible;
}