package com.amway.lms.backend.rest;

import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.amway.lms.backend.entity.Session;
import com.amway.lms.backend.exception.ObjectNotFoundException;
import com.amway.lms.backend.service.SessionService;

/**
 * @author: Hung (Charles) V. PHAM
 * */
@RestController
public class SessionController {
    private static final Logger logger = LoggerFactory
            .getLogger(SessionController.class);

    @Autowired
    private SessionService sessionService;

    
    @RequestMapping(value = "/api/v1/sessions", method = RequestMethod.POST)
    public ResponseEntity<?> addNewSession(@RequestBody final Session session)
            throws ObjectNotFoundException, SQLException, Exception {
        logger.info("CourseController********getCoursesList");
        return sessionService.addNewSession(session);
    }
    
    @RequestMapping(value = "/api/v1/sessions", method = RequestMethod.PUT)
    public ResponseEntity<?> updateSession(
            @RequestBody final Session session)
            throws ObjectNotFoundException, SQLException, Exception {
        logger.info("CourseController********getCoursesList");
        return sessionService.updateSession(session);
    }
    
    @RequestMapping(value = "/api/v1/sessions/{sessionId}", method = RequestMethod.DELETE)
    public ResponseEntity<?> deleteSession( @PathVariable Integer sessionId)
            throws ObjectNotFoundException, SQLException, Exception {
        logger.info("CourseController********getCoursesList");
        return sessionService.deleteSession(sessionId);
    }
    
    
    @RequestMapping(value = "/api/v1/sessions/{sessionId}", method = RequestMethod.GET)
    public ResponseEntity<?> getSessionDetail(
            @PathVariable Integer sessionId)
            throws ObjectNotFoundException, SQLException, Exception {
        logger.info("CourseController********getCoursesList");
        return sessionService.getSessionDetail(sessionId);
    }
}
