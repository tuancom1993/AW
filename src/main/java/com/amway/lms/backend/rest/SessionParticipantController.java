package com.amway.lms.backend.rest;

import java.io.IOException;
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
import org.springframework.web.multipart.MultipartFile;

import com.amway.lms.backend.common.AmwayEnum;
import com.amway.lms.backend.entity.SessionParticipant;
import com.amway.lms.backend.entity.User;
import com.amway.lms.backend.service.SessionParticipantService;

/**
 * @author: Hung (Charles) V. PHAM
 */
@RestController
public class SessionParticipantController {
    private static final Logger logger = LoggerFactory.getLogger(SessionParticipantController.class);

    @Autowired
    SessionParticipantService sessionParticipantService;

    // Add Participants to Session by sesisonId
    @RequestMapping(value = "/api/v1/sessionParticipants/{sessionId}", method = RequestMethod.POST)
    public ResponseEntity<?> addSessionPaticipant(@PathVariable int sessionId, @RequestBody final List<User> users)
            throws SQLException, Exception {
        logger.info("Add Participants to Course: " + users);
        return sessionParticipantService.addSessionPaticipant(sessionId, users);
    }

    // Import Participants to Session by sesisonId
    @RequestMapping(value = "/api/v1/sessionParticipants/import", headers = ("content-type=multipart/*"), method = RequestMethod.POST)
    public ResponseEntity<?> importParticipant(@RequestParam("file-participant") MultipartFile participantFile,
            @RequestParam("sessionId") int sessionId) throws IOException, Exception {
        return sessionParticipantService.importParticipant(participantFile, sessionId);
    }

    // Delete Participants out of Course by courseId
    @RequestMapping(value = "/api/v1/sessionParticipants/{sessionId}", method = RequestMethod.DELETE)
    public ResponseEntity<?> delCoursePaticipant(@PathVariable int sessionId, @RequestBody final List<User> users)
            throws SQLException, Exception {
        logger.info("Add Participants to Course: " + users);
        return sessionParticipantService.delSessionPaticipant(sessionId, users);
    }

    // Get users/participants by sessionId and confirmation status
    @RequestMapping(value = "/api/v1/sessionParticipants/{sessionId}/users", method = RequestMethod.GET)
    public ResponseEntity<?> getUsersBySessionId(@PathVariable int sessionId,
            @RequestParam(name = "confirmationStatus", required = false) Integer confirmationStatus,
            @RequestParam(name = "departmentId", required = false) Integer departmentId)
            throws SQLException, Exception {
        logger.debug("SessionParticipantController****Method getUsersBySession: confirmationsStatus = "
                + confirmationStatus + " departmentId = " + departmentId);

        return sessionParticipantService.getUsersBySessionId(sessionId, confirmationStatus, departmentId);
    }

    // Get users accepted/participants by sessionId and completion status
    @RequestMapping(value = "/api/v1/sessionParticipants/{sessionId}/usersAccepted", method = RequestMethod.GET)
    public ResponseEntity<?> getUsersAcceptedBySessionId(@PathVariable int sessionId) throws SQLException, Exception {
        logger.debug("SessionParticipantController****Method getUsersBySession");

        return sessionParticipantService.getUsersAcceptedBySessionId(sessionId);
    }

    // Get users/participants by sessionId and to checkin
    @RequestMapping(value = "/api/v1/sessionParticipants/{sessionId}/users/inout", method = RequestMethod.GET)
    public ResponseEntity<?> getUsersCheckinCheckout(@PathVariable int sessionId,
            @RequestParam(name = "name", required = false) String name) throws SQLException, Exception {
        logger.debug("SessionParticipantController****Method getUsersCheckInBySessionId: name = " + name);

        return sessionParticipantService.getUsersCheckinCheckout(sessionId, name);
    }

    // Get users/participants by sessionId and to checkin
    @RequestMapping(value = "/api/v1/sessionParticipants/{sessionId}/users/checkin", method = RequestMethod.GET)
    public ResponseEntity<?> getUsersCheckInBySessionId(@PathVariable int sessionId,
            @RequestParam(name = "name", required = false) String name) throws SQLException, Exception {
        logger.debug("SessionParticipantController****Method getUsersCheckInBySessionId: name = " + name);

        return sessionParticipantService.getUsersCheckInOutBySessionId(sessionId, name,
                AmwayEnum.CompletionStatus.ACCEPTED.getValue());
    }

    // Get users/participants by sessionId and to checkout
    @RequestMapping(value = "/api/v1/sessionParticipants/checkin", method = RequestMethod.PUT)
    public ResponseEntity<?> userCheckIn(@RequestBody final SessionParticipant sessionParticipant)
            throws SQLException, Exception {
        logger.debug("SessionParticipantController****Method userCheckIn: sessionId = "
                + sessionParticipant.getSessionId() + " --- userId = " + sessionParticipant.getUserId());

        return sessionParticipantService.userCheckIn(sessionParticipant);
    }

    // Get users/participants by sessionId and to checkout
    @RequestMapping(value = "/api/v1/sessionParticipants/{sessionId}/users/checkout", method = RequestMethod.GET)
    public ResponseEntity<?> getUsersCheckOutBySessionId(@PathVariable int sessionId,
            @RequestParam(name = "name", required = false) String name) throws SQLException, Exception {
        logger.debug("SessionParticipantController****Method getUsersCheckInBySessionId: name = " + name);

        return sessionParticipantService.getUsersCheckInOutBySessionId(sessionId, name,
                AmwayEnum.CompletionStatus.PARTICIPATED.getValue());
    }

    // Get users/participants by sessionId and to checkin
    @RequestMapping(value = "/api/v1/sessionParticipants/checkout", method = RequestMethod.PUT)
    public ResponseEntity<?> userCheckOut(@RequestBody final SessionParticipant sessionParticipant)
            throws SQLException, Exception {
        logger.debug("SessionParticipantController****Method userCheckIn: sessionId = "
                + sessionParticipant.getSessionId() + " --- userId = " + sessionParticipant.getUserId());

        return sessionParticipantService.userCheckOut(sessionParticipant);
    }

    @RequestMapping(value = "/api/v1/sessionParticipants/status/{userId}", method = RequestMethod.GET)
    public ResponseEntity<?> getStatusList(@PathVariable int userId,
            @RequestParam(name = "trainingPlanId", required = true) int trainingPlanId,
            @RequestParam(name = "year", required = true) int year)
            throws SQLException, Exception {
        logger.debug("SessionParticipantController****Method getStatusList: userId = "
                + userId);

        return sessionParticipantService.getStatusList(userId, trainingPlanId, year);
    }

}
