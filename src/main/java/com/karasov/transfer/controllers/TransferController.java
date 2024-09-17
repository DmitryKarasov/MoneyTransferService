package com.karasov.transfer.controllers;

import com.karasov.transfer.dto.RequestDto;
import com.karasov.transfer.service.TransferService;
import com.karasov.transfer.utils.TransferLogger;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.karasov.transfer.utils.RequestMapper.requestDtoToCardFrom;
import static com.karasov.transfer.utils.RequestMapper.requestDtoToCardTo;
import static com.karasov.transfer.utils.RequestMapper.requestDtoToPayment;


/**
 * Рест контроллер для осуществления переводов.
 * Этот контроллер обрабатывает HTTP-запросы по адресу '/transfer'.
 * Он отвечает за выполнение операции перевода денежных средств и
 * подтверждение операции.
 */
@RequiredArgsConstructor
@RestController
@RequestMapping("/transfer")
public class TransferController {

    private final TransferService transferService;

    /**
     * Осуществляет перевод денежных средств.
     * Этот метод принимает запрос на перевод, выполняет его с помощью
     * сервиса {@link TransferService} и логирует результат. Если перевод успешен,
     * метод возвращает идентификатор операции с статусом 200 (OK). Если
     * произошла ошибка, возвращается идентификатор операции с статусом
     * 400 (BAD REQUEST).
     *
     * @param request объект, содержащий данные для перевода, включая
     *                информацию о картах и сумму перевода.
     * @return ResponseEntity с идентификатором операции: в случае успеха
     * возвращается статус 200, в случае ошибки — статус 400.
     */
    @PostMapping("/transfer")
    public ResponseEntity<String> transferMoney(@RequestBody RequestDto request) {
        boolean transferred = transferService.transferMoney(
                requestDtoToCardFrom(request),
                requestDtoToCardTo(request),
                requestDtoToPayment(request)
        );

        String logMessage = String.format(
                "card from: %s, " +
                        "card to: %s, " +
                        "amount: %d, " +
                        "commission: %.2f",
                request.cardFromNumber(),
                request.cardToNumber(),
                request.paymentValue(),
                request.paymentValue() / 100.0
        );

        if (transferred) {
            TransferLogger.log(logMessage, "Success confirmation");
            return new ResponseEntity<>(String.valueOf(transferService.getOperationId()), HttpStatus.OK);
        }

        TransferLogger.log(logMessage, "Error customer message");
        return new ResponseEntity<>(String.valueOf(transferService.getOperationId()), HttpStatus.BAD_REQUEST);
    }


    /**
     *
     */
    @PostMapping
    public ResponseEntity<String> confirmOperation() {
        //TODO контроллер подтверждения операции
        return null;
    }
}
