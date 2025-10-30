package ru.lipnin.itmohomework.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import java.time.LocalDateTime;

//Дто запроса о выручке (передаем период, либо если ничего не передали, то ищем по текущую дату)
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public record AppointmentEarningsRequestDTO(

        @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
        LocalDateTime from,
        @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
        LocalDateTime to
) {
}
