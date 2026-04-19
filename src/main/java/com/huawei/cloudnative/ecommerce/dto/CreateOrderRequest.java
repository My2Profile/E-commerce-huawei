package com.huawei.cloudnative.ecommerce.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public record CreateOrderRequest(
        @NotNull(message = "productIds is required")
        @NotEmpty(message = "productIds must not be empty")
        List<Long> productIds
) {
}
