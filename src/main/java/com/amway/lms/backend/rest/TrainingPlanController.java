package com.amway.lms.backend.rest;

import java.sql.SQLException;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.amway.lms.backend.entity.TrainingPlan;
import com.amway.lms.backend.entity.User;
import com.amway.lms.backend.exception.DeleteObjectException;
import com.amway.lms.backend.exception.EditObjectException;
import com.amway.lms.backend.exception.ObjectNotFoundException;
import com.amway.lms.backend.service.TrainingPlanService;

@RestController
public class TrainingPlanController {
    private static final Logger logger = LoggerFactory
            .getLogger(TrainingPlanController.class);

    @Autowired
    private TrainingPlanService trainingPlanService;

    @RequestMapping(value = "/api/v1/trainingPlans/{trainingPlanId}/courses", method = RequestMethod.GET)
    public ResponseEntity<?> getCourseListByPlanAndStatus(
            @PathVariable int trainingPlanId,
            @RequestParam(name = "sessionStatus", required = false) Integer sessionStatus)
            throws ObjectNotFoundException, SQLException, Exception {
        logger.info("TrainingPlanController********getCourseListByPlanAndStatus");
        if (sessionStatus == null) {
            return this.trainingPlanService.getCourseListByPlan(trainingPlanId);
        } else {
            return this.trainingPlanService.getCourseListByPlanAndStatus(
                    trainingPlanId, sessionStatus);
        }
    }
    
    @RequestMapping(value = "/api/v1/trainingPlans/coursesStatus", method = RequestMethod.GET)
    public ResponseEntity<?> getCoursesStatusList(
            @RequestParam(name = "trainingPlanId", required = true) int trainingPlanId,
            @RequestParam(name = "sessionStatus", required = true) int sessionStatus)
            throws ObjectNotFoundException, SQLException, Exception {
        logger.info("TrainingPlanController********getCourseStatusList");
        return this.trainingPlanService.getCoursesStatusList(trainingPlanId, sessionStatus);
    }

    @RequestMapping(value = "/api/v1/trainingPlans", method = RequestMethod.POST)
    public ResponseEntity<?> addTrainingPlan(
            @RequestBody final TrainingPlan trainingPlan) throws SQLException,
            Exception {
        logger.info("TrainingPlanController***addTrainingPlan: " + trainingPlan);
        return trainingPlanService.addTrainingPlan(trainingPlan);
    }

    @RequestMapping(value = "/api/v1/trainingPlans", method = RequestMethod.GET)
    public ResponseEntity<?> getTrainingPlans(
            @RequestParam(name = "q", required = false) String planName)
            throws SQLException, Exception {
        logger.info("TrainingPlanController***getTrainingPlans");
        if (planName == null)
            return trainingPlanService.getTrainingPlans();
        else
            return trainingPlanService.searchTrainingPlans(planName);
    }

    @RequestMapping(value = "/api/v1/trainingPlans", method = RequestMethod.PUT)
    public ResponseEntity<?> updateTrainingPlan(
            @RequestBody final TrainingPlan trainingPlan)
            throws EditObjectException, SQLException, Exception {
        logger.info("TrainingPlanController***updateTrainingPlan:"
                + trainingPlan);
        return trainingPlanService.updateTrainingPlan(trainingPlan);

    }

    @RequestMapping(value = "/api/v1/trainingPlans", method = RequestMethod.DELETE)
    public ResponseEntity<?> delTrainingPlans(
            @RequestBody final List<TrainingPlan> trainingPlans)
            throws DeleteObjectException, SQLException, Exception {
        logger.info("TrainingPlanController***delTrainingPlans:"
                + trainingPlans);
        return trainingPlanService.delTrainingPlans(trainingPlans);

    }

    @RequestMapping(value = "/api/v1/trainingPlans/{trainingPlanId}", method = RequestMethod.GET)
    public ResponseEntity<?> getTrainingPlanById(
            @PathVariable int trainingPlanId) throws ObjectNotFoundException,
            SQLException, Exception {
        logger.info("TrainingPlanController***getTrainingPlanById:"
                + trainingPlanId);
        return trainingPlanService.getTrainingPlanById(trainingPlanId);
    }

    @RequestMapping(value = "/api/v1/trainingPlans/{trainingPlanId}/users", method = RequestMethod.GET)
    public ResponseEntity<?> getUsersOfTrainingPlan(
            @PathVariable int trainingPlanId,
            @RequestParam(name = "departmentId", required = false) Integer departmentId,
            @RequestParam(name = "existed", required = true) Integer existed)
            throws ObjectNotFoundException, SQLException, Exception {
        logger.info("TrainingPlanController***getUsersOfTrainingPlan:"
                + trainingPlanId);
        return trainingPlanService.getUsersOfTrainingPlan(trainingPlanId,
                existed, departmentId);
    }

    @RequestMapping(value = "/api/v1/trainingPlans/{trainingPlanId}/users", method = RequestMethod.POST)
    public ResponseEntity<?> addUsersToTrainingPlan(
            @PathVariable int trainingPlanId,
            @RequestBody final List<User> users)
            throws ObjectNotFoundException, SQLException, Exception {
        logger.info("TrainingPlanController***addUsersToTrainingPlan:"
                + trainingPlanId);
        return trainingPlanService
                .addUsersToTrainingPlan(trainingPlanId, users);
    }

    @RequestMapping(value = "/api/v1/trainingPlans/{trainingPlanId}/users", method = RequestMethod.DELETE)
    public ResponseEntity<?> delUsersFromTrainingPlan(
            @PathVariable int trainingPlanId,
            @RequestBody final List<User> users)
            throws ObjectNotFoundException, SQLException, Exception {
        logger.info("TrainingPlanController***addUsersToTrainingPlan:"
                + trainingPlanId);
        return trainingPlanService.delUsersFromTrainingPlan(trainingPlanId,
                users);
    }
}
