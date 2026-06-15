package com.practiceproj.main.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record OrderItemRequest(
        @NotBlank(message = "Название товара не должно быть пустым") String productName,
        @NotNull(message = "Количество должно быть указано") @Min(value = 1, message = "Количество должно быть не меньше 1") Integer quantity,
        @NotNull(message = "Цена должна быть указана") @DecimalMin(value = "0.01", message = "Цена должна быть больше 0") BigDecimal price
) {}