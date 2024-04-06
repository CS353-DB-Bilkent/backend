package com.ticketseller.backend.dto.request.event;

import lombok.*;

import java.time.LocalDateTime;

@Builder
@Getter
@Setter
@ToString
@AllArgsConstructor
@RequiredArgsConstructor
public class FilterEventsRequest {

    private String searchTerm;
    private String artistName;
    private String brandName;
    private String venueName;
    private String location;
    private String type;
    private Integer minAgeAllowed;
    private LocalDateTime startDate;
    private String orderBy;
    private String orderDirection;

}
