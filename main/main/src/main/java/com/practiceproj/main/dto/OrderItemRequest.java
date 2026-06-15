package com.practiceproj.main.dto;

import java.math.BigDecimal;

public record OrderItemRequest(String productName, Integer quantity, BigDecimal price) {}
