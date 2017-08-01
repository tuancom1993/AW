package com.amway.lms.backend.model;

public class CourseInformation extends DashBoard {
	
	private GeneralInformation generalInformation;
	private LocationAndTime locationAndTime;
	private QuizResult quizResult;

	public GeneralInformation getGeneralInformation() {
		return generalInformation;
	}

	public void setGeneralInformation(GeneralInformation generalInformation) {
		this.generalInformation = generalInformation;
	}

	public LocationAndTime getLocationAndTime() {
		return locationAndTime;
	}

	public void setLocationAndTime(LocationAndTime locationAndTime) {
		this.locationAndTime = locationAndTime;
	}

	public QuizResult getQuizResult() {
		return quizResult;
	}

	public void setQuizResult(QuizResult quizResult) {
		this.quizResult = quizResult;
	}

}
