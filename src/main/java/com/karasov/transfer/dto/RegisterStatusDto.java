package com.karasov.transfer.dto;

public record RegisterStatusDto(
        boolean requestValidated,
        String operationId,
        String statusMessage
) {
}
