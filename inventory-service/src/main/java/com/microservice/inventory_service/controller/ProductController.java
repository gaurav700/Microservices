package com.microservice.inventory_service.controller;

import com.microservice.inventory_service.clients.OrderClient;
import com.microservice.inventory_service.dto.OrderRequestDTO;
import com.microservice.inventory_service.dto.ProductDTO;
import com.microservice.inventory_service.service.ProductService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestClient;

import java.util.List;

@RefreshScope
@RequiredArgsConstructor
@RestController
@Slf4j
@RequestMapping("/products")
public class ProductController {

    private final ProductService productService;
    private final DiscoveryClient discoveryClient;
//    private final RestClient restClient;
    private final OrderClient orderClient;

    @GetMapping("/fetchOrders")
    public String fetchOrdersFromOrderService(){
        ServiceInstance orderInstance = discoveryClient.getInstances("order-service").get(0);

//        return restClient.get()
//                .uri(orderInstance.getUri()+"/orders/core/hello")
//                .retrieve()
//                .body(String.class);

        return orderClient.helloOrders();
    }

    @GetMapping
    public ResponseEntity<List<ProductDTO>> getAllInventory(){
        List<ProductDTO> inventory = productService.getAllInventory();
        return ResponseEntity.ok(inventory);
    }

    @PostMapping("/{id}")
    public ResponseEntity<ProductDTO> getInventoryById(@PathVariable Long id){
        ProductDTO inventory = productService.getProductById(id);
        return ResponseEntity.ok(inventory);
    }

    @PutMapping("reduce-stocks")
    public ResponseEntity<Double> reduceStocks(@RequestBody OrderRequestDTO orderRequestDTO){
        return ResponseEntity.ok(productService.reduceStocks(orderRequestDTO));
    }







}
