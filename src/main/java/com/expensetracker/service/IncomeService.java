package com.expensetracker.service;

import com.expensetracker.dto.IncomeDto;
import com.expensetracker.entity.Income;
import com.expensetracker.entity.User;

import java.util.List;

/**
 * Service interface for Income business logic.
 */
public interface IncomeService {

    /**
     * Get all income entries for a user.
     */
    List<Income> getAllIncome(User user);

    /**
     * Save/Add a new income entry.
     */
    Income addIncome(IncomeDto incomeDto, User user);

    /**
     * Get income by ID.
     */
    Income getIncomeById(Long id);

    /**
     * Update an existing income entry.
     */
    Income updateIncome(Long id, IncomeDto incomeDto);

    /**
     * Delete an income entry.
     */
    void deleteIncome(Long id);

    /**
     * Calculate total income for a user.
     */
    Double getTotalIncome(User user);
}
