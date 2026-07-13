package com.expensetracker.controller;

import com.expensetracker.entity.User;
import com.expensetracker.service.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 * Controller handling user profile viewing and updates.
 */
@Controller
public class ProfileController {

    @Autowired
    private UserService userService;

    /**
     * Show profile page.
     */
    @GetMapping("/profile")
    public String showProfile(HttpSession session, Model model) {
        User loggedUser = (User) session.getAttribute("loggedUser");
        if (loggedUser == null) {
            return "redirect:/login";
        }
        
        // Refresh user from DB to get the latest state
        User user = userService.findById(loggedUser.getId());
        model.addAttribute("user", user);
        return "profile";
    }

    /**
     * Update profile details (Name and/or Password).
     */
    @PostMapping("/profile/update")
    public String updateProfile(HttpSession session,
                                @RequestParam("fullName") String fullName,
                                @RequestParam(value = "newPassword", required = false) String newPassword,
                                Model model,
                                RedirectAttributes redirectAttributes) {
        User loggedUser = (User) session.getAttribute("loggedUser");
        if (loggedUser == null) {
            return "redirect:/login";
        }

        // Validate fullName
        if (fullName == null || fullName.trim().isEmpty()) {
            model.addAttribute("errorMessage", "Full Name cannot be empty.");
            model.addAttribute("user", loggedUser);
            return "profile";
        }

        // Validate password if supplied
        if (newPassword != null && !newPassword.trim().isEmpty()) {
            if (newPassword.length() < 6) {
                model.addAttribute("errorMessage", "Password must be at least 6 characters.");
                model.addAttribute("user", loggedUser);
                return "profile";
            }
        }

        // Save updates
        User updated = userService.updateProfile(loggedUser.getId(), fullName.trim(), newPassword);
        
        // Update session user
        session.setAttribute("loggedUser", updated);
        
        redirectAttributes.addFlashAttribute("successMessage", "Profile updated successfully!");
        return "redirect:/profile";
    }
}
