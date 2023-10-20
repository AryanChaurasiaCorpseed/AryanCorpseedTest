package com.corpseed.config;

import java.io.IOException;
import java.util.Collection;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

@Component
public class CustomSuccessHandler implements AuthenticationSuccessHandler {

	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
			Authentication authentication) throws IOException, ServletException {

		String redirectUrl = null;

		Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
		for (GrantedAuthority grantedAuthority : authorities){
			if(grantedAuthority.getAuthority().equals("ROLE_ADMIN")){
				try{
					redirectUrl = "/admin/";
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			} else if (grantedAuthority.getAuthority().equals("ROLE_DELIVERY")||grantedAuthority.getAuthority().equals("ROLE_MARKETING")) {
				try {
					redirectUrl = "/admin/lawupdate/";
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}else if (grantedAuthority.getAuthority().equals("ROLE_HR_MANAGER")||grantedAuthority.getAuthority().equals("ROLE_HR_EXECUTIVE")) {
				try {
					redirectUrl = "/hrm/jobs/";
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}else if (grantedAuthority.getAuthority().equals("ROLE_USER")) {
				try {
					redirectUrl = "/candidate/";
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}else if (grantedAuthority.getAuthority().equals("ROLE_QUALITY")) {
				try {
					redirectUrl = "/admin/meeting/";
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}else {
	            throw new IllegalStateException();
	        }
		}
		
		if (redirectUrl == null) {
			throw new IllegalStateException();
		}
		new DefaultRedirectStrategy().sendRedirect(request, response, redirectUrl);
	}
}