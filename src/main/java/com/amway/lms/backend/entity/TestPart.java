package com.amway.lms.backend.entity;

import java.io.Serializable;
import java.util.List;

import javax.persistence.*;


/**
 * The persistent class for the test_parts database table.
 * 
 */
@Entity
@Table(name="test_parts")
@NamedQueries({
    @NamedQuery(name="TestPart.findAll", query="SELECT t FROM TestPart t"),
    @NamedQuery(name="getTestPartListByTestId", query="SELECT tp FROM TestPart tp WHERE tp.id IN (SELECT tq.testPartId FROM TestQuestion tq WHERE tq.testId = ?) ORDER BY tp.id")
})

public class TestPart implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private int id;

	@Column(name="part_desc")
	private String partDesc;
	
	@Transient
	private List<Question> questions;
	
	@Transient
    private String tableName;

	public TestPart() {
	}

	public int getId() {
		return this.id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getPartDesc() {
		return this.partDesc;
	}

	public void setPartDesc(String partDesc) {
		this.partDesc = partDesc;
	}

    public List<Question> getQuestions() {
        return questions;
    }

    public void setQuestions(List<Question> questions) {
        this.questions = questions;
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

}