package com.amway.lms.backend.rest;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.amway.lms.backend.model.Employee;
import com.amway.lms.backend.service.ParticipantService;

@RestController
@RequestMapping("/api/v1/participant")
public class ParticipantController {

	private static final Logger logger = LoggerFactory.getLogger(ParticipantController.class);

	@Autowired
	private ParticipantService participantService;

	@RequestMapping(value = "/newParticipant/{sessionId}", method = RequestMethod.GET)
	public ResponseEntity<?> getInforCourseAndSession(@PathVariable("sessionId") Integer sessionId) {
		logger.debug("newParticipant: sessionId={}", sessionId);
		return participantService.getInforCourseAndSession(sessionId);
	}

	@RequestMapping(value = "/newParticipantEmployee/{sessionId}/{departmentId}", method = RequestMethod.GET)
	public ResponseEntity<?> getEmployee(@PathVariable("sessionId") Integer sessionId, @PathVariable("departmentId") Integer departmentId) {
		logger.debug("newParticipant: participant={}", departmentId);
		return participantService.getEmployee(sessionId, departmentId);
	}

	@RequestMapping(value = "/submittedList/{sessionId}", method = RequestMethod.GET)
	public ResponseEntity<?> getSubmitedList(@PathVariable("sessionId") Integer sessionId) {
		return participantService.getSubmitedList(sessionId);
	}


	@RequestMapping(value = "/newParticipant", method = RequestMethod.POST)
	public ResponseEntity<?> newParticipant(@RequestBody List<Employee> employees) {
		return participantService.newParticipant(employees);
	}	
}
