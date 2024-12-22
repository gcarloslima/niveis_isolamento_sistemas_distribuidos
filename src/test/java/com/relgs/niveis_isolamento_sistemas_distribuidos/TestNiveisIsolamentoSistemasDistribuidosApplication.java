package com.relgs.niveis_isolamento_sistemas_distribuidos;

import org.springframework.boot.SpringApplication;

public class TestNiveisIsolamentoSistemasDistribuidosApplication {

    public static void main(String[] args) {
        SpringApplication.from(NiveisIsolamentoSistemasDistribuidosApplication::main).with(TestcontainersConfiguration.class).run(args);
    }

}
