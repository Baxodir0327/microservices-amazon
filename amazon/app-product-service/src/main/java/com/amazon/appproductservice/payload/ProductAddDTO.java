package com.amazon.appproductservice.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductAddDTO {

    private String name;

    private Double price;

    private String categoryId;
}
