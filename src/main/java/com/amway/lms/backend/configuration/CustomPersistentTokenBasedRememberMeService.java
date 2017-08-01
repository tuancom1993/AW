package com.amway.lms.backend.configuration;

import java.util.Arrays;
import java.util.Enumeration;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.rememberme.CookieTheftException;
import org.springframework.security.web.authentication.rememberme.InMemoryTokenRepositoryImpl;
import org.springframework.security.web.authentication.rememberme.InvalidCookieException;
import org.springframework.security.web.authentication.rememberme.PersistentRememberMeToken;
import org.springframework.security.web.authentication.rememberme.PersistentTokenBasedRememberMeServices;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;
import org.springframework.security.web.authentication.rememberme.RememberMeAuthenticationException;


public class CustomPersistentTokenBasedRememberMeService extends PersistentTokenBasedRememberMeServices {
    
	private PersistentTokenRepository tokenRepository = new InMemoryTokenRepositoryImpl();

	@Value("${param.access_token}")
    private String ACCESS_TOKEN_NAME;

	public CustomPersistentTokenBasedRememberMeService(String key, UserDetailsService userDetailsService,
			PersistentTokenRepository tokenRepository) {
		super(key, userDetailsService, tokenRepository);
		this.tokenRepository = tokenRepository;
	}
	
	protected boolean rememberMeRequested(HttpServletRequest request, String parameter) {
		String value = request.getHeader(DEFAULT_PARAMETER);
		
		logger.debug("*************************TOKEN: "+value);
		
		return value != null && Boolean.parseBoolean(value) ? 
				Boolean.parseBoolean(value) : 
					super.rememberMeRequested(request, parameter);
	}

	/**
     * Locates the Spring Security remember me token in the request and returns its value.
     *
     * @param request the submitted request which is to be authenticated
     * @return the value of the request header (which was originally provided by the cookie - API expects it in header)
     */
    @Override protected String extractRememberMeCookie(HttpServletRequest request) {
    	String token = request.getHeader(ACCESS_TOKEN_NAME);
    	Enumeration<?> headerNames = request.getHeaderNames();
    	while(headerNames.hasMoreElements()) {
    	  String headerName = (String)headerNames.nextElement();
    	  System.out.println("Header Name - " + headerName + ", Value - " + request.getHeader(headerName));
    	}
    	System.out.println("Token-Request: "+token);
        if ((token == null) || (token.length() == 0)) {
            return null;
        }
        System.out.println("Token-Request: "+token);
        return token;
    }

    /**
     * Saves the Spring Security remember me token in the response header
     */
    protected void setCookie(String[] tokens, int maxAge, HttpServletRequest request, HttpServletResponse response) {
		String cookieValue = encodeCookie(tokens);
		response.setHeader(ACCESS_TOKEN_NAME, cookieValue);
	}

	@Override
	protected UserDetails processAutoLoginCookie(String[] cookieTokens, 
			HttpServletRequest request, HttpServletResponse response) {

		if (cookieTokens.length != 2) {
			throw new InvalidCookieException("Cookie token did not contain " + 2
					+ " tokens, but contained '" + Arrays.asList(cookieTokens) + "'");
		}

		final String presentedSeries = cookieTokens[0];
		final String presentedToken = cookieTokens[1];

		PersistentRememberMeToken token = tokenRepository
				.getTokenForSeries(presentedSeries);

		if (token == null) {
			// No series match, so we can't authenticate using this cookie
			throw new RememberMeAuthenticationException(
					"No persistent token found for series id: " + presentedSeries);
		}

		// We have a match for this user/series combination
		if (!presentedToken.equals(token.getTokenValue())) {
			// Token doesn't match series value. Delete all logins for this user and throw
			// an exception to warn them.
			tokenRepository.removeUserTokens(token.getUsername());

			throw new CookieTheftException(
					messages.getMessage(
							"PersistentTokenBasedRememberMeServices.cookieStolen",
							"Invalid remember-me token (Series/token) mismatch. Implies previous cookie theft attack."));
		}

		if (token.getDate().getTime() + getTokenValiditySeconds() * 1000L < System
				.currentTimeMillis()) {
			throw new RememberMeAuthenticationException("Remember-me login has expired");
		}

		// Token also matches, so login is valid. Update the token value, keeping the
		// *same* series number.
		if (logger.isDebugEnabled()) {
			logger.debug("Refreshing persistent login token for user '"
					+ token.getUsername() + "', series '" + token.getSeries() + "'");
		}

		// PersistentRememberMeToken newToken = new PersistentRememberMeToken(
		// 		token.getUsername(), token.getSeries(), generateTokenData(), new Date());

		// try {
		// 	tokenRepository.updateToken(newToken.getSeries(), newToken.getTokenValue(),
		// 			newToken.getDate());
		// 	addCookie(newToken, request, response);
		// }
		// catch (Exception e) {
		// 	logger.error("Failed to update token: ", e);
		// 	throw new RememberMeAuthenticationException(
		// 			"Autologin failed due to data access problem");
		// }

		/**
		 * Simply return the current token to re-use until it's expired.
		 */
		addCookie(token, request, response);

		return getUserDetailsService().loadUserByUsername(token.getUsername());
	}

	private void addCookie(PersistentRememberMeToken token, HttpServletRequest request,
			HttpServletResponse response) {
		setCookie(new String[] { token.getSeries(), token.getTokenValue() },
				getTokenValiditySeconds(), request, response);
	}

}
