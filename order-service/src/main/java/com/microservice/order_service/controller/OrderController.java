package com.microservice.order_service.controller;

import com.microservice.order_service.clients.InventoryClient;
import com.microservice.order_service.dto.OrderRequestDTO;
import com.microservice.order_service.dto.OrderRequestItemDTO;
import com.microservice.order_service.service.OrderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@Slf4j
@RequestMapping("/core")
public class OrderController {
    private final OrderService orderService;

    @GetMapping("/hello")
    public String helloOrders(){
        return "Hello Orders from orders-service";
    }


    @GetMapping
    public ResponseEntity<List<OrderRequestDTO>> getAllInventory(){
        List<OrderRequestDTO> inventory = orderService.getAllInventory();
        return ResponseEntity.ok(inventory);
    }

    @PostMapping("/{id}")
    public ResponseEntity<OrderRequestDTO> getInventoryById(@PathVariable Long id){
        OrderRequestDTO inventory = orderService.getProductById(id);
        return ResponseEntity.ok(inventory);
    }

    @PostMapping("/create-order")
    public ResponseEntity<OrderRequestDTO> createOrder(@RequestBody OrderRequestDTO orderRequestDTO){
        OrderRequestDTO orderRequestDTO1 = orderService.createOrder(orderRequestDTO);
        return ResponseEntity.ok(orderRequestDTO1);
    }
}
