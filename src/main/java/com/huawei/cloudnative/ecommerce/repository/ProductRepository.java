package com.huawei.cloudnative.ecommerce.repository;

import com.huawei.cloudnative.ecommerce.entity.ProductEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<ProductEntity, Long> {
}
