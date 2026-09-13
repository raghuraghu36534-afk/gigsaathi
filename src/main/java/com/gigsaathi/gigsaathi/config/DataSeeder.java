package com.gigsaathi.gigsaathi.config;

import com.gigsaathi.gigsaathi.model.WelfareScheme;
import com.gigsaathi.gigsaathi.repository.WelfareSchemeRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Component
public class DataSeeder implements CommandLineRunner {

    private static final Logger log = LoggerFactory.getLogger(DataSeeder.class);

    private final WelfareSchemeRepository welfareSchemeRepository;

    public DataSeeder(WelfareSchemeRepository welfareSchemeRepository) {
        this.welfareSchemeRepository = welfareSchemeRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        long existingCount = welfareSchemeRepository.count();
        
        if (existingCount == 0) {
            log.info("No existing welfare schemes found. Seeding initial data...");
            seedCentralGovernmentSchemes();
            seedStateGovernmentSchemes();
            
            long count = welfareSchemeRepository.count();
            log.info("Seeded {} welfare schemes successfully", count);
        } else {
            log.info("Found {} existing welfare schemes. Skipping seed.", existingCount);
        }
    }

    private void seedCentralGovernmentSchemes() {
        // Pradhan Mantri Shram Yogi Maan-Dhan (PM-SYM)
        WelfareScheme pmsym = new WelfareScheme();
        pmsym.setSchemeCode("PM-SYM");
        pmsym.setName("Pradhan Mantri Shram Yogi Maan-Dhan (PM-SYM)");
        pmsym.setDescription("A pension scheme for unorganized sector workers providing minimum assured pension of ₹3000/- per month after attaining the age of 60 years.");
        pmsym.setCategory("Pension");
        pmsym.setGovernmentLevel("CENTRAL");
        pmsym.setMinistry("Ministry of Labour and Employment");
        pmsym.setState("ALL");
        pmsym.setMinAge(18);
        pmsym.setMaxAge(40);
        pmsym.setMinMonthlyEarning(new BigDecimal("0"));
        pmsym.setMaxMonthlyEarning(new BigDecimal("15000"));
        pmsym.setGenderEligibility("ALL");
        pmsym.setOccupationEligibility("Unorganized Sector Workers");
        pmsym.setWorkerType("Unorganized Sector");
        pmsym.setBenefits("Minimum assured pension of ₹3000/- per month after age 60");
        pmsym.setApplicationProcess("Registration through Common Service Centers (CSC) or online portal");
        pmsym.setRequiredDocuments("Aadhaar Card, Bank Account, Mobile Number");
        pmsym.setOfficialWebsite("https://pmsym.gov.in");
        pmsym.setHelpline("1800-11-0044");
        pmsym.setActive(true);
        pmsym.setCreatedAt(LocalDateTime.now());
        pmsym.setUpdatedAt(LocalDateTime.now());
        welfareSchemeRepository.save(pmsym);

        // Atal Pension Yojana (APY)
        WelfareScheme apy = new WelfareScheme();
        apy.setSchemeCode("APY");
        apy.setName("Atal Pension Yojana (APY)");
        apy.setDescription("A pension scheme focused on the unorganized sector workers with government co-contribution providing minimum pension of ₹1000 to ₹5000 per month.");
        apy.setCategory("Pension");
        apy.setGovernmentLevel("CENTRAL");
        apy.setMinistry("Ministry of Finance");
        apy.setState("ALL");
        apy.setMinAge(18);
        apy.setMaxAge(40);
        apy.setMinMonthlyEarning(new BigDecimal("0"));
        apy.setMaxMonthlyEarning(new BigDecimal("20000"));
        apy.setGenderEligibility("ALL");
        apy.setOccupationEligibility("Unorganized Sector Workers");
        apy.setWorkerType("Unorganized Sector");
        apy.setBenefits("Pension from ₹1000 to ₹5000 per month after age 60");
        apy.setApplicationProcess("Registration through banks or online portal");
        apy.setRequiredDocuments("Aadhaar Card, Mobile Number, Bank Account");
        apy.setOfficialWebsite("https://nps.gov.in/APY");
        apy.setHelpline("1800-11-0044");
        apy.setActive(true);
        apy.setCreatedAt(LocalDateTime.now());
        apy.setUpdatedAt(LocalDateTime.now());
        welfareSchemeRepository.save(apy);

        // National Social Assistance Programme (NSAP)
        WelfareScheme nsap = new WelfareScheme();
        nsap.setSchemeCode("NSAP");
        nsap.setName("National Social Assistance Programme (NSAP)");
        nsap.setDescription("Central government social security programme for elderly, widows, and disabled persons belonging to below poverty line households.");
        nsap.setCategory("Social Security");
        nsap.setGovernmentLevel("CENTRAL");
        nsap.setMinistry("Ministry of Rural Development");
        nsap.setState("ALL");
        nsap.setMinAge(60);
        nsap.setMaxAge(100);
        nsap.setMinMonthlyEarning(new BigDecimal("0"));
        nsap.setMaxMonthlyEarning(new BigDecimal("10000"));
        nsap.setGenderEligibility("ALL");
        nsap.setOccupationEligibility("BPL Households");
        nsap.setBenefits("Financial assistance for elderly, widows, and disabled persons");
        nsap.setApplicationProcess("Through State Governments and UT Administrations");
        nsap.setRequiredDocuments("Aadhaar Card, BPL Card, Bank Account");
        nsap.setOfficialWebsite("https://nsap.gov.in");
        nsap.setHelpline("1800-11-0044");
        nsap.setActive(true);
        nsap.setCreatedAt(LocalDateTime.now());
        nsap.setUpdatedAt(LocalDateTime.now());
        welfareSchemeRepository.save(nsap);

        // Pradhan Mantri Jan Dhan Yojana (PMJDY)
        WelfareScheme pmjdy = new WelfareScheme();
        pmjdy.setSchemeCode("PMJDY");
        pmjdy.setName("Pradhan Mantri Jan Dhan Yojana (PMJDY)");
        pmjdy.setDescription("Financial inclusion program providing access to banking services with accidental insurance cover and overdraft facility.");
        pmjdy.setCategory("Financial Inclusion");
        pmjdy.setGovernmentLevel("CENTRAL");
        pmjdy.setMinistry("Ministry of Finance");
        pmjdy.setState("ALL");
        pmjdy.setMinAge(18);
        pmjdy.setMaxAge(100);
        pmjdy.setMinMonthlyEarning(new BigDecimal("0"));
        pmjdy.setMaxMonthlyEarning(new BigDecimal("50000"));
        pmjdy.setGenderEligibility("ALL");
        pmjdy.setBenefits("Accidental insurance cover of ₹2 lakh, overdraft facility up to ₹10,000");
        pmjdy.setApplicationProcess("Visit any bank branch with Aadhaar card");
        pmjdy.setRequiredDocuments("Aadhaar Card, PAN Card, Mobile Number");
        pmjdy.setOfficialWebsite("https://pmjdy.gov.in");
        pmjdy.setHelpline("1800-11-0044");
        pmjdy.setActive(true);
        pmjdy.setCreatedAt(LocalDateTime.now());
        pmjdy.setUpdatedAt(LocalDateTime.now());
        welfareSchemeRepository.save(pmjdy);

        // Pradhan Mantri Jeevan Jyoti Bima Yojana (PMJJBY)
        WelfareScheme pmjjby = new WelfareScheme();
        pmjjby.setSchemeCode("PMJJBY");
        pmjjby.setName("Pradhan Mantri Jeevan Jyoti Bima Yojana (PMJJBY)");
        pmjjby.setDescription("Life insurance scheme offering death cover of ₹2 lakh at an annual premium of ₹330.");
        pmjjby.setCategory("Insurance");
        pmjjby.setGovernmentLevel("CENTRAL");
        pmjjby.setMinistry("Ministry of Finance");
        pmjjby.setState("ALL");
        pmjjby.setMinAge(18);
        pmjjby.setMaxAge(50);
        pmjjby.setMinMonthlyEarning(new BigDecimal("0"));
        pmjjby.setMaxMonthlyEarning(new BigDecimal("50000"));
        pmjjby.setGenderEligibility("ALL");
        pmjjby.setBenefits("Death cover of ₹2 lakh, accidental death cover of ₹2 lakh");
        pmjjby.setApplicationProcess("Auto-enrollment through bank accounts, or enroll through banks");
        pmjjby.setRequiredDocuments("Aadhaar Card, Bank Account");
        pmjjby.setOfficialWebsite("https://pmjjby.gov.in");
        pmjjby.setHelpline("1800-11-0044");
        pmjjby.setActive(true);
        pmjjby.setCreatedAt(LocalDateTime.now());
        pmjjby.setUpdatedAt(LocalDateTime.now());
        welfareSchemeRepository.save(pmjjby);

        // Pradhan Mantri Suraksha Bima Yojana (PMSBY)
        WelfareScheme pmsby = new WelfareScheme();
        pmsby.setSchemeCode("PMSBY");
        pmsby.setName("Pradhan Mantri Suraksha Bima Yojana (PMSBY)");
        pmsby.setDescription("Accidental insurance scheme offering ₹2 lakh cover for death and permanent disability at annual premium of ₹20.");
        pmsby.setCategory("Insurance");
        pmsby.setGovernmentLevel("CENTRAL");
        pmsby.setMinistry("Ministry of Finance");
        pmsby.setState("ALL");
        pmsby.setMinAge(18);
        pmsby.setMaxAge(70);
        pmsby.setMinMonthlyEarning(new BigDecimal("0"));
        pmsby.setMaxMonthlyEarning(new BigDecimal("50000"));
        pmsby.setGenderEligibility("ALL");
        pmsby.setBenefits("Accidental death cover of ₹2 lakh, permanent total disability cover of ₹2 lakh");
        pmsby.setApplicationProcess("Auto-enrollment through bank accounts, or enroll through banks");
        pmsby.setRequiredDocuments("Aadhaar Card, Bank Account");
        pmsby.setOfficialWebsite("https://pmsby.gov.in");
        pmsby.setHelpline("1800-11-0044");
        pmsby.setActive(true);
        pmsby.setCreatedAt(LocalDateTime.now());
        pmsby.setUpdatedAt(LocalDateTime.now());
        welfareSchemeRepository.save(pmsby);

        // Ayushman Bharat Pradhan Mantri Jan Arogya Yojana (AB-PMJAY)
        WelfareScheme abpmjay = new WelfareScheme();
        abpmjay.setSchemeCode("AB-PMJAY");
        abpmjay.setName("Ayushman Bharat Pradhan Mantri Jan Arogya Yojana (AB-PMJAY)");
        abpmjay.setDescription("Health insurance scheme providing health coverage of ₹5 lakh per family per year for secondary and tertiary care hospitalization.");
        abpmjay.setCategory("Healthcare");
        abpmjay.setGovernmentLevel("CENTRAL");
        abpmjay.setMinistry("Ministry of Health and Family Welfare");
        abpmjay.setState("ALL");
        abpmjay.setMinAge(1);
        abpmjay.setMaxAge(100);
        abpmjay.setMinMonthlyEarning(new BigDecimal("0"));
        abpmjay.setMaxMonthlyEarning(new BigDecimal("25000"));
        abpmjay.setGenderEligibility("ALL");
        abpmjay.setBenefits("Health coverage of ₹5 lakh per family per year");
        abpmjay.setApplicationProcess("Verify eligibility through e-KYC and register online");
        abpmjay.setRequiredDocuments("Aadhaar Card, Ration Card, Mobile Number");
        abpmjay.setOfficialWebsite("https://abpmjay.gov.in");
        abpmjay.setHelpline("1800-11-0044");
        abpmjay.setActive(true);
        abpmjay.setCreatedAt(LocalDateTime.now());
        abpmjay.setUpdatedAt(LocalDateTime.now());
        welfareSchemeRepository.save(abpmjay);

        // PM SVANidhi
        WelfareScheme pmsvanidhi = new WelfareScheme();
        pmsvanidhi.setSchemeCode("PM-SVANIDHI");
        pmsvanidhi.setName("Pradhan Mantri Street Vendor's AtmaNirbhar Nidhi (PM SVANidhi)");
        pmsvanidhi.setDescription("Micro-credit scheme to provide working capital loan of ₹10,000 to street vendors to restart their businesses.");
        pmsvanidhi.setCategory("Financial Assistance");
        pmsvanidhi.setGovernmentLevel("CENTRAL");
        pmsvanidhi.setMinistry("Ministry of Housing and Urban Affairs");
        pmsvanidhi.setState("ALL");
        pmsvanidhi.setMinAge(18);
        pmsvanidhi.setMaxAge(65);
        pmsvanidhi.setMinMonthlyEarning(new BigDecimal("0"));
        pmsvanidhi.setMaxMonthlyEarning(new BigDecimal("15000"));
        pmsvanidhi.setGenderEligibility("ALL");
        pmsvanidhi.setOccupationEligibility("Street Vendors");
        pmsvanidhi.setBenefits("Working capital loan of ₹10,000, interest subsidy on timely repayment");
        pmsvanidhi.setApplicationProcess("Online registration through PM SVANidhi portal");
        pmsvanidhi.setRequiredDocuments("Aadhaar Card, Vendor ID, Mobile Number");
        pmsvanidhi.setOfficialWebsite("https://pmsvanidhi.mohua.gov.in");
        pmsvanidhi.setHelpline("1800-11-0044");
        pmsvanidhi.setActive(true);
        pmsvanidhi.setCreatedAt(LocalDateTime.now());
        pmsvanidhi.setUpdatedAt(LocalDateTime.now());
        welfareSchemeRepository.save(pmsvanidhi);

        log.info("Seeded central government schemes");
    }

