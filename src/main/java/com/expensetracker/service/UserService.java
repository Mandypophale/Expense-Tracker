package com.expensetracker.service;

import com.expensetracker.dto.UserLoginDto;
import com.expensetracker.dto.UserRegisterDto;
import com.expensetracker.entity.User;

/**
 * Service interface for User business logic.
 */
public interface UserService {

    /**
     * Register a new user.
     * @param registerDto registration data
     * @return the registered User entity
     */
    User registerUser(UserRegisterDto registerDto);

    /**
     * Authenticate a user by email and password.
     * @param loginDto login data
     * @return the authenticated User entity, or null if auth fails
     */
    User loginUser(UserLoginDto loginDto);

    /**
     * Check if a user with the given email already exists.
     * @param email user email
     * @return true if exists, false otherwise
     */
    boolean existsByEmail(String email);

    /**
     * Find a user by their ID.
     */
    User findById(Long id);

    /**
     * Update user profile information.
     */
    User updateProfile(Long id, String fullName, String newPassword);
}
