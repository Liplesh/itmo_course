package ru.lipnin.itmohomework.dto.notification;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import jakarta.validation.constraints.NotNull;

//Костыль для проверки, пока не знаю, как токен прокидывать
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public record UserSecNotificationDto(
        @NotNull
        String message,
        @NotNull
        String email
) {
}
