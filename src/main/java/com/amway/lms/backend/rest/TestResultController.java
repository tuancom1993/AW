package com.amway.lms.backend.rest;

import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.amway.lms.backend.exception.ObjectNotFoundException;
import com.amway.lms.backend.model.TestResultModel;
import com.amway.lms.backend.service.TestResultService;

@RestController
@RequestMapping("/api/v1")
public class TestResultController {

    private static final Logger logger = LoggerFactory
            .getLogger(TestResultController.class);

    @Autowired
    TestResultService testResultService;

    @PostMapping(value = "/testResults")
    public ResponseEntity<?> postTestResult(@RequestBody TestResultModel model) {
        return testResultService.testResult(model);
    }

    @GetMapping(value = "/testResults/{testId}")
    public ResponseEntity<?> viewTestResult(
            @PathVariable int testId,
            @RequestParam(name = "user", required = false) String user)
            throws ObjectNotFoundException, SQLException, Exception {
        return testResultService.viewTestResult(testId, user);

    }
}
