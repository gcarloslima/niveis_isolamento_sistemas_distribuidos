package com.relgs.niveis_isolamento_sistemas_distribuidos.repositories;

import com.relgs.niveis_isolamento_sistemas_distribuidos.models.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, Long> {
}