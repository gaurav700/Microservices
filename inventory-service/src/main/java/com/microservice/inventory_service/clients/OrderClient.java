package com.microservice.inventory_service.clients;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

@FeignClient(name="order-service", path = "/orders")
public interface OrderClient {

    @GetMapping("/core/hello")
    public String helloOrders();

}
