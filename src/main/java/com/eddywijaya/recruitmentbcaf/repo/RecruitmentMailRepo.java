package com.eddywijaya.recruitmentbcaf.repo;

import com.eddywijaya.recruitmentbcaf.model.Account;
import com.eddywijaya.recruitmentbcaf.model.RecruitmentMail;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import javax.swing.text.html.Option;
import java.util.List;
import java.util.Optional;

public interface RecruitmentMailRepo extends JpaRepository<RecruitmentMail,Long> {
    Optional<RecruitmentMail> findTopByOrderByIdDesc();
    Optional<RecruitmentMail> findByEmailAndRecruitments_id(String value, Long idRecruitment);
    Optional<RecruitmentMail> findByRecruitments_id(Long idRecruitment);

    Page<RecruitmentMail> findByEmailContainingIgnoreCase(Pageable pageable, String value);

    List<RecruitmentMail> findByEmailContainingIgnoreCase(String value);

}
