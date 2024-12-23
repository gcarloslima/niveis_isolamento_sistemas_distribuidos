package com.relgs.niveis_isolamento_sistemas_distribuidos.repositories;

import com.relgs.niveis_isolamento_sistemas_distribuidos.models.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Long> {
}