package ru.lipnin.itmohomework.client.dwh;

import ru.lipnin.itmohomework.dto.dwh.DwhRequestStatisticDto;
import ru.lipnin.itmohomework.dto.dwh.DwhResponseDto;
import ru.lipnin.itmohomework.dto.notification.UserSecNotificationDto;
import ru.lipnin.itmohomework.security.entity.ApplicationUser;

public interface DwhClient {

    DwhResponseDto putStatistic(DwhRequestStatisticDto dwhRequestStatisticDto);
}
