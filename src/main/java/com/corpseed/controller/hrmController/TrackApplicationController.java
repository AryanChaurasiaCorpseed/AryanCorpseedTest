package com.corpseed.controller.hrmController;

import java.io.PrintWriter;
import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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
import com.corpseed.entity.documententity.DocumentVerification;
import com.corpseed.entity.schedulerentity.EmailScheduler;
import com.corpseed.entity.hrmentity.HrAndTechnical;
import com.corpseed.entity.hrmentity.HrScreening;
import com.corpseed.entity.hrmentity.JobApplication;
import com.corpseed.entity.hrmentity.JobReport;
import com.corpseed.entity.hrmentity.Jobs;
import com.corpseed.entity.hrmentity.OfferLetter;
import com.corpseed.entity.hrmentity.TechnicalRound;
import com.corpseed.entity.hrmentity.TrackApplication;
import com.corpseed.entity.User;
import com.corpseed.util.EmailSender;
import com.corpseed.util.Message;
import com.corpseed.serviceImpl.AzureBlobAdapter;
import com.corpseed.serviceImpl.CandidateDocumentService;
import com.corpseed.serviceImpl.CommonServices;
import com.corpseed.serviceImpl.hrmserviceimpl.JobApplicationServices;
import com.corpseed.serviceImpl.hrmserviceimpl.JobReportService;
import com.corpseed.serviceImpl.hrmserviceimpl.JobServices;
import com.corpseed.serviceImpl.hrmserviceimpl.TrackAppService;
import com.corpseed.serviceImpl.UserServices;
import com.corpseed.service.EmailSchedulerService;
import com.corpseed.service.hrmservice.InterviewPermissionService;

@Controller
@RequestMapping("/hrm/track-application")
public class TrackApplicationController {

	@Autowired
	private TrackAppService trackAppService;
	
	@Autowired
	private UserServices userService;
	
	@Autowired
	private CommonServices commonServices;
	
	@Autowired
    AzureBlobAdapter azureAdapter;
	
	@Autowired
	private EmailSender emailSender;
		
	@Autowired
	private JobApplicationServices jobApplicationService;
	
	@Autowired
	private CandidateDocumentService candidateDocumentService;
	
	@Autowired
	private JobReportService jobReportService;
	
	@Autowired
	private JobServices jobService;
	
	@Autowired
	private Environment env;
	
	@Autowired
	private EmailSchedulerService emailSchedulerService;
	
	@Autowired
	private InterviewPermissionService interviewPermService;
	
	@GetMapping("/")
	public String trackApplication(@RequestParam("page") Optional<Integer> page, 
		      @RequestParam("size") Optional<Integer> size,Model model,Principal principal) {
		
		int currentPage = page.orElse(1);
        int pageSize = size.orElse(10);
        
		model.addAttribute("title", "Corpseed Dashboard | Track Application");
		model.addAttribute("appendClass", "hrm");
        Pageable pageable=PageRequest.of((currentPage-1), pageSize);
        Page<TrackApplication> trackApplication=null;
   
		String role=this.commonServices.getLoginedUser(principal).getRole();
		if(role.equals("ROLE_ADMIN")||role.equals("ROLE_HR_MANAGER")) {			
			
			trackApplication = this.trackAppService.findByOrderByIdDesc(pageable);
			
		}else if(role.equals("ROLE_HR_EXECUTIVE")){			
			
			trackApplication= this.trackAppService.findByAddedByUuidOrderByIdDesc(this.commonServices.getLoginedUser(principal).getUuid(),pageable);
			
		}
		model.addAttribute("cpage", currentPage);
        model.addAttribute("candidates", trackApplication);
		
        int totalPages = trackApplication.getTotalPages();
        model.addAttribute("totalRecords", trackApplication.getTotalElements());
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
		model.addAttribute("candidates",trackApplication);
		return "admin/track-application";
	}
	
