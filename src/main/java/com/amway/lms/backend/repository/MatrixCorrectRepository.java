/**
 * 
 */
package com.amway.lms.backend.repository;

import java.util.List;

import com.amway.lms.backend.entity.MatrixCorrect;

/**
 * @author acton
 * @email acton@enclave.vn
 */
public interface MatrixCorrectRepository {
    public void addMatrixCorrect(MatrixCorrect matrixCorrect);
    public List<MatrixCorrect> getMatrixCorrectListByQuestionId(int questionId);
}
