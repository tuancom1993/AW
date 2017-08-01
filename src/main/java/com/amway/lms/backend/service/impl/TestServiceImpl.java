package com.amway.lms.backend.service.impl;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URLEncoder;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.transaction.Transactional;

import org.apache.commons.lang.StringUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.amway.lms.backend.common.AmwayEnum.QuestionTypes;
import com.amway.lms.backend.common.Common;
import com.amway.lms.backend.common.EmailHepler;
import com.amway.lms.backend.common.ErrorCode;
import com.amway.lms.backend.common.Message;
import com.amway.lms.backend.common.Utils;
import com.amway.lms.backend.entity.Answer;
import com.amway.lms.backend.entity.Question;
import com.amway.lms.backend.entity.Test;
import com.amway.lms.backend.entity.TestPart;
import com.amway.lms.backend.entity.TestParticipant;
import com.amway.lms.backend.entity.TestQuestion;
import com.amway.lms.backend.entity.User;
import com.amway.lms.backend.exception.AddObjectException;
import com.amway.lms.backend.exception.DeleteObjectException;
import com.amway.lms.backend.exception.EditObjectException;
import com.amway.lms.backend.exception.ObjectNotFoundException;
import com.amway.lms.backend.exception.OutOfExpiredDateException;
import com.amway.lms.backend.exception.QuestionExistException;
import com.amway.lms.backend.exception.TestNotAvailableException;
import com.amway.lms.backend.exception.TestingTimeException;
import com.amway.lms.backend.exception.UserLoginNotCorrectException;
import com.amway.lms.backend.exception.UserNotFoundException;
import com.amway.lms.backend.model.ChoiceModel;
import com.amway.lms.backend.model.ManageTestModel;
import com.amway.lms.backend.model.PageModel;
import com.amway.lms.backend.model.QuestionModel;
import com.amway.lms.backend.model.TestEmailModel;
import com.amway.lms.backend.model.TestGenURL;
import com.amway.lms.backend.model.TestInformation;
import com.amway.lms.backend.model.TestModel;
import com.amway.lms.backend.model.TestModelForCreateEdit;
import com.amway.lms.backend.model.TestParticipantInfoModel;
import com.amway.lms.backend.repository.AnswerRepository;
import com.amway.lms.backend.repository.QuestionRepository;
import com.amway.lms.backend.repository.TestPartRepository;
import com.amway.lms.backend.repository.TestParticipantRepository;
import com.amway.lms.backend.repository.TestQuestionRepository;
import com.amway.lms.backend.repository.TestRepository;
import com.amway.lms.backend.repository.UserRepository;
import com.amway.lms.backend.service.EmailService;
import com.amway.lms.backend.service.TestService;

@Service
@Transactional
public class TestServiceImpl implements TestService {

    private static final Logger logger = LoggerFactory.getLogger(TestServiceImpl.class);

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
    EmailService emailService;

    @Autowired
    UserRepository userRepository;

    @Autowired
    TestParticipantRepository testParticipantRepository;

    @Autowired
    EmailHepler emailHepler;

    private Workbook workbook;

