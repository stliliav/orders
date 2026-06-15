package com.practiceproj.main.dto;

import java.math.BigDecimal;
import java.util.List;

public record OrderRequest(String customerName, List<OrderItemRequest> items) {}

