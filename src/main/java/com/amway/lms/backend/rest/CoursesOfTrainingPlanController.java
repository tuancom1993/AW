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

import com.amway.lms.backend.entity.Course;
import com.amway.lms.backend.service.CourseOfTrainingPlanService;
import com.amway.lms.backend.service.CourseService;

@RestController
public class CoursesOfTrainingPlanController { 
    private static final Logger logger = LoggerFactory.getLogger(CoursesOfTrainingPlanController.class);
    
    @Autowired
    CourseOfTrainingPlanService courseOfTrainingPlanService;
    
    @Autowired
    CourseService courseService;
    
    // Add Courses to Training Plan by trainingPlanId
    @RequestMapping(value = "/api/v1/courseOfTrainingPlan/{trainingPlanId}", method = RequestMethod.POST)
    public ResponseEntity<?> addCourseOfTrainingPlan(@PathVariable int trainingPlanId,
            @RequestBody final List<Course> courses) throws SQLException, Exception {
        logger.info("Add Participants to Course: " + courses);
        return courseOfTrainingPlanService.addCourseOfTrainingPlan(trainingPlanId, courses);
    }
    
    @RequestMapping(value = "/api/v1/courseOfTrainingPlan/{trainingPlanId}/courses", method = RequestMethod.GET)
    public ResponseEntity<?> getCoursesOfTrainingPlan(@PathVariable int trainingPlanId) throws SQLException, Exception {
        logger.info("CoursesOfTrainingPlanController*********getCoursesOfTrainingPlan:");
        return courseService.getCoursesOfTrainingPlan(trainingPlanId);
    }
    
    @RequestMapping(value = "/api/v1/courseOfTrainingPlan/{trainingPlanId}/courses", method = RequestMethod.DELETE)
    public ResponseEntity<?> removeCoursesOfTrainingPlan(@PathVariable int trainingPlanId,
            @RequestBody final List<Course> courses) throws SQLException, Exception {
        logger.info("CoursesOfTrainingPlanController*********getCoursesOfTrainingPlan:");
        return courseOfTrainingPlanService.removeCoursesOfTrainingPlan(trainingPlanId, courses);
    }
    
    @RequestMapping(value = "/api/v1/courseOfTrainingPlan/{trainingPlanId}/courses", method = RequestMethod.PUT)
    public ResponseEntity<?> setMandatoryForCourses(@PathVariable int trainingPlanId,
            @RequestParam(name = "isRequired", required = true) int isRequired,
            @RequestBody final List<Course> courses) throws SQLException, Exception {
        logger.info("CoursesOfTrainingPlanController*********getCoursesOfTrainingPlan:");
        return courseOfTrainingPlanService.setMandatoryForCourses(trainingPlanId, courses, isRequired);
    }

}
