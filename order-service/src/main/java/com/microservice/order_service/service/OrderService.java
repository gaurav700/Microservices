package com.microservice.order_service.service;

import com.microservice.order_service.clients.InventoryClient;
import com.microservice.order_service.dto.OrderRequestDTO;
import com.microservice.order_service.dto.OrderRequestItemDTO;
import com.microservice.order_service.entity.OrderEntity;
import com.microservice.order_service.entity.OrderItemEntity;
import com.microservice.order_service.entity.OrderStatus;
import com.microservice.order_service.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
@Slf4j
public class OrderService {

    private final OrderRepository orderRepository;
    private final ModelMapper modelMapper;
    private final InventoryClient inventoryClient;


    public List<OrderRequestDTO> getAllInventory(){
        log.info("Fetching all orders");
        List<OrderEntity> inventory = orderRepository.findAll();
        return inventory.stream()
                .map(productEntity -> modelMapper.map(productEntity, OrderRequestDTO.class))
                .toList();
    }

    public OrderRequestDTO getProductById(Long id){
        log.info("Fetching order with ID : {}", id);
        Optional<OrderEntity> inventory = orderRepository.findById(id);
        return inventory.map(item -> modelMapper.map(item, OrderRequestDTO.class))
                .orElseThrow(() -> new RuntimeException(String.format("order not found with ID : %d", id)));
    }


    public OrderRequestDTO createOrder(OrderRequestDTO orderRequestDTO) {
        Double totalPrice = inventoryClient.reduceStocks(orderRequestDTO);
        OrderEntity orderEntity = modelMapper.map(orderRequestDTO, OrderEntity.class);
        for(OrderItemEntity orderItemEntity : orderEntity.getItems()){
            orderItemEntity.setOrder(orderEntity);
        }
        orderEntity.setPrice(totalPrice);
        orderEntity.setOrderStatus(OrderStatus.CONFIRMED);
        OrderEntity order = orderRepository.save(orderEntity);

        return modelMapper.map(order, OrderRequestDTO.class);
    }
}
