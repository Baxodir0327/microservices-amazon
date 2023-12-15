package com.amazon.appproductservice.redis;

import com.amazon.appproductservice.config.AuthClient;
import com.amazon.appproductservice.payload.UserDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class CacheService {

    private final AuthClient authClient;


    @Cacheable(value = "auth-user",key = "#authorization")
    public Mono<UserDTO> getUserMe(String authorization) {
        return authClient.getUserMe(authorization);
    }
}
