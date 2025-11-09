package ru.lipnin.itmohomework.client.dwh.rest;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import ru.lipnin.itmohomework.client.dwh.DwhClient;
import ru.lipnin.itmohomework.dto.dwh.DwhRequestStatisticDto;
import ru.lipnin.itmohomework.dto.dwh.DwhResponseDto;

import static ru.lipnin.itmohomework.client.ClientUtils.customRequestFactory;

@Profile("rest")
@Slf4j
@Component
public class RestDwhClient implements DwhClient {

    private final RestClient dwhRestClient;

    public RestDwhClient() {
        this.dwhRestClient = RestClient.builder()
                .baseUrl("http://localhost:8083/api/v1/notifier")
                .requestFactory(customRequestFactory(200, 200))
                .build();
    }

    @Override
    public DwhResponseDto putStatistic(DwhRequestStatisticDto dwhRequestStatisticDto) {
        DwhResponseDto dwhResponseDto= null;
        try {
            dwhResponseDto = dwhRestClient.post()
                    .uri("/statistic")
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(dwhRequestStatisticDto)
                    .retrieve()
                            .body(DwhResponseDto.class);
            log.info("DwhResponseDto: {}", dwhResponseDto);
        } catch (Exception e) {
            log.info("Не доступен сервис");
        }
        return dwhResponseDto;
    }
}
