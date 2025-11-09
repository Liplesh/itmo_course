package ru.lipnin.itmohomework.feign;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.lipnin.itmohomework.client.notification.feign.FeignNotifyClientInt;
import ru.lipnin.itmohomework.dto.notification.GeneralNotificationDto;
import ru.lipnin.itmohomework.dto.notification.UserNotificationDto;
import ru.lipnin.itmohomework.dto.notification.UserSecNotificationDto;

@Component
@Slf4j
public class NotificationFeignClientFallback implements FeignNotifyClientInt {
    @Override
    public void notifyUser(UserNotificationDto notification) {
        log.info("Сервис не работает, пишу лог в методе notifyUser");
    }

    @Override
    public void notifyAllUser(GeneralNotificationDto notification) {
        log.info("Сервис не работает, пишу лог в методе notifyAllUser");
    }

    @Override
    public void notifyUser(UserSecNotificationDto notification) {
        log.info("Сервис не работает, пишу лог в методе notifyUser");
    }
}
