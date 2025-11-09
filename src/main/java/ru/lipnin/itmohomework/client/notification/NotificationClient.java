package ru.lipnin.itmohomework.client.notification;

import ru.lipnin.itmohomework.dto.notification.UserSecNotificationDto;
import ru.lipnin.itmohomework.security.entity.ApplicationUser;

public interface NotificationClient {

    void notifyUser(ApplicationUser user, String message);

    void notifyAllUser(String message);

    void notifySecUser(UserSecNotificationDto notificationDto);

}
