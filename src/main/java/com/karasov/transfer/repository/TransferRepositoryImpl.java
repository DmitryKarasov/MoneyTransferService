package com.karasov.transfer.repository;

import com.karasov.transfer.models.Request;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Реализация интерфейса {@link TransferRepository}, предоставляющая методы для работы с запросами на перевод.
 * <p>
 * Данный класс хранит запросы на перевод в памяти с использованием
 * коллекции {@link ConcurrentHashMap}. Он позволяет добавлять новые запросы и
 * получать существующие по идентификатору.
 */
@Repository
public class TransferRepositoryImpl implements TransferRepository {

    private final Map<String, Request> requests = new ConcurrentHashMap<>();

    /**
     * Добавляет новый запрос на перевод в репозиторий.
     *
     * @param request запрос на перевод, который будет добавлен
     */
    @Override
    public void addRequest(Request request) {
        requests.put(request.getId(), request);
    }

    /**
     * Получает запрос на перевод по идентификатору.
     *
     * @param id идентификатор запроса для поиска
     * @return {@link Request} объект, если запрос найден; {@code null}, если запрос с указанным идентификатором не найден
     */
    @Override
    public Request getRequest(String id) {
        return requests.get(id);
    }
}

