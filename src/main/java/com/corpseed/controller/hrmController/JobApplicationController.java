package com.corpseed.controller.hrmController;

import java.io.PrintWriter;
import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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
import com.corpseed.entity.History;
import com.corpseed.entity.hrmentity.HrPermissions;
import com.corpseed.entity.hrmentity.InterviewPermissions;
import com.corpseed.entity.hrmentity.InterviewSchedule;
import com.corpseed.entity.hrmentity.JobApplication;
import com.corpseed.entity.hrmentity.Jobs;
import com.corpseed.entity.hrmentity.TrackApplication;
import com.corpseed.entity.User;
import com.corpseed.util.Message;
import com.corpseed.serviceImpl.AzureBlobAdapter;
import com.corpseed.serviceImpl.CandidateDocumentService;
import com.corpseed.serviceImpl.CommonServices;
import com.corpseed.serviceImpl.HistoryService;
import com.corpseed.serviceImpl.hrmserviceimpl.InterviewScheduleService;
import com.corpseed.serviceImpl.hrmserviceimpl.JobApplicationServices;
import com.corpseed.serviceImpl.hrmserviceimpl.JobServices;
import com.corpseed.serviceImpl.hrmserviceimpl.TrackAppService;
import com.corpseed.serviceImpl.UserServices;
import com.corpseed.service.EmailSchedulerService;
import com.corpseed.service.hrmservice.InterviewPermissionService;

@Controller
@RequestMapping("/hrm/job-application")
public class JobApplicationController {

	@Autowired
	private JobApplicationServices jobAppServices;
	
	@Autowired
	private CommonServices commonServices;
	
	@Autowired
	private JobServices jobService;
	
	@Autowired
    AzureBlobAdapter azureAdapter;
	
	@Autowired
	private InterviewScheduleService interviewScheduleService;	
	
	@Autowired
	private TrackAppService trackAppService;
	
	@Autowired
	private UserServices userService;
	
	@Autowired
	private CandidateDocumentService candidateDocService;
	
	@Autowired
	private HistoryService historyService;
	
	@Autowired
	private EmailSchedulerService emailSchedulerService;
	
	@Autowired
	private Environment env;
	
	@Autowired
	private BCryptPasswordEncoder passwordEncoder;
	
	@Autowired
	private InterviewPermissionService interviewPermService;
	
