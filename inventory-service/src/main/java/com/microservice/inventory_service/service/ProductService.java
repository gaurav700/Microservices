package com.microservice.inventory_service.service;

import com.microservice.inventory_service.dto.OrderRequestDTO;
import com.microservice.inventory_service.dto.OrderRequestItemDTO;
import com.microservice.inventory_service.dto.ProductDTO;
import com.microservice.inventory_service.entity.ProductEntity;
import com.microservice.inventory_service.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
@Slf4j
public class ProductService {

    private final ProductRepository productRepository;
    private final ModelMapper modelMapper;


    public List<ProductDTO> getAllInventory(){
        log.info("Fetching all inventory items");
        List<ProductEntity> inventory = productRepository.findAll();
        return inventory.stream()
                .map(productEntity -> modelMapper.map(productEntity, ProductDTO.class))
                .toList();
    }

    public ProductDTO getProductById(Long id){
        log.info("Fetching product with ID : {}", id);
        Optional<ProductEntity> inventory = productRepository.findById(id);
        return inventory.map(item-> modelMapper.map(item, ProductDTO.class))
                .orElseThrow(()-> new RuntimeException("Inventory not found with ID : %d"+id));
    }

    @Transactional
    public Double reduceStocks(OrderRequestDTO orderRequestDTO) {
        log.info("Reducing the stocks");
        Double totalPrice = 0.0;

        for(OrderRequestItemDTO orderRequestItemDTO : orderRequestDTO.getItems()){
            Long productId = orderRequestItemDTO.getProductId();
            Integer quantity = orderRequestItemDTO.getQuantity();

            ProductEntity product = productRepository.findById(productId).orElseThrow(()-> new RuntimeException("Product with the given id not exists"));

            if(product.getStock() < quantity){
                throw  new RuntimeException("Product cannot be fulfilled for given quantity");
            }

            product.setStock(product.getStock()-quantity);
            productRepository.save(product);

            totalPrice = quantity*product.getPrice();
        }
        return totalPrice;
    }
}
