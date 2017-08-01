package com.amway.lms.backend.configuration;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.ldap.core.support.LdapContextSource;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.RememberMeAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.RememberMeServices;
import org.springframework.security.web.authentication.rememberme.JdbcTokenRepositoryImpl;
import org.springframework.security.web.authentication.rememberme.PersistentTokenBasedRememberMeServices;

import com.amway.lms.backend.common.AmwayEnum.Roles;
import com.amway.lms.backend.common.ErrorCode;
import com.amway.lms.backend.common.Utils;
import com.amway.lms.backend.entity.User;
import com.amway.lms.backend.service.UserService;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    private static final Logger logger = LoggerFactory.getLogger(SecurityConfig.class);

    @Autowired
    Environment env;

    @Autowired
    private LdapContextSource ldapContextSourceAmway;

    @Autowired
    private DataSource dataSource;
    
    @Autowired
    private UserService userService;
    
    @Value("${param.username}")
    private String PARAM_USERNAME;
    
    @Value("${param.password}")
    private String PARAM_PASSWORD;
    
    @Value("${param.access_token}")
    private String ACCESS_TOKEN_NAME;
    
    @Autowired
    private BasicRememberMeUserDetailsService rememberMeUserDetailsService;

    /**
     * Authenticates users against LDAP servers (Login).
     * 
     * Login process:
     * 1. Search for Distinguished Name (DN) of user using the given username (uid or email).
     * 2. Use the found DN and the given password to log the user in.
     * 
     * LDAP servers are in the form of a whitespace-separated list. E.g.:
     * "ldap://192.168.21.113:389/dc=renton1,dc=lan ldap://192.168.21.114:389/dc=renton2,dc=lan"
     * 
     * @param auth
     * @throws Exception
     */
    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth)
            throws Exception {
        auth.ldapAuthentication()
                 .userSearchFilter("(cn={0})")
                 .contextSource(ldapContextSourceAmway);
//                .contextSource().url(getLdapUrl()).managerDn(env.getProperty("ldap.amway.username"))
//                .managerPassword(env.getProperty("ldap.amway.password"));
    }

    /**
     * Security Configurations including:
     * 1. Cross Origin config
     * 2. CSRF config
     * 3. Exception handling
     * 4. Login end point, parameters, handlers
     * 5. Logout
     * 6. Session
     * 7. Remember me config
     */
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        logger.debug("PARAMS: username=" + PARAM_USERNAME + ", password=" + PARAM_PASSWORD);
        http.cors()
                .and()
                .csrf()
                .disable()
                .exceptionHandling()
                .authenticationEntryPoint(authenticationEntryPoint())
                .accessDeniedHandler(accessDeniedHandler())
                .and()
                .authorizeRequests()
                //--------------------------------Public API----------------------------------
                .antMatchers("/internal-error", "/invalid-token").permitAll()
                .antMatchers("/api/v1/login").permitAll()
               // .antMatchers(HttpMethod.GET, "/api/v1/categories").authenticated()
                .antMatchers("/email/**").permitAll()
                .antMatchers("/api/v1/**").permitAll()
                .antMatchers("/files/**").permitAll()
                .antMatchers("/amway-file/**").permitAll()
                .antMatchers("/resources/**").permitAll()
                .antMatchers("/test/send").permitAll()
               // .antMatchers("/test/send/calendar").permitAll()
                .antMatchers("/test/test-token").authenticated()
                .antMatchers("/test/test-admin").hasRole(Roles.ADMIN.name())
                .antMatchers("/test/test-sup").hasAnyRole(Roles.APPROVAL_MANAGER.name())
                .antMatchers("/test/test-trc").hasRole(Roles.CODINATOR.name())
                .antMatchers("/test/test-trainee").hasRole(Roles.TRAINEE.name())
                .antMatchers("/test/test-hod").hasRole(Roles.HOD.name())

                //----------------------------Authenticate API---------------------------
