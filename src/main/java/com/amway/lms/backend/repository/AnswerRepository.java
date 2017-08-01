/**
 * 
 */
package com.amway.lms.backend.repository;

import java.util.List;

import com.amway.lms.backend.entity.Answer;

/**
 * @author acton
 * @email acton@enclave.vn
 */
public interface AnswerRepository {
    public List<Answer> getAnswerListByQuestionId(int questionId);
    public void addAnswer(Answer answer);
    public List<Answer> getAnswerListByQuestionIdAndIsCorrect(int questionId);
    public Answer getAnswerById(int answerId);
}
