package com.amazon.apporderservice.config;

import com.amazon.apporderservice.payload.ProductDTO;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.service.annotation.HttpExchange;
import org.springframework.web.service.annotation.PostExchange;
import reactor.core.publisher.Flux;

import java.util.Collection;

@HttpExchange
public interface ProductClient {

    @PostExchange("/api/v1/product/product/by-ids")
    Flux<ProductDTO> getProductsByIds(@RequestBody Collection<String> ids);
}
