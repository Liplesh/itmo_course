package ru.lipnin.itmohomework.security.exception;

import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

@Getter
@Setter
public class CustomSecurityException extends RuntimeException {
    private HttpStatus httpStatus;

    public CustomSecurityException(HttpStatus httpStatus, String message) {
        super(message);
        this.httpStatus = httpStatus;
    }
}
