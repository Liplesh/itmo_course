package ru.lipnin.itmohomework.client.notification.rest;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import ru.lipnin.itmohomework.client.notification.NotificationClient;
import ru.lipnin.itmohomework.dto.notification.GeneralNotificationDto;
import ru.lipnin.itmohomework.dto.notification.UserNotificationDto;
import ru.lipnin.itmohomework.dto.notification.UserSecNotificationDto;
import ru.lipnin.itmohomework.security.entity.ApplicationUser;

import java.util.List;

import static ru.lipnin.itmohomework.client.ClientUtils.customRequestFactory;

@Profile("rest")
@Slf4j
@Component
public class RestNotificationClient implements NotificationClient {

    private final RestClient notifierRestClient;

    public RestNotificationClient() {
        this.notifierRestClient = RestClient.builder()
                .baseUrl("http://localhost:8082/api/v1/notifier")
                .requestFactory(customRequestFactory(200, 200))
                .build();
    }

    @Override
    public void notifyUser(ApplicationUser user, String message) {
        try {
            ResponseEntity<Void> bodilessEntity = notifierRestClient.post()
                    .uri("/user")
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(new UserNotificationDto(message, user.getId()))
                    .retrieve()
                    .toBodilessEntity();
            log.info(bodilessEntity.getStatusCode().toString());
        } catch (Exception e) {
            log.info("Не доступен сервис");
        }
//                .onStatus(status -> !status.is2xxSuccessful(), (request, response) -> {
//                    log.error(response.getStatusText());
//                });
    }

    @Override
    public void notifyAllUser(String message) {
        ResponseEntity<Void> bodilessEntity = null;
        try {
            bodilessEntity = notifierRestClient.post()
                    .uri("/general")
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(new GeneralNotificationDto(message))
                    .retrieve()
                    .toBodilessEntity();
            log.info(bodilessEntity.getStatusCode().toString());
        } catch (Exception e) {
            log.info("Не доступен сервис");
        }
    }


    //Ниже костыли для проверки, так как не знаю пока как прокидывать токен
    @Override
    public void notifySecUser(UserSecNotificationDto userSecNotificationDto) {
        try {
            ResponseEntity<Void> bodilessEntity = notifierRestClient.post()
                    .uri("/user_second")
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(new UserSecNotificationDto(userSecNotificationDto.message(), userSecNotificationDto.email()))
                    .retrieve()
                    .toBodilessEntity();
            log.info(bodilessEntity.getStatusCode().toString());
        } catch (Exception e) {
            log.info("Не доступен сервис");
        }
    }
}
