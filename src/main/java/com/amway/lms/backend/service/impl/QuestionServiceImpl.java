/**
 * 
 */
package com.amway.lms.backend.service.impl;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.multipart.MultipartFile;

import com.amway.lms.backend.common.AmwayEnum.QuestionTypes;
import com.amway.lms.backend.common.Common;
import com.amway.lms.backend.common.ErrorCode;
import com.amway.lms.backend.common.Utils;
import com.amway.lms.backend.entity.Question;
import com.amway.lms.backend.entity.Quiz;
import com.amway.lms.backend.entity.Survey;
import com.amway.lms.backend.entity.SurveySession;
import com.amway.lms.backend.exception.ObjectNotFoundException;
import com.amway.lms.backend.exception.UploadFileException;
import com.amway.lms.backend.repository.AnswerRepository;
import com.amway.lms.backend.repository.MatrixColumnRepository;
import com.amway.lms.backend.repository.MatrixRowRepository;
import com.amway.lms.backend.repository.QuestionRepository;
import com.amway.lms.backend.repository.QuizQuestionRepository;
import com.amway.lms.backend.repository.QuizRepository;
import com.amway.lms.backend.repository.SurveyQuestionRepository;
import com.amway.lms.backend.repository.SurveyRepository;
import com.amway.lms.backend.repository.SurveySessionRepository;
import com.amway.lms.backend.service.QuestionService;

/**
 * @author acton
 * @email acton@enclave.vn
 */
@Service
@Transactional
public class QuestionServiceImpl implements QuestionService {

    private static final Logger logger = LoggerFactory.getLogger(QuestionServiceImpl.class);
    

    @Value("${question.id.survey.training.need.default}")
    private String questionIdSurveyTrainingNeedDefault;
    
    @Value("${question.id.survey.pre.training.default}")
    private String questionIdSurveyPreTrainingDefault;

    @Autowired
    QuestionRepository questionRepository;

    @Autowired
    SurveyQuestionRepository surveyQuestionRepository;

    @Autowired
    SurveySessionRepository surveySessionRepository;

    @Autowired
    SurveyRepository surveyRepository;

    @Autowired
    QuizRepository quizRepository;

    @Autowired
    AnswerRepository answerRepository;

    @Autowired
    MatrixColumnRepository matrixColumnRepository;

    @Autowired
    MatrixRowRepository matrixRowRepository;
    
    @Autowired
    QuizQuestionRepository quizQuestionRepository;

