package com.amway.lms.backend.service.impl;/**
 * @author acton
 * @email acton@enclave.vn
 */

import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import com.amway.lms.backend.common.AmwayEnum.QuestionTypes;
import com.amway.lms.backend.common.AmwayEnum.QuizResultStatus;
import com.amway.lms.backend.common.AmwayEnum.QuizStatus;
import com.amway.lms.backend.common.ErrorCode;
import com.amway.lms.backend.common.Utils;
import com.amway.lms.backend.entity.Answer;
import com.amway.lms.backend.entity.Course;
import com.amway.lms.backend.entity.Question;
import com.amway.lms.backend.entity.Quiz;
import com.amway.lms.backend.entity.QuizResult;
import com.amway.lms.backend.entity.Result;
import com.amway.lms.backend.entity.Session;
import com.amway.lms.backend.entity.SessionParticipant;
import com.amway.lms.backend.entity.User;
import com.amway.lms.backend.exception.ObjectNotFoundException;
import com.amway.lms.backend.exception.QuizNotAvailableException;
import com.amway.lms.backend.repository.AnswerRepository;
import com.amway.lms.backend.repository.CourseRepository;
import com.amway.lms.backend.repository.QuestionRepository;
import com.amway.lms.backend.repository.QuizQuestionRepository;
import com.amway.lms.backend.repository.QuizRepository;
import com.amway.lms.backend.repository.SessionParticipantRepository;
import com.amway.lms.backend.repository.SessionRepository;
import com.amway.lms.backend.repository.UserRepository;
import com.amway.lms.backend.rest.SessionParticipantController;
import com.amway.lms.backend.service.QuizResultService;

/**
 * @author acton
 * @email acton@enclave.vn
 */
@Service
@Transactional
@EnableTransactionManagement
public class QuizResultServiceImpl implements QuizResultService {
    
    private static final Logger logger = LoggerFactory.getLogger(SessionParticipantController.class);

    @Autowired
    QuizRepository quizRepository;

    @Autowired
    QuizQuestionRepository quizQuestionRepository;

    @Autowired
    QuestionRepository questionRepository;

    @Autowired
    AnswerRepository answerRepository;

    @Autowired
    SessionParticipantRepository sessionParticipantRepository;

    @Autowired 
    CourseRepository courseRepository;
    
    @Autowired
    SessionRepository sessionRepository;
    
    @Autowired
    UserRepository userRepository; 
    
    private static final String MSG_TESTING_TIME_PARTICIPANT_LAGER_TESTING_TIME_QUIZ = "Number testing time of Participant is lager than Quiz's testing time";

    @Override
    public ResponseEntity<?> quizResults(QuizResult quizResult) throws Exception{
        try {
            int quizId = quizResult.getQuizId();
            int courseId = quizResult.getCourseId();
            int sessionId = quizResult.getSessionId();
            int userId = quizResult.getUserId();
            
            SessionParticipant sessionParticipant = sessionParticipantRepository.getSessionParticipant(sessionId, userId);
            if(sessionParticipant == null)
                throw new ObjectNotFoundException("Cannot find SessionParticipant");
            
            Course course = courseRepository.getCourseById(courseId);
            if(course == null) course = new Course();

            Quiz quiz = getFullQuiz(quizResult.getQuizId());

            if(outOfMaxTestingTime(quiz, sessionParticipant))
                throw new QuizNotAvailableException(MSG_TESTING_TIME_PARTICIPANT_LAGER_TESTING_TIME_QUIZ);

            List<Question> questionsResults = arraygementQuizResult(quiz.getQuestions(), quizResult.getResults());
            int totalQuestionCorrect = calculateQuizResult(quiz.getQuestions(), questionsResults);
            
            if(sessionParticipant.getQuizCorrectAnswers() < totalQuestionCorrect)
                sessionParticipant.setQuizCorrectAnswers(totalQuestionCorrect);
            
            if(sessionParticipant.getQuizResultStatus() != QuizResultStatus.PASSED.getValue()){
                if(quiz.getAmountQuestionPassed() <= totalQuestionCorrect){
                    sessionParticipant.setQuizResultStatus(QuizResultStatus.PASSED.getValue());
                    User user = userRepository.getUserById(userId);
                    if(user != null){
                        user.setAjpv(user.getAjpv() + course.getPassQuizAJPV());
                        userRepository.editUser(user);
                    }
                    
                }else {
                    sessionParticipant.setQuizResultStatus(QuizResultStatus.FAILED.getValue());
                }
            }

            sessionParticipant.setQuizStatus(QuizStatus.TESTED.getValue());
            sessionParticipant.setNumberOfTesting(sessionParticipant.getNumberOfTesting() + 1);

            sessionParticipantRepository.updateSessionParticipant(sessionParticipant);
            
            return Utils.generateSuccessResponseEntity(sessionParticipant);
        } catch (ObjectNotFoundException e) {
            e.printStackTrace();
            return Utils.generateFailureResponseEntity(ErrorCode.CODE_OBJECT_NOT_FOUND_EXCEPTION, ErrorCode.MSG_OBJECT_NOT_FOUND_EXCEPTION);
        } catch (Exception e) {
            e.printStackTrace();
            return Utils.generateFailureResponseEntity(ErrorCode.CODE_ADD_OBJECT_EXCEPTION, ErrorCode.MSG_ADD_OBJECT_EXCEPTION);
        }
    }

