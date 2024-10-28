package com.karasov.transfer.controllers;

import com.karasov.transfer.dto.ConfirmOperationDTO;
import com.karasov.transfer.dto.RegisterStatusDto;
import com.karasov.transfer.dto.RequestDto;
import com.karasov.transfer.models.TransferResponse;
import com.karasov.transfer.service.TransferService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;


/**
 * Рест контроллер для осуществления переводов.
 * Этот контроллер обрабатывает HTTP-запросы по адресу '/transfer'.
 * Он отвечает за выполнение операции перевода денежных средств и
 * подтверждение операции.
 */
@Slf4j
@RequiredArgsConstructor
@RestController
public class TransferController {

    private final TransferService transferService;

    /**
     * Осуществляет прием запроса на перевод денежных средств с карты на карту.
     * Этот метод принимает запрос на перевод {@link RequestDto}, передает его на регистрацию и валидацию в
     * сервис {@link TransferService} и логгирует результат. Если данные перевода валидны,
     * метод возвращает идентификатор операции со статусом 200 (OK). Если
     * произошла ошибка, возвращается идентификатор операции со статусом
     * 400 (BAD REQUEST).
     *
     * @param requestDto объект, содержащий данные для перевода, включая
     *                   информацию о картах и сумму перевода.
     * @return ResponseEntity с объектом {@link TransferResponse}, инкапсулирующим идентификатор операции
     */
    @CrossOrigin
    @PostMapping("/transfer")
    public ResponseEntity<TransferResponse> transfer(@RequestBody RequestDto requestDto) {
        RegisterStatusDto registerStatusDto = transferService.transfer(requestDto);
        if (registerStatusDto.requestValidated()) {
            return ResponseEntity.ok(new TransferResponse(String.valueOf(registerStatusDto.operationId())));
        }

        log.info(registerStatusDto.statusMessage());
        return new ResponseEntity<>(new TransferResponse(String.valueOf(registerStatusDto.operationId())), HttpStatus.BAD_REQUEST);
    }

    /**
     * Осуществляет перевод денежных средств с карты на карту.
     * Этот метод принимает данные для подтверждения перевода {@link ConfirmOperationDTO}, передает его на валидацию в
     * сервис {@link TransferService} и логгирует результат. Если данные валидны,
     * метод совершает перевод со статусом 200 (OK) или 400 (BAD REQUEST) в противном случае.
     *
     * @param confirmOperationDTO объект, содержащий подтверждающие данные для перевода:
     *                            номер операции и верификационный номер.
     * @return ResponseEntity с идентификатором операции
     */
    @CrossOrigin
    @PostMapping("/confirmOperation")
    public ResponseEntity<String> confirm(@RequestBody ConfirmOperationDTO confirmOperationDTO) {
        RegisterStatusDto registerStatusDto = transferService.confirm(confirmOperationDTO);
        log.info(registerStatusDto.statusMessage());

        if (registerStatusDto.requestValidated()) {
            return new ResponseEntity<>(String.valueOf(registerStatusDto.operationId()), HttpStatus.OK);
        }
        return new ResponseEntity<>(String.valueOf(registerStatusDto.operationId()), HttpStatus.BAD_REQUEST);
    }
}
