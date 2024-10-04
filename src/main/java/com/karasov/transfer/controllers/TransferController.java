package com.karasov.transfer.controllers;

import com.karasov.transfer.dto.ConfirmOperationDTO;
import com.karasov.transfer.dto.FormDataDto;
import com.karasov.transfer.dto.RegisterStatusDto;
import com.karasov.transfer.service.TransferService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
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
     * @param formDataDto объект, содержащий данные для перевода, включая
     *                    информацию о картах и сумму перевода.
     * @return ResponseEntity с идентификатором операции
     */
    @PostMapping("/transfer")
    public ResponseEntity<String> transfer(@RequestBody FormDataDto formDataDto) {
        RegisterStatusDto registerStatusDto = transferService.transfer(formDataDto);
        if (registerStatusDto.requestValidated()) {
            return new ResponseEntity<>(String.valueOf(registerStatusDto.operationId()), HttpStatus.OK);
        }

        int operationId = registerStatusDto.operationId();
        log.info(registerStatusDto.statusMessage());
        return new ResponseEntity<>(String.valueOf(operationId), HttpStatus.BAD_REQUEST);
    }

    /**
     *
     */
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
