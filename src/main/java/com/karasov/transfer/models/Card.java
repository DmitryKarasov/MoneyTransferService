package com.karasov.transfer.models;


import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
@EqualsAndHashCode
public class Card {

    private final String cardNumber;
    private final String validTill;
    private final String cvv;
    @EqualsAndHashCode.Exclude
    private double balance;

    public Card(String cardNumber, String validTill, String cvv, double balance) {
        this.cardNumber = cardNumber;
        this.validTill = validTill;
        this.cvv = cvv;
        this.balance = balance;

    }

    public Card(String cardNumber, String validTill, String cvv) {
        this(cardNumber, validTill, cvv, 0.0);
    }

    public void refillBalance(double amount) {
        balance += amount;
    }

    public void withdrawMoney(double money) {
        if (balance >= money) {
            balance -= money;
        }
    }
}
