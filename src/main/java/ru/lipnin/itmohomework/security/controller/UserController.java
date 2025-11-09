package ru.lipnin.itmohomework.security.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.lipnin.itmohomework.security.dto.security.LoginResponseDTO;
import ru.lipnin.itmohomework.security.dto.security.RegistrationResponseDTO;
import ru.lipnin.itmohomework.security.dto.security.UserDto;
import ru.lipnin.itmohomework.security.entity.Token;
import ru.lipnin.itmohomework.security.exception.CustomSecurityException;
import ru.lipnin.itmohomework.security.services.SecurityService;

import java.util.List;

@RequiredArgsConstructor
@RestController
@Slf4j
@RequestMapping("/api/v1/users")
public class UserController {

    private final SecurityService securityService;

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

    @GetMapping
    public ResponseEntity<List<UserDto>> getUsers() {
        log.info("Retrieving all users");
        return ResponseEntity.ok(securityService.getAllUsers());
    }

    @GetMapping("{id}")
    public ResponseEntity<UserDto> getUserById(@NotNull @Positive @PathVariable Long id) {
        log.info("Retrieving user by id: {}", id);
        return ResponseEntity.ok(securityService.getUserById(id));
    }
}
