package com.ing.brokage.persistance.repository;

import com.ing.brokage.persistance.entity.Customer;
import com.ing.brokage.persistance.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {

    List<Order> findAllByCustomerAndCreatedAtBetween(Customer customer, LocalDateTime startDate, LocalDateTime endDate);
}
