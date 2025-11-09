package ru.lipnin.itmohomework.dto.dwh;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import java.time.LocalDateTime;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public record DwnAppointmentDto(
        Long id,
        String clientName,
        String clientPhone,
        @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
        LocalDateTime appointmentTime,
        @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
        LocalDateTime appointmentClose,
        int totalPrice,
        String priceNote,
        Long serviceId,
        Long userId
) {
}
