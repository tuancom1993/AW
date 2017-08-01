package com.amway.lms.backend.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.amway.lms.backend.service.EmailService;

@Controller
public class EmailController {
	@Autowired
	private EmailService emailService;

	@RequestMapping(value = "/email/acceptUserbyCourse", method = RequestMethod.GET)
	public String accpetUserbyCourse(@RequestParam(value = "accept", required = false) String accept) {
		try {
			emailService.accepetCourseByEmail(accept);
			return "success";
		} catch (Exception e) {
			e.printStackTrace();
			return "error";
		}
	}

	@RequestMapping(value = "/email/denyUserInCourse", method = RequestMethod.GET)
	public String denyUserInCourse(@RequestParam(value = "deny", required = false) String deny) {
		try {
			emailService.denyCourseByEmail(deny);
			return "denySuccess";
		} catch (Exception e) {
			e.printStackTrace();
			return "error";
		}
	}

	@RequestMapping(value = "/email/traineeAcceptCourse", method = RequestMethod.GET)
	public String traineeAcceptCourse(@RequestParam(value = "accept", required = false) String accept) {
		try {
			emailService.traineeAcceptCourse(accept);
			return "traineeAcceptSuccess";
		} catch (Exception e) {
			e.printStackTrace();
			return "error";
		}
	}

	@RequestMapping(value = "/email/traineeDenyCourse", method = RequestMethod.GET)
	public String traineeDenyCourse(@RequestParam(value = "deny", required = false) String deny) {
		try {
			emailService.traineeDenyCourse(deny);
			return "traineeDenySuccess";
		} catch (Exception e) {
			e.printStackTrace();
			return "error";
		}
	}

}
