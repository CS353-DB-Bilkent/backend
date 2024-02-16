package com.ticketseller.backend.dto.request.custommessage;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Builder
@Getter
@Setter
@ToString
@AllArgsConstructor
@RequiredArgsConstructor
public class CustomMessageRequest {

    @NotNull @NotBlank
    private String contactId;

    @NotNull
    @Size(min = 3, max = 100)
    private String title;

    @NotNull
    @Size(min = 3, max = 1000)
    private String message;

}
