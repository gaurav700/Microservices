package com.microservice.inventory_service.dto;

import lombok.Data;

@Data
public class OrderRequestItemDTO {
    private Long productId;
    private Integer quantity;
}
