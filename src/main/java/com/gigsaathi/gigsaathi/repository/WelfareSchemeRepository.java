package com.gigsaathi.gigsaathi.repository;

import com.gigsaathi.gigsaathi.model.WelfareScheme;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface WelfareSchemeRepository extends JpaRepository<WelfareScheme, Long> {

    List<WelfareScheme> findAll();

    List<WelfareScheme> findByActiveTrue();

    List<WelfareScheme> findByState(String state);

    List<WelfareScheme> findByGovernmentLevel(String governmentLevel);

    List<WelfareScheme> findByCategory(String category);

    @Query("SELECT s FROM WelfareScheme s WHERE s.active = true AND " +
           "(:state IS NULL OR s.state = 'ALL' OR s.state = 'NATIONAL' OR s.state = :state)")
    List<WelfareScheme> findActiveSchemesByState(@Param("state") String state);

    @Query("SELECT s FROM WelfareScheme s WHERE s.active = true AND " +
           "(:age IS NULL OR s.minAge IS NULL OR :age >= s.minAge) AND " +
           "(:age IS NULL OR s.maxAge IS NULL OR :age <= s.maxAge) AND " +
           "(:state IS NULL OR s.state = 'ALL' OR s.state = 'NATIONAL' OR s.state = :state)")
    List<WelfareScheme> findEligibleSchemes(@Param("age") Integer age, 
                                            @Param("state") String state);

    @Query("SELECT s FROM WelfareScheme s WHERE s.active = true AND " +
           "LOWER(s.name) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           "LOWER(s.description) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    List<WelfareScheme> searchSchemes(@Param("keyword") String keyword);

    @Query("SELECT s FROM WelfareScheme s WHERE s.active = true AND " +
           "(:governmentLevel IS NULL OR s.governmentLevel = :governmentLevel) AND " +
           "(:category IS NULL OR s.category = :category) AND " +
           "(:state IS NULL OR s.state = 'ALL' OR s.state = 'NATIONAL' OR s.state = :state)")
    List<WelfareScheme> filterSchemes(@Param("governmentLevel") String governmentLevel,
                                       @Param("category") String category,
                                       @Param("state") String state);

    WelfareScheme findBySchemeCode(String schemeCode);
}
