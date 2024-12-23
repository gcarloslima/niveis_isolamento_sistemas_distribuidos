package com.relgs.niveis_isolamento_sistemas_distribuidos.infra;

import com.relgs.niveis_isolamento_sistemas_distribuidos.exceptions.InsufficientStockException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(InsufficientStockException.class)
    public ResponseEntity<String> handleInsufficientStockException(InsufficientStockException e) {
        return ResponseEntity.badRequest().body(e.getMessage());
    }

}
