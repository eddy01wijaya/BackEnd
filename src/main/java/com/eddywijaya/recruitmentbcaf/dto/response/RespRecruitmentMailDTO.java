package com.eddywijaya.recruitmentbcaf.dto.response;

import com.eddywijaya.recruitmentbcaf.model.Access;
import com.eddywijaya.recruitmentbcaf.model.Notification;
import com.eddywijaya.recruitmentbcaf.model.Recruitment;
import jakarta.persistence.*;

import java.util.List;

public class RespRecruitmentMailDTO {
    private String email;
    private String code;
    private Recruitment recruitments;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Recruitment getRecruitments() {
        return recruitments;
    }

    public void setRecruitments(Recruitment recruitments) {
        this.recruitments = recruitments;
    }
}
