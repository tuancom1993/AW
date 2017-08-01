/**
 * 
 */
package com.amway.lms.backend.service.impl;

import java.io.File;
import java.io.FileOutputStream;
import java.net.URLEncoder;
import java.sql.Timestamp;
import java.util.List;

import javax.transaction.Transactional;

import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.amway.lms.backend.common.Common;
import com.amway.lms.backend.common.ErrorCode;
import com.amway.lms.backend.common.Message;
import com.amway.lms.backend.common.Utils;
import com.amway.lms.backend.entity.Course;
import com.amway.lms.backend.entity.Location;
import com.amway.lms.backend.entity.PostTrainingSurveyTrainer;
import com.amway.lms.backend.entity.Session;
import com.amway.lms.backend.entity.Test;
import com.amway.lms.backend.entity.TestParticipant;
import com.amway.lms.backend.entity.User;
import com.amway.lms.backend.exception.AddObjectException;
import com.amway.lms.backend.exception.ObjectNotFoundException;
import com.amway.lms.backend.exception.PostTrainingSurveyTrainerException;
import com.amway.lms.backend.model.PostTrainingSurvey;
import com.amway.lms.backend.model.PostTrainingSurveyInformation;
import com.amway.lms.backend.model.TestModel;
import com.amway.lms.backend.repository.CourseRepository;
import com.amway.lms.backend.repository.LocationRepository;
import com.amway.lms.backend.repository.PostTrainingSurveyTrainerRepository;
import com.amway.lms.backend.repository.SessionRepository;
import com.amway.lms.backend.repository.UserRepository;
import com.amway.lms.backend.service.PostTrainingSurveyTrainerService;

/**
 * @author acton
 * @email acton@enclave.vn
 */
@Service
@Transactional
public class PostTrainingSurveyTrainerServiceImpl implements PostTrainingSurveyTrainerService{
    
    private static final Logger logger = LoggerFactory
            .getLogger(PostTrainingSurveyTrainerServiceImpl.class);
    
    @Value("${expired.days.post.training.survey}")
    private int EXPERIED_DAYS;
    
    @Autowired
    PostTrainingSurveyTrainerRepository postTrainingSurveyTrainerRepository;
    
    @Autowired
    SessionRepository sessionRepository;
    
    @Autowired
    CourseRepository courseRepository; 
    
    @Autowired
    LocationRepository locationRepository;
    
    @Autowired
    UserRepository userRepository;
    
    private Workbook workbook;
    
