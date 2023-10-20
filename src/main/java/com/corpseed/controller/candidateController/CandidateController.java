package com.corpseed.controller.candidateController;

import java.io.PrintWriter;
import java.security.Principal;
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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.corpseed.entity.hrmentity.CandidateDocuments;
import com.corpseed.entity.schedulerentity.EmailScheduler;
import com.corpseed.entity.hrmentity.JobApplication;
import com.corpseed.entity.hrmentity.Jobs;
import com.corpseed.entity.User;
import com.corpseed.util.Message;
import com.corpseed.serviceImpl.AzureBlobAdapter;
import com.corpseed.serviceImpl.CandidateDocumentService;
import com.corpseed.serviceImpl.CommonServices;
import com.corpseed.serviceImpl.hrmserviceimpl.JobApplicationServices;
import com.corpseed.serviceImpl.hrmserviceimpl.JobServices;
import com.corpseed.serviceImpl.UserServices;
import com.corpseed.service.EmailSchedulerService;

@Controller
@RequestMapping("/candidate")
public class CandidateController {

	@Autowired
	private JobApplicationServices jobAppServices;
	
	@Autowired
	private JobServices jobServices;
	
	@Autowired
	private CommonServices commonService;
	
	@Autowired
    AzureBlobAdapter azureAdapter;
	
	@Autowired
	private UserServices userService;
	
	@Autowired
	private CandidateDocumentService candidateDocumentService;
	
	@Autowired
	private BCryptPasswordEncoder passwordEncoder;
		
	@Autowired
	private EmailSchedulerService emailService;
	
	@GetMapping("/")
	public String home(Model model,Principal principal,HttpSession session) {
		model.addAttribute("title", "Corpseed Dashboard | Job Application");
		model.addAttribute("appendClass", "jobApp");
		User user=this.commonService.getLoginedUser(principal);
		model.addAttribute("jobApplication", this.jobAppServices.findByEmailAndDisplayStatus(user.getEmail(),"1"));
		session.setAttribute("displayPicture", user.getProfilePicture());
		return "candidates/job-application";
	}
	@GetMapping("/my-profile")
	public String myProfile(Model model,Principal principal) {
		model.addAttribute("title", "Corpseed Dashboard | My Profile");
		model.addAttribute("appendClass", "profile");
		model.addAttribute("user",this.commonService.getLoginedUser(principal));
		return "candidates/users-edit";
	}
	@GetMapping("/change-password")
	public String changePassword(Model model) {
		model.addAttribute("title", "Corpseed Dashboard | Change password");		
		return "candidates/change-password";
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
			return "candidates/change-password";
		}
		