	@PostMapping("/filter")
	public String trackApplicationFilter(@RequestParam("status") String status,@RequestParam("department") String department,
			@RequestParam("round") String round,@RequestParam("page") Optional<Integer> page, 
		      @RequestParam("size") Optional<Integer> size,Model model,Principal principal) {
		try {		
			int currentPage = page.orElse(1);
	        int pageSize = size.orElse(10);
	       
	        model.addAttribute("title", "Corpseed Dashboard | Track Application");
			model.addAttribute("appendClass", "hrm");
	        
	        Pageable pageable=PageRequest.of((currentPage-1), pageSize);
	    	        
	        Page<TrackApplication> trackApplication=null;
			String role=this.commonServices.getLoginedUser(principal).getRole();
			if(role.equals("ROLE_ADMIN")||role.equals("ROLE_HR_MANAGER")) {

				boolean result = department != null && department.length() > 0 && round != null && round.length() > 0;
				if(result) {
					if(round.equals("1"))
					trackApplication=this.trackAppService.findByStatusAndDepartmentAndRoundOne(department,status,pageable);
					else if(round.equals("2"))
						trackApplication=this.trackAppService.findByStatusAndDepartmentAndRoundSecond(department,status,pageable);
					else if(round.equals("3"))
						trackApplication=this.trackAppService.findByStatusAndDepartmentAndRoundThird(department,status,pageable);
					else if(round.equals("4"))
						trackApplication=this.trackAppService.findByStatusAndDepartmentAndRoundFourth(department,status,pageable);
					
				}else if(department!=null&&department.length()>0) {
					trackApplication=this.trackAppService.findByStatusAndDepartment(department,status,pageable);
				}else if(round!=null&&round.length()>0) {
					if(round.equals("1"))
						trackApplication=this.trackAppService.findByStatusAndRoundOne(status,pageable);
					else if(round.equals("2"))
						trackApplication=this.trackAppService.findByStatusAndRoundSecond(status,pageable);
					else if(round.equals("3"))
						trackApplication=this.trackAppService.findByStatusAndRoundThird(status,pageable);
					else if(round.equals("4"))
						trackApplication=this.trackAppService.findByStatusAndRoundFourth(status,pageable);
				}else
					trackApplication = this.trackAppService.findByStatusOrderByIdDesc(status,pageable);
							
			
			}else if(role.equals("ROLE_HR_EXECUTIVE")) {
				String addedByUuid=this.commonServices.getLoginedUser(principal).getUuid();
				boolean result = department != null && department.length() > 0 && round != null && round.length() > 0;
				if(result) {
					if(round.equals("1"))
					trackApplication=this.trackAppService.findByStatusAndDepartmentAndRoundOne(department,status,addedByUuid,pageable);
					if(round.equals("2"))
						trackApplication=this.trackAppService.findByStatusAndDepartmentAndRoundSecond(department,status,addedByUuid,pageable);
					else if(round.equals("3"))
						trackApplication=this.trackAppService.findByStatusAndDepartmentAndRoundThird(department,status,addedByUuid,pageable);
					else if(round.equals("4"))
						trackApplication=this.trackAppService.findByStatusAndDepartmentAndRoundFourth(department,status,addedByUuid,pageable);
					
				}else if(department!=null&&department.length()>0) {
					trackApplication=this.trackAppService.findByStatusAndDepartment(department,status,addedByUuid,pageable);
				}else if(round!=null&&round.length()>0) {
					if(round.equals("1"))
						trackApplication=this.trackAppService.findByStatusAndRoundOne(status,addedByUuid,pageable);
					else if(round.equals("2"))
						trackApplication=this.trackAppService.findByStatusAndRoundSecond(status,addedByUuid,pageable);
					else if(round.equals("3"))
						trackApplication=this.trackAppService.findByStatusAndRoundThird(status,addedByUuid,pageable);
					else if(round.equals("4"))
						trackApplication=this.trackAppService.findByStatusAndRoundFourth(status,addedByUuid,pageable);
				}else
					trackApplication = this.trackAppService.findByAddedByUuidAndStatus(addedByUuid,status,pageable);
			}    
	       			
			model.addAttribute("searchStatus", status);
			model.addAttribute("searchDepartment", department);
			model.addAttribute("searchRound", round);
	        model.addAttribute("cpage", currentPage);
	        model.addAttribute("candidates", trackApplication);
			
	        int totalPages = trackApplication.getTotalPages();
	        model.addAttribute("totalRecords", trackApplication.getTotalElements());
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
	        			
		} catch (Exception e) {e.getMessage();
			e.printStackTrace();
		}
		return "admin/track-application";
	}
	@GetMapping("/filter")
	public String trackApplicationNoFilter(Model model) {
		return "redirect:/hrm/track-application/";
	}
	
