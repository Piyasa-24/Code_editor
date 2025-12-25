package com.editor.code.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import com.editor.code.Model.User;
import com.editor.code.repository.Userrepository;

@Controller
public class RegisterController {

    @Autowired
    private Userrepository userRepository;

    @GetMapping("/register")
    public String registerPage() {
        return "register";
    }

    @PostMapping("/register")
    public String registerUser(@RequestParam String username,
                               @RequestParam String email,
                               @RequestParam String password,
                               @RequestParam String confirmPassword,
                               Model model) {

        // Check password match
        if (!password.equals(confirmPassword)) {
            model.addAttribute("error", "Passwords do not match");
            return "register";
        }

        // Check if email already exists
        if (userRepository.findByEmail(email).isPresent()) {
            model.addAttribute("error", "Email already registered");
            return "register";
        }

        // Save new user
        User user = new User();
        user.setUsername(username);
        user.setEmail(email);
        user.setPassword(password); // ⚠ Plain text (hash later)

        userRepository.save(user);

        model.addAttribute("success", "Registration successful! Please login.");
        return "login";
    }
}
