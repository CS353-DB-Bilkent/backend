package com.ticketseller.backend.dto.request.event;

import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDateTime;

@Builder
@Getter
@Setter
@ToString
@AllArgsConstructor
@RequiredArgsConstructor
public class CreateEventRequest {
    @NotNull
    private String name;

    @NotNull
    private String details;

    @NotNull
    private LocalDateTime startDate;

    @NotNull
    private LocalDateTime endDate;

    @NotNull
    private Double ticketPrice;

    @NotNull
    private Integer numberOfTickets;

    @NotNull
    private Integer minAgeAllowed;

    @NotNull
    private String eventType;

    @NotNull
    private Long venueId;

    @NotNull
    private String brandName;

    @NotNull
    private String eventPersonName;
}