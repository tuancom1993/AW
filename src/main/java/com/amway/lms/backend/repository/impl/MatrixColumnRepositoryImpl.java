/**
 * 
 */
package com.amway.lms.backend.repository.impl;

import java.util.List;

import org.hibernate.Query;
import org.springframework.stereotype.Repository;

import com.amway.lms.backend.entity.MatrixColumn;
import com.amway.lms.backend.repository.AbstractRepository;
import com.amway.lms.backend.repository.MatrixColumnRepository;

/**
 * @author acton
 * @email acton@enclave.vn
 */
@Repository
public class MatrixColumnRepositoryImpl extends AbstractRepository<Integer, MatrixColumn> implements MatrixColumnRepository{

    @Override
    public List<MatrixColumn> getMatrixColumnListByQuestionId(int questionId) {
        Query query = createNamedQuery("getMatrixColumnListByQuestionId", -1, -1, questionId);
        return query.list();
    }

    @Override
    public void addMatrixColumn(MatrixColumn matrixColumn) {
        persist(matrixColumn);
    }

}
