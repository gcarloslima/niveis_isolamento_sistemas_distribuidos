package com.relgs.niveis_isolamento_sistemas_distribuidos.repositories;

import com.relgs.niveis_isolamento_sistemas_distribuidos.models.Client;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClientRepository extends JpaRepository<Client, Long> {
}