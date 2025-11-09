package ru.lipnin.itmohomework.dto.dwh;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import java.util.List;
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public record DwhResponseDto(
        List<Long> ids
) {
}
