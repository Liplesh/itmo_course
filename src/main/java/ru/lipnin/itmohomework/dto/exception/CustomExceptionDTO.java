package ru.lipnin.itmohomework.dto.exception;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import java.time.LocalDateTime;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public record CustomExceptionDTO(
        String message,
        LocalDateTime time
) {
}
