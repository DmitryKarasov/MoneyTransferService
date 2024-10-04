package com.karasov.transfer.dto;

public record RegisterStatusDto(
        boolean requestValidated,
        int operationId,
        String statusMessage
) {
}
