
package com.corpseed.controller;

import java.io.PrintWriter;
import java.net.InetAddress;
import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

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
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.corpseed.entity.blogentity.Blogs;
import com.corpseed.entity.hrmentity.CandidateDocuments;
import com.corpseed.entity.ForgetPassword;
import com.corpseed.entity.History;
import com.corpseed.entity.hrmentity.HrPermissions;
import com.corpseed.entity.hrmentity.HrmBlog;
import com.corpseed.entity.hrmentity.InterviewPermissions;
import com.corpseed.entity.Position;
import com.corpseed.entity.User;
import com.corpseed.util.Message;
import com.corpseed.serviceImpl.AzureBlobAdapter;
import com.corpseed.serviceImpl.blogserviceimpl.BlogService;
import com.corpseed.serviceImpl.CandidateDocumentService;
import com.corpseed.serviceImpl.CommonServices;
import com.corpseed.serviceImpl.ForgetPasswordService;
import com.corpseed.serviceImpl.HistoryService;
import com.corpseed.serviceImpl.hrmserviceimpl.HrPermissionService;
import com.corpseed.serviceImpl.PositionService;
import com.corpseed.serviceImpl.UserServices;
import com.corpseed.service.hrmservice.HrmBlogService;
import com.corpseed.service.hrmservice.InterviewPermissionService;

@Controller
@RequestMapping("/admin/users")
public class UserController {
	
	@Autowired
	private BCryptPasswordEncoder passwordEncoder;

	@Autowired
	private UserServices userService;
		
	@Autowired
	private CommonServices commonServices;
	
	@Autowired
	private HrPermissionService hrPermissionService;
	
	@Autowired
    private AzureBlobAdapter azureAdapter;
		
	@Autowired
	private PositionService positionService;
	
	@Autowired
	private CandidateDocumentService candDocService;
	
	@Autowired
	private ForgetPasswordService forgetPasswordService;
	
	@Autowired
	private HistoryService historyService;
	
	@Autowired
	private BlogService blogService;
	
	@Autowired
	private InterviewPermissionService interviewPermissionService;
	
	@Autowired
	private HrmBlogService hrmBlogService;
	
	@GetMapping("/")
	public String user(Model model) {
		model.addAttribute("title", "Corpseed Dashboard | Manage Users");
		model.addAttribute("appendClass", "users");
		model.addAttribute("users", this.userService.findAllNotUser());
		
		return "admin/users";
	}	
	
	@GetMapping("/add")
	public String userAdd(Model model) {
		model.addAttribute("title", "Corpseed Dashboard | Add User");
		model.addAttribute("appendClass", "users");
		model.addAttribute("user", new User());
		return "admin/users-add";
	}

	@PutMapping("/enabledisable")
	public void enabledisable(@RequestParam("blogUserUid") String blogUserUid,@RequestParam("uuid") String uuid,
			@RequestParam("status") int status,PrintWriter pw) {		
		User userByUuid = this.userService.getUserByUuid(uuid);
//		System.out.println("blogUserUid=="+blogUserUid+"#"+userByUuid);
		if(userByUuid!=null) {			
			userByUuid.setAccountStatus(status);
			User saveUser = this.userService.saveUser(userByUuid);
			if(saveUser!=null &&status!=1) {
				User blogUser = this.userService.getUserByUuid(blogUserUid);
				if(blogUser!=null) {
				List<Blogs> blogs = this.blogService.findByPostedByUuidAndDeleteStatus(uuid,2);
				blogs.forEach(b->{
//					System.out.println(blogUser.getFirstName()+" "+blogUser.getLastName());
					b.setPostedByUuid(blogUserUid);
					b.setPostedByName(blogUser.getFirstName()+" "+blogUser.getLastName());
				});
				this.blogService.saveAllBlogs(blogs);
				//update all hr blogs if added
					List<HrmBlog> hrmBlogs=this.hrmBlogService.findBlogsByUser(saveUser);
					if(!hrmBlogs.isEmpty()) {
						hrmBlogs.forEach(b->b.setUser(blogUser));
						this.hrmBlogService.updateAll(hrmBlogs);
					}
			}	
			}
			
			pw.write("pass");
		}else {
			pw.write("fail");
		}
	}
	
