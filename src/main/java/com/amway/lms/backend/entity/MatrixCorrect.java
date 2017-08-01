package com.amway.lms.backend.entity;

import java.io.Serializable;
import javax.persistence.*;

/**
 * The persistent class for the matrix_correct database table.
 * 
 */
@Entity
@Table(name = "matrix_correct")
@NamedQueries({ @NamedQuery(name = "MatrixCorrect.findAll", query = "SELECT m FROM MatrixCorrect m") })
public class MatrixCorrect implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private int id;

    @Column(name = "column_matrix_id")
    private int columnMatrixId;

    @Column(name = "question_id")
    private int questionId;

    @Column(name = "row_matrix_id")
    private int rowMatrixId;

    public MatrixCorrect() {
    }

    public int getId() {
        return this.id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getColumnMatrixId() {
        return this.columnMatrixId;
    }

    public void setColumnMatrixId(int columnMatrixId) {
        this.columnMatrixId = columnMatrixId;
    }

    public int getQuestionId() {
        return this.questionId;
    }

    public void setQuestionId(int questionId) {
        this.questionId = questionId;
    }

    public int getRowMatrixId() {
        return this.rowMatrixId;
    }

    public void setRowMatrixId(int rowMatrixId) {
        this.rowMatrixId = rowMatrixId;
    }

}