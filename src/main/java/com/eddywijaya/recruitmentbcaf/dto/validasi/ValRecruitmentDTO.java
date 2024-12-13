package com.eddywijaya.recruitmentbcaf.dto.validasi;

import com.eddywijaya.recruitmentbcaf.model.Stages;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import jakarta.persistence.Column;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

public class ValRecruitmentDTO {
    @NotNull
    @NotBlank
    @NotEmpty
    @Pattern(
            regexp = "^[a-zA-Z0-9\\-_&/ ]{1,50}$",
            message = "Format alfanumeric (-_& spasi) min 1 max 50 karakter, contoh : Oprec CMO Jakpus November")
    @JsonProperty("recruitment-name")
    private String recruitmentName;

    @NotNull
    @NotBlank
    @NotEmpty
    @Pattern(
            regexp = "^.{1,255}$",
            message = "Requirement Format alfanumeric (-_& spasi) min 1 max 255 karakter, contoh : minimal 2 tahun")
    private String requirements;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonDeserialize(using = LocalDateDeserializer.class)
    @JsonSerialize(using = LocalDateSerializer.class)
    @JsonProperty("open-date")
    private LocalDate openDate;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonDeserialize(using = LocalDateDeserializer.class)
    @JsonSerialize(using = LocalDateSerializer.class)
    @JsonProperty("close-date")
    private LocalDate closeDate;

    @JsonProperty("approval")
    private char approval;

    public @NotNull @NotBlank @NotEmpty @Pattern(
            regexp = "^[a-zA-Z0-9\\-_&/ ]{1,50}$",
            message = "Format alfanumeric (-_& spasi) min 1 max 50 karakter, contoh : Oprec CMO Jakpus November") String getRecruitmentName() {
        return recruitmentName;
    }


    public void setRecruitmentName(@NotNull @NotBlank @NotEmpty @Pattern(
            regexp = "^[a-zA-Z0-9\\-_&/ ]{1,50}$",
            message = "Format alfanumeric (-_& spasi) min 1 max 50 karakter, contoh : Oprec CMO Jakpus November") String recruitmentName) {
        this.recruitmentName = recruitmentName;
    }

    public @NotNull @NotBlank @NotEmpty @Pattern(
            regexp = "^.{1,255}$",
            message = "Requirement Format alfanumeric (-_& spasi) min 1 max 255 karakter, contoh : minimal 2 tahun") String getRequirements() {
        return requirements;
    }

    public void setRequirements(@NotNull @NotBlank @NotEmpty @Pattern(
            regexp = "^.{1,255}$",
            message = "Requirement Format alfanumeric (-_& spasi) min 1 max 255 karakter, contoh : minimal 2 tahun") String requirements) {
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

    public char getApproval() {
        return approval;
    }

    public void setApproval(char approval) {
        this.approval = approval;
    }
}
