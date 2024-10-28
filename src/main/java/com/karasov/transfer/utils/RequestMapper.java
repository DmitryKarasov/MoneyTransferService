package com.karasov.transfer.utils;

import com.karasov.transfer.dto.RequestDto;
import com.karasov.transfer.models.Card;
import com.karasov.transfer.models.Request;

/**
 * Класс маппер для преобразований {@link RequestDto} в {@link Request}.
 * Предоставляет методы для преобразования объектов запроса в объекты перевода.
 */
public class RequestMapper {

    /**
     * Преобразует пришедший запрос в объект {@link Request}, который содержит информацию о переводе.
     *
     * @param requestDto объект запроса, содержащий данные для перевода.
     * @return {@link Request} объект, содержащий информацию о переводе, включая
     *         карту, с которой совершается перевод, номер карты получателя,
     *         сумму перевода и валюту.
     */
    public static Request requestDtoToRequest(RequestDto requestDto) {
        return new Request(
                new Card(
                        requestDto.cardFromNumber(),
                        requestDto.cardFromValidTill().replaceAll("[^\\d]", ""),
                        requestDto.cardFromCVV()
                ),
                requestDto.cardToNumber(),
                requestDto.amount().value() / 100,
                requestDto.amount().currency()
        );
    }
}
