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
    @NotNull
    private String venueName;
    @NotNull
    private String venueAddress;
    @NotNull
    private String venueCity;
    @NotNull
    private Long venueCapacity;
}
