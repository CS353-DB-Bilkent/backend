package com.ticketseller.backend.enums;

public enum Role {
    EVENT_ORGANIZER,
    USER,
    ADMIN,
    SUPER_ADMIN,
    UNRECOGNIZED;

    public static Role getRoleFromStringValue(String value) {
        Role role = Role.UNRECOGNIZED;

        for (Role r : values()) {
            if (r.name().equalsIgnoreCase(value)) {
                role = r;
                break;
            }
        }

        return role;
    }
}
