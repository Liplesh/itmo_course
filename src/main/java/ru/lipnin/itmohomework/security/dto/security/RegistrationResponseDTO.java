package ru.lipnin.itmohomework.security.dto.security;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public record RegistrationResponseDTO(
        @NotNull
        @NotBlank
        String name,
        @NotNull
        @NotBlank
        @Size(min = 8)
        String password,
        @NotNull
        @Email
        String email
) {
}