    private void seedStateGovernmentSchemes() {
        // Karnataka Sandhya Suraksha Yojana
        WelfareScheme karnataka = new WelfareScheme();
        karnataka.setSchemeCode("KARNATAKA-SSY");
        karnataka.setName("Karnataka Sandhya Suraksha Yojana");
        karnataka.setDescription("Karnataka state social security scheme for unorganized sector workers providing financial assistance and pension benefits.");
        karnataka.setCategory("Social Security");
        karnataka.setGovernmentLevel("STATE");
        karnataka.setMinistry("Department of Labour");
        karnataka.setState("KARNATAKA");
        karnataka.setMinAge(18);
        karnataka.setMaxAge(70);
        karnataka.setMinMonthlyEarning(new BigDecimal("0"));
        karnataka.setMaxMonthlyEarning(new BigDecimal("30000"));
        karnataka.setGenderEligibility("ALL");
        karnataka.setOccupationEligibility("Unorganized Sector Workers");
        karnataka.setBenefits("Financial assistance, pension benefits, health insurance");
        karnataka.setApplicationProcess("Through Karnataka Labour Department");
        karnataka.setRequiredDocuments("Aadhaar Card, Bank Account, Mobile Number");
        karnataka.setOfficialWebsite("https://labour.karnataka.gov.in");
        karnataka.setHelpline("1800-425-1234");
        karnataka.setActive(true);
        karnataka.setCreatedAt(LocalDateTime.now());
        karnataka.setUpdatedAt(LocalDateTime.now());
        welfareSchemeRepository.save(karnataka);

        // Maharashtra Shram Yogi Madad Yojana
        WelfareScheme maharashtra = new WelfareScheme();
        maharashtra.setSchemeCode("MAHARASHTRA-SYMY");
        maharashtra.setName("Maharashtra Shram Yogi Madad Yojana");
        maharashtra.setDescription("Maharashtra state scheme for gig workers and unorganized sector workers providing financial assistance and support services.");
        maharashtra.setCategory("Social Security");
        maharashtra.setGovernmentLevel("STATE");
        maharashtra.setMinistry("Department of Labour");
        maharashtra.setState("MAHARASHTRA");
        maharashtra.setMinAge(18);
        maharashtra.setMaxAge(65);
        maharashtra.setMinMonthlyEarning(new BigDecimal("0"));
        maharashtra.setMaxMonthlyEarning(new BigDecimal("25000"));
        maharashtra.setGenderEligibility("ALL");
        maharashtra.setOccupationEligibility("Gig Workers, Unorganized Sector Workers");
        maharashtra.setBenefits("Financial assistance, skill development, healthcare support");
        maharashtra.setApplicationProcess("Through Maharashtra Labour Department");
        maharashtra.setRequiredDocuments("Aadhaar Card, Bank Account, Mobile Number");
        maharashtra.setOfficialWebsite("https://labour.maharashtra.gov.in");
        maharashtra.setHelpline("1800-102-0182");
        maharashtra.setActive(true);
        maharashtra.setCreatedAt(LocalDateTime.now());
        maharashtra.setUpdatedAt(LocalDateTime.now());
        welfareSchemeRepository.save(maharashtra);

        // Tamil Nadu Unorganized Workers Social Security Scheme
        WelfareScheme tamilNadu = new WelfareScheme();
        tamilNadu.setSchemeCode("CM-SK");
        tamilNadu.setName("Tamil Nadu Chief Minister's Unorganized Workers Social Security Scheme");
        tamilNadu.setDescription("Tamil Nadu state scheme providing life insurance, accidental insurance, and pension benefits to unorganized sector workers.");
        tamilNadu.setCategory("Social Security");
        tamilNadu.setGovernmentLevel("STATE");
        tamilNadu.setMinistry("Department of Labour");
        tamilNadu.setState("TAMIL_NADU");
        tamilNadu.setMinAge(18);
        tamilNadu.setMaxAge(60);
        tamilNadu.setMinMonthlyEarning(new BigDecimal("0"));
        tamilNadu.setMaxMonthlyEarning(new BigDecimal("35000"));
        tamilNadu.setGenderEligibility("ALL");
        tamilNadu.setOccupationEligibility("Unorganized Sector Workers");
        tamilNadu.setBenefits("Life insurance, accidental insurance, pension after age 60");
        tamilNadu.setApplicationProcess("Through Tamil Nadu Labour Department");
        tamilNadu.setRequiredDocuments("Aadhaar Card, Bank Account, Mobile Number");
        tamilNadu.setOfficialWebsite("https://labour.tn.gov.in");
        tamilNadu.setHelpline("1800-425-2699");
        tamilNadu.setActive(true);
        tamilNadu.setCreatedAt(LocalDateTime.now());
        tamilNadu.setUpdatedAt(LocalDateTime.now());
        welfareSchemeRepository.save(tamilNadu);

        // Telangana Gig Workers Welfare Scheme
        WelfareScheme telangana = new WelfareScheme();
        telangana.setSchemeCode("TS-GIG");
        telangana.setName("Telangana Gig Workers Welfare Scheme");
        telangana.setDescription("Telangana state scheme providing welfare benefits to gig workers including financial assistance and healthcare support.");
        telangana.setCategory("Worker Welfare");
        telangana.setGovernmentLevel("STATE");
        telangana.setMinistry("Department of Labour");
        telangana.setState("TELANGANA");
        telangana.setMinAge(18);
        telangana.setMaxAge(65);
        telangana.setMinMonthlyEarning(new BigDecimal("0"));
        telangana.setMaxMonthlyEarning(new BigDecimal("30000"));
        telangana.setGenderEligibility("ALL");
        telangana.setOccupationEligibility("Gig Workers");
        telangana.setBenefits("Financial assistance, healthcare, skill development");
        telangana.setApplicationProcess("Through Telangana Labour Department");
        telangana.setRequiredDocuments("Aadhaar Card, Bank Account, Mobile Number");
        telangana.setOfficialWebsite("https://labour.telangana.gov.in");
        telangana.setHelpline("1800-425-3636");
        telangana.setActive(true);
        telangana.setCreatedAt(LocalDateTime.now());
        telangana.setUpdatedAt(LocalDateTime.now());
        welfareSchemeRepository.save(telangana);

        // Kerala Gig Workers Welfare Scheme
        WelfareScheme kerala = new WelfareScheme();
        kerala.setSchemeCode("KL-GIG");
        kerala.setName("Kerala Gig Workers Welfare Scheme");
        kerala.setDescription("Kerala state scheme providing comprehensive welfare benefits to gig workers including financial assistance and social security.");
        kerala.setCategory("Worker Welfare");
        kerala.setGovernmentLevel("STATE");
        kerala.setMinistry("Department of Labour");
        kerala.setState("KERALA");
        kerala.setMinAge(18);
        kerala.setMaxAge(60);
        kerala.setMinMonthlyEarning(new BigDecimal("0"));
        kerala.setMaxMonthlyEarning(new BigDecimal("32000"));
        kerala.setGenderEligibility("ALL");
        kerala.setOccupationEligibility("Gig Workers");
        kerala.setBenefits("Financial assistance, pension, healthcare, education support");
        kerala.setApplicationProcess("Through Kerala Labour Department");
        kerala.setRequiredDocuments("Aadhaar Card, Bank Account, Mobile Number");
        kerala.setOfficialWebsite("https://labour.kerala.gov.in");
        kerala.setHelpline("1800-425-4555");
        kerala.setActive(true);
        kerala.setCreatedAt(LocalDateTime.now());
        kerala.setUpdatedAt(LocalDateTime.now());
        welfareSchemeRepository.save(kerala);

        // Gujarat Unorganized Workers Social Security Scheme
        WelfareScheme gujarat = new WelfareScheme();
        gujarat.setSchemeCode("GJ-UWSS");
        gujarat.setName("Gujarat Unorganized Workers Social Security Scheme");
        gujarat.setDescription("Gujarat state scheme providing social security benefits to unorganized sector workers including insurance and pension.");
        gujarat.setCategory("Social Security");
        gujarat.setGovernmentLevel("STATE");
        gujarat.setMinistry("Department of Labour");
        gujarat.setState("GUJARAT");
        gujarat.setMinAge(18);
        gujarat.setMaxAge(65);
        gujarat.setMinMonthlyEarning(new BigDecimal("0"));
        gujarat.setMaxMonthlyEarning(new BigDecimal("28000"));
        gujarat.setGenderEligibility("ALL");
        gujarat.setOccupationEligibility("Unorganized Sector Workers");
        gujarat.setBenefits("Life insurance, accidental insurance, pension benefits");
        gujarat.setApplicationProcess("Through Gujarat Labour Department");
        gujarat.setRequiredDocuments("Aadhaar Card, Bank Account, Mobile Number");
        gujarat.setOfficialWebsite("https://labour.gujarat.gov.in");
        gujarat.setHelpline("1800-103-0808");
        gujarat.setActive(true);
        gujarat.setCreatedAt(LocalDateTime.now());
        gujarat.setUpdatedAt(LocalDateTime.now());
        welfareSchemeRepository.save(gujarat);

        log.info("Seeded state government schemes");
    }
}
