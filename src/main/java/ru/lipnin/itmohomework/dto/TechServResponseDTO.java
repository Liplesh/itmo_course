package ru.lipnin.itmohomework.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import ru.lipnin.itmohomework.constants.DifficultyLevel;

import java.time.LocalDateTime;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public record TechServResponseDTO(

        Long id,

        String name,

        String description,

        LocalDateTime signedAt,

        boolean reserve,

        DifficultyLevel diffLevel
) {
}
