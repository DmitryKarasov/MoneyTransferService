package com.karasov.transfer.utils;

import com.karasov.transfer.dto.RequestDto;
import com.karasov.transfer.models.Card;
import com.karasov.transfer.models.Payment;

/**
 * Класс маппер для преобразований {@link RequestDto} в {@link Card} и {@link Payment}
 */
public class RequestMapper {

    /**
     * преобразует пришедший запрос в карту, с которой совершается перевод
     *
     * @param requestDto объект запроса.
     * @return {@link Card} карта, с которой совершается перевод
     */
    public static Card requestDtoToCardFrom(RequestDto requestDto) {
        return new Card(
                requestDto.cardFromNumber(),
                requestDto.cardFromValidTill(),
                requestDto.cardFromCVV()
        );
    }

    /**
     * преобразует пришедший запрос в карту, на которую совершается перевод
     *
     * @param requestDto объект запроса.
     * @return {@link Card} карта, на которую совершается перевод
     */
    public static Card requestDtoToCardTo(RequestDto requestDto) {
        return new Card(
                requestDto.cardToNumber(),
                null,
                null
        );
    }

    /**
     * преобразует пришедший запрос в платеж
     *
     * @param requestDto объект запроса.
     * @return {@link Payment} платеж
     */
    public static Payment requestDtoToPayment(RequestDto requestDto) {
        return new Payment(
                requestDto.paymentValue(),
                requestDto.paymentCurrency()
        );
    }

}
