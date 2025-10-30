package ru.lipnin.itmohomework.exception;

import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

@Getter
@Setter
public class BeautyServiceException extends RuntimeException {
    private HttpStatus httpStatus;

    public BeautyServiceException(HttpStatus httpStatus, String message) {
        super(message);
        this.httpStatus = httpStatus;
    }
}
