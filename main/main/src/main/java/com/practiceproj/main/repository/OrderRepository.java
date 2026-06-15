package com.practiceproj.main.repository;

import com.practiceproj.main.entity.Order;
import com.practiceproj.main.entity.OrderStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.util.UUID;

public interface OrderRepository extends JpaRepository<Order, UUID> {

    Page<Order> findByStatus(OrderStatus status, Pageable pageable);

    @Query("SELECT SUM(i.price * i.quantity) FROM Order o JOIN o.items i WHERE o.customerName = :customerName")
    BigDecimal calculateTotalAmountByCustomer(@Param("customerName") String customerName);
}