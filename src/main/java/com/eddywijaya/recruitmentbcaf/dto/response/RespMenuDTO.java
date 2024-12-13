package com.eddywijaya.recruitmentbcaf.dto.response;

import com.eddywijaya.recruitmentbcaf.model.Access;
import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.util.Date;
import java.util.List;

public class RespMenuDTO {
    private Long idMenu;
    private String menuName;
    private String path;
//    private Long createdBy;
//    private Date createdAt;
//    private Long modifiedBy;
//    private Date modifiedAt;
//    private Boolean isActive;
//    private List<Access> accesses;

    public Long getIdMenu() {
        return idMenu;
    }

    public void setIdMenu(Long idMenu) {
        this.idMenu = idMenu;
    }

    public String getMenuName() {
        return menuName;
    }

    public void setMenuName(String menuName) {
        this.menuName = menuName;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

//    public Long getCreatedBy() {
//        return createdBy;
//    }
//
//    public void setCreatedBy(Long createdBy) {
//        this.createdBy = createdBy;
//    }
//
//    public Date getCreatedAt() {
//        return createdAt;
//    }
//
//    public void setCreatedAt(Date createdAt) {
//        this.createdAt = createdAt;
//    }
//
//    public Long getModifiedBy() {
//        return modifiedBy;
//    }
//
//    public void setModifiedBy(Long modifiedBy) {
//        this.modifiedBy = modifiedBy;
//    }
//
//    public Date getModifiedAt() {
//        return modifiedAt;
//    }
//
//    public void setModifiedAt(Date modifiedAt) {
//        this.modifiedAt = modifiedAt;
//    }
//
//    public Boolean getActive() {
//        return isActive;
//    }
//
//    public void setActive(Boolean active) {
//        isActive = active;
//    }
//
//    public List<Access> getAccesses() {
//        return accesses;
//    }
//
//    public void setAccesses(List<Access> accesses) {
//        this.accesses = accesses;
//    }
}
