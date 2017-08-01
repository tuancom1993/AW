    /**
 * 
 */
package com.amway.lms.backend.service.impl;

import java.sql.Timestamp;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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
import com.amway.lms.backend.entity.SurveySession;
import com.amway.lms.backend.exception.AddObjectException;
import com.amway.lms.backend.exception.DeleteObjectException;
import com.amway.lms.backend.exception.EditObjectException;
import com.amway.lms.backend.exception.ObjectNotFoundException;
import com.amway.lms.backend.exception.QuestionExistException;
import com.amway.lms.backend.exception.SurveyNotAvailableException;
import com.amway.lms.backend.exception.SurveyNotStartYetException;
import com.amway.lms.backend.exception.SurveyOutOfExpiredDateException;
import com.amway.lms.backend.model.ChoiceModel;
import com.amway.lms.backend.model.ColumnModel;
import com.amway.lms.backend.model.PageModel;
import com.amway.lms.backend.model.QuestionModel;
import com.amway.lms.backend.model.RowModel;
import com.amway.lms.backend.model.SurveyModel;
import com.amway.lms.backend.model.SurveyModelForCreate;
import com.amway.lms.backend.model.ValidatorModel;
import com.amway.lms.backend.repository.AnswerRepository;
import com.amway.lms.backend.repository.CategoryRepository;
import com.amway.lms.backend.repository.MatrixColumnRepository;
import com.amway.lms.backend.repository.MatrixRowRepository;
import com.amway.lms.backend.repository.QuestionRepository;
import com.amway.lms.backend.repository.QuestionTypeRepository;
import com.amway.lms.backend.repository.ResultRepository;
import com.amway.lms.backend.repository.SurveyQuestionRepository;
import com.amway.lms.backend.repository.SurveyRepository;
import com.amway.lms.backend.repository.SurveyResultRepository;
import com.amway.lms.backend.repository.SurveySessionRepository;
import com.amway.lms.backend.repository.SurveyTypeRepository;
import com.amway.lms.backend.service.SurveyService;


/**
 * @author acton
 * @email acton@enclave.vn
 */
@Transactional
@Service
public class SurveyServiceImpl implements SurveyService {

    private static final Logger logger = LoggerFactory.getLogger(SurveyServiceImpl.class);
    
    @Value("${question.id.survey.training.need.default}")
    private String questionIdSurveyTrainingNeedDefault;
    
    @Value("${question.id.survey.pre.training.default}")
    private String questionIdSurveyPreTrainingDefault;
    
    @Autowired
    SurveyRepository surveyRepository;

    @Autowired
    SurveyQuestionRepository surveyQuestionRepository;

    @Autowired
    QuestionRepository questionRepository;

    @Autowired
    AnswerRepository answerRepository;

    @Autowired
    MatrixColumnRepository matrixColumnRepository;

    @Autowired
    MatrixRowRepository matrixRowRepository;

    @Autowired
    CategoryRepository categoryRepository;

    @Autowired
    QuestionTypeRepository questionTypeRepository;

    @Autowired
    SurveyTypeRepository surveyTypeRepository;

    @Autowired
    SurveySessionRepository surveySessionRepository;

    @Autowired
    SurveyResultRepository surveyResultRepository;

    @Autowired
    ResultRepository resultRepository;

    @Override
    public ResponseEntity<?> getTrainingNeedAssessmentSurvey(String surveyIdEncoded) throws ObjectNotFoundException,
            SurveyNotStartYetException, SurveyOutOfExpiredDateException, SurveyNotAvailableException, Exception {
        int surveyId = 0;
        try {
            surveyId = Integer.valueOf(Utils.decode(surveyIdEncoded).split("_")[1]);
        } catch (Exception e) {
            e.printStackTrace();
            throw new SurveyNotAvailableException("Survey not available");
        }
        Survey survey = surveyRepository.getSurveyById(surveyId);

        if (survey == null)
            throw new SurveyNotAvailableException("Survey Not Found By Id: " + surveyId);

        Timestamp toDate = new Timestamp(System.currentTimeMillis());

        if (toDate.before(Common.setTimeToZero(survey.getBeginingTime())))
            throw new SurveyNotStartYetException("Survey hasn't started yet");

        if (toDate.after(Common.setTimeToMidnight(survey.getEndingTime())))
            throw new SurveyOutOfExpiredDateException("Survey out of expried date");

        if (survey.getSurveyTypeId() != 1)
            throw new SurveyNotAvailableException("Survey type is missing");

        survey.setQuestions(getQuestionListBySurvey(survey));
        return Utils.generateSuccessResponseEntity(converDataSurveyEntityToSurveyModel(survey));
    }

