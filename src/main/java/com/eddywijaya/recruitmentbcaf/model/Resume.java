package com.eddywijaya.recruitmentbcaf.model;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDate;
import java.util.Date;

@Entity
@Table(name = "Resume")
public class Resume {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "IdResume") // Updated to Pascal case
    private Long id;

    @Column(name = "FullName", length = 100)
    private String fullName;

    @Column(name = "Email", nullable = false, length = 100)
    private String email;

    @Column(name = "BirthPlace", length = 100) // Updated to Pascal case
    private String birthPlace;

    @Column(name = "BirthDate") // Added name in Pascal case
    private LocalDate birthDate;

    @Column(name = "SchoolName", length = 100) // Updated to Pascal case
    private String schoolName;

    @Column(name = "LastEducation", length = 50) // Updated to Pascal case
    private String lastEducation;

    @Column(name = "GraduationYear") // Added name in Pascal case
    private Integer graduationYear;

    @Column(name = "GPA") // Added name in Pascal case
    private Double gpa;

    @Column(name = "IdNumber", length = 20) // Updated to Pascal case
    private String idNumber;

    @Column(name = "Gender", length = 10) // Updated to Pascal case
    private String gender;

    @Column(name = "Religion", length = 50) // Updated to Pascal case
    private String religion;

    @Column(name = "MaritalStatus", length = 20) // Updated to Pascal case
    private String maritalStatus;

    @Column(name = "Address", columnDefinition = "TEXT") // Updated to Pascal case
    private String address;

    @Column(name = "PhoneNumber", length = 15) // Updated to Pascal case
    private String phoneNumber;

    @Column(name = "AIRecommendation", columnDefinition = "TEXT") // Updated to Pascal case
    private String aiRecommendation;

    @ManyToOne
    @JoinColumn(name = "IdStages", foreignKey = @ForeignKey(name = "fk-to-stages")) // Updated to Pascal case
    private Stages stages;

    @ManyToOne
    @JoinColumn(name = "IdRecruitment")
    private Recruitment recruitment;

    @Column(name = "CreatedBy", updatable = false, nullable = false)
    private Long createdBy;

    @CreationTimestamp
    @Column(name = "CreatedAt", updatable = false)
    private Date createdAt;

    @Column(name = "ModifiedBy", insertable = false)
    private Long modifiedBy;

    @UpdateTimestamp
    @Column(name = "ModifiedAt")
    private Date modifiedAt;

    @Column(name = "IsActive") // Updated to Pascal case
    private Boolean isActive;

    // Getters and Setters

    public Recruitment getRecruitment() {
        return recruitment;
    }

    public void setRecruitment(Recruitment recruitment) {
        this.recruitment = recruitment;
    }

    public Long getId() {
        return id;
    }

    public String getBirthPlace() {
        return birthPlace;
    }

    public void setBirthPlace(String birthPlace) {
        this.birthPlace = birthPlace;
    }

    public LocalDate getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(LocalDate birthDate) {
        this.birthDate = birthDate;
    }

    public String getSchoolName() {
        return schoolName;
    }

    public void setSchoolName(String schoolName) {
        this.schoolName = schoolName;
    }

    public String getLastEducation() {
        return lastEducation;
    }

    public void setLastEducation(String lastEducation) {
        this.lastEducation = lastEducation;
    }

    public Integer getGraduationYear() {
        return graduationYear;
    }

    public void setGraduationYear(Integer graduationYear) {
        this.graduationYear = graduationYear;
    }

    public Double getGpa() {
        return gpa;
    }

    public void setGpa(Double gpa) {
        this.gpa = gpa;
    }

    public String getIdNumber() {
        return idNumber;
    }

    public void setIdNumber(String idNumber) {
        this.idNumber = idNumber;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getReligion() {
        return religion;
    }

    public void setReligion(String religion) {
        this.religion = religion;
    }

    public String getMaritalStatus() {
        return maritalStatus;
    }

    public void setMaritalStatus(String maritalStatus) {
        this.maritalStatus = maritalStatus;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getAiRecommendation() {
        return aiRecommendation;
    }

    public void setAiRecommendation(String aiRecommendation) {
        this.aiRecommendation = aiRecommendation;
    }

    public Stages getStages() {
        return stages;
    }

    public void setStages(Stages stages) {
        this.stages = stages;
    }

    public Long getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(Long createdBy) {
        this.createdBy = createdBy;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Long getModifiedBy() {
        return modifiedBy;
    }

    public void setModifiedBy(Long modifiedBy) {
        this.modifiedBy = modifiedBy;
    }

    public Date getModifiedAt() {
        return modifiedAt;
    }

    public void setModifiedAt(Date modifiedAt) {
        this.modifiedAt = modifiedAt;
    }

    public Boolean getActive() {
        return isActive;
    }

    public void setActive(Boolean active) {
        isActive = active;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
