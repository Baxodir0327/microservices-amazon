package com.amazon.apporderservice.payload;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OrderItemDTO {

    private String productId;

    private String productName;

    private Integer quantity;

    private Double unitPrice;
}
