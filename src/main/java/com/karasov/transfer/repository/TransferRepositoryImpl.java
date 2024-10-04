package com.karasov.transfer.repository;

import com.karasov.transfer.models.Request;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Map;

@Repository
public class TransferRepositoryImpl implements TransferRepository {

    private final Map<Integer, Request> requests = new HashMap<>();

    @Override
    public void addRequest(Request request) {
        requests.put(request.getId(), request);
    }

    @Override
    public Request getRequest(Integer id) {
        return requests.get(id);
    }
}

