package com.karasov.transfer.models;


import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import java.util.Objects;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

/**
 * Представляет платежную карту с ассоциированными свойствами и функциональностью.
 */
@Getter
@ToString
@EqualsAndHashCode
public class Card {

    /**
     * Номер карты.
     */
    private final String cardNumber;

    /**
     * Дата истечения срока действия карты в формате ММ/ГГ.
     */
    private final String validTill;

    /**
     * CVV (код проверки карты) карты.
     */
    private final String cvv;

    /**
     * Баланс, доступный на карте.
     * Это поле исключено из вычислений хэш-кода и сравнений.
     */
    @EqualsAndHashCode.Exclude
    private double balance;

    /**
     * Создает новую карту с указанными данными.
     *
     * @param cardNumber номер карты
     * @param validTill  дата истечения срока действия карты
     * @param cvv        CVV карты
     * @param balance    начальный баланс на карте
     */
    public Card(String cardNumber, String validTill, String cvv, double balance) {
        this.cardNumber = cardNumber;
        this.validTill = validTill;
        this.cvv = cvv;
        this.balance = balance;
    }

    /**
     * Создает новую карту с указанными данными и начальным балансом 0.0.
     *
     * @param cardNumber номер карты
     * @param validTill  дата истечения срока действия карты
     * @param cvv        CVV карты
     */
    public Card(String cardNumber, String validTill, String cvv) {
        this(cardNumber, validTill, cvv, 0.0);
    }

    /**
     * Пополняет баланс карты на указанную сумму.
     *
     * @param amount сумма, которую нужно добавить к балансу
     */
    public void refillBalance(double amount) {
        balance += amount;
    }

    /**
     * Снимает деньги с баланса карты.
     * Снятие происходит только в случае наличия достаточного баланса.
     *
     * @param money сумма денег для снятия
     */
    public void withdrawMoney(double money) {
        if (balance >= money) {
            balance -= money;
        }
    }
}

