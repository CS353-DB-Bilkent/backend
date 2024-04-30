package com.ticketseller.backend.entity;

import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Review {
    private Integer rating;

    private String description;

    private LocalDateTime reviewDate;

    private Long eventId;

    private Long userId;

    private String userInitials;
}
