package com.amway.lms.backend.service.impl;

import java.io.IOException;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.transaction.Transactional;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

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
import com.amway.lms.backend.exception.ImportSurveyException;
import com.amway.lms.backend.exception.QuestionExistException;
import com.amway.lms.backend.repository.AnswerRepository;
import com.amway.lms.backend.repository.CategoryRepository;
import com.amway.lms.backend.repository.MatrixColumnRepository;
import com.amway.lms.backend.repository.MatrixRowRepository;
import com.amway.lms.backend.repository.QuestionRepository;
import com.amway.lms.backend.repository.QuestionTypeRepository;
import com.amway.lms.backend.repository.SurveyQuestionRepository;
import com.amway.lms.backend.repository.SurveyRepository;
import com.amway.lms.backend.repository.SurveyResultRepository;
import com.amway.lms.backend.repository.SurveySessionRepository;
import com.amway.lms.backend.repository.SurveyTypeRepository;
import com.amway.lms.backend.service.ImportSurveyService;

/**
 * @author acton
 * @email acton@enclave.vn
 */

@Service
@Transactional
public class ImportSurveyServiceImpl implements ImportSurveyService {

    private static final Logger logger = LoggerFactory.getLogger(ImportSurveyServiceImpl.class);
    
    @Value("${question.id.survey.training.need.default}")
    private String questionIdSurveyTrainingNeedDefault;
    
    @Value("${question.id.survey.pre.training.default}")
    private String questionIdSurveyPreTrainingDefault;
    
    private static final String DATE_FORMAT = "dd/MMM/yyyy";

    private static final String MSG_SURVEY_TITLE_EMPTY = "Survey title cannot empty";
    private static final String MSG_SURVEY_DESC_EMPTY = "Survey description cannot empty";
    private static final String MSG_SURVEY_TYPE_MISSING = "Survey type is not correct";
    private static final String MSG_SURVEY_END_DATE_CANNOT_PAST = "End date should be after to date";
    private static final String MSG_SURVEY_START_LAGER_END_DATE = "Start Date cannot after End Date";
    private static final String MSG_SURVEY_NOT_HAVE_PART = "Survey don't have any part";
    private static final String MSG_PART_TITLE_EMPTY = "Part title cannot empty";
    private static final String MSG_PART_NOT_HAVE_QUESTION = "Part don't have any question";
    private static final String MSG_QUESTION_DES_EMPTY = "Question description cannot empty";
    private static final String MSG_QUESTION_NOT_HAVE_ANSWER = "Question don't have any answer";
    private static final String MSG_QUESTION_NOT_HAVE_ROW = "Question matrix don't have any row";
    private static final String MSG_QUESTION_NOT_HAVE_COLUMN = "Question matrix don't have any column";

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

