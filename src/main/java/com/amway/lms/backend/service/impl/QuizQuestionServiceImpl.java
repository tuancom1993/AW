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
import com.amway.lms.backend.common.ErrorCode;
import com.amway.lms.backend.common.Utils;
import com.amway.lms.backend.entity.Answer;
import com.amway.lms.backend.entity.Question;
import com.amway.lms.backend.entity.Quiz;
import com.amway.lms.backend.entity.QuizQuestion;
import com.amway.lms.backend.exception.AddObjectException;
import com.amway.lms.backend.exception.DeleteObjectException;
import com.amway.lms.backend.exception.ObjectNotFoundException;
import com.amway.lms.backend.exception.QuestionExistException;
import com.amway.lms.backend.repository.AnswerRepository;
import com.amway.lms.backend.repository.MatrixColumnRepository;
import com.amway.lms.backend.repository.MatrixCorrectRepository;
import com.amway.lms.backend.repository.MatrixRowRepository;
import com.amway.lms.backend.repository.QuestionRepository;
import com.amway.lms.backend.repository.QuizQuestionRepository;
import com.amway.lms.backend.repository.QuizRepository;
import com.amway.lms.backend.repository.ResultRepository;
import com.amway.lms.backend.service.QuizQuestionService;

/**
 * @author acton
 * @email acton@enclave.vn
 */
@Service
@Transactional
public class QuizQuestionServiceImpl implements QuizQuestionService {

    private static final Logger logger = LoggerFactory.getLogger(QuizQuestionServiceImpl.class);

    @Autowired
    QuizQuestionRepository quizQuestionRepository;

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
    MatrixCorrectRepository matrixCorrectRepository;
    
    @Autowired
    QuizRepository quizRepository;

    @Override
    public ResponseEntity<?> addQuizQuestionWithNewQuestion(int quizId, Question question)
            throws AddObjectException, Exception {
        try {
            logger.debug("addQuizQuestionWithNewQuestion");
            
            Quiz quiz = quizRepository.getQuizById(quizId);
            if (quiz == null) throw new AddObjectException("QuizQuestion cannot add, cause Quiz cannot found");
            
            question.setIsQuiz(1);
            question.setIsRequired(0);
            questionRepository.addQuestion(question);
            int questionId = question.getId();

            if (question.getQuestionTypeId() == QuestionTypes.RADIO.getValue() 
                    || question.getQuestionTypeId() == QuestionTypes.CHECKBOX.getValue()) {
                List<Answer> answers = question.getAnswers();
                if (answers == null || answers.isEmpty()) {
                    throw new AddObjectException("Cannot add Question. Cause don't have Answer in this Question");
                } else {
                    if(question.getQuestionTypeId() == QuestionTypes.RADIO.getValue() && countCorrectAnswer(answers) > 1){
                        throw new AddObjectException("Cannot add Question. Cause Question Radio have Answer Correct lager 1");
                    }
                    for (Answer answer : answers) {
                        answer.setId(0);
                        answer.setQuestionId(questionId);
                        if(answer.getCorrect() == null) answer.setCorrect(0);
                        answerRepository.addAnswer(answer);
                    }
                }
            } else {
                throw new AddObjectException("Cannot add Question. Cause type of Question is not correct.");
            }

            if(question.getQuestionTypeId() == QuestionTypes.RADIO.getValue()) 
                question.setQuestionType(QuestionTypes.RADIO.getStrValue());
            if(question.getQuestionTypeId() == QuestionTypes.CHECKBOX.getValue()) 
                question.setQuestionType(QuestionTypes.CHECKBOX.getStrValue());
            
            QuizQuestion quizQuestion = new QuizQuestion();
            quizQuestion.setQuizId(quizId);
            quizQuestion.setQuestionId(questionId);
            quizQuestion.setIndexQuestion(question.getNo() != 0 ? question.getNo() : getIndexQuestionForQuizQuestion(quizId));
            quizQuestionRepository.addQuizQuestion(quizQuestion);
            quizQuestion.setQuestion(question);

            updateNumberOfQuestion(quizId);
            
            return Utils.generateSuccessResponseEntity(quizQuestion);

        } catch (Exception e) {
            e.printStackTrace();
            logger.debug("Exception - addQuizQuestionWithNewQuestion: " + e.getMessage());
            return Utils.generateFailureResponseEntity(ErrorCode.CODE_ADD_OBJECT_EXCEPTION,
                    ErrorCode.MSG_ADD_OBJECT_EXCEPTION);
        }
    }

