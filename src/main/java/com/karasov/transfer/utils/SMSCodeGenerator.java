package com.karasov.transfer.utils;

/**
 * Класс генератора SMS-кодов для верификации.
 * Предоставляет метод для генерации верификационного кода.
 */
public class SMSCodeGenerator {

    /**
     * Генерирует верификационный SMS-код.
     *
     * @return строка, представляющая верификационный код.
     *         В данной реализации всегда возвращается "0000".
     */
    public static String generateVerificationSMSCode() {
        return "0000";
    }
}
