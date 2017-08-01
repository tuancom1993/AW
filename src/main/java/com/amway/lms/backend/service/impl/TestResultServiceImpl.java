package com.amway.lms.backend.service.impl;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.amway.lms.backend.common.AmwayEnum.QuestionTypes;
import com.amway.lms.backend.common.AmwayEnum.TestResultStatus;
import com.amway.lms.backend.common.AmwayEnum.TestStatus;
import com.amway.lms.backend.common.Common;
import com.amway.lms.backend.common.ErrorCode;
import com.amway.lms.backend.common.Message;
import com.amway.lms.backend.common.Utils;
import com.amway.lms.backend.entity.Answer;
import com.amway.lms.backend.entity.Question;
import com.amway.lms.backend.entity.Result;
import com.amway.lms.backend.entity.Test;
import com.amway.lms.backend.entity.TestPart;
import com.amway.lms.backend.entity.TestParticipant;
import com.amway.lms.backend.entity.User;
import com.amway.lms.backend.exception.ObjectNotFoundException;
import com.amway.lms.backend.exception.TestNotAvailableException;
import com.amway.lms.backend.model.TestPartResultView;
import com.amway.lms.backend.model.TestParticipantResultView;
import com.amway.lms.backend.model.TestResultModel;
import com.amway.lms.backend.model.TestResultView;
import com.amway.lms.backend.repository.AnswerRepository;
import com.amway.lms.backend.repository.QuestionRepository;
import com.amway.lms.backend.repository.TestPartRepository;
import com.amway.lms.backend.repository.TestParticipantRepository;
import com.amway.lms.backend.repository.TestQuestionRepository;
import com.amway.lms.backend.repository.TestRepository;
import com.amway.lms.backend.repository.UserRepository;
import com.amway.lms.backend.service.TestResultService;

@Service
@Transactional
public class TestResultServiceImpl implements TestResultService {
    private static final Logger logger = LoggerFactory
            .getLogger(TestResultServiceImpl.class);

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

    @Autowired
    UserRepository userRepository;

    @Autowired
    TestParticipantRepository testParticipantRepository;

    @Override
    public ResponseEntity<?> testResult(TestResultModel testResultModel) {
        try {
            int testId = testResultModel.getTestId();
            int userId = testResultModel.getUserId();

            Test test = testRepository.getTestById(testId);
            if (test == null)
                throw new TestNotAvailableException(
                        Message.MSG_TEST_NOT_AVAILABE);

            if (outOfExpDate(test))
                throw new TestNotAvailableException(
                        Message.MSG_TEST_OUT_OF_EXPIRED_DATE);

            TestParticipant testParticipant = testParticipantRepository
                    .getTestParticipantByTestIdAndUserId(testId, userId);

            if (testParticipant == null)
                throw new TestNotAvailableException(
                        Message.MSG_USER_LOGIN_NOT_CORRECT);

            if (outOfMaxTestingTime(test, testParticipant))
                throw new TestNotAvailableException(
                        Message.MSG_TESTING_TIME_PARTICIPANT_LAGER_TESTING_TIME);

            test.setTestParts(getListTestPartFullWithAnswerIsCorrect(testId));
            List<TestPart> testPartAndQuestionsResults = arraygementTestResult(
                    test.getTestParts(), testResultModel.getResults());

            TestResultView testResultView = calculateTestResult(
                    testParticipant, test, testPartAndQuestionsResults);

            testParticipant = setTestParticipantResult(testResultView,
                    testParticipant);
            testParticipantRepository.editTestParticipant(testParticipant);

            return Utils.generateSuccessResponseEntity(testResultView);
        } catch (Exception e) {
            e.printStackTrace();
            return Utils.generateFailureResponseEntity(
                    ErrorCode.CODE_TEST_RESULT_EXCEPTION, e.getMessage());
        }
    }

    private TestParticipant setTestParticipantResult(
            TestResultView testResultView, TestParticipant testParticipant) {
        if (testParticipant.getTestCorrectAnswers() < testResultView
                .getTotalCorrectAnswer())
            testParticipant.setTestCorrectAnswers(testResultView
                    .getTotalCorrectAnswer());

        testParticipant.setTestStatus(testResultView.getTestStatusInt());
        testParticipant.setNumberOfTested(testResultView.getNumberOfTested());

        if (testParticipant.getTestResultStatus() != TestResultStatus.PASSED
                .getIntValue()) {
            if (testResultView.getTestResultStatusInt() == TestResultStatus.PASSED
                    .getIntValue()) {
                testParticipant.setTestResultStatus(TestResultStatus.PASSED
                        .getIntValue());
            } else {
                testParticipant.setTestResultStatus(TestResultStatus.FAILED
                        .getIntValue());
            }
        }
        testParticipant.setUpdatedAt(Utils.getCurrentTime());
        return testParticipant;
    }

