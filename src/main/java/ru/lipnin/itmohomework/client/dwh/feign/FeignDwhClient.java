package ru.lipnin.itmohomework.client.dwh.feign;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import ru.lipnin.itmohomework.client.dwh.DwhClient;
import ru.lipnin.itmohomework.dto.dwh.DwhRequestStatisticDto;
import ru.lipnin.itmohomework.dto.dwh.DwhResponseDto;
import ru.lipnin.itmohomework.dto.notification.GeneralNotificationDto;
import ru.lipnin.itmohomework.dto.notification.UserNotificationDto;
import ru.lipnin.itmohomework.dto.notification.UserSecNotificationDto;
import ru.lipnin.itmohomework.security.entity.ApplicationUser;

@Profile("feign")
@Slf4j
@RequiredArgsConstructor
@Component
public class FeignDwhClient implements DwhClient {

    private final FeignDwhClientInt feignDwhClientInt;

    @Override
    public DwhResponseDto putStatistic(DwhRequestStatisticDto requestStatisticDto) {
        DwhResponseDto dwhResponseDto = feignDwhClientInt.putStatistic(requestStatisticDto);
        log.info("DwhResponseDto: {}", dwhResponseDto);
        return dwhResponseDto;
    }

}
