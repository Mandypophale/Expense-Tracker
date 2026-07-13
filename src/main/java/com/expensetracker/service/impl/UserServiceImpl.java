package com.expensetracker.service.impl;

import com.expensetracker.dto.UserLoginDto;
import com.expensetracker.dto.UserRegisterDto;
import com.expensetracker.entity.User;
import com.expensetracker.repository.UserRepository;
import com.expensetracker.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Optional;

/**
 * Service implementation for User business logic.
 */
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public User registerUser(UserRegisterDto registerDto) {
        // Hash the password for security
        String hashedPassword = hashPassword(registerDto.getPassword());
        User user = new User(registerDto.getFullName(), registerDto.getEmail(), hashedPassword);
        return userRepository.save(user);
    }

    @Override
    public User loginUser(UserLoginDto loginDto) {
        Optional<User> userOpt = userRepository.findByEmail(loginDto.getEmail());
        if (userOpt.isPresent()) {
            User user = userOpt.get();
            String hashedInput = hashPassword(loginDto.getPassword());
            if (user.getPassword().equals(hashedInput)) {
                return user;
            }
        }
        return null;
    }

    @Override
    public boolean existsByEmail(String email) {
        return userRepository.findByEmail(email).isPresent();
    }

    @Override
    public User findById(Long id) {
        return userRepository.findById(id).orElse(null);
    }

    @Override
    public User updateProfile(Long id, String fullName, String newPassword) {
        User user = userRepository.findById(id).orElse(null);
        if (user != null) {
            user.setFullName(fullName);
            // Only update password if a new one is provided
            if (newPassword != null && !newPassword.trim().isEmpty()) {
                user.setPassword(hashPassword(newPassword));
            }
            return userRepository.save(user);
        }
        return null;
    }

    /**
     * SHA-256 Hashing helper method for secure password storage.
     */
    private String hashPassword(String password) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] encodedhash = digest.digest(password.getBytes());
            StringBuilder hexString = new StringBuilder(2 * encodedhash.length);
            for (byte b : encodedhash) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) {
                    hexString.append('0');
                }
                hexString.append(hex);
            }
            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Error hashing password", e);
        }
    }
}
