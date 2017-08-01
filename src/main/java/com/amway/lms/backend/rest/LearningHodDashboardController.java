package com.amway.lms.backend.rest;

import java.sql.SQLException;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.amway.lms.backend.exception.ObjectNotFoundException;
import com.amway.lms.backend.service.LearningDashboardService;

@RestController
@RequestMapping("/api/v1/hod")
public class LearningHodDashboardController {
    private static final Logger logger = LoggerFactory.getLogger(LearningSelfDashboardController.class);

    @Autowired
    private LearningDashboardService learningDashboardService;

    @RequestMapping(value = "/trainingAction/{hodId}/search", method = RequestMethod.GET)
    public ResponseEntity<?> getEmployeeAndCoursesByCode(@PathVariable("hodId") int hodId,
            @RequestParam(value = "staffCode", required = false) String staffCode) throws SQLException, Exception {
        logger.info("getEmployeeAndCoursesByCode: " + staffCode);
        return learningDashboardService.searchByHodStaffCode(hodId, staffCode);
    }

    @RequestMapping(value = "/roaldMap/courses/{hodId}", method = RequestMethod.GET)
    public ResponseEntity<?> getListCourseOfTrainee(@PathVariable("hodId") int hodId) throws ObjectNotFoundException {
        logger.debug("getListCourseOfTrainee with userId: " + hodId);
        List<Integer> userIds = learningDashboardService.getListUserIdOfHod(hodId);
        return learningDashboardService.getAllCourseBySuper(userIds);
    }

    @RequestMapping(value = "/optionalTraining/courses/{hodId}", method = RequestMethod.GET)
    public ResponseEntity<?> getCoursesInoptionalTraining(@PathVariable("hodId") int hodId)
            throws ObjectNotFoundException {
        logger.debug("getCoursesInoptionalTraining");
        List<Integer> userIds = learningDashboardService.getListApprovalManagerIdOfHod(hodId);
        return learningDashboardService.getListCourseCoursesApprovedByHR(userIds);
    }

    @RequestMapping(value = "/getAllEmployees/{hodId}", method = RequestMethod.GET)
    public ResponseEntity<?> getAllEmployeesByHod(@PathVariable("hodId") int hodId) {
        return learningDashboardService.getAllEmployeesByHod(hodId);
    }

    @RequestMapping(value = "/getEmployeesByDepartmentId/{hodId}/{departmentId}", method = RequestMethod.GET)
    public ResponseEntity<?> getEmployeesByDepartment(@PathVariable("hodId") int hodId,
            @PathVariable("departmentId") int departmentId) {
        return learningDashboardService.getEmployeesByDepartment(hodId, departmentId);
    }

    @RequestMapping(value = "/getEmployeesByApprovalManagerId/{approvalManagerId}/{departmentId}", method = RequestMethod.GET)
    public ResponseEntity<?> getEmployeesByApprovalManagerId(@PathVariable("approvalManagerId") int approvalManagerId,
            @PathVariable("departmentId") int departmentId) {
        return learningDashboardService.getEmployeesByApprovalManagerId(approvalManagerId, departmentId);
    }
}
