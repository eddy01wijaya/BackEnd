package com.eddywijaya.recruitmentbcaf.dto.response;

import com.eddywijaya.recruitmentbcaf.model.Stages;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.Column;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

public class RespRecruitmentDTO {
    private Long idRecruitment;
    private String recruitmentName;
    private String requirements;
    private LocalDate openDate;
    private LocalDate closeDate;
    private Character approval;
    private List<RespStagesDTO> stages;

    public Long getIdRecruitment() {
        return idRecruitment;
    }

    public void setIdRecruitment(Long idRecruitment) {
        this.idRecruitment = idRecruitment;
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


    public List<RespStagesDTO> getStages() {
        return stages;
    }

    public void setStages(List<RespStagesDTO> stages) {
        this.stages = stages;
    }

    public Character getApproval() {
        return approval;
    }

    public void setApproval(Character approval) {
        this.approval = approval;
    }


    //    public List<Stages> getStages() {
//        return stages;
//    }
//
//    public void setStages(List<Stages> stages) {
//        this.stages = stages;
//    }
}
