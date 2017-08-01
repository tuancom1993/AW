package com.amway.lms.backend.common;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

@Component
public class EmailHepler {

	private static final String URL_ACCEPT_BY_EMAIL = "email.accept.course";

	private static final String URL_DENY_BY_EMAIL = "email.deny.course";

	private static final String URL_TRAINEE_ACCEPT_BY_EMAIL = "email.trainee.accept.course";

	private static final String URL_TRAINEEDENY_BY_EMAIL = "email.trainee.deny.course";
	
	private static final String URL_LEARNING_DASHBORAD = "email.trainee.dashboard";

	private static final String SUBJECT_SEND_MANAGER = "email.subject.send.manager";

	private static final String SUBJECT_SEND_TRAINEE = "email.subject.send.trainee";
	
	private static final String URL_TAKE_QUIZ = "email.quiz";
	
	private static final String URL_POST_SURVEY_TRAINEE = "email.post.survey.trainee";
	
	private static final String URL_POST_SURVEY_TRAINER = "email.post.survey.trainer";
	
	private static final String SUBJECT_SEND_CHANGE_SESSION_INFOR = "email.subject.send.change.session.information";
	
	private static final String URL_TAKE_TEST = "email.take.test";

	@Autowired
	private Environment env;

	private static String linkAccept;
	private static String linkDeny;
	private static String linkTrainneeAccept;
	private static String linkTraineeDeny;
	private static String linkLearning;
	private static String subjectToManager;
	private static String subjectTotrainee;
	private static String linkTakeQuiz;
	private static String linkPostSurveyTrainee;
	private static String linkPostSurveyTrainer;
	private static String subjectChangeSessionInfor;
	private static String linkTakeTest;

	@PostConstruct
	public void init() {
		// COAST related
		linkAccept = env.getProperty(URL_ACCEPT_BY_EMAIL);
		linkDeny = env.getProperty(URL_DENY_BY_EMAIL);
		linkTrainneeAccept = env.getProperty(URL_TRAINEE_ACCEPT_BY_EMAIL);
		linkTraineeDeny = env.getProperty(URL_TRAINEEDENY_BY_EMAIL);
		linkLearning = env.getProperty(URL_LEARNING_DASHBORAD);
		subjectToManager = env.getProperty(SUBJECT_SEND_MANAGER);
		subjectTotrainee = env.getProperty(SUBJECT_SEND_TRAINEE);
		linkTakeQuiz = env.getProperty(URL_TAKE_QUIZ);
		linkPostSurveyTrainee = env.getProperty(URL_POST_SURVEY_TRAINEE);
		linkPostSurveyTrainer = env.getProperty(URL_POST_SURVEY_TRAINER);
		subjectChangeSessionInfor = env.getProperty(SUBJECT_SEND_CHANGE_SESSION_INFOR);
		linkTakeTest = env.getProperty(URL_TAKE_TEST);
	}

	public String getLinkAccept() {
		return linkAccept;
	}

	public String getLinkDeny() {
		return linkDeny;
	}

	public String getLinkTrainneeAccept() {
		return linkTrainneeAccept;
	}

	public String getLinkTraineeDeny() {
		return linkTraineeDeny;
	}

	public String getLinkLearning() {
		return linkLearning;
	}

	public String getSubjectToManager() {
		return subjectToManager;
	}

	public String getSubjectTotrainee() {
		return subjectTotrainee;
	}

    public String getLinkTakeQuiz() {
        return linkTakeQuiz;
    }

    public String getLinkPostSurveyTrainee() {
        return linkPostSurveyTrainee;
    }

    public String getLinkPostSurveyTrainer() {
        return linkPostSurveyTrainer;
    }

    public String getSubjectChangeSessionInfor() {
        return subjectChangeSessionInfor;
    }
    
    public String getLinkTakeTest() {
        return linkTakeTest;
    }
}
