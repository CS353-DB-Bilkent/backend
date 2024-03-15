package com.ticketseller.backend.dto;


import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.UUID;

@Builder
@Getter
@Setter
@ToString
public class EventDTO {
    @NotNull
    @NotBlank
    private UUID eventID;
    @NotNull
    @NotBlank
    private String name;
    @NotNull
    @NotBlank
    private String start_date;
    @NotNull
    @NotBlank
    private String end_date;
    @NotNull
    @NotBlank
    private String details;
    @NotNull
    @NotBlank
    private String min_age_allowed;
    @NotNull
    @NotBlank
    private String door_opening_hours;
    @NotNull
    @NotBlank
    private String status;
    @NotNull
    @NotBlank
    private String event_person_id;
    @NotNull
    @NotBlank
    private String brand_id;
    @NotNull
    @NotBlank
    private String event_type_id;
    @NotNull
    @NotBlank
    private String venue_id;
}
