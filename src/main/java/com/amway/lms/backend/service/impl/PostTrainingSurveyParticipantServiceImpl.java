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
import com.amway.lms.backend.entity.PostTrainingSurveyParticipant;
import com.amway.lms.backend.entity.Session;
import com.amway.lms.backend.entity.SessionParticipant;
import com.amway.lms.backend.entity.User;
import com.amway.lms.backend.exception.AddObjectException;
import com.amway.lms.backend.exception.ObjectNotFoundException;
import com.amway.lms.backend.exception.PostTrainingSurveyParticipantException;
import com.amway.lms.backend.model.PostTrainingSurvey;
import com.amway.lms.backend.model.PostTrainingSurveyInformation;
import com.amway.lms.backend.repository.CourseRepository;
import com.amway.lms.backend.repository.LocationRepository;
import com.amway.lms.backend.repository.PostTrainingSurveyParticipantRepository;
import com.amway.lms.backend.repository.SessionParticipantRepository;
import com.amway.lms.backend.repository.SessionRepository;
import com.amway.lms.backend.repository.UserRepository;
import com.amway.lms.backend.service.PostTrainingSurveyParticipantService;


/**
 * @author acton
 * @email acton@enclave.vn
 */
@Service
@Transactional
public class PostTrainingSurveyParticipantServiceImpl implements PostTrainingSurveyParticipantService{
    private static final Logger logger = LoggerFactory.getLogger(PostTrainingSurveyParticipantServiceImpl.class);
    
    @Value("${expired.days.post.training.survey}")
    private int EXPERIED_DAYS;
    
    @Autowired
    PostTrainingSurveyParticipantRepository postTrainingSurveyParticipantRepository;
    
    @Autowired
    SessionRepository sessionRepository;
    
    @Autowired
    CourseRepository courseRepository; 
    
    @Autowired
    LocationRepository locationRepository;
    
    @Autowired
    UserRepository userRepository;
    
    @Autowired
    SessionParticipantRepository sessionParticipantRepository;
    
    private Workbook workbook;

    @Override
    public ResponseEntity<?> addPostTrainingSurveyParticipant(PostTrainingSurveyParticipant survey)
            throws AddObjectException, Exception {
        try {
            postTrainingSurveyParticipantRepository.addPostTrainingSurveyParticipant(survey);
            return Utils.generateSuccessResponseEntity(survey);
        } catch (Exception e) {
            e.printStackTrace();
            throw new AddObjectException("Cannot add Object PostTrainingSurveyParticipant");
        }
    }

