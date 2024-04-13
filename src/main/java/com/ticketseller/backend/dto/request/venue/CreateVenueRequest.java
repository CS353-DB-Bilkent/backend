package com.ticketseller.backend.dto.request.venue;
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
public class CreateVenueRequest {
    @NotNull
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