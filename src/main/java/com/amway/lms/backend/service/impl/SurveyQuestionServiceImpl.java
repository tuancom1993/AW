/**
 * 
 */
package com.amway.lms.backend.service.impl;

import java.util.List;

import javax.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.amway.lms.backend.common.AmwayEnum.QuestionTypes;
import com.amway.lms.backend.common.Common;
import com.amway.lms.backend.common.ErrorCode;
import com.amway.lms.backend.common.Utils;
import com.amway.lms.backend.entity.Answer;
import com.amway.lms.backend.entity.MatrixColumn;
import com.amway.lms.backend.entity.MatrixRow;
import com.amway.lms.backend.entity.Question;
import com.amway.lms.backend.entity.Survey;
import com.amway.lms.backend.entity.SurveyQuestion;
import com.amway.lms.backend.exception.AddObjectException;
import com.amway.lms.backend.exception.DeleteObjectException;
import com.amway.lms.backend.exception.ObjectNotFoundException;
import com.amway.lms.backend.exception.QuestionExistException;
import com.amway.lms.backend.repository.AnswerRepository;
import com.amway.lms.backend.repository.MatrixColumnRepository;
import com.amway.lms.backend.repository.MatrixRowRepository;
import com.amway.lms.backend.repository.QuestionRepository;
import com.amway.lms.backend.repository.QuestionTypeRepository;
import com.amway.lms.backend.repository.ResultRepository;
import com.amway.lms.backend.repository.SurveyQuestionRepository;
import com.amway.lms.backend.repository.SurveyRepository;
import com.amway.lms.backend.service.SurveyQuestionService;

/**
 * @author acton
 * @email acton@enclave.vn
 */
@Transactional
@Service
public class SurveyQuestionServiceImpl implements SurveyQuestionService {

    private static final Logger logger = LoggerFactory.getLogger(SurveyQuestionServiceImpl.class);

    @Autowired
    SurveyQuestionRepository surveyQuestionRepository;

    @Autowired
    SurveyRepository surveyRepository;

    @Autowired
    QuestionRepository questionRepository;

    @Autowired
    AnswerRepository answerRepository;

    @Autowired
    MatrixColumnRepository matrixColumnRepository;

    @Autowired
    MatrixRowRepository matrixRowRepository;

    @Autowired
    ResultRepository resultRepository;
    
    @Autowired
    QuestionTypeRepository questionTypeRepository;

    @Override
    public ResponseEntity<?> addSurveyQuestionWithNewQuestion(SurveyQuestion surveyQuestion) {
        try {
            logger.debug("addSurveyQuestionWithNewQuestion");
            Question question = surveyQuestion.getQuestion();
            question.setIsQuiz(0);
            questionRepository.addQuestion(question);
            int questionId = question.getId();

            if (question.getQuestionTypeId() == QuestionTypes.RADIO.getValue() 
                    || question.getQuestionTypeId() == QuestionTypes.CHECKBOX.getValue()
                    || question.getQuestionTypeId() == QuestionTypes.CHECKBOX_OTHER.getValue() 
                    || question.getQuestionTypeId() == QuestionTypes.DROP_DOWN_LIST.getValue()) {
                List<Answer> answers = question.getAnswers();
                if (answers != null)
                    for (Answer answer : answers) {
                        answer.setQuestionId(questionId);
                        answer.setId(0);
                        answerRepository.addAnswer(answer);
                    }
            } else if (question.getQuestionTypeId() == QuestionTypes.TEXT_BOX.getValue() 
                    || question.getQuestionTypeId() == QuestionTypes.COMMENT.getValue()
                    || question.getQuestionTypeId() == QuestionTypes.EMAIL.getValue()) {
                List<Answer> answers = question.getAnswers();
                if (answers != null && !answers.isEmpty()) {
                    Answer answer = answers.get(0);
                    answer.setId(0);
                    answer.setQuestionId(questionId);
                    answerRepository.addAnswer(answer);
                }

            } else if (question.getQuestionTypeId() == QuestionTypes.MATRIX.getValue()) {
                List<MatrixColumn> matrixColumns = question.getMatrixColumns();
                if (matrixColumns != null)
                    for (MatrixColumn matrixColumn : matrixColumns) {
                        matrixColumn.setQuestionId(questionId);
                        matrixColumn.setId(0);
                        matrixColumnRepository.addMatrixColumn(matrixColumn);
                    }
                List<MatrixRow> matrixRows = question.getMatrixRows();
                if (matrixRows != null)
                    for (MatrixRow matrixRow : matrixRows) {
                        matrixRow.setQuestionId(questionId);
                        matrixRow.setId(0);
                        matrixRowRepository.addMatrixRow(matrixRow);
                    }
            }

            question.setQuestionType(Common.getQuestionTypes(question.getQuestionTypeId()));

            surveyQuestion.setQuestionId(questionId);
            surveyQuestion.setIndexQuestion(question.getNo());
            surveyQuestionRepository.addSurveyQuestion(surveyQuestion);
            surveyQuestion.setQuestion(question);

            return Utils.generateSuccessResponseEntity(new QuestionReturn(questionId));
        } catch (Exception e) {
            e.printStackTrace();
            logger.debug("Exception - addSurveyQuestionWithNewQuestion: " + e.getMessage());
            return Utils.generateFailureResponseEntity(ErrorCode.CODE_ADD_OBJECT_EXCEPTION,
                    ErrorCode.MSG_ADD_OBJECT_EXCEPTION);
        }
    }

