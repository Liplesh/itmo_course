package ru.lipnin.itmohomework.client.dwh.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import ru.lipnin.itmohomework.config.FeignConfiguration;
import ru.lipnin.itmohomework.dto.dwh.DwhRequestStatisticDto;
import ru.lipnin.itmohomework.dto.dwh.DwhResponseDto;
import ru.lipnin.itmohomework.dto.notification.GeneralNotificationDto;
import ru.lipnin.itmohomework.dto.notification.UserNotificationDto;
import ru.lipnin.itmohomework.dto.notification.UserSecNotificationDto;

import java.util.List;

@FeignClient(value = "dwh",
        url = "http://localhost:8083/api/v1/dwh",
        configuration = FeignConfiguration.class
)
@Component
public interface FeignDwhClientInt {

    @PostMapping("/statistic")
    DwhResponseDto putStatistic(@RequestBody DwhRequestStatisticDto requestStatisticDto);
}
