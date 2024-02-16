package com.ticketseller.backend.entity;

import com.ticketseller.backend.dto.UserDto;
import com.ticketseller.backend.enums.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;



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

    public UserDto toUserDto() {

        return UserDto.builder()
                .email(email)
                .userId(userId)
                .role(role.name())
                .name(name)
                .build();
    }
}

