package com.eddywijaya.recruitmentbcaf.repo;

import com.eddywijaya.recruitmentbcaf.model.Recruitment;
import com.eddywijaya.recruitmentbcaf.model.Stages;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.Optional;

public interface RecruitmentRepo extends JpaRepository<Recruitment,Long> {
    Optional<Recruitment> findTopByOrderByIdDesc();

    Page<Recruitment> findByRecruitmentNameContainingIgnoreCase(Pageable pageable, String value);
    Page<Recruitment> findByRequirementsContainingIgnoreCase(Pageable pageable, String value);
    Page<Recruitment> findByOpenDate(Pageable pageable, LocalDate openDate);
    Page<Recruitment> findByCloseDate(Pageable pageable, LocalDate closeDate);
    Page<Recruitment> findByApproval(Pageable pageable, Character approval);

    List<Recruitment> findByRecruitmentNameContainingIgnoreCase(String value);
    List<Recruitment> findByRequirementsContainingIgnoreCase(String value);
//    List<Recruitment> findByOpenDateContainingIgnoreCase(String value);
    List<Recruitment> findByOpenDate(LocalDate openDate);
    List<Recruitment> findByCloseDate(LocalDate closeDate);
//    List<Recruitment> findByIsActiveContainingIgnoreCase(String value);
//    List<Recruitment> findByStagesContainingIgnoreCase(String value);

    Optional<Recruitment> findByRecruitmentName(String value);

}
