package ru.lipnin.itmohomework.security.mapper;

import org.springframework.stereotype.Service;
import ru.lipnin.itmohomework.security.dto.security.RegistrationResponseDTO;
import ru.lipnin.itmohomework.security.dto.security.UserDto;
import ru.lipnin.itmohomework.security.entity.ApplicationUser;

@Service
public class SecurityMapper {

    public ApplicationUser mapToEntity(RegistrationResponseDTO registrationResponseDTO) {
        return ApplicationUser.builder()
                .username(registrationResponseDTO.name())
                .email(registrationResponseDTO.email())
                .build();
    }

    public UserDto mapToDto(ApplicationUser applicationUser) {
        return new UserDto(
                applicationUser.getId(),
                applicationUser.getUsername(),
                applicationUser.getEmail()
        );
    }
}