	@GetMapping("/business-round/{uuid}")
	public String hrScreening(@PathVariable("uuid") String uuid,Model model) {
		TrackApplication trackApp=this.trackAppService.findByUuid(uuid);
		HrScreening hrScreening=this.trackAppService.findHrScreeningByTrackApplication(trackApp);
		
		model.addAttribute("users", this.userService.findByRole("ROLE_HR_MANAGER"));
		model.addAttribute("appendClass", "hrm");
		model.addAttribute("title", "Corpseed Dashboard | Hr Screening");		
		if(hrScreening==null) {
			HrScreening newHrScreening=new HrScreening();
			newHrScreening.setTrackApplication(trackApp);
			newHrScreening.setUuid(this.commonServices.getUUID());
			model.addAttribute("hrScreening",newHrScreening);
		}else {
			model.addAttribute("hrScreening",hrScreening);
		}
		
		return "admin/hr-screening";
	}
	@PostMapping("/business-round/update")
	public String updateHrScreening(@Valid @ModelAttribute("hrScreening") HrScreening hrScreening,BindingResult result,
			Model model,HttpSession session,Principal principal) {
		
		try {		
		
			if(result.hasErrors()) {
				model.addAttribute("appendClass", "hrm");
				model.addAttribute("title", "Corpseed Dashboard | Hr Screening");
				model.addAttribute("hrScreening", hrScreening);
				model.addAttribute("users", this.userService.findByRole("ROLE_HR_MANAGER"));
				session.setAttribute("message", new Message("Please fill out the blank fields !!", "alert-danger"));
				return "admin/hr-screening";
			}		
			User role_user=this.commonServices.getLoginedUser(principal);
			hrScreening.setAddedByUuid(role_user.getUuid());
			HrScreening saveHrScreening = this.trackAppService.saveHrScreening(hrScreening);		

				if(saveHrScreening.getStatus().equals("1")&&saveHrScreening.getTrackApplication().getStatus().equals("2")) {
					TrackApplication trackApp=saveHrScreening.getTrackApplication();
					trackApp.setStatus("1");					
					this.trackAppService.saveTrackApplication(trackApp);
				}else if(saveHrScreening.getStatus().equals("2")&&saveHrScreening.getTrackApplication().getStatus().equals("1")) {
					TrackApplication trackApp=saveHrScreening.getTrackApplication();
					trackApp.setStatus("2");
					this.trackAppService.saveTrackApplication(trackApp);
				}
				
				String role=role_user.getRole();
				if(saveHrScreening.getStatus().equals("1")&&(role.contains("HR")||role.contains("ADMIN"))) {
					//getting interviewer details
					User user=saveHrScreening.getUser();
					if(user!=null) {
						//sending email for interview
						String message1="<p>Dear <b>"+saveHrScreening.getTrackApplication().getJobApplication().getName()+"</b></p>"
						+"<p>Thank you for taking the time to discuss the <b>"+saveHrScreening.getTrackApplication().getJobApplication().getJobs().getPosition()+"</b> role with us at "
						+"your interview on <b>"+saveHrScreening.getTrackApplication().getTechnicalRound().getDateTime()+"</b>. We enjoyed getting to know "
						+"you and learning more about your skills. We would like to invite you back for "
						+"a third interview / We are glad to inform you that we have shortlisted you for "
						+"the final round. </p>"
						+"<p>This interview would be with <b>"+user.getFirstName()+" "+user.getLastName()+"</b>. Please find the Details below: </p>"
						+"<p><b>Date-Time :</b> "+saveHrScreening.getDateTime()+"</p>"
						+"<p><b>Mode :</b> "+saveHrScreening.getInterviewMode()+"</p>"
						+"<p>Address : A-154A, 2nd Floor, A Block, Sector 63, Noida, Uttar Pradesh - 201301</p>"
						+"<p>Please visit <a href='https://www.corpseed.com/'>www.corpseed.com</a> to know more about Corpseed.</p>";
						List<EmailScheduler> emailScheduler=new ArrayList<>();
						emailScheduler.add(new EmailScheduler(0, saveHrScreening.getTrackApplication().getJobApplication().getEmail(),"empty", "Congratulations | Next Round Interview", message1, 2));
						//sending email to interviewer
						String message="<p>Hi <b>"+user.getFirstName()+" "+user.getLastName()+"</b></p><p> Interview for <b>"+saveHrScreening.getTrackApplication().getJobApplication().getJobs().getPosition()+"</b> with <b>"+saveHrScreening.getTrackApplication().getJobApplication().getName()+"</b> has been scheduled on <b>"+saveHrScreening.getDateTime()+"</b> in <b>"+saveHrScreening.getInterviewMode()+"</b> mode.</p><p>Please be available for the interview or to reschedule you can reach us at <a href='mailto:hr@corpseed.com'>hr@corpseed.com</a></p><p>Click <a href='"+env.getProperty("domain.name")+"/admin/interviews/new'>here</a> to see aligned interviews.</p><p>Thanks,</p><p>Regard's</p><p>HR Team</p>";
						emailScheduler.add(new EmailScheduler(0,user.getEmail(),"empty", "Interview scheduled(HR Manager Round)", message, 2));
						
						if(!emailScheduler.isEmpty())
						this.emailSchedulerService.saveAllEmail(emailScheduler);
					}					
				}				
				return "redirect:/hrm/track-application/";			
			
		} catch (Exception e) {
			e.printStackTrace();
			model.addAttribute("appendClass", "hrm");
			model.addAttribute("title", "Corpseed Dashboard | Hr Screening");
			model.addAttribute("hrScreening", hrScreening);
			model.addAttribute("users", this.userService.findByRole("ROLE_HR_MANAGER"));
			session.setAttribute("message", new Message("Error : " + e.getMessage(), "alert-danger"));
			return "admin/hr-screening";
		}
	}
	
	@GetMapping("/hr-technical/{uuid}")
	public String hrTechnical(@PathVariable("uuid") String uuid,Model model) {		
		TrackApplication trackApp=this.trackAppService.findByUuid(uuid);
		HrAndTechnical hrTechnical=this.trackAppService.findHrTechnicalByTrackApplication(trackApp);
		model.addAttribute("appendClass", "hrm");
		if(hrTechnical==null) {
			HrAndTechnical newHrTechnical=new HrAndTechnical();
			newHrTechnical.setTrackApplication(trackApp);
			newHrTechnical.setUuid(this.commonServices.getUUID());
			model.addAttribute("hrTechnical",newHrTechnical);
		}else {
			model.addAttribute("hrTechnical",hrTechnical);
		}
		return "admin/hr-technical";
	}
	
