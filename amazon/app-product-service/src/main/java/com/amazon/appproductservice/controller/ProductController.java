package com.amazon.appproductservice.controller;

import com.amazon.appproductservice.payload.ProductAddDTO;
import com.amazon.appproductservice.payload.ProductDTO;
import com.amazon.appproductservice.service.ProductService;
import com.amazon.appproductservice.utils.AppConstants;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

@RestController
@RequestMapping(ProductController.BASE_PATH)
@RequiredArgsConstructor
public class ProductController {

    public static final String BASE_PATH = AppConstants.BASE_PATH + "/product";

    private final ProductService productService;

    @Value("${server.port}")
    private String port;

    @PostMapping
    public Mono<ProductDTO> add(@RequestBody ProductAddDTO productAddDTO) {
        return productService.create(productAddDTO);
    }

    @GetMapping
    public Flux<ProductDTO> list() {
        System.out.println("PORTDA ISHLADI: " + port);
        return productService.list();
    }

    @GetMapping("/{id}")
    public Mono<ProductDTO> one(@PathVariable String id) {
        return productService.byId(id);
    }

    @PostMapping("/by-ids")
    public Flux<ProductDTO> getByIds(@RequestBody List<String> productIds) {
        return productService.getProductsByIds(productIds);
    }

//bootstraps config
    @Value("${app.default.size}")
    String defSize;

    private final Environment environment;

    @GetMapping("/test-pagination")
    public Mono<String> test(@RequestParam(defaultValue = "${app.default.page}") int page,
                             @RequestParam(defaultValue = "${app.default.size}") int size) {
        System.out.println(page);
        System.out.println(size);
        return Mono.just("Page: "+page+", Size: "+size+", DEF: "+ defSize +", ENV: "+environment.getProperty("app.default.size"));
    }
}
