package com.pedidos.exceptions;

import com.pedidos.exceptions.detail.ValidationErrorDetails;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.Date;
import java.util.stream.Collectors;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

@ControllerAdvice
public class ResourceExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException manvException) {
        var fieldErrors = manvException.getBindingResult().getFieldErrors();
        var fields = fieldErrors.stream()
                .map(FieldError::getField)
                .collect(Collectors.joining(", "));
        var fieldMessages = fieldErrors.stream()
                .map(FieldError::getDefaultMessage)
                .collect(Collectors.joining(", "));
        var veDetails = ValidationErrorDetails.builder()
                .timestamp(new Date().getTime())
                .status(BAD_REQUEST.value())
                .title("Erro na validação de campos")
                .detail("Campo(s) preenchido(s) incorretamente")
                .message(fieldMessages)
                .field(fields)
                .build();

        return new ResponseEntity<>(veDetails, BAD_REQUEST);
    }
}