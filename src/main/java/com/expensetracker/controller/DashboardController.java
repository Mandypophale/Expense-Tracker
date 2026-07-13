package com.expensetracker.controller;

import com.expensetracker.entity.Expense;
import com.expensetracker.entity.User;
import com.expensetracker.service.ExpenseService;
import com.expensetracker.service.IncomeService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

/**
 * Controller handling dashboard view calculations.
 */
@Controller
public class DashboardController {

    @Autowired
    private ExpenseService expenseService;

    @Autowired
    private IncomeService incomeService;

    @GetMapping("/")
    public String index(HttpSession session) {
        if (session.getAttribute("loggedUser") == null) {
            return "redirect:/login";
        }
        return "redirect:/dashboard";
    }

    @GetMapping("/dashboard")
    public String showDashboard(HttpSession session, Model model) {
        User loggedUser = (User) session.getAttribute("loggedUser");
        if (loggedUser == null) {
            return "redirect:/login";
        }

        // Calculate and add totals to model
        Double totalIncome = incomeService.getTotalIncome(loggedUser);
        Double totalExpense = expenseService.getTotalExpense(loggedUser);
        Double remainingBalance = totalIncome - totalExpense;

        // Fetch recent 5 expenses
        List<Expense> recentExpenses = expenseService.getRecentExpenses(loggedUser);

        model.addAttribute("totalIncome", totalIncome);
        model.addAttribute("totalExpense", totalExpense);
        model.addAttribute("remainingBalance", remainingBalance);
        model.addAttribute("recentExpenses", recentExpenses);

        return "dashboard";
    }
}
