package com.relgs.niveis_isolamento_sistemas_distribuidos.repositories;

import com.relgs.niveis_isolamento_sistemas_distribuidos.models.Product;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ProductRepository extends JpaRepository<Product, Long> {

    @Modifying
    @Query("UPDATE Product p SET p.stock = p.stock - :quantidade WHERE p.id = :id")
    void updateStockWithoutLock(@Param("id") Long id, @Param("quantidade") Integer quantidade);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT p FROM Product p WHERE p.id = :id")
    Product findByIdWithLock(@Param("id") Long id);
}