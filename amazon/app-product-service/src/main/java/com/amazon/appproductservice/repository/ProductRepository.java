package com.amazon.appproductservice.repository;

import com.amazon.appproductservice.collection.Category;
import com.amazon.appproductservice.collection.Product;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Collection;

@Repository
public interface ProductRepository extends ReactiveMongoRepository<Product, String> {

}
