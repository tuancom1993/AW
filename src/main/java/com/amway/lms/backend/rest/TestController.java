package com.amway.lms.backend.rest;

import java.io.File;
import java.io.FileInputStream;
import java.sql.SQLException;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.apache.poi.util.IOUtils;
import org.hibernate.HibernateException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.DeleteMapping;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import com.amway.lms.backend.common.ErrorCode;
import com.amway.lms.backend.common.Utils;
import com.amway.lms.backend.configuration.CustomUserDetail;
import com.amway.lms.backend.entity.Test;
import com.amway.lms.backend.entity.User;
import com.amway.lms.backend.model.TestModelForCreateEdit;
import com.amway.lms.backend.service.TestService;

@RestController
@RequestMapping("/api/v1")
public class TestController {

    private static final Logger logger = LoggerFactory.getLogger(TestController.class);

    @Autowired
    private TestService testService;

    @RequestMapping(value = "/tests/full", method = RequestMethod.POST)
    public ResponseEntity<?> createTestFull(@RequestBody TestModelForCreateEdit testModelForCreateEdit)
            throws Exception {
        return testService.createTestFull(testModelForCreateEdit);
    }

    @RequestMapping(value = "/tests/full", method = RequestMethod.PUT)
    public ResponseEntity<?> editTestFull(@RequestBody TestModelForCreateEdit testModelForCreateEdit) throws Exception {
        return testService.editTestFull(testModelForCreateEdit);
    }

    @RequestMapping(value = "/tests/manage", method = RequestMethod.GET)
    public ResponseEntity<?> getTestList() throws Exception {
        logger.info("ManageTestController********getTestList");
        return testService.getTestList();
    }

    @RequestMapping(value = "/tests/manage/export/{testId}", method = RequestMethod.GET)
    public void exportTest(HttpServletResponse response, @PathVariable("testId") int testId)
            throws HibernateException, SQLException, Exception {
        try {
            File exportFile = testService.exportTestHorizontal(testId);
            if(exportFile == null) throw new Exception("Cannot export file");
            String fileName = exportFile.getName();
            response.setContentType("application/octet-stream; charset=UTF-8");
            response.setCharacterEncoding("UTF-8");
            response.setHeader("Content-Disposition", "inline; filename=" + fileName);
            IOUtils.copy(new FileInputStream(exportFile), response.getOutputStream());
            response.flushBuffer();
        } catch (Exception e) {
            e.printStackTrace();            
            response.getWriter().printf(Utils.generateFailureResponseString(ErrorCode.CODE_EXPORT_FILE_EXCEPTION, ErrorCode.MSG_EXPORT_FILE_EXCEPTION)).flush();
        }
    }

    @PutMapping(value = "/tests/clone")
    public ResponseEntity<?> cloneTest(@RequestBody Test test) throws Exception {
        logger.info("ManageTestController********cloneTest");
        return testService.cloneTest(test.getId());
    }

    @DeleteMapping(value = "/tests/manage")
    public ResponseEntity<?> deleteTests(@RequestBody List<Test> tests) throws Exception {
        logger.info("ManageTestController********deleteTests");
        return testService.deleteTest(tests);
    }

    @GetMapping(value="/tests/genURL/{testId}/{testParticipantId}")
    public String genTestURL(@PathVariable("testId") int testId, @PathVariable("testParticipantId") int testParticipantId) throws Exception {
        logger.info("ManageTestController********genTestURL");
        return testService.genURL(testId, testParticipantId);
    }
    
    @GetMapping(value = "/tests/{testId}")
    public ResponseEntity<?>getTestsByTestId(@PathVariable("testId") int testId) throws Exception {
        logger.info("ManageTestController********getTestsByTestId");
        return testService.getTestModelFull(testId);
    }
    
    @GetMapping(value= "/tests/start/{encodeValue}")
    public ResponseEntity<?> startTest(@PathVariable("encodeValue") String encodeValue) throws Exception{
        User userLogin = null;
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof CustomUserDetail) {
            CustomUserDetail customUserDetails = (CustomUserDetail) principal;
            userLogin = customUserDetails.getUser();
        }
        return testService.startTest(userLogin, encodeValue);
    }
    
    @GetMapping(value = "/tests/{testId}/infor")
    public ResponseEntity<?>getTestInformationByTestId(@PathVariable("testId") int testId) throws Exception {
        logger.info("ManageTestController********getTestsByTestId");
        return testService.getTestEntityInfor(testId);
    }
    
    @GetMapping(value="/tests/info/{userId}")
    public ResponseEntity<?> getTestInfo(@PathVariable("userId") int userId) throws Exception{
        return testService.getTestInfo(userId);
    }
}
