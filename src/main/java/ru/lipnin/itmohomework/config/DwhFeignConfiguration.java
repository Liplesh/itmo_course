package ru.lipnin.itmohomework.config;

import feign.Retryer;
import feign.codec.Decoder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;
import ru.lipnin.itmohomework.feign.dwh.DwhDecoder;
import ru.lipnin.itmohomework.feign.dwh.DwhResponseRetryer;

@Configuration
@Profile("feign")
public class DwhFeignConfiguration {

    @Bean
    @Primary
    public Retryer dwhFeignRetryer() {
        return new DwhResponseRetryer(1000, 5000, 3);
    }

    @Bean
    @Primary
    public Decoder dwhFeignDecoder() {
        return new DwhDecoder();
    }
}
