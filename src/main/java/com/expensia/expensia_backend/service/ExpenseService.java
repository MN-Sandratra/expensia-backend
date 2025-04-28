package com.expensia.expensia_backend.service;

import com.expensia.expensia_backend.dto.ExpenseRequest;
import com.expensia.expensia_backend.dto.ExpenseResponse;
import com.expensia.expensia_backend.model.Expense;
import com.expensia.expensia_backend.model.ExpenseType;
import com.expensia.expensia_backend.model.User;
import com.expensia.expensia_backend.repository.ExpenseRepository;
import com.expensia.expensia_backend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class ExpenseService {
    private final UserRepository userRepository;
    private final ExpenseRepository expenseRepository;

    public ExpenseResponse addNewExpense(ExpenseRequest expenseRequest) {
        String userName = SecurityContextHolder.getContext().getAuthentication().getName();

        User connectedUser = userRepository.findByUsername(userName)
                .orElseThrow(() -> new UsernameNotFoundException("User no found"));

        Expense newExpense = Expense.builder()
                .amount(expenseRequest.getAmount())
                .type(expenseRequest.getType())
                .description(expenseRequest.getDescription())
                .date(expenseRequest.getDate())
                .user(connectedUser)
                .build();

        expenseRepository.save(newExpense);

        return ExpenseResponse.builder()
                .id(newExpense.getId())
                .amount(newExpense.getAmount())
                .type(newExpense.getType())
                .description(newExpense.getDescription())
                .date(newExpense.getDate())
                .userId(connectedUser.getId())
                .build();
    }

    public List<ExpenseResponse> getAllExpense() {
        String userName = SecurityContextHolder.getContext().getAuthentication().getName();

        User connectedUser = userRepository.findByUsername(userName)
                .orElseThrow(() -> new UsernameNotFoundException("User no found"));

        return expenseRepository.findByUser(connectedUser).stream()
                .map(expense -> ExpenseResponse.builder()
                        .id(expense.getId())
                        .amount(expense.getAmount())
                        .type(expense.getType())
                        .description(expense.getDescription())
                        .date(expense.getDate())
                        .userId(expense.getUser().getId())
                        .build()).toList();
    }

    public ExpenseResponse updateExpense(Long expenseId, ExpenseRequest expenseRequest){
        String userName = SecurityContextHolder.getContext().getAuthentication().getName();

        User connectedUser = userRepository.findByUsername(userName)
                .orElseThrow(() -> new UsernameNotFoundException("User no found"));

        Expense expenseToUpdate = expenseRepository.findById(expenseId)
                .filter(expense -> expense.getUser().getId().equals(connectedUser.getId()))
                .orElseThrow(()->new RuntimeException("Expense not found or not authorized"));

        expenseToUpdate.setAmount(expenseRequest.getAmount());
        expenseToUpdate.setType(expenseRequest.getType());
        expenseToUpdate.setDescription(expenseRequest.getDescription());
        expenseToUpdate.setDate(expenseRequest.getDate());

        Expense updated = expenseRepository.save(expenseToUpdate);

        return ExpenseResponse.builder()
                .id(updated.getId())
                .amount(updated.getAmount())
                .type(updated.getType())
                .description(updated.getDescription())
                .date(updated.getDate())
                .userId(connectedUser.getId())
                .build();
    }

    public void deleteExpense(Long expenseId){
        String userName = SecurityContextHolder.getContext().getAuthentication().getName();

        User connectedUser = userRepository.findByUsername(userName)
                .orElseThrow(() -> new UsernameNotFoundException("User no found"));

        Expense expenseToDelete = expenseRepository.findById(expenseId)
                .filter(expense -> expense.getUser().getId().equals(connectedUser.getId()))
                .orElseThrow(()->new RuntimeException("Expense not found or not authorized"));

        expenseRepository.delete(expenseToDelete);
    }

    public Map<String,Double> getSummary(){
        String userName = SecurityContextHolder.getContext().getAuthentication().getName();

        User connectedUser = userRepository.findByUsername(userName)
                .orElseThrow(() -> new UsernameNotFoundException("User no found"));

        double incomes = expenseRepository.findByUser(connectedUser)
                .stream()
                .filter(expense -> expense.getType().equals(ExpenseType.INCOME))
                .mapToDouble(Expense::getAmount)
                .sum();

        double expenses = expenseRepository.findByUser(connectedUser)
                .stream()
                .filter(expense -> expense.getType().equals(ExpenseType.EXPENSE))
                .mapToDouble(Expense::getAmount)
                .sum();

        double balance = incomes - expenses ;

        return Map.of(
                "totalIncome", incomes,
                "totalExpense", expenses,
                "balance", balance
        );
    }
}
