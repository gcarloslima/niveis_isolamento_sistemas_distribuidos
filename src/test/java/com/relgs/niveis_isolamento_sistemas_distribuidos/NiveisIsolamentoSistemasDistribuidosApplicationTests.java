package com.relgs.niveis_isolamento_sistemas_distribuidos;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;

@Import(TestcontainersConfiguration.class)
@SpringBootTest
class NiveisIsolamentoSistemasDistribuidosApplicationTests {

    @Test
    void contextLoads() {
    }

}
