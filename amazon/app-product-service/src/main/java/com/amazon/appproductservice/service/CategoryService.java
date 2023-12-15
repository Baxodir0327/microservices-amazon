package com.amazon.appproductservice.service;

import com.amazon.appproductservice.collection.Category;
import com.amazon.appproductservice.payload.CategoryDTO;
import com.amazon.appproductservice.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepository;

    public Mono<CategoryDTO> create(Category category) {
        System.out.println("CREATY");
        Mono<Category> categoryMono = categoryRepository.save(category);
        return categoryMono.map(cat -> mapCategoryDTO(category));
    }

    public Flux<CategoryDTO> list() {
        return categoryRepository.findAll().map(this::mapCategoryDTO);
    }

    public Mono<CategoryDTO> byId(String id) {
        return categoryRepository.findById(id).map(this::mapCategoryDTO);
    }

    private CategoryDTO mapCategoryDTO(Category category) {
        return new CategoryDTO(category.getId(), category.getName());
    }

}
