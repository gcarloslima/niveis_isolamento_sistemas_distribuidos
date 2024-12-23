package com.relgs.niveis_isolamento_sistemas_distribuidos;

import com.relgs.niveis_isolamento_sistemas_distribuidos.models.Product;
import com.relgs.niveis_isolamento_sistemas_distribuidos.repositories.ClientRepository;
import com.relgs.niveis_isolamento_sistemas_distribuidos.repositories.OrderDetailRepository;
import com.relgs.niveis_isolamento_sistemas_distribuidos.repositories.OrderRepository;
import com.relgs.niveis_isolamento_sistemas_distribuidos.repositories.ProductRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDate;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Import(TestcontainersConfiguration.class)
@SpringBootTest
@AutoConfigureMockMvc
class NiveisIsolamentoSistemasDistribuidosApplicationTests {

    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private OrderDetailRepository orderDetailRepository;

    @Autowired
    private MockMvc mockMvc;

    private Product testProduct;

    @BeforeEach
    void setup() {
        orderDetailRepository.deleteAll();
        orderRepository.deleteAll();
        productRepository.deleteAll();

        Product product = new Product();
        product.setName("Playstation 5");
        product.setStock(1000);
        product.setPrice(BigDecimal.valueOf(1999.99));
        testProduct = productRepository.save(product);
    }

    @Test
    void testarEndpointSemLock100() throws InterruptedException {
        executarTesteConcorrente(100,"/pedido/novo", "Sem Lock");
    }

    @Test
    void testarEndpointComLockOtimista100() throws InterruptedException {
        executarTesteConcorrente(100,"/pedido_otimista/novo", "Lock Otimista");
    }

    @Test
    void testarEndpointComLockPessimista100() throws InterruptedException {
        executarTesteConcorrente(100,"/pedido_pessimista/novo", "Lock Pessimista");
    }

    @Test
    void testarEndpointSemLock1000() throws InterruptedException {
        executarTesteConcorrente(1000,"/pedido/novo", "Sem Lock");
    }

    @Test
    void testarEndpointComLockOtimista1000() throws InterruptedException {
        executarTesteConcorrente(1000, "/pedido_otimista/novo", "Lock Otimista");
    }

    @Test
    void testarEndpointComLockPessimista1000() throws InterruptedException {
        executarTesteConcorrente(1000, "/pedido_pessimista/novo", "Lock Pessimista");
    }

    @Test
    void testarEndpointSemLock10000() throws InterruptedException {
        executarTesteConcorrente(10000, "/pedido/novo", "Sem Lock");
    }

    @Test
    void testarEndpointComLockOtimista10000() throws InterruptedException {
        executarTesteConcorrente(10000, "/pedido_otimista/novo", "Lock Otimista");
    }

    @Test
    void testarEndpointComLockPessimista10000() throws InterruptedException {
        executarTesteConcorrente(10000, "/pedido_pessimista/novo", "Lock Pessimista");
    }

    private void executarTesteConcorrente(Integer quantidadeRequisicoes, String endpoint, String abordagem) throws InterruptedException {
        AtomicLong totalResponseTime = new AtomicLong();
        AtomicInteger successfulRequests = new AtomicInteger();
        AtomicInteger conflictRequests = new AtomicInteger();
        AtomicInteger otherErrorRequests = new AtomicInteger();

        int numberOfRequests = quantidadeRequisicoes;
        int numberOfThreads = 10;
        ExecutorService executorService = Executors.newFixedThreadPool(numberOfThreads);

        Instant start = Instant.now();

        for (int i = 0; i < numberOfRequests; i++) {
            executorService.submit(() -> {
                try {
                    Instant requestStart = Instant.now();
                    mockMvc.perform(post(endpoint)
                                    .contentType(MediaType.APPLICATION_JSON)
                                    .content(String.format("""
                                        {
                                            "id": null,
                                            "clientId": 1,
                                            "orderDate": "%s",
                                            "productId": %d,
                                            "quantity": %d,
                                            "discount": 0
                                        }
                                    """, LocalDate.now(), testProduct.getId(), new Random().nextInt(5) + 1)))
                            .andExpect(status().isOk());
                    successfulRequests.incrementAndGet();
                    totalResponseTime.addAndGet(Duration.between(requestStart, Instant.now()).toMillis());
                } catch (Exception e) {
                    if (e.getMessage().contains("409")) {
                        conflictRequests.incrementAndGet();
                    } else {
                        otherErrorRequests.incrementAndGet();
                    }
                }
            });
        }

        executorService.shutdown();
        executorService.awaitTermination(1, TimeUnit.MINUTES);

        Instant end = Instant.now();

        Product product = productRepository.findById(testProduct.getId())
                .orElseThrow(() -> new RuntimeException("Produto não encontrado"));

        reportarResultados(abordagem, start, end, totalResponseTime, successfulRequests, conflictRequests, otherErrorRequests);

        Assertions.assertTrue(product.getStock() >= 0);
    }

    private void reportarResultados(String abordagem, Instant start, Instant end, AtomicLong totalResponseTime, AtomicInteger successfulRequests, AtomicInteger conflictRequests, AtomicInteger otherErrorRequests) {
        long tempoTotal = Duration.between(start, end).toMillis();
        long tempoMedio = successfulRequests.get() > 0 ? totalResponseTime.get() / successfulRequests.get() : 0;

        System.out.printf("\nResultados para %s:\n", abordagem);
        System.out.println("\nEstoque final: " + productRepository.findAll().getFirst().getStock());
        System.out.printf("Tempo total: %d ms\n", tempoTotal);
        System.out.printf("Tempo médio por requisição: %d ms\n", tempoMedio);
        System.out.printf("Requisições bem-sucedidas: %d\n", successfulRequests.get());
        System.out.printf("Requisições conflitantes (409): %d\n", conflictRequests.get());
        System.out.printf("Outros erros: %d\n", otherErrorRequests.get());
    }
}
