package com.expensia.expensia_backend.dto;

import com.expensia.expensia_backend.model.ExpenseType;
import lombok.Data;

import java.time.LocalDate;

@Data
public class ExpenseRequest {
    private Double amount;
    private ExpenseType type;
    private String description;
    private LocalDate date;
}
