package com.amway.lms.backend.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.ldap.core.LdapTemplate;
import org.springframework.ldap.core.support.LdapContextSource;

@Configuration
public class LdapConfiguration {

    @Autowired
    Environment env;

//    @Bean
//    public LdapContextSource ldapContextSourceOne() {
//        LdapContextSource contextSource= new LdapContextSource();
//        contextSource.setUrl(env.getRequiredProperty("ldap.01.url"));
//        contextSource.setBase(env.getRequiredProperty("ldap.01.base"));
//        contextSource.setUserDn(env.getRequiredProperty("ldap.01.username"));
//        contextSource.setPassword(env.getRequiredProperty("ldap.01.password"));
//        return contextSource;
//    }
//
//    @Bean
//    public LdapContextSource ldapContextSourceTwo() {
//        LdapContextSource contextSource= new LdapContextSource();
//        contextSource.setUrl(env.getRequiredProperty("ldap.02.url"));
//        contextSource.setBase(env.getRequiredProperty("ldap.02.base"));
//        contextSource.setUserDn(env.getRequiredProperty("ldap.02.username"));
//        contextSource.setPassword(env.getRequiredProperty("ldap.02.password"));
//        return contextSource;
//    }
    
    @Bean
    public LdapContextSource ldapContextSourceAmway() {
        LdapContextSource contextSource= new LdapContextSource();
        contextSource.setUrl(env.getRequiredProperty("ldap.amway.url"));
        contextSource.setBase(env.getRequiredProperty("ldap.amway.base"));
        contextSource.setUserDn(env.getRequiredProperty("ldap.amway.username"));
        contextSource.setPassword(env.getRequiredProperty("ldap.amway.password"));
        return contextSource;
    }

    @Bean
    public LdapTemplate ldapTemplate() {
        return new LdapTemplate(ldapContextSourceAmway());
    }

}
