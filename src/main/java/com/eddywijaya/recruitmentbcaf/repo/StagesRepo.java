package com.eddywijaya.recruitmentbcaf.repo;

import com.eddywijaya.recruitmentbcaf.model.Resume;
import com.eddywijaya.recruitmentbcaf.model.Stages;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface StagesRepo extends JpaRepository<Stages,Long> {
    Optional<Stages> findTopByOrderByIdDesc();

    // Menggunakan Pageable untuk paginasi
    Page<Stages> findByStagesNameContainingIgnoreCase(Pageable pageable, String stagesName);
    Page<Stages> findByRecruitments_Id(Pageable pageable, Long id);
    // Versi tanpa Pageable (mengembalikan List)
    List<Stages> findByStagesNameContainingIgnoreCase(String stagesName);

//    Optional<Resume> findByResumeName(String value);

}
