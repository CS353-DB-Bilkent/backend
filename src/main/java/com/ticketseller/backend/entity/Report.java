package com.ticketseller.backend.entity;

import com.ticketseller.backend.enums.EventStatus;
import com.ticketseller.backend.enums.EventType;
import com.ticketseller.backend.enums.Role;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDateTime;


@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Report {
    @NotNull
    private LocalDateTime reportDate;
    @NotNull
    private int totalSales;
    @NotNull
    private double totalRevenue;
}
