package ru.lipnin.itmohomework.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.lipnin.itmohomework.client.notification.NotificationClient;
import ru.lipnin.itmohomework.dto.notification.UserSecNotificationDto;
import ru.lipnin.itmohomework.security.entity.UserRole;
import ru.lipnin.itmohomework.security.repository.ApplicationUserRepository;
import ru.lipnin.itmohomework.services.AdministrationService;

@RequiredArgsConstructor
@RestController
@Slf4j
@Validated
@RequestMapping("/api/v1/account")
public class AccountController {

    private final AdministrationService administrationService;
    private final NotificationClient notificationClient;



    @PutMapping("/changing_role")
    public ResponseEntity<Void> changeRole(@NotNull @RequestParam Long id ,
                                        @NotNull @RequestParam UserRole.RoleType role) {

        administrationService.changeUserRole(id, role);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/all_notification")
    public ResponseEntity<Void> sendAllNotification(@Valid @RequestBody UserSecNotificationDto notification) {

        notificationClient.notifySecUser(notification);
        return ResponseEntity.ok().build();
    }
    
}
