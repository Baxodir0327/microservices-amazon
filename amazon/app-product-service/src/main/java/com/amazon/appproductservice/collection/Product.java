package com.amazon.appproductservice.collection;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
@Data
public class Product {

    @Id
    private String id;

    @Indexed(unique = true, sparse = true)
    private String name;

    private Double price;

    private String categoryId;
}
