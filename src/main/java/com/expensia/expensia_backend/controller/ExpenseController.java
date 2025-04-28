package com.expensia.expensia_backend.controller;

import com.expensia.expensia_backend.dto.ExpenseRequest;
import com.expensia.expensia_backend.dto.ExpenseResponse;
import com.expensia.expensia_backend.model.Expense;
import com.expensia.expensia_backend.service.ExpenseService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/expenses")
public class ExpenseController {
    private final ExpenseService expenseService;

    @PostMapping
    public ExpenseResponse addNewExpense(@RequestBody ExpenseRequest expenseRequest){
        return expenseService.addNewExpense(expenseRequest);
    }

    @GetMapping
    public List<ExpenseResponse> getAllExpense(){
        return expenseService.getAllExpense();
    }

    @PutMapping("/{id}")
    public ExpenseResponse updateExpense(@PathVariable Long id,@RequestBody ExpenseRequest expenseRequest){
        return expenseService.updateExpense(id,expenseRequest);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        expenseService.deleteExpense(id);
        return ResponseEntity.ok(Map.of("message", "Expense deleted successfully"));
    }

    @GetMapping("/summary")
    public Map<String,Double> getSummary(){
        return expenseService.getSummary();
    }
}
