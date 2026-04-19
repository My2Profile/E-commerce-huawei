package com.huawei.cloudnative.ecommerce.model;

import java.util.List;

public record Order(Long id, String username, List<Long> productIds, double totalAmount) {
}
