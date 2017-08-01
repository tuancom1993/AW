/**
 * 
 */
package com.amway.lms.backend.service.impl;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import javax.sql.DataSource;
import javax.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.codec.Base64;
import org.springframework.security.web.authentication.rememberme.InvalidCookieException;
import org.springframework.security.web.authentication.rememberme.JdbcTokenRepositoryImpl;
import org.springframework.security.web.authentication.rememberme.PersistentRememberMeToken;
import org.springframework.security.web.authentication.rememberme.RememberMeAuthenticationException;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.amway.lms.backend.common.AmwayEnum;
import com.amway.lms.backend.common.AmwayEnum.QuestionTypes;
import com.amway.lms.backend.common.Common;
import com.amway.lms.backend.common.ErrorCode;
import com.amway.lms.backend.common.Utils;
import com.amway.lms.backend.entity.Answer;
import com.amway.lms.backend.entity.Course;
import com.amway.lms.backend.entity.Question;
import com.amway.lms.backend.entity.Quiz;
import com.amway.lms.backend.entity.QuizQuestion;
import com.amway.lms.backend.entity.Session;
import com.amway.lms.backend.entity.SessionParticipant;
import com.amway.lms.backend.entity.User;
import com.amway.lms.backend.exception.AddObjectException;
import com.amway.lms.backend.exception.DeleteObjectException;
import com.amway.lms.backend.exception.EditObjectException;
import com.amway.lms.backend.exception.ObjectNotFoundException;
import com.amway.lms.backend.exception.QuestionExistException;
import com.amway.lms.backend.exception.QuizNotAvailableException;
import com.amway.lms.backend.model.ChoiceModel;
import com.amway.lms.backend.model.PageModel;
import com.amway.lms.backend.model.QuestionModel;
import com.amway.lms.backend.model.QuizModel;
import com.amway.lms.backend.repository.AnswerRepository;
import com.amway.lms.backend.repository.CourseRepository;
import com.amway.lms.backend.repository.QuestionRepository;
import com.amway.lms.backend.repository.QuizQuestionRepository;
import com.amway.lms.backend.repository.QuizRepository;
import com.amway.lms.backend.repository.SessionParticipantRepository;
import com.amway.lms.backend.repository.SessionRepository;
import com.amway.lms.backend.repository.UserRepository;
import com.amway.lms.backend.service.QuizService;

/**
 * @author acton
 * @email acton@enclave.vn
 */
@Service
@Transactional
public class QuizServiceImpl implements QuizService {

    private static final Logger logger = LoggerFactory.getLogger(QuizServiceImpl.class);

    @Value("${number.questions.per.page}")
    private int MAX_NUMBER_QUESTION_PER_PAGE;

    private static final String DELIMITER = ":";

    @Autowired
    QuizRepository quizRepository;

    @Autowired
    QuestionRepository questionRepository;

    @Autowired
    CourseRepository courseRepository;

    @Autowired
    QuizQuestionRepository quizQuestionRepository;

    @Autowired
    AnswerRepository answerRepository;

    @Autowired
    SessionParticipantRepository sessionParticipantRepository;

    @Autowired
    SessionRepository sessionRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    DataSource dataSource;

    private static final String MSG_COURSE_HAS_NOT_QUIZ = "Course hasn't quiz";
    private static final String MSG_QUIZ_HAS_NOT_STARTED_YET = "Quiz has not stated yet";
    private static final String MSG_QUIZ_OUT_OF_EXPRIED_DATE = "Quiz is out of Expried Date";
    private static final String MSG_TESTING_TIME_PARTICIPANT_LAGER_TESTING_TIME_QUIZ = "Number testing time of Participant is lager than Quiz's testing time";
    private static final String MSG_PARTICIPANT_IS_NOT_CORRECT = "The user is not allow in this quiz";
    private static final String MSG_PARTICIPANT_STATUS_NOT_END_OR_COMPLETED = "The user not end session or complet session";