	@GetMapping("/delete/")
	public @ResponseBody void deleteUser(@RequestParam("uuid") String uuid,@RequestParam("password") String password,
			Principal principal,PrintWriter pw,HttpServletRequest req) {
		try {
		User user=this.commonServices.getLoginedUser(principal);
		if(user.getRole().equals("ROLE_ADMIN")) {
			if(passwordEncoder.matches(password, user.getPassword())) {				
				User deleteUser=this.userService.getUserByUuid(uuid);
				if(deleteUser!=null) {
					deleteUser.setDeleteStatus(1);
					this.userService.saveUser(deleteUser);
					
					String item=deleteUser.getFirstName()+" "+deleteUser.getLastName()+" : "+deleteUser.getRole().substring(5);					
					//adding user in history
					this.historyService.savehistory(new History(0, this.commonServices.getUUID(),
							"User", deleteUser.getId(), this.commonServices.getBrowser(req), 
							this.commonServices.getIpAddress(req), user.getId(), user.getFirstName()+" "+user.getLastName()
							, this.commonServices.getToday(), this.commonServices.getTime(),item));		
					
					//updating position
					Position position=deleteUser.getPosition();
					if(position!=null){
						position.setDeleteStatus(1);
						this.positionService.savePosition(position);
					}					
					
					//updating candidate document
					List<CandidateDocuments> candidateDoc=deleteUser.getDocList();
					if(!candidateDoc.isEmpty()) {
						candidateDoc.forEach(cd->cd.setDeleteStatus(1));
						this.candDocService.saveDocuments(candidateDoc);
					}
					
					//updating permissions
					List<HrPermissions> hrPermission=deleteUser.getHrPermissions();
					if(!hrPermission.isEmpty()) {
						hrPermission.forEach(hp->hp.setDeleteStatus(1));
						this.hrPermissionService.saveHrPermissions(hrPermission);
					}	
					
					//updating forget password
					List<ForgetPassword> forgetPassword=deleteUser.getForgetPassword();
					if(!forgetPassword.isEmpty()) {
						forgetPassword.forEach(fp->fp.setDeleteStatus(1));
						this.forgetPasswordService.saveForgetPasswordLinks(forgetPassword);
					}
				}
				pw.write("pass");			
			}else {
				pw.write("fail");
			}
		}else {
			pw.write("fail");
		}
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	@GetMapping("/edit/{useruuid}")
	public String userEdit(@PathVariable("useruuid") String useruuid,Model model) {
		model.addAttribute("title", "Corpseed Dashboard | Edit User");
		model.addAttribute("appendClass", "users");
		model.addAttribute("useruuid", useruuid);
		User user=this.userService.getUserByUuid(useruuid);
		if(user==null)
			return "error/404";
		
		model.addAttribute("user", user);

		setInterviewPermission(user,model);
		
		return "admin/users-edit";
	}
	
	private void setInterviewPermission(User user,Model model) {
		List<String> permissions=new ArrayList<>();
		permissions.add("it");
		permissions.add("sales");
		permissions.add("delivery");
		permissions.add("hr");
		permissions.add("marketing");
		permissions.add("legal");
		List<String> permissions2=new ArrayList<>();
		permissions2.addAll(permissions);
		
		if(!user.getHrPermissions().isEmpty())
		for (HrPermissions perm : user.getHrPermissions()) {
			permissions.remove(perm.getJobType());
		}
		model.addAttribute("permissions", permissions);

		List<InterviewPermissions> interviewPermissions = this.interviewPermissionService.findByUser(user);
		for (InterviewPermissions interviewP2 : interviewPermissions) {
			permissions2.remove(interviewP2.getDepartment());
		}
		model.addAttribute("interviewPermissions", interviewPermissions);
		model.addAttribute("permissions1", permissions2);
	}

	@PostMapping("/update")
	public String updateUser(@Valid @ModelAttribute("user") User user,BindingResult result,
			@RequestParam("profileImage") MultipartFile file,Model model,HttpSession session,
			Principal principal,@RequestParam("interviewPermission") List<String> interviewPermission) {
		try {					
			String permissions=user.getPermissions();
						
			if(result.hasErrors()) {
				model.addAttribute("title", "Corpseed Dashboard | Edit User");
				model.addAttribute("appendClass", "users");
				model.addAttribute("user", user);
				session.setAttribute("message", new Message("Please fill out the blank fields !!", "alert-danger"));
				setInterviewPermission(user, model);
				return "admin/users-edit";
			}
			if(user.getRole().equals("ROLE_HR_EXECUTIVE")&&(user.getPermissions()==null||user.getPermissions().length()<=0)) {
				model.addAttribute("title", "Corpseed Dashboard | Edit User");
				model.addAttribute("appendClass", "users");
				model.addAttribute("user", user);
				session.setAttribute("message", new Message("Please select Permission for HR Executive !!", "alert-danger"));
				setInterviewPermission(user, model);
				return "admin/users-edit";
			}
			
			User userExist = userService.findByEmailAndUuidNot(user.getEmail(),user.getUuid());
			if(userExist!=null) {
				model.addAttribute("title", "Corpseed Dashboard | Edit User");
				model.addAttribute("appendClass", "users");
				model.addAttribute("user", user);
				if(userExist.getDeleteStatus()==1)
					session.setAttribute("message", new Message("User exist in Trash !!", "alert-danger"));
				else
					session.setAttribute("message", new Message("User already exist !!", "alert-danger"));
				
				setInterviewPermission(user, model);

				return "admin/users-edit";
			}

			User userSlug=userService.findBySlugRoleNotAndIdNot(user.getSlug(),"ROLE_USER",user.getId());
			if(userSlug!=null){
				model.addAttribute("title", "Corpseed Dashboard | Edit User");
				model.addAttribute("appendClass", "users");
				model.addAttribute("user", user);
				session.setAttribute("message", new Message("Slug already exist !!", "alert-danger"));
				return "admin/users-edit";
			}
				//getting ip address
				InetAddress myIP=InetAddress.getLocalHost();
				user.setIpAddress(myIP.getHostAddress());
				//setting addedByUUID
				user.setAddedByUUID(this.commonServices.getLoginedUser(principal).getUuid());
				
				//checking profile picture exist or not
				if(!file.isEmpty()) {
					String name = azureAdapter.upload(file,this.commonServices.getUniqueCode());
					user.setProfilePicture(name);
				}
							
				//saving user
				User saveUser = this.userService.saveUser(user);
				if(saveUser==null) {
					model.addAttribute("title", "Corpseed Dashboard | Edit User");
					model.addAttribute("appendClass", "users");
					model.addAttribute("user", user);
					model.addAttribute("message", new Message("Data Not Saved, Please try-again later !!", "alert-danger"));
					setInterviewPermission(user, model);
					return "admin/users-edit";
				}
				if(user.getRole().equals("ROLE_HR_EXECUTIVE")&&permissions!=null&&permissions.length()>0) {
					String x[]=permissions.split(",");
					List<HrPermissions> permissionList=new ArrayList<>();
					for (String jobType : x) {
						HrPermissions hrPermission=this.hrPermissionService.findByUserAndJobType(saveUser,jobType);
						if(hrPermission==null) {
							permissionList.add(new HrPermissions(0, this.commonServices.getUUID(), jobType, saveUser));
						}else {
							permissionList.add(new HrPermissions(hrPermission.getId(), hrPermission.getUuid(), jobType, saveUser));
						}
					}
					permissionList=this.hrPermissionService.saveHrPermissions(permissionList);
					List<HrPermissions> allPermissionList=this.hrPermissionService.findByUser(saveUser);
					allPermissionList.removeAll(permissionList);
					this.hrPermissionService.deleteAlls(allPermissionList);
											
				}
				if(interviewPermission!=null&&!interviewPermission.isEmpty()) {
					List<InterviewPermissions> newList=new ArrayList<>();
					for (String ip : interviewPermission) {
						InterviewPermissions interview=this.interviewPermissionService.findByDepartmentAndUser(ip, saveUser);
						if(interview==null) {
							newList.add(new InterviewPermissions(0, ip, saveUser));
						}else newList.add(interview);
					}
					if(!newList.isEmpty()) {
						newList=this.interviewPermissionService.saveAllInterviewDepartment(newList);
						List<InterviewPermissions> allIP=this.interviewPermissionService.findByUser(saveUser);
						allIP.removeAll(newList);
						if(!allIP.isEmpty())
							this.interviewPermissionService.deleteAll(allIP);
					}
					
				}
				
				return "redirect:/admin/users/";
				
		} catch (Exception e) {
			model.addAttribute("title", "Corpseed Dashboard | Edit User");
			model.addAttribute("appendClass", "users");
			model.addAttribute("user", user);
			session.setAttribute("message", new Message("Error : "+e.getMessage(), "alert-danger"));
			return "admin/users-edit";
		}		
	}
	
	@PostMapping("/add")
	public String saveUser(@Valid @ModelAttribute("user") User user,BindingResult result,
			@RequestParam("profileImage") MultipartFile file,Model model,HttpSession session,
			Principal principal,@RequestParam("interviewPermission") List<String> interviewPermission) {		
		
		try {
			if(result.hasErrors()) {
				model.addAttribute("title", "Corpseed Dashboard | Add User");
				model.addAttribute("appendClass", "users");
				model.addAttribute("user", user);
				session.setAttribute("message", new Message("Please fill out the blank fields !!", "alert-danger"));
				return "admin/users-add";
			}
			if(user.getRole().equals("ROLE_HR_EXECUTIVE")&&(user.getPermissions()==null||user.getPermissions().length()<=0)) {
				model.addAttribute("title", "Corpseed Dashboard | Add User");
				model.addAttribute("appendClass", "users");
				model.addAttribute("user", user);
				session.setAttribute("message", new Message("Please select Permission for HR Executive !!", "alert-danger"));
				return "admin/users-add";
			}
			User userExist = userService.isUserExist(user.getEmail());
			if(userExist!=null) {				
				model.addAttribute("title", "Corpseed Dashboard | Add User");
				model.addAttribute("appendClass", "users");
				model.addAttribute("user", user);
				if(userExist.getDeleteStatus()==1)
					session.setAttribute("message", new Message("User exist in Trash !!", "alert-danger"));
				else
					session.setAttribute("message", new Message("User already exist !!", "alert-danger"));
				
				return "admin/users-add";
			}
			User userSlug=userService.findBySlugRoleNot(user.getSlug(),"ROLE_USER");
			if(userSlug!=null){
				model.addAttribute("title", "Corpseed Dashboard | Add User");
				model.addAttribute("appendClass", "users");
				model.addAttribute("user", user);
				session.setAttribute("message", new Message("Slug already exist !!", "alert-danger"));
				return "admin/users-add";
			}
				user.setUuid(this.commonServices.getUUID());				
				//getting ip address
				InetAddress myIP=InetAddress.getLocalHost();
				user.setIpAddress(myIP.getHostAddress());
				//setting addedByUUID
				user.setAddedByUUID(this.commonServices.getLoginedUser(principal).getUuid());
				user.setAccountStatus(1);
				//encoding password
				user.setPassword(passwordEncoder.encode(user.getPassword()));
				//checking profile picture exist or not
				if(file.isEmpty()) {
					user.setProfilePicture("profile.png");
				}else {
					String name = azureAdapter.upload(file,this.commonServices.getUniqueCode());
					user.setProfilePicture(name);
				}
							
				//saving user
				User saveUser = this.userService.saveUser(user);
				if(saveUser==null) {
					model.addAttribute("title", "Corpseed Dashboard | Add User");
					model.addAttribute("appendClass", "users");
					model.addAttribute("user", user);
					model.addAttribute("message", new Message("Data Not Saved, Please try-again later !!", "alert-danger"));
					return "admin/users-add";
				}
				if(user.getRole().equals("ROLE_HR_EXECUTIVE")&&user.getPermissions()!=null&&user.getPermissions().length()>0) {
					String x[]=user.getPermissions().split(",");
					List<HrPermissions> permissionList=new ArrayList<>();
					for (String jobType : x) {
						permissionList.add(new HrPermissions(0, this.commonServices.getUUID(), jobType, saveUser));
					}
					if(!permissionList.isEmpty()) {
						this.hrPermissionService.saveHrPermissions(permissionList);
					}
				}		
				if(interviewPermission!=null&&!interviewPermission.isEmpty()) {
					List<InterviewPermissions> pList=new ArrayList<>();
					for (String ip : interviewPermission) {
						pList.add(new InterviewPermissions(0, ip, saveUser));
					}
					if(!pList.isEmpty())this.interviewPermissionService.saveAllInterviewDepartment(pList);
				}
				
				return "redirect:/admin/users/";
						
						
		} catch (Exception e) {e.printStackTrace();
			model.addAttribute("title", "Corpseed Dashboard | Add User");
			model.addAttribute("appendClass", "users");
			model.addAttribute("user", user);
			session.setAttribute("message", new Message("Error : ", "alert-danger"));
			return "admin/users-add";
		}		
	}
	@GetMapping("/change-password/{uuid}")
	public String changePassword(@PathVariable("uuid") String uuid,Model model) {
		model.addAttribute("title", "Corpseed Dashboard | Change Password");
		model.addAttribute("appendClass", "users");
		model.addAttribute("uuid", uuid);
		return "admin/password-update";
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
				return "admin/password-update";
			}
			if(!newPassword.equals(confirmNewPassword)) {
				model.addAttribute("title", "Corpseed Dashboard | Change Password");
				model.addAttribute("appendClass", "users");
				model.addAttribute("newPassword", newPassword);
				model.addAttribute("confirmNewPassword", confirmNewPassword);
				session.setAttribute("message", new Message("New Password and Confirm password didn't match !!", "alert-danger"));
				return "admin/password-update";
			}
			
			User user=this.userService.getUserByUuid(uuid);
			if(user==null) {
				model.addAttribute("title", "Corpseed Dashboard | Change Password");
				model.addAttribute("appendClass", "users");
				model.addAttribute("newPassword", newPassword);
				model.addAttribute("confirmNewPassword", confirmNewPassword);
				session.setAttribute("message", new Message("User not found ,Please Try again later !!", "alert-danger"));
				return "admin/password-update";
			}
				user.setPassword(this.passwordEncoder.encode(newPassword));
				this.userService.saveUser(user);
				return "redirect:/admin/users/";
						
		} catch (Exception e) {
			model.addAttribute("title", "Corpseed Dashboard | Change Password");
			model.addAttribute("appendClass", "users");
			model.addAttribute("newPassword", newPassword);
			model.addAttribute("confirmNewPassword", confirmNewPassword);
			session.setAttribute("message", new Message("Error : "+e.getMessage(), "alert-danger"));
			return "admin/password-update";
		}
	}
}
