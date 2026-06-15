package com.practiceproj.main.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import java.util.List;

public record OrderRequest(
        @NotBlank(message = "Имя клиента не должно быть пустым") String customerName,
        @NotEmpty(message = "Список товаров не должен быть пустым") List<@Valid OrderItemRequest> items
) {}
