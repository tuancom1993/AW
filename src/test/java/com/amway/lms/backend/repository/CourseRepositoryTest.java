package com.amway.lms.backend.repository;

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

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {AppConfig.class})
@EnableTransactionManagement
@WebAppConfiguration
@Transactional
public class CourseRepositoryTest {
	
	@Autowired
    private CourseRepository courseRepository;
	
	@Test
	public void init() {
		Assert.assertNotNull(courseRepository);         
	}
	
	@Test
	public void getParticipantsListByCourseId() {
		System.out.println(JsonUtils.toJson(courseRepository.getCourseById(2)));
	}
	
	@Test
	public void getCoursesApprovedList() {
		System.out.println(JsonUtils.toJson(courseRepository.getCoursesApprovedList(0, 2)));
	}
	
	@Test
	public void getCoursesImprogressList() {
		System.out.println(JsonUtils.toJson(courseRepository.getCoursesImprogressList(0, 5)));
	}
}