		if(!newPassword.equals(confirmNewPassword)) {
			model.addAttribute("title", "Corpseed Dashboard | Change password");
			model.addAttribute("oldPassword", oldPassword);
			model.addAttribute("newPassword", newPassword);
			model.addAttribute("confirmNewPassword", confirmNewPassword);
			session.setAttribute("message", new Message("New Password and Confirm Password didn't match !!", "text-danger"));
			return "candidates/change-password";
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
			return "candidates/change-password";
		}
		
		
	}
	@GetMapping("/upload-document/{uuid}")
	public String uploadDocument(@PathVariable("uuid") String uuid,Model model,Principal principal) {
		model.addAttribute("title", "Corpseed Dashboard | Upload Documents");
		model.addAttribute("appendClass", "jobApp");
		JobApplication jobApp=this.jobAppServices.getJobAppByUuid(uuid);
		if(jobApp.getUploadDocStatus().equals("1")) {
			model.addAttribute("documents",jobApp.getUser().getDocList());
			
			return "candidates/doc-upload";
		}else {
			return "error/403";
		}
	}
	@PostMapping("/my-profile/update")
	public String updateMyProfile(@Valid @ModelAttribute("user") User user,BindingResult result,
			@RequestParam("profileImage") MultipartFile file,Model model,Principal principal,HttpSession session
			,HttpServletRequest req) {
		try {
			if(result.hasErrors()) {
				model.addAttribute("title", "Corpseed Dashboard | My Profile");
				model.addAttribute("appendClass", "profile");
				model.addAttribute("user", user);
				session.setAttribute("message", new Message("Please fill out the blank fields !!", "alert-danger"));
				return "candidates/users-edit";
			}
			if(!file.isEmpty()) {
				if(!user.getProfilePicture().equalsIgnoreCase("profile.png")) {
					this.azureAdapter.deleteFile(user.getProfilePicture());
				}
				String name = azureAdapter.upload(file,this.commonService.getUniqueCode());
				user.setProfilePicture(name);				
			}else {
				user.setProfilePicture("profile.png");
			}
			User findUser=this.userService.getUserByUuid(user.getUuid());
			if(findUser==null) {
				model.addAttribute("title", "Corpseed Dashboard | My Profile");
				model.addAttribute("appendClass", "profile");
				model.addAttribute("user", user);
				session.setAttribute("message", new Message("User Not Found, Please try-again later !!", "alert-danger"));
				return "candidates/users-edit";
			}
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
				if(saveUser==null) {
					model.addAttribute("title", "Corpseed Dashboard | My Profile");
					model.addAttribute("appendClass", "profile");
					model.addAttribute("user", user);
					session.setAttribute("message", new Message("Data Not Saved, Please try-again later !!", "alert-danger"));
					return "candidates/users-edit";
				}else {
					return "redirect:/candidate/";
				}
		} catch (Exception e) {
			model.addAttribute("title", "Corpseed Dashboard | My Profile");
			model.addAttribute("appendClass", "profile");
			model.addAttribute("user", user);
			session.setAttribute("message", new Message("Someting went wrong, Please try-again later !!", "alert-danger"));
			return "candidates/users-edit";
		}
		
		
	}
	@PostMapping("/uploadFile")
	@ResponseBody
	public void uploadFile(@RequestParam("uuid") String uuid,@RequestParam("file") MultipartFile file,PrintWriter pw,Principal principal) {
		if(!file.isEmpty()) {	
			CandidateDocuments candDoc=this.candidateDocumentService.findByUuid(uuid);
			long addKey=this.commonService.getUniqueCode();
			String fileName=addKey+file.getOriginalFilename().replace(" ", "_");
			if(candDoc!=null) {
				if(!candDoc.getName().equalsIgnoreCase("NA"))
					this.azureAdapter.deleteFile(candDoc.getName());
				String name = azureAdapter.upload(file,this.commonService.getUniqueCode());
				candDoc.setName(name);
				this.candidateDocumentService.saveDocument(candDoc);
			}
			JobApplication jobApplication = candDoc.getJobApplication();
			if(jobApplication.getUploadDocEmail().equals("2")) {
				int doc=0;
				List<CandidateDocuments> candidateDocument = jobApplication.getCandidateDocument();
				for (CandidateDocuments cd : candidateDocument) {
					String docType=cd.getType();
					if(!cd.getName().equalsIgnoreCase("NA"))
					if(docType.equals("Passport Size Photo")||docType.equals("Aaddhar Card")||
							docType.equals("Pan card")||docType.equals("Qualification Certificate")
							||docType.equals("Cancelled Cheque")||docType.equals("Background Verification Sheet"))doc+=1;
				}
				if(doc>5) {
					//sending mail to hr to verify document
					User user=this.userService.getUserByUuid(jobApplication.getTrackApplication().getAddedByUuid());
					User loginUser=this.commonService.getLoginedUser(principal);
					String message="<p>Hi <b>"+user.getFirstName()+" "+user.getLastName()+"</b>,</p>"
							+"<p>Candidate <b>"+loginUser.getFirstName()+" "+loginUser.getLastName()
									+ "</b> has submitted the documents for verification process."
									+ "Please review the documents.</p><p><b>Note : </b> Please reject document if documents are need to be upload again.</p>"
									+ "<p>Regards,<br/>Corpseed updates</p>";
					this.emailService.saveEmail(new EmailScheduler(0, user.getEmail(),"empty", "Notification : Verify Document", message, 2));
				jobApplication.setUploadDocEmail("1");
				this.jobAppServices.saveJobApplication(jobApplication);
				}
			}
			
			pw.write(fileName);
		}
	}
	/*---------------------------------------open roles-------------------------------*/
	@GetMapping("/open-roles")
	public String openRoles(Model model) {
		model.addAttribute("title", "Corpseed Dashboard | Open Roles");
		model.addAttribute("appendClass", "roles");
		model.addAttribute("jobs", this.jobServices.getJobByStatus("1"));
		return "candidates/open-roles";
	}
	@GetMapping("/open-role/{slug}/{uuid}")
	public String joinOurTeam(@PathVariable("uuid") String uuid,Model model) {
		Jobs job=this.jobServices.findJobByUuidAndStatus(uuid,"1");
		model.addAttribute("title", "Corpseed Dashboard | Open role");
		model.addAttribute("appendClass", "roles");
		if(job==null) {
			return "error/404";
		}else {
			model.addAttribute("job", job);
			model.addAttribute("jobApp", new JobApplication());
			return "candidates/role-details";
		}
	}
	
	@GetMapping("/apply-job/{uuid}")
	public String applyJob(@PathVariable("uuid") String uuid,Model model,Principal principal) {
		Jobs job=this.jobServices.findJobByUuidAndStatus(uuid, "1");		
		model.addAttribute("title", "Corpseed Dashboard | Apply Job");
		model.addAttribute("appendClass", "roles");
		JobApplication jobApp=this.jobAppServices.findTop1ByEmailOrderByIdDesc(this.commonService.getLoginedUser(principal).getEmail());
		
		if(jobApp==null) {
			jobApp=new JobApplication();
			jobApp.setJobs(job);
			jobApp.setDisplayStatus("1");
			model.addAttribute("jobApplication", jobApp);
		}else {
			jobApp.setJobs(job);
			jobApp.setDisplayStatus("1");
			model.addAttribute("jobApplication", jobApp);
		}
		return "candidates/job-application-add";
	}
	@PostMapping("/apply-job")
	public String saveJobApply(@Valid @ModelAttribute("jobApplication") JobApplication jobApp,BindingResult result,Model model,
			@RequestParam("file") MultipartFile file,HttpSession session,Principal principal) {
		try {
		
			if(result.hasErrors()) {
				model.addAttribute("title", "Corpseed Dashboard | Apply Job");
				model.addAttribute("appendClass", "roles");
				session.setAttribute("message", new Message("Please fill out the blank fields !!", "alert-danger"));
				return "candidates/job-application-add";
			}
			if(jobApp.getJobs()==null) {
				model.addAttribute("title", "Corpseed Dashboard | Apply Job");
				model.addAttribute("appendClass", "roles");
				session.setAttribute("message", new Message("Something went wrong, Please try-again later !!", "alert-danger"));
				return "candidates/job-application-add";
			}
			String email=this.commonService.getLoginedUser(principal).getEmail();
			jobApp.setEmail(email);
			if(this.jobAppServices.findByEmailAndJobsAndPostDateAfter(email,jobApp.getJobs(),this.commonService.dateBeforeDays(90))!=null){
				model.addAttribute("title", "Corpseed Dashboard | Apply Job");
				model.addAttribute("appendClass", "roles");
				session.setAttribute("message", new Message("You already applied for this position, Please try after 3 months !!", "alert-danger"));
				return "candidates/job-application-add";
			}
			if(!file.isEmpty()) {
				String name = azureAdapter.upload(file,this.commonService.getUniqueCode());
				jobApp.setAttachedFile(name);
			}
			jobApp.setId(0);
			jobApp.setUuid(this.commonService.getUUID());
			jobApp.setAddedByUUID(this.commonService.getLoginedUser(principal).getUuid());
			JobApplication saveJobApplication = this.jobAppServices.saveJobApplication(jobApp);
			if(saveJobApplication==null) {
				model.addAttribute("title", "Corpseed Dashboard | Apply Job");
				model.addAttribute("appendClass", "roles");
				session.setAttribute("message", new Message("Data Not Saved, Please try-again later !!", "alert-danger"));
				return "candidates/job-application-add";
			}
			return "redirect:/candidate/";
			
		}catch(Exception e) {
			model.addAttribute("title", "Corpseed Dashboard | Apply Job");
			model.addAttribute("appendClass", "roles");
			session.setAttribute("message", new Message("Something went wrong, Please try-again later !!", "alert-danger"));
			return "candidates/job-application-add";
		}
	}
}
