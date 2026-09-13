package com.gigsaathi.gigsaathi.controller;

import com.gigsaathi.gigsaathi.model.Earning;
import com.gigsaathi.gigsaathi.model.User;
import com.gigsaathi.gigsaathi.repository.EarningRepository;
import com.gigsaathi.gigsaathi.repository.UserRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;

@Controller
public class EarningController {

    private final UserRepository userRepository;
    private final EarningRepository earningRepository;

    public EarningController(UserRepository userRepository, EarningRepository earningRepository) {
        this.userRepository = userRepository;
        this.earningRepository = earningRepository;
    }

    @GetMapping("/users/{id}/earnings")
    public String listEarnings(@PathVariable Long id, Model model) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid user Id:" + id));
        List<Earning> earnings = earningRepository.findByUserId(id);
        if (earnings != null && !earnings.isEmpty()) {
            earnings.sort(Comparator.comparing(Earning::getDate).reversed());
        }
        BigDecimal total = earnings.stream()
                .map(Earning::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        model.addAttribute("user", user);
        model.addAttribute("earnings", earnings);
        model.addAttribute("total", total);
        return "earnings";
    }

    @GetMapping("/users/{id}/earnings/new")
    public String showEarningForm(@PathVariable Long id, Model model) {
        Earning earning = new Earning();
        earning.setDate(LocalDate.now());
        model.addAttribute("userId", id);
        model.addAttribute("earning", earning);
        return "earning-form";
    }

    @PostMapping("/users/{id}/earnings")
    public String createEarning(@PathVariable Long id, @ModelAttribute Earning earning) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid user Id:" + id));
        
        // Create a new earning object to avoid optimistic locking issues
        Earning newEarning = new Earning();
        newEarning.setAmount(earning.getAmount());
        newEarning.setSource(earning.getSource());
        newEarning.setDescription(earning.getDescription());
        newEarning.setDate(earning.getDate());
        newEarning.setUser(user);
        
        earningRepository.save(newEarning);
        return "redirect:/users/" + id + "/earnings";
    }
}
