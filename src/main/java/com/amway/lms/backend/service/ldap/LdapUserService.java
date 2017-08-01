package com.amway.lms.backend.service.ldap;

import java.util.List;

import org.springframework.context.event.ContextRefreshedEvent;

import com.amway.lms.backend.model.ldap.LdapUser;

public interface LdapUserService {

	List<LdapUser> getAllUsers();
	void syncUserData();
	void runCronjobOnStartUp(ContextRefreshedEvent event);
}
