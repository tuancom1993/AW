package com.amway.lms.backend.model;

public class PostTrainingAction extends DashBoard{
	private String courseName;
	private String courseTye;
	private String actionType;
	private String status;
	private ActionPlan actionPlan;
	private QuizResult quizResult;

	public String getCourseName() {
		return courseName;
	}

	public void setCourseName(String courseName) {
		this.courseName = courseName;
	}

	public String getCourseTye() {
		return courseTye;
	}

	public void setCourseTye(String courseTye) {
		this.courseTye = courseTye;
	}

	public String getActionType() {
		return actionType;
	}

	public void setActionType(String actionType) {
		this.actionType = actionType;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public ActionPlan getActionPlan() {
		return actionPlan;
	}

	public void setActionPlan(ActionPlan actionPlan) {
		this.actionPlan = actionPlan;
	}

	public QuizResult getQuizResult() {
		return quizResult;
	}

	public void setQuizResult(QuizResult quizResult) {
		this.quizResult = quizResult;
	}
	

}
