package com.ticketseller.backend.entity;
import java.time.LocalDateTime;

import com.ticketseller.backend.enums.TicketStatus;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Ticket {
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
