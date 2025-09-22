package com.pedidos.exceptions.detail;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ErrorDetails {

    private String title;
    private int status;
    private String detail;
    private long timestamp;
    private String message;

}