    @Override
    public ResponseEntity<?> startQuiz(int sessionId, int userId) throws QuizNotAvailableException, Exception {
        try {
            Session session = sessionRepository.getSessionById(sessionId);
            if (session == null)
                throw new ObjectNotFoundException("Cannot find Session by sessionId = " + sessionId);

            Course course = courseRepository.getCourseById(session.getCourseId());
            if (course == null)
                throw new ObjectNotFoundException("Cannot find Course by sessionId = " + sessionId);

            SessionParticipant sessionParticipant = sessionParticipantRepository.getSessionParticipant(sessionId,
                    userId);
            if (sessionParticipant == null)
                throw new ObjectNotFoundException("Cannot find Session Participant");

            Quiz quiz = quizRepository.getQuizByCourseId(course.getId());
            if (quiz == null)
                throw new QuizNotAvailableException(MSG_COURSE_HAS_NOT_QUIZ);

            if (notStartYet(quiz, session))
                throw new QuizNotAvailableException(MSG_QUIZ_HAS_NOT_STARTED_YET);

            if (outOfExpDate(quiz, session)) {
                logger.error("Quiz is out of Expried Date with quizId = " + quiz.getId() + " sessionId = " + sessionId);
                throw new QuizNotAvailableException(MSG_QUIZ_OUT_OF_EXPRIED_DATE);
            }

            if (outOfMaxTestingTime(quiz, sessionParticipant))
                throw new QuizNotAvailableException(MSG_TESTING_TIME_PARTICIPANT_LAGER_TESTING_TIME_QUIZ);

            quiz.setQuestions(getListQuestion(quiz));

            return Utils.generateSuccessResponseEntity(
                    converDataFromQuizEntityToQuizModel(quiz, course.getId(), sessionId, userId));

        } catch (QuizNotAvailableException e) {
            e.printStackTrace();
            return Utils.generateFailureResponseEntity(ErrorCode.CODE_QUIZ_NOT_AVAILABLE_EXCEPTION, e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            return Utils.generateFailureResponseEntity(ErrorCode.CODE_QUIZ_NOT_AVAILABLE_EXCEPTION,
                    ErrorCode.MSG_QUIZ_NOT_AVAILABLE_EXCEPTION);
        }

    }

    @Override
    public ResponseEntity<?> startQuiz(int sessionParticipantId, String cookieValue)
            throws QuizNotAvailableException, Exception {
        try {
            SessionParticipant sessionParticipant = sessionParticipantRepository
                    .getSessionParticipantById(sessionParticipantId);
            if (sessionParticipant == null)
                throw new ObjectNotFoundException("Cannot find Session Participant");

            if (!validateCurrentUser(sessionParticipant, cookieValue))
                throw new QuizNotAvailableException(MSG_PARTICIPANT_IS_NOT_CORRECT);

            Session session = sessionRepository.getSessionById(sessionParticipant.getSessionId());
            if (session == null)
                throw new ObjectNotFoundException(
                        "Cannot find Session by sessionId = " + sessionParticipant.getSessionId());

            Course course = courseRepository.getCourseById(session.getCourseId());
            if (course == null)
                throw new ObjectNotFoundException("Cannot find Course by sessionId = " + session.getId());

            Quiz quiz = quizRepository.getQuizByCourseId(course.getId());
            if (quiz == null)
                throw new QuizNotAvailableException(MSG_COURSE_HAS_NOT_QUIZ);

            // if(notStartYet(quiz, session))
            // throw new
            // QuizNotAvailableException(MSG_QUIZ_HAS_NOT_STARTED_YET);

            if (!validateStatus(sessionParticipant))
                throw new QuizNotAvailableException(MSG_PARTICIPANT_STATUS_NOT_END_OR_COMPLETED);

            if (outOfExpDate(quiz, session))
                throw new QuizNotAvailableException(MSG_QUIZ_OUT_OF_EXPRIED_DATE);

            if (outOfMaxTestingTime(quiz, sessionParticipant))
                throw new QuizNotAvailableException(MSG_TESTING_TIME_PARTICIPANT_LAGER_TESTING_TIME_QUIZ);

            quiz.setQuestions(reOderQuestion(getListQuestion(quiz)));

            return Utils.generateSuccessResponseEntity(converDataFromQuizEntityToQuizModel(quiz, course.getId(),
                    session.getId(), sessionParticipant.getUserId()));

        } catch (QuizNotAvailableException e) {
            e.printStackTrace();
            return Utils.generateFailureResponseEntity(ErrorCode.CODE_QUIZ_NOT_AVAILABLE_EXCEPTION, e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            return Utils.generateFailureResponseEntity(ErrorCode.CODE_QUIZ_NOT_AVAILABLE_EXCEPTION,
                    ErrorCode.MSG_QUIZ_NOT_AVAILABLE_EXCEPTION);
        }
    }

    private List<Question> getListQuestion(Quiz quiz) {
        List<Question> questions = questionRepository.getQuestionListByQuizId(quiz.getId());
        for (Question question : questions) {
            List<Answer> answers = answerRepository.getAnswerListByQuestionId(question.getId());
            question.setAnswers(answers);
        }
        return questions;
    }

    private QuizModel converDataFromQuizEntityToQuizModel(Quiz quiz, int courseId, int sessionId, int userId) {
        QuizModel quizModel = new QuizModel();
        quizModel.setName(String.valueOf(quiz.getId()));
        quizModel.setTitle(quiz.getTitle());
        quizModel.setQuestionTitleTemplate("{no}. {title}");
        quizModel.setCourseId(courseId);
        quizModel.setSessionId(sessionId);
        quizModel.setUserId(userId);
        quizModel.setQuizId(quiz.getId());

        List<PageModel> pageModels = new ArrayList<>();

        /*
         * PageModel page1 = new PageModel(); page1.setName("page1");
         * page1.setQuestions(setQuestionModelForPage1(quiz));
         * pageModels.add(page1);
         */

        int pageNumber = 1;
        int partInt = quiz.getQuestions().size() / MAX_NUMBER_QUESTION_PER_PAGE;
        int partSur = quiz.getQuestions().size() % MAX_NUMBER_QUESTION_PER_PAGE;
        for (int i = 0; i < partInt; i++) {
            PageModel page = new PageModel();
            page.setName("page" + pageNumber);
            pageModels.add(page);
            List<Question> questionsEntityInPage = quiz.getQuestions().subList(i * MAX_NUMBER_QUESTION_PER_PAGE,
                    (i + 1) * MAX_NUMBER_QUESTION_PER_PAGE);
            List<QuestionModel> questionModels = new ArrayList<>();
            for (Question question : questionsEntityInPage) {
                QuestionModel questionModel = convertDataFromQuestionEntityToQuestionModel(question);
                questionModels.add(questionModel);
            }
            page.setQuestions(questionModels);
            pageNumber++;
        }
        if (partSur > 0) {
            PageModel page = new PageModel();
            page.setName("page" + pageNumber);
            pageModels.add(page);
            List<Question> questionsEntityInPage = quiz.getQuestions().subList(MAX_NUMBER_QUESTION_PER_PAGE * partInt,
                    quiz.getQuestions().size());
            List<QuestionModel> questionModels = new ArrayList<>();
            for (Question question : questionsEntityInPage) {
                QuestionModel questionModel = convertDataFromQuestionEntityToQuestionModel(question);
                questionModels.add(questionModel);
            }
            page.setQuestions(questionModels);
        }
        quizModel.setPages(pageModels);
        return quizModel;
    }

    private QuestionModel convertDataFromQuestionEntityToQuestionModel(Question questionEntity) {
        QuestionModel questionModel = new QuestionModel();
        questionModel.setName(String.valueOf(questionEntity.getId()));
        questionModel.setIsRequired(questionEntity.getIsRequired() == 0 ? false : true);
        questionModel.setTitle(questionEntity.getQuestionDesc());
        questionModel.setFileLocation(questionEntity.getFileLocation());
        questionModel.setFileType(getFileType(questionEntity.getFileLocation()));

        if (questionEntity.getQuestionTypeId() == QuestionTypes.RADIO.getValue()) {
            questionModel.setType(QuestionTypes.RADIO.getStrValue());
            questionModel.setChoices(convertDataFromAnswerEntityToChoiceModel(questionEntity));
        } else if (questionEntity.getQuestionTypeId() == QuestionTypes.CHECKBOX.getValue()) {
            questionModel.setType(QuestionTypes.CHECKBOX.getStrValue());
            questionModel.setChoices(convertDataFromAnswerEntityToChoiceModel(questionEntity));
        }

        return questionModel;
    }

    private String getFileType(String fileLocation) {
        if (fileLocation == null)
            return null;

        String fileType = null;
        String extension = fileLocation.substring(fileLocation.lastIndexOf((int) (char) '.') + 1,
                fileLocation.length());
        if (Common.isContain(extension, Common.ARR_IMAGES_EXTENSION)) {
            fileType = "image";
        } else if (Common.isContain(extension, Common.ARR_AUDIOS_EXTENSION)) {
            fileType = "audio";
        } else if (Common.isContain(extension, Common.ARR_VIDEOS_EXTENSION)) {
            fileType = "video";
        }

        return fileType;
    }

    private List<ChoiceModel> convertDataFromAnswerEntityToChoiceModel(Question questionEntity) {
        List<ChoiceModel> choiceModels = new ArrayList<>();
        for (Answer answer : questionEntity.getAnswers()) {
            ChoiceModel choiceModel = new ChoiceModel();
            choiceModel.setValue(String.valueOf(answer.getId()));
            choiceModel.setText(answer.getAnswerDesc());
            choiceModels.add(choiceModel);
        }
        return choiceModels;
    }

    private List<QuestionModel> setQuestionModelForPage1(Quiz quiEntity) {
        List<QuestionModel> questionModels = new ArrayList<>();
        QuestionModel questionModel = new QuestionModel("info", "html", "");
        questionModels.add(questionModel);
        return questionModels;
    }

    public boolean outOfExpDate(Quiz quiz) {
        return true;
    }

    public boolean outOfExpDate(Quiz quiz, Session session) {
        Timestamp toDate = new Timestamp(System.currentTimeMillis());
        Timestamp expDate = new Timestamp(session.getEndTime().getTime() + quiz.getExpireDay() * 24 * 60 * 60 * 1000L);
        return toDate.after(expDate);
    }

    public boolean notStartYet(Quiz quiz, Session session) {
        Timestamp toDate = new Timestamp(System.currentTimeMillis());
        return toDate.before(session.getStartTime());
    }

    public boolean outOfMaxTestingTime(Quiz quiz, SessionParticipant participant) {
        return (participant.getNumberOfTesting() >= quiz.getMaxTestTimes());
    }

    private boolean validateStatus(SessionParticipant participant) {
        if (participant.getCompletionStatus() == AmwayEnum.CompletionStatus.ENDED_SESSION.getValue()
                || participant.getCompletionStatus() == AmwayEnum.CompletionStatus.COMPLETED.getValue())
            return true;
        else
            return false;
    }

    private List<Question> reOderQuestion(List<Question> questions) {
        Collections.shuffle(questions);
        return questions;
    }

    @Override
    public ResponseEntity<?> getQuizList() throws Exception {
        logger.debug("getQuizList()");
        return Utils.generateSuccessResponseEntity(quizRepository.getQuizList());
    }

    @Override
    public ResponseEntity<?> getQuizInfor(int quizId) throws ObjectNotFoundException, Exception {
        Quiz quiz = quizRepository.getQuizById(quizId);
        if (quiz == null)
            throw new ObjectNotFoundException();
        return Utils.generateSuccessResponseEntity(quiz);
    }

    @Override
    public ResponseEntity<?> addQuiz(Quiz quiz) throws AddObjectException, Exception {
        try {
            logger.debug("addQuiz");
            if (courseRepository.getCourseById(quiz.getCourseId()) == null) {
                throw new AddObjectException("Cannot add new Quiz cause Course is not exist");
            }
            if (quizRepository.getQuizByCourseId(quiz.getCourseId()) != null) {
                throw new AddObjectException("Cannot add new Quiz cause Quiz with CourseId is exist in table");
            }

            quizRepository.addQuiz(quiz);
            return Utils.generateSuccessResponseEntity(quiz);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("Exception - Cannot add new Quiz cause: " + e.getMessage());
            return Utils.generateFailureResponseEntity(ErrorCode.CODE_ADD_OBJECT_EXCEPTION,
                    ErrorCode.MSG_ADD_OBJECT_EXCEPTION);
        }
    }

    @Override
    public ResponseEntity<?> editQuiz(Quiz quiz) throws EditObjectException, Exception {
        try {
            Quiz quizEntity = quizRepository.getQuizById(quiz.getId());
            if (quizEntity == null)
                throw new EditObjectException("Cannot edit Quiz. Cause not found Quiz with id = " + quiz.getId());

            quizEntity.setAmountQuestionPassed(quiz.getAmountQuestionPassed());
            quizEntity.setExpireDay(quiz.getExpireDay());
            quizEntity.setMaxTestTimes(quiz.getMaxTestTimes());
            quizEntity.setTitle(quiz.getTitle());
            quizEntity.setUserId(quiz.getUserId());

            quizRepository.editQuiz(quizEntity);

            return Utils.generateSuccessResponseEntity(quizEntity);
        } catch (Exception e) {
            e.printStackTrace();
            return Utils.generateFailureResponseEntity(ErrorCode.CODE_EDIT_OBJECT_EXCEPTION,
                    ErrorCode.MSG_EDIT_OBJECT_EXCEPTION);
        }
    }

    @Override
    public ResponseEntity<?> deleteQuiz(int quizId) throws DeleteObjectException, Exception {
        try {
            Quiz quiz = quizRepository.getQuizById(quizId);
            if (quiz == null)
                throw new DeleteObjectException("Cannot Delete Quiz. Cause quiz is not exist with id = " + quizId);

            quizRepository.deleteQuiz(quiz);
            quizQuestionRepository.deleteQuizQuestionByQuizId(quizId);
            return Utils.generateSuccessResponseEntity(quiz);
        } catch (Exception e) {
            e.printStackTrace();
            return Utils.generateFailureResponseEntity(ErrorCode.CODE_DELETE_OBJECT_EXCEPTION,
                    ErrorCode.MSG_DELETE_OBJECT_EXCEPTION);
        }
    }

    @Override
    public ResponseEntity<?> searchQuizByName(String searchString) throws Exception {

        return Utils.generateSuccessResponseEntity(quizRepository.searchQuizByName(searchString));
    }

    @Override
    public ResponseEntity<?> addQuizFull(Quiz quiz) throws AddObjectException, Exception {
        try {
            logger.debug("addQuiz");
            if (courseRepository.getCourseById(quiz.getCourseId()) == null) {
                throw new AddObjectException("Cannot add new Quiz cause Course is not exist");
            }
            if (quizRepository.getQuizByCourseId(quiz.getCourseId()) != null) {
                throw new AddObjectException("Cannot add new Quiz cause Quiz with CourseId is exist in table");
            }

            quiz.setNumberQuestions(quiz.getQuestions().size());
            quizRepository.addQuiz(quiz);
            addQuestionListForQuiz(quiz);

            return Utils.generateSuccessResponseEntity(quiz);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("Exception - Cannot add new Quiz cause: " + e.getMessage());
            return Utils.generateFailureResponseEntity(ErrorCode.CODE_ADD_OBJECT_EXCEPTION,
                    ErrorCode.MSG_ADD_OBJECT_EXCEPTION);
        }
    }

    private void addQuestionListForQuiz(Quiz quiz) throws AddObjectException, Exception {
        List<Question> questions = quiz.getQuestions();
        if (questions == null || questions.isEmpty())
            throw new AddObjectException("Question list is empty");

        int indexQuestion = 1;
        for (Question question : questions) {
            if (question.getId() == 0)
                addQuizQuestionWithNewQuestion(quiz, question, indexQuestion);
            else
                addQuizQuestionWithOldQuestion(quiz, question, indexQuestion);
            indexQuestion++;
        }
    }

    private void addQuizQuestionWithNewQuestion(Quiz quiz, Question question, int indexQuestion)
            throws AddObjectException, Exception {
        logger.debug("addQuizQuestionWithNewQuestion");

        question.setIsQuiz(1);
        question.setIsRequired(0);
        questionRepository.addQuestion(question);
        int questionId = question.getId();

        if (question.getQuestionTypeId() == QuestionTypes.RADIO.getValue()
                || question.getQuestionTypeId() == QuestionTypes.CHECKBOX.getValue()) {
            List<Answer> answers = question.getAnswers();
            if (answers == null || answers.isEmpty()) {
                throw new AddObjectException("Cannot add Question. Cause don't have any Answer in this Question");
            } else {
                if (question.getQuestionTypeId() == QuestionTypes.RADIO.getValue() && countCorrectAnswer(answers) > 1) {
                    throw new AddObjectException(
                            "Cannot add Question. Cause Question Radio have Answer Correct lager 1");
                }
                for (Answer answer : answers) {
                    answer.setId(0);
                    answer.setQuestionId(questionId);
                    if (answer.getCorrect() == null)
                        answer.setCorrect(0);
                    answerRepository.addAnswer(answer);
                }
            }
        } else {
            throw new AddObjectException("Cannot add Question. Cause type of Question is not correct.");
        }

        if (question.getQuestionTypeId() == QuestionTypes.RADIO.getValue())
            question.setQuestionType(QuestionTypes.RADIO.getStrValue());
        if (question.getQuestionTypeId() == QuestionTypes.CHECKBOX.getValue())
            question.setQuestionType(QuestionTypes.CHECKBOX.getStrValue());

        QuizQuestion quizQuestion = new QuizQuestion();
        quizQuestion.setQuizId(quiz.getId());
        quizQuestion.setQuestionId(questionId);
        quizQuestion.setIndexQuestion(indexQuestion);
        quizQuestionRepository.addQuizQuestion(quizQuestion);
        quizQuestion.setQuestion(question);
    }

    private void addQuizQuestionWithOldQuestion(Quiz quiz, Question question, int indexQuestion)
            throws AddObjectException, Exception {
        logger.debug("addQuizQuestionWithOldQuestion " + question.getId());

        Question questionCheck = questionRepository.getQuestionById(question.getId());
        if (questionCheck == null)
            throw new AddObjectException("QuizQuestion cannot add, cause Question cannot found");

        QuizQuestion quizQuestionCheck = quizQuestionRepository.getQuizQuestionByQuizIdAndQuestionId(quiz.getId(),
                question.getId());
        if (quizQuestionCheck != null)
            throw new QuestionExistException("Question is exist in quiz");

        QuizQuestion quizQuestion = new QuizQuestion();
        quizQuestion.setQuestionId(question.getId());
        quizQuestion.setQuizId(quiz.getId());
        quizQuestion.setIndexQuestion(indexQuestion);
        quizQuestionRepository.addQuizQuestion(quizQuestion);
        quizQuestion.setQuestion(questionCheck);

        if (question.getQuestionTypeId() == QuestionTypes.RADIO.getValue())
            question.setQuestionType(QuestionTypes.RADIO.getStrValue());
        if (question.getQuestionTypeId() == QuestionTypes.CHECKBOX.getValue())
            question.setQuestionType(QuestionTypes.CHECKBOX.getStrValue());
    }

    private int countCorrectAnswer(List<Answer> answers) {
        int total = 0;
        for (Answer answer : answers) {
            if (answer.getCorrect() != null && answer.getCorrect() == 1)
                total++;
        }
        return total;
    }

    @Override
    public ResponseEntity<?> editQuizFull(Quiz quiz) throws EditObjectException, Exception {
        // TODO Auto-generated method stub
        try {
            logger.debug("editQuizFull");
            int quizId = quiz.getId();
            Quiz quizDB = quizRepository.getQuizById(quizId);

            if (quizDB == null)
                throw new EditObjectException("Cannot edit full Quiz cause Cannot find Quiz by id = " + quizId);

            if (courseRepository.getCourseById(quizDB.getCourseId()) == null) {
                throw new AddObjectException("Cannot edit full Quiz cause Course is not exist");
            }

            quizDB.setNumberQuestions(quiz.getQuestions().size());
            quizDB.setAmountQuestionPassed(quiz.getAmountQuestionPassed());
            quizDB.setExpireDay(quiz.getExpireDay());
            quizDB.setMaxTestTimes(quiz.getMaxTestTimes());
            quizDB.setTitle(quiz.getTitle());

            quizRepository.editQuiz(quizDB);

            deleteQuizQuestion(quizId);

            addQuestionListForQuiz(quiz);

            return Utils.generateSuccessResponseEntity(quiz);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("Exception - Cannot edit full Quiz cause: " + e.getMessage());
            return Utils.generateFailureResponseEntity(ErrorCode.CODE_EDIT_OBJECT_EXCEPTION,
                    ErrorCode.MSG_EDIT_OBJECT_EXCEPTION);
        }
    }
    
    public void deleteQuizQuestion(int quizId) throws DeleteObjectException, Exception{
        quizQuestionRepository.deleteQuizQuestionByQuizId(quizId);
    }

    private boolean validateCurrentUser(SessionParticipant sessionParticipant, String cookieValue) {
        try {
            System.err.println("CookieValue: " + cookieValue);
            User user = userRepository.getUserById(sessionParticipant.getUserId());
            if (user == null)
                return false;

            String[] tokens = decodeCookie(cookieValue);
            String userID = getUserNameByArrayCookieTokens(tokens);
            logger.debug("User login: " + userID);

            if (!user.getUserID().toLowerCase().equals(userID.toLowerCase())) {
                logger.error("User login: " + userID + " is not user in sessionParticipant: " + user.getUserID());
                return false;
            }

            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    private String[] decodeCookie(String cookieValue) throws InvalidCookieException {
        for (int j = 0; j < cookieValue.length() % 4; j++) {
            cookieValue = cookieValue + "=";
        }

        if (!Base64.isBase64(cookieValue.getBytes())) {
            throw new InvalidCookieException("Cookie token was not Base64 encoded; value was '" + cookieValue + "'");
        }

        String cookieAsPlainText = new String(Base64.decode(cookieValue.getBytes()));

        String[] tokens = StringUtils.delimitedListToStringArray(cookieAsPlainText, DELIMITER);

        if ((tokens[0].equalsIgnoreCase("http") || tokens[0].equalsIgnoreCase("https")) && tokens[1].startsWith("//")) {
            // Assume we've accidentally split a URL (OpenID identifier)
            String[] newTokens = new String[tokens.length - 1];
            newTokens[0] = tokens[0] + ":" + tokens[1];
            System.arraycopy(tokens, 2, newTokens, 1, newTokens.length - 1);
            tokens = newTokens;
        }
        return tokens;
    }

    protected String getUserNameByArrayCookieTokens(String[] cookieTokens) {

        if (cookieTokens.length != 2) {
            throw new InvalidCookieException("Cookie token did not contain " + 2 + " tokens, but contained '"
                    + Arrays.asList(cookieTokens) + "'");
        }

        final String presentedSeries = cookieTokens[0];

        JdbcTokenRepositoryImpl tokenRepository = new JdbcTokenRepositoryImpl();
        tokenRepository.setDataSource(dataSource);

        PersistentRememberMeToken token = tokenRepository.getTokenForSeries(presentedSeries);

        if (token == null) {
            // No series match, so we can't authenticate using this cookie
            throw new RememberMeAuthenticationException("No persistent token found for series id: " + presentedSeries);
        }

        return token.getUsername();
    }
}
