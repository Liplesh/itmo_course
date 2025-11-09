package ru.lipnin.itmohomework.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import ru.lipnin.itmohomework.client.notification.NotificationClient;
import ru.lipnin.itmohomework.client.notification.feign.FeignNotificationClient;
import ru.lipnin.itmohomework.client.notification.rest.RestNotificationClient;

import java.util.concurrent.Executor;

@Configuration
public class BeautyServiceConfiguration {

    @Bean("dwh-executor")
    public Executor dwhExecutor(){
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(1);
        executor.setMaxPoolSize(5);
        executor.setQueueCapacity(20);
        executor.setKeepAliveSeconds(60);
        executor.setThreadNamePrefix("DWH-EXECUTOR-");
        executor.initialize();
        return executor;
    }

    // Бин для профиля "feign"
//    @Bean
//    @Profile("feign")
//    public NotificationClient feignNotificationClient() {
//        System.out.println("Feign профиль АКТИВЕН! Создаю FeignNotificationClient");
//        return new FeignNotificationClient();
//    }

    // Бин для профиля "rest"
//    @Bean
//    @Profile("rest")
//    public NotificationClient restNotificationClient() {
//        System.out.println("Rest профиль АКТИВЕН! Создаю RestNotificationClient");
//        return new RestNotificationClient();
//    }


}
