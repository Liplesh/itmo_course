package ru.lipnin.itmohomework.security.dto.security;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import ru.lipnin.itmohomework.security.entity.UserRole;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public record UserDto(
        Long id,
        String login,
        String email
) {
}
