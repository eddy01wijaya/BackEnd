package com.eddywijaya.recruitmentbcaf.dto.validasi;

import com.eddywijaya.recruitmentbcaf.model.Stages;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.*;
import java.time.LocalDate;

public class ValResumeDTO {

    @NotNull
    @NotBlank
    @NotEmpty
    @Pattern(regexp = "^(?=.{1,256})(?=.{1,64}@.{1,255}$)(?:(?![.])[a-zA-Z0-9._%+-]+(?:(?<!\\\\)[.][a-zA-Z0-9-]+)*?)@[a-zA-Z0-9.-]+(?:\\.[a-zA-Z]{2,50})+$",
            message = "Format tidak valid contoh : user_name123@sub.domain.com")
    private String email;

    @NotNull
    @NotBlank
    @NotEmpty
    @Pattern(
            regexp = "^[a-zA-Z0-9\\- ]{1,50}$",
            message = "Format alfanumeric (- dan spasi) min 1 max 50 karakter, contoh : Oprec CMO Jakpus November")
    private String fullName;

    @NotNull
    @NotBlank
    @NotEmpty
    @Pattern(
            regexp = "^[a-zA-Z0-9\\-_&/ ]{1,50}$",
            message = "Format alfanumeric (-_& spasi) min 1 max 50 karakter, contoh : Kotabaru")
    private String birthPlace;

    @NotNull(message = "Tanggal lahir tidak boleh kosong")
    private LocalDate birthDate;

    @NotNull
    @NotBlank
    @Size(min = 2, max = 100, message = "Nama sekolah minimal 2 dan maksimal 100 karakter")
    private String schoolName;

    @NotNull
    @NotBlank
    @Pattern(
            regexp = "^(SD|SMP|SMA|D3|S1|S2|S3)$",
            message = "Pendidikan terakhir harus salah satu dari: SD, SMP, SMA, D3, S1, S2, atau S3")
    private String lastEducation;

    @NotNull
    @Min(value = 1900, message = "Tahun kelulusan tidak valid")
    @Max(value = 2100, message = "Tahun kelulusan tidak valid")
    private Integer graduationYear;

    @NotNull
    @DecimalMin(value = "0.0", inclusive = true, message = "GPA harus lebih dari atau sama dengan 0.0")
    @DecimalMax(value = "4.0", inclusive = true, message = "GPA harus kurang dari atau sama dengan 4.0")
    private Double gpa;

    @NotNull
    @NotBlank
    @Size(min = 16, max = 16, message = "ID Number harus 16 karakter")
    private String idNumber;

    @NotNull
    @NotBlank
    @Pattern(
            regexp = "^(Laki-laki|Perempuan)$",
            message = "Jenis kelamin harus 'Laki-laki' atau 'Perempuan'")
    private String gender;

    @NotNull
    @NotBlank
    private String religion;

    @NotNull
    @NotBlank
    @Pattern(
            regexp = "^(Menikah|Belum Menikah|Cerai)$",
            message = "Status pernikahan harus 'Menikah', 'Belum Menikah', atau 'Cerai'")
    private String maritalStatus;

    @NotNull
    @NotBlank
    @Size(min = 5, max = 200, message = "Alamat minimal 5 dan maksimal 200 karakter")
    private String address;

    @NotNull
    @NotBlank
    @Pattern(
            regexp = "^\\+?[0-9]{10,15}$",
            message = "Nomor telepon harus berupa angka dan dapat dimulai dengan '+', antara 10-15 digit")
    private String phoneNumber;

    @NotNull
    @NotBlank
    private String aiRecommendation;

    @NotNull
    private Stages stages;

    public @NotNull @NotBlank @NotEmpty @Pattern(regexp = "^(?=.{1,256})(?=.{1,64}@.{1,255}$)(?:(?![.])[a-zA-Z0-9._%+-]+(?:(?<!\\\\)[.][a-zA-Z0-9-]+)*?)@[a-zA-Z0-9.-]+(?:\\.[a-zA-Z]{2,50})+$",
            message = "Format tidak valid contoh : user_name123@sub.domain.com") String getEmail() {
        return email;
    }

    public void setEmail(@NotNull @NotBlank @NotEmpty @Pattern(regexp = "^(?=.{1,256})(?=.{1,64}@.{1,255}$)(?:(?![.])[a-zA-Z0-9._%+-]+(?:(?<!\\\\)[.][a-zA-Z0-9-]+)*?)@[a-zA-Z0-9.-]+(?:\\.[a-zA-Z]{2,50})+$",
            message = "Format tidak valid contoh : user_name123@sub.domain.com") String email) {
        this.email = email;
    }

    public @NotNull @NotBlank @NotEmpty @Pattern(
            regexp = "^[a-zA-Z0-9\\- ]{1,50}$",
            message = "Format alfanumeric (- dan spasi) min 1 max 50 karakter, contoh : Oprec CMO Jakpus November") String getFullName() {
        return fullName;
    }

    public void setFullName(@NotNull @NotBlank @NotEmpty @Pattern(
            regexp = "^[a-zA-Z0-9\\- ]{1,50}$",
            message = "Format alfanumeric (- dan spasi) min 1 max 50 karakter, contoh : Oprec CMO Jakpus November") String fullName) {
        this.fullName = fullName;
    }

    public @NotNull @NotBlank @NotEmpty @Pattern(
            regexp = "^[a-zA-Z0-9\\-_&/ ]{1,50}$",
            message = "Format alfanumeric (-_& spasi) min 1 max 50 karakter, contoh : Kotabaru") String getBirthPlace() {
        return birthPlace;
    }

