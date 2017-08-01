/**
 * 
 */
package com.amway.lms.backend.service.impl;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.RichTextString;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFRichTextString;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.hibernate.HibernateException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.amway.lms.backend.common.AmwayEnum;
import com.amway.lms.backend.common.AmwayEnum.QuestionTypes;
import com.amway.lms.backend.common.Common;
import com.amway.lms.backend.common.Utils;
import com.amway.lms.backend.entity.Answer;
import com.amway.lms.backend.entity.MatrixColumn;
import com.amway.lms.backend.entity.MatrixRow;
import com.amway.lms.backend.entity.Question;
import com.amway.lms.backend.entity.Result;
import com.amway.lms.backend.entity.Survey;
import com.amway.lms.backend.entity.SurveyQuestion;
import com.amway.lms.backend.entity.SurveyResult;
import com.amway.lms.backend.entity.SurveySession;
import com.amway.lms.backend.exception.ObjectNotFoundException;
import com.amway.lms.backend.model.ChoiceModel;
import com.amway.lms.backend.model.ColumnModel;
import com.amway.lms.backend.model.PageModel;
import com.amway.lms.backend.model.QuestionModel;
import com.amway.lms.backend.model.RowModel;
import com.amway.lms.backend.model.SurveyModel;
import com.amway.lms.backend.repository.AnswerRepository;
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
import com.amway.lms.backend.service.SurveyResultService;

/**
 * @author acton
 * @email acton@enclave.vn
 */
@Transactional
@Service
public class SurveyResultServiceImpl implements SurveyResultService {

    private static final Logger logger = LoggerFactory.getLogger(SurveyResultServiceImpl.class);

    private static final String SHEET_NAME = "SurveyResult";

    @Autowired
    SurveyResultRepository surveyResultRepository;

    @Autowired
    ResultRepository resultRepository;

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
    QuestionTypeRepository questionTypeRepository;

    @Autowired
    SurveyTypeRepository surveyTypeRepository;

    @Autowired
    SurveySessionRepository surveySessionRepository;

    private Workbook workbook;

    @Override
    public ResponseEntity<?> addSurveyResult(SurveyResult surveyResult)
            throws HibernateException, SQLException, Exception {
        surveyResult.setCreateAt(Utils.getCurrentTime());
        surveyResultRepository.addSurveyResult(surveyResult);
        for (Result result : surveyResult.getResults()) {
            result.setSurveyId(surveyResult.getSurveyId());
            result.setSurveyResultId(surveyResult.getId());
            resultRepository.addResult(result);
        }
        return Utils.generateSuccessResponseEntity(surveyResult);
    }

