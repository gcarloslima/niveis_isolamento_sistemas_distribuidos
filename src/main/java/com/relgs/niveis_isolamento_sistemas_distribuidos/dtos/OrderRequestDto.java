package com.relgs.niveis_isolamento_sistemas_distribuidos.dtos;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * DTO for {@link com.relgs.niveis_isolamento_sistemas_distribuidos.models.Order}
 */
public record OrderRequestDto(Long id, Long clientId, LocalDate orderDate, Long productId, Integer quantity,
                             BigDecimal discount) implements Serializable {
}