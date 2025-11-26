package com.github.GuilhermeBauer16.EstaparTesteTecnico.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class InvalidExitTimeException extends RuntimeException {
    public InvalidExitTimeException(String message) {
        super(message);
    }

}
