package com.amway.lms.backend.model;

import java.sql.Timestamp;

public class DepartmentModel {
    private int id;
    private String name;
    private String others;
    private int hodId;
    private int adminId;
    private boolean isHodBelongDepartment;
    private boolean isManageDepartment;
    private Timestamp createdAt;
    private Timestamp updatedAt;
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getOthers() {
        return others;
    }
    public void setOthers(String others) {
        this.others = others;
    }
    public int getHodId() {
        return hodId;
    }
    public void setHodId(int hodId) {
        this.hodId = hodId;
    }
    public int getAdminId() {
        return adminId;
    }
    public void setAdminId(int adminId) {
        this.adminId = adminId;
    }
    public boolean isHodBelongDepartment() {
        return isHodBelongDepartment;
    }
    public void setIsHodBelongDepartment(boolean isHodBelongDepartment) {
        this.isHodBelongDepartment = isHodBelongDepartment;
    }
    public boolean isManageDepartment() {
        return isManageDepartment;
    }
    public void setIsManageDepartment(boolean isManageDepartment) {
        this.isManageDepartment = isManageDepartment;
    }
    public Timestamp getCreatedAt() {
        return createdAt;
    }
    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }
    public Timestamp getUpdatedAt() {
        return updatedAt;
    }
    public void setUpdatedAt(Timestamp updatedAt) {
        this.updatedAt = updatedAt;
    }
}
