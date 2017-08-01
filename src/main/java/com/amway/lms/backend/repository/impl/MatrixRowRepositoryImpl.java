/**
 * 
 */
package com.amway.lms.backend.repository.impl;

import java.util.List;

import org.hibernate.Query;
import org.springframework.stereotype.Repository;

import com.amway.lms.backend.entity.MatrixRow;
import com.amway.lms.backend.repository.AbstractRepository;
import com.amway.lms.backend.repository.MatrixRowRepository;

/**
 * @author acton
 * @email acton@enclave.vn
 */
@Repository
public class MatrixRowRepositoryImpl extends AbstractRepository<Integer, MatrixRow> implements MatrixRowRepository{

    @Override
    public List<MatrixRow> getMatrixRowListByQuestionId(int questionId) {
        Query query = createNamedQuery("getMatrixRowListByQuestionId", -1, -1, questionId);
        return query.list();
    }

    /* (non-Javadoc)
     * @see com.amway.lms.backend.repository.MatrixRowRepository#addMatrixRow(com.amway.lms.backend.entity.MatrixRow)
     */
    @Override
    public void addMatrixRow(MatrixRow matrixRow) {
        persist(matrixRow);
    }

}
