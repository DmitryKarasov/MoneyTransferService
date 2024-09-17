package com.karasov.transfer.models;

public record Card(
        String cardNumber,
        String validTill,
        String cvv
) {
}
