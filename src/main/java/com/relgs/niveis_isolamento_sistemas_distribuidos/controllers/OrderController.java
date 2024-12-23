package com.relgs.niveis_isolamento_sistemas_distribuidos.controllers;

import com.relgs.niveis_isolamento_sistemas_distribuidos.dtos.OrderRequestDto;
import com.relgs.niveis_isolamento_sistemas_distribuidos.services.OrderService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping("/pedido/novo")
    public ResponseEntity<OrderRequestDto> createOrderWithoutLocks(@RequestBody OrderRequestDto orderRequestDto) {
        return ResponseEntity.ok(orderService.createOrderWithoutLocks(orderRequestDto));
    }

    @PostMapping("/pedido_otimista/novo")
    public ResponseEntity<OrderRequestDto> createOrderWithOptimisticLock(@RequestBody OrderRequestDto orderRequestDto) {
        return ResponseEntity.ok(orderService.createOrderWithOptimisticLock(orderRequestDto));
    }

    @PostMapping("/pedido_pessimista/novo")
    public ResponseEntity<OrderRequestDto> createOrderWithPessimisticLock(@RequestBody OrderRequestDto orderRequestDto) {
        return ResponseEntity.ok(orderService.createOrderWithPessimisticLock(orderRequestDto));
    }

}
