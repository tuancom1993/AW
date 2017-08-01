package com.amway.lms.backend.common;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import com.amway.lms.backend.common.AmwayEnum.TestResultStatus;
import com.amway.lms.backend.common.AmwayEnum.TestStatus;
import com.amway.lms.backend.entity.Department;
import com.amway.lms.backend.entity.User;
import com.amway.lms.backend.model.Employee;

public class Common {

    public static final int ITEMS_PER_PAGE = 5;

    public static final String PATH_FOLDER_AMWAY_RESOURCES_EXTERNAL = "/amway-resources";
    public static final String IMAGES_PATH = "/questions/images";
    public static final String VIDEOS_PATH = "/questions/videos";
    public static final String AUDIOS_PATH = "/questions/audios";
    public static final String SURVEY_RESULT_PATH = "/files/surveyresult/temp";

    public static final String[] ARR_IMAGES_EXTENSION = { "jpg", "jpeg", "png",
            "svg" };
    public static final String[] ARR_VIDEOS_EXTENSION = { "flv", "avi", "mov",
            "mp4", "mpg", "mkv", "3gp", "m4v", "wav" };
    public static final String[] ARR_AUDIOS_EXTENSION = { "mp3", "wma" };

    public static final String PATH_TRAINING_NEED_SURVEY = "/trainee-survey#";
    public static final String PATH_PRE_TRAINING_SURVEY = "/pre-training-survey#";
    
    public static final String DATE_FORMAT_DD_MMM_YYYY = "dd-MMM-yyyy";
    public static final String DATE_TIME_FORMAT_DD_MMM_YYYY = "dd-MMM-yyyy hh:mm:ss";
    public static final String DATE_TIME_FORMAT_DD_MMM_YYYY_AM_PM = "dd-MMM-yyyy hh:mm a";
    
    public static final String FILE_NAME_SURVEY_EXPORT = "SurveyResult";
    public static final String FILE_NAME_TEST_EXPORT = "TestResult";
    public static final String FILE_NAME_POST_TRAINING_SURVEY_PARTICIPANT_EXPORT = "Post Training Survey For Participant.xlsx";
    public static final String FILE_NAME_POST_TRAINING_SURVEY_TRAINER_EXPORT = "Post Training Survey For Trainer.xlsx";
    public static final String MSG_QUESTION_NO_DATA = "There is no data in this question";

    public static int getFirstItem(int currentPage) {
        return (currentPage - 1) * ITEMS_PER_PAGE;
    }

    public static boolean isContain(String extension, String[] arrExtensions) {
        if (extension == null || arrExtensions == null)
            return false;
        for (String extInArray : arrExtensions) {
            if (extension.toLowerCase().equals(extInArray.toLowerCase())) {
                return true;
            }
        }
        return false;
    }

