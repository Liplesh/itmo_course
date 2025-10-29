package ru.lipnin.itmohomework.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import ru.lipnin.itmohomework.constants.Category;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public record ServiceResponseDTO(
        String name,
        String description,
        int duration,
        int price,
        Category category
) {
}
