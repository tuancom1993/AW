package com.amway.lms.backend.model;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

public class EmailInfor {

	private String subject;
	private String htmlContent;
	private String linkSurvey;
	private List<String> emails = new ArrayList<>();
	@JsonProperty
	private boolean isSaveEmailTemplate;
	
	private List<Integer> lstUserId = new ArrayList<>();

	//acton
	private int surveyId;

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public String getHtmlContent() {
		return htmlContent;
	}

	public void setHtmlContent(String htmlContent) {
		this.htmlContent = htmlContent;
	}

	public String getLinkSurvey() {
		return linkSurvey;
	}

	public void setLinkSurvey(String linkSurvey) {
		this.linkSurvey = linkSurvey;
	}

	public boolean isSaveEmailTemplate() {
		return isSaveEmailTemplate;
	}

	public void setSaveEmailTemplate(boolean isSaveEmailTemplate) {
		this.isSaveEmailTemplate = isSaveEmailTemplate;
	}

	public List<String> getEmails() {
		return emails;
	}

	public void setEmails(List<String> emails) {
		this.emails = emails;
	}

	public List<Integer> getLstUserId() {
		return lstUserId;
	}

	public void setLstUserId(List<Integer> lstUserId) {
		this.lstUserId = lstUserId;
	}

    public int getSurveyId() {
        return surveyId;
    }

    public void setSurveyId(int surveyId) {
        this.surveyId = surveyId;
    }
}