//                .antMatchers("/internal-error", "/invalid-token").permitAll()
//                .antMatchers("/api/v1/login").permitAll()
//                .antMatchers("/api/v1/surveys/start/**").permitAll()
//                .antMatchers("/api/v1/surveyResults").permitAll()
//                .antMatchers("/api/v1/surveyResults/**").permitAll()
//                .antMatchers("/api/v1/surveys/import").permitAll()
//                .antMatchers("/api/v1/questions/file").permitAll()
//                .antMatchers("/amway-file/**").permitAll()
//                .antMatchers(HttpMethod.POST, "/api/v1/postTrainingSurveyParticipant").permitAll()
//                .antMatchers(HttpMethod.POST, "/api/v1/postTrainingSurveyTrainers").permitAll()
//                .antMatchers("/api/v1/courses/postTrainingSurvey").permitAll()
//                .antMatchers(HttpMethod.GET, "/api/v1/courses/{\\d+}/sessions").permitAll()
//                .antMatchers("/email/**").permitAll()
//                .antMatchers("/api/v1/**").authenticated()
//                .antMatchers("/files/**").permitAll()
//                .antMatchers("/resources/**").permitAll()
//                .antMatchers("/test/test-token").authenticated()
                //-------------------------------------------------------------------------
                
                
                .anyRequest()
                .authenticated()
                .and()
                .formLogin()
                .loginProcessingUrl("/api/v1/login")
                .usernameParameter(PARAM_USERNAME)
                .passwordParameter(PARAM_PASSWORD)
                .successHandler(successHandler())
                .failureHandler(failureHandler())
                .and()
                .logout()
                .and()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .rememberMe()
                .rememberMeServices(
                        rememberMeServices(getTokenValiditySeconds()))
                .and()
                .authenticationProvider(rememberMeAuthenticationProvider())
                .addFilter(new CORSFilter())
                ;
    }
    
