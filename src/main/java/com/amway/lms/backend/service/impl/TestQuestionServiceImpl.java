package com.amway.lms.backend.service.impl;

import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.amway.lms.backend.common.ErrorCode;
import com.amway.lms.backend.common.Utils;
import com.amway.lms.backend.common.AmwayEnum.QuestionTypes;
import com.amway.lms.backend.entity.Answer;
import com.amway.lms.backend.entity.Question;
import com.amway.lms.backend.entity.TestQuestion;
import com.amway.lms.backend.exception.AddObjectException;
import com.amway.lms.backend.exception.DeleteObjectException;
import com.amway.lms.backend.exception.ObjectNotFoundException;
import com.amway.lms.backend.exception.QuestionExistException;
import com.amway.lms.backend.repository.AnswerRepository;
import com.amway.lms.backend.repository.QuestionRepository;
import com.amway.lms.backend.repository.TestPartRepository;
import com.amway.lms.backend.repository.TestQuestionRepository;
import com.amway.lms.backend.repository.TestRepository;
import com.amway.lms.backend.service.TestQuestionService;

@Service
@Transactional
public class TestQuestionServiceImpl implements TestQuestionService {

    @Autowired
    TestRepository testRepository;

    @Autowired
    TestPartRepository testPartRepository;

    @Autowired
    TestQuestionRepository testQuestionRepository;

    @Autowired
    QuestionRepository questionRepository;

    @Autowired
    AnswerRepository answerRepository;

    @Override
    public ResponseEntity<?> addTestQuestionWithNewQuestion(TestQuestion testQuestion)
            throws AddObjectException, Exception {
        try {
            Question question = testQuestion.getQuestion();
            validateQuestions(question, true);
            question.setIsQuiz(2);
            question.setIsRequired(0);

            questionRepository.addQuestion(question);
            for (Answer answer : question.getAnswers()) {
                answer.setId(0);
                answer.setQuestionId(question.getId());
                answerRepository.addAnswer(answer);
            }

            testQuestion.setQuestionId(question.getId());
            testQuestionRepository.addTestQuestion(testQuestion);

            return Utils.generateSuccessResponseEntity(testQuestion);
        } catch (Exception e) {
            e.printStackTrace();
            return Utils.generateFailureResponseEntity(ErrorCode.CODE_ADD_OBJECT_EXCEPTION,
                    ErrorCode.MSG_ADD_OBJECT_EXCEPTION);
        }
    }

    @Override
    public ResponseEntity<?> addTestQuestionWithOldQuestion(TestQuestion testQuestion)
            throws AddObjectException, Exception {
        try {
            int questionId = testQuestion.getQuestion().getId();
            Question questionCheckExits = questionRepository.getQuestionById(questionId);
            if (questionCheckExits == null)
                throw new ObjectNotFoundException("TestQuestion cannot add, cause Question cannot found with id =" + questionId);
            
            TestQuestion testQuestionCheckExist = testQuestionRepository.getTestQuestionByTestIdAndQuestionId(testQuestion.getTestId(), questionId);
            if(testQuestionCheckExist != null)
                throw new QuestionExistException("Duplicate...Question is exist in Test");
            
            testQuestion.setQuestionId(questionId);
            testQuestionRepository.addTestQuestion(testQuestion);
            
            testQuestion.setQuestion(questionCheckExits);
            
            return Utils.generateSuccessResponseEntity(testQuestion);
        } catch (Exception e) {
            e.printStackTrace();
            return Utils.generateFailureResponseEntity(ErrorCode.CODE_ADD_OBJECT_EXCEPTION,
                    ErrorCode.MSG_ADD_OBJECT_EXCEPTION);
        }
    }

    @Override
    public ResponseEntity<?> deleteTestQuestion(int testId, int questionId)
            throws ObjectNotFoundException, DeleteObjectException, Exception {
        try {
            TestQuestion testQuestion = testQuestionRepository.getTestQuestionByTestIdAndQuestionId(testId, questionId);
            if(testQuestion == null)
                throw new DeleteObjectException("Cannot delete testquestion. Cause cannot find testquestion");
            
            testQuestionRepository.deleteTestQuestion(testQuestion);
            
            return Utils.generateSuccessResponseEntity(testQuestion);
        } catch (Exception e) {
            e.printStackTrace();
            return Utils.generateFailureResponseEntity(ErrorCode.CODE_DELETE_OBJECT_EXCEPTION,
                    ErrorCode.MSG_DELETE_OBJECT_EXCEPTION);
        }
    }

    private void validateQuestions(Question question, boolean isValidateRoot) throws Exception {
        if (question == null)
            throw new Exception("Question is null or empty");

        if (StringUtils.isEmpty(question.getQuestionDesc()))
            throw new Exception("Question Desc is null or empty");

        if (question.getQuestionTypeId() != QuestionTypes.CHECKBOX.getValue()
                && question.getQuestionTypeId() != QuestionTypes.RADIO.getValue())
            throw new Exception("Question Type Id is not correct");

        if (isValidateRoot)
            validateAnswers(question);

    }

    private void validateAnswers(Question question) throws Exception {
        List<Answer> answers = question.getAnswers();
        if (answers == null || answers.isEmpty())
            throw new Exception("Answer List is null or empty");
        int numberAnswerCorrect = 0;
        for (Answer answer : answers) {
            if (StringUtils.isEmpty(answer.getAnswerDesc()))
                throw new Exception("Answer Desc is null or empty");

            if (answer.getCorrect() == null || answer.getCorrect() == 0)
                numberAnswerCorrect += 0;
            else
                numberAnswerCorrect += 1;
        }
        if (numberAnswerCorrect == 0)
            throw new Exception("Answers don't have any answer correct");
        if (question.getQuestionTypeId() == QuestionTypes.RADIO.getValue()) {
            if (numberAnswerCorrect != 1)
                throw new Exception("Question RADIO have more than one Answer correct: " + numberAnswerCorrect + " | "
                        + question.getQuestionDesc());
        }
    }

}
