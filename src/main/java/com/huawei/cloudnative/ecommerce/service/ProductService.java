package com.huawei.cloudnative.ecommerce.service;

import com.huawei.cloudnative.ecommerce.model.Product;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductService {

    private final List<Product> products = List.of(
            new Product(1L, "Huawei Watch", "Smart watch for fitness and productivity", 1999.0),
            new Product(2L, "Wireless Earbuds", "Noise cancellation earbuds", 799.0),
            new Product(3L, "MateBook", "Lightweight performance laptop", 6999.0)
    );

    public List<Product> getAllProducts() {
        return products;
    }

    public List<Product> findByIds(List<Long> ids) {
        return products.stream().filter(p -> ids.contains(p.id())).toList();
    }
}
