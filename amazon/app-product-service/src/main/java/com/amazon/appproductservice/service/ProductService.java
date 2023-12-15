package com.amazon.appproductservice.service;

import com.amazon.appproductservice.collection.Category;
import com.amazon.appproductservice.collection.Product;
import com.amazon.appproductservice.exceptions.RestException;
import com.amazon.appproductservice.mapper.ProductMapper;
import com.amazon.appproductservice.payload.CategoryDTO;
import com.amazon.appproductservice.payload.ProductAddDTO;
import com.amazon.appproductservice.payload.ProductDTO;
import com.amazon.appproductservice.repository.CategoryRepository;
import com.amazon.appproductservice.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.function.Function;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final ProductMapper productMapper;

    public Mono<ProductDTO> create(ProductAddDTO productAddDTO) {
        Product product = productMapper.toProduct(productAddDTO);
        return productRepository.save(product).map(productMapper::toProductDTO);
//        return categoryRepository.findById(productAddDTO.getCategoryId())
//                .flatMap(category -> {
//                    product.setCategory(category);
//                    return productRepository.save(product);
//                })
//                .map(productMapper::toProductDTO)
//                .switchIfEmpty(Mono.error(new RestException("Category not found", HttpStatus.NOT_FOUND)));
    }


    public Flux<ProductDTO> list() {
        return productRepository.findAll()
                .map(productMapper::toProductDTO);
    }

    public Mono<ProductDTO> byId(String id) {
        return productRepository.findById(id)
                .map(productMapper::toProductDTO);
    }

    public Flux<ProductDTO> getProductsByIds(List<String> productIds) {
        System.out.println(productIds);
        return productRepository.findAllById(productIds)
                .map(productMapper::toProductDTO);
    }
}
