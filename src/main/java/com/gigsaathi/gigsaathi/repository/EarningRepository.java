package com.gigsaathi.gigsaathi.repository;

import com.gigsaathi.gigsaathi.model.Earning;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface EarningRepository extends JpaRepository<Earning, Long> {
    List<Earning> findByUserId(Long userId);
    
    @Query("SELECT e FROM Earning e WHERE e.user.id = :userId ORDER BY e.date DESC")
    List<Earning> findByUserIdOrderByDateDesc(Long userId);
}
