package com.relgs.niveis_isolamento_sistemas_distribuidos.repositories;

import com.relgs.niveis_isolamento_sistemas_distribuidos.models.OrderDetail;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderDetailRepository extends JpaRepository<OrderDetail, Long> {
}