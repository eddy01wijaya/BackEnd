package com.eddywijaya.recruitmentbcaf.repo;

import com.eddywijaya.recruitmentbcaf.model.Resume;
import com.eddywijaya.recruitmentbcaf.model.Stages;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface ResumeRepo extends JpaRepository<Resume,Long> {
    Optional<Resume> findTopByOrderByIdDesc();

    // Menggunakan Pageable untuk paginasi
    Page<Resume> findByBirthPlaceContainingIgnoreCase(Pageable pageable, String birthPlace);
    Page<Resume> findBySchoolNameContainingIgnoreCase(Pageable pageable, String schoolName);
    Page<Resume> findByLastEducationContainingIgnoreCase(Pageable pageable, String lastEducation);
    Page<Resume> findByIdNumberContainingIgnoreCase(Pageable pageable, String idNumber);
    Page<Resume> findByGenderContainingIgnoreCase(Pageable pageable, String gender);
    Page<Resume> findByReligionContainingIgnoreCase(Pageable pageable, String religion);
    Page<Resume> findByMaritalStatusContainingIgnoreCase(Pageable pageable, String maritalStatus);
    Page<Resume> findByAddressContainingIgnoreCase(Pageable pageable, String address);
    Page<Resume> findByPhoneNumberContainingIgnoreCase(Pageable pageable, String phoneNumber);
    Page<Resume> findByAiRecommendationContainingIgnoreCase(Pageable pageable, String aiRecommendation);

    Page<Resume> findByStages_IdAndRecruitment_Id(Pageable pageable, Integer idStages, Integer idRecruitment);




    // Pencarian langsung tanpa Contains untuk tipe data LocalDate, Integer, Double, dan Boolean
    Page<Resume> findByBirthDate(Pageable pageable, LocalDate birthDate);
    Page<Resume> findByGraduationYear(Pageable pageable, Integer graduationYear);
    Page<Resume> findByGpa(Pageable pageable, Double gpa);
    Page<Resume> findByIsActive(Pageable pageable, Boolean isActive);

    // Versi tanpa Pageable (mengembalikan List)
    List<Resume> findByBirthPlaceContainingIgnoreCase(String birthPlace);
    List<Resume> findBySchoolNameContainingIgnoreCase(String schoolName);
    List<Resume> findByLastEducationContainingIgnoreCase(String lastEducation);
    List<Resume> findByIdNumberContainingIgnoreCase(String idNumber);
    List<Resume> findByGenderContainingIgnoreCase(String gender);
    List<Resume> findByReligionContainingIgnoreCase(String religion);
    List<Resume> findByMaritalStatusContainingIgnoreCase(String maritalStatus);
    List<Resume> findByAddressContainingIgnoreCase(String address);
    List<Resume> findByPhoneNumberContainingIgnoreCase(String phoneNumber);
    List<Resume> findByAiRecommendationContainingIgnoreCase(String aiRecommendation);

    List<Resume> findByBirthDate(LocalDate birthDate);
    List<Resume> findByGraduationYear(Integer graduationYear);
    List<Resume> findByGpa(Double gpa);
    List<Resume> findByIsActive(Boolean isActive);

//    Optional<Resume> findByResumeName(String value);

}
