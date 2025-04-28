package com.expensia.expensia_backend.dto;

import com.expensia.expensia_backend.model.ExpenseType;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Data
@Builder
public class ExpenseResponse {
    private Long id;
    private Double amount;
    private ExpenseType type;
    private String description;
    private LocalDate date;
    private Long userId;
}
