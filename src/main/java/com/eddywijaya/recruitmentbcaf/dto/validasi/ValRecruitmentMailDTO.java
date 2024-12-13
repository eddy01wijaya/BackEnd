package com.eddywijaya.recruitmentbcaf.dto.validasi;

import com.eddywijaya.recruitmentbcaf.model.Recruitment;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.*;

import java.util.List;

public class ValRecruitmentMailDTO {
    @NotNull
    @Size(min = 1, message = "Setidaknya harus ada satu email")
    private List<@NotBlank @Pattern(regexp = "^(?=.{1,256})(?=.{1,64}@.{1,255}$)(?:(?![.])[a-zA-Z0-9._%+-]+(?:(?<!\\\\)[.][a-zA-Z0-9-]+)*?)@[a-zA-Z0-9.-]+(?:\\.[a-zA-Z]{2,50})+$",
                message = "Format tidak valid contoh : user_name123@sub.domain.com")String> emails;

    @NotNull
    @JsonProperty("id-rec")
    private Long idRecruitment;


    public @NotNull Long getIdRecruitment() {
        return idRecruitment;
    }

    public void setIdRecruitment(@NotNull Long idRecruitment) {
        this.idRecruitment = idRecruitment;
    }

    public @NotNull @Size(min = 1, message = "Setidaknya harus ada satu email") List<@NotBlank @Pattern(regexp = "^(?=.{1,256})(?=.{1,64}@.{1,255}$)(?:(?![.])[a-zA-Z0-9._%+-]+(?:(?<!\\\\)[.][a-zA-Z0-9-]+)*?)@[a-zA-Z0-9.-]+(?:\\.[a-zA-Z]{2,50})+$",
            message = "Format tidak valid contoh : user_name123@sub.domain.com") String> getEmails() {
        return emails;
    }

    public void setEmails(@NotNull @Size(min = 1, message = "Setidaknya harus ada satu email") List<@NotBlank @Pattern(regexp = "^(?=.{1,256})(?=.{1,64}@.{1,255}$)(?:(?![.])[a-zA-Z0-9._%+-]+(?:(?<!\\\\)[.][a-zA-Z0-9-]+)*?)@[a-zA-Z0-9.-]+(?:\\.[a-zA-Z]{2,50})+$",
            message = "Format tidak valid contoh : user_name123@sub.domain.com") String> emails) {
        this.emails = emails;
    }
}
