package com.amazon.appproductservice.mapper;

import com.amazon.appproductservice.collection.Product;
import com.amazon.appproductservice.payload.ProductAddDTO;
import com.amazon.appproductservice.payload.ProductDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ProductMapper {


    Product toProduct(ProductAddDTO productAddDTO);

    @Mapping(target = "category",ignore = true)
    ProductDTO toProductDTO(Product pro);
}