    @Override
    public ResponseEntity<?> importSurvey(MultipartFile surveyFile)
            throws IOException, ImportSurveyException, Exception {
        try {
            if (surveyFile == null)
                throw new ImportSurveyException("File is null");
            logger.debug("========================Survey file name: " + surveyFile.getOriginalFilename() + " | Size: "
                    + (double) surveyFile.getSize() / 1000 + "KB");
            String extension = Common.getExtension(surveyFile.getOriginalFilename());

            Workbook workbook = null;
            if ("xls".equals(extension)) {
                workbook = new HSSFWorkbook(surveyFile.getInputStream());
            } else {
                workbook = new XSSFWorkbook(surveyFile.getInputStream());
            }

            logger.debug("========================Sheet name: " + workbook.getSheetAt(0).getSheetName());
            Sheet sheet = workbook.getSheetAt(0);

            Survey survey = new Survey();
            setSurveyInformation(sheet, survey);
            List<SurveySession> surveySessions = new ArrayList<>();
            survey.setSurveySessions(surveySessions);
            if(survey.getSurveyTypeId() == 1) setQuestionDefaulForSurveyTraininNeed(survey);
            else setQuestionDefaulForSurveyPreTraining(survey);
            setSurveySessionFromSheet(sheet, survey);

            validateSurvey(survey);

            surveyRepository.addSurvey(survey);
            addNewSurveySession(survey);

            workbook.close();
            return Utils.generateSuccessResponseEntity(survey);
        } catch (ImportSurveyException e) {
            e.printStackTrace();
            return Utils.generateFailureResponseEntity(ErrorCode.CODE_SURVEY_CANNOT_IMPORT_EXCEPTION, e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            return Utils.generateFailureResponseEntity(ErrorCode.CODE_SURVEY_CANNOT_IMPORT_EXCEPTION,
                    ErrorCode.MSG_SURVEY_CANNOT_IMPORT_EXCEPTION);
        }

    }

    private void setSurveyInformation(Sheet sheet, Survey survey) {
        survey.setSurveyTypeId(1);
        for (int i = 0; i <= sheet.getLastRowNum(); i++) {
            Row row = sheet.getRow(i);
            if (row == null)
                continue;
            if ("Survey title".equals(getCellContent(row, 0)))
                survey.setSurveyTitle(getCellContent(row, 1));
            else if ("Survey description".equals(getCellContent(row, 0)))
                survey.setSurveyDesc(getCellContent(row, 1));
            else if ("Start date".equals(getCellContent(row, 0)))
                survey.setBeginingTime(getTimeStampByString(getCellContent(row, 1)));
            else if ("End date".equals(getCellContent(row, 0)))
                survey.setEndingTime(getTimeStampByString(getCellContent(row, 1)));
            if ("Survey Body".equals(getCellContent(row, 0)) || "Part Title".equals(getCellContent(row, 0)))
                break;
        }
    }

    private void setQuestionDefaulForSurveyPreTraining(Survey survey) {
        logger.debug("is Pre-Training Suvey");
        SurveySession surveySession = new SurveySession();
        surveySession.setSessionDesc("Thông tin cá nhân");
        List<Question> questions = new ArrayList<Question>();
        for (Integer i : convertListStringQuestionDefaltToArray(questionIdSurveyPreTrainingDefault)) {
            Question question = new Question();
            question.setId(i);
            questions.add(question);
        }
        surveySession.setQuestions(questions);
        survey.getSurveySessions().add(surveySession);
    }

    private void setQuestionDefaulForSurveyTraininNeed(Survey survey) {
        logger.debug("is Training Need Suvey");
        SurveySession surveySession = new SurveySession();
        surveySession.setSessionDesc("Thông tin chung");
        List<Question> questions = new ArrayList<Question>();
        for (Integer i : convertListStringQuestionDefaltToArray(questionIdSurveyTrainingNeedDefault)) {
            Question question = new Question();
            question.setId(i);
            questions.add(question);
        }
        surveySession.setQuestions(questions);
        survey.getSurveySessions().add(surveySession);

    }

    private void setSurveySessionFromSheet(Sheet sheet, Survey survey) {
        for (int i = 6; i <= sheet.getLastRowNum(); i++) {
            Row row = sheet.getRow(i);

            if (row == null)
                continue;

            if ("Part Title".equals(getCellContent(row, 0))) {
                SurveySession surveySession = new SurveySession();
                surveySession.setSessionDesc(getCellContent(row, 1));
                survey.getSurveySessions().add(surveySession);
                List<Question> questions = new ArrayList<>();
                surveySession.setQuestions(questions);
            } else if ("No.".equals(getCellContent(row, 0))) {
                continue;
            } else {
                if (row.getCell(0) == null || row.getCell(0).getCellType() == Cell.CELL_TYPE_BLANK)
                    continue;
                else {
                    if (survey.getSurveySessions() != null && survey.getSurveySessions().size() > 0) {
                        SurveySession surveySession = survey.getSurveySessions()
                                .get(survey.getSurveySessions().size() - 1);
                        setQuestionFromRow(row, surveySession);
                    }
                }
            }
        }
    }

    private void setQuestionFromRow(Row row, SurveySession surveySession) {
        try {
            List<Question> questions = surveySession.getQuestions();
            Question question = new Question();
            question.setQuestionDesc(getCellContent(row, 1));
            question.setIsQuiz(0);
            question.setNo(Integer.valueOf(getCellContent(row, 0)));
            if ("yes".equals(getCellContent(row, 2).toLowerCase()))
                question.setIsRequired(1);
            else
                question.setIsRequired(0);

            String questionType = getCellContent(row, 3).toLowerCase();
            if ("text box".equals(questionType))
                question.setQuestionTypeId(1);
            else if ("radio".equals(questionType))
                question.setQuestionTypeId(2);
            else if ("check box".equals(questionType))
                question.setQuestionTypeId(3);
//            else if ("check box has other".equals(questionType))
//                question.setQuestionTypeId(4);
//            else if ("comment".equals(questionType))
//                question.setQuestionTypeId(5);
//            else if ("email".equals(questionType))
//                question.setQuestionTypeId(6);
//            else if ("drop down list".equals(questionType))
//                question.setQuestionTypeId(7);
            else if ("matrix".equals(questionType))
                question.setQuestionTypeId(11);
            else
                return;

            setAnswerFromRow(row, question);

            questions.add(question);
            surveySession.setQuestions(questions);
        } catch (Exception e) {
            logger.error("ERR setQuestionFromRow: "+e.getMessage());
        }
    }

    private void setAnswerFromRow(Row row, Question question) {
        List<Answer> answers = new ArrayList<>();
        List<MatrixRow> matrixRows = new ArrayList<>();
        List<MatrixColumn> matrixColumns = new ArrayList<>();

        for (int j = 4; j < row.getLastCellNum(); j++) {
            if (row.getCell(j) == null || row.getCell(j).getCellType() == Cell.CELL_TYPE_BLANK)
                continue;
            if (question.getQuestionTypeId() != 11) {
                Answer answer = new Answer();
                answer.setAnswerDesc(getCellContent(row, j));
                answers.add(answer);
            } else {
                String desc = getCellContent(row, j);

                if ("Row:".equals(desc.substring(0, 4))) {
                    MatrixRow matrixRow = new MatrixRow();
                    matrixRow.setRowDesc(desc.substring(4, desc.length()).trim());
                    matrixRows.add(matrixRow);
                } else if ("Column:".equals(desc.substring(0, 7))) {
                    MatrixColumn matrixColumn = new MatrixColumn();
                    matrixColumn.setColumnDesc(desc.substring(7, desc.length()).trim());
                    matrixColumns.add(matrixColumn);
                }

            }
        }
        question.setAnswers(answers);
        question.setMatrixColumns(matrixColumns);
        question.setMatrixRows(matrixRows);
    }

    private String getCellContent(Row row, int cellIndex) {
        try {
            if (row == null)
                return null;
            int cellType = row.getCell(cellIndex).getCellType();
            if (cellType == Cell.CELL_TYPE_STRING)
                return row.getCell(cellIndex).getStringCellValue().trim();
            else if (cellType == Cell.CELL_TYPE_NUMERIC)
                return String.valueOf((int) row.getCell(cellIndex).getNumericCellValue());
            else
                return "";
        } catch (Exception e) {
            logger.error("Error from getCellContent: " + e.getMessage());
            return null;
        }
    }

    private String getCellContent(Sheet sheet, int rowIndex, int columnIndex) {
        Row row = sheet.getRow(rowIndex);
        return getCellContent(row, columnIndex);
    }

    private Timestamp getTimeStampByString(String dateString) {
        try {
            DateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT);
            Date date = dateFormat.parse(dateString);
            long time = date.getTime();
            return new Timestamp(time);
        } catch (Exception e) {
            e.printStackTrace();
            return new Timestamp(System.currentTimeMillis());
        }
    }

