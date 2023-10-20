package com.corpseed.controller;

import java.security.Principal;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.corpseed.entity.User;
import com.corpseed.util.Message;
import com.corpseed.serviceImpl.AzureBlobAdapter;
import com.corpseed.serviceImpl.CommonServices;
import com.corpseed.serviceImpl.UserServices;

@Controller
@RequestMapping("/my-profile")
public class ProfileController {

	@Autowired
	private CommonServices commonService;
	
	@Autowired
	private BCryptPasswordEncoder passwordEncoder;
	
	@Autowired
	private UserServices userService;
	
	@Autowired
    AzureBlobAdapter azureAdapter;
	
	@GetMapping("/change-password")
	public String changePassword(Model model) {
		model.addAttribute("title", "Corpseed Dashboard | Change password");		
		return "admin/change-password";
	}
	
	@PostMapping("/change-password")
	public String updatePassword(@RequestParam("oldPassword") String oldPassword,
			@RequestParam("newPassword") String newPassword,@RequestParam("confirmNewPassword") String confirmNewPassword,
			Model model,Principal principal,HttpSession session) {
		if(oldPassword==null||oldPassword.length()<=0||newPassword==null||newPassword.length()<=0||confirmNewPassword==null||confirmNewPassword.length()<=0) {
			model.addAttribute("title", "Corpseed Dashboard | Change Password");
			model.addAttribute("oldPassword", oldPassword);
			model.addAttribute("newPassword", newPassword);
			model.addAttribute("confirmNewPassword", confirmNewPassword);
			model.addAttribute("message", new Message("Please fill out the blank fields !!", "alert-danger"));
			return "admin/change-password";
		}
		
		if(!newPassword.equals(confirmNewPassword)) {
			model.addAttribute("title", "Corpseed Dashboard | Change password");
			model.addAttribute("oldPassword", oldPassword);
			model.addAttribute("newPassword", newPassword);
			model.addAttribute("confirmNewPassword", confirmNewPassword);
			session.setAttribute("message", new Message("New Password and Confirm Password didn't match !!", "text-danger"));
			return "admin/change-password";
		}
		
		User user=this.commonService.getLoginedUser(principal);
		if(passwordEncoder.matches(oldPassword, user.getPassword())) {
			user.setPassword(this.passwordEncoder.encode(newPassword));	
			this.userService.saveUser(user);
			return "redirect:/logout";
		}else {
			model.addAttribute("title", "Corpseed Dashboard | Change password");
			model.addAttribute("oldPassword", oldPassword);
			model.addAttribute("newPassword", newPassword);
			model.addAttribute("confirmNewPassword", confirmNewPassword);
			session.setAttribute("message", new Message("Old password doesn't match !!", "text-danger"));
			return "admin/change-password";
		}		
	}
	
	@GetMapping("/")
	public String myProfile(Model model,Principal principal) {
		model.addAttribute("title", "Corpseed Dashboard | My Profile");
		model.addAttribute("appendClass", "profile");
		User loginedUser = this.commonService.getLoginedUser(principal);
		model.addAttribute("user",loginedUser);
//		System.out.println("--------------------------------"+loginedUser.getRole());
		if(loginedUser.getRole().equalsIgnoreCase("ROLE_USER"))
			return "redirect:/candidate/";
		else
		return "admin/profile";
	}
	@PostMapping("/update")
	public String updateMyProfile(@Valid @ModelAttribute("user") User user,BindingResult result,
			@RequestParam("profileImage") MultipartFile file,Model model,HttpSession session
			,HttpServletRequest req) {
		try {
			if(result.hasErrors()) {
				model.addAttribute("title", "Corpseed Dashboard | My Profile");
				model.addAttribute("appendClass", "profile");
				model.addAttribute("user", user);
				session.setAttribute("message", new Message("Please fill out the blank fields !!", "alert-danger"));
				return "admin/profile";
			}
			if(!file.isEmpty()) {
				if(!user.getProfilePicture().equalsIgnoreCase("profile.png")) {
					this.azureAdapter.deleteFile(user.getProfilePicture());
				}
				String name = azureAdapter.upload(file,this.commonService.getUniqueCode());
				user.setProfilePicture(name);
				session.setAttribute("profileDp",name);
			}else {
				user.setProfilePicture("profile.png");
			}
			User findUser=this.userService.getUserByUuid(user.getUuid());
			if(findUser!=null) {
				user.setId(findUser.getId());
				user.setRole(findUser.getRole());
				user.setDisplayStatus("1");
				user.setRegDate(findUser.getRegDate());
				user.setJobTitle(findUser.getJobTitle());
				user.setPassword(findUser.getPassword());
				user.setEmail(findUser.getEmail());
				user.setAddedByUUID(findUser.getAddedByUUID());
				user.setIpAddress(this.commonService.getIpAddress(req));
				User saveUser = this.userService.saveUser(user);
				model.addAttribute("title", "Corpseed Dashboard | My Profile");
				model.addAttribute("appendClass", "profile");
				model.addAttribute("user", user);
				if(saveUser==null) {
					session.setAttribute("message", new Message("Data not saved, Please try-again later !!", "alert-danger"));
				}else {
					session.setAttribute("message", new Message("Profile Updated successfully !!", "alert-success"));
				}
			}else {
				model.addAttribute("title", "Corpseed Dashboard | My Profile");
				model.addAttribute("appendClass", "profile");
				model.addAttribute("user", user);
				session.setAttribute("message", new Message("User Not Found !!", "alert-danger"));
			}
			return "admin/profile";
		} catch (Exception e) {
			model.addAttribute("title", "Corpseed Dashboard | My Profile");
			model.addAttribute("appendClass", "profile");
			model.addAttribute("user", user);
			session.setAttribute("message", new Message("Error : "+e.getMessage(), "alert-danger"));
			return "admin/profile";
		}
		
		
	}
}
