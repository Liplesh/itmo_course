package ru.lipnin.itmohomework.config;

import feign.Retryer;
import feign.codec.ErrorDecoder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FeignConfiguration {
    @Bean
    public Retryer feignRetryer() {
        return new Retryer.Default(
                1000, // период между попытками (ms)
                5000, // максимальный период между попытками (ms)
                3     // максимальное количество попыток
        );
    }

    @Bean
    public ErrorDecoder feignErrorDecoder() {
        return new ErrorDecoder.Default();
    }
}
