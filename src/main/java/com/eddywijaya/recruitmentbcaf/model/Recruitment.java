package com.eddywijaya.recruitmentbcaf.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "Recruitment")
public class Recruitment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "IdRecruitment") // Updated to Pascal case
    private Long id;

    @Column(name = "RecruitmentName", nullable = false, length = 100) // Updated to Pascal case
    private String recruitmentName;

    @Column(name = "Requirements", nullable = false, columnDefinition = "TEXT") // Updated to Pascal case
    private String requirements;

    @Column(name = "OpenDate") // Updated to Pascal case
    private LocalDate openDate;

    @Column(name = "Approval")
    private Character approval;

    @Column(name = "CloseDate") // Updated to Pascal case
    private LocalDate closeDate;

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

    @ManyToMany
    @JoinTable(
            name = "Recruitment_Stages",
            joinColumns = @JoinColumn(name = "IdRecruitment"),
            inverseJoinColumns = @JoinColumn(name = "IdStages")
    )
    @JsonBackReference
    private List<Stages> stages;

    @OneToMany(mappedBy = "recruitment")
    private List<Resume> resumes;

    // Getters and Setters


    public List<Resume> getResumes() {
        return resumes;
    }

    public void setResumes(List<Resume> resumes) {
        this.resumes = resumes;
    }

    public Long getIdRecruitment() {
        return id;
    }

    public void setIdRecruitment(Long idRecruitment) {
        this.id = idRecruitment;
    }

    public String getRecruitmentName() {
        return recruitmentName;
    }

    public void setRecruitmentName(String recruitmentName) {
        this.recruitmentName = recruitmentName;
    }

    public String getRequirements() {
        return requirements;
    }

    public void setRequirements(String requirements) {
        this.requirements = requirements;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getOpenDate() {
        return openDate;
    }

    public void setOpenDate(LocalDate openDate) {
        this.openDate = openDate;
    }

    public LocalDate getCloseDate() {
        return closeDate;
    }

    public void setCloseDate(LocalDate closeDate) {
        this.closeDate = closeDate;
    }

    public Character getApproval() {
        return approval;
    }

    public void setApproval(Character approval) {
        this.approval = approval;
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

    public List<Stages> getStages() {
        return stages;
    }

    public void setStages(List<Stages> stages) {
        this.stages = stages;
    }
}
