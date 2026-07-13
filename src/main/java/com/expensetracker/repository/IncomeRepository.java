package com.expensetracker.repository;

import com.expensetracker.entity.Income;
import com.expensetracker.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repository interface for Income entity.
 */
@Repository
public interface IncomeRepository extends JpaRepository<Income, Long> {

    /**
     * Find all income entries for a user ordered by date descending.
     */
    List<Income> findByUserOrderByIncomeDateDesc(User user);
}
