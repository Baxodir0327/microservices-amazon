package com.amazon.appproductservice.config;

import com.amazon.appproductservice.payload.UserDTO;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.service.annotation.GetExchange;
import org.springframework.web.service.annotation.HttpExchange;
import reactor.core.publisher.Mono;

@HttpExchange
public interface AuthClient {

    @GetExchange("/api/v1/auth/user/me")
    Mono<UserDTO> getUserMe(@RequestHeader(value = "Authorization",required = false) String authorization);
}
