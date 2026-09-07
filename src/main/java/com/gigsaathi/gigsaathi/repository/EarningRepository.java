package com.gigsaathi.gigsaathi.repository;

import com.gigsaathi.gigsaathi.model.Earning;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EarningRepository extends JpaRepository<Earning, Long> {
    List<Earning> findByUserId(Long userId);
}
