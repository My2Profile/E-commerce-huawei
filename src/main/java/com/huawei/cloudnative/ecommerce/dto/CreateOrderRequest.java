package com.huawei.cloudnative.ecommerce.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public record CreateOrderRequest(
        @NotNull(message = "items is required")
        @NotEmpty(message = "items must not be empty")
        List<@Valid OrderItemRequest> items
) {
}
