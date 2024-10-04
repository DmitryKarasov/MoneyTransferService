package com.karasov.transfer.utils;

import com.karasov.transfer.service.TransferService;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Утилитарный касс валидации данных, обрабатываемых в классе {@link TransferService}
 */
public class Validator {

    /**
     * Осуществляет проверку номера карты.
     * Условие проверки: номер должен состоять из 16 цифр
     *
     * @param cardNumber номер карты в формате String.
     * @return true, если условие проверки соблюдается, иначе false.
     */
    public static boolean validateCardNumber(String cardNumber) {
        return cardNumber.matches("^[0-9]{16}$");
    }

    /**
     * Осуществляет проверку номера карты.
     * Условие проверки:
     * 1) дата окончания действия карты должны соответствовать формату MMYY
     * 2) дата окончания действия карты не должна быть раньше сегодняшней
     *
     * @param validTill String дата окончания действия карты.
     * @return true, если условие проверки соблюдается, иначе false.
     */
    public static boolean validateExpireDate(String validTill) {
        if (!validTill.matches("^(0[1-9]|1[0-2])[0-9]{2}$")) {
            return false;
        }
        int month = Integer.parseInt(validTill.substring(0, 2));
        int year = 2000 + Integer.parseInt(validTill.substring(2, 4));
        return LocalDateTime.now().isBefore(LocalDate.of(year, month, 1).atStartOfDay());
    }

    /**
     * Осуществляет проверку номера cvv карты.
     * Условие проверки: номер cvv карты должен состоять из трех цифр.
     *
     * @param cvv String номер cvv карты.
     * @return true, если условие проверки соблюдается, иначе false.
     */
    public static boolean validateCVV(String cvv) {
        return cvv.matches("^[0-9]{3}$");
    }

    /**
     * Осуществляет проверку переводимой суммы.
     * Условие проверки: сумма не может быть меньше или равна 0.
     *
     * @param value int переводимая сумма.
     * @return true, если условие проверки соблюдается, иначе false.
     */
    public static boolean validatePaymentValue(double value) {
        return value > 0.0;
    }

    /**
     * Осуществляет проверку валюты.
     * Условие проверки: значение валюты равно "RUB".
     *
     * @param currency String значение валюты.
     * @return true, если условие проверки соблюдается, иначе false.
     */
    public static boolean validatePaymentCurrency(String currency) {
        return currency.equals("RUB");
    }
}
