/**
 * 
 */
package com.amway.lms.backend.repository;

import java.util.List;

import com.amway.lms.backend.entity.MatrixRow;

/**
 * @author acton
 * @email acton@enclave.vn
 */
public interface MatrixRowRepository {
    public List<MatrixRow> getMatrixRowListByQuestionId(int questionId);
    public void addMatrixRow(MatrixRow matrixRow);
}
