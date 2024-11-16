package com.microservice.inventory_service.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class OrderRequestDTO {
    private List<OrderRequestItemDTO> items;
}