    @Override
    public ResponseEntity<?> getPreTrainingServeySurvey(String surveyIdEncoded) throws ObjectNotFoundException,
            SurveyNotStartYetException, SurveyOutOfExpiredDateException, SurveyNotAvailableException, Exception {
        int surveyId = 0;
        try {
            surveyId = Integer.valueOf(Utils.decode(surveyIdEncoded).split("_")[1]);
        } catch (Exception e) {
            e.printStackTrace();
            throw new SurveyNotAvailableException("Survey not available");
        }
        Survey survey = surveyRepository.getSurveyById(surveyId);
        if (survey == null)
            throw new SurveyNotAvailableException("Survey Not Found By Id: " + surveyId);

        Timestamp toDate = new Timestamp(System.currentTimeMillis());
        if (toDate.before(Common.setTimeToZero(survey.getBeginingTime())))
            throw new SurveyNotStartYetException("Survey hasn't started yet");

        if (toDate.after(Common.setTimeToMidnight(survey.getEndingTime())))
            throw new SurveyOutOfExpiredDateException("Survey out of expried date");

        if (survey.getSurveyTypeId() != 2)
            throw new SurveyNotAvailableException("Survey type is missing");

        survey.setQuestions(getQuestionListBySurvey(survey));
        return Utils.generateSuccessResponseEntity(converDataSurveyEntityToSurveyModel(survey));
    }

    private List<SurveyQuestion> getSurveyQuestionListBySurveyIdOrderBySurveySessionId(int surveyId) {
        List<SurveyQuestion> surveyQuestions = surveyQuestionRepository
                .getSurveyQuestionListBySurveyIdOrderBySurveySessionId(surveyId);
        return surveyQuestions;
    }

    private List<Question> getQuestionListBySurvey(Survey survey) {
        List<Question> questions = new ArrayList<>();
        List<SurveyQuestion> surveyQuestions = getSurveyQuestionListBySurveyIdOrderBySurveySessionId(survey.getId());
        for (SurveyQuestion surveyQuestion : surveyQuestions) {
            Question question = questionRepository.getQuestionById(surveyQuestion.getQuestionId());
            if (question == null)
                continue;

            SurveySession surveySession = surveySessionRepository
                    .getSurveySessionById(surveyQuestion.getSurveySessionId());
            if (surveySession == null) {
                surveySession = new SurveySession();
                surveySession.setId(0);
                surveySession.setSessionDesc("");
                continue;
            }
            question.setSurveySession(surveySession);
            int questionId = question.getId();
            if (question.getQuestionTypeId() == QuestionTypes.TEXT_BOX.getValue() 
                    || question.getQuestionTypeId() == QuestionTypes.RADIO.getValue()
                    || question.getQuestionTypeId() == QuestionTypes.CHECKBOX.getValue()
                    || question.getQuestionTypeId() == QuestionTypes.CHECKBOX_OTHER.getValue()
                    || question.getQuestionTypeId() == QuestionTypes.COMMENT.getValue() 
                    || question.getQuestionTypeId() == QuestionTypes.EMAIL.getValue()
                    || question.getQuestionTypeId() == QuestionTypes.DROP_DOWN_LIST.getValue()) {
                question.setAnswers(getAnswerListByQuestionId(questionId));
            } else if (question.getQuestionTypeId() == QuestionTypes.MATRIX.getValue()) {
                question.setMatrixColumns(getMatrixColumnListByQuestionId(questionId));
                question.setMatrixRows(getMatrixRowListByQuestionId(questionId));
            }
            questions.add(question);
        }

        return questions;
    }

    private List<Answer> getAnswerListByQuestionId(int questionId) {
        return answerRepository.getAnswerListByQuestionId(questionId);
    }

    private List<MatrixColumn> getMatrixColumnListByQuestionId(int questionId) {
        return matrixColumnRepository.getMatrixColumnListByQuestionId(questionId);
    }

    private List<MatrixRow> getMatrixRowListByQuestionId(int questionId) {
        return matrixRowRepository.getMatrixRowListByQuestionId(questionId);
    }

    private SurveyModel converDataSurveyEntityToSurveyModel(Survey surveyEntity) {
        SurveyModel surveyModel = new SurveyModel();
        surveyModel.setName(String.valueOf(surveyEntity.getId()));
        surveyModel.setTitle(surveyEntity.getSurveyTitle());
        surveyModel.setQuestionTitleTemplate("{no}. {title} {require}:");
        surveyModel.setRequiredText("(*)");

        List<PageModel> pageModels = new ArrayList<>();
        PageModel page1 = new PageModel();
        page1.setName("page1");
        page1.setQuestions(setQuestionModelForPage1(surveyEntity));
        pageModels.add(page1);

        int pageNumber = 1;
        int serveySessionId = -1;
        PageModel pageOther = new PageModel();
        List<QuestionModel> questionModels = new ArrayList<>();
        for (int i = 0; i < surveyEntity.getQuestions().size(); i++) {
            Question questionEntity = surveyEntity.getQuestions().get(i);

            if (serveySessionId != questionEntity.getSurveySession().getId()) {
                serveySessionId = questionEntity.getSurveySession().getId();
                pageNumber++;
                pageOther = new PageModel();
                questionModels = new ArrayList<>();
                pageOther.setName("page" + pageNumber);
                pageOther.setTitle(questionEntity.getSurveySession().getSessionDesc());
                pageOther.setQuestions(questionModels);
                pageModels.add(pageOther);
            }
            questionModels.add(convertDataFromQuestionEntityToQuestionModel(questionEntity));
        }
        surveyModel.setPages(pageModels);
        return surveyModel;
    }

