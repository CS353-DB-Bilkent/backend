package com.ticketseller.backend.entity;

import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor

public class Venue {
    private Long venueId;

    private String venueName;

    private String venueAddress;

    private String venueCity;

    private Long venueCapacity;
}
