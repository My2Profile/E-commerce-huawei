package com.huawei.cloudnative.ecommerce.service;

import com.huawei.cloudnative.ecommerce.dto.OrderItemRequest;
import com.huawei.cloudnative.ecommerce.entity.AppUser;
import com.huawei.cloudnative.ecommerce.entity.OrderEntity;
import com.huawei.cloudnative.ecommerce.entity.OrderItemEntity;
import com.huawei.cloudnative.ecommerce.entity.ProductEntity;
import com.huawei.cloudnative.ecommerce.model.Order;
import com.huawei.cloudnative.ecommerce.repository.AppUserRepository;
import com.huawei.cloudnative.ecommerce.repository.OrderRepository;
import com.huawei.cloudnative.ecommerce.repository.ProductRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.List;

@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final AppUserRepository appUserRepository;
    private final ProductRepository productRepository;

    public OrderService(OrderRepository orderRepository,
                        AppUserRepository appUserRepository,
                        ProductRepository productRepository) {
        this.orderRepository = orderRepository;
        this.appUserRepository = appUserRepository;
        this.productRepository = productRepository;
    }

    @Transactional
    public Order createOrder(String username, List<OrderItemRequest> items) {
        AppUser user = appUserRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        OrderEntity orderEntity = new OrderEntity();
        orderEntity.setUser(user);
        orderEntity.setCreatedAt(OffsetDateTime.now());

        BigDecimal totalAmount = BigDecimal.ZERO;

        for (OrderItemRequest itemRequest : items) {
            ProductEntity product = productRepository.findById(itemRequest.productId())
                    .orElseThrow(() -> new IllegalArgumentException("Product not found: " + itemRequest.productId()));

            OrderItemEntity item = new OrderItemEntity();
            item.setOrder(orderEntity);
            item.setProduct(product);
            item.setQuantity(itemRequest.quantity());
            item.setUnitPrice(product.getPrice());
            orderEntity.getItems().add(item);

            BigDecimal lineAmount = product.getPrice().multiply(BigDecimal.valueOf(itemRequest.quantity()));
            totalAmount = totalAmount.add(lineAmount);
        }

        orderEntity.setTotalAmount(totalAmount);
        return toModel(orderRepository.save(orderEntity));
    }

    @Transactional
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
