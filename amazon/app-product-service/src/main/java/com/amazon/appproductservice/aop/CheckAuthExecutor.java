package com.amazon.appproductservice.aop;

import com.amazon.appproductservice.redis.CacheService;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.http.HttpHeaders;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.Arrays;
import java.util.Set;

@Aspect
@Component
public class CheckAuthExecutor {
    private final CacheService cacheService;

    public CheckAuthExecutor(CacheService cacheService) {
        this.cacheService = cacheService;
    }

    @Before("@annotation(checkAuth)")
    public Mono<Void> checkAuthExecutor(CheckAuth checkAuth) {
        return getServerHttpRequest().flatMap(serverHttpRequest -> {
            System.out.println("Qalay:");
            String authorization = serverHttpRequest.getHeaders().getFirst(HttpHeaders.AUTHORIZATION);
            if (authorization == null) {
                System.out.println(authorization);
                return Mono.error(RuntimeException::new);
            }
            return cacheService.getUserMe(authorization)
                    .flatMap(userDTO -> {
                        if (!hasPermissions(checkAuth.permissions(), userDTO.getPermissions()))
                            return Mono.error(RuntimeException::new);
                        return Mono.empty();
                    });
        });
    }

    public static Mono<ServerHttpRequest> getServerHttpRequest() {
//        return Mono.deferContextual(Mono::just)
//                .map(contextView -> contextView.get(ServerWebExchange.class).getRequest());
        return Mono.deferContextual(contextView ->
                Mono.justOrEmpty(contextView.get(ServerWebExchange.class))
                        .map(ServerWebExchange::getRequest)
        );
    }

    public static final Class<ServerHttpRequest> CONTEXT_KEY = ServerHttpRequest.class;

    private static boolean hasPermissions(String[] permissions1, Set<String> permissions) {
        System.out.println("permissions1 = " + Arrays.toString(permissions1));
        System.out.println("permissions = " + permissions);
        return Arrays.stream(permissions1).allMatch(permissions::contains);
    }
}