	@GetMapping("/search/{search}")
	public void searchApplication(@PathVariable("search") String search,PrintWriter pw,Principal principal) {
		String data="";
		try {
			User user=this.commonServices.getLoginedUser(principal);
			String role=user.getRole();
			List<JobApplication> jobApplication=null;
			if(role.equals("ROLE_ADMIN")||role.equals("ROLE_HR_MANAGER")) {
				jobApplication=this.jobAppServices.findJobApplicationByApplicationStatusAndBySearch(search);
			}else if(role.equals("ROLE_HR_EXECUTIVE")){
				List<HrPermissions> hrPermissions=user.getHrPermissions();
				List<String> jobType=new ArrayList<>();
				hrPermissions.forEach(h->jobType.add(h.getJobType()));
				jobApplication=this.jobAppServices.getJobApplicationByStatusAndJobTypeAndSearch(jobType, search);
				
			}		
			if(!jobApplication.isEmpty()) {
				for (JobApplication ja : jobApplication) {
					data+="<li><a href='/hrm/job-application/?search="+search+"'>"+ja.getPostDate()+"/"+ja.getJobs().getPosition()+"/"+ja.getName()+"/"+ja.getEmail()+"</a></li>";
				}				
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		pw.write(data);
	}
	
	
	@GetMapping("/")
	public String jobApplication(Model model,@RequestParam("page") Optional<Integer> page, 
		      @RequestParam("size") Optional<Integer> size,@RequestParam("col") Optional<String> col,
		      @RequestParam("ord") Optional<String> ord,@RequestParam("search") Optional<String> search,
		      Principal principal) {
		int currentPage = page.orElse(1);
        int pageSize = size.orElse(10);
        String column=col.orElse("id");
        String order=ord.orElse("desc");
        String searchText=search.orElse("");
        
		model.addAttribute("title", "Corpseed Dashboard | Manage Job Application");
		model.addAttribute("appendClass", "hrm");
		model.addAttribute("searchText", searchText);
		
		Pageable pageable=null;
		if(order.equalsIgnoreCase("desc")) {
			pageable=PageRequest.of((currentPage-1), pageSize,Sort.by(column).descending());
			model.addAttribute("order", "asc");
			model.addAttribute("sortTitle", "Descending Order");
		}			
		else {
			pageable=PageRequest.of((currentPage-1), pageSize,Sort.by(column).ascending());
			model.addAttribute("order", "desc");
			model.addAttribute("sortTitle", "Ascending Order");
		}
		Page<JobApplication> jobApplication=null;
		User user=this.commonServices.getLoginedUser(principal);
		String role=user.getRole();
		if(role.equals("ROLE_ADMIN")||role.equals("ROLE_HR_MANAGER")) {
			if(searchText==null)
				jobApplication=this.jobAppServices.findJobApplicationByApplicationStatus("3",pageable);
			else
				jobApplication=this.jobAppServices.findJobAppByStatusAndSearch(searchText,pageable);
		}else if(role.equals("ROLE_HR_EXECUTIVE")){
			List<HrPermissions> hrPermissions=user.getHrPermissions();
			List<String> jobType=new ArrayList<>();
			hrPermissions.forEach(h->{jobType.add(h.getJobType());});
			
			if(searchText==null)
				jobApplication=this.jobAppServices.getJobApplicationByApplicationStatusAndJobType(jobType, pageable);
			else
				jobApplication=this.jobAppServices.getJobAppByStatusAndJobTypeAndSearch(jobType,searchText, pageable);
		}	
		
		model.addAttribute("jobApplication",jobApplication);
		
		int totalPages = jobApplication.getTotalPages();
        model.addAttribute("totalRecords", jobApplication.getTotalElements());
        model.addAttribute("totalPages", totalPages);
		
        int startRange=currentPage-2;
        int endRange=currentPage+2;
        
        if (totalPages > 1) {        	 
             if((endRange-2)==totalPages)startRange=currentPage-4;        
             if(startRange==currentPage)endRange=currentPage+4;
             if(startRange<1) {startRange=1;endRange=startRange+4;}
             if(endRange>totalPages) {endRange=totalPages;startRange=endRange-4;}             
             if(startRange<1)startRange=1;
             
            List<Integer> pageNumbers = IntStream.rangeClosed(startRange, endRange)
                .boxed()
                .collect(Collectors.toList());
            model.addAttribute("pageNumbers", pageNumbers);
        }
        
		model.addAttribute("cpage", currentPage);
		model.addAttribute("column", column);
		
		return "admin/job-application";
	}
	@GetMapping("/selected/")
	public String selectedJobApplication(Model model,Principal principal) {
		model.addAttribute("title", "Corpseed Dashboard | Manage Selected Job Application");
		model.addAttribute("appendClass", "hrm");
		User user=this.commonServices.getLoginedUser(principal);
		String role=user.getRole();	
		List<JobApplication> jobApplication=null;
		
		if(role.equals("ROLE_ADMIN")||role.equals("ROLE_HR_MANAGER")) {
			jobApplication=this.jobAppServices.getJobApplicationByApplicationStatus("1");		
		}else if(role.equals("ROLE_HR_EXECUTIVE")){
			List<HrPermissions> hrPermissions=user.getHrPermissions();
			List<String> jobType=new ArrayList<>(); 
			hrPermissions.forEach(h->{jobType.add(h.getJobType());});
			jobApplication=this.jobAppServices.getJobApplicationByApplicationStatusAndJobType(jobType,"1");
		
		}		
		model.addAttribute("jobApplication",jobApplication);
		return "admin/selected-application";
	}
	@GetMapping("/hold/")
	public String holdJobApplication(Model model,Principal principal) {
		model.addAttribute("title", "Corpseed Dashboard | Manage Hold Job Application");
		model.addAttribute("appendClass", "hrm");
		User user=this.commonServices.getLoginedUser(principal);
		String role=user.getRole();	
		List<JobApplication> jobApplication=null;
		
		if(role.equals("ROLE_ADMIN")||role.equals("ROLE_HR_MANAGER")) {
			jobApplication=this.jobAppServices.getJobApplicationByApplicationStatus("4");		
		}else if(role.equals("ROLE_HR_EXECUTIVE")){
			List<HrPermissions> hrPermissions=user.getHrPermissions();
			List<String> jobType=new ArrayList<>(); 
			hrPermissions.forEach(h->{jobType.add(h.getJobType());});
			jobApplication=this.jobAppServices.getJobApplicationByApplicationStatusAndJobType(jobType,"4");
		
		}		
		model.addAttribute("jobApplication",jobApplication);
		return "admin/hold-application";
	}
	@GetMapping("/rejected/")
	public String rejectedJobApplication(Model model,Principal principal) {
		model.addAttribute("title", "Corpseed Dashboard | Manage Rejected Job Application");
		model.addAttribute("appendClass", "hrm");
		List<JobApplication> jobApplicationList=new ArrayList<>();
		User user=this.commonServices.getLoginedUser(principal);
		String role=user.getRole();
		if(role.equals("ROLE_ADMIN")||role.equals("ROLE_HR_MANAGER")) {
			jobApplicationList=this.jobAppServices.getJobApplicationByApplicationStatus("2");		
		}else if(role.equals("ROLE_HR_EXECUTIVE")){
			List<HrPermissions> hrPermissions=user.getHrPermissions();
			List<String> jobType=new ArrayList<>();
			hrPermissions.forEach(h->{jobType.add(h.getJobType());});
			jobApplicationList=this.jobAppServices.getJobApplicationByApplicationStatusAndJobType(jobType,"2");		
			
		}		
		model.addAttribute("jobApplication",jobApplicationList);
		return "admin/rejected-application";
	}
	@GetMapping("/interviewer/")
	@ResponseBody
	public void interviewers(Model model,@RequestParam("dept") String dept,PrintWriter pw) {
		List<String> roles=new ArrayList<>();
		roles.add("ROLE_ADMIN");
		roles.add("ROLE_USER"); 
				
		List<InterviewPermissions> findByDepartment = this.interviewPermService.findByDepartment(dept);
		
		StringBuffer options=new StringBuffer("<option value=''>Select Interviewer</option>");
		findByDepartment.forEach(d->
			options.append("<option value='"+d.getUser().getId()+"#"+d.getUser().getFirstName()+" "+d.getUser().getLastName()+"'>"+d.getUser().getFirstName()+" "+d.getUser().getLastName()+"</option>"));
		pw.write(options.toString());
	}	
	@GetMapping("/add")
	public String jobApplicationAdd(Model model) {
		model.addAttribute("title", "Corpseed Dashboard | Add Jobs");
		model.addAttribute("appendClass", "hrm");
		model.addAttribute("jobs", this.jobService.getAllJobs());
		model.addAttribute("jobApplication", new JobApplication());
		return "admin/job-application-add";
	}	
	
	@GetMapping("/delete/{uuid}")
	public String deleteJobApplication(@PathVariable("uuid") String uuid,Principal principal,HttpServletRequest req) {
		if(this.commonServices.getLoginedUser(principal).getRole().equals("ROLE_ADMIN")) {
			JobApplication jobApp=this.jobAppServices.getJobAppByUuid(uuid);
			if(jobApp!=null) {
				jobApp.setDeleteStatus(1);
				this.jobAppServices.saveJobApplication(jobApp);

				User user=this.commonServices.getLoginedUser(principal);
				
				//adding job application
				this.historyService.savehistory(new History(0, this.commonServices.getUUID(),
						"Job Application", jobApp.getId(), this.commonServices.getBrowser(req), 
						this.commonServices.getIpAddress(req), user.getId(), user.getFirstName()+" "+user.getLastName()
						, this.commonServices.getToday(), this.commonServices.getTime(),jobApp.getName()));		
				
				//updating scheduled interview
				List<InterviewSchedule> interviewSchedule=jobApp.getInterviewSchedule();
				if(!interviewSchedule.isEmpty()) {
					interviewSchedule.forEach(is->is.setDeleteStatus(1));
					this.interviewScheduleService.saveScheduledInterview(interviewSchedule);
				}
				//updating track application
				TrackApplication trackApp=jobApp.getTrackApplication();
				if(trackApp!=null) {
					trackApp.setDeleteStatus(1);
					this.trackAppService.saveTrackApplication(trackApp);					
				}
				//updating candidate documents
				List<CandidateDocuments> candidateDocument=jobApp.getCandidateDocument();
				if(!candidateDocument.isEmpty()) {
					candidateDocument.forEach(cd->cd.setDeleteStatus(1));
					this.candidateDocService.saveDocuments(candidateDocument);
				}
				
			}
			
			return "redirect:/hrm/job-application/";
		}else {
			return "error/403";
		}
	}
	
	@GetMapping("/edit/{jobsAppUUID}")
	public String editJobApplication(@PathVariable("jobsAppUUID") String jobsAppUUID,Model model) {
		model.addAttribute("title", "Corpseed Dashboard | Edit Job-Application");
		model.addAttribute("appendClass", "hrm");
		model.addAttribute("jobs", this.jobService.getAllJobs());
		model.addAttribute("jobApplication", this.jobAppServices.getJobAppByUuid(jobsAppUUID));
		return "admin/job-application-edit";
	}
	@GetMapping("/update/")
	@ResponseBody
	public void updateStatus(@RequestParam("uuid") String uuid,@RequestParam("name") String name,
			Principal principal){
		JobApplication jobApp=this.jobAppServices.getJobAppByUuid(uuid);
		if(jobApp!=null&&!jobApp.getApplicationStatus().equals("2")) {
			jobApp.setApplicationStatus("2");
			jobApp.setActionBy(this.commonServices.getLoginedUser(principal));
			this.jobAppServices.saveJobApplication(jobApp);
			String message="<p>Dear "+name+",</p><br/>"
					+"<p>Thank you for your application for the <b>"+jobApp.getJobs().getPosition()+"</b> at Corpseed. We really appreciate your interest in joining our company and we want to thank you for the time and energy you invested in applying for our job opening.</p>"
					+"<p>We received a large number of applications, and after carefully reviewing all of them, unfortunately, we have to inform you that this time we won’t be able to invite you to the next phase of our selection process.</p>"
					+"<p>Though your qualifications are impressive, we have decided to move forward with a candidate whose experiences better meet our needs for this particular role.</p>"
					+"<p>However, we really do appreciate your interest in our company. We hope you’ll keep us in mind and apply again in the future should you see a job opening for which you qualify.<br>If you have any questions or need additional information, please don’t hesitate to contact me by email <a href=\"mailto:hr@corpseed.com\" style=\"color:rgb(5,99,193)\" target=\"_blank\">hr@corpseed.com</a>.</p>"
					+"<p>We wish you success in your future endeavours.</p>"
					+"<p>Once again, thank you for your interest in working with us.</p>"
					+"<p>Regards,<br/><b>(HR Team)</b></p>Corpseed ITES Pvt. Ltd.<br/>A-154A, 2nd Floor, A Block, Sector 63, Noida, Uttar Pradesh - 201301<br/>+91-7558-640-644<br/>hello@corpseed.com</p>";
			this.emailSchedulerService.saveEmail(new EmailScheduler(0, jobApp.getEmail(),"empty", "Your Job Application To Corpseed", message, 2));
		}
	}

	@GetMapping("/reset/")
	@ResponseBody
	public void resetStatus(@RequestParam("uuid") String uuid,@RequestParam("status") String status,
			Principal principal){
		JobApplication jobApp=this.jobAppServices.getJobAppByUuid(uuid);
		if(jobApp!=null&&!jobApp.getApplicationStatus().equals(status)) {
			jobApp.setApplicationStatus(status);
			jobApp.setActionBy(this.commonServices.getLoginedUser(principal));
			this.jobAppServices.saveJobApplication(jobApp);	
			
			if(status.equals("4")) {
				TrackApplication trackApp = jobApp.getTrackApplication();
				trackApp.setStatus("3");
				this.trackAppService.saveTrackApplication(trackApp);
			}
		}
	}	
	
	@GetMapping("/update-status/")
	@ResponseBody
	public void updateJobStatus(@RequestParam("uuid") String uuid,@RequestParam("status") String status,
			Principal principal){
		JobApplication jobApp=this.jobAppServices.getJobAppByUuid(uuid);
		if(jobApp!=null&&!jobApp.getApplicationStatus().equals(status)) {
			jobApp.setApplicationStatus(status);
			jobApp.setActionBy(this.commonServices.getLoginedUser(principal));
			this.jobAppServices.saveJobApplication(jobApp);
			if(status.equals("4"))status="3";
			TrackApplication trackApp = jobApp.getTrackApplication();
			if(trackApp!=null) {				
				trackApp.setStatus(status);
				this.trackAppService.saveTrackApplication(trackApp);
			}
		}
	}
	
	@PostMapping("/interview-schedule")
	@ResponseBody
	public void scheduleInterview(@RequestParam("date") String date,@RequestParam("time") String time
			,@RequestParam("uuid") String uuid,@RequestParam("message") String message,
			@RequestParam("interviewerName") String interviewerName,@RequestParam("id") long id,
			@RequestParam("interviewMode") String interviewMode,PrintWriter pw,Principal principal){
				
		if(time!=null&&time.length()>0)
			time=this.commonServices.convertTo12Hours(time);
		
		if(message.contains("[Date]"))message = message.replace("[Date]", date);
		if(message.contains("[Time]"))message = message.replace("[Time]", time);
		if(message.contains("[Interviewer Name]"))message = message.replace("[Interviewer Name]", interviewerName);
		if(message.contains("[Mode]"))message = message.replace("[Mode]", interviewMode);
		JobApplication jobApp=this.jobAppServices.getJobAppByUuid(uuid);
		//getting interviewer
		User interviewer=this.userService.findByIdAndDeleteStatus(id, 2);
		if(jobApp!=null) {
			User user = this.commonServices.getLoginedUser(principal);
			if(!jobApp.getApplicationStatus().equals("1")) {
				jobApp.setApplicationStatus("1");
				jobApp.setActionBy(user);
				this.jobAppServices.saveJobApplication(jobApp);
			}
					
			//adding interview schedule
			InterviewSchedule scheduleInterview = this.interviewScheduleService.scheduleInterview(new InterviewSchedule(0, this.commonServices.getUUID(), date, time, message, jobApp));
//			System.out.println("interview scheduled....................");
			if(scheduleInterview!=null) {
				TrackApplication findTrackApp=this.trackAppService.findByJobApplication(jobApp);
				if(findTrackApp==null) {
//					System.out.println("Find track app is not null---------------------------");
					TrackApplication trackApp=new TrackApplication();	
					trackApp.setPostDate(this.commonServices.getToday());
					trackApp.setUser(interviewer);
					trackApp.setUuid(this.commonServices.getUUID());
					trackApp.setDate(date);
					trackApp.setTime(time);
					trackApp.setJobApplication(jobApp);
					trackApp.setStatus("1");
					trackApp.setAddedByUuid(user.getUuid());
					this.trackAppService.saveTrackApplication(trackApp);
				}else {
					findTrackApp.setPostDate(this.commonServices.getToday());					
					findTrackApp.setAddedByUuid(user.getUuid());
					findTrackApp.setDate(date);
					findTrackApp.setTime(time);
					findTrackApp.setUser(interviewer);
					this.trackAppService.saveTrackApplication(findTrackApp);
				}
				List<EmailScheduler> emailSch=new ArrayList<>();
				emailSch.add(new EmailScheduler(0, jobApp.getEmail(),"empty", "Invitation for Interview | Corpseed", message, 2));
				//sending email to interviewer
				message="<p>Hi <b>"+interviewer.getFirstName()+" "+interviewer.getLastName()+"</b></p><p>You interview for <b>"+jobApp.getJobs().getPosition()+"</b> with <b>"+jobApp.getName()+"</b> has been scheduled on <b>"+date+" "+time+"</b> in <b>"+interviewMode+"</b> mode.</p><p>Please be available for the interview or to reschedule you can reach us at <a href='mailto:hr@corpseed.com'>hr@corpseed.com</a></p><p>Click <a href='"+env.getProperty("domain.name")+"/admin/interviews/new'>here</a> to see aligned interviews.</p><p>Thanks,</p><p>Regard's</p><p>HR Team</p>";
				emailSch.add(new EmailScheduler(0, interviewer.getEmail(),"empty", "Interview scheduled(Technical Round)", message, 2));
				
				if(!emailSch.isEmpty())
					this.emailSchedulerService.saveAllEmail(emailSch);
				
				pw.write("pass");
			}else {	
				pw.write("fail");
			}
		}else {
			pw.write("fail");
		}
	}
	
	@PostMapping("/update/")
	public String updateJobApplication(@Valid @ModelAttribute("jobApplication") JobApplication jobApplication,BindingResult result,
			@RequestParam("file") MultipartFile file,Principal principal,HttpSession session,Model model) {
		try {	
			
			if(result.hasErrors()) {
				model.addAttribute("title", "Corpseed Dashboard | Edit Job-Application");
				model.addAttribute("appendClass", "hrm");
				model.addAttribute("jobs", this.jobService.getAllJobs());
				model.addAttribute("jobApplication", jobApplication);
				session.setAttribute("message", new Message("Please fill out the blank fields !!", "alert-danger"));
				return "admin/job-application-edit";
			}
			
				JobApplication jobApplicationExist=this.jobAppServices.findByEmailAndJobsAndUuidNotAndPostDateAfter(jobApplication.getEmail(),jobApplication.getJobs(),jobApplication.getUuid(),this.commonServices.dateBeforeDays(90));
				
				if(jobApplicationExist!=null) {
					model.addAttribute("title", "Corpseed Dashboard | Edit Job-Application");
					model.addAttribute("appendClass", "hrm");
					model.addAttribute("jobs", this.jobService.getAllJobs());
					model.addAttribute("jobApplication", jobApplication);
					if(jobApplicationExist.getDeleteStatus()==1)
						session.setAttribute("message", new Message("Job Application exist in Trash !!", "alert-danger"));
					else
						session.setAttribute("message", new Message("User already applied for this position, Please try after 3 months !!", "alert-danger"));
					
					return "admin/job-application-edit";
				}
					jobApplication.setAddedByUUID(this.commonServices.getLoginedUser(principal).getUuid());
					
						if(!file.isEmpty()) {							
							if(!jobApplication.getAttachedFile().equalsIgnoreCase("NA")&&jobApplication.getAttachedFile()!=null) {
								this.azureAdapter.deleteFile(jobApplication.getAttachedFile());
							}
							String name = azureAdapter.upload(file,this.commonServices.getUniqueCode());
							jobApplication.setAttachedFile(name);
						}	
						
						jobApplication.setUploadDocStatus("2");
						JobApplication saveJobApplication = this.jobAppServices.saveJobApplication(jobApplication);
						if(saveJobApplication==null) {
							model.addAttribute("title", "Corpseed Dashboard | Edit Job-Application");
							model.addAttribute("appendClass", "hrm");
							model.addAttribute("jobs", this.jobService.getAllJobs());
							model.addAttribute("jobApplication", jobApplication);
							session.setAttribute("message", new Message("Data Not Saved,Please try-again later !! ", "alert-danger"));
							return "admin/job-application-edit";
						}
						return "redirect:/hrm/job-application/";												
			
		} catch (Exception e) {
			e.printStackTrace();
			model.addAttribute("jobApplication", jobApplication);
			session.setAttribute("message", new Message("Error : " + e.getMessage(), "alert-danger"));
			return "admin/category-add";
		}
	}
	
	@PostMapping("/add")
	public String saveJobApplication(@Valid @ModelAttribute("jobApplication") JobApplication jobApplication,BindingResult result,
			@RequestParam("jobs") String jobId,@RequestParam("file") MultipartFile file,Principal principal,HttpSession session,Model model
			,HttpServletRequest req) {
		try {
			model.addAttribute("title", "Corpseed Dashboard | Add Jobs");
			model.addAttribute("appendClass", "hrm");
			model.addAttribute("jobs", this.jobService.getAllJobs());
			
			if(result.hasErrors()) {
				model.addAttribute("jobApplication", jobApplication);
				session.setAttribute("message", new Message("Please fill out the blank fields !!", "alert-danger"));
				return "admin/job-application-add";
			}
			
			Jobs jobs=this.jobService.getJobById(jobId);
			if(jobs==null) {
				model.addAttribute("jobApplication", jobApplication);
				session.setAttribute("message", new Message("Something Went wrong,Please try-again later !! ", "alert-danger"));
				return "admin/job-application-add";
			}
				JobApplication jobApplicationExist=this.jobAppServices.findByEmailAndJobsAndPostDateAfter(jobApplication.getEmail(),jobs,this.commonServices.dateBeforeDays(90));
				if(jobApplicationExist!=null) {
					String appStatus = jobApplicationExist.getApplicationStatus();
					String appPosition="";
					if(appStatus.equalsIgnoreCase("1"))appPosition="Selected";
					else if(appStatus.equalsIgnoreCase("2"))appPosition="Rejected";
					else if(appStatus.equalsIgnoreCase("3"))appPosition="New";
					else if(appStatus.equalsIgnoreCase("4"))appPosition="Hold";
					
					model.addAttribute("jobApplication", jobApplication);
					if(jobApplicationExist.getDeleteStatus()==1)
						session.setAttribute("message", new Message("Job Application exist in Trash !!", "alert-danger"));
					else
						session.setAttribute("message", new Message("User already applied for this position, Please try after 3 months !!\n "+jobApplicationExist.getName()+" : "+appPosition, "alert-danger"));
					
					return "admin/job-application-add";
				}
				String message="<p>Dear "+jobApplication.getName()+"</p></p>Thank you for showing interest in working with Corpseed.</p><p>Please login to see application status.</p><p>Login link : <a href='"+env.getProperty("domain.name")+"/login'>www.corpseed.com</p><p>Username : "+jobApplication.getEmail()+"</p><p>If you have any questions or need additional information, please don’t hesitate to contact me by email <a href='mailto:hr@corpseed.com'>hr@corpseed.com</a></p><br/><p>Thanks & Regards</p><p>HR Team</p>";
				
				User user = this.userService.isUserExist(jobApplication.getEmail());
				
				if(user==null) {
					//creating candidate's user account
					String uuid=this.commonServices.getUUID();
					String password=this.commonServices.getUUID().substring(0,7);
					
					String x[]=jobApplication.getName().split(" ");					
					String firstName=x[0];
					String lastName=".";
					
					String jobTitle= jobs.getJobTitle();
//					System.out.println("jobtitle=="+jobTitle);
					if(x.length>1)
						lastName=jobApplication.getName().substring(firstName.length()+1);
					
					user = this.userService.saveUser(new User(0, uuid, firstName, lastName, jobApplication.getEmail(), this.passwordEncoder.encode(password),jobTitle, "profile.png", jobTitle, "1", "0", this.commonServices.getIpAddress(req), this.commonServices.getToday(),jobTitle,null, "ROLE_USER",2,null,null,null,null,1,"NA",firstName+" "+lastName,firstName+" "+lastName,firstName+" "+lastName));
					message="<p>Dear "+jobApplication.getName()+"</p></p>Thank you for showing interest in working with Corpseed.</p><p>Please login to see application status.</p><p>Login link : <a href='"+env.getProperty("domain.name")+"/login'>www.corpseed.com</p><p>Username : "+jobApplication.getEmail()+"</p><p>Password : "+password+"</p><p>If you have any questions or need additional information, please don’t hesitate to contact me by email <a href='mailto:hr@corpseed.com'>hr@corpseed.com</a></p><br/><p>Thanks & Regards</p><p>HR Team</p>";
					this.emailSchedulerService.saveEmail(new EmailScheduler(0, jobApplication.getEmail(),"empty", "Corpseed | Login Credential", message, 2));
				}
				
					jobApplication.setUser(user);
					jobApplication.setUuid(this.commonServices.getUUID());
					jobApplication.setAddedByUUID(this.commonServices.getLoginedUser(principal).getUuid());
					jobApplication.setJobs(jobs);
					jobApplication.setUploadDocStatus("2");
					
					if(file.isEmpty()) {
						model.addAttribute("jobApplication", jobApplication);
						session.setAttribute("message", new Message("Please attach resume !!", "alert-danger"));
						return "admin/job-application-add";
					}else {
						String name = azureAdapter.upload(file,this.commonServices.getUniqueCode());
						jobApplication.setAttachedFile(name);						
										
					jobApplication.setUploadDocStatus("2");
					jobApplication.setUploadDocEmail("2");
					JobApplication saveJobApplication = this.jobAppServices.saveJobApplication(jobApplication);
					if(saveJobApplication==null) {
						model.addAttribute("jobApplication", jobApplication);
						session.setAttribute("message", new Message("Data Not Saved,Please try-again later !! ", "alert-danger"));
						return "admin/job-application-add";
					}
					return "redirect:/hrm/job-application/";
					
				}
		} catch (Exception e) {
			e.printStackTrace();
			model.addAttribute("jobApplication", jobApplication);
			session.setAttribute("message", new Message("Error : " + e.getMessage(), "alert-danger"));
			return "admin/job-application-add";
		}
	}
}
