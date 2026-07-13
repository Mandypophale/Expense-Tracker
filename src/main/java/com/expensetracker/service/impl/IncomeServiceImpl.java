package com.expensetracker.service.impl;

import com.expensetracker.dto.IncomeDto;
import com.expensetracker.entity.Income;
import com.expensetracker.entity.User;
import com.expensetracker.repository.IncomeRepository;
import com.expensetracker.service.IncomeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Service implementation for Income business logic.
 */
@Service
public class IncomeServiceImpl implements IncomeService {

    @Autowired
    private IncomeRepository incomeRepository;

    @Override
    public List<Income> getAllIncome(User user) {
        return incomeRepository.findByUserOrderByIncomeDateDesc(user);
    }

    @Override
    public Income addIncome(IncomeDto incomeDto, User user) {
        Income income = new Income(
                incomeDto.getSource(),
                incomeDto.getAmount(),
                incomeDto.getIncomeDate(),
                user
        );
        return incomeRepository.save(income);
    }

    @Override
    public Income getIncomeById(Long id) {
        return incomeRepository.findById(id).orElse(null);
    }

    @Override
    public Income updateIncome(Long id, IncomeDto incomeDto) {
        Income income = incomeRepository.findById(id).orElse(null);
        if (income != null) {
            income.setSource(incomeDto.getSource());
            income.setAmount(incomeDto.getAmount());
            income.setIncomeDate(incomeDto.getIncomeDate());
            return incomeRepository.save(income);
        }
        return null;
    }

    @Override
    public void deleteIncome(Long id) {
        incomeRepository.deleteById(id);
    }

    @Override
    public Double getTotalIncome(User user) {
        List<Income> incomeList = getAllIncome(user);
        return incomeList.stream()
                .mapToDouble(Income::getAmount)
                .sum();
    }
}
