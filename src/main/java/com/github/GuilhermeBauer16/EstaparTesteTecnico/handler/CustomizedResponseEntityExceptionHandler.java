package com.github.GuilhermeBauer16.EstaparTesteTecnico.handler;


import com.github.GuilhermeBauer16.EstaparTesteTecnico.exception.ActiveParkingNotFoundException;
import com.github.GuilhermeBauer16.EstaparTesteTecnico.exception.ExceptionResponse;
import com.github.GuilhermeBauer16.EstaparTesteTecnico.exception.GarageClosedException;
import com.github.GuilhermeBauer16.EstaparTesteTecnico.exception.GarageFullException;
import com.github.GuilhermeBauer16.EstaparTesteTecnico.exception.GarageNotFoundException;
import com.github.GuilhermeBauer16.EstaparTesteTecnico.exception.InvalidEntryDayException;
import com.github.GuilhermeBauer16.EstaparTesteTecnico.exception.InvalidEventTypeException;
import com.github.GuilhermeBauer16.EstaparTesteTecnico.exception.InvalidExitTimeException;
import com.github.GuilhermeBauer16.EstaparTesteTecnico.exception.InvalidSimulatorException;
import com.github.GuilhermeBauer16.EstaparTesteTecnico.exception.ParkingEventNotFoundException;
import com.github.GuilhermeBauer16.EstaparTesteTecnico.exception.VehicleAlreadyParkedException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.WebRequest;

import java.util.Date;

@ControllerAdvice
@RestController
public class CustomizedResponseEntityExceptionHandler {

    @ExceptionHandler({
            GarageFullException.class
    })
    public final ResponseEntity<ExceptionResponse> handlerConflictException(
            Exception ex,
            WebRequest webRequest
    ) {
        ExceptionResponse exceptionResponse = new ExceptionResponse(
                ex.getMessage(),
                webRequest.getDescription(false),
                new Date()
        );
        return new ResponseEntity<>(exceptionResponse, HttpStatus.CONFLICT);

    }

    @ExceptionHandler({
            GarageClosedException.class,
            InvalidSimulatorException.class,
            VehicleAlreadyParkedException.class,
            InvalidExitTimeException.class,
            InvalidEntryDayException.class,
            InvalidEventTypeException.class
    })
    public final ResponseEntity<ExceptionResponse> handlerBadRequestException(
            Exception ex,
            WebRequest webRequest
    ) {
        ExceptionResponse exceptionResponse = new ExceptionResponse(
                ex.getMessage(),
                webRequest.getDescription(false),
                new Date()
        );
        return new ResponseEntity<>(exceptionResponse, HttpStatus.BAD_REQUEST);

    }

    @ExceptionHandler({
            ActiveParkingNotFoundException.class,
            GarageNotFoundException.class,
            ParkingEventNotFoundException.class
    })
    public final ResponseEntity<ExceptionResponse> handlerNotFoundException(
            Exception ex,
            WebRequest webRequest
    ) {
        ExceptionResponse exceptionResponse = new ExceptionResponse(
                ex.getMessage(),
                webRequest.getDescription(false),
                new Date()
        );
        return new ResponseEntity<>(exceptionResponse, HttpStatus.NOT_FOUND);

    }

}
