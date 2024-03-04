package com.ticketseller.backend.entity;

import com.ticketseller.backend.dto.UserDto;
import com.ticketseller.backend.enums.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.Date;


@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class User extends BaseEntity {
    private Long userId;

    private String name;

    private String email;

    private String password;

    private Role role;

    private String phone;

    private LocalDateTime registeredDate;

    private LocalDateTime birthDate;

    private String IBAN;

    private String companyName;

    public UserDto toUserDto() {

        return UserDto.builder()
                .email(email)
                .userId(userId)
                .role(role.name())
                .name(name)
                .phone(phone)
                .registeredDate(registeredDate)
                .birthDate(birthDate)
                .IBAN(IBAN)
                .companyName(companyName)
                .build();
    }
}

