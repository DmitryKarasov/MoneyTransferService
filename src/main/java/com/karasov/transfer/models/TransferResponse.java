package com.karasov.transfer.models;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * Представляет ответ на запрос перевода денег.
 */
@Getter
@Setter
@EqualsAndHashCode
@ToString
@RequiredArgsConstructor
public class TransferResponse {

    /**
     * Уникальный идентификатор операции перевода.
     */
    private final String operationId;

}
