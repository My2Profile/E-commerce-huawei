package com.huawei.cloudnative.ecommerce.service;

import com.huawei.cloudnative.ecommerce.entity.AppUser;
import com.huawei.cloudnative.ecommerce.entity.OrderEntity;
import com.huawei.cloudnative.ecommerce.entity.OrderItemEntity;
import com.huawei.cloudnative.ecommerce.entity.ProductEntity;
import com.huawei.cloudnative.ecommerce.model.Order;
import com.huawei.cloudnative.ecommerce.repository.AppUserRepository;
import com.huawei.cloudnative.ecommerce.repository.OrderRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.List;

@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final AppUserRepository appUserRepository;

    public OrderService(OrderRepository orderRepository, AppUserRepository appUserRepository) {
        this.orderRepository = orderRepository;
        this.appUserRepository = appUserRepository;
    }

    public Order createOrder(String username, List<ProductEntity> products) {
        AppUser user = appUserRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        OrderEntity orderEntity = new OrderEntity();
        orderEntity.setUser(user);
        orderEntity.setCreatedAt(OffsetDateTime.now());

        BigDecimal totalAmount = products.stream()
                .map(ProductEntity::getPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        orderEntity.setTotalAmount(totalAmount);

        products.forEach(product -> {
            OrderItemEntity item = new OrderItemEntity();
            item.setOrder(orderEntity);
            item.setProduct(product);
            item.setQuantity(1);
            item.setUnitPrice(product.getPrice());
            orderEntity.getItems().add(item);
        });

        return toModel(orderRepository.save(orderEntity));
    }

    public List<Order> getOrdersByUsername(String username) {
        return orderRepository.findByUserUsernameOrderByCreatedAtDesc(username).stream().map(this::toModel).toList();
    }

    private Order toModel(OrderEntity orderEntity) {
        List<Long> productIds = orderEntity.getItems().stream().map(item -> item.getProduct().getId()).toList();
        return new Order(
                orderEntity.getId(),
                orderEntity.getUser().getUsername(),
                productIds,
                orderEntity.getTotalAmount(),
                orderEntity.getCreatedAt()
        );
    }
}
