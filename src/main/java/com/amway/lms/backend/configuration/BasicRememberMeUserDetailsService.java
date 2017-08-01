package com.amway.lms.backend.configuration;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.amway.lms.backend.common.Utils;
import com.amway.lms.backend.repository.UserRepository;
import com.amway.lms.backend.service.UserService;

@Service
public class BasicRememberMeUserDetailsService implements UserDetailsService {
    
    private static final Logger logger = LoggerFactory.getLogger(BasicRememberMeUserDetailsService.class);
    
    @Autowired
    UserRepository userRepository;
    
    @Autowired
    private UserService userService;
    
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
	    System.out.println("Logger Username: " + username);
	    com.amway.lms.backend.entity.User userEntity = userService.getUserByUserID(username);
	    if(userEntity == null){
	        logger.info("Cannot find User by username: "+username);
	        throw new UsernameNotFoundException("Cannot find User by: "+username);
	    } else {
	        logger.info("Logger userEntity: "+Utils.generateSuccessResponseString(userEntity));
	    }
	    CustomUserDetail customUserDetail = new CustomUserDetail(userEntity);
	    logger.info("Logger customUserDetail: "+Utils.generateSuccessResponseString(customUserDetail));
		return customUserDetail;
	}

}
