package com.gigsaathi.gigsaathi.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class WelfareScheme {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String schemeCode;

    private String name;

    @Column(columnDefinition = "TEXT")
    private String description;

    private String category;

    private String governmentLevel;

    private String ministry;

    private String state;

    private Integer minAge;

    private Integer maxAge;

    private BigDecimal minMonthlyEarning;

    private BigDecimal maxMonthlyEarning;

    private String genderEligibility;

    private String occupationEligibility;

    private String workerType;

    @Column(columnDefinition = "TEXT")
    private String eligibilityCriteria;

    @Column(columnDefinition = "TEXT")
    private String benefits;

    @Column(columnDefinition = "TEXT")
    private String applicationProcess;

    @Column(columnDefinition = "TEXT")
    private String requiredDocuments;

    private String officialWebsite;

    private String helpline;

    private Boolean active;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}
