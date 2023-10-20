package com.corpseed.controller.admincontroller;

import java.net.InetAddress;
import java.security.Principal;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
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
@RequestMapping("/admin/candidates")
public class AdminCandidateController {

	@Autowired
	private BCryptPasswordEncoder passwordEncoder;
	
	@Autowired
	private UserServices userService;
	
	@Autowired
	private CommonServices commonServices;
	
	@Autowired
	private AzureBlobAdapter azureBlobAdapter;
	
	@GetMapping("/")
	public String candidates(Model model) {
		model.addAttribute("title", "Corpseed Dashboard | Manage Candidates");
		model.addAttribute("appendClass", "candidates");
		model.addAttribute("users", this.userService.findByRole("ROLE_USER"));
		return "admin/candidates";
	}
	@GetMapping("/change-password/{uuid}")
	public String changePassword(@PathVariable("uuid") String uuid,Model model) {
		model.addAttribute("title", "Corpseed Dashboard | Change Password");
		model.addAttribute("appendClass", "users");
		model.addAttribute("uuid", uuid);
		return "admin/cpassword-update";
	}
	
	@PostMapping("/change-password/{uuid}")
	public String updatePassword(@PathVariable("uuid") String uuid,@RequestParam("newPassword") String newPassword,
			@RequestParam("confirmNewPassword") String confirmNewPassword,Model model,HttpSession session) {
		try {		
			if(newPassword==null||newPassword.length()<=0||confirmNewPassword==null||confirmNewPassword.length()<=0) {
				model.addAttribute("title", "Corpseed Dashboard | Change Password");
				model.addAttribute("appendClass", "users");
				model.addAttribute("newPassword", newPassword);
				model.addAttribute("confirmNewPassword", confirmNewPassword);
				session.setAttribute("message", new Message("Please fill out the blank fields !!", "alert-danger"));
				return "admin/cpassword-update";
			}
			if(!newPassword.equals(confirmNewPassword)) {
				model.addAttribute("title", "Corpseed Dashboard | Change Password");
				model.addAttribute("appendClass", "users");
				model.addAttribute("newPassword", newPassword);
				model.addAttribute("confirmNewPassword", confirmNewPassword);
				session.setAttribute("message", new Message("New Password and Confirm password didn't match !!", "alert-danger"));
				return "admin/cpassword-update";
			}
			
			User user=this.userService.getUserByUuid(uuid);
			if(user!=null) {
				user.setPassword(this.passwordEncoder.encode(newPassword));
				this.userService.saveUser(user);
				return "redirect:/admin/candidates/";
			}else {
				model.addAttribute("title", "Corpseed Dashboard | Change Password");
				model.addAttribute("appendClass", "users");
				model.addAttribute("newPassword", newPassword);
				model.addAttribute("confirmNewPassword", confirmNewPassword);
				session.setAttribute("message", new Message("User not found ,Please Try again later !!", "alert-danger"));
				return "admin/cpassword-update";
			}
			
		} catch (Exception e) {
			model.addAttribute("title", "Corpseed Dashboard | Change Password");
			model.addAttribute("appendClass", "users");
			model.addAttribute("newPassword", newPassword);
			model.addAttribute("confirmNewPassword", confirmNewPassword);
			session.setAttribute("message", new Message("Error : "+e.getMessage(), "alert-danger"));
			return "admin/cpassword-update";
		}
	}
	
	@GetMapping("/edit/{useruuid}")
	public String userEdit(@PathVariable("useruuid") String useruuid,Model model) {
		model.addAttribute("title", "Corpseed Dashboard | Edit Candidate");
		model.addAttribute("appendClass", "users");
		model.addAttribute("useruuid", useruuid);
		model.addAttribute("user", this.userService.getUserByUuid(useruuid));
		return "admin/candidate-edit";
	}
	
	@PostMapping("/update")
	public String updateUser(@Valid @ModelAttribute("user") User user,BindingResult result,
			@RequestParam("profileImage") MultipartFile file,Model model,HttpSession session,
			Principal principal) {
				
		try {							
			if(result.hasErrors()) {
				model.addAttribute("title", "Corpseed Dashboard | Edit Candidate");
				model.addAttribute("appendClass", "users");
				model.addAttribute("user", user);
				session.setAttribute("message", new Message("Please fill out the blank fields !!", "alert-danger"));
				return "admin/candidate-edit";
			}	
			User candidate=this.userService.getUserByUuid(user.getUuid());
			if(candidate==null||!candidate.getRole().equalsIgnoreCase("ROLE_USER")) {
				model.addAttribute("title", "Corpseed Dashboard | Edit Candidate");
				model.addAttribute("appendClass", "users");
				model.addAttribute("user", user);
				session.setAttribute("message", new Message("Candidate not found, Please try again later !!", "alert-danger"));
				return "admin/candidate-edit";
			}
			if(!candidate.getEmail().equalsIgnoreCase(user.getEmail())) {
				model.addAttribute("title", "Corpseed Dashboard | Edit Candidate");
				model.addAttribute("appendClass", "users");
				model.addAttribute("user", user);
				session.setAttribute("message", new Message("Candidate can't update their email !!", "alert-danger"));
				return "admin/candidate-edit";
			}
				//getting ip address
				InetAddress myIP=InetAddress.getLocalHost();
				user.setIpAddress(myIP.getHostAddress());
				//setting addedByUUID
				user.setAddedByUUID(this.commonServices.getLoginedUser(principal).getUuid());
				
				//checking profile picture exist or not
				if(!file.isEmpty()) {
					String name = azureBlobAdapter.upload(file,this.commonServices.getUniqueCode());
					user.setProfilePicture(name);
				}
							
				//saving user
				this.userService.saveUser(user);
									
				return "redirect:/admin/candidates/";
			
		} catch (Exception e) {
			model.addAttribute("title", "Corpseed Dashboard | Edit User");
			model.addAttribute("appendClass", "users");
			model.addAttribute("user", user);
			session.setAttribute("message", new Message("Error : "+e.getMessage(), "alert-danger"));
			return "admin/candidate-edit";
		}		
	}
}
