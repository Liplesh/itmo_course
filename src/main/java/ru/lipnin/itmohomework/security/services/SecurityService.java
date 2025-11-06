package ru.lipnin.itmohomework.security.services;

import com.nimbusds.jose.JOSEException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.lipnin.itmohomework.dto.security.LoginResponseDTO;
import ru.lipnin.itmohomework.dto.security.RegistrationResponseDTO;
import ru.lipnin.itmohomework.mapper.SecurityMapper;
import ru.lipnin.itmohomework.security.entity.ApplicationUser;
import ru.lipnin.itmohomework.security.entity.Token;
import ru.lipnin.itmohomework.security.entity.UserRole;
import ru.lipnin.itmohomework.security.model.CustomUserDetails;
import ru.lipnin.itmohomework.security.repository.ApplicationUserRepository;
import ru.lipnin.itmohomework.security.repository.UserRoleRepository;

@RequiredArgsConstructor
@Service
public class SecurityService {

    private final ApplicationUserRepository applicationUserRepository;
    private final UserRoleRepository userRoleRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtSecurityService jwtSecurityService;
    private final AuthenticationManager authenticationManager;
    private final SecurityMapper securityMapper;

    public Long registration(RegistrationResponseDTO registrationResponseDTO) throws SecurityException {
        if (applicationUserRepository.existsByUsername(registrationResponseDTO.userName())) {
            throw new SecurityException("Username is already taken");
        }

        ApplicationUser user = securityMapper.mapToEntity(registrationResponseDTO);
        UserRole.RoleType type = null;
        if (user.getUsername().equalsIgnoreCase("admin")) {
            //Костыль, чтобы сразу создать админа
            type = UserRole.RoleType.ROLE_ADMIN;
        } else {
            type = UserRole.RoleType.ROLE_USER;
        }
        UserRole.RoleType finalType = type;
        userRoleRepository.findByRoleType(type)
                .ifPresentOrElse(user::setUserRole,
                        () -> {
                            UserRole userRole = new UserRole();
                            userRole.setRoleType(finalType);
                            user.setUserRole(userRole);
                            userRoleRepository.save(userRole);
                        }
                );

        user.setPassword(passwordEncoder.encode(registrationResponseDTO.password()));
        applicationUserRepository.save(user);

        return user.getId();
    }

    public Token loginAccount(LoginResponseDTO loginResponseDTO) throws SecurityException {

        Authentication authentication = authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken(loginResponseDTO.name(), loginResponseDTO.password()));

        SecurityContextHolder.getContext().setAuthentication(authentication);
        Token token = new Token();
        try {
            token.setToken(jwtSecurityService.generateToken((CustomUserDetails) authentication.getPrincipal()));
            token.setRefreshToken(jwtSecurityService.generateRefreshToken());
        } catch (JOSEException e) {
            throw new SecurityException("Token cannot be created: " + e.getMessage());
        }
        return token;
    }
}
