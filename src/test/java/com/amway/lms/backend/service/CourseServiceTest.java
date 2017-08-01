package com.amway.lms.backend.service;

import javax.transaction.Transactional;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import com.amway.lms.backend.common.JsonUtils;
import com.amway.lms.backend.configuration.AppConfig;
import com.amway.lms.backend.exception.ObjectNotFoundException;
import com.amway.lms.backend.rest.CourseController;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {AppConfig.class})
@EnableTransactionManagement
@WebAppConfiguration
@Transactional
public class CourseServiceTest {

	@Autowired
	private LearningDashboardService learningDashboardService;
	
	@Autowired
    private CourseService courseService;
	
	@Test
	public void init() {
		Assert.assertNotNull(courseService);         
	}
	
	@Test
	public void getCoursesById() {
		System.out.println(JsonUtils.toJson(courseService.getCoursesById(2)));
	}
	
	@Test
	public void getCoursesInProgressList() {
		System.out.println(JsonUtils.toJson(courseService.getCoursesInProgressList(0, 5)));
	}
	

	@Test
	public void getCoursesApproveList() {
		System.out.println(JsonUtils.toJson(courseService.getCoursesApprovedListExport(-1, -1)));
	}
	
	@Test
	public void getAllCourseSessionExport() throws ObjectNotFoundException {
		System.out.println(JsonUtils.toJson(learningDashboardService.getAllCourseSessionExport(1)));
	}
}
