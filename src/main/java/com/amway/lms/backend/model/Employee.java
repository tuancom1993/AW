package com.amway.lms.backend.model;

import java.sql.Timestamp;

public class Employee {
	private int employeeId;
	private String staffCode;
	private String firstName;
	private String lastName;
	private Byte status;
	private String competency;
	private String jobTitle;
	private int subLevel;
	private String department;
	private String fullName;
	private String linkAccept;
	private String linkDeny;
	private String no;
	private String fullNameManager;
	private int isTrainneePlan;
	private int sessionId;
	private String emailAddress;
	private String fromTraineePlan;
	private String AJPVPoints;
	private Timestamp confirmationDate;
	private int approvalManagerId;
	private int roleId;
	private String roleName;
	private String postSurveyStatus;
	
	public int getEmployeeId() {
		return employeeId;
	}

	public void setEmployeeId(int employeeId) {
		this.employeeId = employeeId;
	}

	public String getStaffCode() {
		return staffCode;
	}

	public void setStaffCode(String staffCode) {
		this.staffCode = staffCode;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

    /**
     * @return the status
     */
    public Byte getStatus() {
        return status;
    }

    /**
     * @param status the status to set
     */
    public void setStatus(Byte status) {
        this.status = status;
    }
    
    /**
     * @return the approvalManagerId
     */
    public int getApprovalManagerId() {
        return approvalManagerId;
    }

    /**
     * @param approvalManagerId the approvalManagerId to set
     */
    public void setApprovalManagerId(int approvalManagerId) {
        this.approvalManagerId = approvalManagerId;
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
     * @return the roleName
     */
    public String getRoleName() {
        return roleName;
    }

    /**
     * @param roleName the roleName to set
     */
    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    public String getCompetency() {
		return competency;
	}

	public void setCompetency(String competency) {
		this.competency = competency;
	}

	public String getJobTitle() {
		return jobTitle;
	}

	public void setJobTitle(String jobTitle) {
		this.jobTitle = jobTitle;
	}

	public int getSubLevel() {
		return subLevel;
	}

	public void setSubLevel(int subLevel) {
		this.subLevel = subLevel;
	}

	public String getDepartment() {
		return department;
	}

	public void setDepartment(String department) {
		this.department = department;
	}

	public String getFullName() {
		return fullName;
	}

	public void setFullName(String fullName) {
		this.fullName = fullName;
	}

	public String getLinkAccept() {
		return linkAccept;
	}

	public void setLinkAccept(String linkAccept) {
		this.linkAccept = linkAccept;
	}

	public String getLinkDeny() {
		return linkDeny;
	}

	public void setLinkDeny(String linkDeny) {
		this.linkDeny = linkDeny;
	}

	public String getNo() {
		return no;
	}

	public void setNo(String no) {
		this.no = no;
	}

	public String getFullNameManager() {
		return fullNameManager;
	}

	public void setFullNameManager(String fullNameManager) {
		this.fullNameManager = fullNameManager;
	}

	public int getIsTrainneePlan() {
		return isTrainneePlan;
	}

	public void setIsTrainneePlan(int isTrainneePlan) {
		this.isTrainneePlan = isTrainneePlan;
	}

	public int getSessionId() {
		return sessionId;
	}

	public void setSessionId(int sessionId) {
		this.sessionId = sessionId;
	}

	public String getEmailAddress() {
		return emailAddress;
	}

	public void setEmailAddress(String emailAddress) {
		this.emailAddress = emailAddress;
	}

	public String getFromTraineePlan() {
		return fromTraineePlan;
	}

	public void setFromTraineePlan(String fromTraineePlan) {
		this.fromTraineePlan = fromTraineePlan;
	}

	public String getAJPVPoints() {
		return AJPVPoints;
	}

	public void setAJPVPoints(String aJPVPoints) {
		AJPVPoints = aJPVPoints;
	}

    public Timestamp getConfirmationDate() {
        return confirmationDate;
    }

    public void setConfirmationDate(Timestamp confirmationDate) {
        this.confirmationDate = confirmationDate;
    }

    public String getPostSurveyStatus() {
        return postSurveyStatus;
    }

    public void setPostSurveyStatus(String postSurveyStatus) {
        this.postSurveyStatus = postSurveyStatus;
    }
	
}