    private QuestionModel convertDataFromQuestionEntityToQuestionModel(Question questionEntity) {
        QuestionModel questionModel = new QuestionModel();
        questionModel.setName(String.valueOf(questionEntity.getId()));
        questionModel.setIsRequired(questionEntity.getIsRequired() == 0 ? false : true);
        questionModel.setTitle(questionEntity.getQuestionDesc());
        questionModel.setType(Common.getQuestionTypes(questionEntity.getQuestionTypeId()));
        questionModel.setFileLocation(questionEntity.getFileLocation());
        questionModel.setFileType(getFileType(questionEntity.getFileLocation()));
        if (questionEntity.getQuestionTypeId() == QuestionTypes.TEXT_BOX.getValue() 
                || questionEntity.getQuestionTypeId() == QuestionTypes.RADIO.getValue()
                || questionEntity.getQuestionTypeId() == QuestionTypes.CHECKBOX.getValue()
                || questionEntity.getQuestionTypeId() == QuestionTypes.CHECKBOX_OTHER.getValue()
                || questionEntity.getQuestionTypeId() == QuestionTypes.COMMENT.getValue() 
                || questionEntity.getQuestionTypeId() == QuestionTypes.EMAIL.getValue()
                || questionEntity.getQuestionTypeId() == QuestionTypes.DROP_DOWN_LIST.getValue()) {
            if (questionEntity.getQuestionTypeId() == QuestionTypes.EMAIL.getValue()) {
                List<ValidatorModel> validatorModels = new ArrayList<>();
                ValidatorModel validatorModel = new ValidatorModel();
                validatorModel.setType("email");
                questionModel.setType("text");
                validatorModels.add(validatorModel);
                questionModel.setValidators(validatorModels);
            } else if (questionEntity.getQuestionTypeId() == QuestionTypes.CHECKBOX_OTHER.getValue()) {
                questionModel.setHasOther(true);
                questionModel.setType("checkbox");
            }
            questionModel.setChoices(convertDataFromAnswerEntityToChoiceModel(questionEntity));
        } else if (questionEntity.getQuestionTypeId() == QuestionTypes.MATRIX.getValue()) {
            questionModel.setIsRequired(false);
            questionModel.setIsAllRowRequired(questionEntity.getIsRequired() == 0 ? false : true);
            questionModel.setColumns(coverDataFromMatrixColumnEntityToColumnModel(questionEntity));
            questionModel.setRows(coverDataFromMatrixRowEntityToColumnModel(questionEntity));
        }
        return questionModel;
    }