    @Override
    public ResponseEntity<?> getSurveyResultBySurveyId(int surveyId) throws Exception {

        Survey survey = surveyRepository.getSurveyById(surveyId);
        if (survey == null)
            throw new ObjectNotFoundException("Survey Not Found By Id: " + surveyId);

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
            if (question.getQuestionTypeId() != QuestionTypes.MATRIX.getValue()) {
                question.setAnswers(getAnswerListByQuestionId(questionId));
            } else if (question.getQuestionTypeId() == QuestionTypes.MATRIX.getValue()) {
                question.setMatrixColumns(getMatrixColumnListByQuestionId(questionId));
                question.setMatrixRows(getMatrixRowListByQuestionId(questionId));
            }
            calculateResults(survey.getId(), question);
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

    private void calculateResults(int surveyId, Question question) {
        int typeId = question.getQuestionTypeId();
        int questionId = question.getId();
        if (typeId == QuestionTypes.RADIO.getValue() || typeId == QuestionTypes.CHECKBOX.getValue() 
                || typeId == QuestionTypes.CHECKBOX_OTHER.getValue() || typeId == QuestionTypes.DROP_DOWN_LIST.getValue()) {
            List<Result> results = resultRepository.getResultsBySurveyIdAndQuestionId(surveyId, questionId);
            int total = 0;
            List<Answer> answers = question.getAnswers();
            if (typeId == QuestionTypes.CHECKBOX_OTHER.getValue()) {
                Answer answerOther = new Answer();
                answerOther.setId(-1);
                answerOther.setAnswerDesc("Other");
                answers.add(answerOther);
            }
            for (Answer answer : answers) {
                int times = 0;
                for (Result result : results) {
                    if (answer.getId() == result.getAnswerId()) {
                        times++;
                    }
                }
                answer.setChooseTimes(times);
                total += times;
            }
            question.setTotal(total);
        } else if (typeId == QuestionTypes.MATRIX.getValue()) {
            for (int i = 0; i < question.getMatrixRows().size(); i++) {
                MatrixRow row = question.getMatrixRows().get(i);
                for (MatrixColumn column : question.getMatrixColumns()) {
                    long chooseTime = resultRepository.countResultBySurveyIdQuestionIdMatrixRowIdMatrixColumnId(
                            surveyId, questionId, row.getId(), column.getId());
                    column.getChooseTimes().add(chooseTime);
                    row.setTotal(row.getTotal() + chooseTime);
                }
            }
        }
    }

    private SurveyModel converDataSurveyEntityToSurveyModel(Survey surveyEntity) {
        SurveyModel surveyModel = new SurveyModel();
        surveyModel.setName(String.valueOf(surveyEntity.getId()));
        surveyModel.setTitle(surveyEntity.getSurveyTitle());
        surveyModel.setQuestionTitleTemplate("{title} {require}:");
        surveyModel.setRequiredText("(*)");

        List<PageModel> pageModels = new ArrayList<>();

        int pageNumber = 0;
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
            } else {

            }
            questionModels.add(convertDataFromQuestionEntityToQuestionModel(surveyEntity.getId(), questionEntity, i));
        }
        surveyModel.setPages(pageModels);
        return surveyModel;
    }

    private QuestionModel convertDataFromQuestionEntityToQuestionModel(int surveyId, Question questionEntity,
            int index) {
        QuestionModel questionModel = new QuestionModel();
        questionModel.setName(String.valueOf(questionEntity.getId()));
        questionModel.setIsRequired(questionEntity.getIsRequired() == 0 ? false : true);
        questionModel.setTitle((index + 1) + ". " + questionEntity.getQuestionDesc());
        questionModel.setType(Common.getQuestionTypes(questionEntity.getQuestionTypeId()));
        questionModel.setFileLocation(questionEntity.getFileLocation());
        if (questionEntity.getQuestionTypeId() != QuestionTypes.MATRIX.getValue()) {
            if (questionEntity.getQuestionTypeId() == QuestionTypes.TEXT_BOX.getValue() 
                    || questionEntity.getQuestionTypeId() == QuestionTypes.COMMENT.getValue()
                    || questionEntity.getQuestionTypeId() == QuestionTypes.EMAIL.getValue()) {
                questionModel.setType("html");
                StringBuffer html = new StringBuffer();
                html.append("<div class>").append("<div ").append("id='sq_1").append(getIdQuestionForTagHTML(index))
                        .append("' ").append("class style='display: inline-block; vertical-align: top; width: 100%;'>")
                        .append("<h5>").append(index + 1).append(". ").append(questionEntity.getQuestionDesc());
                if (questionModel.getIsRequired())
                    html.append("(*)");
                html.append("</h5>");
                List<Result> results = resultRepository.getResultsBySurveyIdAndQuestionId(surveyId,
                        questionEntity.getId());
                if (results == null || results.isEmpty()) {
                    html.append("<p class='no-data'>").append(Common.MSG_QUESTION_NO_DATA).append("</p>");
                } else {
                    html.append("<ul class='list-results'>");
                    for (Result result : results) {
                        html.append("<li>").append(result.getAnswerContent()).append("</li>");
                    }
                    html.append("</ul>");
                }

                html.append("</div>").append("</div>");
                questionModel.setHtml(html.toString());
            } else if (questionEntity.getQuestionTypeId() == QuestionTypes.CHECKBOX_OTHER.getValue()) {
                questionModel.setType("checkbox");
            } else if (questionEntity.getQuestionTypeId() == QuestionTypes.DROP_DOWN_LIST.getValue()) {
                questionModel.setType("radiogroup");
            }
            questionModel.setChoices(convertDataFromAnswerEntityToChoiceModel(questionEntity));
        } else {
            StringBuffer html = new StringBuffer();
            html.append("<div class>").append("<div ").append("id='sq_1").append(getIdQuestionForTagHTML(index))
                    .append("' ").append("class style='display: inline-block; vertical-align: top; width: 100%;'>")
                    .append(" <h5> ").append(index + 1).append(". ").append(questionEntity.getQuestionDesc());
            if (questionModel.getIsRequired())
                html.append("(*)");
            html.append("</h5>");
            html.append("<table class = 'table table-style data-table'>");
            html.append("<thead><tr>");
            html.append("<th></th>");
            for (MatrixColumn columnHeader : questionEntity.getMatrixColumns()) {
                StringBuffer header = new StringBuffer();
                header.append("<th>").append(columnHeader.getColumnDesc()).append("</th>");
                html.append(header.toString());
            }
            html.append("</tr></thead>");

            html.append("<tbody>");
            for (int i = 0; i < questionEntity.getMatrixRows().size(); i++) {
                MatrixRow row = questionEntity.getMatrixRows().get(i);
                StringBuffer rowHtml = new StringBuffer();
                rowHtml.append("<tr>");
                rowHtml.append("<td>").append(row.getRowDesc()).append("</td>");
                long total = row.getTotal();

                for (MatrixColumn column : questionEntity.getMatrixColumns()) {
                    if (total != 0) {
                        /*
                         * rowHtml.append("<td>").append(String.format("%.2f",
                         * (double) column.getTimes() / total * 100))
                         * .append("%").append("</td>");
                         */
                        rowHtml.append("<td>").append(column.getChooseTimes().get(i)).append("/").append(total)
                                .append("</td>");
                    } else {
                        /*
                         * rowHtml.append("<td>").append("0%").append("</td>");
                         */
                        rowHtml.append("<td>").append("0/0").append("</td>");
                    }
                }
                rowHtml.append("</tr>");
                html.append(rowHtml.toString());
            }

            html.append("</tbody>");
            html.append("</table>");
            html.append("</div>").append("</div>");
            questionModel.setType("html");
            questionModel.setHtml(html.toString());
        }
        return questionModel;
    }

    private List<ChoiceModel> convertDataFromAnswerEntityToChoiceModel(Question questionEntity) {
        int typeId = questionEntity.getQuestionTypeId();
        long total = questionEntity.getTotal();

        List<ChoiceModel> choiceModels = new ArrayList<>();
        for (Answer answer : questionEntity.getAnswers()) {
            ChoiceModel choiceModel = new ChoiceModel();
            choiceModel.setValue(String.valueOf(answer.getId()));
            if (typeId == QuestionTypes.RADIO.getValue() 
                    || typeId == QuestionTypes.CHECKBOX.getValue() 
                    || typeId == QuestionTypes.CHECKBOX_OTHER.getValue() 
                    || typeId == QuestionTypes.DROP_DOWN_LIST.getValue()) {
                StringBuffer strResult = new StringBuffer();
                if (total != 0) {
                    strResult.append(answer.getChooseTimes()).append("/").append(total);
                } else {
                    strResult.append("0/0");
                }
                choiceModel.setText("(" + strResult.toString() + ") " + answer.getAnswerDesc().trim());
            } else {
                choiceModel.setText(answer.getAnswerDesc());
            }
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

    private String getIdQuestionForTagHTML(int index) {
        if (index < 10)
            return "0" + (index + 1);
        else
            return String.valueOf(index + 1);
    }

    @Override
    public File exportSurveyResult(int surveyId) throws IOException, Exception {
        try {
            Survey survey = surveyRepository.getSurveyById(surveyId);
            if (survey == null)
                throw new ObjectNotFoundException("Survey Not Found By Id: " + surveyId);
            setSurveyFull(survey);

            String pathTemp = new File(System.getProperty("java.io.tmpdir")).getAbsolutePath();

            Workbook workbook = new XSSFWorkbook();
            this.workbook = workbook;
            setWorkBookData(workbook, survey);

            File fileReport = new File(pathTemp + "/" + Common.FILE_NAME_SURVEY_EXPORT);
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

    private void setSurveyFull(Survey survey) {
        List<Integer> listSurveySessionId = surveyQuestionRepository.getSurveySessionIdListBySurveyId(survey.getId());
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
    }

    private void setWorkBookData(Workbook workbook, Survey survey) {
        Sheet sheet = workbook.createSheet("SurveyResult");
        setSurveyInformationIntoSheet(sheet, survey);
        setSurveySessionIntoSheet(sheet, survey);
        sheet.setColumnWidth(0, 5000);
        sheet.setColumnWidth(1, 5000);
        for (int i = 1; i < 100; i++) {
            sheet.setColumnWidth(i, 5000);
        }
        sheet.setDefaultColumnWidth(5000);
        // sheet.autoSizeColumn(0);
    }

    private void setSurveyInformationIntoSheet(Sheet sheet, Survey survey) {

        Row rowTitle = sheet.createRow(0);
        rowTitle.setHeight((short) (36 * 20));
        rowTitle.createCell(0).setCellValue("Survey title:");
        rowTitle.getCell(0).setCellStyle(getCellStyleHeader());
        rowTitle.createCell(1).setCellValue(survey.getSurveyTitle());

        Row rowDesc = sheet.createRow(1);
        rowDesc.createCell(0).setCellValue("Survey description:");
        rowDesc.setHeight((short) (36 * 20));
        rowDesc.getCell(0).setCellStyle(getCellStyleHeader());
        rowDesc.createCell(1).setCellValue(survey.getSurveyDesc());

        Row rowType = sheet.createRow(2);
        rowType.createCell(0).setCellValue("Survey type:");
        rowType.setHeight((short) (36 * 20));
        rowType.getCell(0).setCellStyle(getCellStyleHeader());
        String type = null;
        if (survey.getSurveyTypeId() == 1)
            type = "Training Need";
        else
            type = "Pre-training";
        rowType.createCell(1).setCellValue(type);

        Row rowStartTime = sheet.createRow(3);
        rowStartTime.createCell(0).setCellValue("Start date:");
        rowStartTime.setHeight((short) (36 * 20));
        rowStartTime.getCell(0).setCellStyle(getCellStyleHeader());
        rowStartTime.createCell(1)
                .setCellValue(Common.getStringDate(survey.getBeginingTime(), Common.DATE_FORMAT_DD_MMM_YYYY));

        Row rowEndTime = sheet.createRow(4);
        rowEndTime.createCell(0).setCellValue("End date:");
        rowEndTime.setHeight((short) (36 * 20));
        rowEndTime.getCell(0).setCellStyle(getCellStyleHeader());
        rowEndTime.createCell(1)
                .setCellValue(Common.getStringDate(survey.getEndingTime(), Common.DATE_FORMAT_DD_MMM_YYYY));
    }

    private void setSurveySessionIntoSheet(Sheet sheet, Survey survey) {
        int rowIndex = 6;
        for (SurveySession surveySession : survey.getSurveySessions()) {
            rowIndex++;
            Row rowSession = sheet.createRow(rowIndex);
            rowSession.createCell(0).setCellValue("Path:");
            rowSession.setHeight((short) (20 * 20));
            rowSession.setRowStyle(getCellStyleForSurveySession(true));
            rowSession.getCell(0).setCellStyle(getCellStyleForSurveySession(true));
            rowSession.createCell(1).setCellValue(surveySession.getSessionDesc());
            rowSession.getCell(1).setCellStyle(getCellStyleForSurveySession(true));
            rowIndex++;
            for (int i = 0; i < surveySession.getQuestions().size(); i++) {
                Question question = surveySession.getQuestions().get(i);
                Row rowQuestion = sheet.createRow(rowIndex);
                rowQuestion.createCell(0).setCellValue(i + 1 + ". " + question.getQuestionDesc());
                rowQuestion.getCell(0).setCellStyle(getCellStyleBold());
                rowIndex++;
                rowIndex = setSurveyAnswerIntoSheet(sheet, rowIndex, survey.getId(), question);
            }
        }
    }

    private int setSurveyAnswerIntoSheet(Sheet sheet, int rowIndex, int surveyId, Question question) {
        int questionType = question.getQuestionTypeId();

        if (questionType != QuestionTypes.MATRIX.getValue()) {
            // calculate answer result for question checkbox, radio,
            // checkoxother, dropdown and append results befor answerDesc
            calculateResults(surveyId, question);
            // Create answer for each result with question are text, comment,
            // email
            setAnswerForQuestionText(surveyId, question);
            if (question.getAnswers().size() > 0) {
                for (int i = 0; i < question.getAnswers().size(); i++) {
                    Answer answer = question.getAnswers().get(i);
                    Row rowAnswer = sheet.createRow(rowIndex);
                    rowAnswer.createCell(1).setCellValue(i + 1 + ". " + answer.getAnswerDesc());
                    rowIndex++;
                }
            } else {
                Row rowAnswer = sheet.createRow(rowIndex);
                rowAnswer.createCell(1).setCellValue(Common.MSG_QUESTION_NO_DATA);
                rowIndex++;
            }

        } else if (questionType == QuestionTypes.MATRIX.getValue()) {
            rowIndex = setResutlQuestionMatrixIntoSheet(sheet, rowIndex, surveyId, question);
        }
        return rowIndex;
    }

    private void setAnswerForQuestionText(int surveyId, Question question) {
        int questionType = question.getQuestionTypeId();
        if (questionType == QuestionTypes.TEXT_BOX.getValue() 
                || questionType == QuestionTypes.COMMENT.getValue() 
                || questionType == QuestionTypes.EMAIL.getValue()) {
            List<Result> results = resultRepository.getResultsBySurveyIdAndQuestionId(surveyId, question.getId());
            List<Answer> answers = new ArrayList<>();
            for (Result result : results) {
                Answer answer = new Answer();
                answer.setAnswerDesc(result.getAnswerContent());
                answers.add(answer);
            }
            question.setAnswers(answers);
        }
    }

    private int setResutlQuestionMatrixIntoSheet(Sheet sheet, int rowIndex, int surveyId, Question question) {
        // set header
        List<MatrixRow> matrixRows = question.getMatrixRows();
        List<MatrixColumn> matrixColumns = question.getMatrixColumns();
        Row rowHeader = sheet.createRow(rowIndex);
        for (int i = 0; i < matrixColumns.size(); i++) {
            MatrixColumn column = matrixColumns.get(i);
            rowHeader.createCell(i + 2).setCellValue(column.getColumnDesc());
            rowHeader.getCell(i + 2).setCellStyle(getCellStyleBorder(true));
        }
        rowIndex++;
        
        calculateResults(surveyId, question);
        for (int i = 0; i < matrixRows.size(); i++) {
            MatrixRow matrixRow = matrixRows.get(i);
            Row row = sheet.createRow(rowIndex);
            row.createCell(1).setCellValue(matrixRow.getRowDesc());
            row.getCell(1).setCellStyle(getCellStyleBorder(true));
            long total = matrixRow.getTotal();

            for (int j = 0; j < matrixColumns.size(); j++) {
                MatrixColumn matrixColumn = matrixColumns.get(j);
                String cellValue = null;
                if (total == 0)
                    cellValue = "0%";
                else
                    cellValue = String.format("%.2f", (double) matrixColumn.getChooseTimes().get(i) / total * 100) + "%";
                row.createCell(j + 2).setCellValue(cellValue);
                row.getCell(j + 2).setCellStyle(getCellStyleBorder(true));
            }

            rowIndex++;
        }
        return rowIndex;
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

    private CellStyle getCellStyleBorder(boolean isWrapText) {
        CellStyle style = workbook.createCellStyle();
        style.setWrapText(isWrapText);
        style.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
        style.setBorderBottom(CellStyle.BORDER_THIN);
        style.setBorderTop(CellStyle.BORDER_THIN);
        style.setBorderRight(CellStyle.BORDER_THIN);
        style.setBorderLeft(CellStyle.BORDER_THIN);
        return style;
    }

    private CellStyle getCellStyleBold() {
        CellStyle style = workbook.createCellStyle();
        Font font = workbook.createFont();
        font.setBold(true);
        style.setFont(font);
        return style;
    }

    private CellStyle getCellStyleForSurveySession(boolean isBold) {
        CellStyle style = workbook.createCellStyle();
        Font font = workbook.createFont();
        font.setBold(isBold);
        font.setColor(IndexedColors.BLACK.index);
        // style.setFillBackgroundColor(IndexedColors.YELLOW.index);
        // style.setFillPattern(CellStyle.);
        style.setFont(font);
        style.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
        return style;
    }

    @Override
    public File exportSurveyResultHorizontal(int surveyId) throws IOException, Exception {
        try {
            Survey survey = surveyRepository.getSurveyById(surveyId);
            if (survey == null)
                throw new ObjectNotFoundException("Survey Not Found By Id: " + surveyId);
            setSurveyFull(survey);

            String pathTemp = new File(System.getProperty("java.io.tmpdir")).getAbsolutePath();

            Workbook workbook = new XSSFWorkbook();
            this.workbook = workbook;
            setWorkBookDataHorizontal(workbook, survey);

            File fileReport = new File(pathTemp + "/" + getFileName(survey));
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

    private String getFileName(Survey survey) {
        String fileName = Common.FILE_NAME_SURVEY_EXPORT + "_" + survey.getSurveyTitle();
        fileName = fileName.length() > 220 ? fileName.substring(0, 220) : fileName;
        fileName = StringUtils.stripAccents(fileName);
        fileName += ".xlsx";
        return fileName;
    }

    private void setWorkBookDataHorizontal(Workbook workbook, Survey survey) {
        Sheet sheet = workbook.createSheet(SHEET_NAME);
        setSurveyInformationIntoSheet(sheet, survey);
        // setSurveySessionIntoSheet(sheet, survey);
        setSurveyResultBody(sheet, survey);
        sheet.setColumnWidth(0, 5000);
        sheet.setColumnWidth(1, 5000);
        for (int i = 2; i < 200; i++) {
            sheet.setColumnWidth(i, 10000);
        }
    }

    private void setSurveyResultBody(Sheet sheet, Survey survey) {
        int rowIndex = 6;
        rowIndex = setSurveyQuestionIntoSheet(sheet, rowIndex, survey);
        rowIndex = setSurveyResultEachRownIntoSheet(sheet, rowIndex, survey);
    }

    private int setSurveyQuestionIntoSheet(Sheet sheet, int rowIndex, Survey survey) {
        int cellIndex = 1;
        Row rowHeader = sheet.createRow(rowIndex);
        rowHeader.createCell(cellIndex).setCellValue("1. Survey date");
        rowHeader.getCell(cellIndex).setCellStyle(getCellStyleWrap(true));
        cellIndex++;
        for (SurveySession surveySession : survey.getSurveySessions()) {
            for (int i = 0; i < surveySession.getQuestions().size(); i++) {
                Question question = surveySession.getQuestions().get(i);

                if (question.getQuestionTypeId() != QuestionTypes.MATRIX.getValue()) {
                    Cell cellQuestion = rowHeader.createCell(cellIndex);
                    cellQuestion.setCellValue(new StringBuffer().append(cellIndex).append(". ")
                            .append(question.getQuestionDesc()).append(" (")
                            .append(getQuestionTypesForExport(question.getQuestionTypeId())).append(")").toString());
                    cellQuestion.setCellStyle(getCellStyleWrap(true));
                    cellIndex++;
                } else if (question.getQuestionTypeId() == QuestionTypes.MATRIX.getValue()) {
                    for (MatrixRow matrixRow : question.getMatrixRows()) {
                        Cell cellQuestion = rowHeader.createCell(cellIndex);
                        StringBuffer bufferQuestion = new StringBuffer();
                        bufferQuestion.append(cellIndex).append(". ").append(question.getQuestionDesc());
                        StringBuffer bufferMaxtrixRow = new StringBuffer();
                        bufferMaxtrixRow.append(" - ").append(matrixRow.getRowDesc());
                        StringBuffer bufferType = new StringBuffer();
                        bufferType.append(" (Matrix)");

                        String cellStr = bufferQuestion.toString() + bufferMaxtrixRow.toString()
                                + bufferType.toString();

                        Font fontBold = workbook.createFont();
                        fontBold.setBold(true);
                        Font fontNormal = workbook.createFont();
                        fontNormal.setBold(false);
                        RichTextString richTextString = new XSSFRichTextString(cellStr);
                        richTextString.applyFont(0, bufferQuestion.toString().length(), fontBold);
                        richTextString.applyFont(bufferQuestion.toString().length(),
                                bufferQuestion.toString().length() + bufferMaxtrixRow.toString().length(), fontNormal);
                        richTextString.applyFont(cellStr.length() - 8, cellStr.length(), fontBold);
                        cellQuestion.setCellValue(richTextString);
                        cellQuestion.setCellStyle(getCellStyleWrap());

                        cellIndex++;
                    }
                }
            }
        }

        return ++rowIndex;
    }

    private int setSurveyResultEachRownIntoSheet(Sheet sheet, int rowIndex, Survey survey) {

        int surveyId = survey.getId();
        List<SurveyResult> surveyResults = surveyResultRepository.getSurveyResultListBySurveyId(surveyId);

        int indexResult = 1;
        for (SurveyResult surveyResult : surveyResults) {
            int cellIndex = 0;
            int surveyResultId = surveyResult.getId();
            Row rowResult = sheet.createRow(rowIndex);

            Cell cellIndexResult = rowResult.createCell(cellIndex);
            cellIndexResult.setCellValue(indexResult);
            cellIndexResult.setCellStyle(getCellStyleWrap(false));
            cellIndex++;

            Cell cellSurveyDate = rowResult.createCell(cellIndex);
            cellSurveyDate.setCellValue(
                    Common.getStringDate(surveyResult.getCreateAt(), Common.DATE_TIME_FORMAT_DD_MMM_YYYY));
            cellSurveyDate.setCellStyle(getCellStyleWrap(false));
            cellIndex++;

            for (SurveySession surveySession : survey.getSurveySessions()) {
                for (int i = 0; i < surveySession.getQuestions().size(); i++) {
                    Question question = surveySession.getQuestions().get(i);
                    int questionTypeId = question.getQuestionTypeId();
                    if (questionTypeId == QuestionTypes.TEXT_BOX.getValue() 
                            || questionTypeId == QuestionTypes.COMMENT.getValue() 
                            || questionTypeId == QuestionTypes.EMAIL.getValue()) {
                        Cell cellResult = rowResult.createCell(cellIndex);
                        cellResult.setCellValue(
                                getResultValueForQuestionText(surveyResultId, surveyId, question.getId()));
                        cellResult.setCellStyle(getCellStyleWrap(false));
                        cellIndex++;
                    } else if (questionTypeId == QuestionTypes.RADIO.getValue() 
                            || questionTypeId == QuestionTypes.CHECKBOX.getValue() 
                            || questionTypeId == QuestionTypes.DROP_DOWN_LIST.getValue()) {
                        Cell cellResult = rowResult.createCell(cellIndex);
                        cellResult.setCellValue(
                                getResultValueForQuestionChoice(surveyResultId, surveyId, question.getId()));
                        cellResult.setCellStyle(getCellStyleWrap(false));
                        cellIndex++;
                    } else if (question.getQuestionTypeId() == QuestionTypes.MATRIX.getValue()) {
                        for (MatrixRow matrixRow : question.getMatrixRows()) {
                            Cell cellResult = rowResult.createCell(cellIndex);
                            cellResult.setCellValue(getResultValueForQuestionMatrix(surveyResultId, surveyId,
                                    question.getId(), matrixRow.getId(), question.getMatrixColumns()));
                            cellResult.setCellStyle(getCellStyleWrap(false));
                            cellIndex++;
                        }
                    }
                }
            }
            rowIndex++;
            indexResult++;
        }
        return rowIndex;
    }

    private String getResultValueForQuestionText(int surveyResultId, int surveyId, int questionId) {
        StringBuffer stringBuffer = new StringBuffer();
        try {
            List<Result> results = resultRepository.getResultsBySurveyResultIdSurveyIdAndQuestionId(surveyResultId,
                    surveyId, questionId);
            if (results.size() == 0) {
                stringBuffer.append("");
            } else {
                stringBuffer.append(results.get(0).getAnswerContent());
            }
            return stringBuffer.toString();
        } catch (Exception e) {
            e.printStackTrace();
            return stringBuffer.append("").toString();
        }
    }

    private String getResultValueForQuestionChoice(int surveyResultId, int surveyId, int questionId) {
        StringBuffer stringBuffer = new StringBuffer();
        try {
            List<Result> results = resultRepository.getResultsBySurveyResultIdSurveyIdAndQuestionId(surveyResultId,
                    surveyId, questionId);
            if (results.size() == 0) {
                stringBuffer.append("");
            } else {
                for (Result result : results) {
                    Answer answer = answerRepository.getAnswerById(result.getAnswerId());
                    if (answer != null) {
                        stringBuffer.append(answer.getAnswerDesc()).append(", ");
                    }
                }
                if (stringBuffer.toString().length() > 3)
                    stringBuffer.delete(stringBuffer.toString().length() - 2, stringBuffer.toString().length());
            }

            return stringBuffer.toString();
        } catch (Exception e) {
            e.printStackTrace();
            return stringBuffer.append("").toString();
        }
    }

    private String getResultValueForQuestionMatrix(int surveyResultId, int surveyId, int questionId, int matrixRowId,
            List<MatrixColumn> matrixColumns) {
        StringBuffer stringBuffer = new StringBuffer();
        try {
            for (MatrixColumn matrixColumn : matrixColumns) {
                List<Result> results = resultRepository
                        .getResultsBySurveyResultIdSurveyIdAndQuestionIdMatrixRowIdMatrixColumnId(surveyResultId,
                                surveyId, questionId, matrixRowId, matrixColumn.getId());
                if (results.isEmpty())
                    stringBuffer.append("");
                else {
                    stringBuffer.append(matrixColumn.getColumnDesc());
                }
            }
            return stringBuffer.toString();
        } catch (Exception e) {
            e.printStackTrace();
            return stringBuffer.append("").toString();
        }
    }

    private CellStyle getCellStyleWrap(boolean isBold) {
        CellStyle style = workbook.createCellStyle();
        Font font = workbook.createFont();
        font.setBold(isBold);
        style.setFont(font);
        style.setWrapText(true);
        return style;
    }

    private CellStyle getCellStyleWrap() {
        CellStyle style = workbook.createCellStyle();
        style.setWrapText(true);
        return style;
    }
    
    public static String getQuestionTypesForExport(int questionTypeId) {
        if (questionTypeId == AmwayEnum.QuestionTypesForExport.TEXT_BOX.getValue())
            return AmwayEnum.QuestionTypesForExport.TEXT_BOX.getStrValue();
        else if (questionTypeId == AmwayEnum.QuestionTypesForExport.RADIO.getValue())
            return AmwayEnum.QuestionTypesForExport.RADIO.getStrValue();
        else if (questionTypeId == AmwayEnum.QuestionTypesForExport.CHECKBOX.getValue())
            return AmwayEnum.QuestionTypesForExport.CHECKBOX.getStrValue();
        else if (questionTypeId == AmwayEnum.QuestionTypesForExport.CHECKBOX_OTHER.getValue())
            return AmwayEnum.QuestionTypesForExport.CHECKBOX_OTHER.getStrValue();
        else if (questionTypeId == AmwayEnum.QuestionTypesForExport.COMMENT.getValue())
            return AmwayEnum.QuestionTypesForExport.COMMENT.getStrValue();
        else if (questionTypeId == AmwayEnum.QuestionTypesForExport.DROP_DOWN_LIST.getValue())
            return AmwayEnum.QuestionTypesForExport.DROP_DOWN_LIST.getStrValue();
        else if (questionTypeId == AmwayEnum.QuestionTypesForExport.EMAIL.getValue())
            return AmwayEnum.QuestionTypesForExport.EMAIL.getStrValue();
        else if (questionTypeId == AmwayEnum.QuestionTypesForExport.MATRIX.getValue())
            return AmwayEnum.QuestionTypesForExport.MATRIX.getStrValue();

        else
            return "";
    }
    
}
