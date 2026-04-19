package com.huawei.cloudnative.ecommerce.repository;

import com.huawei.cloudnative.ecommerce.entity.OrderEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderRepository extends JpaRepository<OrderEntity, Long> {
    List<OrderEntity> findByUserUsernameOrderByCreatedAtDesc(String username);
}