    @Override
    public ResponseEntity<?> getQuestionListBySurveyId(int surveyId) throws Exception {
        Survey survey = surveyRepository.getSurveyById(surveyId);
        if (survey == null)
            throw new ObjectNotFoundException("Cannot find Survey with id = " + surveyId);
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
                } else if (question.getQuestionTypeId() == QuestionTypes.MATRIX.getValue()){
                    question.setMatrixRows(matrixRowRepository.getMatrixRowListByQuestionId(questionId));
                    question.setMatrixColumns(matrixColumnRepository.getMatrixColumnListByQuestionId(questionId));
                }
            }
            sessions.add(surveySession);
        }
        return Utils.generateSuccessResponseEntity(sessions);
    }

    @Override
    public ResponseEntity<?> uploadFileForQuestion(MultipartFile file, String rootPath) throws IOException, Exception {
        try {
            if (file == null)
                throw new UploadFileException("File is null");

            if (!new File(rootPath + Common.AUDIOS_PATH).exists()) {
                logger.debug("Root folder is not exists");
                new File(rootPath + Common.AUDIOS_PATH).mkdirs();
                logger.debug("Root folder created");
            }
            if (!new File(rootPath + Common.IMAGES_PATH).exists()) {
                logger.debug("Root folder is not exists");
                new File(rootPath + Common.IMAGES_PATH).mkdirs();
                logger.debug("Root folder created");
            }
            if (!new File(rootPath + Common.VIDEOS_PATH).exists()) {
                logger.debug("Root folder is not exists");
                new File(rootPath + Common.VIDEOS_PATH).mkdirs();
                logger.debug("Root folder created");
            }

            String extension = Common.getExtension(file.getOriginalFilename());
            String fileNameNew = String.valueOf(System.currentTimeMillis()) + "." + extension;
            String fileLocation = "";
            if (Common.isContain(extension, Common.ARR_IMAGES_EXTENSION)) {
                fileLocation = Common.IMAGES_PATH + "/" + fileNameNew;
            } else if (Common.isContain(extension, Common.ARR_AUDIOS_EXTENSION)) {
                fileLocation = Common.AUDIOS_PATH + "/" + fileNameNew;
            } else if (Common.isContain(extension, Common.ARR_VIDEOS_EXTENSION)) {
                fileLocation = Common.VIDEOS_PATH + "/" + fileNameNew;
            } else
                throw new UploadFileException("File extension is not support");

            File fileToSave = new File(rootPath + fileLocation);
            BufferedOutputStream stream = null;
            try {
                stream = new BufferedOutputStream(new FileOutputStream(fileToSave));
                FileCopyUtils.copy(file.getInputStream(), stream);
                logger.debug("File " + fileNameNew + " uploaded");
            } catch (Exception e) {
                e.printStackTrace();
                throw new UploadFileException("File cannot save");
            } finally {
                try {
                    stream.close();
                } catch (Exception e2) {
                    e2.printStackTrace();
                }
            }
            Question question = new Question();
            question.setFileLocation(fileLocation);
            return Utils.generateSuccessResponseEntity(new FileLocation(fileLocation));
        } catch (Exception e) {
            e.printStackTrace();
            return Utils.generateFailureResponseEntity(ErrorCode.CODE_UPLOAD_FILE_EXCEPTION,
                    ErrorCode.MSG_UPLOAD_FILE_EXCEPTION);
        }

    }

    public class FileLocation {
        private String fileLocation;

        public FileLocation(String fileLocation) {
            super();
            this.fileLocation = fileLocation;
        }

        public String getFileLocation() {
            return fileLocation;
        }

        public void setFileLocation(String fileLocation) {
            this.fileLocation = fileLocation;
        }

    }

    @Override
    public ResponseEntity<?> getQuestionListNotInSurvey(int surveyId) throws Exception {
        List<Question> questions = questionRepository.getQuestionListNotInSurvey(surveyId);
        
        ArrayList<Integer> listQuestionDefault = new ArrayList<>();
        listQuestionDefault.addAll(convertListStringQuestionDefaltToArray(questionIdSurveyPreTrainingDefault));
        listQuestionDefault.addAll(convertListStringQuestionDefaltToArray(questionIdSurveyTrainingNeedDefault));
        
        Iterator<Question> it = questions.iterator();
        while(it.hasNext()){
            Question question = it.next();
            if(listQuestionDefault.contains(question.getId()))
                it.remove();
        }
        
        for (Question question : questions) {
            int questionId = question.getId();
            if (question.getQuestionTypeId() == QuestionTypes.RADIO.getValue() 
                    || question.getQuestionTypeId() == QuestionTypes.CHECKBOX.getValue()
                    || question.getQuestionTypeId() == QuestionTypes.CHECKBOX_OTHER.getValue() 
                    || question.getQuestionTypeId() == QuestionTypes.DROP_DOWN_LIST.getValue()) {
                question.setAnswers(answerRepository.getAnswerListByQuestionId(questionId));
            } else if (question.getQuestionTypeId() == QuestionTypes.MATRIX.getValue()){
                question.setMatrixRows(matrixRowRepository.getMatrixRowListByQuestionId(questionId));
                question.setMatrixColumns(matrixColumnRepository.getMatrixColumnListByQuestionId(questionId));
            }
        }
        return Utils.generateSuccessResponseEntity(questions);
    }

    @Override
    public ResponseEntity<?> getQuestionListNotInQuiz(int quizId) throws Exception {
        List<Question> questions = questionRepository.getQuestionListNotInQuiz(quizId);
        for (Question question : questions) {
            int questionId = question.getId();
            if (question.getQuestionTypeId() == QuestionTypes.RADIO.getValue() 
                    || question.getQuestionTypeId() == QuestionTypes.CHECKBOX.getValue()
                    || question.getQuestionTypeId() == QuestionTypes.CHECKBOX_OTHER.getValue() 
                    || question.getQuestionTypeId() == QuestionTypes.DROP_DOWN_LIST.getValue()) {
                question.setAnswers(answerRepository.getAnswerListByQuestionId(questionId));
            } else if (question.getQuestionTypeId() == QuestionTypes.MATRIX.getValue()){
                question.setMatrixRows(matrixRowRepository.getMatrixRowListByQuestionId(questionId));
                question.setMatrixColumns(matrixColumnRepository.getMatrixColumnListByQuestionId(questionId));
            }
        }
        return Utils.generateSuccessResponseEntity(questions);
    }

    @Override
    public ResponseEntity<?> getQuestionListByQuizId(int quizId) throws Exception {
        Quiz quiz = quizRepository.getQuizById(quizId);
        if (quiz == null)
            throw new ObjectNotFoundException(
                    "Cannot get list Question of Quiz. Cause not found Quiz by id =" + quizId);
        
        List<Question> questions = questionRepository.getQuestionListByQuizId(quizId);
        for (Question question : questions) {
            int questionId = question.getId();
            if (question.getQuestionTypeId() == QuestionTypes.RADIO.getValue() 
                    || question.getQuestionTypeId() == QuestionTypes.CHECKBOX.getValue()
                    || question.getQuestionTypeId() == QuestionTypes.CHECKBOX_OTHER.getValue() 
                    || question.getQuestionTypeId() == QuestionTypes.DROP_DOWN_LIST.getValue()) {
                question.setAnswers(answerRepository.getAnswerListByQuestionId(questionId));
            } else if (question.getQuestionTypeId() == QuestionTypes.MATRIX.getValue()){
                question.setMatrixRows(matrixRowRepository.getMatrixRowListByQuestionId(questionId));
                question.setMatrixColumns(matrixColumnRepository.getMatrixColumnListByQuestionId(questionId));
            }
            question.setNo(quizQuestionRepository.getQuizQuestionByQuizIdAndQuestionId(quizId, questionId).getIndexQuestion());
        }
        
        return Utils.generateSuccessResponseEntity(questions);
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

    @Override
    public ResponseEntity<?> getQuestionListNotInTest(int testId) throws Exception {
        List<Question> questions = questionRepository.getQuestionListNotInTest(testId);
        for (Question question : questions) {
            int questionId = question.getId();
            if (question.getQuestionTypeId() == QuestionTypes.RADIO.getValue() 
                    || question.getQuestionTypeId() == QuestionTypes.CHECKBOX.getValue()
                    || question.getQuestionTypeId() == QuestionTypes.CHECKBOX_OTHER.getValue() 
                    || question.getQuestionTypeId() == QuestionTypes.DROP_DOWN_LIST.getValue()) {
                question.setAnswers(answerRepository.getAnswerListByQuestionId(questionId));
            } else if (question.getQuestionTypeId() == QuestionTypes.MATRIX.getValue()){
                question.setMatrixRows(matrixRowRepository.getMatrixRowListByQuestionId(questionId));
                question.setMatrixColumns(matrixColumnRepository.getMatrixColumnListByQuestionId(questionId));
            }
        }
        return Utils.generateSuccessResponseEntity(questions);
    }

}
