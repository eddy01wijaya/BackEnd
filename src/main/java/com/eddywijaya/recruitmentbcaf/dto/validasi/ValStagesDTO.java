package com.eddywijaya.recruitmentbcaf.dto.validasi;

import com.eddywijaya.recruitmentbcaf.model.Stages;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.*;

import java.time.LocalDate;

public class ValStagesDTO {
    @NotNull
    @NotBlank
    @NotEmpty
    @Size(min = 2, max = 100, message = "Nama stages minimal 2 dan maksimal 100 karakter")
    private String stagesName;

    @NotNull
    private Boolean isDefault;

    @NotNull
    @Positive(message = "Field 'stageOrder' harus berupa angka positif")
    private Integer stageOrder;

    public @NotNull @NotBlank @Size(min = 2, max = 100, message = "Nama stages minimal 2 dan maksimal 100 karakter") String getStagesName() {
        return stagesName;
    }

    public void setStagesName(@NotNull @NotBlank @Size(min = 2, max = 100, message = "Nama stages minimal 2 dan maksimal 100 karakter") String stagesName) {
        this.stagesName = stagesName;
    }

    public @NotNull Boolean getDefault() {
        return isDefault;
    }

    public void setDefault(@NotNull Boolean aDefault) {
        isDefault = aDefault;
    }

    public @NotNull @Positive(message = "Field 'stageOrder' harus berupa angka positif") Integer getStageOrder() {
        return stageOrder;
    }

    public void setStageOrder(@NotNull @Positive(message = "Field 'stageOrder' harus berupa angka positif") Integer stageOrder) {
        this.stageOrder = stageOrder;
    }
}

