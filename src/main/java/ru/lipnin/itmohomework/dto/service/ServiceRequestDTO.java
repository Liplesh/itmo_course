package ru.lipnin.itmohomework.dto.service;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import jakarta.validation.constraints.*;
import ru.lipnin.itmohomework.constants.Category;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public record ServiceRequestDTO (

        @NotNull
        @NotBlank
        String name,

        @NotBlank
        String description,

        @NotNull
        @Positive
        @Min(30)
        @Max(120)
        int duration,

        @NotNull
        @Positive
        @Max(5000)
        int price,

        @NotNull
        Category category
){
}