	@PostMapping("/hr-technical/update")
	public String updateHrTechnical(@Valid @ModelAttribute("hrTechnical") HrAndTechnical hrTechnical,BindingResult result,
			Model model,HttpSession session,Principal principal) {
		try {
			if(result.hasErrors()) {
				model.addAttribute("appendClass", "hrm");
				model.addAttribute("title", "Corpseed Dashboard | Hr & Technical");
				model.addAttribute("hrTechnical", hrTechnical);
				session.setAttribute("message", new Message("Please fill out the blank fields !!", "alert-danger"));
				return "admin/hr-technical";
			}	
			long id=hrTechnical.getId();
			TrackApplication trackAppl=hrTechnical.getTrackApplication();
			HrAndTechnical existhrTechnical=this.trackAppService.findHrTechnicalByTrackApplication(trackAppl);
			if(existhrTechnical!=null)hrTechnical.setId(existhrTechnical.getId());
			
			HrAndTechnical saveHrTechnical = this.trackAppService.saveHrTechnical(hrTechnical);			
			
			if(saveHrTechnical==null) {
				model.addAttribute("appendClass", "hrm");
				model.addAttribute("title", "Corpseed Dashboard | Hr & Technical");
				model.addAttribute("hrTechnical", hrTechnical);
				session.setAttribute("message", new Message("Data Not Saved, Please contact to admin !! ", "alert-danger"));
				return "admin/hr-technical";
			}
				if(saveHrTechnical.getStatus().equals("1")&&saveHrTechnical.getTrackApplication().getStatus().equals("2")) {
					TrackApplication trackApp=saveHrTechnical.getTrackApplication();
					trackApp.setStatus("1");
					this.trackAppService.saveTrackApplication(trackApp);
				}else if(saveHrTechnical.getStatus().equals("2")&&saveHrTechnical.getTrackApplication().getStatus().equals("1")) {
					TrackApplication trackApp=saveHrTechnical.getTrackApplication();
					trackApp.setStatus("2");
					this.trackAppService.saveTrackApplication(trackApp);
				}
				if(saveHrTechnical.getStatus().equals("2")) {
					String message="<p>Dear <b>"+saveHrTechnical.getTrackApplication().getJobApplication().getName()+"</b>,</p>"+
							"<p>It was a pleasure to meet you at your interview earlier this week. We were impressed with your experience, "
							+"professionalism, and approach, but we have chosen to go with another candidate.</p>"
							+"<p>I know you will be disappointed with this news. The interview panel enjoyed your presentation and valued your insights into our strategy.</p>"
							+"<p>We are undergoing a transformation and are seeking individuals with strong background. I am sure that your knowledge, experience, and qualifications will help you find a suitable position to achieve your goals.</p>"+
							"<p>Sincerely,</p><p>HR Team</p>";
					this.emailSchedulerService.saveEmail(new EmailScheduler(0, saveHrTechnical.getTrackApplication().getJobApplication().getEmail(),"empty", "Notification | Business Round", message, 2));
					
				}else if(hrTechnical.getStatus().equals("1")&&id==0) {
					//checking user already exist or not
					User findUser=hrTechnical.getTrackApplication().getJobApplication().getUser();
					JobApplication jobApp=saveHrTechnical.getTrackApplication().getJobApplication();
					//creating document list
					List<CandidateDocuments> docList=new ArrayList<>();
					String today=this.commonServices.getToday();
					docList.add(new CandidateDocuments(0, this.commonServices.getUUID(), "Passport Size Photo", "NA", today, findUser,jobApp));
					docList.add(new CandidateDocuments(0, this.commonServices.getUUID(), "Aaddhar Card", "NA", today, findUser,jobApp));
					docList.add(new CandidateDocuments(0, this.commonServices.getUUID(), "Pan card", "NA", today, findUser,jobApp));
					docList.add(new CandidateDocuments(0, this.commonServices.getUUID(), "Qualification Certificate", "NA", today, findUser,jobApp));
					docList.add(new CandidateDocuments(0, this.commonServices.getUUID(), "Professional Certificate", "NA", today, findUser,jobApp));
					docList.add(new CandidateDocuments(0, this.commonServices.getUUID(), "Previous company's Appointment Letter", "NA", today, findUser,jobApp));
					docList.add(new CandidateDocuments(0, this.commonServices.getUUID(), "Previous company's Appraisal letter", "NA", today, findUser,jobApp));
					docList.add(new CandidateDocuments(0, this.commonServices.getUUID(), "Previous company's Salary Slip", "NA", today, findUser,jobApp));
					docList.add(new CandidateDocuments(0, this.commonServices.getUUID(), "Previous company's Bank Statement", "NA", today, findUser,jobApp));
					docList.add(new CandidateDocuments(0, this.commonServices.getUUID(), "Previous company's Experience letter", "NA", today, findUser,jobApp));
					docList.add(new CandidateDocuments(0, this.commonServices.getUUID(), "Previous company's Relieving letter", "NA", today, findUser,jobApp));
					docList.add(new CandidateDocuments(0, this.commonServices.getUUID(), "Cancelled Cheque", "NA", today, findUser,jobApp));
					docList.add(new CandidateDocuments(0, this.commonServices.getUUID(), "Background Verification Sheet", "NA", today, findUser,jobApp));
					
					this.candidateDocumentService.saveDocuments(docList);
					
					//sending to candidate
					String message="<p>Hi <b>"+saveHrTechnical.getTrackApplication().getJobApplication().getName()+"</b></p>"
							+"<p>Greetings for the day.</p>"
							+"<p>We are pleased to offer you the position of <b>"+saveHrTechnical.getTrackApplication().getJobApplication().getJobs().getPosition()+".</b></p>"
							+"<p>Please note that upload all the documents on the portal as per the given list:</p>"
							+"<p>Login link : <a href='"+env.getProperty("domain.name")+"/login'>www.corpseed.com</p><p>Username : "+saveHrTechnical.getTrackApplication().getJobApplication().getEmail()+"</p>"
							+"<p>If Password not remember then click on login link and forget password.</p>"
							+"<p>If you have any questions or need additional information, please don’t hesitate to contact me by email <a href='mailto:hr@corpseed.com'>hr@corpseed.com</a></p><br/><p>Thanks & Regards</p><p>HR Team</p>";
					this.emailSchedulerService.saveEmail(new EmailScheduler(0, saveHrTechnical.getTrackApplication().getJobApplication().getEmail(),"empty", "Corpseed | Share documents", message, 2));
				}
				//update job application upload document status on 
				JobApplication jobApp=saveHrTechnical.getTrackApplication().getJobApplication();
				jobApp.setUploadDocStatus("1");
				this.jobApplicationService.saveJobApplication(jobApp);
				return "redirect:/hrm/track-application/";
			
			
		} catch (Exception e) {
			model.addAttribute("appendClass", "hrm");
			model.addAttribute("title", "Corpseed Dashboard | Hr & Technical");
			model.addAttribute("hrTechnical", hrTechnical);
			session.setAttribute("message", new Message("Error : " + e.getMessage(), "alert-danger"));
			return "admin/hr-technical";
		}
	}
	
