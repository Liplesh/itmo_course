package ru.lipnin.itmohomework.mapper;

import org.springframework.stereotype.Service;
import ru.lipnin.itmohomework.dto.security.RegistrationResponseDTO;
import ru.lipnin.itmohomework.security.entity.ApplicationUser;

@Service
public class SecurityMapper {

    public ApplicationUser mapToEntity(RegistrationResponseDTO registrationResponseDTO) {
        return ApplicationUser.builder()
                .username(registrationResponseDTO.userName())
                .email(registrationResponseDTO.email())
                .build();
    }
}
