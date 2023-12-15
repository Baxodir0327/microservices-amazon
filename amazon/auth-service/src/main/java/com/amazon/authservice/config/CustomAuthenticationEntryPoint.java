package com.amazon.authservice.config;

import com.amazon.authservice.payload.ApiResult;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.server.ServerAuthenticationEntryPoint;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;


public class CustomAuthenticationEntryPoint implements ServerAuthenticationEntryPoint {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public Mono<Void> commence(ServerWebExchange exchange, AuthenticationException ex) {
        ServerHttpResponse response = exchange.getResponse();
        response.setStatusCode(HttpStatus.UNAUTHORIZED);
        response.getHeaders().setContentType(MediaType.APPLICATION_JSON);

        ApiResult apiResult = new ApiResult(false, "Custom message: Authentication required.");
        DataBuffer dataBuffer;
        try {
            dataBuffer = response.bufferFactory().wrap(objectMapper.writeValueAsBytes(apiResult));
        } catch (JsonProcessingException e) {
            // Handle JSON serialization error
            dataBuffer = response.bufferFactory().wrap(new byte[0]);
        }

        return response.writeWith(Flux.just(dataBuffer));
    }
}