    @Override
    public ResponseEntity<?> addSurveyQuestionWithOldQuestion(SurveyQuestion surveyQuestion)
            throws AddObjectException, Exception {
        logger.debug("addSurveyQuestionWithOldQuestion");
        try {
            Question question = surveyQuestion.getQuestion();
            Question questionEntity = questionRepository.getQuestionById(question.getId());
            if (questionEntity == null)
                throw new AddObjectException("SurveyQuestion cannot add, cause Question cannot found");

            SurveyQuestion surveyQuestionCheckExist = surveyQuestionRepository
                    .getSurveyQuestionBySurveyIdAndQuestionId(surveyQuestion.getSurveyId(), question.getId());
            if (surveyQuestionCheckExist != null)
                throw new QuestionExistException("Question is exist in survey");

            surveyQuestion.setQuestionId(question.getId());
            surveyQuestion.setIndexQuestion(question.getNo());
            surveyQuestionRepository.addSurveyQuestion(surveyQuestion);
            
            questionEntity.setQuestionType(Common.getQuestionTypes(question.getQuestionTypeId()));
            surveyQuestion.setQuestion(questionEntity);

            return Utils.generateSuccessResponseEntity(surveyQuestion);
        } catch (QuestionExistException e) {
            e.printStackTrace();
            logger.error("QuestionExistException - addSurveyQuestionWithOldQuestion: " + e.getMessage());
            return Utils.generateFailureResponseEntity(ErrorCode.CODE_QUESTION_EXIST_EXCEPTION,
                    ErrorCode.MSG_QUESTION_EXSIT_EXCEPTION);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("Exception - addSurveyQuestionWithOldQuestion: " + e.getMessage());
            return Utils.generateFailureResponseEntity(ErrorCode.CODE_ADD_OBJECT_EXCEPTION,
                    ErrorCode.MSG_ADD_OBJECT_EXCEPTION);
        }

    }

    @Override
    public ResponseEntity<?> deleteSurveyQuestion(int surveyId, int questionId)
            throws ObjectNotFoundException, DeleteObjectException, Exception {
        logger.debug("deleteSurveyQuestion");
        try {
            Survey survey = surveyRepository.getSurveyById(surveyId);
            if (survey.getSurveyTypeId() == 2 && questionId <= 6)
                throw new DeleteObjectException("Cannot delete question with id < 6 on Survey with SurveyTypeId = 2");
            
            if (survey.getSurveyTypeId() == 1 && questionId <= 26)
                throw new DeleteObjectException("Cannot delete question with id < 26 on Survey with SurveyTypeId = 1");
            
            SurveyQuestion surveyQuestion = surveyQuestionRepository.getSurveyQuestionBySurveyIdAndQuestionId(surveyId,
                    questionId);
            if (surveyQuestion == null)
                throw new ObjectNotFoundException(
                        "SurveyQuestion not found with surveyId = " + surveyId + " and questionId = " + questionId);
            else {
                surveyQuestionRepository.deleteSurveyQuestion(surveyQuestion);
                resultRepository.deleteResultBySurveyIdAndQuestionId(surveyId, questionId);
                return Utils.generateSuccessResponseEntity(surveyQuestion);
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("Exception - deleteSurveyQuestion: " + e.getMessage());
            return Utils.generateFailureResponseEntity(ErrorCode.CODE_DELETE_OBJECT_EXCEPTION,
                    ErrorCode.MSG_DELETE_OBJECT_EXCEPTION);
        }
    }
    
    private class QuestionReturn{
        private int id;

        public QuestionReturn(int id){
            this.id = id;
        }
        
        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }
        
    }

}
