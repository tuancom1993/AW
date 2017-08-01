/**
 * 
 */
package com.amway.lms.backend.repository.impl;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.amway.lms.backend.entity.MatrixCorrect;
import com.amway.lms.backend.repository.AbstractRepository;
import com.amway.lms.backend.repository.MatrixCorrectRepository;

/**
 * @author acton
 * @email acton@enclave.vn
 */
@Repository
public class MatrixCorrectRepositoryImpl extends AbstractRepository<Integer, MatrixCorrect> implements MatrixCorrectRepository{

    @Override
    public void addMatrixCorrect(MatrixCorrect matrixCorrect) {
        persist(matrixCorrect);
    }

    @Override
    public List<MatrixCorrect> getMatrixCorrectListByQuestionId(int questionId) {
        // TODO Auto-generated method stub
        return null;
    }

}
