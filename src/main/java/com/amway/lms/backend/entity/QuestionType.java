package com.amway.lms.backend.entity;

import java.io.Serializable;
import javax.persistence.*;


/**
 * The persistent class for the question_types database table.
 * 
 */
@Entity
@Table(name="question_types")
@NamedQueries({
    @NamedQuery(name="getQuestionTypeById", query="SELECT q FROM QuestionType q Where q.id = ?"),
    @NamedQuery(name="getQuestionTypeNameById", query="SELECT q.name FROM QuestionType q Where q.id = ?"),
})

public class QuestionType implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	private int id;

	private String name;

	public QuestionType() {
	}

	public int getId() {
		return this.id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

}