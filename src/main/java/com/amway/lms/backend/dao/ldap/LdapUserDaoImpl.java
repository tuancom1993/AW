package com.amway.lms.backend.dao.ldap;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ldap.core.LdapTemplate;
import org.springframework.ldap.core.support.LdapContextSource;
import org.springframework.stereotype.Repository;

import com.amway.lms.backend.model.ldap.LdapUser;

@Repository("ldapUserDao")
public class LdapUserDaoImpl implements LdapUserDao<LdapUser> {

	@Autowired
	private LdapTemplate ldapTemplate;

	@Autowired
	private LdapContextSource ldapContextSourceTwo;

	/**
	 * (non-Javadoc)
	 *
	 * @see ldap.advance.example.UserRepositoryIntf#getAllUsers()
	 **/
	@Override
	public List<LdapUser> getAllUsers() {
		List<LdapUser> listUser = null;

		listUser = ldapTemplate.findAll(LdapUser.class);

//		ldapTemplate.setContextSource(ldapContextSourceTwo);
//		listUser.addAll(ldapTemplate.findAll(LdapUser.class));

		return listUser;
	}

}
