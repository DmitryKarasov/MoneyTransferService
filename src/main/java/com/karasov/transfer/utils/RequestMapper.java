package com.karasov.transfer.utils;

import com.karasov.transfer.dto.FormDataDto;
import com.karasov.transfer.models.Card;
import com.karasov.transfer.models.Request;

/**
 * Класс маппер для преобразований {@link FormDataDto} в {@link Request}
 */
public class RequestMapper {

    /**
     * преобразует пришедший запрос в карту, с которой совершается перевод
     *
     * @param formDataDto объект запроса.
     * @return {@link Card} карта, с которой совершается перевод
     */
    public static Request requestDtoToRequest(FormDataDto formDataDto) {
        return new Request(
                new Card(
                        formDataDto.cardFromNumber(),
                        formDataDto.cardFromValidTill(),
                        formDataDto.cardFromCVV()
                ),
                formDataDto.cardToNumber(),
                formDataDto.paymentValue(),
                formDataDto.paymentCurrency()
        );
    }


}
