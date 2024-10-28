package com.karasov.transfer.models;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * Представляет запрос на перевод денег с одной карты на другую.
 */
@Getter
@EqualsAndHashCode
@ToString
public class Request {

    /**
     * Карта, с которой осуществляется перевод.
     */
    private final Card cardFrom;

    /**
     * Номер карты, на которую осуществляется перевод.
     */
    private final String cardToNumber;

    /**
     * Информация о платеже, включая сумму и валюту.
     */
    private final Payment payment;

    /**
     * Код верификации для подтверждения операции.
     */
    @Setter
    private String verificationCode;

    /**
     * Уникальный идентификатор запроса.
     */
    @Setter
    private String id;

    /**
     * Создает новый запрос на перевод с указанной карты на другую карту.
     *
     * @param cardFrom      карта, с которой осуществляется перевод
     * @param cardToNumber  номер карты, на которую осуществляется перевод
     * @param paymentValue  сумма перевода
     * @param currency      валюта перевода
     */
    public Request(Card cardFrom, String cardToNumber, double paymentValue, String currency) {
        this.cardFrom = cardFrom;
        this.cardToNumber = cardToNumber;
        this.payment = new Payment(paymentValue, currency);
    }

    /**
     * Представляет информацию о платеже, включая сумму и валюту.
     */
    @Getter
    @ToString
    @EqualsAndHashCode
    public class Payment {
        /**
         * Сумма перевода.
         */
        private final double paymentValue;

        /**
         * Валюта перевода.
         */
        private final String currency;

        /**
         * Создает новый объект Payment с указанной суммой и валютой.
         *
         * @param paymentValue сумма перевода
         * @param currency     валюта перевода
         */
        public Payment(double paymentValue, String currency) {
            this.paymentValue = paymentValue;
            this.currency = currency;
        }
    }
}
