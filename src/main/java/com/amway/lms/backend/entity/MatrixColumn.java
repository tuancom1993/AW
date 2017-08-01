package com.amway.lms.backend.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * The persistent class for the matrix_columns database table.
 * 
 */
@Entity
@Table(name = "matrix_columns")
@NamedQueries({
        @NamedQuery(name = "getMatrixColumnListByQuestionId", query = "SELECT m FROM MatrixColumn m Where m.questionId = ?") })

public class MatrixColumn implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    @Lob
    @Column(name = "column_desc")
    private String columnDesc;

    @Column(name = "question_id")
    private int questionId;

    @Transient
    @JsonIgnore
    private List<Long> chooseTimes = new ArrayList<>();
    
    public MatrixColumn() {
    }

    public int getId() {
        return this.id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getColumnDesc() {
        return this.columnDesc;
    }

    public void setColumnDesc(String columnDesc) {
        this.columnDesc = columnDesc;
    }

    public int getQuestionId() {
        return this.questionId;
    }

    public void setQuestionId(int questionId) {
        this.questionId = questionId;
    }

    public List<Long> getChooseTimes() {
        return chooseTimes;
    }

    public void setChooseTimes(List<Long> chooseTimes) {
        this.chooseTimes = chooseTimes;
    }

}