package ru.lipnin.itmohomework.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.lipnin.itmohomework.dto.security.LoginResponseDTO;
import ru.lipnin.itmohomework.dto.security.RegistrationResponseDTO;
import ru.lipnin.itmohomework.security.entity.Token;
import ru.lipnin.itmohomework.security.entity.UserRole;
import ru.lipnin.itmohomework.security.services.SecurityService;
import ru.lipnin.itmohomework.services.AdministrationService;

@RequiredArgsConstructor
@RestController
@Slf4j
@Validated
@RequestMapping("/api/v1/account")
public class AccountController {

    private final SecurityService securityService;
    private final AdministrationService administrationService;

    @PostMapping("/registration")
    public ResponseEntity<?> createAccount(@Valid @RequestBody RegistrationResponseDTO registrationResponseDTO) {
        Long registration = securityService.registration(registrationResponseDTO);
        log.info("Account created: {}", registration);
        return ResponseEntity.ok().build();
    }

    //Правильно ли здесь испольовать POST
    //и нужно ли в дто ответа добавлять еще каких-то полей, кроме самого токена
    @PostMapping("/login")
    public ResponseEntity<?> loginAccount(@Valid @RequestBody LoginResponseDTO loginResponseDTO) {
        Token token = securityService.loginAccount(loginResponseDTO);
        return ResponseEntity.ok(token);
    }

    @Secured("ROLE_ADMIN")
    @PutMapping("/changing_role")
    public ResponseEntity<Void> changeRole(@NotNull @RequestParam Long id ,
                                        @NotNull @RequestParam UserRole.RoleType role) {

        administrationService.changeUserRole(id, role);
        return ResponseEntity.ok().build();
    }
    
}
