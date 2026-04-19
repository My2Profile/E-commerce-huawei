package com.huawei.cloudnative.ecommerce.model;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.List;

public record Order(Long id, String username, List<Long> productIds, BigDecimal totalAmount, OffsetDateTime createdAt) {
}
