package com.amway.lms.backend.model;

import java.util.List;

public class ManagerOfDepartment {
    private int userId;
    private List<Integer> departmentIds;
    
    public int getUserId() {
        return userId;
    }
    public void setUserId(int userId) {
        this.userId = userId;
    }
    public List<Integer> getDepartmentIds() {
        return departmentIds;
    }
    public void setDepartmentIds(List<Integer> departmentIds) {
        this.departmentIds = departmentIds;
    }
    
    
}
