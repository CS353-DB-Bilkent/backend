package com.ticketseller.backend.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Builder
@Getter
@Setter
@ToString
public class CustomMessageContactDto {

    private String id;
    private String name;

}
