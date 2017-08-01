package com.amway.lms.backend.model;

public class QuizResult {
    private String userName;
	private String numberOfTesting;
	private String correctAnswer;
	private String quizStatus;
	private int score;
	private String quizResult;
	private int isMaxTest;

	public String getNumberOfTesting() {
		return numberOfTesting;
	}

	public void setNumberOfTesting(String numberOfTesting) {
		this.numberOfTesting = numberOfTesting;
	}

	public String getCorrectAnswer() {
		return correctAnswer;
	}

	public void setCorrectAnswer(String correctAnswer) {
		this.correctAnswer = correctAnswer;
	}

	public String getQuizStatus() {
		return quizStatus;
	}

	public void setQuizStatus(String quizStatus) {
		this.quizStatus = quizStatus;
	}

	public int getScore() {
		return score;
	}

	public void setScore(int score) {
		this.score = score;
	}

	public String getQuizResult() {
		return quizResult;
	}

	public void setQuizResult(String quizResult) {
		this.quizResult = quizResult;
	}

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public int getIsMaxTest() {
        return isMaxTest;
    }

    public void setIsMaxTest(int isMaxTest) {
        this.isMaxTest = isMaxTest;
    }

}