    @Override
    public ResponseEntity<?> addQuizQuestionWithOldQuestion(int quizId, Question question)
            throws AddObjectException, Exception {
        try {
            Quiz quiz = quizRepository.getQuizById(quizId);
            if (quiz == null) throw new AddObjectException("QuizQuestion cannot add, cause Quiz cannot found");
            
            Question questionCheck = questionRepository.getQuestionById(question.getId());
            if(questionCheck == null) throw new AddObjectException("QuizQuestion cannot add, cause Question cannot found");
            
            QuizQuestion quizQuestionCheck = quizQuestionRepository.getQuizQuestionByQuizIdAndQuestionId(quizId, question.getId());
            if(quizQuestionCheck != null) throw new QuestionExistException("Question is exist in quiz");
            
            QuizQuestion quizQuestion = new QuizQuestion();
            quizQuestion.setQuestionId(question.getId());
            quizQuestion.setQuizId(quizId);
            quizQuestion.setIndexQuestion(question.getNo() != 0 ? question.getNo() : getIndexQuestionForQuizQuestion(quizId));
            
            quizQuestionRepository.addQuizQuestion(quizQuestion);
            quizQuestion.setQuestion(questionCheck);
            
            updateNumberOfQuestion(quizId);
            
            if(question.getQuestionTypeId() == QuestionTypes.RADIO.getValue()) 
                question.setQuestionType(QuestionTypes.RADIO.getStrValue());
            if(question.getQuestionTypeId() == QuestionTypes.CHECKBOX.getValue()) 
                question.setQuestionType(QuestionTypes.CHECKBOX.getStrValue());
            
            return Utils.generateSuccessResponseEntity(quizQuestion);
        } catch (QuestionExistException e){
            e.printStackTrace();
            return Utils.generateFailureResponseEntity(ErrorCode.CODE_QUESTION_EXIST_EXCEPTION, ErrorCode.MSG_QUESTION_EXSIT_EXCEPTION);
        } catch (Exception e) {
            e.printStackTrace();
            return Utils.generateFailureResponseEntity(ErrorCode.CODE_ADD_OBJECT_EXCEPTION, ErrorCode.MSG_ADD_OBJECT_EXCEPTION);
        }
        
    }

    @Override
    public ResponseEntity<?> deleteQuizQuestion(int quizId, int questionId)
            throws ObjectNotFoundException, DeleteObjectException, Exception {
        try {
            QuizQuestion quizQuestion = quizQuestionRepository.getQuizQuestionByQuizIdAndQuestionId(quizId, questionId);
            if (quizQuestion == null)
                throw new DeleteObjectException("Cannot delete QuizQuestion. Cause not found QuizQuestion");
            quizQuestionRepository.deleteQuizQuestion(quizQuestion);
            updateNumberOfQuestion(quizId);
            return Utils.generateSuccessResponseEntity(quizQuestion);
        } catch (Exception e) {
            e.printStackTrace();
            return Utils.generateFailureResponseEntity(ErrorCode.CODE_DELETE_OBJECT_EXCEPTION, ErrorCode.MSG_DELETE_OBJECT_EXCEPTION);
        }
    }

    private void updateNumberOfQuestion(int quizId) throws Exception{
        try {
            Quiz quiz = quizRepository.getQuizById(quizId);
            int numberOfQuestion = quizQuestionRepository.getQuizQuestionListByQuizId(quizId).size();
            quiz.setNumberQuestions(numberOfQuestion);
            quizRepository.editQuiz(quiz);
        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception("Cannot update NumberOfQuestion");
        }
    }
    
    private int countCorrectAnswer(List<Answer> answers){
        int total = 0;
        for(Answer answer : answers){
            if(answer.getCorrect() != null && answer.getCorrect() == 1) total++;
        }
        return total;
    }
    
    private int getIndexQuestionForQuizQuestion(int quizId){
        try {
            List<QuizQuestion> quizQuestions = quizQuestionRepository.getQuizQuestionListByQuizId(quizId);
            return quizQuestions.get(quizQuestions.size()-1).getIndexQuestion() + 1;
        } catch (Exception e) {
            e.printStackTrace();
            return 1;
        }
    }
}
