package com.amway.lms.backend.rest;

import java.io.IOException;
import java.util.List;

import javax.mail.MessagingException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.amway.lms.backend.common.Utils;
import com.amway.lms.backend.configuration.CustomUserDetail;
import com.amway.lms.backend.entity.User;
import com.amway.lms.backend.exception.ObjectNotFoundException;
import com.amway.lms.backend.model.Employee;
import com.amway.lms.backend.model.ldap.LdapUser;
import com.amway.lms.backend.service.EmailService;
import com.amway.lms.backend.service.RemindEmailService;
import com.amway.lms.backend.service.UserService;
import com.amway.lms.backend.service.ldap.LdapUserService;

@RestController
 @RequestMapping("/test")
public class TestingController {

	@Autowired
	UserService userService;

	@Autowired
	LdapUserService ldapUserService;
	
	@Autowired
	RemindEmailService remindEmailService;
	
	@Autowired
	EmailService emailService;

	/* Getting List of objects in Json format in Spring Restful Services */
//	@RequestMapping(value = "/user/list", method = RequestMethod.GET)
//	public @ResponseBody List<User> getUsers() {
//
//		List<User> userList = null;
//		try {
//			userList = userService.findAllUsers();
//
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//
//		return userList;
//	}

	/* Getting List of objects in Json format in Spring Restful Services */
	@RequestMapping(value = "/ldap/user/list", method = RequestMethod.GET)
	public @ResponseBody List<LdapUser> getLdapUsers() {
		return ldapUserService.getAllUsers();
	}
	
	@RequestMapping(value="/test-token")
	public ResponseEntity<?> test(){
	    return Utils.generateSuccessResponseEntity("OK");
	}
	
	@RequestMapping(value="/userLogin")
    public ResponseEntity<?> getUserLogin(){
	    User userLogin = null;
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof CustomUserDetail) {
            CustomUserDetail customUserDetails = (CustomUserDetail) principal;
            userLogin = customUserDetails.getUser();
        }
        return Utils.generateSuccessResponseEntity(userLogin);
    }
	
	@RequestMapping(value="/test-admin")
    public ResponseEntity<?> testRoleAdmin(){
        return Utils.generateSuccessResponseEntity("OK testRoleAdmin");
    }
	@RequestMapping(value="/test-sup")
    public ResponseEntity<?> testRoleSup(){
        return Utils.generateSuccessResponseEntity("OK testRoleSup");
    }
	@RequestMapping(value="/test-trCoor")
    public ResponseEntity<?> testRoleTrainingCoordinator(){
        return Utils.generateSuccessResponseEntity("OK testRoleTrainingCoordinator");
    }
	@RequestMapping(value="/test-trainee")
    public ResponseEntity<?> testRoleTrainee(){
        return Utils.generateSuccessResponseEntity("OK testRoleTrainee");
    }
	@RequestMapping(value="/test-hod")
    public ResponseEntity<?> testRoleHOD(){
        return Utils.generateSuccessResponseEntity("OK testRoleHOD");
    }

	@RequestMapping(value="/send")
	public ResponseEntity<?> testSendEmailCalendar() throws MessagingException, IOException, ObjectNotFoundException{
	    Employee employee = new Employee();
	    employee.setEmailAddress("anh.vxt.it@gmail.com");
        emailService.sendEmailToTraineeForQuiz(employee, 5);
        emailService.sendEmailToTraineeForPostSurvey(employee, 50);
        emailService.sendEmailToTrainerForPostSurvey(employee, 50);
        return Utils.generateSuccessResponseEntity("ok");
    }
	
	@RequestMapping(value="/send/calendar")
    public ResponseEntity<?> testSendEmailCalendarForTrainee() throws MessagingException, IOException, ObjectNotFoundException{
	    Employee employee = new Employee();
	    employee.setEmailAddress("charles@enclave.vn");
	    employee.setEmployeeId(100);
        emailService.sendEmailToTrainee(employee, 50);
        return Utils.generateSuccessResponseEntity("ok");
    }
}