    protected void addNewSurveySession(Survey survey)
            throws AddObjectException, QuestionExistException {
        int indexQuestion = 1;
//        if(survey.getSurveyTypeId() == 1)
//            indexQuestion = convertListStringQuestionDefaltToArray(questionIdSurveyTrainingNeedDefault).size() + 1;
//        else
//            indexQuestion = convertListStringQuestionDefaltToArray(questionIdSurveyPreTrainingDefault).size() + 1;
        
        for(SurveySession surveySession : survey.getSurveySessions()){
            surveySessionRepository.addSurveySession(surveySession);
            for (Question question : surveySession.getQuestions()) {
                if (question.getId() == 0) {
                    addSurveyQuestionByNewQuestion(survey.getId(), surveySession.getId(), question, indexQuestion);
                } else {
                    addSurveyQuestionByOldQuestion(survey.getId(), surveySession.getId(), question, indexQuestion);
                }
                indexQuestion++;
            }
        }
        
    }

    protected void addSurveyQuestionByNewQuestion(int surveyId, int surveySessionId, Question question, int indexQuestion)
            throws AddObjectException {
        try {
            question.setId(0);
            question.setIsQuiz(0);
            questionRepository.addQuestion(question);
            question.setQuestionType(questionTypeRepository.getQuestionTypeNameById(question.getQuestionTypeId()));

            int questionId = question.getId();

            if (question.getQuestionTypeId() == 2 || question.getQuestionTypeId() == 3
                    || question.getQuestionTypeId() == 4 || question.getQuestionTypeId() == 7) {
                List<Answer> answers = question.getAnswers();
                if (answers != null)
                    for (Answer answer : answers) {
                        answer.setQuestionId(questionId);
                        answerRepository.addAnswer(answer);
                    }
            } else if (question.getQuestionTypeId() == 1 || question.getQuestionTypeId() == 5
                    || question.getQuestionTypeId() == 6) {
                List<Answer> answers = question.getAnswers();
                if (answers != null && !answers.isEmpty()) {
                    Answer answer = answers.get(0);
                    answer.setQuestionId(questionId);
                    answerRepository.addAnswer(answer);
                }

            } else if (question.getQuestionTypeId() == 11) {
                List<MatrixColumn> matrixColumns = question.getMatrixColumns();
                if (matrixColumns != null)
                    for (MatrixColumn matrixColumn : matrixColumns) {
                        matrixColumn.setQuestionId(questionId);
                        matrixColumnRepository.addMatrixColumn(matrixColumn);
                    }
                List<MatrixRow> matrixRows = question.getMatrixRows();
                if (matrixRows != null)
                    for (MatrixRow matrixRow : matrixRows) {
                        matrixRow.setQuestionId(questionId);
                        matrixRowRepository.addMatrixRow(matrixRow);
                    }
            }

            SurveyQuestion surveyQuestion = new SurveyQuestion();
            surveyQuestion.setSurveyId(surveyId);
            surveyQuestion.setSurveySessionId(surveySessionId);
            surveyQuestion.setQuestionId(questionId);
            surveyQuestion.setIndexQuestion(indexQuestion);
            surveyQuestionRepository.addSurveyQuestion(surveyQuestion);
        } catch (Exception e) {
            e.printStackTrace();
            throw new AddObjectException("Question cannot add");
        }
    }

