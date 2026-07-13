package com.expensetracker.controller;

import com.expensetracker.dto.UserLoginDto;
import com.expensetracker.dto.UserRegisterDto;
import com.expensetracker.entity.User;
import com.expensetracker.service.UserService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 * Controller handling user authentication (Login, Register, Logout).
 */
@Controller
public class AuthController {

    @Autowired
    private UserService userService;

    /**
     * Show registration form.
     */
    @GetMapping("/register")
    public String showRegisterForm(Model model, HttpSession session) {
        // If already logged in, redirect to dashboard
        if (session.getAttribute("loggedUser") != null) {
            return "redirect:/dashboard";
        }
        model.addAttribute("registerDto", new UserRegisterDto());
        return "register";
    }

    /**
     * Process registration form.
     */
    @PostMapping("/register")
    public String registerUser(@Valid @ModelAttribute("registerDto") UserRegisterDto registerDto,
                               BindingResult result,
                               Model model) {
        if (result.hasErrors()) {
            return "register";
        }

        if (userService.existsByEmail(registerDto.getEmail())) {
            result.rejectValue("email", "error.registerDto", "Email address is already in use.");
            return "register";
        }

        userService.registerUser(registerDto);
        return "redirect:/login?registered=true";
    }

    /**
     * Show login form.
     */
    @GetMapping("/login")
    public String showLoginForm(Model model, HttpSession session) {
        // If already logged in, redirect to dashboard
        if (session.getAttribute("loggedUser") != null) {
            return "redirect:/dashboard";
        }
        model.addAttribute("loginDto", new UserLoginDto());
        return "login";
    }

    /**
     * Process login form.
     */
    @PostMapping("/login")
    public String loginUser(@Valid @ModelAttribute("loginDto") UserLoginDto loginDto,
                            BindingResult result,
                            HttpSession session,
                            Model model) {
        if (result.hasErrors()) {
            return "login";
        }

        User user = userService.loginUser(loginDto);
        if (user == null) {
            model.addAttribute("loginError", "Invalid email or password.");
            return "login";
        }

        // Set user in session
        session.setAttribute("loggedUser", user);
        return "redirect:/dashboard";
    }

    /**
     * Logout user.
     */
    @GetMapping("/logout")
    public String logout(HttpSession session, RedirectAttributes redirectAttributes) {
        session.invalidate();
        redirectAttributes.addFlashAttribute("logoutMessage", "You have been logged out successfully.");
        return "redirect:/login";
    }
}