    public boolean outOfExpDate(Test test) {
        Timestamp toDate = new Timestamp(System.currentTimeMillis());
        Timestamp expDate = test.getEndingTime();
        return toDate.after(expDate);
    }

    public boolean notStartYet(Test test) {
        Timestamp toDate = new Timestamp(System.currentTimeMillis());
        return toDate.before(test.getBeginingTime());
    }

    public boolean outOfMaxTestingTime(Test test, TestParticipant participant) {
        return (participant.getNumberOfTested() >= test.getMaxTestTimes());
    }

    private Test getTestFull(int testId) throws ObjectNotFoundException,
            Exception {
        Test test = testRepository.getTestById(testId);
        if (test == null)
            throw new ObjectNotFoundException("Cannot find test with id = "
                    + testId);

        List<TestPart> testParts = getListTestPartFull(testId);
        test.setTestParts(testParts);
        return test;
    }

    private List<TestPart> getListTestPartFull(int testId)
            throws ObjectNotFoundException, Exception {
        List<TestPart> testParts = testPartRepository
                .getTestPartListByTestId(testId);
        for (TestPart testPart : testParts) {
            List<Question> questions = questionRepository
                    .getQuestionListByTestPartId(testPart.getId());
            for (Question question : questions) {
                int questionId = question.getId();
                if (question.getQuestionTypeId() == QuestionTypes.RADIO
                        .getValue()
                        || question.getQuestionTypeId() == QuestionTypes.CHECKBOX
                                .getValue()) {
                    question.setAnswers(answerRepository
                            .getAnswerListByQuestionId(questionId));
                }
            }
            testPart.setQuestions(questions);
        }
        return testParts;
    }

    private List<TestPart> getListTestPartFullWithAnswerIsCorrect(int testId)
            throws ObjectNotFoundException, Exception {
        List<TestPart> testParts = testPartRepository
                .getTestPartListByTestId(testId);
        for (TestPart testPart : testParts) {
            List<Question> questions = questionRepository
                    .getQuestionListByTestPartId(testPart.getId());
            for (Question question : questions) {
                int questionId = question.getId();
                if (question.getQuestionTypeId() == QuestionTypes.RADIO
                        .getValue()
                        || question.getQuestionTypeId() == QuestionTypes.CHECKBOX
                                .getValue()) {
                    question.setAnswers(answerRepository
                            .getAnswerListByQuestionIdAndIsCorrect(questionId));
                }
            }
            testPart.setQuestions(questions);
        }
        return testParts;
    }

    private List<TestPart> arraygementTestResult(List<TestPart> testParts,
            List<Result> results) {
        List<TestPart> testPartAndQuestionResults = new ArrayList<>();
        for (TestPart testPart : testParts) {
            TestPart testPartResult = new TestPart();
            testPartResult.setId(testPart.getId());
            List<Question> questionResults = new ArrayList<>();
            for (Question question : testPart.getQuestions()) {
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
                questionResults.add(questionResult);
            }
            testPartResult.setQuestions(questionResults);
            testPartAndQuestionResults.add(testPartResult);
        }
        return testPartAndQuestionResults;
    }

    private TestResultView calculateTestResult(TestParticipant testParticipant,
            Test test, List<TestPart> testPartAndQuestionsResults) {
        // TODO Auto-generated method stub
        TestResultView testResultView = new TestResultView();
        testResultView.setTestId(test.getId());
        testResultView.setAmountQuestionPassed(test.getAmountQuestionPassed());
        testResultView.setMaxTestTimes(test.getMaxTestTimes());
        testResultView.setNumberQuestions(test.getNumberQuestions());
        List<TestPartResultView> testPartResultViews = new ArrayList<>();

        for (int i = 0; i < test.getTestParts().size(); i++) {
            TestPart testPartEntity = test.getTestParts().get(i);
            TestPart testPartResult = testPartAndQuestionsResults.get(i);
            TestPartResultView testPartResultView = calculateTestPartResult(
                    testPartEntity, testPartResult);
            testPartResultViews.add(testPartResultView);
        }
        int totalCorrectAnswer = 0;
        for (TestPartResultView view : testPartResultViews) {
            totalCorrectAnswer += view.getTotalCorrectAnswerInPart();
        }
        testResultView.setTotalCorrectAnswer(totalCorrectAnswer);
        testResultView.setTestPartResults(testPartResultViews);

        testResultView
                .setNumberOfTested(testParticipant.getNumberOfTested() + 1);

        testResultView.setTestStatusInt(TestStatus.TESTED.getIntValue());
        testResultView.setTestStatus(String.format(Common.
                convertTestStatus(TestStatus.TESTED.getIntValue()),
                testResultView.getNumberOfTested()));

        if (totalCorrectAnswer >= test.getAmountQuestionPassed()) {
            testResultView.setTestResultStatusInt(TestResultStatus.PASSED
                    .getIntValue());
            testResultView.setTestResultStatus(TestResultStatus.PASSED
                    .getStrValue());
        } else {
            testResultView.setTestResultStatusInt(TestResultStatus.FAILED
                    .getIntValue());
            testResultView.setTestResultStatus(TestResultStatus.FAILED
                    .getStrValue());
        }

        return testResultView;
    }