    protected void addSurveyQuestionByOldQuestion(int surveyId, int surveySessionId, Question question, int indexQuestion)
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
        surveyQuestion.setIndexQuestion(indexQuestion);
        surveyQuestionRepository.addSurveyQuestion(surveyQuestion);

        questionEntity
                .setQuestionType(questionTypeRepository.getQuestionTypeNameById(questionEntity.getQuestionTypeId()));
    }

    private boolean isNullOrEmpty(String str) {
        return (str == null || "".equals(str));
    }

    private void validateSurvey(Survey survey) throws ImportSurveyException {
        if (isNullOrEmpty(survey.getSurveyTitle()))
            throw new ImportSurveyException(MSG_SURVEY_TITLE_EMPTY);
        if (isNullOrEmpty(survey.getSurveyDesc()))
            throw new ImportSurveyException(MSG_SURVEY_DESC_EMPTY);
        if(survey.getSurveyTypeId() != 1 && survey.getSurveyTypeId() != 2)
            throw new ImportSurveyException(MSG_SURVEY_TYPE_MISSING);
        if (survey.getEndingTime().before(new Timestamp(System.currentTimeMillis())))
            throw new ImportSurveyException(MSG_SURVEY_END_DATE_CANNOT_PAST);
        if (Common.setTimeToZero(survey.getBeginingTime()).after(Common.setTimeToMidnight(survey.getEndingTime())))
            throw new ImportSurveyException(MSG_SURVEY_START_LAGER_END_DATE);
        validateSurveySession(survey);
    }

    private void validateSurveySession(Survey survey) throws ImportSurveyException {
        if (survey.getSurveySessions() == null || survey.getSurveySessions().size() == 0)
            throw new ImportSurveyException(MSG_SURVEY_NOT_HAVE_PART);
        for (SurveySession surveySession : survey.getSurveySessions()) {
            if (isNullOrEmpty(surveySession.getSessionDesc()))
                throw new ImportSurveyException(MSG_PART_TITLE_EMPTY);
            validateQuestion(surveySession);
        }
    }

    private void validateQuestion(SurveySession surveySession) throws ImportSurveyException {
        if (surveySession.getQuestions() == null || surveySession.getQuestions().size() == 0)
            throw new ImportSurveyException(MSG_PART_NOT_HAVE_QUESTION);
        for (Question question : surveySession.getQuestions()) {
            if(question.getId() != 0) continue;
            
            if (isNullOrEmpty(question.getQuestionDesc()))
                throw new ImportSurveyException(MSG_QUESTION_DES_EMPTY + " (No." + question.getNo() + ")");
            if (question.getQuestionTypeId() == 2 || question.getQuestionTypeId() == 3
                    || question.getQuestionTypeId() == 4 || question.getQuestionTypeId() == 7) {
                if (question.getAnswers() == null || question.getAnswers().size() == 0)
                    throw new ImportSurveyException(MSG_QUESTION_NOT_HAVE_ANSWER + " (No." + question.getNo() + ")");
            } else if (question.getQuestionTypeId() == 11) {
                if (question.getMatrixRows() == null || question.getMatrixRows().size() == 0)
                    throw new ImportSurveyException(MSG_QUESTION_NOT_HAVE_ROW + " (No." + question.getNo() + ")");
                if (question.getMatrixColumns() == null || question.getMatrixColumns().size() == 0)
                    throw new ImportSurveyException(MSG_QUESTION_NOT_HAVE_COLUMN + " (No." + question.getNo() + ")");
            }
        }
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
