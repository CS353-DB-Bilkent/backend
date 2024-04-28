package com.ticketseller.backend.entity;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor

public class EventPerson {
    private Long eventPersonId;

    private String eventPersonName;
}
