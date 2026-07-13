package com.expensetracker.controller;

import com.expensetracker.dto.ExpenseDto;
import com.expensetracker.entity.Expense;
import com.expensetracker.entity.User;
import com.expensetracker.service.ExpenseService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

/**
 * Controller handling expense management (View, Search, Filter, Add, Edit, Delete).
 */
@Controller
@RequestMapping("/expenses")
public class ExpenseController {

    @Autowired
    private ExpenseService expenseService;

    // Predefined categories requested by user
    private static final List<String> CATEGORIES = List.of(
            "Food", "Travel", "Shopping", "Bills", "Entertainment", "Health", "Education", "Other"
    );

    @ModelAttribute("categories")
    public List<String> populateCategories() {
        return CATEGORIES;
    }

    /**
     * View all expenses, with support for title search and category filtering.
     */
    @GetMapping
    public String viewExpenses(HttpSession session,
                               Model model,
                               @RequestParam(value = "search", required = false) String search,
                               @RequestParam(value = "category", required = false) String category) {
        User loggedUser = (User) session.getAttribute("loggedUser");
        if (loggedUser == null) {
            return "redirect:/login";
        }

        List<Expense> expenses = expenseService.searchAndFilterExpenses(loggedUser, search, category);
        
        model.addAttribute("expenses", expenses);
        model.addAttribute("search", search);
        model.addAttribute("selectedCategory", category);
        
        return "expenses";
    }

    /**
     * Show add expense form.
     */
    @GetMapping("/add")
    public String showAddExpenseForm(Model model, HttpSession session) {
        if (session.getAttribute("loggedUser") == null) {
            return "redirect:/login";
        }
        model.addAttribute("expenseDto", new ExpenseDto());
        return "add-expense";
    }

    /**
     * Process add expense form.
     */
    @PostMapping("/add")
    public String addExpense(@Valid @ModelAttribute("expenseDto") ExpenseDto expenseDto,
                             BindingResult result,
                             HttpSession session,
                             RedirectAttributes redirectAttributes) {
        User loggedUser = (User) session.getAttribute("loggedUser");
        if (loggedUser == null) {
            return "redirect:/login";
        }

        if (result.hasErrors()) {
            return "add-expense";
        }

        expenseService.addExpense(expenseDto, loggedUser);
        redirectAttributes.addFlashAttribute("successMessage", "Expense added successfully!");
        return "redirect:/expenses";
    }

    /**
     * Show edit expense form.
     */
    @GetMapping("/edit/{id}")
    public String showEditExpenseForm(@PathVariable("id") Long id,
                                      Model model,
                                      HttpSession session,
                                      RedirectAttributes redirectAttributes) {
        User loggedUser = (User) session.getAttribute("loggedUser");
        if (loggedUser == null) {
            return "redirect:/login";
        }

        Expense expense = expenseService.getExpenseById(id);
        if (expense == null || !expense.getUser().getId().equals(loggedUser.getId())) {
            redirectAttributes.addFlashAttribute("errorMessage", "Expense not found or unauthorized access.");
            return "redirect:/expenses";
        }

        // Map Entity to DTO
        ExpenseDto expenseDto = new ExpenseDto(
                expense.getId(),
                expense.getTitle(),
                expense.getAmount(),
                expense.getCategory(),
                expense.getExpenseDate(),
                expense.getDescription()
        );

        model.addAttribute("expenseDto", expenseDto);
        return "edit-expense";
    }

    /**
     * Process edit expense form.
     */
    @PostMapping("/edit/{id}")
    public String editExpense(@PathVariable("id") Long id,
                              @Valid @ModelAttribute("expenseDto") ExpenseDto expenseDto,
                              BindingResult result,
                              HttpSession session,
                              RedirectAttributes redirectAttributes) {
        User loggedUser = (User) session.getAttribute("loggedUser");
        if (loggedUser == null) {
            return "redirect:/login";
        }

        Expense expense = expenseService.getExpenseById(id);
        if (expense == null || !expense.getUser().getId().equals(loggedUser.getId())) {
            redirectAttributes.addFlashAttribute("errorMessage", "Expense not found or unauthorized access.");
            return "redirect:/expenses";
        }

        if (result.hasErrors()) {
            return "edit-expense";
        }

        expenseService.updateExpense(id, expenseDto);
        redirectAttributes.addFlashAttribute("successMessage", "Expense updated successfully!");
        return "redirect:/expenses";
    }

    /**
     * Delete expense.
     */
    @GetMapping("/delete/{id}")
    public String deleteExpense(@PathVariable("id") Long id,
                                HttpSession session,
                                RedirectAttributes redirectAttributes) {
        User loggedUser = (User) session.getAttribute("loggedUser");
        if (loggedUser == null) {
            return "redirect:/login";
        }

        Expense expense = expenseService.getExpenseById(id);
        if (expense == null || !expense.getUser().getId().equals(loggedUser.getId())) {
            redirectAttributes.addFlashAttribute("errorMessage", "Expense not found or unauthorized access.");
            return "redirect:/expenses";
        }

        expenseService.deleteExpense(id);
        redirectAttributes.addFlashAttribute("successMessage", "Expense deleted successfully!");
        return "redirect:/expenses";
    }
}
