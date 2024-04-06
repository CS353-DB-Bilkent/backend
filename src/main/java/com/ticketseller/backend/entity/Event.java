package com.ticketseller.backend.entity;

import com.ticketseller.backend.enums.EventStatus;
import com.ticketseller.backend.enums.EventType;
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
public class Event extends BaseEntity {

    private Long eventId;

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
    private EventType eventType;

    @NotNull
    private EventStatus eventStatus;

    @NotNull
    private Long organizerId;

    @NotNull
    private Long venueId;

    // It should be used after a join, whenever we need venue info alongside the event info
    // Implement venue, and uncomment this.
    // private Venue venue;





}

