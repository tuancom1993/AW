package com.amway.lms.backend.entity;

import java.io.Serializable;
import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * The persistent class for the matrix_rows database table.
 * 
 */
@Entity
@Table(name = "matrix_rows")
@NamedQueries({
        @NamedQuery(name = "getMatrixRowListByQuestionId", query = "SELECT m FROM MatrixRow m Where m.questionId = ?") })
public class MatrixRow implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private int id;

    @Column(name = "question_id")
    private int questionId;

    @Lob
    @Column(name = "row_desc")
    private String rowDesc;
    
    @Transient
    @JsonIgnore
    private long total;

    public MatrixRow() {
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

    public String getRowDesc() {
        return this.rowDesc;
    }

    public void setRowDesc(String rowDesc) {
        this.rowDesc = rowDesc;
    }

    public long getTotal() {
        return total;
    }

    public void setTotal(long total) {
        this.total = total;
    }

}