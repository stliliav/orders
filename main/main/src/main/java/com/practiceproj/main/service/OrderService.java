package com.practiceproj.main.service;


import com.practiceproj.main.config.RabbitConfig;
import com.practiceproj.main.dto.OrderRequest;
import com.practiceproj.main.entity.Order;
import com.practiceproj.main.entity.OrderItem;
import com.practiceproj.main.entity.OrderStatus;
import com.practiceproj.main.event.OrderCreatedEvent;
import com.practiceproj.main.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Slf4j
@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final RabbitTemplate rabbitTemplate;

    @Transactional
    public Order createOrder(OrderRequest request) {
        Order order = new Order();
        order.setCustomerName(request.customerName());
        order.setOrderDate(LocalDateTime.now());
        order.setStatus(OrderStatus.CREATED);

        BigDecimal totalAmount = BigDecimal.ZERO;

        for (var itemReq : request.items()) {
            OrderItem item = new OrderItem();
            item.setProductName(itemReq.productName());
            item.setQuantity(itemReq.quantity());
            item.setPrice(itemReq.price());
            order.addItem(item);

            totalAmount = totalAmount.add(itemReq.price().multiply(BigDecimal.valueOf(itemReq.quantity())));
        }

        Order savedOrder = orderRepository.save(order);

        OrderCreatedEvent event = new OrderCreatedEvent(savedOrder.getId(), savedOrder.getCustomerName(), totalAmount); // [cite: 22]
        rabbitTemplate.convertAndSend(RabbitConfig.QUEUE_NAME, event);

        return savedOrder;
    }

    @RabbitListener(queues = RabbitConfig.QUEUE_NAME)
    @Transactional
    public void handleOrderCreated(OrderCreatedEvent event) {
        log.info("Обработка события создания заказа. ID: {}", event.orderId());
        orderRepository.findById(event.orderId()).ifPresent(order -> {
            order.setStatus(OrderStatus.PROCESSING);
            orderRepository.save(order);
        });
    }
}