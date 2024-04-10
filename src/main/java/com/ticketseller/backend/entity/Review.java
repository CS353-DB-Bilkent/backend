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
    private Long reviewId;

    private int rating;

    private String description;

    private LocalDate reviewDate;
}