    private TestPartResultView calculateTestPartResult(TestPart testPartEntity,
            TestPart testPartResult) {
        TestPartResultView testPartResultView = new TestPartResultView();
        testPartResultView.setNumberQuestionInPart(testPartEntity
                .getQuestions().size());
        testPartResultView.setTestPartDesc(testPartEntity.getPartDesc());
        int totalCorrectAnswerInPart = 0;
        for (int i = 0; i < testPartEntity.getQuestions().size(); i++) {
            Question questionEntity = testPartEntity.getQuestions().get(i);
            Question questionResult = testPartResult.getQuestions().get(i);

            int typeId = questionEntity.getQuestionTypeId();
            if (typeId == QuestionTypes.RADIO.getValue())
                totalCorrectAnswerInPart += calculateResultRadioQuestion(
                        questionEntity, questionResult);
            else if (typeId == QuestionTypes.CHECKBOX.getValue())
                totalCorrectAnswerInPart += calculateResultCheckBoxQuestion(
                        questionEntity, questionResult);
        }
        testPartResultView
                .setTotalCorrectAnswerInPart(totalCorrectAnswerInPart);
        return testPartResultView;
    }

    private int calculateResultCheckBoxQuestion(Question questionEntity,
            Question questionResult) {
        if (questionEntity.getAnswers().size() != questionResult.getAnswers()
                .size())
            return 0;

        for (Answer answerEntity : questionEntity.getAnswers()) {
            boolean isCorrect = false;
            for (Answer answerResult : questionResult.getAnswers()) {
                if (answerEntity.getId() == answerResult.getId())
                    isCorrect = true;
            }
            if (!isCorrect)
                return 0;
        }
        return 1;
    }

    private int calculateResultRadioQuestion(Question questionEntity,
            Question questionResult) {
        if (questionEntity.getAnswers().size() != questionResult.getAnswers()
                .size())
            return 0;
        for (Answer answerEntity : questionEntity.getAnswers()) {
            boolean isCorrect = false;
            for (Answer answerResult : questionResult.getAnswers()) {
                if (answerEntity.getId() == answerResult.getId())
                    isCorrect = true;
            }
            if (!isCorrect)
                return 0;
        }
        return 1;
    }

   

    @Override
    public ResponseEntity<?> viewTestResult(int testId, String trainee) {
        try {
            List<TestParticipant> testParticipants = null;
            if (trainee == null || trainee.isEmpty()) {
                testParticipants = this.testParticipantRepository
                        .getTestParticipantListByTestId(testId);
            } else {
                testParticipants = this.testParticipantRepository
                        .searchTestParticipantListByTestIdAndTraineeName(
                                testId, trainee);
            }
            if (testParticipants == null || testParticipants.size() == 0)
                throw new ObjectNotFoundException(
                        "TestParticipant could not be found!");
            return Utils
                    .generateSuccessResponseEntity(testParticipantEntityToModel(testParticipants));
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("EXCEPTION - getStatusList " + e.getMessage());
            return Utils.generateFailureResponseEntity(
                    ErrorCode.CODE_OBJECT_NOT_FOUND_EXCEPTION,
                    e.getMessage());
        }
    }

    private List<TestParticipantResultView> testParticipantEntityToModel(
            List<TestParticipant> testParticipants) {
        List<TestParticipantResultView> testParticipantResultViewLst = new ArrayList<TestParticipantResultView>();
        for (TestParticipant testParticipant : testParticipants) {
            TestParticipantResultView testParticipantResultView = new TestParticipantResultView();
            User user = this.userRepository.getUserById(testParticipant
                    .getUserId());
            if (user == null) {
                continue;
            } else {
                testParticipantResultView.setTraineeName(user.getFirstName()
                        + " " + user.getLastName());
            }
            testParticipantResultView.setNumberOfTested(testParticipant
                    .getNumberOfTested());
            testParticipantResultView
                    .setTestResultStatus(Common.convertTestResultStatus(testParticipant
                            .getTestResultStatus()));
            testParticipantResultViewLst.add(testParticipantResultView);
        }
        return testParticipantResultViewLst;

    }
}