    @Override
    public ResponseEntity<?> addPostTrainingSurveyTrainer(PostTrainingSurveyTrainer survey) throws AddObjectException, Exception {
        logger.debug("addPostTrainingSurveyTrainer");
        try {
            postTrainingSurveyTrainerRepository.addPostTrainingSurveyTrainer(survey);
            return Utils.generateSuccessResponseEntity(survey);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("AddObjectException - addPostTrainingSurveyTrainer: "+e.getMessage());
            //throw new AddObjectException("Cannot add PostTrainingSurveyTrainer");
            return Utils.generateFailureResponseEntity(ErrorCode.CODE_ADD_OBJECT_EXCEPTION, ErrorCode.MSG_ADD_OBJECT_EXCEPTION);
        }
    }
    @Override
    public File exportPostTrainingSurveyTrainer(int sessionId) {
        try {
            Session session = sessionRepository.getSessionById(sessionId);
            if(session ==null) throw new ObjectNotFoundException("Cannot find Session with id ="+sessionId);
            
            List<PostTrainingSurveyTrainer> surveys = postTrainingSurveyTrainerRepository.getPostTrainingSurveyTrainerListBySessionId(sessionId);
            
            Workbook workbook = new XSSFWorkbook();
            this.workbook = workbook;
            setWorkBookData(workbook, session, surveys);
            
            String pathTemp = new File(System.getProperty("java.io.tmpdir")).getAbsolutePath();
            File fileReport = new File(pathTemp+"/"+Common.FILE_NAME_POST_TRAINING_SURVEY_TRAINER_EXPORT);
            System.out.println("Name: "+fileReport.getName() + " Path: "+fileReport.getAbsolutePath());
            
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
    private void setWorkBookData(Workbook workbook2, Session session, List<PostTrainingSurveyTrainer> surveys) {
        Sheet sheet = workbook.createSheet("Result");
        for(int i = 0; i < 100; i++){
            sheet.setColumnWidth(i, 5000);
        }
        setSessionInformationIntoSheet(sheet, session);
        setDataResultIntoSheet(sheet, surveys);
        
    }
    
    private void setSessionInformationIntoSheet(Sheet sheet, Session session) {
        Course course = courseRepository.getCourseById(session.getCourseId());
        Location location = locationRepository.getLocationById(session.getLocationId());
        
        Row rowCourseName = sheet.createRow(0);
        rowCourseName.setHeight((short)(36*20));
        rowCourseName.createCell(0).setCellValue("Course Name:");
        rowCourseName.getCell(0).setCellStyle(getCellStyleHeader());
        rowCourseName.createCell(1).setCellValue(course == null ? "" : course.getName());
        
        Row rowSessionName = sheet.createRow(1);
        rowSessionName.setHeight((short)(36*20));
        rowSessionName.createCell(0).setCellValue("Learning session:");
        rowSessionName.getCell(0).setCellStyle(getCellStyleHeader());
        rowSessionName.createCell(1).setCellValue(session.getName());
        
        Row rowLocation = sheet.createRow(2);
        rowLocation.setHeight((short)(36*20));
        rowLocation.createCell(0).setCellValue("Location:");
        rowLocation.getCell(0).setCellStyle(getCellStyleHeader());
        rowLocation.createCell(1).setCellValue(location == null ? "" : location.getName());
        
        Row rowStartTime = sheet.createRow(3);
        rowStartTime.setHeight((short)(36*20));
        rowStartTime.createCell(0).setCellValue("Start Time:");
        rowStartTime.getCell(0).setCellStyle(getCellStyleHeader());
        rowStartTime.createCell(1).setCellValue(Common.getStringDate(session.getStartTime(), Common.DATE_TIME_FORMAT_DD_MMM_YYYY_AM_PM));
        
        Row rowEndTime = sheet.createRow(4);
        rowEndTime.setHeight((short)(36*20));
        rowEndTime.createCell(0).setCellValue("End Time:");
        rowEndTime.getCell(0).setCellStyle(getCellStyleHeader());
        rowEndTime.createCell(1).setCellValue(Common.getStringDate(session.getEndTime(), Common.DATE_TIME_FORMAT_DD_MMM_YYYY_AM_PM));
        
    }
    private void setDataResultIntoSheet(Sheet sheet, List<PostTrainingSurveyTrainer> surveys) {
        int rowIndex = 7;
        rowIndex = setSurveyQuestionIntoSheet(sheet, rowIndex);
        rowIndex = setSurveyDataIntoSheet(sheet, rowIndex, surveys);
    }

    private int setSurveyQuestionIntoSheet(Sheet sheet, int rowIndex) {
        Row rowHeader = sheet.createRow(rowIndex);
        Row rowSubQuestion = sheet.createRow(rowIndex+1);
        
        rowHeader.createCell(0).setCellValue("Index");
        rowHeader.getCell(0).setCellStyle(getCellStyleHeader());
        
        rowHeader.createCell(1).setCellValue("Trainer’s Name");
        rowHeader.getCell(1).setCellStyle(getCellStyleHeader());
        
        sheet.addMergedRegion(new CellRangeAddress(rowIndex, rowIndex, 2, 4));
        rowHeader.createCell(2).setCellValue("1. Please evaluate yourself as trainer");
        rowHeader.getCell(2).setCellStyle(getCellStyleHeader());

        rowSubQuestion.createCell(2);
        rowSubQuestion.createCell(2).setCellValue("Good points");
        rowSubQuestion.getCell(2).setCellStyle(getCellStyleWrap(true));
        
        rowSubQuestion.createCell(3);
        rowSubQuestion.createCell(3).setCellValue("Improving points");
        rowSubQuestion.getCell(3).setCellStyle(getCellStyleWrap(true));
        
        rowSubQuestion.createCell(4);
        rowSubQuestion.createCell(4).setCellValue("Suggestions");
        rowSubQuestion.getCell(4).setCellStyle(getCellStyleWrap(true));
        
        sheet.addMergedRegion(new CellRangeAddress(rowIndex, rowIndex, 5, 10));
        rowHeader.createCell(5).setCellValue("2. Please choose top 3 pro-active participants in your trainings and note some comments");
        rowHeader.getCell(5).setCellStyle(getCellStyleHeader());
        
        rowSubQuestion.createCell(5);
        rowSubQuestion.createCell(5).setCellValue("No 1. Good points");
        rowSubQuestion.getCell(5).setCellStyle(getCellStyleWrap(true));
        rowSubQuestion.createCell(6);
        rowSubQuestion.createCell(6).setCellValue("No 1. Comment");
        rowSubQuestion.getCell(6).setCellStyle(getCellStyleWrap(true));
        
        rowSubQuestion.createCell(7);
        rowSubQuestion.createCell(7).setCellValue("No 2. Good points");
        rowSubQuestion.getCell(7).setCellStyle(getCellStyleWrap(true));
        rowSubQuestion.createCell(8);
        rowSubQuestion.createCell(8).setCellValue("No 2. Comment");
        rowSubQuestion.getCell(8).setCellStyle(getCellStyleWrap(true));
        
        rowSubQuestion.createCell(9);
        rowSubQuestion.createCell(9).setCellValue("No 3. Good points");
        rowSubQuestion.getCell(9).setCellStyle(getCellStyleWrap(true));
        rowSubQuestion.createCell(10);
        rowSubQuestion.createCell(10).setCellValue("No 3. Comment");
        rowSubQuestion.getCell(10).setCellStyle(getCellStyleWrap(true));
        
        return rowIndex += 2;
    }
    
    private int setSurveyDataIntoSheet(Sheet sheet, int rowIndex, List<PostTrainingSurveyTrainer> surveys) {
        for(int i = 0; i < surveys.size(); i++){
            PostTrainingSurveyTrainer survey = surveys.get(i);
            
            StringBuffer trainerName = new StringBuffer();
            User trainer = userRepository.getUserById(survey.getUserId());
            if(trainer == null) continue;
            else {
                trainerName.append(trainer.getFirstName()).append(" ").append(trainer.getLastName());
            }

            Row rowResult = sheet.createRow(rowIndex);

            rowResult.createCell(0).setCellValue(i+1);
            rowResult.createCell(1).setCellValue(trainerName.toString());
            rowResult.createCell(2).setCellValue(survey.getQ_1_1());
            rowResult.createCell(3).setCellValue(survey.getQ_1_2());
            rowResult.createCell(4).setCellValue(survey.getQ_1_3());
            rowResult.createCell(5).setCellValue(survey.getQ_2_1_1());
            rowResult.createCell(6).setCellValue(survey.getQ_2_1_2());
            rowResult.createCell(7).setCellValue(survey.getQ_2_2_1());
            rowResult.createCell(8).setCellValue(survey.getQ_2_2_2());
            rowResult.createCell(9).setCellValue(survey.getQ_2_3_1());
            rowResult.createCell(10).setCellValue(survey.getQ_2_3_2());
            
            for(int j = 0; j <= 10; j++){
                rowResult.getCell(j).setCellStyle(getCellStyleWrap());
            }
            
            rowIndex++;
        }
        return rowIndex;
    }
    private CellStyle getCellStyleHeader(){
        CellStyle style_header = workbook.createCellStyle();
        Font font_header = workbook.createFont();
        font_header.setBold(true);
        font_header.setColor(IndexedColors.BLACK.index);
        style_header.setFont(font_header);
        style_header.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
        style_header.setWrapText(true);
        return style_header;
    }
    
    private CellStyle getCellStyleWrap(boolean isBold){
        CellStyle style = workbook.createCellStyle();
        Font font = workbook.createFont();
        font.setBold(isBold);
        style.setFont(font);
        style.setWrapText(true);
        return style;
    }
    
    private CellStyle getCellStyleWrap(){
        CellStyle style = workbook.createCellStyle();
        style.setWrapText(true);
        return style;
    }
    
    @Override
    public String encodePostSurveyTrainer(int sessionId, int userId) {
        try {
            PostTrainingSurvey postTrainingSurvey = new PostTrainingSurvey();
            postTrainingSurvey.setSessionId(sessionId);
            postTrainingSurvey.setUserId(userId);
            return URLEncoder.encode(Utils.encode(Utils.objectToJsonString(postTrainingSurvey)), "UTF-8");
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    @Override
    public ResponseEntity<?> getSessionInformationByEncodedValue(String encodedValue, User userLogin) throws Exception {
        try {
            if(userLogin == null)
                throw new PostTrainingSurveyTrainerException(Message.MSG_USER_NOT_FOUND);

            PostTrainingSurvey postTrainingSurvey = Utils.jsonStringToObject(Utils.decode(encodedValue), PostTrainingSurvey.class);
            
            if (postTrainingSurvey.getUserId() != userLogin.getId())
                throw new PostTrainingSurveyTrainerException(Message.MSG_USER_LOGIN_NOT_CORRECT);
            
            Session session = sessionRepository.getSessionById(postTrainingSurvey.getSessionId());
            if(session == null)
                throw new PostTrainingSurveyTrainerException(Message.MSG_SESSION_NOT_FOUND);
            
            if(session.getIsInternalTrainer() != 1){
                logger.error("Session isn't internal trainer");
                throw new PostTrainingSurveyTrainerException(Message.MSG_TRAINER_NOT_FOUND);
            }
            
            int trainerId = session.getTrainerId();
            User trainer = userRepository.getUserById(trainerId);
            if(trainer == null){
                logger.error("Trainer could'nt be found");
                throw new PostTrainingSurveyTrainerException(Message.MSG_TRAINER_NOT_FOUND);
            }
            
            Course course = courseRepository.getCourseById(session.getCourseId());
            if (course == null)
                throw new PostTrainingSurveyTrainerException(Message.MSG_COURSE_NOT_FOUND);
            
            if(isOutOfExpiredDate(session))
                throw new PostTrainingSurveyTrainerException(Message.MSG_OUT_OF_EXPIRED_DATE);
            
            return Utils.generateSuccessResponseEntity(getPostTrainingSurveyInformation(course, session));
        } catch (PostTrainingSurveyTrainerException e) {
            e.printStackTrace();
            return Utils.generateFailureResponseEntity(ErrorCode.CODE_POST_TRAINING_SURVEY_TRAINER_EXCEPTION, e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            return Utils.generateFailureResponseEntity(ErrorCode.CODE_POST_TRAINING_SURVEY_TRAINER_EXCEPTION, Message.MSG_POST_SURVEY_NOT_AVAILABE);
        }
    }
    
    private TestModel convertDataTestEntityToTestModel(Test test, TestParticipant testParticipant){
        TestModel testModel = new TestModel();
        
        return testModel;
    }
    
    private boolean isOutOfExpiredDate(Session session){
        Timestamp toDay = new Timestamp(System.currentTimeMillis());
        
        if(session.getEndTime() == null)
            return true;
        if(toDay.after(new Timestamp(session.getEndTime().getTime() + (EXPERIED_DAYS * 24 * 60 * 60 * 1000))))
            return true;
        
        return false;
    }
    
    private PostTrainingSurveyInformation getPostTrainingSurveyInformation(Course course, Session session){
        PostTrainingSurveyInformation information = new PostTrainingSurveyInformation();
        information.setCourseName(course.getName());
        information.setSessionId(session.getId());
        information.setSessionName(session.getName());
        information.setTrainerName(session.getTrainerFullName());
        information.setUserId(session.getTrainerId());
        Location location = locationRepository.getLocationById(session.getLocationId());
        information.setLocation(location == null ? "" : location.getName());
        information.setDuration(Common.getDateDiff(session.getStartTime(), session.getEndTime()));   
        return information;
    }

}