    public static String getExtension(String originalFilename) {
        String[] arrString = originalFilename.split("\\.");
        try {
            return arrString[arrString.length - 1];
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static Timestamp setTimeToZero(Timestamp timestamp) {
        Calendar calStart = new GregorianCalendar();
        calStart.setTimeInMillis(timestamp.getTime());
        calStart.set(Calendar.HOUR_OF_DAY, 0);
        calStart.set(Calendar.MINUTE, 0);
        calStart.set(Calendar.SECOND, 0);
        calStart.set(Calendar.MILLISECOND, 0);
        // System.out.println("*****Date: "+calStart.getTime());
        return new Timestamp(calStart.getTimeInMillis());
    }

    public static Timestamp setTimeToMidnight(Timestamp timestamp) {
        timestamp = setTimeToZero(timestamp);
        return new Timestamp(timestamp.getTime() + (23 * 60 * 60 * 1000)
                + (3599 * 1000));
    }

    public static Timestamp setStringTimeToTimestamp(Timestamp date, String timeStr)
            throws ParseException {
        String strDateFormat = "hh:mm a";
        DateFormat formatter = new SimpleDateFormat(strDateFormat);
        Date time = formatter.parse(timeStr);
        date.setHours(time.getHours());
        date.setMinutes(time.getMinutes());
        return date;
    }
    
    public static String getStringDate(Timestamp timestamp, String strFormat) {
        SimpleDateFormat format = new SimpleDateFormat(strFormat);
        return format.format(timestamp);
    }
    
    public static Timestamp getTimeStampFromString(String strTime, String strFormat) throws ParseException{
        SimpleDateFormat format = new SimpleDateFormat(strFormat);
        return new Timestamp(format.parse(strTime).getTime());
    }
    
    public static Department getEmptyDepartment(){
        Department department = new Department();
        department.setName("N/A");
        return department;
    }
    
    public static User getEmptyUser(){
        User user = new User();
        user.setFirstName("N/A");
        user.setLastName("");
        return user;
    }
    
    public static Employee getEmployee(User user) {
        Employee employee = new Employee();
        employee.setEmployeeId(user.getId());
        employee.setFirstName(user.getFirstName());
        employee.setLastName(user.getLastName());
        employee.setFullName(new StringBuilder(user.getFirstName()).append(" ").append(user.getLastName()).toString());
        employee.setStaffCode(user.getStaffCode());
        employee.setJobTitle(user.getJobTitle());
        employee.setEmailAddress(user.getEmail());
        return employee;
    }
    
    public static String getDateDiff(Timestamp date1, Timestamp date2) {
        String result = "";
        long diff = date2.getTime() - date1.getTime();
        
        result += diff / (24 * 60 * 60 * 1000) + " days, ";
        result += diff / (60 * 60 * 1000) % 24 + " hours, ";
        result += diff / (60*1000) % 60 + " minutes";
        return result;
    }


    public static String getQuestionTypes(int questionTypeId) {
        if (questionTypeId == AmwayEnum.QuestionTypes.TEXT_BOX.getValue())
            return AmwayEnum.QuestionTypes.TEXT_BOX.getStrValue();
        else if (questionTypeId == AmwayEnum.QuestionTypes.RADIO.getValue())
            return AmwayEnum.QuestionTypes.RADIO.getStrValue();
        else if (questionTypeId == AmwayEnum.QuestionTypes.CHECKBOX.getValue())
            return AmwayEnum.QuestionTypes.CHECKBOX.getStrValue();
        else if (questionTypeId == AmwayEnum.QuestionTypes.CHECKBOX_OTHER.getValue())
            return AmwayEnum.QuestionTypes.CHECKBOX_OTHER.getStrValue();
        else if (questionTypeId == AmwayEnum.QuestionTypes.COMMENT.getValue())
            return AmwayEnum.QuestionTypes.COMMENT.getStrValue();
        else if (questionTypeId == AmwayEnum.QuestionTypes.DROP_DOWN_LIST.getValue())
            return AmwayEnum.QuestionTypes.DROP_DOWN_LIST.getStrValue();
        else if (questionTypeId == AmwayEnum.QuestionTypes.EMAIL.getValue())
            return AmwayEnum.QuestionTypes.EMAIL.getStrValue();
        else if (questionTypeId == AmwayEnum.QuestionTypes.MATRIX.getValue())
            return AmwayEnum.QuestionTypes.MATRIX.getStrValue();

        else
            return "";
    }
    
    public static String convertTestStatus(Integer value) {
        if (value == null) {
            return null;
        }
        if (value.equals(TestStatus.NOT_TAKEN_YET.getIntValue())) {
            return TestStatus.NOT_TAKEN_YET.getStrValue();
        }
        return TestStatus.TESTED.getStrValue();

    }

    public static String convertTestResultStatus(Integer value) {

        if (value == null) {
            return null;
        }
        if (value.equals(TestResultStatus.NOT_TAKEN_YET.getIntValue())) {
            return TestResultStatus.NOT_TAKEN_YET.getStrValue();
        } else if (value.equals(TestResultStatus.FAILED.getIntValue())) {
            return TestResultStatus.FAILED.getStrValue();
        }
        return TestResultStatus.PASSED.getStrValue();

    }
}
