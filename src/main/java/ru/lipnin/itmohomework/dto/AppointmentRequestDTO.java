package ru.lipnin.itmohomework.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import jakarta.validation.constraints.*;

import java.time.LocalDateTime;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public record AppointmentRequestDTO(
        @NotNull
        @NotBlank
        @JsonProperty("name")
        String clientName,

        @NotNull
        @NotBlank
        @JsonProperty("phone")
        @Pattern(regexp = "^(8|\\+7) \\d{3} \\d{3}-\\d{2}-\\d{2}$")
        String clientPhone,

        @NotBlank
        String note,

        @NotNull
        @Future
        @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
        LocalDateTime appointmentTime,

        @NotNull()
        @Positive()
        Long serviceId
) {
}