	@GetMapping("/technical-round/{uuid}")
	public String technicalRound(@PathVariable("uuid") String uuid,Model model) {		
		TrackApplication trackApp=this.trackAppService.findByUuid(uuid);
		TechnicalRound technicalRound=trackApp.getTechnicalRound();
		model.addAttribute("appendClass", "hrm");
		if(technicalRound==null) {
			TechnicalRound newTechnicalRound=new TechnicalRound();
			newTechnicalRound.setUuid(this.commonServices.getUUID());
			newTechnicalRound.setTrackApplication(trackApp);
			model.addAttribute("technicalRound",newTechnicalRound);
		}else {
			model.addAttribute("technicalRound",technicalRound);
		}
		List<String> roles=new ArrayList<>();
		roles.add("ROLE_ADMIN");
		roles.add("ROLE_USER");
		model.addAttribute("interviewPermission", this.interviewPermService.findByDepartment(trackApp.getJobApplication().getJobs().getJobTitle()));
		return "admin/technical-round";
	}
	
	@PostMapping("/technical-round/update")
	public String updateTechnicalRound(@Valid @ModelAttribute("technicalRound") TechnicalRound technicalRound,
			BindingResult result,Model model,HttpSession session,Principal principal) {
		List<String> roles=new ArrayList<>();
		roles.add("ROLE_ADMIN");
		roles.add("ROLE_USER");
		
		try {				
			if(result.hasErrors()) {
				model.addAttribute("appendClass", "hrm");
				model.addAttribute("title", "Corpseed Dashboard | Technical Round");
				model.addAttribute("technicalRound", technicalRound);
				model.addAttribute("interviewPermission", this.interviewPermService.findByDepartment(technicalRound.getTrackApplication().getJobApplication().getJobs().getJobTitle()));
				session.setAttribute("message", new Message("Please fill out the blank fields !!", "alert-danger"));
				return "admin/technical-round";
			}		
			technicalRound.setAddedByUuid(this.commonServices.getLoginedUser(principal).getUuid());
			TechnicalRound saveTechnicalRound = this.trackAppService.saveTechnicalRound(technicalRound);			
			
			if(saveTechnicalRound==null) {
				model.addAttribute("appendClass", "hrm");
				model.addAttribute("title", "Corpseed Dashboard | Technical Round");
				model.addAttribute("technicalRound", technicalRound);
				model.addAttribute("interviewPermission", this.interviewPermService.findByDepartment(technicalRound.getTrackApplication().getJobApplication().getJobs().getJobTitle()));
				session.setAttribute("message", new Message("Data Not Saved, Please contact to admin !! ", "alert-danger"));
				return "admin/technical-round";
			}
			if(saveTechnicalRound.getStatus().equals("1")&&saveTechnicalRound.getTrackApplication().getStatus().equals("2")) {
				TrackApplication trackApp=saveTechnicalRound.getTrackApplication();
				trackApp.setStatus("1");
				this.trackAppService.saveTrackApplication(trackApp);
			}else if(saveTechnicalRound.getStatus().equals("2")&&saveTechnicalRound.getTrackApplication().getStatus().equals("1")) {
				TrackApplication trackApp=saveTechnicalRound.getTrackApplication();
				trackApp.setStatus("2");
				this.trackAppService.saveTrackApplication(trackApp);
			}
			User loginedUser = this.commonServices.getLoginedUser(principal);
			if(loginedUser.getRole().contains("HR")||loginedUser.getRole().contains("ADMIN")) {
			List<EmailScheduler> emailScheduler=new ArrayList<>();
			if(saveTechnicalRound.getStatus().equals("2")) {
				String message="<p>Dear <b>"+saveTechnicalRound.getTrackApplication().getJobApplication().getName()+"</b>,</p>"+
				"<p>It was a pleasure to meet you at your interview earlier this week. We were impressed with your experience, "
				+"professionalism, and approach, but we have chosen to go with another candidate.</p>"
				+"<p>I know you will be disappointed with this news. The interview panel enjoyed your presentation and valued your insights into our strategy.</p>"
				+"<p>We are undergoing a transformation and are seeking individuals with strong background. I am sure that your knowledge, experience, and qualifications will help you find a suitable position to achieve your goals.</p>"+
				"<p>Sincerely,</p><p>HR Team</p>";				
				emailScheduler.add(new EmailScheduler(0, saveTechnicalRound.getTrackApplication().getJobApplication().getEmail(),"empty", "Notification | Technical round", message, 2));
			}else if(saveTechnicalRound.getStatus().equals("1")) {
				User user=saveTechnicalRound.getUser();
				String message1="<p>Dear <b>"+saveTechnicalRound.getTrackApplication().getJobApplication().getName()+"</b>,</p>"
						+"<p>Thank you for taking the time to discuss the <b>"+saveTechnicalRound.getTrackApplication().getJobApplication().getJobs().getPosition()+"</b> role with us at "
						+"your interview on <b>"+saveTechnicalRound.getTrackApplication().getDate()+" "+saveTechnicalRound.getTrackApplication().getTime()+"</b>. We enjoyed getting to know "
						+"you and learning more about your skills. We would like to invite you back for "
						+"a second interview/We are glad to inform you that we have shortlisted you for "
						+"the second round. </p>"
						+"<p>This interview would be with <b>"+user.getFirstName()+" "+user.getLastName()+"</b>. Please find the Details below: </p>"
						+"<p><b>Date-Time :</b> "+saveTechnicalRound.getDateTime()+"</p>"
						+"<p><b>Mode :</b> "+saveTechnicalRound.getInterviewMode()+"</p>"
						+"<p>Address : A-154A, 2nd Floor, A Block, Sector 63, Noida, Uttar Pradesh - 201301</p>"
						+"<p>Please visit <a href='https://www.corpseed.com/'>www.corpseed.com</a> to know more about Corpseed.</p>";
				emailScheduler.add(new EmailScheduler(0, saveTechnicalRound.getTrackApplication().getJobApplication().getEmail(),"empty", "Congratulations | Next Round Interview", message1, 2));
				//sending email to interviewer
				String message="<p>Hi <b>"+user.getFirstName()+" "+user.getLastName()+"</b></p><p>You interview for <b>"+saveTechnicalRound.getTrackApplication().getJobApplication().getJobs().getPosition()+"</b> with <b>"+saveTechnicalRound.getTrackApplication().getJobApplication().getName()+"</b> has been scheduled on <b>"+saveTechnicalRound.getDateTime()+"</b> in <b>"+saveTechnicalRound.getInterviewMode()+"</b> mode.</p><p>Please be available for the interview or to reschedule you can reach us at <a href='mailto:hr@corpseed.com'>hr@corpseed.com</a></p><p>Click <a href='"+env.getProperty("domain.name")+"/admin/interviews/new'>here</a> to see aligned interviews.</p><p>Thanks,</p><p>Regard's</p><p>HR Team</p>";
				emailScheduler.add(new EmailScheduler(0, user.getEmail(),"empty", "Interview scheduled (Business Round)", message, 2));				
			}			
			if(!emailScheduler.isEmpty())
				this.emailSchedulerService.saveAllEmail(emailScheduler);
			}
			return "redirect:/hrm/track-application/";
					
		} catch (Exception e) {
			model.addAttribute("appendClass", "hrm");
			model.addAttribute("title", "Corpseed Dashboard | Technical Round");
			model.addAttribute("technicalRound", technicalRound);
			model.addAttribute("interviewPermission", this.interviewPermService.findByDepartment(technicalRound.getTrackApplication().getJobApplication().getJobs().getJobTitle()));
			session.setAttribute("message", new Message("Error : " + e.getMessage(), "alert-danger"));
			return "admin/technical-round";
		}
	}
	
