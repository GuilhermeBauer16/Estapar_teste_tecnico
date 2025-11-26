package com.github.GuilhermeBauer16.EstaparTesteTecnico.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class ActiveParkingNotFoundException extends RuntimeException {
    public ActiveParkingNotFoundException(String message) {
        super(message);
    }

}
