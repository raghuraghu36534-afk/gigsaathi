package com.gigsaathi.gigsaathi.controller;

import com.gigsaathi.gigsaathi.model.User;
import com.gigsaathi.gigsaathi.repository.UserRepository;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class RegistrationController {

    private static final Logger log = LoggerFactory.getLogger(RegistrationController.class);

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public RegistrationController(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping("/register")
    public String showRegistrationForm(Model model) {
        model.addAttribute("user", new User());
        return "register";
    }

    @PostMapping("/register")
    public String registerUser(@Valid @ModelAttribute User user, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            log.info("Validation errors during registration: {}", bindingResult.getAllErrors());
            return "register";
        }
        
        // Check if phone number already exists
        if (userRepository.findByPhoneNumber(user.getPhoneNumber()).isPresent()) {
            log.info("Registration failed: Phone number already exists - {}", user.getPhoneNumber());
            model.addAttribute("error", "This phone number is already registered. Please use a different number or login.");
            return "register";
        }
        
        log.info("Registering user: name={}, phone={}, age={}, state={}", 
                user.getName(), user.getPhoneNumber(), user.getAge(), user.getState());
        
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);
        
        log.info("User saved successfully: id={}, name={}, phone={}, age={}, state={}", 
                user.getId(), user.getName(), user.getPhoneNumber(), user.getAge(), user.getState());
        
        return "redirect:/login?registered";
    }

    @GetMapping("/login")
    public String showLoginForm() {
        return "login";
    }
}
