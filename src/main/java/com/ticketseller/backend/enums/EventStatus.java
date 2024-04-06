package com.ticketseller.backend.enums;

public enum EventStatus {

    CANCELLED,
    POSTPONED, // will probably not implement these
    RESCHEDULED, // will probably not implement these
    ACTIVE,
    PASSED,
    WAITING_APPROVAL,
    UNAPPROVED,
    UNRECOGNIZED;

    public static EventStatus getEventStatusFromStringValue(String value) {
        EventStatus eventStatus = EventStatus.UNRECOGNIZED;

        for (EventStatus r : values()) {
            if (r.name().equalsIgnoreCase(value)) {
                eventStatus = r;
                break;
            }
        }

        return eventStatus;
    }
}
