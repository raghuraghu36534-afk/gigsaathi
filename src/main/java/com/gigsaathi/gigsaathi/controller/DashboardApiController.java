package com.gigsaathi.gigsaathi.controller;

import com.gigsaathi.gigsaathi.model.Earning;
import com.gigsaathi.gigsaathi.model.User;
import com.gigsaathi.gigsaathi.model.WelfareScheme;
import com.gigsaathi.gigsaathi.repository.EarningRepository;
import com.gigsaathi.gigsaathi.repository.UserRepository;
import com.gigsaathi.gigsaathi.repository.WelfareSchemeRepository;
import com.gigsaathi.gigsaathi.service.WelfareMatchingService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class DashboardApiController {

    private static final Logger log = LoggerFactory.getLogger(DashboardApiController.class);

    private final EarningRepository earningRepository;
    private final WelfareSchemeRepository welfareSchemeRepository;
    private final WelfareMatchingService welfareMatchingService;
    private final UserRepository userRepository;

    public DashboardApiController(EarningRepository earningRepository, 
                                WelfareSchemeRepository welfareSchemeRepository,
                                WelfareMatchingService welfareMatchingService,
                                UserRepository userRepository) {
        this.earningRepository = earningRepository;
        this.welfareSchemeRepository = welfareSchemeRepository;
        this.welfareMatchingService = welfareMatchingService;
        this.userRepository = userRepository;
    }

    @GetMapping("/earnings/summary/{userId}")
    public ResponseEntity<Map<String, Object>> getEarningsSummary(@PathVariable Long userId, Authentication authentication) {
        // Verify the authenticated user can only access their own data
        String phoneNumber = authentication.getName();
        User authenticatedUser = userRepository.findByPhoneNumber(phoneNumber)
                .orElseThrow(() -> new IllegalArgumentException("User not found: " + phoneNumber));
        
        if (!authenticatedUser.getId().equals(userId)) {
            return ResponseEntity.badRequest().body(Map.of("error", "You can only view your own earnings"));
        }
        
        log.info("Getting earnings summary for user {}", userId);
        
        List<Earning> allEarnings = earningRepository.findByUserId(userId);
        BigDecimal totalEarnings = allEarnings.stream()
                .map(Earning::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        
        LocalDate now = LocalDate.now();
        YearMonth currentMonth = YearMonth.from(now);
        BigDecimal monthEarnings = allEarnings.stream()
                .filter(e -> {
                    LocalDate earningDate = e.getDate();
                    if (earningDate == null) return false;
                    YearMonth earningMonth = YearMonth.from(earningDate);
                    return earningMonth.equals(currentMonth);
                })
                .map(Earning::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        
        Map<String, Object> summary = new HashMap<>();
        summary.put("totalEarnings", totalEarnings);
        summary.put("monthEarnings", monthEarnings);
        
        return ResponseEntity.ok(summary);
    }

    @GetMapping("/earnings/recent/{userId}")
    public ResponseEntity<List<Earning>> getRecentEarnings(@PathVariable Long userId, Authentication authentication) {
        // Verify the authenticated user can only access their own data
        String phoneNumber = authentication.getName();
        User authenticatedUser = userRepository.findByPhoneNumber(phoneNumber)
                .orElseThrow(() -> new IllegalArgumentException("User not found: " + phoneNumber));
        
        if (!authenticatedUser.getId().equals(userId)) {
            return ResponseEntity.badRequest().build();
        }
        
        log.info("Getting recent earnings for user {}", userId);
        
        List<Earning> earnings = earningRepository.findByUserIdOrderByDateDesc(userId);
        return ResponseEntity.ok(earnings);
    }

    @GetMapping("/earnings/user/{userId}")
    public ResponseEntity<List<Earning>> getUserEarnings(@PathVariable Long userId, Authentication authentication) {
        // Verify the authenticated user can only access their own data
        String phoneNumber = authentication.getName();
        User authenticatedUser = userRepository.findByPhoneNumber(phoneNumber)
                .orElseThrow(() -> new IllegalArgumentException("User not found: " + phoneNumber));
        
        if (!authenticatedUser.getId().equals(userId)) {
            return ResponseEntity.badRequest().build();
        }
        
        log.info("Getting all earnings for user {}", userId);
        
        List<Earning> earnings = earningRepository.findByUserIdOrderByDateDesc(userId);
        return ResponseEntity.ok(earnings);
    }

    @PostMapping("/earnings/add")
    public ResponseEntity<?> addEarning(@RequestBody Map<String, Object> earningData, Authentication authentication) {
        log.info("Adding earning for user: {}", earningData.get("userId"));
        
        try {
            // Get the authenticated user's ID from their phone number
            String phoneNumber = authentication.getName();
            User authenticatedUser = userRepository.findByPhoneNumber(phoneNumber)
                    .orElseThrow(() -> new IllegalArgumentException("User not found: " + phoneNumber));
            
            // Verify the userId in the request matches the authenticated user
            Long requestUserId = Long.parseLong(earningData.get("userId").toString());
            if (!authenticatedUser.getId().equals(requestUserId)) {
                return ResponseEntity.badRequest().body("You can only add earnings to your own account");
            }
            
            Earning earning = new Earning();
            earning.setAmount(new BigDecimal(earningData.get("amount").toString()));
            earning.setDate(LocalDate.parse(earningData.get("date").toString()));
            earning.setSource(earningData.get("source").toString());
            earning.setDescription(earningData.get("description") != null ? earningData.get("description").toString() : null);
            
            // Set user relationship - we already verified this is the authenticated user
            earning.setUser(authenticatedUser);
            
            Earning savedEarning = earningRepository.save(earning);
            return ResponseEntity.ok(savedEarning);
        } catch (NumberFormatException e) {
            log.error("Invalid user ID format", e);
            return ResponseEntity.badRequest().body("Invalid user ID format");
        } catch (Exception e) {
            log.error("Error adding earning", e);
            return ResponseEntity.badRequest().body("Error adding earning: " + e.getMessage());
        }
    }

    @GetMapping("/schemes/all")
    public ResponseEntity<List<WelfareScheme>> getAllSchemes() {
        log.info("Getting all schemes");
        
        List<WelfareScheme> schemes = welfareSchemeRepository.findByActiveTrue();
        return ResponseEntity.ok(schemes);
    }

    @GetMapping("/schemes/search")
    public ResponseEntity<List<WelfareScheme>> searchSchemes(@RequestParam Integer age, 
                                                               @RequestParam String state) {
        log.info("Searching schemes for age {} and state {}", age, state);
        
        if (age == null || age < 1 || age > 100) {
            return ResponseEntity.badRequest().build();
        }
        
        if (state == null || state.trim().isEmpty()) {
            return ResponseEntity.badRequest().build();
        }

        List<WelfareScheme> schemes = welfareSchemeRepository.findEligibleSchemes(age, state);
        return ResponseEntity.ok(schemes);
    }
}