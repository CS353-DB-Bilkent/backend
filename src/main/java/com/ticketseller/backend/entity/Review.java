package com.ticketseller.backend.entity;

import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Review {
    private int rating;

    private String description;

    private LocalDate reviewDate;

    private Long userId;
}
