package com.karasov.transfer.models;

import lombok.Getter;
import lombok.Setter;

@Getter
public class Request {
    private final Card cardFrom;
    private final String cardToNumber;
    private final Payment payment;
    @Setter
    private String verificationCode;
    @Setter
    private int id;

    public Request(Card cardFrom, String cardToNumber, double paymentValue, String currency) {
        this.cardFrom = cardFrom;
        this.cardToNumber = cardToNumber;
        this.payment = new Payment(paymentValue, currency);
    }

    @Getter
    public class Payment {
        private final double paymentValue;
        private final String currency;

        public Payment(double paymentValue, String currency) {
            this.paymentValue = paymentValue;
            this.currency = currency;
        }
    }
}
