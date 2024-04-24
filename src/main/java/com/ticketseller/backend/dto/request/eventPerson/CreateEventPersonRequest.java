package com.ticketseller.backend.dto.request.eventPerson;

import jakarta.validation.constraints.NotNull;
import lombok.*;

@Builder
@Getter
@Setter
@ToString
@AllArgsConstructor
@RequiredArgsConstructor

public class CreateEventPersonRequest {
    @NotNull
    private String eventPersonName;
}