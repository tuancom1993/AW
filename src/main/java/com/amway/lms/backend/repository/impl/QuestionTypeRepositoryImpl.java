/**
 * 
 */
package com.amway.lms.backend.repository.impl;

import org.hibernate.Query;
import org.springframework.stereotype.Repository;

import com.amway.lms.backend.entity.QuestionType;
import com.amway.lms.backend.repository.AbstractRepository;
import com.amway.lms.backend.repository.QuestionTypeRepository;

/**
 * @author acton
 * @email acton@enclave.vn
 */
@Repository
public class QuestionTypeRepositoryImpl extends AbstractRepository<Integer, QuestionType> implements QuestionTypeRepository{

    @Override
    public QuestionType getQuestionTypeById(int questionTypeId) {
        Query query = createNamedQuery("getQuestionTypeById", -1, -1, questionTypeId);
        return (QuestionType) query.uniqueResult();
    }
    
    @Override
    public String getQuestionTypeNameById(int questionTypeId) {
        Query query = createNamedQuery("getQuestionTypeNameById", -1, -1, questionTypeId);
        return (String) query.uniqueResult();
    }

}
