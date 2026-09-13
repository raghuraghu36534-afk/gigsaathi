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
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class GovernmentSchemeController {

    private static final Logger log = LoggerFactory.getLogger(GovernmentSchemeController.class);

    private final WelfareMatchingService welfareMatchingService;
    private final UserRepository userRepository;

    public GovernmentSchemeController(WelfareMatchingService welfareMatchingService, UserRepository userRepository) {
        this.welfareMatchingService = welfareMatchingService;
        this.userRepository = userRepository;
    }

    @GetMapping("/schemes")
    public String showSchemesPage(Authentication authentication, Model model) {
        log.info("Loading schemes discovery page");
        
        // If user is logged in, pre-fill their age and state
        if (authentication != null && authentication.isAuthenticated()) {
            String phoneNumber = authentication.getName();
            User user = userRepository.findByPhoneNumber(phoneNumber).orElse(null);
            if (user != null && user.getAge() != null && user.getState() != null) {
                model.addAttribute("userAge", user.getAge());
                model.addAttribute("userState", user.getState());
                model.addAttribute("hasProfile", true);
            }
        }
        
        model.addAttribute("states", getIndianStates());
        return "schemes";
    }

    @PostMapping("/schemes/search")
    public String searchSchemes(@RequestParam Integer age, 
                              @RequestParam String state,
                              Model model) {
        log.info("Searching schemes for age {} and state {}", age, state);
        
        try {
            // Validate age
            if (age == null) {
                model.addAttribute("error", "Age is required");
                model.addAttribute("states", getIndianStates());
                return "schemes";
            }
            
            if (age < 1 || age > 100) {
                model.addAttribute("error", "Age must be between 1 and 100");
                model.addAttribute("states", getIndianStates());
                return "schemes";
            }
            
            // Validate state
            if (state == null || state.trim().isEmpty()) {
                model.addAttribute("error", "State is required");
                model.addAttribute("states", getIndianStates());
                return "schemes";
            }
            
            List<WelfareScheme> eligibleSchemes = welfareMatchingService.findEligibleSchemes(age, state);
            
            // Separate central and state schemes
            List<WelfareScheme> centralSchemes = eligibleSchemes.stream()
                    .filter(s -> "CENTRAL".equalsIgnoreCase(s.getGovernmentLevel()) || 
                               "ALL".equalsIgnoreCase(s.getState()) || 
                               "NATIONAL".equalsIgnoreCase(s.getState()))
                    .toList();
            
            List<WelfareScheme> stateSchemes = eligibleSchemes.stream()
                    .filter(s -> !"CENTRAL".equalsIgnoreCase(s.getGovernmentLevel()) && 
                               !"ALL".equalsIgnoreCase(s.getState()) && 
                               !"NATIONAL".equalsIgnoreCase(s.getState()))
                    .toList();
            
            model.addAttribute("age", age);
            model.addAttribute("state", state);
            model.addAttribute("eligibleSchemes", eligibleSchemes);
            model.addAttribute("centralSchemes", centralSchemes);
            model.addAttribute("stateSchemes", stateSchemes);
            model.addAttribute("states", getIndianStates());
            model.addAttribute("searchPerformed", true);
            
            log.info("Found {} central schemes and {} state schemes", centralSchemes.size(), stateSchemes.size());
            
        } catch (IllegalArgumentException e) {
            model.addAttribute("error", e.getMessage());
            model.addAttribute("states", getIndianStates());
        } catch (Exception e) {
            log.error("Error searching schemes", e);
            model.addAttribute("error", "An error occurred while searching schemes. Please try again.");
            model.addAttribute("states", getIndianStates());
        }
        
        return "schemes";
    }

    @GetMapping("/schemes/search")
    public String searchSchemesGet(@RequestParam(required = false) Integer age,
                                   @RequestParam(required = false) String state,
                                   @RequestParam(required = false) String keyword,
                                   @RequestParam(required = false) String category,
                                   @RequestParam(required = false) String governmentLevel,
                                   Model model) {
        
        if (keyword != null && !keyword.trim().isEmpty()) {
            return searchByKeyword(keyword, model);
        }
        
        if (category != null || governmentLevel != null) {
            return filterSchemes(category, governmentLevel, state, model);
        }
        
        if (age != null && state != null) {
            return searchSchemes(age, state, model);
        }
        
        return showSchemesPage(null, model);
    }

    @GetMapping("/schemes/my-schemes")
    public String findMySchemes(Authentication authentication, Model model) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return "redirect:/login";
        }
        
        String phoneNumber = authentication.getName();
        User user = userRepository.findByPhoneNumber(phoneNumber)
                .orElseThrow(() -> new IllegalArgumentException("User not found: " + phoneNumber));
        
        if (user.getAge() == null || user.getState() == null) {
            model.addAttribute("error", "Please complete your profile (age and state) to find schemes");
            model.addAttribute("states", getIndianStates());
            return "schemes";
        }
        
        return searchSchemes(user.getAge(), user.getState(), model);
    }

    @GetMapping("/schemes/{id}")
    public String showSchemeDetails(@PathVariable Long id, Model model) {
        log.info("Loading details for scheme id {}", id);
        
        try {
            WelfareScheme scheme = welfareMatchingService.findSchemeById(id);
            if (scheme == null) {
                model.addAttribute("error", "Scheme not found");
                return "schemes";
            }
            
            model.addAttribute("scheme", scheme);
            return "scheme-details";
        } catch (Exception e) {
            log.error("Error loading scheme details", e);
            model.addAttribute("error", "An error occurred while loading scheme details. Please try again.");
            return "schemes";
        }
    }

    private String searchByKeyword(String keyword, Model model) {
        log.info("Searching schemes by keyword: {}", keyword);
        List<WelfareScheme> schemes = welfareMatchingService.searchSchemes(keyword);
        
        model.addAttribute("schemes", schemes);
        model.addAttribute("keyword", keyword);
        model.addAttribute("searchType", "keyword");
        model.addAttribute("states", getIndianStates());
        
        return "schemes";
    }

    private String filterSchemes(String category, String governmentLevel, String state, Model model) {
        log.info("Filtering schemes - category: {}, governmentLevel: {}, state: {}", category, governmentLevel, state);
        List<WelfareScheme> schemes = welfareMatchingService.filterSchemes(governmentLevel, category, state);
        
        model.addAttribute("schemes", schemes);
        model.addAttribute("category", category);
        model.addAttribute("governmentLevel", governmentLevel);
        model.addAttribute("state", state);
        model.addAttribute("searchType", "filter");
        model.addAttribute("states", getIndianStates());
        
        return "schemes";
    }

    private List<String> getIndianStates() {
        return List.of(
            "ANDHRA_PRADESH", "ARUNACHAL_PRADESH", "ASSAM", "BIHAR", "CHHATTISGARH",
            "GOA", "GUJARAT", "HARYANA", "HIMACHAL_PRADESH", "JHARKHAND",
            "KARNATAKA", "KERALA", "MADHYA_PRADESH", "MAHARASHTRA", "MANIPUR",
            "MEGHALAYA", "MIZORAM", "NAGALAND", "ODISHA", "PUNJAB",
            "RAJASTHAN", "SIKKIM", "TAMIL_NADU", "TELANGANA", "TRIPURA",
            "UTTAR_PRADESH", "UTTARAKHAND", "WEST_BENGAL", "ANDAMAN_AND_NICOBAR",
            "CHANDIGARH", "DADRA_AND_NAGAR_HAVELI_AND_DAMAN_AND_DIU", "DELHI",
            "JAMMU_AND_KASHMIR", "LADAKH", "LAKSHADWEEP", "PUDUCHERRY"
        );
    }
}