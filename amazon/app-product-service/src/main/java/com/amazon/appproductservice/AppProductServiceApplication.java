package com.amazon.appproductservice;

import com.amazon.appproductservice.exceptions.RestException;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.Bean;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebExceptionHandler;
import reactor.core.publisher.Mono;

@SpringBootApplication
@EnableDiscoveryClient
public class AppProductServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(AppProductServiceApplication.class, args);
    }

    @Bean
    public WebExceptionHandler exceptionHandler() {
        return (ServerWebExchange exchange, Throwable ex) -> {
            if (ex instanceof RestException) {
                exchange.getResponse().setStatusCode(((RestException) ex).getStatus());
                return exchange.getResponse().setComplete();
            }
            return Mono.error(ex);
        };
    }
}

