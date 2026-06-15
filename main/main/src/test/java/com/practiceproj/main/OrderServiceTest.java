package com.practiceproj.main;

import com.practiceproj.main.config.RabbitConfig;
import com.practiceproj.main.dto.OrderItemRequest;
import com.practiceproj.main.dto.OrderRequest;
import com.practiceproj.main.entity.Order;
import com.practiceproj.main.event.OrderCreatedEvent;
import com.practiceproj.main.repository.OrderRepository;
import com.practiceproj.main.service.OrderService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.amqp.rabbit.core.RabbitTemplate;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class OrderServiceTest {
    @Mock
    private OrderRepository orderRepository;

    @Mock
    private RabbitTemplate rabbitTemplate;

    @InjectMocks
    private OrderService orderService;

    @Test
    void createOrder_ShouldSaveAndSendEvent() {
        OrderRequest request = new OrderRequest("Test", List.of(new OrderItemRequest("Item", 1, BigDecimal.TEN)));
        Order savedOrder = new Order();
        savedOrder.setId(UUID.randomUUID());

        when(orderRepository.save(any())).thenReturn(savedOrder);

        orderService.createOrder(request);

        verify(orderRepository).save(any());
        verify(rabbitTemplate).convertAndSend(eq(RabbitConfig.QUEUE_NAME), any(OrderCreatedEvent.class));
    }
}