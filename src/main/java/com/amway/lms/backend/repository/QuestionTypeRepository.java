/**
 * 
 */
package com.amway.lms.backend.repository;

import com.amway.lms.backend.entity.QuestionType;

/**
 * @author acton
 * @email acton@enclave.vn
 */
public interface QuestionTypeRepository {
    public QuestionType getQuestionTypeById(int questionTypeId);
    public String getQuestionTypeNameById(int questionTypeId);
}
