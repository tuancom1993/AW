package com.amway.lms.backend.rest;

import java.io.IOException;
import java.util.List;

import javax.mail.MessagingException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.MailException;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.amway.lms.backend.model.EmailInfor;
import com.amway.lms.backend.model.Employee;
import com.amway.lms.backend.model.TestEmailModel;
import com.amway.lms.backend.service.EmailService;
import com.amway.lms.backend.service.TestService;

@RestController
public class EmailAmwayController {
	private static final Logger logger = LoggerFactory.getLogger(EmailAmwayController.class);

	@Autowired
	private EmailService emailService;
	
	@Autowired
	private TestService testService;

	@RequestMapping(value = "/api/v1/email/emailTemplate", method = RequestMethod.GET)
	public ResponseEntity<?> getEmailTemplate(@RequestParam(name = "emailTemplate", required = false) String emailTemplate)
			throws MailException, InterruptedException, MessagingException, IOException {
		return emailService.getEmailTemplate(emailTemplate);
	}

	@RequestMapping(value = "/api/v1/email/emailForSurvey", method = RequestMethod.POST)
	public ResponseEntity<?> sendEmailForSurvey(@RequestBody EmailInfor emailInfor) throws IOException, MessagingException {
		return emailService.sendEmailForSurvey(emailInfor);
	}

	@RequestMapping(value = "/api/v1/email/acceptDenyByEmail/{sessionId}", method = RequestMethod.POST)
    public ResponseEntity<?> sendEmailForSurvey(@RequestBody List<Employee> employees,
            @PathVariable("sessionId") int sessionId) throws Exception {
		return emailService.acceptDenyByEmail(employees, sessionId);
	}

	//	@RequestMapping(value = "/email/acceptUserbyCourse", method = RequestMethod.GET)
	//	public ResponseEntity<?> accpetUserbyCourse(@RequestParam(value = "accept", required = false) String accept) {
	//		try {
	//			learningDashboardService.accepetCourseByEmail(accept);
	//			return new ResponseEntity<>("Accept Course Success Thank you!", HttpStatus.ACCEPTED);
	//		} catch (Exception e) {
	//			e.printStackTrace();
	//			logger.debug(e.getMessage());
	//			return new ResponseEntity<>("ERROR", HttpStatus.BAD_REQUEST);
	//		}
	//	}
	//
	//	@RequestMapping(value = "/email/denyUserInCourse", method = RequestMethod.GET)
	//	public ResponseEntity<?> denyUserInCourse(@RequestParam(value = "deny", required = false) String deny) {
	//		try {
	//			learningDashboardService.denyCourseByEmail(deny);
	//			return new ResponseEntity<>("Deny Course Success Thank you!", HttpStatus.ACCEPTED);
	//		} catch (Exception e) {
	//			e.printStackTrace();
	//			logger.debug(e.getMessage());
	//			return new ResponseEntity<>("ERROR", HttpStatus.BAD_REQUEST);
	//		}
	//	}
	//
//		@RequestMapping(value = "/api/v1/email/sendEmailToTrainee/{sessionId}", method = RequestMethod.POST)
//		public ResponseEntity<?> sendEmailToTrainee(@RequestBody List<Employee> employees, @PathVariable("sessionId") Integer sessionId) throws IOException, MessagingException, ObjectNotFoundException {
//			return emailService.sendEmailToTrainee(employees, sessionId);
//		}
	
	@RequestMapping(value = "/api/v1/email/emailForTest", method = RequestMethod.POST)
    public ResponseEntity<?> sendEmailForTest(@RequestBody TestEmailModel testEmailModel) throws Exception {
        return testService.sendEmail(testEmailModel);
    }

}
