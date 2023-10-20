package com.corpseed.config;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import com.corpseed.entity.User;
import com.corpseed.serviceImpl.UserServices;

@Component
public class CustomAuthenticationFailureHandler implements AuthenticationFailureHandler {

	@Autowired
	private UserServices userService;
	
	@Override
	public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
			AuthenticationException exception) throws IOException, ServletException {
		/*
		 * this handles issues like when a user is not found(i.e.
		 * UsernameNotFoundException) or other exceptions thrown inside authentication
		 * provider. In fact, this handles other authentication exceptions that are not
		 * handled by AccessDeniedException and AuthenticationEntryPoint.
		 */
		String redirectUrl = request.getContextPath() + "/login?error";
		String email=request.getParameter("username");
		if(email!=null&&email.length()>0) {
			User users=this.userService.findByEmailAndDeleteStatus(email, 2);
			if(users!=null) {
				if(users.getAccountStatus()==2) {
					redirectUrl = request.getContextPath() + "/login?inactive";
				}
			}
		}		
        response.sendRedirect(redirectUrl);
	}

}
