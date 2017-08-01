package com.amway.lms.backend.entity;

import java.io.Serializable;

import javax.persistence.*;

import java.sql.Timestamp;
import java.util.List;

/**
 * The persistent class for the users database table.
 * 
 */
@Entity
@Table(name = "users")
@NamedQueries({
        @NamedQuery(name = "getUserList", query = "SELECT u FROM User u"),
        @NamedQuery(name = "getCoodinatorList", query = "SELECT u FROM User u WHERE u.roleId = 3"),
        @NamedQuery(name = "getApprovalManagerList", query = "SELECT u FROM User u WHERE u.roleId = 2"),
        @NamedQuery(name = "getHodList", query = "SELECT u FROM User u WHERE u.roleId = 5"),
        @NamedQuery(name = "getHodAndAdminList", query = "SELECT u FROM User u WHERE u.roleId = 5 or u.roleId = 1"),
        @NamedQuery(name = "getUsersAccepted", query = "SELECT u FROM User u WHERE u.id IN (SELECT sp.userId FROM SessionParticipant sp WHERE sp.sessionId=? AND sp.completionStatus =2)"),
        @NamedQuery(name = "getUsersCheckInBySessionId", query = "SELECT u FROM User u WHERE u.id IN (SELECT sp.userId FROM SessionParticipant sp WHERE sp.sessionId=? AND (sp.completionStatus=? OR sp.completionStatus=?))"),
        @NamedQuery(name = "getUsersCheckOutBySessionId", query = "SELECT u FROM User u WHERE u.id IN (SELECT sp.userId FROM SessionParticipant sp WHERE sp.sessionId=? AND (sp.completionStatus=? OR sp.completionStatus=? OR sp.completionStatus=?))"),
        @NamedQuery(name = "getUsersCheckinCheckout", query = "SELECT u FROM User u WHERE u.id IN (SELECT sp.userId FROM SessionParticipant sp WHERE sp.sessionId=? AND sp.completionStatus IN (?, ?, ?, ?))"),
        @NamedQuery(name = "getUsersCheckinCheckoutByName", query = "SELECT u FROM User u WHERE u.id IN (SELECT sp.userId FROM SessionParticipant sp WHERE sp.sessionId=? AND sp.completionStatus IN (?, ?, ?, ?)) AND (u.firstName LIKE ? OR u.lastName LIKE ?)"),
        @NamedQuery(name = "getUsersCheckInBySessionIdAndName", query = "SELECT u FROM User u WHERE u.id IN (SELECT sp.userId FROM SessionParticipant sp WHERE sp.sessionId=? AND (sp.completionStatus=? OR sp.completionStatus=?)) AND (u.firstName LIKE ? OR u.lastName LIKE ?)"),
        @NamedQuery(name = "getUsersCheckOutBySessionIdAndName", query = "SELECT u FROM User u WHERE u.id IN (SELECT sp.userId FROM SessionParticipant sp WHERE sp.sessionId=? AND (sp.completionStatus=? OR sp.completionStatus=? OR sp.completionStatus=?)) AND (u.firstName LIKE ? OR u.lastName LIKE ?)"),
        @NamedQuery(name = "searchByUserIdandStaffCode", query = "SELECT a FROM User a where a.id=? and a.staffCode=?"),
        @NamedQuery(name = "searchUser", query = "SELECT u FROM User u WHERE u.firstName LIKE ? OR u.lastName LIKE ? "),
        @NamedQuery(name = "getUserByApprovalMangerId", query = "SELECT u.id FROM User u WHERE u.approvalManagerId =?"),
        @NamedQuery(name = "getUserByHodId", query = "SELECT u.id FROM User u WHERE u.departmentId IN (SELECT d.id FROM Department d WHERE d.hodId = ?)"),
        @NamedQuery(name = "getUserByHodIdAndDepartmentId", query = "SELECT u.id FROM User u WHERE u.departmentId IN (SELECT d.id FROM Department d WHERE d.hodId = ? AND d.id = ?)"),
        @NamedQuery(name = "getTraineeByApprovalManager", query = "SELECT u.id FROM User u WHERE u.approvalManagerId =? AND u.roleId=4"),
        @NamedQuery(name = "getUserByAmIdAndDepartmentId", query = "SELECT u.id FROM User u WHERE u.approvalManagerId =? AND u.departmentId=? AND u.roleId=4"),
        @NamedQuery(name = "getListUserByApprovalMangerId", query = "SELECT u FROM User u WHERE u.approvalManagerId =?"),
        @NamedQuery(name = "getParticipantsList", query = "SELECT u FROM User u WHERE u.roleId=4"),
        @NamedQuery(name = "getUserByUserID", query = "SELECT u FROM User u WHERE u.userID = ?"),
        @NamedQuery(name = "getAllUsersOfTrainingPlan", query = "SELECT u FROM User u WHERE u.id NOT IN (SELECT pp.userId FROM PreparatoryParticipant pp WHERE pp.trainingPlanId = ?)"),
        @NamedQuery(name = "getUsersOfTrainingPlan", query = "SELECT u FROM User u WHERE u.id NOT IN (SELECT pp.userId FROM PreparatoryParticipant pp WHERE pp.trainingPlanId = ?) AND u.departmentId IN (SELECT d.id FROM Department d WHERE d.adminId = ?)"),
        @NamedQuery(name = "getUsersOfTrainingPlanByDepartment", query = "SELECT u FROM User u WHERE u.id NOT IN (SELECT pp.userId FROM PreparatoryParticipant pp WHERE pp.trainingPlanId = ?) AND u.departmentId = ?"),
        @NamedQuery(name = "getExistedAllUsersOfTrainingPlan", query = "SELECT u FROM User u WHERE u.id IN (SELECT pp.userId FROM PreparatoryParticipant pp WHERE pp.trainingPlanId = ?)"),
        @NamedQuery(name = "getExistedUsersOfTrainingPlan", query = "SELECT u FROM User u WHERE u.id IN (SELECT pp.userId FROM PreparatoryParticipant pp WHERE pp.trainingPlanId = ?) AND u.departmentId IN (SELECT d.id FROM Department d WHERE d.adminId = ?)"),
        @NamedQuery(name = "getExistedUsersOfTrainingPlanByDepartment", query = "SELECT u FROM User u WHERE u.id IN (SELECT pp.userId FROM PreparatoryParticipant pp WHERE pp.trainingPlanId = ?) AND u.departmentId = ?"),
        @NamedQuery(name = "getListUserAddParticipant", query = "SELECT u FROM User u WHERE u.id NOT IN (SELECT sp.userId FROM SessionParticipant sp WHERE sp.sessionId IN (SELECT s1.id FROM Session s1 WHERE s1.courseId IN (SELECT s2.courseId FROM Session s2 WHERE s2.id=?))) AND u.id NOT IN (SELECT s.trainerId FROM Session s WHERE s.id=?)"),
        @NamedQuery(name = "getSubmmittedListUser", query = "SELECT u FROM User u WHERE u.id IN (SELECT sp.userId FROM SessionParticipant sp Where sp.sessionId=? AND sp.confirmationStatus=1)"),
        @NamedQuery(name = "getListUserAddParticipantByDepartmentId", query = "SELECT u FROM User u WHERE u.id NOT IN (SELECT sp.userId FROM SessionParticipant sp WHERE sp.sessionId IN (SELECT s1.id FROM Session s1 WHERE s1.courseId IN (SELECT s2.courseId FROM Session s2 WHERE s2.id=?))) AND u.id NOT IN (SELECT s.trainerId FROM Session s WHERE s.id=?) AND u.departmentId=? "),
        @NamedQuery(name = "getUserByEmail", query = "SELECT u FROM User u WHERE u.email = ?"),
        @NamedQuery(name = "getHodOfUserByUserId", query = "SELECT u FROM User u WHERE u.id IN (SELECT d.hodId FROM Department d WHERE d.id IN (SELECT u2.departmentId FROM User u2 WHERE u2.id = ?))"),
        @NamedQuery(name = "getAdminOfUserByUserId", query = "SELECT u FROM User u WHERE u.id IN (SELECT d.adminId FROM Department d WHERE d.id IN (SELECT u2.departmentId FROM User u2 WHERE u2.id = ?))"),
        @NamedQuery(name = "getHodByDepartmentId", query = "SELECT u FROM User u WHERE u.id IN (SELECT d.hodId FROM Department d WHERE d.id = ?)"),
        @NamedQuery(name = "getAdminByDepartmentId", query = "SELECT u FROM User u WHERE u.id IN (SELECT d.adminId FROM Department d WHERE d.id = ?)"),
        @NamedQuery(name = "getListApprovalManagerByHODId", query = "SELECT u FROM User u WHERE u.roleId = 2 AND u.departmentId IN (SELECT d.id FROM Department d WHERE d.hodId = ?)"),
        @NamedQuery(name = "getListApprovalManagerByDepartmentId", query = "SELECT u FROM User u WHERE u.roleId = 2 AND u.departmentId =?"),
        @NamedQuery(name = "getListExceptAmAndHodByDepartmentId", query = "SELECT u FROM User u WHERE u.roleId != 2 AND u.roleId != 5 AND u.departmentId =?"),
        @NamedQuery(name = "getListUsersWithoutAdminAndHodByDepartment", query = "SELECT u FROM User u WHERE u.roleId != 1 AND u.roleId != 5 AND u.departmentId =?"),
        @NamedQuery(name = "getListUserAddParticipantBySessionIdAndAdminId", query = "SELECT u FROM User u WHERE u.id NOT IN (SELECT sp.userId FROM SessionParticipant sp WHERE sp.sessionId IN (SELECT s1.id FROM Session s1 WHERE s1.courseId IN (SELECT s2.courseId FROM Session s2 WHERE s2.id=?))) AND u.id NOT IN (SELECT s.trainerId FROM Session s WHERE s.id=?) AND u.departmentId IN (SELECT d.id FROM Department d WHERE d.adminId = ?)"),
        @NamedQuery(name = "getListApprovalManagerIdByHodId", query = "SELECT u.id FROM User u WHERE u.departmentId IN (SELECT d.id FROM Department d WHERE d.hodId = ?) AND u.roleId = 2"),
})
public class User implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    private byte active;

    private String userID;

    @Lob
    private String address;

    @Column(name = "created_at")
    private Timestamp createdAt;

    @Column(name = "date_of_birth")
    private Timestamp dateOfBirth;

    @Column(name = "department_id")
    private int departmentId;

    @Column(name = "approval_manager_id")
    private int approvalManagerId;

    @Column(name = "email")
    private String email;

    @Column(name = "first_name")
    private String firstName;

    private byte gender;

    @Column(name = "is_deleted")
    private byte isDeleted;

    @Column(name = "job_title")
    private String jobTitle;

    @Column(name = "last_name")
    private String lastName;

    @Lob
    private String note;

    @Column(name = "office_location")
    private String officeLocation;

    @Lob
    @Column(name = "phone_number")
    private String phoneNumber;

    @Column(name = "role_id")
    private int roleId;

    @Column(name = "staff_code")
    private String staffCode;

    @Column(name = "updated_at")
    private Timestamp updatedAt;

    @Column(name = "manager_id")
    private int managerId;

    @Column(name = "ajpv")
    private int ajpv;

    @Transient
    private Department department;

    @Transient
    private List<Role> roles;

    @Transient
    private Role role;

    @Transient
    private Timestamp checkinAt;

    @Transient
    private Timestamp checkoutAt;

    @Transient
    private int completionStatus;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public byte getActive() {
        return active;
    }

    public void setActive(byte active) {
        this.active = active;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }

    public Timestamp getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(Timestamp dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public int getDepartmentId() {
        return departmentId;
    }

    public void setDepartmentId(int departmentId) {
        this.departmentId = departmentId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public byte getGender() {
        return gender;
    }

    public void setGender(byte gender) {
        this.gender = gender;
    }

    public byte getIsDeleted() {
        return isDeleted;
    }

    public void setIsDeleted(byte isDeleted) {
        this.isDeleted = isDeleted;
    }

    public String getJobTitle() {
        return jobTitle;
    }

    public void setJobTitle(String jobTitle) {
        this.jobTitle = jobTitle;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getOfficeLocation() {
        return officeLocation;
    }

    public void setOfficeLocation(String officeLocation) {
        this.officeLocation = officeLocation;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public int getRoleId() {
        return roleId;
    }

    public void setRoleId(int roleId) {
        this.roleId = roleId;
    }

    public String getStaffCode() {
        return staffCode;
    }

    public void setStaffCode(String staffCode) {
        this.staffCode = staffCode;
    }

    public Timestamp getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Timestamp updatedAt) {
        this.updatedAt = updatedAt;
    }

    public Department getDepartment() {
        return department;
    }

    public void setDepartment(Department department) {
        this.department = department;
    }

    public List<Role> getRoles() {
        return roles;
    }

    public void setRoles(List<Role> roles) {
        this.roles = roles;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public Timestamp getCheckinAt() {
        return checkinAt;
    }

    public void setCheckinAt(Timestamp checkinAt) {
        this.checkinAt = checkinAt;
    }

    public Timestamp getCheckoutAt() {
        return checkoutAt;
    }

    public void setCheckoutAt(Timestamp checkoutAt) {
        this.checkoutAt = checkoutAt;
    }

    public int getCompletionStatus() {
        return completionStatus;
    }

    public void setCompletionStatus(int completionStatus) {
        this.completionStatus = completionStatus;
    }

    public int getManagerId() {
        return managerId;
    }

    public void setManagerId(int managerId) {
        this.managerId = managerId;
    }

    public int getAjpv() {
        return ajpv;
    }

    public void setAjpv(int ajpv) {
        this.ajpv = ajpv;
    }

    public int getApprovalManagerId() {
        return approvalManagerId;
    }

    public void setApprovalManagerId(int approvalManagerId) {
        this.approvalManagerId = approvalManagerId;
    }

}