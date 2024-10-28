package com.karasov.transfer.dto;

public record RequestDto(
        String cardFromNumber,
        String cardFromValidTill,
        String cardFromCVV,
        String cardToNumber,
        PaymentDto amount
) {
    public record PaymentDto(
            String currency,
            double value
    ) {
    }
}


