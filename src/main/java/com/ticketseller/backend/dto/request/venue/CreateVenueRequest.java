package com.ticketseller.backend.dto.request.venue;

import jakarta.validation.constraints.NotNull;
import lombok.*;

@Builder
@Getter
@Setter
@ToString
@AllArgsConstructor
@RequiredArgsConstructor
public class CreateVenueRequest {
    @NotNull
    private String venueName;
    @NotNull
    private String venueAddress;
    @NotNull
    private String venueCity;
    @NotNull
    private Long venueCapacity;
}
