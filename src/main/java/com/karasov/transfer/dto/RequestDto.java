package com.karasov.transfer.dto;

public record RequestDto(
        String cardFromNumber,
        String cardFromValidTill,
        String cardFromCVV,
        String cardToNumber,
        int paymentValue,
        String paymentCurrency
) {
}


