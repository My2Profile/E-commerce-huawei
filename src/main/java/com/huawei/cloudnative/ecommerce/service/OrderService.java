package com.huawei.cloudnative.ecommerce.service;

import com.huawei.cloudnative.ecommerce.model.Order;
import com.huawei.cloudnative.ecommerce.model.Product;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

@Service
public class OrderService {

    private final List<Order> orders = new ArrayList<>();
    private final AtomicLong sequence = new AtomicLong(1L);

    public synchronized Order createOrder(String username, List<Product> products) {
        double totalAmount = products.stream().mapToDouble(Product::price).sum();
        List<Long> productIds = products.stream().map(Product::id).toList();
        Order order = new Order(sequence.getAndIncrement(), username, productIds, totalAmount);
        orders.add(order);
        return order;
    }

    public synchronized List<Order> getOrdersByUsername(String username) {
        return orders.stream().filter(order -> order.username().equals(username)).toList();
    }
}
