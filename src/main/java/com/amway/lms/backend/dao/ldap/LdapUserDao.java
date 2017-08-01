package com.amway.lms.backend.dao.ldap;

import java.util.List;

public interface LdapUserDao <T>{

	public List<T> getAllUsers();

}
