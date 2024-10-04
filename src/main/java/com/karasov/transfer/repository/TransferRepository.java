package com.karasov.transfer.repository;

import com.karasov.transfer.models.Request;

public interface TransferRepository {
    void addRequest(Request request);

    Request getRequest(Integer id);
}