    @Override
    public ResponseEntity<?> startTest(User userLogin, String encodedValue) throws Exception {
        try {
            if (userLogin == null)
                throw new UserNotFoundException("UserLogin is null");

            TestGenURL testGenURL = Utils.jsonStringToObject(Utils.decode(encodedValue), TestGenURL.class);

            Test test = testRepository.getTestById(testGenURL.getTestId());
            if (test == null)
                throw new TestNotAvailableException(Message.MSG_TEST_NOT_AVAILABE);

            int testId = test.getId();
            int userId = userLogin.getId();

            TestParticipant testParticipant = testParticipantRepository.getTestParticipantByTestIdAndUserId(testId,
                    userId);
            if (testParticipant == null)
                throw new UserLoginNotCorrectException(Message.MSG_USER_LOGIN_NOT_CORRECT);

            if (outOfExpDate(test))
                throw new OutOfExpiredDateException(Message.MSG_TEST_OUT_OF_EXPIRED_DATE);

            if (outOfMaxTestingTime(test, testParticipant))
                throw new TestingTimeException(Message.MSG_TESTING_TIME_PARTICIPANT_LAGER_TESTING_TIME);

            test.setTestParts(getListTestPartFull(testId));

            return Utils.generateSuccessResponseEntity(convertDataTestEntityToTestModel(test, testParticipant));
        } catch (TestNotAvailableException e) {
            e.printStackTrace();
            return Utils.generateFailureResponseEntity(e.CODE, e.MESSAGE);
        } catch (UserLoginNotCorrectException e) {
            e.printStackTrace();
            return Utils.generateFailureResponseEntity(e.CODE, e.MESSAGE);
        } catch (UserNotFoundException e) {
            e.printStackTrace();
            return Utils.generateFailureResponseEntity(e.CODE, e.MESSAGE);
        } catch (OutOfExpiredDateException e) {
            e.printStackTrace();
            return Utils.generateFailureResponseEntity(e.CODE, e.MESSAGE);
        } catch (TestingTimeException e) {
            e.printStackTrace();
            return Utils.generateFailureResponseEntity(e.CODE, e.MESSAGE);
        } catch (Exception e) {
            e.printStackTrace();
            return Utils.generateFailureResponseEntity(TestNotAvailableException.CODE,
                    TestNotAvailableException.MESSAGE);
        }
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

    private TestModel convertDataTestEntityToTestModel(Test test, TestParticipant testParticipant) {
        TestModel testModel = new TestModel();
        testModel.setName(String.valueOf(test.getId()));
        testModel.setTitle(test.getTitle());
        testModel.setUserId(testParticipant.getUserId());
        testModel.setTestId(test.getId());
        testModel.setQuestionTitleTemplate("{no}. {title}");

        List<PageModel> pageModels = new ArrayList<>();
        int pageNumber = 1;
        for (TestPart testPart : test.getTestParts()) {
            PageModel pageModel = new PageModel();
            pageModel.setName(new StringBuffer().append("page").append(pageNumber++).toString());
            pageModel.setTitle(testPart.getPartDesc());
            List<QuestionModel> questionModels = new ArrayList<>();
            // convert QuestionEntity to QuestionModel
            for (Question questionEntity : testPart.getQuestions()) {
                questionModels.add(convertDataFromQuestionEntityToQuestionModel(questionEntity));
            }
            // random question in part
            Collections.shuffle(questionModels);

            pageModel.setQuestions(questionModels);
            pageModels.add(pageModel);
        }
        testModel.setPages(pageModels);
        return testModel;
    }

    private QuestionModel convertDataFromQuestionEntityToQuestionModel(Question questionEntity) {
        QuestionModel questionModel = new QuestionModel();
        questionModel.setName(String.valueOf(questionEntity.getId()));
        questionModel.setIsRequired(false);
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

    @Override
    public ResponseEntity<?> createTestFull(TestModelForCreateEdit testModelForCreateEdit)
            throws AddObjectException, Exception {
        try {
            Test test = new Test();
            test.setAmountQuestionPassed(testModelForCreateEdit.getTestInfo().getNumberOfQuestionPass());
            test.setBeginingTime(Common.getTimeStampFromString(testModelForCreateEdit.getTestInfo().getStartDate(),
                    Common.DATE_FORMAT_DD_MMM_YYYY));
            test.setEndingTime(Common.setTimeToMidnight(Common.getTimeStampFromString(
                    testModelForCreateEdit.getTestInfo().getEndDate(), Common.DATE_FORMAT_DD_MMM_YYYY)));
            test.setCreatedAt(Utils.getCurrentTime());
            test.setMaxTestTimes(testModelForCreateEdit.getTestInfo().getMaxTestingTime());
            test.setTitle(testModelForCreateEdit.getTestInfo().getTestTitle());
            test.setTopic(testModelForCreateEdit.getTestInfo().getTopic());
            test.setNumberQuestions(countNumberQuestionInTestParts(testModelForCreateEdit.getParts()));

            testRepository.addTest(test);
            int testId = test.getId();
            testModelForCreateEdit.getTestInfo().setId(testId);

            createTestParts(testId, testModelForCreateEdit.getParts());
            setIndexQuestion(testId, testModelForCreateEdit.getParts());

            return Utils.generateSuccessResponseEntity(testModelForCreateEdit);
        } catch (Exception e) {
            e.printStackTrace();
            return Utils.generateFailureResponseEntity(ErrorCode.CODE_ADD_OBJECT_EXCEPTION,
                    ErrorCode.MSG_ADD_OBJECT_EXCEPTION);
        }
    }

    private List<TestPart> createTestParts(int testId, List<TestPart> testParts) throws Exception {
        validateTestParts(testParts, true);
        for (TestPart testPart : testParts) {
            testPartRepository.addTestPart(testPart);
            for (Question question : testPart.getQuestions()) {
                if (question.getId() == 0)
                    createTestQuestionByNewQuestion(testId, testPart.getId(), question);
                else
                    createTestQuestionByOldQuestion(testId, testPart.getId(), question);
            }
        }
        return testParts;
    }

    private void createTestQuestionByNewQuestion(int testId, int testPartId, Question question) throws Exception {
        question.setId(0);
        question.setIsRequired(0);
        question.setIsQuiz(2);
        questionRepository.addQuestion(question);

        for (Answer answer : question.getAnswers()) {
            answer.setId(0);
            answer.setQuestionId(question.getId());
            answerRepository.addAnswer(answer);
        }

        TestQuestion testQuestion = new TestQuestion();
        testQuestion.setIndexQuestion(question.getNo());
        testQuestion.setQuestionId(question.getId());
        testQuestion.setTestId(testId);
        testQuestion.setTestPartId(testPartId);

        testQuestionRepository.addTestQuestion(testQuestion);
    }

    private void createTestQuestionByOldQuestion(int testId, int testPartId, Question question)
            throws ObjectNotFoundException, QuestionExistException, Exception {
        int questionId = question.getId();
        Question questionCheckExits = questionRepository.getQuestionById(questionId);
        if (questionCheckExits == null)
            throw new ObjectNotFoundException(
                    "TestQuestion cannot create, cause Question cannot found with id = " + questionId);

        TestQuestion testQuestionCheckExist = testQuestionRepository.getTestQuestionByTestIdAndQuestionId(testId,
                questionId);
        if (testQuestionCheckExist != null)
            throw new QuestionExistException("Duplicated...Question is exist in Test");

        TestQuestion testQuestion = new TestQuestion();
        testQuestion.setTestId(testId);
        testQuestion.setTestPartId(testPartId);
        testQuestion.setQuestionId(questionId);
        testQuestionRepository.addTestQuestion(testQuestion);
    }

    @Override
    public ResponseEntity<?> editTestFull(TestModelForCreateEdit testModelForCreateEdit)
            throws EditObjectException, Exception {
        try {
            int testId = testModelForCreateEdit.getTestInfo().getId();
            Test testDB = testRepository.getTestById(testId);

            validateTestToEdit(testDB, testId);
            deleteAllTestPartAndTestQuestion(testDB);

            testDB.setAmountQuestionPassed(testModelForCreateEdit.getTestInfo().getNumberOfQuestionPass());
            testDB.setBeginingTime(Common.getTimeStampFromString(testModelForCreateEdit.getTestInfo().getStartDate(),
                    Common.DATE_FORMAT_DD_MMM_YYYY));
            testDB.setEndingTime(Common.setTimeToMidnight(Common.getTimeStampFromString(
                    testModelForCreateEdit.getTestInfo().getEndDate(), Common.DATE_FORMAT_DD_MMM_YYYY)));
            testDB.setUpdatedAt(Utils.getCurrentTime());
            testDB.setMaxTestTimes(testModelForCreateEdit.getTestInfo().getMaxTestingTime());
            testDB.setTitle(testModelForCreateEdit.getTestInfo().getTestTitle());
            testDB.setTopic(testModelForCreateEdit.getTestInfo().getTopic());
            testDB.setNumberQuestions(countNumberQuestionInTestParts(testModelForCreateEdit.getParts()));

            testRepository.updateTest(testDB);

            validateTestParts(testModelForCreateEdit.getParts(), true);
            for (TestPart testPart : testModelForCreateEdit.getParts()) {
                if (testPart.getId() != 0)
                    updateTestPart(testId, testPart);
                else
                    createTestPart(testId, testPart);
            }
            setIndexQuestion(testId, testModelForCreateEdit.getParts());

            return Utils.generateSuccessResponseEntity(testModelForCreateEdit);
        } catch (Exception e) {
            e.printStackTrace();
            return Utils.generateFailureResponseEntity(ErrorCode.CODE_EDIT_OBJECT_EXCEPTION,
                    ErrorCode.MSG_EDIT_OBJECT_EXCEPTION);
        }
    }

    private void deleteAllTestPartAndTestQuestion(Test test) throws ObjectNotFoundException, Exception {
        List<TestPart> testParts = testPartRepository.getTestPartListByTestId(test.getId());
        for (TestPart testPart : testParts) {
            testPartRepository.deleteTestPart(testPart);
        }
        List<TestQuestion> testQuestions = testQuestionRepository.getTestQuestionListByTestId(test.getId());
        for (TestQuestion testQuestion : testQuestions) {
            testQuestionRepository.deleteTestQuestion(testQuestion);
        }
    }

    private void validateTestToEdit(Test testDB, int testId) throws ObjectNotFoundException, Exception {
        if (testDB == null)
            throw new ObjectNotFoundException("Cannot find Test with id = " + testId);
        if (testDB.getDateSentMail() != null)
            throw new EditObjectException("Cannot edit this test. Cause this test already sent mail.");
    }

    private void updateTestPart(int testId, TestPart testPart) throws ObjectNotFoundException, Exception {
        TestPart testPartDB = testPartRepository.getTestPartById(testPart.getId());
        if (testPartDB == null)
            throw new ObjectNotFoundException("Cannot found TestPart by id = " + testPart.getId());
        testPartDB.setPartDesc(testPart.getPartDesc());
        testPartRepository.updateTestPart(testPartDB);

        for (Question question : testPart.getQuestions()) {
            if (question.getId() == 0)
                createTestQuestionByNewQuestion(testId, testPart.getId(), question);
            else {
                TestQuestion testQuestionCheck = testQuestionRepository.getTestQuestionByTestIdAndQuestionId(testId,
                        question.getId());
                if (testQuestionCheck == null)
                    createTestQuestionByOldQuestion(testId, testPart.getId(), question);
            }
        }
    }

    private void createTestPart(int testId, TestPart testPart)
            throws ObjectNotFoundException, AddObjectException, Exception {
        testPartRepository.addTestPart(testPart);
        for (Question question : testPart.getQuestions()) {
            if (question.getId() == 0)
                createTestQuestionByNewQuestion(testId, testPart.getId(), question);
            else
                createTestQuestionByOldQuestion(testId, testPart.getId(), question);
        }

    }

    @Override
    public ResponseEntity<?> getTestModelFull(int testId) throws ObjectNotFoundException, Exception {
        logger.debug("getTestModelFull(int testId).....!");
        try {
            Test testEntity = getTestFull(testId);
            TestModelForCreateEdit testModel = new TestModelForCreateEdit();
            testModel.setParts(testEntity.getTestParts());

            TestInformation testInfo = new TestInformation();
            testInfo.setEndDate(Common.getStringDate(testEntity.getEndingTime(), Common.DATE_FORMAT_DD_MMM_YYYY));
            testInfo.setStartDate(Common.getStringDate(testEntity.getBeginingTime(), Common.DATE_FORMAT_DD_MMM_YYYY));
            testInfo.setId(testId);
            testInfo.setMaxTestingTime(testEntity.getMaxTestTimes());
            testInfo.setNumberOfQuestionPass(testEntity.getAmountQuestionPassed());
            testInfo.setTestTitle(testEntity.getTitle());
            testInfo.setTopic(testEntity.getTopic());
            testInfo.setNumberOfQuestion(testEntity.getNumberQuestions());

            testModel.setTestInfo(testInfo);
            return Utils.generateSuccessResponseEntity(testModel);
        } catch (Exception e) {
            e.printStackTrace();
            return Utils.generateFailureResponseEntity(ErrorCode.CODE_OBJECT_NOT_FOUND_EXCEPTION,
                    ErrorCode.MSG_OBJECT_NOT_FOUND_EXCEPTION);
        }

    }

    private Test getTestFull(int testId) throws ObjectNotFoundException, Exception {
        Test test = testRepository.getTestById(testId);
        if (test == null)
            throw new ObjectNotFoundException("Cannot find test with id = " + testId);

        List<TestPart> testParts = getListTestPartFull(testId);
        test.setTestParts(testParts);
        return test;
    }

    @Override
    public ResponseEntity<?> getTestEntityInfor(int testId) throws Exception {
        logger.debug("getTestEntityInfor(int testId).....!");
        try {
            Test test = testRepository.getTestById(testId);
            if (test == null)
                throw new ObjectNotFoundException("Cannot find test with id = " + testId);

            return Utils.generateSuccessResponseEntity(test);
        } catch (Exception e) {
            e.printStackTrace();
            return Utils.generateFailureResponseEntity(ErrorCode.CODE_OBJECT_NOT_FOUND_EXCEPTION,
                    ErrorCode.MSG_OBJECT_NOT_FOUND_EXCEPTION);
        }
    }

    private List<TestPart> getListTestPartFull(int testId) throws ObjectNotFoundException, Exception {
        List<TestPart> testParts = testPartRepository.getTestPartListByTestId(testId);
        for (TestPart testPart : testParts) {
            List<Question> questions = questionRepository.getQuestionListByTestPartId(testPart.getId());
            for (Question question : questions) {
                int questionId = question.getId();
                if (question.getQuestionTypeId() == QuestionTypes.RADIO.getValue()
                        || question.getQuestionTypeId() == QuestionTypes.CHECKBOX.getValue()
                        || question.getQuestionTypeId() == QuestionTypes.CHECKBOX_OTHER.getValue()
                        || question.getQuestionTypeId() == QuestionTypes.DROP_DOWN_LIST.getValue()) {
                    question.setAnswers(answerRepository.getAnswerListByQuestionId(questionId));
                }
            }
            testPart.setQuestions(questions);
        }
        return testParts;
    }

    @Override
    public ResponseEntity<?> getTestList() throws Exception {
        try {
            List<Test> testList = testRepository.getTestList();
            if (testList == null || testList.isEmpty()) {
                throw new ObjectNotFoundException(ErrorCode.MSG_TEST_NOT_AVAILABLE_EXCEPTION);
            }
            List<ManageTestModel> resTestList = convertTestListToModelList(testList);
            return Utils.generateSuccessResponseEntity(resTestList);
        } catch (Exception e) {
            logger.error("EXCEPTION - getTestList " + e.getMessage());
            return Utils.generateFailureResponseEntity(ErrorCode.CODE_TEST_NOT_AVAILABLE_EXCEPTION,
                    ErrorCode.MSG_TEST_NOT_AVAILABLE_EXCEPTION);
        }
    }

    private List<ManageTestModel> convertTestListToModelList(List<Test> testList) {
        List<ManageTestModel> modelList = new ArrayList<ManageTestModel>();
        for (Test test : testList) {
            ManageTestModel model = new ManageTestModel();
            model.setTestId(test.getId());
            model.setTitle(test.getTitle());
            model.setBeginningTime(Common.getStringDate(test.getBeginingTime(), Common.DATE_FORMAT_DD_MMM_YYYY));
            model.setEndingTime(Common.getStringDate(test.getEndingTime(), Common.DATE_FORMAT_DD_MMM_YYYY));
            setStatusTestModel(model, test);
            modelList.add(model);
        }
        return modelList;
    }

    private void setStatusTestModel(ManageTestModel manageTestModel, Test test) {
        manageTestModel.setStatus("NEW");
        Timestamp toDate = new Timestamp(System.currentTimeMillis());
        if (test.getDateSentMail() != null) {
            manageTestModel.setStatus("SENT");
            manageTestModel.setIsSent(1);
        }
        if (toDate.after(test.getEndingTime())) {
            manageTestModel.setStatus("EXPIRED");
        }
    }

    @Override
    public String genURL(int testId, int testParticipantId) throws ObjectNotFoundException, Exception {
        return URLEncoder.encode(Utils.encode(Utils.objectToJsonString(new TestGenURL(testId, testParticipantId))),
                "UTF-8");
    }

    @Override
    public ResponseEntity<?> cloneTest(int testId) throws ObjectNotFoundException, Exception {
        // Get test info
        Test test = testRepository.getTestById(testId);
        if (test == null)
            throw new ObjectNotFoundException("Cannot clone Test cause Test with id =" + testId + " cannot found");

        Test testAdd = new Test();
        testAdd.setBeginingTime(test.getBeginingTime());
        testAdd.setEndingTime(test.getEndingTime());
        testAdd.setTitle(test.getTitle() + " (Clone)");
        testAdd.setTopic(test.getTopic());
        testAdd.setMaxTestTimes(test.getMaxTestTimes());
        testAdd.setNumberQuestions(test.getNumberQuestions());
        testAdd.setAmountQuestionPassed(test.getAmountQuestionPassed());
        testAdd.setUserId(test.getUserId());
        testAdd.setCreatedAt(Utils.getCurrentTime());
        // Clone new test
        testRepository.addTest(testAdd);

        int testIdAdd = testAdd.getId();
        int indexQuestion = 1;
        List<TestPart> testParts = testPartRepository.getTestPartListByTestId(testId);
        for (TestPart testPart : testParts) {
            TestPart testPartAdd = new TestPart();
            testPartAdd.setPartDesc(testPart.getPartDesc());
            testPartRepository.addTestPart(testPartAdd);

            int testPartIdAdd = testPartAdd.getId();
            List<TestQuestion> testQuestions = testQuestionRepository.getTestQuestionListByTestPartId(testPart.getId());
            for (TestQuestion testQuestion : testQuestions) {
                TestQuestion testQuestionAdd = new TestQuestion();
                testQuestionAdd.setIndexQuestion(indexQuestion++);
                testQuestionAdd.setQuestionId(testQuestion.getQuestionId());
                testQuestionAdd.setTestPartId(testPartIdAdd);
                testQuestionAdd.setTestId(testIdAdd);
                testQuestionRepository.addTestQuestion(testQuestionAdd);
            }
        }

        return Utils.generateSuccessResponseEntity(testAdd);
    }

    @Override
    public ResponseEntity<?> deleteTest(List<Test> tests)
            throws DeleteObjectException, ObjectNotFoundException, Exception {
        try {
            List<Test> testReturn = new ArrayList<Test>();
            for (Test test : tests) {
                int testId = test.getId();
                Test testToDelete = testRepository.getTestById(testId);
                if (testToDelete == null) {
                    logger.debug("Cannot delete Test. Cause not found Test with id = " + testId);
                    continue;
                }
                if (testToDelete.getDateSentMail() != null) {
                    logger.debug("Cannot delete Test. Cause test was sent with testId = " + testId);
                    testReturn.add(testToDelete);
                    continue;
                }
                deleteTest(testToDelete);
            }
            return Utils.generateSuccessResponseEntity(testReturn);
        } catch (Exception e) {
            e.printStackTrace();
            return Utils.generateFailureResponseEntity(ErrorCode.CODE_DELETE_OBJECT_EXCEPTION,
                    ErrorCode.MSG_DELETE_OBJECT_EXCEPTION);
        }
    }

    private boolean isExpired(Timestamp endDate) {
        Timestamp toDate = new Timestamp(System.currentTimeMillis());
        return toDate.after(endDate);
    }

    private void deleteTest(Test test) {
        int testId = test.getId();
        testRepository.deleteTest(test);
        List<TestQuestion> listTestQuestion = testQuestionRepository.getTestQuestionListByTestId(testId);
        for (TestQuestion testQuestion : listTestQuestion) {
            int testPartId = testQuestion.getTestPartId();
            TestPart testPart = testPartRepository.getTestPartById(testPartId);
            if (testPart == null) {
                // do nothing
            } else {
                testPartRepository.deleteTestPart(testPart);
            }
            testQuestionRepository.deleteTestQuestion(testQuestion);
        }
    }

    private int countNumberQuestionInTestParts(List<TestPart> parts) {
        int count = 0;
        if (parts == null)
            return 0;
        for (TestPart part : parts) {
            if (part.getQuestions() == null || part.getQuestions().isEmpty())
                count += 0;
            else
                count += part.getQuestions().size();
        }
        return count;
    }

    private void validateTestParts(List<TestPart> testParts, boolean isValidateRoot) throws Exception {
        if (testParts == null || testParts.isEmpty())
            throw new Exception("TestPart List is null or empty");
        for (TestPart part : testParts) {
            if (StringUtils.isEmpty(part.getPartDesc()))
                throw new Exception("TestPart Desc is null or empty");
            if (isValidateRoot)
                validateQuestions(part.getQuestions(), isValidateRoot);
        }
    }

    private void validateQuestions(List<Question> questions, boolean isValidateRoot) throws Exception {
        if (questions == null || questions.isEmpty())
            throw new Exception("Question List is null or empty");
        for (Question question : questions) {
            if (StringUtils.isEmpty(question.getQuestionDesc()))
                throw new Exception("Question Desc is null or empty");
            if (question.getQuestionTypeId() != QuestionTypes.CHECKBOX.getValue()
                    && question.getQuestionTypeId() != QuestionTypes.RADIO.getValue())
                throw new Exception("Question Type Id is not correct");
            if (isValidateRoot)
                validateAnswers(question);
        }
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

    private void setIndexQuestion(int testId, List<TestPart> testParts) {
        int indexQuestion = 1;
        for (TestPart testPart : testParts) {
            for (Question question : testPart.getQuestions()) {
                TestQuestion testQuestion = testQuestionRepository.getTestQuestionByTestIdAndQuestionId(testId,
                        question.getId());
                testQuestion.setIndexQuestion(indexQuestion);
                testQuestionRepository.updateTestQuestion(testQuestion);
                indexQuestion++;
            }
        }
    }

    @Override
    public File exportTestHorizontal(int testId) throws IOException, Exception {
        try {
            Test test = testRepository.getTestById(testId);
            String pathTemp = new File(System.getProperty("java.io.tmpdir")).getAbsolutePath();
            Workbook workbook = new XSSFWorkbook();
            this.workbook = workbook;

            setWorkBookData(workbook, test);

            File fileReport = new File(pathTemp + "/" + getFileName(test));
            System.out.println("Name: " + fileReport.getName() + " Path: " + fileReport.getAbsolutePath());

            FileOutputStream fileOutputStream = new FileOutputStream(fileReport);
            workbook.write(fileOutputStream);
            fileOutputStream.flush();
            fileOutputStream.close();
            System.out.println("File done...!");

            return fileReport;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private String getFileName(Test test) {
        String fileName = Common.FILE_NAME_TEST_EXPORT + "_" + test.getTitle();
        fileName = fileName.length() > 220 ? fileName.substring(0, 220) : fileName;
        fileName = org.apache.commons.lang3.StringUtils.stripAccents(fileName);
        fileName += ".xlsx";
        return fileName;
    }

    private void setWorkBookData(Workbook workbook, Test test) {
        Sheet sheet = workbook.createSheet("Test");
        setTestInformationIntoSheet(sheet, test);
        setTestResultsIntoSheet(sheet, test);
        sheet.setColumnWidth(0, 7000);
        for (int i = 1; i < 100; i++) {
            sheet.setColumnWidth(i, 7000);
        }
        sheet.setDefaultColumnWidth(7000);
    }

    private void setTestInformationIntoSheet(Sheet sheet, Test test) {
        Row rowTitle = sheet.createRow(0);
        rowTitle.setHeight((short) (36 * 20));
        rowTitle.createCell(0).setCellValue("Title:");
        rowTitle.getCell(0).setCellStyle(getCellStyleHeader());
        rowTitle.createCell(1).setCellValue(test.getTitle());

        Row rowTopic = sheet.createRow(1);
        rowTopic.setHeight((short) (36 * 20));
        rowTopic.createCell(0).setCellValue("Topic:");
        rowTopic.getCell(0).setCellStyle(getCellStyleHeader());
        rowTopic.createCell(1).setCellValue(test.getTopic());
        
        Row rowDesc = sheet.createRow(2);
        rowDesc.createCell(0).setCellValue("Beginning Time:");
        rowDesc.setHeight((short) (36 * 20));
        rowDesc.getCell(0).setCellStyle(getCellStyleHeader());
        rowDesc.createCell(1)
                .setCellValue(Common.getStringDate(test.getBeginingTime(), Common.DATE_FORMAT_DD_MMM_YYYY));

        Row rowType = sheet.createRow(3);
        rowType.createCell(0).setCellValue("Ending Time:");
        rowType.setHeight((short) (36 * 20));
        rowType.getCell(0).setCellStyle(getCellStyleHeader());
        rowType.createCell(1).setCellValue(Common.getStringDate(test.getEndingTime(), Common.DATE_FORMAT_DD_MMM_YYYY));
        
        Row rowAmountQuestionPass = sheet.createRow(4);
        rowAmountQuestionPass.setHeight((short) (36 * 20));
        rowAmountQuestionPass.createCell(0).setCellValue("Number of Question(s) Pass:");
        rowAmountQuestionPass.getCell(0).setCellStyle(getCellStyleHeader());
        rowAmountQuestionPass.createCell(1).setCellValue(""+test.getAmountQuestionPassed());
        
        Row rowMaxTestTime = sheet.createRow(5);
        rowMaxTestTime.setHeight((short) (36 * 20));
        rowMaxTestTime.createCell(0).setCellValue("Max Testing Times:");
        rowMaxTestTime.getCell(0).setCellStyle(getCellStyleHeader());
        rowMaxTestTime.createCell(1).setCellValue(""+test.getMaxTestTimes());
    }
    

    private void setTestResultsIntoSheet(Sheet sheet, Test test) {
        //TODO 
        setTestResultHeaderTable(sheet, test);
        int rowIndex = 8;
        List<TestParticipant> testParticipants = testParticipantRepository.getTestParticipantListByTestId(test.getId());
        for(TestParticipant testParticipant : testParticipants){
            User user = userRepository.getUserById(testParticipant.getUserId());
            if(user == null){
                logger.error("--setTestResultsIntoSheet: Cannot find user by id = "+testParticipant.getUserId());
                continue;
            }
            Row row = sheet.createRow(rowIndex);
            row.createCell(0).setCellValue(user.getFirstName() + " "+user.getLastName());
            row.createCell(1).setCellValue(String.format(Common.convertTestStatus(testParticipant.getTestStatus()), testParticipant.getNumberOfTested()));
            row.createCell(2).setCellValue(Common.convertTestResultStatus(testParticipant.getTestResultStatus()));
            
            rowIndex++;
        }
    }
    
    private void setTestResultHeaderTable(Sheet sheet, Test test){
        Row row = sheet.createRow(7);
        
        Cell cell = row.createCell(0);
        cell.setCellStyle(getCellStyleHeader());
        cell.setCellValue("Trainee Name");
        
        cell = row.createCell(1);
        cell.setCellStyle(getCellStyleHeader());
        cell.setCellValue("Tested Times");
        
        cell = row.createCell(2);
        cell.setCellStyle(getCellStyleHeader());
        cell.setCellValue("Result");
    }

    private CellStyle getCellStyleHeader() {
        CellStyle style_header = workbook.createCellStyle();
        Font font_header = workbook.createFont();
        font_header.setBold(true);
        font_header.setColor(IndexedColors.BLACK.index);
        style_header.setFont(font_header);
        style_header.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
        style_header.setWrapText(false);
        return style_header;
    }

    @Override
    public ResponseEntity<?> sendEmail(TestEmailModel testEmailModel) throws Exception {
        try {
            Test test = testRepository.getTestById(testEmailModel.getTestId());
            if (test == null)
                throw new ObjectNotFoundException("Cannot find Test by id = " + testEmailModel.getTestId());

            // List<User> users = getUsersFromIds(testEmailModel.getUserIds());

            List<String> emails = testEmailModel.getEmails();
            emails = clearDuplicate(emails);

            /*
             * for (User user : users) { TestParticipant testParticipant = new
             * TestParticipant(); testParticipant.setTestId(test.getId());
             * testParticipant.setUserId(user.getId());
             * testParticipant.setCreatedAt(Utils.getCurrentTime());
             * testParticipantRepository.addTestParticipant(testParticipant); }
             */
            Iterator<String> it = emails.iterator();
            while (it.hasNext()) {
                String email = it.next();
                User user = userRepository.getUserByEmail(email);
                if (user == null) {
                    logger.error("Cannot find user by email = " + email);
                    it.remove();
                    continue;
                }
                TestParticipant testParticipantCheckExist = testParticipantRepository
                        .getTestParticipantByTestIdAndUserId(testEmailModel.getTestId(), user.getId());
                if (testParticipantCheckExist != null) {
                    logger.error("User is exist with id = " + user.getId());
                    it.remove();
                    continue;
                }

                TestParticipant testParticipant = new TestParticipant();
                testParticipant.setTestId(test.getId());
                testParticipant.setUserId(user.getId());
                testParticipant.setCreatedAt(Utils.getCurrentTime());
                testParticipantRepository.addTestParticipant(testParticipant);

            }
            /*
             * for (String email : emails) { User user =
             * userRepository.getUserByEmail(email); if (user == null) {
             * logger.error("Cannot find user by email = " + email); continue; }
             * TestParticipant testParticipantCheckExist =
             * testParticipantRepository
             * .getTestParticipantByTestIdAndUserId(testEmailModel.getTestId(),
             * user.getId()); if (testParticipantCheckExist != null) {
             * logger.error("User is exist with id = " + user.getId());
             * continue; }
             * 
             * TestParticipant testParticipant = new TestParticipant();
             * testParticipant.setTestId(test.getId());
             * testParticipant.setUserId(user.getId());
             * testParticipant.setCreatedAt(Utils.getCurrentTime());
             * testParticipantRepository.addTestParticipant(testParticipant); }
             */
            emailService.sendEmailForTest((String[]) emails.toArray(new String[emails.size()]), test,
                    genURL(test.getId(), 0));

            test.setDateSentMail(Utils.getCurrentTime());
            testRepository.updateTest(test);

            return Utils.generateSuccessResponseEntity(testEmailModel);
        } catch (Exception e) {
            e.printStackTrace();
            return Utils.generateFailureResponseEntity(ErrorCode.CODE_SEND_EMAIL_EXCEPTION,
                    ErrorCode.MSG_SEND_EMAIL_EXCEPTION);
        }
    }

    private List<String> clearDuplicate(List<String> list) {
        Set<String> set = new HashSet<>();
        set.addAll(list);
        list.clear();
        list.addAll(set);
        return list;
    }

    private List<User> getUsersFromIds(List<Integer> userIds) throws ObjectNotFoundException {
        List<User> users = new ArrayList<>();
        for (int i = 0; i < userIds.size(); i++) {
            int userId = userIds.get(i);
            User user = userRepository.getUserById(userId);
            if (user == null)
                throw new ObjectNotFoundException("Cannot find User by id = " + userId);

            users.add(user);
        }
        return users;
    }

    private String[] getEmailsFromUsers(List<User> users) {
        String[] emails = new String[users.size()];
        for (int i = 0; i < users.size(); i++) {
            User user = users.get(i);
            emails[i] = user.getEmail();
        }
        return emails;
    }

    @Override
    public ResponseEntity<?> getTestInfo(int userId) {
        try {
            List<TestParticipant> testParticipants = this.testParticipantRepository.getTestParticipantByUserId(userId);
            if (testParticipants == null || testParticipants.size() == 0)
                throw new ObjectNotFoundException("TestParticipant could not be found!");

            List<TestParticipantInfoModel> testParticipantInfoModels = new ArrayList<TestParticipantInfoModel>();
            for (TestParticipant testParticipant : testParticipants) {
                TestParticipantInfoModel testParticipantInfoModel = new TestParticipantInfoModel();
                Test test = this.testRepository.getTestById(testParticipant.getTestId());
                if (test == null) {
                    continue;
                }
                testParticipantInfoModel.setTestParticipantId(testParticipant.getId());
                testParticipantInfoModel.setCorrectAnswer(testParticipant.getTestCorrectAnswers());
                int takeTest = outOfExpDate(test) || outOfMaxTestingTime(test, testParticipant) ? 0 : 1;
                testParticipantInfoModel.setTakeTest(takeTest);
                testParticipantInfoModel
                        .setTestResultStatus(Common.convertTestResultStatus(testParticipant.getTestResultStatus()));
                testParticipantInfoModel
                        .setTestStatus(String.format(Common.convertTestStatus(testParticipant.getTestStatus()),
                                testParticipant.getNumberOfTested()));
                testParticipantInfoModel.setTestTitle(test.getTitle());
                testParticipantInfoModel.setUrlTesting(emailHepler.getLinkTakeTest() + genURL(test.getId(), 0));
                testParticipantInfoModels.add(testParticipantInfoModel);
            }
            return Utils.generateSuccessResponseEntity(testParticipantInfoModels);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("EXCEPTION - getStatusList " + e.getMessage());
            return Utils.generateFailureResponseEntity(ErrorCode.CODE_OBJECT_NOT_FOUND_EXCEPTION, e.getMessage());
        }
    }

}
