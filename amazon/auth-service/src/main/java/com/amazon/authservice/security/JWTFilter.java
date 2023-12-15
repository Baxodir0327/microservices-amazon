package com.amazon.authservice.security;

import com.amazon.authservice.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class JWTFilter implements WebFilter {

    private final JWTProvider jwtProvider;
    private final AuthService authService;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        String authHeader = exchange.getRequest().getHeaders().getFirst(HttpHeaders.AUTHORIZATION);

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String jwt = authHeader.substring(7);
            System.out.println(jwt);
            return jwtProvider.extractUserId(jwt)
                    .flatMap(userId -> authService.findById(userId)
                            .flatMap(userPrincipal -> {
                                System.out.println(userPrincipal);
                                UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(
                                        userPrincipal, null, userPrincipal.getAuthorities());
                                return chain.filter(exchange).contextWrite(ReactiveSecurityContextHolder.withAuthentication(auth));
                            })
                            .switchIfEmpty(chain.filter(exchange))
                            .onErrorResume(e -> Mono.empty()));
        }

        return chain.filter(exchange);
    }

}
