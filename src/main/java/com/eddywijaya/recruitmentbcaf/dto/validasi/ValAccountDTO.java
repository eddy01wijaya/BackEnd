package com.eddywijaya.recruitmentbcaf.dto.validasi;

import com.eddywijaya.recruitmentbcaf.model.Access;
import com.eddywijaya.recruitmentbcaf.model.Notification;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

public class ValAccountDTO {
    @NotNull
    @NotBlank
    @NotEmpty
    @Pattern(
            regexp = "^[a-zA-Z0-9\\-_&/ ]{1,50}$",
            message = "Format alfanumeric (-_& spasi) min 1 max 50 karakter, contoh : Oprec CMO Jakpus November")
    private String username;

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


    public @NotNull @NotBlank @NotEmpty @Pattern(
            regexp = "^[a-zA-Z0-9\\-_&/ ]{1,50}$",
            message = "Format alfanumeric (-_& spasi) min 1 max 50 karakter, contoh : Oprec CMO Jakpus November") String getUsername() {
        return username;
    }

    public void setUsername(@NotNull @NotBlank @NotEmpty @Pattern(
            regexp = "^[a-zA-Z0-9\\-_&/ ]{1,50}$",
            message = "Format alfanumeric (-_& spasi) min 1 max 50 karakter, contoh : Oprec CMO Jakpus November") String username) {
        this.username = username;
    }

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
}
