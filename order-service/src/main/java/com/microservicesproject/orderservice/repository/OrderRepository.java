package com.microservicesproject.orderservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.microservicesproject.orderservice.model.Order;

public interface OrderRepository extends JpaRepository<Order, Long> {

}
