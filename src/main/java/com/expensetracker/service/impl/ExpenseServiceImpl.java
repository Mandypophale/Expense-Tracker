package com.expensetracker.service.impl;

import com.expensetracker.dto.ExpenseDto;
import com.expensetracker.entity.Expense;
import com.expensetracker.entity.User;
import com.expensetracker.repository.ExpenseRepository;
import com.expensetracker.service.ExpenseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Service implementation for Expense business logic.
 */
@Service
public class ExpenseServiceImpl implements ExpenseService {

    @Autowired
    private ExpenseRepository expenseRepository;

    @Override
    public List<Expense> getAllExpenses(User user) {
        return expenseRepository.findByUserOrderByExpenseDateDesc(user);
    }

    @Override
    public List<Expense> getRecentExpenses(User user) {
        return expenseRepository.findTop5ByUserOrderByExpenseDateDesc(user);
    }

    @Override
    public List<Expense> searchAndFilterExpenses(User user, String title, String category) {
        boolean hasTitle = (title != null && !title.trim().isEmpty());
        boolean hasCategory = (category != null && !category.trim().isEmpty() && !"All".equalsIgnoreCase(category));

        if (hasTitle && hasCategory) {
            return expenseRepository.findByUserAndTitleContainingIgnoreCaseAndCategoryOrderByExpenseDateDesc(user, title.trim(), category.trim());
        } else if (hasTitle) {
            return expenseRepository.findByUserAndTitleContainingIgnoreCaseOrderByExpenseDateDesc(user, title.trim());
        } else if (hasCategory) {
            return expenseRepository.findByUserAndCategoryOrderByExpenseDateDesc(user, category.trim());
        } else {
            return getAllExpenses(user);
        }
    }

    @Override
    public Expense addExpense(ExpenseDto expenseDto, User user) {
        Expense expense = new Expense(
                expenseDto.getTitle(),
                expenseDto.getAmount(),
                expenseDto.getCategory(),
                expenseDto.getExpenseDate(),
                expenseDto.getDescription(),
                user
        );
        return expenseRepository.save(expense);
    }

    @Override
    public Expense getExpenseById(Long id) {
        return expenseRepository.findById(id).orElse(null);
    }

    @Override
    public Expense updateExpense(Long id, ExpenseDto expenseDto) {
        Expense expense = expenseRepository.findById(id).orElse(null);
        if (expense != null) {
            expense.setTitle(expenseDto.getTitle());
            expense.setAmount(expenseDto.getAmount());
            expense.setCategory(expenseDto.getCategory());
            expense.setExpenseDate(expenseDto.getExpenseDate());
            expense.setDescription(expenseDto.getDescription());
            return expenseRepository.save(expense);
        }
        return null;
    }

    @Override
    public void deleteExpense(Long id) {
        expenseRepository.deleteById(id);
    }

    @Override
    public Double getTotalExpense(User user) {
        List<Expense> expenses = getAllExpenses(user);
        return expenses.stream()
                .mapToDouble(Expense::getAmount)
                .sum();
    }
}
