package com.ticketseller.backend.enums;

public enum EventType {
    CONCERT,
    GATHERING,
    PARTY,
    SPORTS,
    THEATER,
    UNRECOGNIZED;

    public static EventType getEventTypeFromStringValue(String value) {
        EventType eventType = EventType.UNRECOGNIZED;

        for (EventType r : values()) {
            if (r.name().equalsIgnoreCase(value)) {
                eventType = r;
                break;
            }
        }

        return eventType;
    }
}
