package ru.lipnin.itmohomework.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import java.math.BigDecimal;

//Дто ответа о выручке
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public record AppointmentEarningsResponseDTO(
        String period,
        BigDecimal sum
) {
}
