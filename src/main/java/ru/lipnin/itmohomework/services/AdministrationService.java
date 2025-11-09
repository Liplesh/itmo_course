package ru.lipnin.itmohomework.services;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import ru.lipnin.itmohomework.exception.AdministrationException;
import ru.lipnin.itmohomework.security.entity.ApplicationUser;
import ru.lipnin.itmohomework.security.entity.UserRole;
import ru.lipnin.itmohomework.security.repository.ApplicationUserRepository;
import ru.lipnin.itmohomework.security.repository.UserRoleRepository;

@RequiredArgsConstructor
@Service
public class AdministrationService {

    private final ApplicationUserRepository applicationUserRepository;
    private final UserRoleRepository userRoleRepository;

    public void changeUserRole(Long userId, UserRole.RoleType role) {
        ApplicationUser user = applicationUserRepository.findById(userId).
                orElseThrow(() -> new AdministrationException(HttpStatus.NOT_FOUND, "User not found"));

        userRoleRepository.findByRoleType(role)
                .ifPresentOrElse(user::setUserRole,
                        () -> {
                            UserRole userRole = new UserRole();
                            userRole.setRoleType(role);
                            user.setUserRole(userRole);
                            userRoleRepository.save(userRole);
                        }
                );

        applicationUserRepository.save(user);
    }


}
