package com.github.GuilhermeBauer16.EstaparTesteTecnico.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class ValidationUtilsException extends RuntimeException {

    public ValidationUtilsException(String message, Throwable cause) {
        super(message, cause);
    }
}
