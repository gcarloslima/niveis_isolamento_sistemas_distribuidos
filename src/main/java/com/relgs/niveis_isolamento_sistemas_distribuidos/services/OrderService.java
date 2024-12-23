package com.relgs.niveis_isolamento_sistemas_distribuidos.services;

import com.relgs.niveis_isolamento_sistemas_distribuidos.dtos.OrderRequestDto;
import com.relgs.niveis_isolamento_sistemas_distribuidos.exceptions.InsufficientStockException;
import com.relgs.niveis_isolamento_sistemas_distribuidos.models.Client;
import com.relgs.niveis_isolamento_sistemas_distribuidos.models.Order;
import com.relgs.niveis_isolamento_sistemas_distribuidos.models.OrderDetail;
import com.relgs.niveis_isolamento_sistemas_distribuidos.models.Product;
import com.relgs.niveis_isolamento_sistemas_distribuidos.repositories.ClientRepository;
import com.relgs.niveis_isolamento_sistemas_distribuidos.repositories.OrderDetailRepository;
import com.relgs.niveis_isolamento_sistemas_distribuidos.repositories.OrderRepository;
import com.relgs.niveis_isolamento_sistemas_distribuidos.repositories.ProductRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

@Service
public class OrderService {

    private final ProductRepository productRepository;
    private final OrderRepository orderRepository;
    private final ClientRepository clientRepository;
    private final OrderDetailRepository orderDetailRepository;

    public OrderService(ProductRepository productRepository, OrderRepository orderRepository, OrderDetailRepository orderDetailRepository, ClientRepository clientRepository) {
        this.productRepository = productRepository;
        this.orderRepository = orderRepository;
        this.orderDetailRepository = orderDetailRepository;
        this.clientRepository = clientRepository;
    }

    @Transactional
    public OrderRequestDto createOrderWithoutLocks(OrderRequestDto orderRequestDto) {
        Product product = productRepository.findById(orderRequestDto.productId()).orElseThrow();
        Client client = clientRepository.findById(orderRequestDto.clientId()).orElseThrow();

        Order order = new Order(null, client, LocalDate.now());
        OrderDetail orderDetail = new OrderDetail(null, product, order, orderRequestDto.quantity(), orderRequestDto.discount());

        if (product.getStock() < orderRequestDto.quantity()) {
            throw new InsufficientStockException("Estoque insuficiente");
        }

        productRepository.updateStockWithoutLock(product.getId(), orderRequestDto.quantity());
        orderRepository.save(order);
        orderDetailRepository.save(orderDetail);

        return orderRequestDto;
    }

    @Transactional
    public OrderRequestDto createOrderWithOptimisticLock(OrderRequestDto orderRequestDto) {
        Product product = productRepository.findById(orderRequestDto.productId()).orElseThrow();
        Client client = clientRepository.findById(orderRequestDto.clientId()).orElseThrow();

        Order order = new Order(null, client, LocalDate.now());
        OrderDetail orderDetail = new OrderDetail(null, product, order, orderRequestDto.quantity(), orderRequestDto.discount());

        if (product.getStock() < orderRequestDto.quantity()) {
            throw new InsufficientStockException("Estoque insuficiente");
        }

        product.setStock(product.getStock() - orderRequestDto.quantity());

        productRepository.save(product);
        orderRepository.save(order);
        orderDetailRepository.save(orderDetail);

        return orderRequestDto;
    }

    @Transactional
    public OrderRequestDto createOrderWithPessimisticLock(OrderRequestDto orderRequestDto) {
        Product product = productRepository.findByIdWithLock(orderRequestDto.productId());
        Client client = clientRepository.findById(orderRequestDto.clientId()).orElseThrow();

        Order order = new Order(null, client, LocalDate.now());
        OrderDetail orderDetail = new OrderDetail(null, product, order, orderRequestDto.quantity(), orderRequestDto.discount());

        if (product.getStock() < orderRequestDto.quantity()) {
            throw new InsufficientStockException("Estoque insuficiente");
        }

        product.setStock(product.getStock() - orderRequestDto.quantity());

        productRepository.save(product);
        orderRepository.save(order);
        orderDetailRepository.save(orderDetail);

        return orderRequestDto;
    }
}
