package ru.lipnin.itmohomework.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import java.time.LocalDateTime;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public record AppointmentResponseDTO(
        String clientName,
        String clientPhone,
        String note,
        //Дата бронирования
        @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
        LocalDateTime appointmentTime,
        //Дата создания брони
        @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
        LocalDateTime createdAt,
        //Дата снятия
        @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
        @JsonInclude(JsonInclude.Include.NON_NULL)
        LocalDateTime appointmentClose,
        //Наименование услуги
        String serviceName
) {
}
