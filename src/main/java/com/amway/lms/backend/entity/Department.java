package com.amway.lms.backend.entity;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Transient;

/**
 * The persistent class for the departments database table.
 * 
 */
@Entity
@Table(name = "departments")
@NamedQueries({
    @NamedQuery(name = "Department.findAll", query = "SELECT d FROM Department d"),
    @NamedQuery(name = "getDepartmentByName", query = "SELECT d FROM Department d Where d.name = ?"),
    @NamedQuery(name = "removeHodInAllDepartmentByHodId", query = "UPDATE Department d SET hodId = 0 WHERE d.hodId = ?"),
    @NamedQuery(name = "removeAdminInAllDepartmentByAdmiId", query = "UPDATE Department d SET adminId = 0 WHERE d.adminId = ?"),
    @NamedQuery(name = "getDepartmentListByHodId", query = "SELECT d FROM Department d Where d.hodId = ?"),
    @NamedQuery(name = "getDepartmentListByAdminId", query = "SELECT d FROM Department d Where d.adminId = ?"),
    @NamedQuery(name = "getDepartmentListWithoutHod", query = "SELECT d FROM Department d Where d.hodId = 0 and d.id !=0"),
    @NamedQuery(name = "getDepartmentListByHodIdOrWithoutHOD", query = "SELECT d FROM Department d Where d.hodId = ? or d.hodId = 0 and d.id !=0"),
    @NamedQuery(name = "getDepartmentListByAdminIdOrWithoutAdmin", query = "SELECT d FROM Department d Where d.adminId = ? or d.adminId = 0 and d.id !=0")
    })
public class Department implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    private int id;

    @Column(name = "created_at")
    private Timestamp createdAt;

    private String name;
    
    @Transient
    private List<User> users;

    @Lob
    private String others;

    @Column(name = "hod_id")
    private int hodId;
    
    @Column(name = "admin_id")
    private int adminId;
    
    @Column(name = "updated_at")
    private Timestamp updatedAt;

    public Department() {
    }

    public int getId() {
        return this.id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Timestamp getCreatedAt() {
        return this.createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getOthers() {
        return this.others;
    }

    public void setOthers(String others) {
        this.others = others;
    }

    public Timestamp getUpdatedAt() {
        return this.updatedAt;
    }

    public void setUpdatedAt(Timestamp updatedAt) {
        this.updatedAt = updatedAt;
    }

    public List<User> getUsers() {
        return users;
    }

    public void setUsers(List<User> users) {
        this.users = users;
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

}