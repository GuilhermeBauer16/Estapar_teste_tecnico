package com.github.GuilhermeBauer16.EstaparTesteTecnico.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class ParkingEventNotFoundException extends RuntimeException {
    public ParkingEventNotFoundException(String message) {
        super(message);
    }

}
