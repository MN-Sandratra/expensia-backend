package com.expensia.expensia_backend.repository;

import com.expensia.expensia_backend.model.Expense;
import com.expensia.expensia_backend.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ExpenseRepository extends JpaRepository<Expense,Long> {
    List<Expense> findByUser(User user);
}
