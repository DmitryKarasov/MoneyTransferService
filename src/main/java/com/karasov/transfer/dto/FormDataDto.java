package com.karasov.transfer.dto;

public record FormDataDto(
        String cardFromNumber,
        String cardFromValidTill,
        String cardFromCVV,
        String cardToNumber,
        double paymentValue,
        String paymentCurrency
) {
}


