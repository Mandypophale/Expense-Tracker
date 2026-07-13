package com.expensetracker.repository;

import com.expensetracker.entity.Expense;
import com.expensetracker.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repository interface for Expense entity.
 */
@Repository
public interface ExpenseRepository extends JpaRepository<Expense, Long> {

    /**
     * Find all expenses for a user ordered by date descending.
     */
    List<Expense> findByUserOrderByExpenseDateDesc(User user);

    /**
     * Find expenses by user and title containing search string (case-insensitive).
     */
    List<Expense> findByUserAndTitleContainingIgnoreCaseOrderByExpenseDateDesc(User user, String title);

    /**
     * Find expenses by user and category.
     */
    List<Expense> findByUserAndCategoryOrderByExpenseDateDesc(User user, String category);

    /**
     * Find expenses by user, title containing search string, and category.
     */
    List<Expense> findByUserAndTitleContainingIgnoreCaseAndCategoryOrderByExpenseDateDesc(User user, String title, String category);

    /**
     * Find the recent 5 expenses of a user.
     */
    List<Expense> findTop5ByUserOrderByExpenseDateDesc(User user);
}
