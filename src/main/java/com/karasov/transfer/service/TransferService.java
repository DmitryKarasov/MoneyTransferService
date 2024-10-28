package com.karasov.transfer.service;

import com.karasov.transfer.controllers.TransferController;
import com.karasov.transfer.dto.ConfirmOperationDTO;
import com.karasov.transfer.dto.RegisterStatusDto;
import com.karasov.transfer.dto.RequestDto;
import com.karasov.transfer.models.Card;
import com.karasov.transfer.models.Request;
import com.karasov.transfer.repository.CardRepository;
import com.karasov.transfer.repository.TransferRepository;
import com.karasov.transfer.utils.SMSCodeGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import static com.karasov.transfer.utils.RequestMapper.requestDtoToRequest;
import static com.karasov.transfer.utils.SMSCodeGenerator.generateVerificationSMSCode;
import static com.karasov.transfer.utils.Validator.validateCVV;
import static com.karasov.transfer.utils.Validator.validateCardNumber;
import static com.karasov.transfer.utils.Validator.validateExpireDate;
import static com.karasov.transfer.utils.Validator.validatePaymentCurrency;
import static com.karasov.transfer.utils.Validator.validatePaymentValue;
import static java.lang.Math.round;

/**
 * Сервис, отвечающий за обработку переводов денежных средств.
 * <p>
 * Класс принимает запросы от контроллера {@link TransferController},
 * выполняет валидацию данных о переводе и управляет процессом перевода средств
 * между картами. Он также отслеживает количество проведенных операций и
 * генерирует верификационные коды для подтверждения переводов.
 */
@Service
public class TransferService {
    private static final AtomicInteger operationId = new AtomicInteger(0);
    private final static double COMMISSION = 0.01;
    private final TransferRepository transferRepository;
    private final CardRepository cardRepository;

    @Autowired
    public TransferService(TransferRepository transferRepository, CardRepository cardRepository) {
        this.transferRepository = transferRepository;
        this.cardRepository = cardRepository;
    }

    /**
     * Обрабатывает запрос на перевод средств.
     * <p>
     * Принимает данные о переводе, присваивает уникальный номер операции и проверяет
     * корректность введенных данных. Если данные корректны, генерирует
     * верификационный код и сохраняет запрос в репозитории.
     * <p>
     * @param requestDto объект, содержащий данные для перевода, включая информацию о картах и сумму перевода.
     * @return объект {@link RegisterStatusDto}, инкапсулирующий статус валидации запроса и номер операции.
     */
    public RegisterStatusDto transfer(RequestDto requestDto) {
        Request request = requestDtoToRequest(requestDto);
        request.setId(String.valueOf(operationId.incrementAndGet()));
        Card cardFrom = request.getCardFrom();

        RegisterStatusDto registerStatusDto = new RegisterStatusDto(
                (
                        validateCardNumber(cardFrom.getCardNumber())
                                && validateCardNumber(request.getCardToNumber())
                                && validateExpireDate(cardFrom.getValidTill())
                                && validateCVV(cardFrom.getCvv())
                                && validatePaymentValue(request.getPayment().getPaymentValue())
                                && validatePaymentCurrency(request.getPayment().getCurrency())
                ),
                request.getId(),
                String.format(
                        "Error input data for transfer with operationId: %s " +
                                "card from: %s, " +
                                "card to: %s, " +
                                "amount: %.2f.",
                        request.getId(),
                        requestDto.cardFromNumber(),
                        requestDto.cardToNumber(),
                        requestDto.amount().value()
                )
        );

        if (registerStatusDto.requestValidated()) {
            request.setVerificationCode(generateVerificationSMSCode());
            transferRepository.addRequest(request);
        }
        System.out.println(registerStatusDto);
        return registerStatusDto;
    }

    /**
     * Подтверждает перевод средств по полученному верификационному коду.
     * <p>
     * Проверяет наличие запроса, соответствие верификационного кода и
     * проводит перевод средств, если все проверки успешны.
     * </p>
     * @param confirmOperationDTO объект, содержащий идентификатор операции и верификационный код.
     * @return объект {@link RegisterStatusDto}, инкапсулирующий статус подтверждения операции.
     */
    public RegisterStatusDto confirm(ConfirmOperationDTO confirmOperationDTO) {

        Optional<Request> optionalRequest = Optional.ofNullable(transferRepository
                .getRequest(confirmOperationDTO.operationId()));
        if (optionalRequest.isEmpty()) {
            return new RegisterStatusDto(
                    false,
                    confirmOperationDTO.operationId(),
                    String.format("Request with operationId %s not found.", confirmOperationDTO.operationId()));
        }

        Request request = optionalRequest.get();
        if (!request.getVerificationCode().equals(confirmOperationDTO.code())) {
            return new RegisterStatusDto(
                    false, request.getId(),
                    String.format("Invalid verification code for transfer with operationId: %s.", request.getId()));
        }

        Optional<Card> optionalCardFrom =
                Optional.ofNullable(cardRepository.getCardByNumber(request.getCardFrom().getCardNumber()));
        Optional<Card> optionalCardTo =
                Optional.ofNullable(cardRepository.getCardByNumber(request.getCardToNumber()));
        if (optionalCardFrom.isEmpty() || optionalCardTo.isEmpty()) {
            return new RegisterStatusDto(
                    false, request.getId(),
                    String.format("Card not found for transfer with operationId: %s.", request.getId()));
        }

        Card cardFrom = optionalCardFrom.get();
        if (!cardFrom.equals(request.getCardFrom())) {
            return new RegisterStatusDto(
                    false, request.getId(),
                    String.format("Invalid sender's card details for transfer with operationId: %s.", request.getId()));
        }

        Card cardTo = optionalCardTo.get();
        if (!cardTo.getCardNumber().equals(request.getCardToNumber())) {
            return new RegisterStatusDto(
                    false, request.getId(),
                    String.format("Invalid recipient's card number for transfer with operationId: %s.", request.getId()));
        }


        boolean successTransferStatus = makeTransfer(cardTo, cardFrom, request.getPayment().getPaymentValue());
        String logMessage = successTransferStatus ? "Success confirmation " : "Not enough money ";

        return new RegisterStatusDto(
                successTransferStatus,
                request.getId(),
                logMessage + String.format(
                        "for transfer with operationId: %s " +
                                "card from: %s, " +
                                "card to: %s, " +
                                "amount: %.2f, " +
                                "commission: %.2f",
                        request.getId(),
                        cardFrom.getCardNumber(),
                        cardTo.getCardNumber(),
                        request.getPayment().getPaymentValue(),
                        request.getPayment().getPaymentValue() * COMMISSION
                )
        );
    }

    /**
     * Выполняет перевод средств между картами.
     * <p>
     * Проверяет наличие достаточных средств на карте отправителя,
     * осуществляет перевод и возвращает статус выполнения.
     * </p>
     * @param cardTo карта получателя
     * @param cardFrom карта отправителя
     * @param payment сумма перевода
     * @return {@code true}, если перевод выполнен успешно; {@code false}, если средств недостаточно.
     */
    private boolean makeTransfer(Card cardTo, Card cardFrom, double payment) {
        double paymentWithCommission = round(payment * (1 + COMMISSION) * 100) / 100.0;

        if (cardFrom.getBalance() < paymentWithCommission) {
            return false;
        }

        final Lock transferLock = new ReentrantLock();
        transferLock.lock();
        try {
            cardTo.refillBalance(paymentWithCommission);
            cardFrom.withdrawMoney(paymentWithCommission);
            return true;
        } finally {
            transferLock.unlock();
        }
    }
}
