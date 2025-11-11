package ru.lipnin.itmohomework.client.dwh.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import ru.lipnin.itmohomework.config.DwhFeignConfiguration;
import ru.lipnin.itmohomework.dto.dwh.DwhRequestStatisticDto;
import ru.lipnin.itmohomework.dto.dwh.DwhResponseDto;

@FeignClient(value = "dwh",
//        url = "http://localhost:8083/api/v1/dwh",
        url = "http://localhost:8082/api/v1/notifier",
        configuration = DwhFeignConfiguration.class
)
@Component
public interface FeignDwhClientInt {

    @PostMapping("/statistic")
    DwhResponseDto putStatistic(@RequestBody DwhRequestStatisticDto requestStatisticDto);
}
