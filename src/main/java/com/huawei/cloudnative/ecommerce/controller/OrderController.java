package com.huawei.cloudnative.ecommerce.controller;

import com.huawei.cloudnative.ecommerce.dto.CreateOrderRequest;
import com.huawei.cloudnative.ecommerce.model.Order;
import com.huawei.cloudnative.ecommerce.model.Product;
import com.huawei.cloudnative.ecommerce.service.OrderService;
import com.huawei.cloudnative.ecommerce.service.ProductService;
import com.huawei.cloudnative.ecommerce.service.TokenService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    private static final String AUTH_HEADER = "Authorization";
    private static final String BEARER_PREFIX = "Bearer ";

    private final TokenService tokenService;
    private final ProductService productService;
    private final OrderService orderService;

    public OrderController(TokenService tokenService, ProductService productService, OrderService orderService) {
        this.tokenService = tokenService;
        this.productService = productService;
        this.orderService = orderService;
    }

    @PostMapping
    public ResponseEntity<Order> createOrder(@RequestHeader(AUTH_HEADER) String authorization,
                                             @Valid @RequestBody CreateOrderRequest request) {
        Optional<String> username = resolveUsername(authorization);
        if (username.isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        List<Product> products = productService.findByIds(request.productIds());
        if (products.isEmpty()) {
            return ResponseEntity.badRequest().build();
        }

        return ResponseEntity.status(HttpStatus.CREATED).body(orderService.createOrder(username.get(), products));
    }

    @GetMapping
    public ResponseEntity<List<Order>> listOrders(@RequestHeader(AUTH_HEADER) String authorization) {
        Optional<String> username = resolveUsername(authorization);
        if (username.isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        return ResponseEntity.ok(orderService.getOrdersByUsername(username.get()));
    }

    private Optional<String> resolveUsername(String authorization) {
        if (authorization == null || !authorization.startsWith(BEARER_PREFIX)) {
            return Optional.empty();
        }
        String token = authorization.substring(BEARER_PREFIX.length());
        return tokenService.resolveUsername(token);
    }
}