    @Override
    public File exportPostTrainingSurveyParticipant(int sessionId) {
        try {
            Session session = sessionRepository.getSessionById(sessionId);
            if(session ==null) throw new ObjectNotFoundException("Cannot find Session with id ="+sessionId);
            
            List<PostTrainingSurveyParticipant> surveys = postTrainingSurveyParticipantRepository.getPostTrainingSurveyParticipantListBySessionId(sessionId);
            
            Workbook workbook = new XSSFWorkbook();
            this.workbook = workbook;
            setWorkBookData(workbook, session, surveys);
            
            String pathTemp = new File(System.getProperty("java.io.tmpdir")).getAbsolutePath();
            File fileReport = new File(pathTemp+"/"+Common.FILE_NAME_POST_TRAINING_SURVEY_PARTICIPANT_EXPORT);
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

    private void setWorkBookData(Workbook workbook, Session session, List<PostTrainingSurveyParticipant> surveys) {
        
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
        
        Row rowTrainerName = sheet.createRow(5);
        rowTrainerName.setHeight((short)(36*20));
        rowTrainerName.createCell(0).setCellValue("Trainer's Name:");
        rowTrainerName.getCell(0).setCellStyle(getCellStyleHeader());
        rowTrainerName.createCell(1).setCellValue(session.getTrainerFullName());
    }
    

    private void setDataResultIntoSheet(Sheet sheet, List<PostTrainingSurveyParticipant> surveys) {
        int rowIndex = 7;
        rowIndex = setSurveyQuestionIntoSheet(sheet, rowIndex);
        rowIndex = setSurveyDataIntoSheet(sheet, rowIndex, surveys);
    }

    private int setSurveyQuestionIntoSheet(Sheet sheet, int rowIndex) {
        Row rowHeader = sheet.createRow(rowIndex);
        int cellHeaderIndex = 0;
        Row rowSubQuestion = sheet.createRow(rowIndex+1);
        int cellSubQuestionIndex = 0;
        
        
        rowHeader.createCell(cellHeaderIndex).setCellValue("Index");
        rowHeader.getCell(cellHeaderIndex).setCellStyle(getCellStyleHeader());
        
        rowHeader.createCell(++cellHeaderIndex).setCellValue("Participant’s Name");
        rowHeader.getCell(cellHeaderIndex).setCellStyle(getCellStyleHeader());
        
        sheet.addMergedRegion(new CellRangeAddress(rowIndex, rowIndex, ++cellHeaderIndex, (cellHeaderIndex += 2)));
        rowHeader.createCell(cellHeaderIndex-2).setCellValue("1. Please state your overall comments for the training (key words)");
        rowHeader.getCell(cellHeaderIndex-2).setCellStyle(getCellStyleHeader());

        rowSubQuestion.createCell(2);
        rowSubQuestion.createCell(2).setCellValue("Good points");
        rowSubQuestion.getCell(2).setCellStyle(getCellStyleWrap(true));
        
        rowSubQuestion.createCell(3);
        rowSubQuestion.createCell(3).setCellValue("Improving points");
        rowSubQuestion.getCell(3).setCellStyle(getCellStyleWrap(true));
        
        rowSubQuestion.createCell(4);
        rowSubQuestion.createCell(4).setCellValue("Suggestions");
        rowSubQuestion.getCell(4).setCellStyle(getCellStyleWrap(true));
        
        rowHeader.createCell(++cellHeaderIndex).setCellValue("2. Please state your gains from this training. Which one will you apply in work?");
        rowHeader.getCell(cellHeaderIndex).setCellStyle(getCellStyleHeader());
        
        rowHeader.createCell(++cellHeaderIndex).setCellValue("3. Your comment on facilitator");
        rowHeader.getCell(cellHeaderIndex).setCellStyle(getCellStyleHeader());
        
        rowHeader.createCell(++cellHeaderIndex).setCellValue("4. The training provides useful skills and/or knowledge for your work");
        rowHeader.getCell(cellHeaderIndex).setCellStyle(getCellStyleHeader());
        
        rowHeader.createCell(++cellHeaderIndex).setCellValue("5. The overall organization, agenda, sequence of activities, presentation, and time allocation are well managed");
        rowHeader.getCell(cellHeaderIndex).setCellStyle(getCellStyleHeader());
        
        rowHeader.createCell(++cellHeaderIndex).setCellValue("6. You have opportunity to discuss and contribute to the training");
        rowHeader.getCell(cellHeaderIndex).setCellStyle(getCellStyleHeader());
        
        rowHeader.createCell(++cellHeaderIndex).setCellValue("7. The facilitator did deliver well the training content");
        rowHeader.getCell(cellHeaderIndex).setCellStyle(getCellStyleHeader());
        
        rowHeader.createCell(++cellHeaderIndex).setCellValue("8. Overall, you are satisfied with the training and its outcomes");
        rowHeader.getCell(cellHeaderIndex).setCellStyle(getCellStyleHeader());
        
        rowHeader.createCell(++cellHeaderIndex).setCellValue("9. Do you have any suggestion on what topics for next Learning sessions? Your input is highly appreciated");
        rowHeader.getCell(cellHeaderIndex).setCellStyle(getCellStyleHeader());
        
        rowIndex = rowIndex + 2;
        return rowIndex;
    }
    

    private int setSurveyDataIntoSheet(Sheet sheet, int rowIndex, List<PostTrainingSurveyParticipant> surveys) {
        for(int i = 0; i < surveys.size(); i++){
            PostTrainingSurveyParticipant survey = surveys.get(i);
            
            StringBuffer participantName = new StringBuffer();
            User participant = userRepository.getUserById(survey.getUserId());
            if(participant == null) continue;
            else {
                participantName.append(participant.getFirstName()).append(" ").append(participant.getLastName());
            }
            
            Row rowResult = sheet.createRow(rowIndex);
            
            rowResult.createCell(0).setCellValue(i+1);
            rowResult.createCell(1).setCellValue(participantName.toString());
            rowResult.createCell(2).setCellValue(survey.getQ_1_1());
            rowResult.createCell(3).setCellValue(survey.getQ_1_2());
            rowResult.createCell(4).setCellValue(survey.getQ_1_3());
            rowResult.createCell(5).setCellValue(survey.getQ_2());
            rowResult.createCell(6).setCellValue(survey.getQ_3());
            rowResult.createCell(7).setCellValue(survey.getQ_4());
            rowResult.createCell(8).setCellValue(survey.getQ_5());
            rowResult.createCell(9).setCellValue(survey.getQ_6());
            rowResult.createCell(10).setCellValue(survey.getQ_7());
            rowResult.createCell(11).setCellValue(survey.getQ_8());
            rowResult.createCell(12).setCellValue(survey.getQ_9());
            
            for(int j = 0; j <= 12; j++){
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
    public String encodePostSurveyParticipant(int sessionId, int userId) {
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
                throw new PostTrainingSurveyParticipantException(Message.MSG_USER_NOT_FOUND);

            PostTrainingSurvey postTrainingSurvey = Utils.jsonStringToObject(Utils.decode(encodedValue), PostTrainingSurvey.class);
            
            if (postTrainingSurvey.getUserId() != userLogin.getId())
                throw new PostTrainingSurveyParticipantException(Message.MSG_USER_LOGIN_NOT_CORRECT);
            
            Session session = sessionRepository.getSessionById(postTrainingSurvey.getSessionId());
            if(session == null)
                throw new PostTrainingSurveyParticipantException(Message.MSG_SESSION_NOT_FOUND);
                
            Course course = courseRepository.getCourseById(session.getCourseId());
            if (course == null)
                throw new PostTrainingSurveyParticipantException(Message.MSG_COURSE_NOT_FOUND);
            
            SessionParticipant sessionParticipant = sessionParticipantRepository.getSessionParticipant(session.getId(), postTrainingSurvey.getUserId());
            if(sessionParticipant == null)
                throw new PostTrainingSurveyParticipantException(Message.MSG_SESSION_PARTICIPANT_NOT_FOUND);
            
            if(isOutOfExpiredDate(sessionParticipant))
                throw new PostTrainingSurveyParticipantException(Message.MSG_OUT_OF_EXPIRED_DATE);
            
            return Utils.generateSuccessResponseEntity(getPostTrainingSurveyInformation(course, session, sessionParticipant));
        } catch (PostTrainingSurveyParticipantException e) {
            e.printStackTrace();
            return Utils.generateFailureResponseEntity(ErrorCode.CODE_POST_TRAINING_SURVEY_PARTICIPANT_EXCEPTION, e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            return Utils.generateFailureResponseEntity(ErrorCode.CODE_POST_TRAINING_SURVEY_PARTICIPANT_EXCEPTION, Message.MSG_POST_SURVEY_NOT_AVAILABE);
        }
    }

    private boolean isOutOfExpiredDate(SessionParticipant sessionParticipant){
        Timestamp toDay = new Timestamp(System.currentTimeMillis());
        
        if(sessionParticipant.getCheckoutAt() == null)
            return true;
        if(toDay.after(new Timestamp(sessionParticipant.getCheckoutAt().getTime() + (EXPERIED_DAYS * 24 * 60 * 60 * 1000))))
            return true;
        
        return false;
    }
    
    private PostTrainingSurveyInformation getPostTrainingSurveyInformation(Course course, Session session, SessionParticipant sessionParticipant){
        PostTrainingSurveyInformation information = new PostTrainingSurveyInformation();
        information.setCourseName(course.getName());
        information.setSessionId(session.getId());
        information.setSessionName(session.getName());
        information.setTrainerName(session.getTrainerFullName());
        information.setUserId(sessionParticipant.getUserId());
        Location location = locationRepository.getLocationById(session.getLocationId());
        information.setLocation(location == null ? "" : location.getName());
        information.setDuration(Common.getDateDiff(session.getStartTime(), session.getEndTime()));

        return information;
    }

}
