package com.pedidos.exceptions.detail;

import lombok.Builder;
import lombok.Getter;

@Getter
public class ValidationErrorDetails extends ErrorDetails {

    private final String field;

    @Builder
    public ValidationErrorDetails (String title, int status, String detail, long timestamp, String message,
                                   String field) {
        super(title, status, detail, timestamp, message);
        this.field = field;
    }

}
