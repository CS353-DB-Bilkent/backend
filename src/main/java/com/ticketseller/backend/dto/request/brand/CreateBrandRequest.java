package com.ticketseller.backend.dto.request.brand;

import jakarta.validation.constraints.NotNull;
import lombok.*;

@Builder
@Getter
@Setter
@ToString
@AllArgsConstructor
@RequiredArgsConstructor

public class CreateBrandRequest {
    @NotNull
    private String brandName;
}
