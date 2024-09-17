package com.karasov.transfer.utils;

import com.karasov.transfer.controllers.TransferController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Утилитарный касс логгирования результатов методов контроллера {@link TransferController}
 */
public class TransferLogger {
    private static final Logger logger = LoggerFactory.getLogger(TransferController.class);

    /**
     * Осуществляет логгирование.
     *
     * @param message сообщение, сформированное в контроллере.
     * @param result  результат, сформированный в контроллере.
     */
    public static void log(String message, String result) {
        logger.info("{}, {}.", message, result);
    }

}
