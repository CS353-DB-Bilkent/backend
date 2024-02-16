package com.ticketseller.backend.entity;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class BaseEntity {
    private Date createdDate;

    private Date updatedDate;

    private Long version;
}
