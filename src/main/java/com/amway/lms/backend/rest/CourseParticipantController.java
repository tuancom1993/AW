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

import com.amway.lms.backend.entity.CourseParticipant;
import com.amway.lms.backend.entity.User;
import com.amway.lms.backend.service.CourseParticipantService;

@RestController
public class CourseParticipantController {
    private static final Logger logger = LoggerFactory
            .getLogger(CourseParticipantController.class);

    @Autowired
    CourseParticipantService courseParticipantService;
    
    @RequestMapping(value = "/api/v1/courseParticipants/{courseId}", method = RequestMethod.GET)
    public ResponseEntity<?> getCourseParticipantList(@PathVariable int courseId)
            throws SQLException, Exception {
        logger.debug("CourseParticipantController****Method getCourseParticipantList");
        
        return courseParticipantService.getCourseParticipantsByCourseId(courseId);
    }
    // Get CoursePaticcipants by Course included Users, Department
    

    // Get users/participants by courseId and confirmation status
    @RequestMapping(value = "/api/v1/courseParticipants/{courseId}/users", method = RequestMethod.GET)
    public ResponseEntity<?> getUsersByCourse(
            @PathVariable int courseId,
            @RequestParam(name = "confirmationStatus", required = false) Integer confirmationStatus,
            @RequestParam(name = "departmentId", required = false) Integer departmentId)
            throws SQLException, Exception {
        logger.debug("CourseParticipantController****Method getUsersByCourse: confirmationsStatus = "
                + confirmationStatus + " departmentId = " + departmentId);
        
        return courseParticipantService.getUsersByCourseId(courseId,
                confirmationStatus, departmentId);
    }

    // Add Participants to Course by courseId
    @RequestMapping(value = "/api/v1/courseParticipants/{courseId}", method = RequestMethod.POST)
    public ResponseEntity<?> addCoursePaticipant(@PathVariable int courseId,
            @RequestBody final List<User> users) throws SQLException, Exception {
        logger.info("Add Participants to Course: " + users);
        return courseParticipantService.addCoursePaticipant(courseId, users);
    }

    // Delete Participants out of Course by courseId
    @RequestMapping(value = "/api/v1/courseParticipants/{courseId}", method = RequestMethod.DELETE)
    public ResponseEntity<?> delCoursePaticipant(@PathVariable int courseId,
            @RequestBody final List<User> users) throws SQLException, Exception {
        logger.info("Add Participants to Course: " + users);
        return courseParticipantService.delCoursePaticipant(courseId, users);
    }

    @RequestMapping(value = "/api/v1/courseParticipants/checkin", method = RequestMethod.PUT)
    public ResponseEntity<?> updateCheckIn(
            @RequestBody final CourseParticipant courseParticipant) {
        logger.info("Method updateCheckIn" + courseParticipant);
        return courseParticipantService.updateCheckIn(courseParticipant);
    }

    @RequestMapping(value = "/api/v1/courseParticipants/checkout", method = RequestMethod.PUT)
    public ResponseEntity<?> updateCheckOut(
            @RequestBody final CourseParticipant courseParticipant) {
        logger.info("Method updateCheckIn" + courseParticipant);
        return courseParticipantService.updateCheckOut(courseParticipant);
    }
}
