package com.amway.lms.backend.model;

import java.sql.Timestamp;
import java.util.List;
import com.amway.lms.backend.entity.Department;
import com.amway.lms.backend.entity.Role;
import com.amway.lms.backend.entity.User;

public class UsersModel {
    private int id;
    private String userID;
    private byte active;
    private String address;
    private Timestamp createdAt;
    private Timestamp dateOfBirth;
    private int departmentId;
    private int approvalManagerId;
    private String approvalManagerName;
    private int hodId;
    private String hodName;
    private int managerId;
    private String managerName;
    private String email;
    private String firstName;
    private byte gender;
    private byte isDeleted;
    private String jobTitle;
    private String lastName;
    private String fullName;
    private String note;
    private String officeLocation;
    private String password;
    private String phoneNumber;
    private int roleId;
    private String staffCode;
    private Timestamp updatedAt;
    private List<Department> departments;
    private Department department;
    private List<Role> roles;
    private Role role;
    private List<User> users;
    /**
     * @return the id
     */
    public int getId() {
        return id;
    }
    /**
     * @param id the id to set
     */
    public void setId(int id) {
        this.id = id;
    }
    /**
     * @return the userID
     */
    public String getUserID() {
        return userID;
    }
    /**
     * @param userID the userID to set
     */
    public void setUserID(String userID) {
        this.userID = userID;
    }
    /**
     * @return the active
     */
    public byte getActive() {
        return active;
    }
    /**
     * @param active the active to set
     */
    public void setActive(byte active) {
        this.active = active;
    }
    /**
     * @return the address
     */
    public String getAddress() {
        return address;
    }
    /**
     * @param address the address to set
     */
    public void setAddress(String address) {
        this.address = address;
    }
    /**
     * @return the createdAt
     */
    public Timestamp getCreatedAt() {
        return createdAt;
    }
    /**
     * @param createdAt the createdAt to set
     */
    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }
    /**
     * @return the dateOfBirth
     */
    public Timestamp getDateOfBirth() {
        return dateOfBirth;
    }
    /**
     * @param dateOfBirth the dateOfBirth to set
     */
    public void setDateOfBirth(Timestamp dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }
    /**
     * @return the departmentId
     */
    public int getDepartmentId() {
        return departmentId;
    }
    /**
     * @param departmentId the departmentId to set
     */
    public void setDepartmentId(int departmentId) {
        this.departmentId = departmentId;
    }
    /**
     * @return the directLineManagerId
     */
    /**
     * @return the hodId
     */
    public int getHodId() {
        return hodId;
    }
    /**
     * @param hodId the hodId to set
     */
    public void setHodId(int hodId) {
        this.hodId = hodId;
    }
    /**
     * @return the hodName
     */
    public String getHodName() {
        return hodName;
    }
    /**
     * @param hodName the hodName to set
     */
    public void setHodName(String hodName) {
        this.hodName = hodName;
    }
    /**
     * @return the email
     */
    public String getEmail() {
        return email;
    }
    /**
     * @param email the email to set
     */
    public void setEmail(String email) {
        this.email = email;
    }
    /**
     * @return the firstName
     */
    public String getFirstName() {
        return firstName;
    }
    /**
     * @param firstName the firstName to set
     */
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }
    /**
     * @return the gender
     */
    public byte getGender() {
        return gender;
    }
    /**
     * @param gender the gender to set
     */
    public void setGender(byte gender) {
        this.gender = gender;
    }
    /**
     * @return the isDeleted
     */
    public byte getIsDeleted() {
        return isDeleted;
    }
    /**
     * @param isDeleted the isDeleted to set
     */
    public void setIsDeleted(byte isDeleted) {
        this.isDeleted = isDeleted;
    }
    /**
     * @return the jobTitle
     */
    public String getJobTitle() {
        return jobTitle;
    }
    /**
     * @param jobTitle the jobTitle to set
     */
    public void setJobTitle(String jobTitle) {
        this.jobTitle = jobTitle;
    }
    /**
     * @return the lastName
     */
    public String getLastName() {
        return lastName;
    }
    /**
     * @param lastName the lastName to set
     */
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }
    /**
     * @return the note
     */
    public String getNote() {
        return note;
    }
    /**
     * @param note the note to set
     */
    public void setNote(String note) {
        this.note = note;
    }
    /**
     * @return the officeLocation
     */
    public String getOfficeLocation() {
        return officeLocation;
    }
    /**
     * @param officeLocation the officeLocation to set
     */
    public void setOfficeLocation(String officeLocation) {
        this.officeLocation = officeLocation;
    }
    /**
     * @return the password
     */
    public String getPassword() {
        return password;
    }
    /**
     * @param password the password to set
     */
    public void setPassword(String password) {
        this.password = password;
    }
    /**
     * @return the phoneNumber
     */
    public String getPhoneNumber() {
        return phoneNumber;
    }
    /**
     * @param phoneNumber the phoneNumber to set
     */
    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
    /**
     * @return the roleId
     */
    public int getRoleId() {
        return roleId;
    }
    /**
     * @param roleId the roleId to set
     */
    public void setRoleId(int roleId) {
        this.roleId = roleId;
    }
    /**
     * @return the staffCode
     */
    public String getStaffCode() {
        return staffCode;
    }
    /**
     * @param staffCode the staffCode to set
     */
    public void setStaffCode(String staffCode) {
        this.staffCode = staffCode;
    }
    /**
     * @return the updatedAt
     */
    public Timestamp getUpdatedAt() {
        return updatedAt;
    }
    /**
     * @param updatedAt the updatedAt to set
     */
    public void setUpdatedAt(Timestamp updatedAt) {
        this.updatedAt = updatedAt;
    }
    /**
     * @return the departments
     */
    public List<Department> getDepartments() {
        return departments;
    }
    /**
     * @param departments the departments to set
     */
    public void setDepartments(List<Department> departments) {
        this.departments = departments;
    }
    /**
     * @return the department
     */
    public Department getDepartment() {
        return department;
    }
    /**
     * @param department the department to set
     */
    public void setDepartment(Department department) {
        this.department = department;
    }
    /**
     * @return the roles
     */
    public List<Role> getRoles() {
        return roles;
    }
    /**
     * @param roles the roles to set
     */
    public void setRoles(List<Role> roles) {
        this.roles = roles;
    }
    /**
     * @return the role
     */
    public Role getRole() {
        return role;
    }
    /**
     * @param role the role to set
     */
    public void setRole(Role role) {
        this.role = role;
    }
    /**
     * @return the users
     */
    public List<User> getUsers() {
        return users;
    }
    /**
     * @param users the users to set
     */
    public void setUsers(List<User> users) {
        this.users = users;
    }
    public int getManagerId() {
        return managerId;
    }
    public void setManagerId(int managerId) {
        this.managerId = managerId;
    }
    public String getManagerName() {
        return managerName;
    }
    public void setManagerName(String managerName) {
        this.managerName = managerName;
    }
    /**
     * @return the fullName
     */
    public String getFullName() {
        return fullName;
    }
    /**
     * @param fullName the fullName to set
     */
    public void setFullName(String fullName) {
        this.fullName = fullName;
    }
    public int getApprovalManagerId() {
        return approvalManagerId;
    }
    public void setApprovalManagerId(int approvalManagerId) {
        this.approvalManagerId = approvalManagerId;
    }
    public String getApprovalManagerName() {
        return approvalManagerName;
    }
    public void setApprovalManagerName(String approvalManagerName) {
        this.approvalManagerName = approvalManagerName;
    }
    
}