    private Quiz getFullQuiz(int quizId) throws ObjectNotFoundException {
        Quiz quiz = quizRepository.getQuizById(quizId);
        if (quiz == null)
            throw new ObjectNotFoundException("Quiz not found with id = " + quizId);

        quiz.setQuestions(getQuestionsForQuiz(quizId));
        return quiz;
    }

    private List<Question> getQuestionsForQuiz(int quizId) {
        List<Question> questions = questionRepository.getQuestionListByQuizId(quizId);
        for (Question question : questions) {
            question.setAnswers(answerRepository.getAnswerListByQuestionIdAndIsCorrect(question.getId()));
        }
        return questions;
    }

    private List<Question> arraygementQuizResult(List<Question> questions, List<Result> results) {
        List<Question> questionsResults = new ArrayList<>();
        for (Question question : questions) {
            int questionId = question.getId();
            Question questionResult = new Question();
            questionResult.setId(questionId);
            List<Answer> answerResults = new ArrayList<>();
            for (Result result : results) {
                if (questionId == result.getQuestionId()) {
                    Answer answer = new Answer();
                    answer.setId(result.getAnswerId());
                    answerResults.add(answer);
                }
            }
            questionResult.setAnswers(answerResults);
            questionsResults.add(questionResult);
        }
        return questionsResults;
    }

    private int calculateQuizResult(List<Question> questionEntitys, List<Question> questionResults) {
        int totalQuestionCorrect = 0;
        for (int i = 0; i < questionEntitys.size(); i++) {
            Question questionEntity = questionEntitys.get(i);
            Question questionResult = questionResults.get(i);
            int typeId = questionEntity.getQuestionTypeId();
            
            if(typeId == QuestionTypes.RADIO.getValue()) totalQuestionCorrect += calculateResultRadioQuestion(questionEntity, questionResult);
            else if (typeId == QuestionTypes.CHECKBOX.getValue()) totalQuestionCorrect += calculateResultCheckBoxQuestion(questionEntity, questionResult);
        }
        return totalQuestionCorrect;
    }

    private int calculateResultCheckBoxQuestion(Question questionEntity, Question questionResult) {
        if (questionEntity.getAnswers().size() != questionResult.getAnswers().size()) return 0;
        
        for(Answer answerEntity : questionEntity.getAnswers()){
            boolean isCorrect = false;
            for(Answer answerResult : questionResult.getAnswers()){
                if(answerEntity.getId() == answerResult.getId()) isCorrect = true;
            }
            if(!isCorrect) return 0;
        }
        return 1;
    }

    private int calculateResultRadioQuestion(Question questionEntity, Question questionResult) {
        if (questionEntity.getAnswers().size() != questionResult.getAnswers().size()) return 0;
        for(Answer answerEntity : questionEntity.getAnswers()){
            boolean isCorrect = false;
            for(Answer answerResult : questionResult.getAnswers()){
                if(answerEntity.getId() == answerResult.getId()) isCorrect = true;
            }
            if(!isCorrect) return 0;
        }
        return 1;
    }
    
