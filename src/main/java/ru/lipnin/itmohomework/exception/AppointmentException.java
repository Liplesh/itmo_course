package ru.lipnin.itmohomework.exception;

import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

@Getter
@Setter
public class AppointmentException extends RuntimeException {
    private HttpStatus httpStatus;

    public AppointmentException(HttpStatus httpStatus, String message) {
        super(message);
        this.httpStatus = httpStatus;
    }
}
