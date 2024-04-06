package com.ticketseller.backend.dto.request.event;

import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDateTime;

@Builder
@Getter
@Setter
@ToString
@AllArgsConstructor
@RequiredArgsConstructor
public class FilterEventstRequest {

    private String searchTerm;
    private String artistName;
    private String brandName;
    private String venueName;
    private String location;
    private String type;
    private Integer minAgeAllowed;
    private LocalDateTime startDate;

}
