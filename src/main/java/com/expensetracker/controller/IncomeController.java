package com.expensetracker.controller;

import com.expensetracker.dto.IncomeDto;
import com.expensetracker.entity.Income;
import com.expensetracker.entity.User;
import com.expensetracker.service.IncomeService;
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
 * Controller handling income management (View list, Add, Edit, Delete).
 * All actions are managed within a unified screen layout (income.html).
 */
@Controller
@RequestMapping("/income")
public class IncomeController {

    @Autowired
    private IncomeService incomeService;

    /**
     * Show income list. If 'editId' query param is passed, pre-populate form for editing.
     */
    @GetMapping
    public String viewIncome(HttpSession session,
                             Model model,
                             @RequestParam(value = "editId", required = false) Long editId,
                             RedirectAttributes redirectAttributes) {
        User loggedUser = (User) session.getAttribute("loggedUser");
        if (loggedUser == null) {
            return "redirect:/login";
        }

        List<Income> incomes = incomeService.getAllIncome(loggedUser);
        model.addAttribute("incomes", incomes);

        // Check if we are in Edit Mode
        if (editId != null) {
            Income income = incomeService.getIncomeById(editId);
            if (income != null && income.getUser().getId().equals(loggedUser.getId())) {
                IncomeDto incomeDto = new IncomeDto(
                        income.getId(),
                        income.getSource(),
                        income.getAmount(),
                        income.getIncomeDate()
                );
                model.addAttribute("incomeDto", incomeDto);
                model.addAttribute("isEdit", true);
            } else {
                redirectAttributes.addFlashAttribute("errorMessage", "Income not found or unauthorized access.");
                return "redirect:/income";
            }
        } else {
            // Add Mode
            model.addAttribute("incomeDto", new IncomeDto());
            model.addAttribute("isEdit", false);
        }

        return "income";
    }

    /**
     * Process add or update income.
     */
    @PostMapping
    public String saveOrUpdateIncome(@Valid @ModelAttribute("incomeDto") IncomeDto incomeDto,
                                     BindingResult result,
                                     HttpSession session,
                                     Model model,
                                     RedirectAttributes redirectAttributes) {
        User loggedUser = (User) session.getAttribute("loggedUser");
        if (loggedUser == null) {
            return "redirect:/login";
        }

        boolean isEdit = (incomeDto.getId() != null);

        if (result.hasErrors()) {
            // Re-populate the list of incomes to render the validation error on the page
            List<Income> incomes = incomeService.getAllIncome(loggedUser);
            model.addAttribute("incomes", incomes);
            model.addAttribute("isEdit", isEdit);
            return "income";
        }

        if (isEdit) {
            // Verify ownership before updating
            Income existingIncome = incomeService.getIncomeById(incomeDto.getId());
            if (existingIncome == null || !existingIncome.getUser().getId().equals(loggedUser.getId())) {
                redirectAttributes.addFlashAttribute("errorMessage", "Unauthorized update attempt.");
                return "redirect:/income";
            }
            incomeService.updateIncome(incomeDto.getId(), incomeDto);
            redirectAttributes.addFlashAttribute("successMessage", "Income updated successfully!");
        } else {
            // Add new income
            incomeService.addIncome(incomeDto, loggedUser);
            redirectAttributes.addFlashAttribute("successMessage", "Income added successfully!");
        }

        return "redirect:/income";
    }

    /**
     * Delete income.
     */
    @GetMapping("/delete/{id}")
    public String deleteIncome(@PathVariable("id") Long id,
                                HttpSession session,
                                RedirectAttributes redirectAttributes) {
        User loggedUser = (User) session.getAttribute("loggedUser");
        if (loggedUser == null) {
            return "redirect:/login";
        }

        Income income = incomeService.getIncomeById(id);
        if (income == null || !income.getUser().getId().equals(loggedUser.getId())) {
            redirectAttributes.addFlashAttribute("errorMessage", "Income not found or unauthorized access.");
            return "redirect:/income";
        }

        incomeService.deleteIncome(id);
        redirectAttributes.addFlashAttribute("successMessage", "Income deleted successfully!");
        return "redirect:/income";
    }
}
