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
public class SessionRepositoryTest {

	@Autowired
    private SessionRepository sessionRepository;
	
	@Test
	public void init() {
		Assert.assertNotNull(sessionRepository);         
	}
	
	@Test
	public void getSessionListByCourseId() {
		//System.out.println(JsonUtils.toJson(sessionRepository.getSessionListByCourseId(2)));
	}
}
