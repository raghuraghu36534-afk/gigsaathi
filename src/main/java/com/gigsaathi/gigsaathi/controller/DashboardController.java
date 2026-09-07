package com.gigsaathi.gigsaathi.controller;

import com.gigsaathi.gigsaathi.model.User;
import com.gigsaathi.gigsaathi.model.WelfareScheme;
import com.gigsaathi.gigsaathi.repository.UserRepository;
import com.gigsaathi.gigsaathi.service.WelfareMatchingService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class DashboardController {

    private static final Logger log = LoggerFactory.getLogger(DashboardController.class);

    private final UserRepository userRepository;
    private final WelfareMatchingService welfareMatchingService;

    public DashboardController(UserRepository userRepository, WelfareMatchingService welfareMatchingService) {
        this.userRepository = userRepository;
        this.welfareMatchingService = welfareMatchingService;
    }

    @GetMapping("/dashboard")
    public String showDashboard(Authentication authentication, Model model) {
        String phoneNumber = authentication.getName();
        log.info("Loading dashboard for user: {}", phoneNumber);
        
        User user = userRepository.findByPhoneNumber(phoneNumber)
                .orElseThrow(() -> new IllegalArgumentException("User not found: " + phoneNumber));
        
        log.info("User found: {} (age: {}, state: {})", user.getName(), user.getAge(), user.getState());
        
        List<WelfareScheme> eligibleSchemes = welfareMatchingService.findEligibleSchemes(user);
        log.info("Found {} eligible schemes for user {}", eligibleSchemes.size(), phoneNumber);
        
        model.addAttribute("user", user);
        model.addAttribute("eligibleSchemes", eligibleSchemes);
        
        return "dashboard";
    }
}
