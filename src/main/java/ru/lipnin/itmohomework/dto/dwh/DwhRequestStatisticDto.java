package ru.lipnin.itmohomework.dto.dwh;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import java.time.LocalDateTime;
import java.util.List;
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public record DwhRequestStatisticDto(
        @JsonFormat(pattern = "yyyy-MM-dd")
        LocalDateTime requestDate, // День, за который отчет
        List<DwnAppointmentDto> appointments
) {
}
