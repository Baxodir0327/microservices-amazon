package com.amazon.apporderservice.payload;

import lombok.Getter;

@Getter
public class OrderAddDTO {

    private String productId;

    private Integer count;
}
