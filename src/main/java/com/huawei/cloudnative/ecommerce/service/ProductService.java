package com.huawei.cloudnative.ecommerce.service;

import com.huawei.cloudnative.ecommerce.entity.ProductEntity;
import com.huawei.cloudnative.ecommerce.model.Product;
import com.huawei.cloudnative.ecommerce.repository.ProductRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductService {

    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public List<Product> getAllProducts() {
        return productRepository.findAll().stream().map(this::toModel).toList();
    }

    public List<ProductEntity> findEntitiesByIds(List<Long> ids) {
        return productRepository.findAllById(ids);
    }

    private Product toModel(ProductEntity productEntity) {
        return new Product(productEntity.getId(), productEntity.getName(), productEntity.getDescription(), productEntity.getPrice());
    }
}
