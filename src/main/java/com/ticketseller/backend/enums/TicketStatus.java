package com.ticketseller.backend.enums;

public enum TicketStatus {
    RESERVED,
    EMPTY,
    UNRECOGNIZED,
    CANCELLED;


    public static TicketStatus getTicketStatusFromStringValue(String value) {
        TicketStatus ticketStatus = TicketStatus.UNRECOGNIZED;

        for (TicketStatus r : values()) {
            if (r.name().equalsIgnoreCase(value)) {
                ticketStatus = r;
                break;
            }
        }

        return ticketStatus;
    }
}
