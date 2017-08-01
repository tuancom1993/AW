package com.amway.lms.backend.model;

public class PresentationSkill extends DashBoard{
	private String postTrainingComment;
	private String preTrainingComment;
	private int sessionId;

	public String getPostTrainingComment() {
		return postTrainingComment;
	}

	public void setPostTrainingComment(String postTrainingComment) {
		this.postTrainingComment = postTrainingComment;
	}

	public String getPreTrainingComment() {
		return preTrainingComment;
	}

	public void setPreTrainingComment(String preTrainingComment) {
		this.preTrainingComment = preTrainingComment;
	}

	public int getSessionId() {
		return sessionId;
	}

	public void setSessionId(int sessionId) {
		this.sessionId = sessionId;
	}

	
}
