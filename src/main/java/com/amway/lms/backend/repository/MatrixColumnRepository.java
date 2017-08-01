/**
 * 
 */
package com.amway.lms.backend.repository;

import java.util.List;

import com.amway.lms.backend.entity.MatrixColumn;

/**
 * @author acton
 * @email acton@enclave.vn
 */
public interface MatrixColumnRepository {
    public List<MatrixColumn> getMatrixColumnListByQuestionId(int questionId);
    public void addMatrixColumn(MatrixColumn matrixColumn);
}
