package com.gigsaathi.gigsaathi.service;

import com.gigsaathi.gigsaathi.model.Earning;
import com.gigsaathi.gigsaathi.model.User;
import com.gigsaathi.gigsaathi.model.WelfareScheme;
import com.gigsaathi.gigsaathi.repository.EarningRepository;
import com.gigsaathi.gigsaathi.repository.WelfareSchemeRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class WelfareMatchingService {

    private static final Logger log = LoggerFactory.getLogger(WelfareMatchingService.class);

    private final EarningRepository earningRepository;
    private final WelfareSchemeRepository welfareSchemeRepository;

    public WelfareMatchingService(EarningRepository earningRepository, WelfareSchemeRepository welfareSchemeRepository) {
        this.earningRepository = earningRepository;
        this.welfareSchemeRepository = welfareSchemeRepository;
    }

    public BigDecimal calculateAverageMonthlyEarning(User user) {
        List<Earning> earnings = earningRepository.findByUserId(user.getId());
        log.info("Found {} earning records for user {}", earnings.size(), user.getPhoneNumber());
        
        if (earnings.isEmpty()) {
            log.info("No earnings found for user {}, returning 0", user.getPhoneNumber());
            return new BigDecimal("0");
        }

        BigDecimal total = earnings.stream()
                .map(Earning::getAmount)
                .reduce(new BigDecimal("0"), BigDecimal::add);
        
        BigDecimal average = total.divide(BigDecimal.valueOf(earnings.size()), 2, java.math.RoundingMode.HALF_UP);
        log.info("Calculated average monthly earning for user {}: ₹{}", user.getPhoneNumber(), average);
        
        return average;
    }

    public List<WelfareScheme> findEligibleSchemes(User user) {
        BigDecimal averageMonthlyEarning = calculateAverageMonthlyEarning(user);
        List<WelfareScheme> allSchemes = welfareSchemeRepository.findByActiveTrue();
        
        log.info("Checking {} active schemes for user {} (age: {}, state: {}, avg earning: ₹{})", 
                allSchemes.size(), user.getPhoneNumber(), user.getAge(), user.getState(), averageMonthlyEarning);
        
        return allSchemes.stream()
                .filter(scheme -> {
                    boolean ageMatch = checkAgeMatch(user.getAge(), scheme.getMinAge(), scheme.getMaxAge());
                    boolean earningMatch = checkEarningMatch(averageMonthlyEarning, scheme.getMinMonthlyEarning(), scheme.getMaxMonthlyEarning());
                    boolean stateMatch = checkStateMatch(user.getState(), scheme.getState());
                    boolean activeMatch = scheme.getActive() != null && scheme.getActive();
                    
                    log.info("Scheme '{}': Age match={}, Earning match={}, State match={}, Active={}", 
                            scheme.getName(), ageMatch, earningMatch, stateMatch, activeMatch);
                    
                    return ageMatch && earningMatch && stateMatch && activeMatch;
                })
                .collect(Collectors.toList());
    }

    public List<WelfareScheme> findEligibleSchemes(Integer age, String state) {
        if (age == null || age < 1 || age > 100) {
            log.warn("Invalid age provided: {}", age);
            throw new IllegalArgumentException("Age must be between 1 and 100");
        }
        
        if (state == null || state.trim().isEmpty()) {
            log.warn("Invalid state provided: {}", state);
            throw new IllegalArgumentException("State is required");
        }

        List<WelfareScheme> schemes = welfareSchemeRepository.findEligibleSchemes(age, state);
        log.info("Found {} eligible schemes for age {} and state {}", schemes.size(), age, state);
        
        return schemes;
    }

    public List<WelfareScheme> searchSchemes(String keyword) {
        List<WelfareScheme> schemes = welfareSchemeRepository.searchSchemes(keyword);
        log.info("Found {} schemes matching keyword '{}'", schemes.size(), keyword);
        return schemes;
    }

    public List<WelfareScheme> filterSchemes(String governmentLevel, String category, String state) {
        List<WelfareScheme> schemes = welfareSchemeRepository.filterSchemes(governmentLevel, category, state);
        log.info("Found {} schemes with filters - governmentLevel: {}, category: {}, state: {}", 
                schemes.size(), governmentLevel, category, state);
        return schemes;
    }

    public List<WelfareScheme> getCentralSchemes(String state) {
        List<WelfareScheme> schemes = welfareSchemeRepository.findByGovernmentLevel("CENTRAL");
        log.info("Found {} central government schemes", schemes.size());
        return schemes;
    }

    public List<WelfareScheme> getStateSchemes(String state) {
        List<WelfareScheme> schemes = welfareSchemeRepository.findByState(state);
        log.info("Found {} state schemes for {}", schemes.size(), state);
        return schemes;
    }

    public WelfareScheme findSchemeById(Long id) {
        return welfareSchemeRepository.findById(id).orElse(null);
    }

    private boolean checkAgeMatch(Integer userAge, Integer minAge, Integer maxAge) {
        if (userAge == null) {
            return true; // Age not provided, don't filter by age
        }
        if (minAge == null && maxAge == null) {
            return true; // No age restriction
        }
        if (minAge != null && userAge < minAge) {
            return false;
        }
        if (maxAge != null && userAge > maxAge) {
            return false;
        }
        return true;
    }

    private boolean checkEarningMatch(BigDecimal userEarning, BigDecimal minEarning, BigDecimal maxEarning) {
        if (minEarning == null && maxEarning == null) {
            return true; // No earning restriction
        }
        if (minEarning != null && userEarning.compareTo(minEarning) < 0) {
            return false;
        }
        if (maxEarning != null && userEarning.compareTo(maxEarning) > 0) {
            return false;
        }
        return true;
    }

    private boolean checkStateMatch(String userState, String schemeState) {
        if (userState == null || userState.trim().isEmpty()) {
            return true; // State not provided, don't filter by state
        }
        if (schemeState == null || schemeState.trim().isEmpty()) {
            return true; // No state restriction
        }
        String normalizedUserState = userState.trim().toUpperCase();
        String normalizedSchemeState = schemeState.trim().toUpperCase();
        
        return normalizedSchemeState.equals("ALL") || 
               normalizedSchemeState.equals("NATIONAL") || 
               normalizedSchemeState.equals(normalizedUserState);
    }
}