	@GetMapping("/document-verification/{uuid}")
	public String documentVerification(@PathVariable("uuid") String uuid,Model model) {		
		TrackApplication trackApp=this.trackAppService.findByUuid(uuid);
		DocumentVerification docVerification=this.trackAppService.findDocumentVerificationByTrackApplication(trackApp);
		model.addAttribute("appendClass", "hrm"); 
		
		User user=trackApp.getJobApplication().getUser();
		List<CandidateDocuments> findByUserAndJobApplication =user.getDocList();
		model.addAttribute("documents", findByUserAndJobApplication);
		
		if(docVerification==null) {
			DocumentVerification newDocVerification=new DocumentVerification();
			newDocVerification.setUuid(this.commonServices.getUUID());
			newDocVerification.setTrackApplication(trackApp);
			model.addAttribute("docVerification",newDocVerification);
		}else {
			model.addAttribute("docVerification",docVerification);
		}
		return "admin/doc-verification";
	}
	
	@PostMapping("/document-verification/update")
	public String updatedocumentVerification(@Valid @ModelAttribute("docVerification") DocumentVerification docVerification,BindingResult result,
			Model model,HttpSession session) {
		try {
			if(result.hasErrors()) {
				model.addAttribute("appendClass", "hrm");
				model.addAttribute("title", "Corpseed Dashboard | Document verification");
				model.addAttribute("docVerification", docVerification); 
				session.setAttribute("message", new Message("Please fill out the blank fields !!", "alert-danger"));
				return "admin/doc-verification";
			}
			TrackApplication trackAppl=docVerification.getTrackApplication();
			DocumentVerification existDocVerification=this.trackAppService.findDocumentVerificationByTrackApplication(trackAppl);
			if(existDocVerification!=null)docVerification.setId(existDocVerification.getId());
			
			DocumentVerification saveDocVerification = this.trackAppService.saveDocumentVerification(docVerification);			
			
			if(saveDocVerification==null) {
				model.addAttribute("appendClass", "hrm");
				model.addAttribute("title", "Corpseed Dashboard | Document verification");
				model.addAttribute("docVerification", docVerification);
				session.setAttribute("message", new Message("Data Not Saved,Please contact to admin !! ", "alert-danger"));
				return "admin/doc-verification";
			}
			
			HrAndTechnical hrAndTech=saveDocVerification.getTrackApplication().getHrAndTechnical();
			hrAndTech.setRoundStatus(1);
			this.trackAppService.saveHrTechnical(hrAndTech);
			
			if(saveDocVerification.getStatus().equals("1")&&saveDocVerification.getTrackApplication().getStatus().equals("2")) {
				TrackApplication trackApp=saveDocVerification.getTrackApplication();
				trackApp.setStatus("1");
				this.trackAppService.saveTrackApplication(trackApp);
			}else if(saveDocVerification.getStatus().equals("2")&&saveDocVerification.getTrackApplication().getStatus().equals("1")) {
				TrackApplication trackApp=saveDocVerification.getTrackApplication();
				trackApp.setStatus("2");
				this.trackAppService.saveTrackApplication(trackApp);
			}	
			if(saveDocVerification.getStatus().equals("2")) {
				String message="<p>Dear <b>"+saveDocVerification.getTrackApplication().getJobApplication().getName()+"</b>,</p>"+
						"<p>It was a pleasure to meet you at your interview earlier this week. We were impressed with your experience, "
						+"professionalism, and approach, but we have chosen to go with another candidate.</p>"
						+"<p>I know you will be disappointed with this news. The interview panel enjoyed your presentation and valued your insights into our strategy.</p>"
						+"<p>We are undergoing a transformation and are seeking individuals with strong background. I am sure that your knowledge, experience, and qualifications will help you find a suitable position to achieve your goals.</p>"+
						"<p>Sincerely,</p><p>HR Team</p>";
				this.emailSchedulerService.saveEmail(new EmailScheduler(0, saveDocVerification.getTrackApplication().getJobApplication().getEmail(),"empty", "Notification | Documents Verification", message, 2));
			}			
			//closing document upload option from candidate side
			JobApplication jobApp=saveDocVerification.getTrackApplication().getJobApplication();
			if(jobApp!=null) {
				jobApp.setUploadDocStatus("2");
				this.jobApplicationService.saveJobApplication(jobApp);
			}
			return "redirect:/hrm/track-application/";
		
			
		} catch (Exception e) {
			model.addAttribute("appendClass", "hrm");
			model.addAttribute("title", "Corpseed Dashboard | Document verification");
			model.addAttribute("docVerification", docVerification);
			session.setAttribute("message", new Message("Error : " + e.getMessage(), "alert-danger"));
			return "admin/doc-verification";
		}
	}
	
