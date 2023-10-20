package com.corpseed.controller.advicecontroller;

import java.security.Principal;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import com.corpseed.entity.User;
import com.corpseed.serviceImpl.CommonServices;

@ControllerAdvice
public class SessionController {

	@Autowired
	private CommonServices commonService;

	@ModelAttribute
	public void getMenu(Principal principal, HttpSession session) {
//		System.out.println("session role="+session.getAttribute("role")+"principal=="+principal);
		if(principal!=null&&session.getAttribute("role")==null) {
//			System.out.println("going to archive role and session..............");
			User user=this.commonService.getLoginedUser(principal);
			session.setAttribute("role",user.getRole());
			session.setAttribute("profileDp", user.getProfilePicture());
		}else if(principal==null&&session.getAttribute("role")!=null) {
//			System.out.println("destroying session......................");
			session.invalidate();
		}
	}

}
