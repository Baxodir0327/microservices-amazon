package com.amazon.appnotificationservice;

import com.amazon.appnotificationservice.config.CacheConfigurationProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
@EnableConfigurationProperties(value = {CacheConfigurationProperties.class})
public class AppNotificationServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(AppNotificationServiceApplication.class, args);
    }

}
