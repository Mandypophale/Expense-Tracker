package com.expensetracker.service;

import com.expensetracker.dto.ExpenseDto;
import com.expensetracker.entity.Expense;
import com.expensetracker.entity.User;

import java.util.List;

/**
 * Service interface for Expense business logic.
 */
public interface ExpenseService {

    /**
     * Get all expenses for a user.
     */
    List<Expense> getAllExpenses(User user);

    /**
     * Get recent 5 expenses for a user.
     */
    List<Expense> getRecentExpenses(User user);

    /**
     * Search and filter expenses for a user.
     */
    List<Expense> searchAndFilterExpenses(User user, String title, String category);

    /**
     * Save/Add a new expense.
     */
    Expense addExpense(ExpenseDto expenseDto, User user);

    /**
     * Get an expense by its ID.
     */
    Expense getExpenseById(Long id);

    /**
     * Update an existing expense.
     */
    Expense updateExpense(Long id, ExpenseDto expenseDto);

    /**
     * Delete an expense by its ID.
     */
    void deleteExpense(Long id);

    /**
     * Calculate total expenses for a user.
     */
    Double getTotalExpense(User user);
}