    public void setBirthPlace(@NotNull @NotBlank @NotEmpty @Pattern(
            regexp = "^[a-zA-Z0-9\\-_&/ ]{1,50}$",
            message = "Format alfanumeric (-_& spasi) min 1 max 50 karakter, contoh : Kotabaru") String birthPlace) {
        this.birthPlace = birthPlace;
    }

    public @NotNull(message = "Tanggal lahir tidak boleh kosong") LocalDate getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(@NotNull(message = "Tanggal lahir tidak boleh kosong") LocalDate birthDate) {
        this.birthDate = birthDate;
    }

    public @NotNull @NotBlank @Size(min = 2, max = 100, message = "Nama sekolah minimal 2 dan maksimal 100 karakter") String getSchoolName() {
        return schoolName;
    }

    public void setSchoolName(@NotNull @NotBlank @Size(min = 2, max = 100, message = "Nama sekolah minimal 2 dan maksimal 100 karakter") String schoolName) {
        this.schoolName = schoolName;
    }

    public @NotNull @NotBlank @Pattern(
            regexp = "^(SD|SMP|SMA|D3|S1|S2|S3)$",
            message = "Pendidikan terakhir harus salah satu dari: SD, SMP, SMA, D3, S1, S2, atau S3") String getLastEducation() {
        return lastEducation;
    }

    public void setLastEducation(@NotNull @NotBlank @Pattern(
            regexp = "^(SD|SMP|SMA|D3|S1|S2|S3)$",
            message = "Pendidikan terakhir harus salah satu dari: SD, SMP, SMA, D3, S1, S2, atau S3") String lastEducation) {
        this.lastEducation = lastEducation;
    }

    public @NotNull @Min(value = 1900, message = "Tahun kelulusan tidak valid") @Max(value = 2100, message = "Tahun kelulusan tidak valid") Integer getGraduationYear() {
        return graduationYear;
    }

    public void setGraduationYear(@NotNull @Min(value = 1900, message = "Tahun kelulusan tidak valid") @Max(value = 2100, message = "Tahun kelulusan tidak valid") Integer graduationYear) {
        this.graduationYear = graduationYear;
    }

    public @NotNull @DecimalMin(value = "0.0", inclusive = true, message = "GPA harus lebih dari atau sama dengan 0.0") @DecimalMax(value = "4.0", inclusive = true, message = "GPA harus kurang dari atau sama dengan 4.0") Double getGpa() {
        return gpa;
    }

    public void setGpa(@NotNull @DecimalMin(value = "0.0", inclusive = true, message = "GPA harus lebih dari atau sama dengan 0.0") @DecimalMax(value = "4.0", inclusive = true, message = "GPA harus kurang dari atau sama dengan 4.0") Double gpa) {
        this.gpa = gpa;
    }

    public @NotNull @NotBlank @Size(min = 16, max = 16, message = "ID Number harus 16 karakter") String getIdNumber() {
        return idNumber;
    }

    public void setIdNumber(@NotNull @NotBlank @Size(min = 16, max = 16, message = "ID Number harus 16 karakter") String idNumber) {
        this.idNumber = idNumber;
    }

    public @NotNull @NotBlank @Pattern(
            regexp = "^(Laki-laki|Perempuan)$",
            message = "Jenis kelamin harus 'Laki-laki' atau 'Perempuan'") String getGender() {
        return gender;
    }

    public void setGender(@NotNull @NotBlank @Pattern(
            regexp = "^(Laki-laki|Perempuan)$",
            message = "Jenis kelamin harus 'Laki-laki' atau 'Perempuan'") String gender) {
        this.gender = gender;
    }

    public @NotNull @NotBlank String getReligion() {
        return religion;
    }

    public void setReligion(@NotNull @NotBlank String religion) {
        this.religion = religion;
    }

    public @NotNull @NotBlank @Pattern(
            regexp = "^(Menikah|Belum Menikah|Cerai)$",
            message = "Status pernikahan harus 'Menikah', 'Belum Menikah', atau 'Cerai'") String getMaritalStatus() {
        return maritalStatus;
    }

    public void setMaritalStatus(@NotNull @NotBlank @Pattern(
            regexp = "^(Menikah|Belum Menikah|Cerai)$",
            message = "Status pernikahan harus 'Menikah', 'Belum Menikah', atau 'Cerai'") String maritalStatus) {
        this.maritalStatus = maritalStatus;
    }

    public @NotNull @NotBlank @Size(min = 5, max = 200, message = "Alamat minimal 5 dan maksimal 200 karakter") String getAddress() {
        return address;
    }

    public void setAddress(@NotNull @NotBlank @Size(min = 5, max = 200, message = "Alamat minimal 5 dan maksimal 200 karakter") String address) {
        this.address = address;
    }

    public @NotNull @NotBlank @Pattern(
            regexp = "^\\+?[0-9]{10,15}$",
            message = "Nomor telepon harus berupa angka dan dapat dimulai dengan '+', antara 10-15 digit") String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(@NotNull @NotBlank @Pattern(
            regexp = "^\\+?[0-9]{10,15}$",
            message = "Nomor telepon harus berupa angka dan dapat dimulai dengan '+', antara 10-15 digit") String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public @NotNull @NotBlank String getAiRecommendation() {
        return aiRecommendation;
    }

    public void setAiRecommendation(@NotNull @NotBlank String aiRecommendation) {
        this.aiRecommendation = aiRecommendation;
    }

    public @NotNull Stages getStages() {
        return stages;
    }

    public void setStages(@NotNull Stages stages) {
        this.stages = stages;
    }
}

