package ru.lipnin.itmohomework.client.notification.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import ru.lipnin.itmohomework.config.FeignConfiguration;
import ru.lipnin.itmohomework.dto.notification.GeneralNotificationDto;
import ru.lipnin.itmohomework.dto.notification.UserNotificationDto;
import ru.lipnin.itmohomework.dto.notification.UserSecNotificationDto;

@FeignClient(value = "notification",
        url = "http://localhost:8082/api/v1/notifier",
        configuration = FeignConfiguration.class
//        fallback = NotificationFeignClientFallback.class
)
@Component
public interface FeignNotifyClientInt {

    @PostMapping("/user")
    void notifyUser(@RequestBody UserNotificationDto notification);

    @PostMapping("/general")
    void notifyAllUser(@RequestBody GeneralNotificationDto notification);

    @PostMapping("/user_second")
    void notifyUser(@RequestBody UserSecNotificationDto notification);

}