    private String getFileType(String fileLocation) {
        if (fileLocation == null || "".equals(fileLocation))
            return null;
        String fileType = null;
        try {
            String extension = fileLocation.substring(fileLocation.lastIndexOf((int) (char) '.') + 1,
                    fileLocation.length());
            if (Common.isContain(extension, Common.ARR_IMAGES_EXTENSION)) {
                fileType = "image";
            } else if (Common.isContain(extension, Common.ARR_AUDIOS_EXTENSION)) {
                fileType = "audio";
            } else if (Common.isContain(extension, Common.ARR_VIDEOS_EXTENSION)) {
                fileType = "video";
            }
        } catch (Exception e) {
            e.printStackTrace();
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

    private List<ColumnModel> coverDataFromMatrixColumnEntityToColumnModel(Question questionEntity) {
        List<ColumnModel> columnModels = new ArrayList<>();
        for (MatrixColumn matrixColumn : questionEntity.getMatrixColumns()) {
            ColumnModel columnModel = new ColumnModel();
            columnModel.setText(matrixColumn.getColumnDesc());
            columnModel.setValue(String.valueOf(matrixColumn.getId()));
            columnModels.add(columnModel);
        }
        return columnModels;
    }

    private List<RowModel> coverDataFromMatrixRowEntityToColumnModel(Question questionEntity) {
        List<RowModel> rowModels = new ArrayList<>();
        for (MatrixRow matrixRow : questionEntity.getMatrixRows()) {
            RowModel rowModel = new RowModel();
            rowModel.setText(matrixRow.getRowDesc());
            rowModel.setValue(String.valueOf(matrixRow.getId()));
            rowModels.add(rowModel);
        }
        return rowModels;
    }

    private List<QuestionModel> setQuestionModelForPage1(Survey surveyEntity) {
        List<QuestionModel> questionModels = new ArrayList<>();
        QuestionModel questionModel = new QuestionModel("info", "html", surveyEntity.getSurveyDesc());
        questionModels.add(questionModel);
        return questionModels;
    }

    @Override
    public ResponseEntity<?> getSurveyList() throws Exception {
        logger.debug("getSurveyList:");
        List<Survey> surveys = surveyRepository.getSurveyList();
        for (Survey survey : surveys) {
            setStatusSurvey(survey);
            try {
                survey.setSurveyType(surveyTypeRepository.getSurveyTypeById(survey.getSurveyTypeId()).getTypeDesc());
            } catch (Exception e) {
                survey.setSurveyType("");
            }
        }
        return Utils.generateSuccessResponseEntity(surveyRepository.getSurveyList());
    }

    @Override
    public ResponseEntity<?> addSurvey(Survey survey) throws AddObjectException, Exception {
        try {
            logger.debug("addSurvey");
            if(survey.getSurveyTypeId() != 1  && survey.getSurveyTypeId() != 2)
                throw new AddObjectException("Cannot create Survey. Cause missing survey type "+survey.getSurveyTypeId());
            
            survey.setBeginingTime(Common.getTimeStampFromString(survey.getBeginingTimeStr(), Common.DATE_FORMAT_DD_MMM_YYYY));
            survey.setEndingTime(Common.getTimeStampFromString(survey.getEndingTimeStr(), Common.DATE_FORMAT_DD_MMM_YYYY));
            surveyRepository.addSurvey(survey);
            if (survey.getSurveyTypeId() == 2) {
                logger.debug("is Pre-Training Suvey");
                SurveySession surveySession = new SurveySession();
                surveySession.setSessionDesc("Thông tin cá nhân");
                surveySessionRepository.addSurveySession(surveySession);
                for (Integer i : convertListStringQuestionDefaltToArray(questionIdSurveyPreTrainingDefault)) {
                    SurveyQuestion surveyQuestion = new SurveyQuestion();
                    surveyQuestion.setQuestionId(i);
                    surveyQuestion.setSurveyId(survey.getId());
                    surveyQuestion.setSurveySessionId(surveySession.getId());
                    surveyQuestionRepository.addSurveyQuestion(surveyQuestion);
                }
            } else {
                logger.debug("is Training Need");
                SurveySession surveySession = new SurveySession();
                surveySession.setSessionDesc("Thông tin chung");
                surveySessionRepository.addSurveySession(surveySession);
                for (Integer i : convertListStringQuestionDefaltToArray(questionIdSurveyTrainingNeedDefault)) {
                    SurveyQuestion surveyQuestion = new SurveyQuestion();
                    surveyQuestion.setQuestionId(i);
                    surveyQuestion.setSurveyId(survey.getId());
                    surveyQuestion.setSurveySessionId(surveySession.getId());
                    surveyQuestionRepository.addSurveyQuestion(surveyQuestion);
                }
            }
            return Utils.generateSuccessResponseEntity(survey);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("AddObjectException - Cannot create new SurveyObject");
            return Utils.generateFailureResponseEntity(ErrorCode.CODE_ADD_OBJECT_EXCEPTION,
                    ErrorCode.MSG_ADD_OBJECT_EXCEPTION);
        }
    }

    @Override
    public ResponseEntity<?> editServeyInfo(Survey survey)
            throws ObjectNotFoundException, EditObjectException, Exception {
        try {
            logger.debug("editServeyInfo");
            
            Survey surveyCheck = surveyRepository.getSurveyById(survey.getId());
            if (surveyCheck == null)
                throw new ObjectNotFoundException("Survey with id = " + survey.getId() + " cannot found");

            surveyCheck.setBeginingTime(Common.getTimeStampFromString(survey.getBeginingTimeStr(), Common.DATE_FORMAT_DD_MMM_YYYY));
            surveyCheck.setEndingTime(Common.getTimeStampFromString(survey.getEndingTimeStr(), Common.DATE_FORMAT_DD_MMM_YYYY));
            surveyCheck.setSubDesc(survey.getSubDesc());
            surveyCheck.setSurveyTitle(survey.getSurveyTitle());
            surveyCheck.setSurveyDesc(survey.getSurveyDesc());
            surveyRepository.editSurvey(surveyCheck);

            return Utils.generateSuccessResponseEntity(survey);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("AddObjectException - Cannot update SurveyObject");
            return Utils.generateFailureResponseEntity(ErrorCode.CODE_EDIT_OBJECT_EXCEPTION,
                    ErrorCode.MSG_EDIT_OBJECT_EXCEPTION);
        }
    }

    @Override
    public ResponseEntity<?> cloneSurvey(int surveyId) throws Exception {

        Survey survey = surveyRepository.getSurveyById(surveyId);

        if (survey == null)
            throw new ObjectNotFoundException(
                    "Cannot clone Survey cause Survey with id =" + surveyId + " cannot found");

        Survey surveyAdd = new Survey();
        surveyAdd.setBeginingTime(survey.getBeginingTime());
        surveyAdd.setEndingTime(survey.getEndingTime());
        surveyAdd.setSurveyDesc(survey.getSurveyDesc());
        surveyAdd.setSubDesc(survey.getSubDesc());
        surveyAdd.setSurveyTitle(survey.getSurveyTitle() + " (clone)");
        surveyAdd.setSurveyTypeId(survey.getSurveyTypeId());
        surveyAdd.setUserId(survey.getUserId());
        surveyAdd.setIsSent(0);
        surveyRepository.addSurvey(surveyAdd);

        List<SurveySession> sessions = surveySessionRepository.getSurveySessionListBySurveyId(surveyId);
        for (SurveySession surveySession : sessions) {
            surveySession.setQuestions(questionRepository.getQuestionListBySurveySessionId(surveySession.getId()));
        }

        int indexQuestion = 1;
        for (SurveySession surveySession : sessions) {
            SurveySession surveySessionAdd = new SurveySession();
            surveySessionAdd.setSessionDesc(surveySession.getSessionDesc());
            surveySessionRepository.addSurveySession(surveySessionAdd);
            for (Question question : surveySession.getQuestions()) {
                SurveyQuestion surveyQuestionAdd = new SurveyQuestion();
                surveyQuestionAdd.setQuestionId(question.getId());
                surveyQuestionAdd.setSurveyId(surveyAdd.getId());
                surveyQuestionAdd.setSurveySessionId(surveySessionAdd.getId());
                surveyQuestionAdd.setIndexQuestion(indexQuestion);
                surveyQuestionRepository.addSurveyQuestion(surveyQuestionAdd);
                indexQuestion++;
            }
        }

        return Utils.generateSuccessResponseEntity(surveyAdd);
    }

    @Override
    public ResponseEntity<?> getSurveyInfor(int surveyId) throws ObjectNotFoundException, Exception {
        logger.debug("getSurveyInfor");
        Survey survey = surveyRepository.getSurveyById(surveyId);
        if (survey == null) {
            logger.error("ObjectNotFoundException - Survey cannot be found with id =" + surveyId);
            throw new ObjectNotFoundException("Survey cannot be found");
        }
        setStatusSurvey(survey);
        return Utils.generateSuccessResponseEntity(survey);
    }

    @Override
    public ResponseEntity<?> genURL(int surveyId) throws Exception {
        Survey survey = surveyRepository.getSurveyById(surveyId);
        if (survey == null)
            throw new ObjectNotFoundException("Survey not found");
        String idEncoded = Utils.encode(System.currentTimeMillis() + "_" + surveyId);
        String url = Common.PATH_TRAINING_NEED_SURVEY + idEncoded;
        if (survey.getSurveyTypeId() == 1)
            url = Common.PATH_TRAINING_NEED_SURVEY + idEncoded;
        else if (survey.getSurveyTypeId() == 2)
            url = Common.PATH_PRE_TRAINING_SURVEY + idEncoded;

        return Utils.generateSuccessResponseEntity(url);
    }

    @Override
    public ResponseEntity<?> deleteSurvey(int surveyId) throws DeleteObjectException, Exception {
        try {
            Survey survey = surveyRepository.getSurveyById(surveyId);
            if (survey == null)
                throw new DeleteObjectException("Cannot delete Survey. Cause not found Survey with id = " + surveyId);

            if (survey.getIsSent() == 1 && !isOutOfExpDate(survey.getEndingTime()))
                throw new DeleteObjectException("Cannot delete Survey. Cause survey was sent and not out of expried date yet");

            deleteSurvey(survey);
            return Utils.generateSuccessResponseEntity(survey);
        } catch (Exception e) {
            e.printStackTrace();
            return Utils.generateFailureResponseEntity(ErrorCode.CODE_DELETE_OBJECT_EXCEPTION,
                    ErrorCode.MSG_DELETE_OBJECT_EXCEPTION);
        }
    }

    @Override
    public ResponseEntity<?> deleteSurvey(List<Survey> surveys) throws DeleteObjectException, Exception {
        try {
            List<Survey> surveyReturn = new ArrayList<>();
            for (Survey surveyController : surveys) {
                int surveyId = surveyController.getId();
                Survey survey = surveyRepository.getSurveyById(surveyId);
                if (survey == null) {
                    logger.debug("Cannot delete Survey. Cause not found Survey with id = " + surveyId);
                    continue;
                }

                if (survey.getIsSent() == 1 && !isOutOfExpDate(survey.getEndingTime())) {
                    logger.debug("Cannot delete Survey. Cause survey was sent (with id = "+surveyId+") and not out of expried date yet" );
                    surveyReturn.add(survey);
                    continue;
                }

                deleteSurvey(survey);
            }

            return Utils.generateSuccessResponseEntity(surveyReturn);
        } catch (Exception e) {
            e.printStackTrace();
            return Utils.generateFailureResponseEntity(ErrorCode.CODE_DELETE_OBJECT_EXCEPTION,
                    ErrorCode.MSG_DELETE_OBJECT_EXCEPTION);
        }
    }

    private void deleteSurvey(Survey survey) throws Exception {
        int surveyId = survey.getId();
        surveyRepository.deleteSurvey(survey);

        List<Integer> listSurveySessionId = surveyQuestionRepository.getSurveySessionIdListBySurveyId(surveyId);
        for (Integer surveySessionId : listSurveySessionId) {
            SurveySession surveySession = new SurveySession();
            surveySession.setId(surveySessionId);
            surveySessionRepository.deleteSurveySession(surveySession);
        }

        surveyQuestionRepository.deleteSurveyQuestionBySurveyId(surveyId);
        surveyResultRepository.deleteSurveyResultBySurveyId(surveyId);
        resultRepository.deleteResultBySurveyId(surveyId);
    }

    @Override
    public ResponseEntity<?> editServeyFull(Survey survey) throws EditObjectException, Exception {
        try {
            editSurvey(survey);
            for (SurveySession surveySession : survey.getSurveySessions()) {
                if (surveySession.getId() != 0) {
                    editSurveySession(surveySession);
                } else {
                    addNewSurveySession(survey.getId(), surveySession);
                }
            }
            setIndexQuestion(survey.getId(), survey.getSurveySessions());
            return Utils.generateSuccessResponseEntity(survey);
        } catch (Exception e) {
            e.printStackTrace();
            return Utils.generateFailureResponseEntity(ErrorCode.CODE_EDIT_OBJECT_EXCEPTION,
                    ErrorCode.MSG_EDIT_OBJECT_EXCEPTION);
        }

    }

    private void editSurvey(Survey survey) throws ObjectNotFoundException, ParseException {
        Survey surveyCheck = surveyRepository.getSurveyById(survey.getId());
        if (surveyCheck == null)
            throw new ObjectNotFoundException("Survey with id = " + survey.getId() + " cannot found");

        surveyCheck.setBeginingTime(Common.getTimeStampFromString(survey.getBeginingTimeStr(), Common.DATE_FORMAT_DD_MMM_YYYY));
        surveyCheck.setEndingTime(Common.getTimeStampFromString(survey.getEndingTimeStr(), Common.DATE_FORMAT_DD_MMM_YYYY));
        surveyCheck.setSubDesc(survey.getSubDesc());
        surveyCheck.setSurveyTitle(survey.getSurveyTitle());
        surveyCheck.setSurveyDesc(survey.getSurveyDesc());
        surveyRepository.editSurvey(surveyCheck);

    }

    private void editSurveySession(SurveySession surveySession) throws ObjectNotFoundException {
        SurveySession surveySessionCheck = surveySessionRepository.getSurveySessionById(surveySession.getId());
        if (surveySessionCheck == null)
            throw new ObjectNotFoundException("Session cannot found with id = " + surveySession.getId());

        surveySessionCheck.setSessionDesc(surveySession.getSessionDesc());
        surveySessionRepository.editSurveySession(surveySessionCheck);
    }

    protected void addNewSurveySession(int surveyId, SurveySession surveySession)
            throws AddObjectException, QuestionExistException {
        surveySessionRepository.addSurveySession(surveySession);
        for (Question question : surveySession.getQuestions()) {
            if (question.getId() == 0) {
                addSurveyQuestionByNewQuestion(surveyId, surveySession.getId(), question);
            } else {
                addSurveyQuestionByOldQuestion(surveyId, surveySession.getId(), question);
            }
        }
    }

    protected void addSurveyQuestionByNewQuestion(int surveyId, int surveySessionId, Question question)
            throws AddObjectException {
        try {
            question.setIsQuiz(0);
            questionRepository.addQuestion(question);
            question.setQuestionType(Common.getQuestionTypes(question.getQuestionTypeId()));

            int questionId = question.getId();

            if (question.getQuestionTypeId() == QuestionTypes.RADIO.getValue() 
                    || question.getQuestionTypeId() == QuestionTypes.CHECKBOX.getValue()
                    || question.getQuestionTypeId() == QuestionTypes.CHECKBOX_OTHER.getValue() 
                    || question.getQuestionTypeId() == QuestionTypes.DROP_DOWN_LIST.getValue()) {
                List<Answer> answers = question.getAnswers();
                if (answers != null)
                    for (Answer answer : answers) {
                        answer.setId(0);
                        answer.setQuestionId(questionId);
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
                } else {
                    
                }

            } else if (question.getQuestionTypeId() == QuestionTypes.MATRIX.getValue()) {
                List<MatrixColumn> matrixColumns = question.getMatrixColumns();
                if (matrixColumns != null)
                    for (MatrixColumn matrixColumn : matrixColumns) {
                        matrixColumn.setId(0);
                        matrixColumn.setQuestionId(questionId);
                        matrixColumnRepository.addMatrixColumn(matrixColumn);
                    }
                List<MatrixRow> matrixRows = question.getMatrixRows();
                if (matrixRows != null)
                    for (MatrixRow matrixRow : matrixRows) {
                        matrixRow.setId(0);
                        matrixRow.setQuestionId(questionId);
                        matrixRowRepository.addMatrixRow(matrixRow);
                    }
            }

            SurveyQuestion surveyQuestion = new SurveyQuestion();
            surveyQuestion.setSurveyId(surveyId);
            surveyQuestion.setSurveySessionId(surveySessionId);
            surveyQuestion.setQuestionId(questionId);
            surveyQuestion.setIndexQuestion(question.getNo());
            surveyQuestionRepository.addSurveyQuestion(surveyQuestion);
        } catch (Exception e) {
            e.printStackTrace();
            throw new AddObjectException("Question cannot add");
        }
    }

    protected void addSurveyQuestionByOldQuestion(int surveyId, int surveySessionId, Question question)
            throws AddObjectException, QuestionExistException {

        Question questionEntity = questionRepository.getQuestionById(question.getId());
        if (questionEntity == null)
            throw new AddObjectException("SurveyQuestion cannot add, cause Question cannot found");

        SurveyQuestion surveyQuestionCheckExist = surveyQuestionRepository
                .getSurveyQuestionBySurveyIdAndQuestionId(surveyId, question.getId());
        if (surveyQuestionCheckExist != null)
            throw new QuestionExistException("Question is exist in survey");

        SurveyQuestion surveyQuestion = new SurveyQuestion();
        surveyQuestion.setSurveyId(surveyId);
        surveyQuestion.setSurveySessionId(surveySessionId);
        surveyQuestion.setQuestionId(question.getId());
        surveyQuestion.setIndexQuestion(question.getNo());
        surveyQuestionRepository.addSurveyQuestion(surveyQuestion);

        questionEntity.setQuestionType(Common.getQuestionTypes(question.getQuestionTypeId()));
    }
    
    private void setIndexQuestion(int surveyId, List<SurveySession> surveySessions){
        int indexQuestion = 1;
        for(SurveySession surveySession : surveySessions){
            for(Question question : surveySession.getQuestions()){
                SurveyQuestion surveyQuestion = surveyQuestionRepository.getSurveyQuestionBySurveyIdAndQuestionId(surveyId, question.getId());
                surveyQuestion.setIndexQuestion(indexQuestion);
                surveyQuestionRepository.editSurveyQuestion(surveyQuestion);
                indexQuestion++;
            }
        }
    }

    @Override
    public ResponseEntity<?> getSurveyFull(int surveyId) throws ObjectNotFoundException, Exception {
        try {
            logger.debug("getSurveyInfor");
            Survey survey = surveyRepository.getSurveyById(surveyId);
            if (survey == null) {
                logger.error("ObjectNotFoundException - Survey cannot be found with id =" + surveyId);
                throw new ObjectNotFoundException("Survey cannot be found");
            }
            setStatusSurvey(survey);
            List<Integer> listSurveySessionId = surveyQuestionRepository.getSurveySessionIdListBySurveyId(surveyId);
            List<SurveySession> sessions = new ArrayList<>();
            for (Integer surveySessionId : listSurveySessionId) {
                SurveySession surveySession = surveySessionRepository.getSurveySessionById(surveySessionId);
                surveySession.setQuestions(questionRepository.getQuestionListBySurveySessionId(surveySessionId));
                for (Question question : surveySession.getQuestions()) {
                    int questionId = question.getId();
                    if (question.getQuestionTypeId() == QuestionTypes.RADIO.getValue() 
                            || question.getQuestionTypeId() == QuestionTypes.CHECKBOX.getValue()
                            || question.getQuestionTypeId() == QuestionTypes.CHECKBOX_OTHER.getValue() 
                            || question.getQuestionTypeId() == QuestionTypes.DROP_DOWN_LIST.getValue()) {
                        question.setAnswers(answerRepository.getAnswerListByQuestionId(questionId));
                    } else if (question.getQuestionTypeId() == QuestionTypes.MATRIX.getValue()) {
                        question.setMatrixRows(matrixRowRepository.getMatrixRowListByQuestionId(questionId));
                        question.setMatrixColumns(matrixColumnRepository.getMatrixColumnListByQuestionId(questionId));
                    }
                }
                sessions.add(surveySession);
            }

            survey.setSurveySessions(sessions);

            return Utils.generateSuccessResponseEntity(survey);
        } catch (Exception e) {
            e.printStackTrace();
            return Utils.generateFailureResponseEntity(ErrorCode.CODE_OBJECT_NOT_FOUND_EXCEPTION,
                    ErrorCode.MSG_OBJECT_NOT_FOUND_EXCEPTION);
        }
    }

    @Override
    public ResponseEntity<?> createSurveyFull(SurveyModelForCreate surveyModelForCreate)
            throws AddObjectException, Exception {
        try {
            if(surveyModelForCreate.getSurveyInfo().getSurveyTypeId() != 1  && surveyModelForCreate.getSurveyInfo().getSurveyTypeId() != 2)
                throw new AddObjectException("Cannot create Survey. Cause missing survey type "+surveyModelForCreate.getSurveyInfo().getSurveyTypeId());
            
            Survey survey = new Survey();
            survey.setBeginingTime(Common.getTimeStampFromString(surveyModelForCreate.getSurveyInfo().getBeginingTimeStr(), Common.DATE_FORMAT_DD_MMM_YYYY));
            survey.setEndingTime(Common.getTimeStampFromString(surveyModelForCreate.getSurveyInfo().getEndingTimeStr(), Common.DATE_FORMAT_DD_MMM_YYYY));
            survey.setSubDesc(surveyModelForCreate.getSurveyInfo().getSubDesc());
            survey.setSurveyTitle(surveyModelForCreate.getSurveyInfo().getSurveyTitle());
            survey.setSurveyTypeId(surveyModelForCreate.getSurveyInfo().getSurveyTypeId());
            survey.setSurveyDesc(surveyModelForCreate.getSurveyInfo().getSurveyDesc());
            surveyRepository.addSurvey(survey);
            int surveyId = survey.getId();

            SurveySession surveySession = null;
            List<Question> questionFirsts = new ArrayList<>();
            
            if (survey.getSurveyTypeId() == 2) {
                logger.debug("is Pre-Training Suvey");
                surveySession = new SurveySession();
                surveySession.setSessionDesc("Thông tin cá nhân");
                surveySessionRepository.addSurveySession(surveySession);
                
                for (Integer i : convertListStringQuestionDefaltToArray(questionIdSurveyPreTrainingDefault)) {
                    SurveyQuestion surveyQuestion = new SurveyQuestion();
                    surveyQuestion.setQuestionId(i);
                    surveyQuestion.setSurveyId(survey.getId());
                    surveyQuestion.setSurveySessionId(surveySession.getId());
                    surveyQuestionRepository.addSurveyQuestion(surveyQuestion);
                    
                    Question question = new Question();
                    question.setId(i);
                    questionFirsts.add(question);
                }
                surveySession.setQuestions(questionFirsts);
            } else {
                logger.debug("is Training Need Survey");
                surveySession = new SurveySession();
                surveySession.setSessionDesc("Thông tin chung");
                surveySessionRepository.addSurveySession(surveySession);
                
                for (Integer i : convertListStringQuestionDefaltToArray(questionIdSurveyTrainingNeedDefault)) {
                    SurveyQuestion surveyQuestion = new SurveyQuestion();
                    surveyQuestion.setQuestionId(i);
                    surveyQuestion.setSurveyId(survey.getId());
                    surveyQuestion.setSurveySessionId(surveySession.getId());
                    surveyQuestionRepository.addSurveyQuestion(surveyQuestion);
                    
                    Question question = new Question();
                    question.setId(i);
                    questionFirsts.add(question);
                }
                surveySession.setQuestions(questionFirsts);
            }

            for (SurveySession surveySessionAdd : surveyModelForCreate.getParts()) {
                addNewSurveySession(surveyId, surveySessionAdd);
            }
            
            List<SurveySession> surveySessionListToEditIndex = surveyModelForCreate.getParts();
            surveySessionListToEditIndex.add(0, surveySession);
            setIndexQuestion(surveyId, surveySessionListToEditIndex);

            return Utils.generateSuccessResponseEntity(survey);
        } catch (Exception e) {
            e.printStackTrace();
            return Utils.generateFailureResponseEntity(ErrorCode.CODE_ADD_OBJECT_EXCEPTION,
                    ErrorCode.MSG_ADD_OBJECT_EXCEPTION);
        }
    }
    
    private void setStatusSurvey(Survey survey){
        survey.setStatus("NEW");
        Timestamp toDate = new Timestamp(System.currentTimeMillis());
        if(survey.getIsSent() == 1) survey.setStatus("SENT");
        if (toDate.after(Common.setTimeToMidnight(survey.getEndingTime())))
            survey.setStatus("EXPRIED");
    }
    
    private boolean isOutOfExpDate(Timestamp endDate){
        Timestamp toDate = new Timestamp(System.currentTimeMillis());
        return toDate.after(Common.setTimeToMidnight(endDate));
    }

    public ArrayList<Integer> convertListStringQuestionDefaltToArray(String str){
        try {
            String[] arrStr = str.split(",");
            ArrayList<Integer> arrayList = new ArrayList<>();
            for(String value : arrStr){
                arrayList.add(Integer.parseInt(value));
            }
            return arrayList;
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }
}