//    @Override
//    public void configure(AuthenticationManagerBuilder auth) throws Exception{
//        auth.userDetailsService(this.rememberMeUserDetailsService);
//    }

    /**
     * Custom Remember me to store access tokens in header instead of cookie
     */
    @Bean
    public RememberMeServices rememberMeServices(String internalSecretKey) {

        // InMemoryTokenRepositoryImpl rememberMeTokenRepository = new InMemoryTokenRepositoryImpl();

        // Saves access tokens to DB
        JdbcTokenRepositoryImpl rememberMeTokenRepository = new JdbcTokenRepositoryImpl();
        rememberMeTokenRepository.setDataSource(dataSource);

        // Custom remember me service to check access tokens in header instead of cookie
        PersistentTokenBasedRememberMeServices services = new CustomPersistentTokenBasedRememberMeService(
                internalSecretKey, this.rememberMeUserDetailsService,
                rememberMeTokenRepository);

        services.setAlwaysRemember(true);
        services.setCookieName(ACCESS_TOKEN_NAME);
        int tokenValiditySeconds = Integer.parseInt(getTokenValiditySeconds());
        services.setTokenValiditySeconds(tokenValiditySeconds);
        // services.setParameter("remember-me");
        // services.setUseSecureCookie(true);
        
        return services;
    }

    @Bean
    RememberMeAuthenticationProvider rememberMeAuthenticationProvider() {
        return new RememberMeAuthenticationProvider(getTokenValiditySeconds());
        
    }

    /**
     * Custom login success handler
     * 
     * Returns user info (JSON) on login success.
     * 
     * We need this custom handler because the default handler redirects users to a login success page,
     * but there's no page in our system, there're only APIs.
     */
    private AuthenticationSuccessHandler successHandler() {
        return new AuthenticationSuccessHandler() {
            @Override
            public void onAuthenticationSuccess(
                    HttpServletRequest httpServletRequest,
                    HttpServletResponse httpServletResponse,
                    Authentication authentication) throws IOException,
                    ServletException {
                
                String username = httpServletRequest.getParameter("username").trim();
                System.out.println("Username of :" +username);
                /**
                 * TODO: Change the code below to return real user info
                 */
                    User user = userService.getUserByUserID(username);
                    
                    if(user == null){
                        logger.info("********User null");
                        String response = Utils.generateFailureResponseString(
                                ErrorCode.CODE_LOGIN_ERROR, 
                                ErrorCode.MSG_LOGIN_ERROR);
                        httpServletResponse.getWriter().append(response);
                        httpServletResponse.setContentType(MediaType.APPLICATION_JSON_VALUE);
                        httpServletResponse.setStatus(HttpStatus.OK.value());
                    } else if (user.getActive() == 0){
                        logger.info("********User inactive");
                        String response = Utils.generateFailureResponseString(
                                ErrorCode.CODE_LOGIN_ERROR, 
                                ErrorCode.MSG_LOGIN_ERROR);
                        httpServletResponse.getWriter().append(response);
                        httpServletResponse.setContentType(MediaType.APPLICATION_JSON_VALUE);
                        httpServletResponse.setStatus(HttpStatus.OK.value());
                    }
                    else {
                        String cookieValue = httpServletResponse.getHeaders(ACCESS_TOKEN_NAME).toString();
                        cookieValue = cookieValue.substring(1, cookieValue.length()-1);
                        logger.info("********Token return when login: "+cookieValue);
                        Map<String, Object> mapResponse = new HashMap<String, Object>();
                        mapResponse.put("user", user);
                        mapResponse.put("token", cookieValue);
                        String response = Utils.generateSuccessResponseString(mapResponse);
                        httpServletResponse.getWriter().append(response);
                        httpServletResponse.setContentType(MediaType.APPLICATION_JSON_VALUE);
                        httpServletResponse.setStatus(HttpStatus.OK.value());
                    }
            }
        };
    }

    /**
     * Custom login failure handler
     * 
     * Returns an error message (JSON) on login failure.
     * 
     * We need this custom handler because the default handler redirects users to a login failure page,
     * but there's no page in our system, there're only APIs.
     * @return
     */
    private AuthenticationFailureHandler failureHandler() {
        return new AuthenticationFailureHandler() {
            @Override
            public void onAuthenticationFailure(
                    HttpServletRequest httpServletRequest,
                    HttpServletResponse httpServletResponse,
                    AuthenticationException e) throws IOException,
                    ServletException {
                logger.debug("Has failed .....");
                String response = Utils.generateFailureResponseString(
                        ErrorCode.CODE_LOGIN_ERROR, 
                        ErrorCode.MSG_LOGIN_ERROR);

                httpServletResponse.getWriter().append(response);
                httpServletResponse.setContentType(MediaType.APPLICATION_JSON_VALUE);
                httpServletResponse.setStatus(HttpStatus.OK.value());
            }
        };
    }

    /**
     * Custom access denied handler
     * 
     * Returns an error message (JSON) on access denied.
     * 
     * We need this custom handler because the default handler redirects users to a access denied page,
     * but there's no page in our system, there're only APIs.
     * @return
     */
    private AccessDeniedHandler accessDeniedHandler() {
        return new AccessDeniedHandler() {
            @Override
            public void handle(HttpServletRequest httpServletRequest,
                    HttpServletResponse httpServletResponse,
                    AccessDeniedException e) throws IOException,
                    ServletException {
                String response = Utils.generateFailureResponseString(
                        ErrorCode.CODE_ACCESS_DENIED, 
                        ErrorCode.MSG_ACCESS_DENIED);

                httpServletResponse.getWriter().append(response);
                httpServletResponse.setContentType(MediaType.APPLICATION_JSON_VALUE);
                httpServletResponse.setStatus(HttpStatus.OK.value());
            }
        };
    }

    /**
     * Custom authentication entry point
     * 
     * Returns an error message (JSON) on authenticated.
     * 
     * We need this custom authentication entry point because the default entry point redirects users to a login page,
     * but there's no page in our system, there're only APIs.
     * @return
     */
    private AuthenticationEntryPoint authenticationEntryPoint() {
        return new AuthenticationEntryPoint() {
            @Override
            public void commence(HttpServletRequest httpServletRequest,
                    HttpServletResponse httpServletResponse,
                    AuthenticationException e) throws IOException,
                    ServletException {
                String response = Utils.generateFailureResponseString(
                        ErrorCode.CODE_AUTHENTICATION_ERROR, 
                        ErrorCode.MSG_AUTHENTICATION_ERROR);

                httpServletResponse.getWriter().append(response);
                httpServletResponse.setContentType(MediaType.APPLICATION_JSON_VALUE);
                httpServletResponse.setStatus(HttpStatus.OK.value());
            }
        };
    }

    private String getLdapUrl() {
        return env.getRequiredProperty("ldap.amway.url") + "/"
                + env.getRequiredProperty("ldap.amway.base");
    }

    private String getTokenValiditySeconds() {
        return env.getRequiredProperty("remember.me.token.validity.seconds");
    }

}