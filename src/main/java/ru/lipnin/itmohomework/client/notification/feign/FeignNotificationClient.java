package ru.lipnin.itmohomework.client.notification.feign;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import ru.lipnin.itmohomework.client.notification.NotificationClient;
import ru.lipnin.itmohomework.dto.notification.GeneralNotificationDto;
import ru.lipnin.itmohomework.dto.notification.UserNotificationDto;
import ru.lipnin.itmohomework.dto.notification.UserSecNotificationDto;
import ru.lipnin.itmohomework.security.entity.ApplicationUser;

@Profile("feign")
@Slf4j
@RequiredArgsConstructor
@Component
public class FeignNotificationClient implements NotificationClient {

    private final FeignNotifyClientInt feignNotifyClientInt;

    @Override
    public void notifyUser(ApplicationUser user, String message) {
        feignNotifyClientInt.notifyUser(new UserNotificationDto(message, user.getId()));
    }

    @Override
    public void notifyAllUser(String message) {
        feignNotifyClientInt.notifyAllUser(new GeneralNotificationDto(message));
    }


    //Ниже костыли для проверки, так как не знаю пока как прокидывать токен
    @Override
    public void notifySecUser(UserSecNotificationDto userSecNotificationDto) {
        feignNotifyClientInt.notifyUser(new UserSecNotificationDto(userSecNotificationDto.message(), userSecNotificationDto.email()));
    }



}
