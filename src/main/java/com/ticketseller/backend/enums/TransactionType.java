package com.ticketseller.backend.enums;

public enum TransactionType {
    DEPOSIT,
    WITHDRAWAL,
    EVENT_BUY,
    EVENT_REFUND,
    UNRECOGNIZED;

    public static TransactionType getTransactionTypeFromStringValue(String value) {
        TransactionType role = TransactionType.UNRECOGNIZED;

        for (TransactionType r : values()) {
            if (r.name().equalsIgnoreCase(value)) {
                role = r;
                break;
            }
        }

        return role;
    }
}
