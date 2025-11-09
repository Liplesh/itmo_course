package ru.lipnin.itmohomework.exception;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import ru.lipnin.itmohomework.dto.exception.CustomExceptionDTO;
import ru.lipnin.itmohomework.security.exception.CustomSecurityException;

import java.time.LocalDateTime;

@ControllerAdvice
public class CustomExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(value = AppointmentException.class)
    protected ResponseEntity<Object> handleAppointmentException(AppointmentException ex, WebRequest request) {
        CustomExceptionDTO exceptionDTO = new CustomExceptionDTO(ex.getMessage(), LocalDateTime.now());
        return handleExceptionInternal(ex, exceptionDTO, new HttpHeaders(), ex.getHttpStatus(), request);
    }

    @ExceptionHandler(value = {BeautyServiceException.class})
    protected ResponseEntity<Object> handleBeautyServiceException(BeautyServiceException ex, WebRequest request) {
        CustomExceptionDTO exceptionDTO = new CustomExceptionDTO(ex.getMessage(), LocalDateTime.now());
        return handleExceptionInternal(ex, exceptionDTO, new HttpHeaders(), ex.getHttpStatus(), request);
    }

    @ExceptionHandler(value = {DiscountException.class})
    protected ResponseEntity<Object> handleDiscountException(DiscountException ex, WebRequest request) {
        CustomExceptionDTO exceptionDTO = new CustomExceptionDTO(ex.getMessage(), LocalDateTime.now());
        return handleExceptionInternal(ex, exceptionDTO, new HttpHeaders(), ex.getHttpStatus(), request);
    }

    @ExceptionHandler(value = {CustomSecurityException.class})
    protected ResponseEntity<Object> handleSecurityException(CustomSecurityException ex, WebRequest request) {
        CustomExceptionDTO exceptionDTO = new CustomExceptionDTO(ex.getMessage(), LocalDateTime.now());
        return handleExceptionInternal(ex, exceptionDTO, new HttpHeaders(), ex.getHttpStatus(), request);
    }
}