	@GetMapping("/offer-letter/{uuid}")
	public String offerLetter(@PathVariable("uuid") String uuid,Model model) {		
		TrackApplication trackApp=this.trackAppService.findByUuid(uuid);
		OfferLetter offerLetter=trackApp.getOfferLetter();
		model.addAttribute("appendClass", "hrm");
		if(offerLetter==null) {
			OfferLetter newOfferLetter=new OfferLetter();
			newOfferLetter.setId(0);

			newOfferLetter.setUuid(this.commonServices.getUUID());
			newOfferLetter.setTrackApplication(trackApp);
			newOfferLetter.setJobPosition(trackApp.getJobApplication().getJobs().getPosition());
			newOfferLetter.setName(trackApp.getJobApplication().getName());
			newOfferLetter.setOfferLetterAttachment("NA");
			model.addAttribute("offerLetter",newOfferLetter);
		}else {
			model.addAttribute("offerLetter",offerLetter);
		}
		return "admin/offer-letter";
	}
	@PostMapping("/offer-letter/update")
	public String updateOfferLetter(@Valid @ModelAttribute("offerLetter") OfferLetter offerLetter,BindingResult result,
			@RequestParam("file") MultipartFile file,Model model,HttpSession session) {
		try {
			if(result.hasErrors()) {
				model.addAttribute("appendClass", "hrm");
				model.addAttribute("title", "Corpseed Dashboard | Offer Letter");
				model.addAttribute("offerLetter", offerLetter);
				session.setAttribute("message", new Message("Please fill out the blank fields !!", "alert-danger"));
				return "admin/offer-letter";
			}
			long id=offerLetter.getId();   
			if(file.isEmpty()&&id==0) {
				model.addAttribute("appendClass", "hrm");
				model.addAttribute("title", "Corpseed Dashboard | Offer Letter");
				model.addAttribute("offerLetter", offerLetter);
				session.setAttribute("message", new Message("Please upload offer letter !!", "alert-danger"));
				return "admin/offer-letter";
			}else if(!file.isEmpty()) {
				this.azureAdapter.deleteFile(offerLetter.getOfferLetterAttachment());
				String name = azureAdapter.upload(file,this.commonServices.getUniqueCode());
				offerLetter.setOfferLetterAttachment(name);
			}
			TrackApplication trackAppl=offerLetter.getTrackApplication();
			
			OfferLetter existLetter=trackAppl.getOfferLetter();
			
			if(existLetter!=null) {
				offerLetter.setId(existLetter.getId());
				offerLetter.setJobReport(existLetter.getJobReport());
			}			
			offerLetter.setRoundStatus(1);
			OfferLetter saveOfferLetter = this.trackAppService.saveOfferLetter(offerLetter);	
						
			if(saveOfferLetter==null) {
				model.addAttribute("appendClass", "hrm");
				model.addAttribute("title", "Corpseed Dashboard | Offer Letter");
				model.addAttribute("offerLetter", offerLetter);
				session.setAttribute("message", new Message("Data Not Saved, Please contact to admin !! ", "alert-danger"));
				return "admin/offer-letter";
			}
			DocumentVerification docVer=saveOfferLetter.getTrackApplication().getDocumentVerification();
			docVer.setRoundStatus(1);
			this.trackAppService.saveDocumentVerification(docVer);
			
			if(saveOfferLetter.getStatus().equals("1")&&saveOfferLetter.getTrackApplication().getStatus().equals("2")) {
				TrackApplication trackApp=saveOfferLetter.getTrackApplication();
				trackApp.setStatus("1");
				this.trackAppService.saveTrackApplication(trackApp);
			}
			if(saveOfferLetter.getStatus().equals("1")&&id==0) {
				String message="<p>Dear <b>"+saveOfferLetter.getTrackApplication().getJobApplication().getName()+"</b>,</p>"
						+ "<p>Congratulations and it gives me immense pleasure in welcoming you to the Corpseed family.</p>"
						+ "<p>Please find the enclosed soft copy of your offer letter. You will be joining us as a <b>"+offerLetter.getJobPosition()+"</b> at the <b>Noida</b> site.</p>"+
						"<p>Your tentative DOJ is <b>"+offerLetter.getJoiningDate()+"</b>, and your onboarding buddy will be <b>Renu Mayal</b>. </p>"
						+"<p><b>You are requested to report at the below-mentioned address at 9 AM: </b></p>"
						+"<p>Corpseed ITES Private Limited</p>"
						+"<p><b>Download offer letter by clicking</b> <a href='"+this.env.getProperty("azure_path")+offerLetter.getOfferLetterAttachment()+"' download='download'>here.</a></p>"
						+"<p>A43, Sector 63, Noida</p>"
						+ "<p><b>Thanks & Regards</b></p><p>HR Team</p>";
				this.emailSchedulerService.saveEmail(new EmailScheduler(0, saveOfferLetter.getTrackApplication().getJobApplication().getEmail(),"empty", "Corpseed | Offer Letter", message, 2));
			}
			JobReport existJobReport = saveOfferLetter.getJobReport();
			if(existJobReport==null) {
				User user=saveOfferLetter.getTrackApplication().getJobApplication().getUser();
				String hrName=user.getFirstName()+" "+user.getLastName();
				user=this.userService.getUserByUuid(saveOfferLetter.getTrackApplication().getJobApplication().getUser().getUuid());
				String reporting =user.getFirstName()+" "+user.getLastName();
				JobReport saveReport = this.jobReportService.saveReport(new JobReport(0, this.commonServices.getUUID(), hrName,reporting,this.commonServices.getToday(), saveOfferLetter));
				if(saveReport!=null) {
					Jobs job=offerLetter.getTrackApplication().getJobApplication().getJobs();
					if(job!=null) {
						int totalPosition=Integer.parseInt(job.getNoOfPosition());
						int filledPosition=Integer.parseInt(job.getPositionFilled())+1;
						if(totalPosition==filledPosition) {
							job.setPositionFilled(totalPosition+"");
							job.setDisplayStatus("3");
						}else {
							job.setPositionFilled(filledPosition+"");								
						}
						this.jobService.saveJobs(job);
					}
				}
			}						
			
			return "redirect:/hrm/track-application/";
			
			
		} catch (Exception e) {
			e.printStackTrace();
			model.addAttribute("appendClass", "hrm");
			model.addAttribute("title", "Corpseed Dashboard | Offer Letter");
			model.addAttribute("offerLetter", offerLetter);
			session.setAttribute("message", new Message("Error : " + e.getMessage(), "alert-danger"));
			return "admin/offer-letter";
		}
	}
	@GetMapping("/reject-document")
	@ResponseBody
	public void rejectDocument(@RequestParam("uuid") String uuid,PrintWriter pw) {
		CandidateDocuments candDoc=this.candidateDocumentService.findByUuid(uuid);
		if(candDoc!=null) {
			this.azureAdapter.deleteFile(candDoc.getName());
			candDoc.setName("NA");
			this.candidateDocumentService.saveDocument(candDoc);
			String message="<p>Hi <b>"+candDoc.getJobApplication().getName()+"</b>,</p><p>I found your uploaded <b>"+candDoc.getType()+"</b> is not  clearly visible so please re-upload this.</p><br/><p><b>Thanks & regards</b></p><p>HR Team</p>";
			this.emailSender.sendmail(candDoc.getJobApplication().getEmail(),"empty", "Notification | Re-Upload Document", message, "NA");
		pw.write("pass");
		}else { 
			pw.write("fail");
		}
	}
}