    private boolean outOfMaxTestingTime(Quiz quiz, SessionParticipant participant) {
        return (participant.getNumberOfTesting() >= quiz.getMaxTestTimes());
    }
    

    @Override
    public ResponseEntity<?> getQuizResults(int quizId) throws Exception {
        try {
            Quiz quiz = quizRepository.getQuizById(quizId);
            if(quiz == null)
                throw new ObjectNotFoundException("Quiz cannot be found with id = "+quizId);
            
            Course course = courseRepository.getCourseById(quiz.getCourseId());
            if(course == null)
                throw new ObjectNotFoundException("Course cannot be found with quizId = "+quizId);
            
            List<Session> sessions = sessionRepository.getSessionsByCourseId(course.getId());
            
            List<SessionParticipant> sessionParticipants = new ArrayList<>();
            for(Session session : sessions){
                sessionParticipants.addAll(sessionParticipantRepository.getSessionParticipantBySessionId(session.getId()));
            }
            
            List<com.amway.lms.backend.model.QuizResult> quizResultModels = new ArrayList<>();
            for(SessionParticipant sessionParticipant : sessionParticipants){
                com.amway.lms.backend.model.QuizResult quizResultModel = convertToQuizResultModel(sessionParticipant, quiz);
                if(quizResultModel == null) continue;
                else quizResultModels.add(quizResultModel);
            }
            return Utils.generateSuccessResponseEntity(quizResultModels);
        } catch (Exception e) {
            e.printStackTrace();
            return Utils.generateFailureResponseEntity(ErrorCode.CODE_OBJECT_NOT_FOUND_EXCEPTION, ErrorCode.MSG_OBJECT_NOT_FOUND_EXCEPTION);
        }
    }
    
    private com.amway.lms.backend.model.QuizResult convertToQuizResultModel(SessionParticipant sessionParticipant, Quiz quiz){
        try {
            User user = userRepository.getUserById(sessionParticipant.getUserId());
            if(user == null)
                throw new ObjectNotFoundException("Cannot found User by SessionParticipant");

            com.amway.lms.backend.model.QuizResult quizResultModel = new com.amway.lms.backend.model.QuizResult();
            quizResultModel.setUserName(new StringBuffer().append(user.getFirstName()).append(" ").append(user.getLastName()).toString());
            quizResultModel.setCorrectAnswer(String.valueOf(sessionParticipant.getQuizCorrectAnswers()));
            quizResultModel.setNumberOfTesting(String.valueOf(sessionParticipant.getNumberOfTesting()));
            quizResultModel.setQuizResult(convertQuizResultStatus(sessionParticipant.getQuizResultStatus()));
            quizResultModel.setQuizStatus(String.format(convertQuizStatus(sessionParticipant.getQuizStatus()), sessionParticipant.getNumberOfTesting()));
            quizResultModel.setIsMaxTest(sessionParticipant.getNumberOfTesting() < quiz.getMaxTestTimes() ? 0 : 1);
            return quizResultModel;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    
    private final String convertQuizStatus(Integer value) {
        if (value == null) {
            return null;
        }
        if (value.equals(QuizStatus.NOT_TAKEN_YET.getValue())) {
            return QuizStatus.NOT_TAKEN_YET.getStrValue();
        } else if (value.equals(QuizStatus.AVAILABLE.getValue())) {
            return QuizStatus.AVAILABLE.getStrValue();
        }
        return QuizStatus.TESTED.getStrValue();

    }

    private final String convertQuizResultStatus(Integer value) {
        if (value == null) {
            return null;
        }
        if (value.equals(QuizResultStatus.NOT_TAKEN_YET.getValue())) {
            return QuizStatus.NOT_TAKEN_YET.getStrValue();
        } else if (value.equals(QuizResultStatus.FAILED.getValue())) {
            return QuizResultStatus.FAILED.getStrValue();
        }
        return QuizResultStatus.PASSED.getStrValue();
    }

}
