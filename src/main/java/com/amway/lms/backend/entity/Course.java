package com.amway.lms.backend.entity;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Transient;

/**
 * The persistent class for the courses database table.
 * 
 */
@Entity
@Table(name = "courses")
@NamedQueries({
        @NamedQuery(name = "getCoursesList", query = "SELECT c FROM Course c WHERE c.isActive = ? AND c.categoryId IN (SELECT ca.id FROM Category ca) ORDER BY c.id DESC"),
        @NamedQuery(name = "getCoursesByTrainingPlan", query = "SELECT c FROM Course c WHERE c.isActive = ? AND c.categoryId IN (SELECT ca.id FROM Category ca) AND c.id IN (SELECT ct.courseId FROM CoursesOfTrainingPlan ct WHERE ct.trainingPlanId = ?) ORDER BY c.id DESC"),
        @NamedQuery(name = "getCoursesListByStatus", query = "SELECT c FROM Course c WHERE c.isActive = ? AND c.categoryId IN (SELECT ca.id FROM Category ca)"),
        @NamedQuery(name = "getCoursesByNameOrCode", query = "SELECT c FROM Course c WHERE c.categoryId IN (SELECT ca.id FROM Category ca) AND c.isActive = ? AND c.name LIKE ?"),
        @NamedQuery(name = "getCoursesApprovedList", query = "FROM Course c WHERE c.categoryId IN (SELECT ca.id FROM Category ca) AND c.isApprove = 1 OR c.isDefaultApproval = 1"),
        @NamedQuery(name = "updateCoursesStatus", query = "UPDATE Course c SET c.isActive = ? WHERE c.id = ?"),
        @NamedQuery(name = "getCourseInformation", query = "SELECT a,b FROM Course a, Session b where a.id=? and a.id=b.courseId"),
        @NamedQuery(name = "getSuggestCourse", query = "SELECT a FROM Course a where a.id not in (select b.courseId from CourseParticipant b where b.userId=?) AND a.categoryId IN (SELECT ca.id FROM Category ca)"),
        @NamedQuery(name = "searchCourseByStaffCode", query = "SELECT a FROM Course a where a.id in (select b.courseId from CourseParticipant b where b.userId=?) AND a.categoryId IN (SELECT ca.id FROM Category ca)"),
        @NamedQuery(name = "getCoursesToAddTrainingPlan", query = "SELECT a FROM Course a WHERE a.isActive=1 AND a.id NOT IN (SELECT b.courseId FROM CoursesOfTrainingPlan b WHERE b.trainingPlanId = ?) AND a.categoryId IN (SELECT ca.id FROM Category ca)"),
        @NamedQuery(name = "getCoursesOfTrainingPlan", query = "SELECT a FROM Course a WHERE a.id IN (SELECT b.courseId FROM CoursesOfTrainingPlan b WHERE b.trainingPlanId = ?) AND a.categoryId IN (SELECT ca.id FROM Category ca)"),
        @NamedQuery(name = "getCoursesStartByToday", query = "FROM Course c WHERE c.id IN (SELECT s.courseId FROM Session s WHERE DATE(s.startTime) = DATE(now()))"),
        @NamedQuery(name = "searchCoursesByNameStartByToday", query = "FROM Course c WHERE c.id IN (SELECT s.courseId FROM Session s WHERE DATE(s.startTime) = DATE(now())) AND c.name LIKE ?"),
        @NamedQuery(name = "getCourseListNotHaveQuiz", query = "SELECT c FROM Course c Where c.id Not In (Select q.courseId From Quiz q) AND c.categoryId IN (SELECT ca.id FROM Category ca) And c.isActive = 1"),
        @NamedQuery(name = "getCourseListForPostTrainingSurvey", query = "SELECT c FROM Course c Where c.categoryId IN (SELECT ca.id FROM Category ca) And c.isActive = 1")
})
public class Course implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer id;

	@Column(name = "category_id")
	private int categoryId;

	@Transient
	private Category category;

	@Transient
	private List<Session> sessions;

	private String objective;
	
	private String description;

	@Column(name = "is_optional")
	private int isOptional;

	@Column(name = "is_active")
	private int isActive;

	@Column(name = "is_approve")
	private int isApprove;

	@Column(name = "is_default_approval")
	private int isDefaultApproval;

	@Column(name = "is_offline")
	private int isOffline;

	@Column(name = "link_course")
	private String linkCourse;

	private String name;

	@Column(name = "number_of_session")
	private int numberOfSession;

	@Column(name = "post_training_comment")
	private String postTrainingComment;

	@Column(name = "pre_training_comment")
	private String preTrainingComment;

	@Column(name = "participation_ajpv")
	private int participationAJPV;

	@Column(name = "completion_ajpv")
	private int completionAJPV;

	@Column(name = "pass_quiz_ajpv")
	private int passQuizAJPV;
	
	@Transient
	private int isCourseRequired;

	public Course() {
	}

	public int getId() {
		return this.id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getCategoryId() {
		return this.categoryId;
	}

	public void setCategoryId(int categoryId) {
		this.categoryId = categoryId;
	}
    public String getObjective() {
        return objective;
    }

    public void setObjective(String objective) {
        this.objective = objective;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getIsActive() {
		return this.isActive;
	}

	public void setIsActive(int isActive) {
		this.isActive = isActive;
	}

	public int getIsApprove() {
		return this.isApprove;
	}

	public void setIsApprove(int isApprove) {
		this.isApprove = isApprove;
	}

	public int getIsDefaultApproval() {
		return this.isDefaultApproval;
	}

	public void setIsDefaultApproval(int isDefaultApproval) {
		this.isDefaultApproval = isDefaultApproval;
	}

	public int getIsOffline() {
		return this.isOffline;
	}

	public void setIsOffline(int isOffline) {
		this.isOffline = isOffline;
	}

	public String getLinkCourse() {
		return this.linkCourse;
	}

	public void setLinkCourse(String linkCourse) {
		this.linkCourse = linkCourse;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getNumberOfSession() {
		return this.numberOfSession;
	}

	public void setNumberOfSession(int numberOfSession) {
		this.numberOfSession = numberOfSession;
	}

	public String getPostTrainingComment() {
		return this.postTrainingComment;
	}

	public void setPostTrainingComment(String postTrainingComment) {
		this.postTrainingComment = postTrainingComment;
	}

	public String getPreTrainingComment() {
		return this.preTrainingComment;
	}

	public void setPreTrainingComment(String preTrainingComment) {
		this.preTrainingComment = preTrainingComment;
	}

	public Category getCategory() {
		return category;
	}

	public void setCategory(Category category) {
		this.category = category;
	}

	public List<Session> getSessions() {
		return sessions;
	}

	public void setSessions(List<Session> sessions) {
		this.sessions = sessions;
	}

	public int getIsOptional() {
		return isOptional;
	}

	public void setIsOptional(int isOptional) {
		this.isOptional = isOptional;
	}

	public int getParticipationAJPV() {
		return participationAJPV;
	}

	public void setParticipationAJPV(int participationAJPV) {
		this.participationAJPV = participationAJPV;
	}

	public int getCompletionAJPV() {
		return completionAJPV;
	}

	public void setCompletionAJPV(int completionAJPV) {
		this.completionAJPV = completionAJPV;
	}

	public int getPassQuizAJPV() {
		return passQuizAJPV;
	}

	public void setPassQuizAJPV(int passQuizAJPV) {
		this.passQuizAJPV = passQuizAJPV;
	}
	
	
	
	public int getIsCourseRequired() {
        return isCourseRequired;
    }

    public void setIsCourseRequired(int isCourseRequired) {
        this.isCourseRequired = isCourseRequired;
    }

    /**
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = (int) (prime * result + ((id == null) ? 0 : id.hashCode()));
		return result;
	}

	/**
	 */
	public boolean equals(Object obj) {
		if (obj == this)
			return true;
		if (!(obj instanceof Course))
			return false;
		Course equalCheck = (Course) obj;
		if ((id == null && equalCheck.id != null) || (id != null && equalCheck.id == null))
			return false;
		if (id != null && !id.equals(equalCheck.id))
			return false;
		return true;
	}

}