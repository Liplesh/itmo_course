package ru.lipnin.itmohomework.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import jakarta.validation.constraints.*;
import ru.lipnin.itmohomework.constants.DifficultyLevel;

import java.time.LocalDateTime;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public record TechServRequestDTO(

        @NotNull
        @Positive
        Long id,

        @NotBlank
        String name,

        @NotBlank
        String description,

        @Future
        @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
        LocalDateTime signetAt, //дата бронирования

        boolean reserve,

        @NotNull
        DifficultyLevel diffLevel
) {
}


