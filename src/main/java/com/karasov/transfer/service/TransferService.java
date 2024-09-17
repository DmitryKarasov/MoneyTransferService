package com.karasov.transfer.service;

import com.karasov.transfer.controllers.TransferController;
import com.karasov.transfer.models.Card;
import com.karasov.transfer.models.Payment;
import org.springframework.stereotype.Service;

import java.util.concurrent.atomic.AtomicInteger;

import static com.karasov.transfer.utils.Validator.validateCVV;
import static com.karasov.transfer.utils.Validator.validateCardNumber;
import static com.karasov.transfer.utils.Validator.validateExpireDate;
import static com.karasov.transfer.utils.Validator.validatePaymentCurrency;
import static com.karasov.transfer.utils.Validator.validatePaymentValue;

/**
 * Класс отвечающий за обработку переводов с контроллера {@link TransferController}.
 * Проверяет правильность операции.
 * Так же считает количество проведенных операций.
 */
@Service
public class TransferService {

    private static final AtomicInteger operationId = new AtomicInteger(0);

    /**
     * Осуществляет проверку запроса.
     *
     * @param cardFrom карта, с которой совершается перевод.
     * @param cardTo   карта, на которую соверщается перевод.
     * @param payment  сумм перевода.
     * @return true, если данные обеих карт корректные, а сумма перевода лежит в допустимых пределах.
     */
    public boolean transferMoney(Card cardFrom, Card cardTo, Payment payment) {
        operationId.incrementAndGet();
        return validateCardNumber(cardFrom.cardNumber())
                && validateCardNumber(cardTo.cardNumber())
                && validateExpireDate(cardFrom.validTill())
                && validateCVV(cardFrom.cvv())
                && validatePaymentValue(payment.value())
                && validatePaymentCurrency(payment.currency());
    }

    /**
     * @return int значение номера операции.
     */
    public int getOperationId() {
        return operationId.get();
    }

}
