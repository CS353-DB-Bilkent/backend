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
    private Long ticketId;

    private Long userId;

    private Long eventId;

    private LocalDateTime purchaseDate;

    private double price;

    private TicketStatus ticketStatus;

    private String qrCode;

    private boolean buyerVisible;
}
