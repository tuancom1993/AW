package com.amway.lms.backend.entity;

import java.io.Serializable;
import javax.persistence.*;

/**
 * The persistent class for the quiz_questions database table.
 * 
 */
@Entity
@Table(name = "quiz_questions")
@NamedQueries({
        @NamedQuery(name = "getQuizQuestionListByQuizId", query = "SELECT q FROM QuizQuestion q Where q.quizId = ? Order By q.indexQuestion"),
        @NamedQuery(name = "getQuizQuestionByQuizIdAndQuestionId", query = "SELECT q FROM QuizQuestion q Where q.quizId = ? and q.questionId = ?"),
        @NamedQuery(name = "deleteQuizQuestionByQuizId", query = "DELETE FROM QuizQuestion q Where q.quizId = ?")
        })

public class QuizQuestion implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private int id;

    @Column(name = "question_id")
    private int questionId;

    @Transient
    private Question question;

    @Column(name="quiz_id")
	private int quizId;
    
    @Column(name = "index_question")
    private int indexQuestion;
    
    public QuizQuestion() {
    }

    public int getId() {
        return this.id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getQuestionId() {
        return this.questionId;
    }

    public void setQuestionId(int questionId) {
        this.questionId = questionId;
    }

    public int getQuizId() {
        return this.quizId;
    }

    public void setQuizId(int quizId) {
        this.quizId = quizId;
    }

    public Question getQuestion() {
        return question;
    }

    public void setQuestion(Question question) {
        this.question = question;
    }

    public int getIndexQuestion() {
        return indexQuestion;
    }

    public void setIndexQuestion(int indexQuestion) {
        this.indexQuestion = indexQuestion;
    }

}