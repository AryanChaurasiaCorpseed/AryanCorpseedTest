package com.corpseed.controller;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.corpseed.entity.hrmentity.CandidateDocuments;
import com.corpseed.entity.schedulerentity.EmailScheduler;
import com.corpseed.entity.hrmentity.HrAndTechnical;
import com.corpseed.entity.hrmentity.HrScreening;
import com.corpseed.entity.hrmentity.JobApplication;
import com.corpseed.entity.hrmentity.TechnicalRound;
import com.corpseed.entity.hrmentity.TrackApplication;
import com.corpseed.entity.User;
import com.corpseed.util.Message;
import com.corpseed.serviceImpl.CandidateDocumentService;
import com.corpseed.serviceImpl.CommonServices;
import com.corpseed.serviceImpl.hrmserviceimpl.JobApplicationServices;
import com.corpseed.serviceImpl.hrmserviceimpl.TrackAppService;
import com.corpseed.serviceImpl.UserServices;
import com.corpseed.service.EmailSchedulerService;

@Controller
@RequestMapping("/admin/interviews")
public class InterviewsController {

	@Autowired
	private CommonServices commonServices;
	
	@Autowired
	private TrackAppService trackAppService;
		
	@Autowired
	private UserServices userService;
		
	@Autowired
	private CandidateDocumentService candidateDocumentService;
	
	@Autowired
	private Environment env;
	
	@Autowired
	private JobApplicationServices jobApplicationService;
	
	@Autowired
	private EmailSchedulerService emailSchedulerService;
	
	@GetMapping("/new")
	public String interviewsNew(Model model,Principal principal) {	
		User loginedUser = this.commonServices.getLoginedUser(principal);
		model.addAttribute("title", "Corpseed Dashboard | Interviews");
		model.addAttribute("appendClass", "interviews");
		model.addAttribute("candidates", this.trackAppService.findTrackApplicationByUser(loginedUser));
		model.addAttribute("login_user", loginedUser);
		return "admin/interviews";
	}
	@GetMapping("/selected")
	public String interviewsSelected(Model model,Principal principal) {	
		User loginedUser = this.commonServices.getLoginedUser(principal);
		model.addAttribute("title", "Corpseed Dashboard | Interviews");
		model.addAttribute("appendClass", "interviews");
		model.addAttribute("candidates", this.trackAppService.findSelectedTrackApplicationByUser(loginedUser));
		model.addAttribute("login_user", loginedUser);
		return "admin/interviews-selected";
	}
	@GetMapping("/hold")
	public String interviewsHold(Model model,Principal principal) {	
		User loginedUser = this.commonServices.getLoginedUser(principal);

		model.addAttribute("title", "Corpseed Dashboard | Interviews");
		model.addAttribute("appendClass", "interviews");
		model.addAttribute("candidates", this.trackAppService.findHoldTrackApplicationByUser(loginedUser));
		model.addAttribute("login_user", loginedUser);
		return "admin/interviews-selected";
	}
	@GetMapping("/rejected")
	public String interviewsRejected(Model model,Principal principal) {	
		User loginedUser = this.commonServices.getLoginedUser(principal);

		model.addAttribute("title", "Corpseed Dashboard | Interviews");
		model.addAttribute("appendClass", "interviews");
		model.addAttribute("candidates", this.trackAppService.findRejectedTrackApplicationByUser(loginedUser));
		model.addAttribute("login_user", loginedUser);
		return "admin/interviews-rejected";
	}
	
	@GetMapping("/hr-technical/{uuid}")
	public String hrTechnical(@PathVariable("uuid") String uuid,Model model) {		
		TrackApplication trackApp=this.trackAppService.findByUuid(uuid);
		HrAndTechnical hrTechnical=this.trackAppService.findHrTechnicalByTrackApplication(trackApp);
		model.addAttribute("appendClass", "interviews");
		if(hrTechnical==null) {
			HrAndTechnical newHrTechnical=new HrAndTechnical();
			newHrTechnical.setTrackApplication(trackApp);
			newHrTechnical.setUuid(this.commonServices.getUUID());
			model.addAttribute("hrTechnical",newHrTechnical);
		}else {
			model.addAttribute("hrTechnical",hrTechnical);
		}
		return "admin/hr-technical1";
	}
	
	@PostMapping("/hr-technical/update")
	public String updateHrTechnical(@Valid @ModelAttribute("hrTechnical") HrAndTechnical hrTechnical,BindingResult result,
			Model model,HttpSession session,Principal principal) {
		try {
			if(result.hasErrors()) {
				model.addAttribute("appendClass", "interviews");
				model.addAttribute("title", "Corpseed Dashboard | Hr & Technical");
				model.addAttribute("hrTechnical", hrTechnical);
				session.setAttribute("message", new Message("Please fill out the blank fields !!", "alert-danger"));
				return "admin/hr-technical1";
			}	
			long id=hrTechnical.getId();
			HrAndTechnical saveHrTechnical = this.trackAppService.saveHrTechnical(hrTechnical);			
			
			if(saveHrTechnical==null) {
				model.addAttribute("appendClass", "interviews");
				model.addAttribute("title", "Corpseed Dashboard | Hr & Technical");
				model.addAttribute("hrTechnical", hrTechnical);
				session.setAttribute("message", new Message("Data Not Saved, Please try-again later !! ", "alert-danger"));
				return "admin/hr-technical1";
			}
			HrScreening hrScreening=saveHrTechnical.getTrackApplication().getHrScreening();
			hrScreening.setRoundStatus(1);
			this.trackAppService.saveHrScreening(hrScreening);
			
				if(saveHrTechnical.getStatus().equals("1")) {
					TrackApplication trackApp=saveHrTechnical.getTrackApplication();
					if(!trackApp.getStatus().equals("1")) {
						trackApp.setStatus("1");
						this.trackAppService.saveTrackApplication(trackApp);
						}
					JobApplication jobApp=trackApp.getJobApplication();
					if(!jobApp.getApplicationStatus().equals("1")) {
						jobApp.setApplicationStatus("1");
						this.jobApplicationService.saveJobApplication(jobApp);
					}
				}else if(saveHrTechnical.getStatus().equals("2")) {
					TrackApplication trackApp=saveHrTechnical.getTrackApplication();
					if(!trackApp.getStatus().equals("2")) {
						trackApp.setStatus("2");
						this.trackAppService.saveTrackApplication(trackApp);
					}
					JobApplication jobApp=trackApp.getJobApplication();
					if(!jobApp.getApplicationStatus().equals("2")) {
						jobApp.setApplicationStatus("2");
						this.jobApplicationService.saveJobApplication(jobApp);
					}
				}else if(saveHrTechnical.getStatus().equals("3")) {
					TrackApplication trackApp=saveHrTechnical.getTrackApplication();
					if(!trackApp.getStatus().equals("3")) {
						trackApp.setStatus("3");
						this.trackAppService.saveTrackApplication(trackApp);
					}
					JobApplication jobApp=trackApp.getJobApplication();
					if(!jobApp.getApplicationStatus().equals("4")) {
						jobApp.setHoldBy(this.commonServices.getLoginedUser(principal));
						jobApp.setHoldRound("HR Manager");
						jobApp.setApplicationStatus("4");
						this.jobApplicationService.saveJobApplication(jobApp);
					}
				}
				User user=this.userService.getUserByUuid(saveHrTechnical.getTrackApplication().getHrScreening().getAddedByUuid());
				User interviewer=saveHrTechnical.getTrackApplication().getTechnicalRound().getUser();
				List<EmailScheduler> emailSchedulers=new ArrayList<>();
				
				if(saveHrTechnical.getStatus().equals("2")) {
					String message="<p>Hi <b>"+saveHrTechnical.getTrackApplication().getJobApplication().getName()+"</b>,</p>"+
							"<p>It was a pleasure to meet you at your interview earlier this week. We were impressed with your experience, "
							+"professionalism, and approach, but we have chosen to go with another candidate.</p>"
							+"<p>I know you will be disappointed with this news. The interview panel enjoyed your presentation and valued your insights into our strategy.</p>"
							+"<p>We are undergoing a transformation and are seeking individuals with strong background. I am sure that your knowledge, experience, and qualifications will help you find a suitable position to achieve your goals.</p>"
							+"<p>Sincerely,</p><p>HR Team</p>";
					emailSchedulers.add(new EmailScheduler(0, saveHrTechnical.getTrackApplication().getJobApplication().getEmail(),"empty", "Notification | Hr & Notification", message, 2));
				//sending to hr					
					String messageHr="<p>Hi <b>"+user.getFirstName()+" "+user.getLastName()+"</b>,</p><p>You have received an update for "+saveHrTechnical.getTrackApplication().getJobApplication().getName()+" (HR Manager Round) in reference to <b>"+saveHrTechnical.getTrackApplication().getJobApplication().getJobs().getPosition()
							+"</b> by <b>"+interviewer.getFirstName()+" "+interviewer.getLastName()
							+"</b>. Candidate not qualified in this round.</p><p>Feedback for the round is as follow: </p>"+
							"<p>"+saveHrTechnical.getComment()+"</p><br/><p><b>Regards,</b></p><p>Corpseed Updates</p>";
					emailSchedulers.add(new EmailScheduler(0, user.getEmail(),"empty", "Interviewer feedback(HR Manager)", messageHr, 2));		
				}else if(saveHrTechnical.getStatus().equals("3")) {
					List<EmailScheduler> emailScheduler=new ArrayList<>();
					String message="<p>Hi <b>"+saveHrTechnical.getTrackApplication().getJobApplication().getName()+"</b>,</p>"+
							"<p>Thank you for taking the time to submit your application for the role of <b>"+saveHrTechnical.getTrackApplication().getJobApplication().getJobs().getPosition()+"</b> with Corpseed."
									+ " We have throughly reviewed your application and understand the effort you have taken to apply. We appricate your interest in this position. "
									+ "Due to the current circumstances the position went on hold and we are unable to move forward at this stage, but we would like to stay in touch and potentially "
									+ "re-connect in the future.</p>"
							+"<p>If you would prefer not to be contacted, please reply to this email and we will update our records accordingly.</p>"
							+"<p>We'll keep you updated about our organization and upcoming roles, and of course, we'll never share your details with any third parties.</p>"+
							"<p>Sincerely,</p><p>HR Team</p>";
					emailScheduler.add(new EmailScheduler(0, saveHrTechnical.getTrackApplication().getJobApplication().getEmail(),"empty", "Notification | HR Manager", message, 2));
					String messageHr="<p>Hi <b>"+user.getFirstName()+" "+user.getLastName()+"</b>,</p><p>You have received an update on <b>"+saveHrTechnical.getTrackApplication().getJobApplication().getJobs().getPosition()
							+".</b></b> application from <b>"+interviewer.getFirstName()+" "+interviewer.getLastName()
							+"</b> Candidate on-hold in this round.</p><p>Feedback for the round is as follow: </p>"+
							"<p>"+saveHrTechnical.getComment()+"</p><br/><p><b>Regards,</b></p><p>Corpseed Updates</p>";
					emailScheduler.add(new EmailScheduler(0, user.getEmail(),"empty", "Interviewer feedback(HR Manager)", messageHr, 2));
					this.emailSchedulerService.saveAllEmail(emailScheduler);
				}else if(hrTechnical.getStatus().equals("1")&&id==0) {
					//checking user already exist or not
					User findUser=saveHrTechnical.getTrackApplication().getJobApplication().getUser();
					JobApplication jobApp=saveHrTechnical.getTrackApplication().getJobApplication();
					
						//creating document list
						List<CandidateDocuments> docList=new ArrayList<>();
						String today=this.commonServices.getToday();
						docList.add(new CandidateDocuments(0, this.commonServices.getUUID(), "Passport Size Photo", "NA", today, findUser,jobApp));
						docList.add(new CandidateDocuments(0, this.commonServices.getUUID(), "Aadhaar Card", "NA", today, findUser,jobApp));
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
						String message="<p>Hi <b>"+saveHrTechnical.getTrackApplication().getJobApplication().getName()+"</b>,</p>"
								+"<p>Greetings for the day.</p>"
								+"<p>We are pleased to offer you the position of <b>"+saveHrTechnical.getTrackApplication().getJobApplication().getJobs().getPosition()+".</b></p>"
								+"<p>Please note that we would require a soft copy of the documents mentioned on your candidate dashboard.</p>"
								+"<p><b>NOTE:</b> Please fill up all the details before submitting the documents."
								+ "Please download <a href='"+env.getProperty("azure_path")+"BGV.xlsx'>background verification form</a> to upload on dashboard.</p>"								
								+"<p>Login link : <a href='"+env.getProperty("domain.name")+"/login'>www.corpseed.com</p><p>Username : "+saveHrTechnical.getTrackApplication().getJobApplication().getEmail()+"</p>"
								+"<p>If Password not remember then click on login link and forget password.</p>"
								+"<p>If you have any questions or need additional information, please don’t hesitate to contact me by email <a href='mailto:hr@corpseed.com'>hr@corpseed.com</a></p><br/><p>Thanks & Regards</p><p>HR Team</p>";
						emailSchedulers.add(new EmailScheduler(0, saveHrTechnical.getTrackApplication().getJobApplication().getEmail(),"empty", "Corpseed | Share documents", message, 2));
					//sending to hr
						String messageHr="<p>Hi <b>"+user.getFirstName()+" "+user.getLastName()+"</b>,</p><p>You have received an update for "+saveHrTechnical.getTrackApplication().getJobApplication().getName()+" (HR Manager Round) in reference to <b>"+saveHrTechnical.getTrackApplication().getJobApplication().getJobs().getPosition()
								+"</b> by <b>"+interviewer.getFirstName()+" "+interviewer.getLastName()
								+"</b>. Please update the application to proceed forward for <a href='"+env.getProperty("domain.name")+"/hrm/track-application/document-verification/"+saveHrTechnical.getTrackApplication().getUuid()+"'>document verification</a>.</p><p>Feedback for the round is as follow: </p>"+
								"<p>"+saveHrTechnical.getComment()+"</p><br/><p><b>Regards,</b></p><p>Corpseed Updates</p>";
						emailSchedulers.add(new EmailScheduler(0, user.getEmail(),"empty", "Interviewer feedback(HR Manager)", messageHr, 2));		
				}
				if(!emailSchedulers.isEmpty())
					this.emailSchedulerService.saveAllEmail(emailSchedulers);
				//update job application upload document status on 
				JobApplication jobApp=saveHrTechnical.getTrackApplication().getJobApplication();
				jobApp.setUploadDocStatus("1");
				this.jobApplicationService.saveJobApplication(jobApp);
				return "redirect:/admin/interviews/new";
			
			
		} catch (Exception e) {
			model.addAttribute("appendClass", "interviews");
			model.addAttribute("title", "Corpseed Dashboard | Hr & Technical");
			model.addAttribute("hrTechnical", hrTechnical);
			session.setAttribute("message", new Message("Error : " + e.getMessage(), "alert-danger"));
			return "admin/hr-technical1";
		}
	}
	
	@GetMapping("/technical-round/{uuid}")
	public String technicalRound(@PathVariable("uuid") String uuid,Model model) {		
		TrackApplication trackApp=this.trackAppService.findByUuid(uuid);
		TechnicalRound technicalRound=this.trackAppService.findTechnicalRoundByTrackApplication(trackApp);
		model.addAttribute("appendClass", "interviews");
		if(technicalRound==null) {
			TechnicalRound newTechnicalRound=new TechnicalRound();
			newTechnicalRound.setUuid(this.commonServices.getUUID());
			newTechnicalRound.setTrackApplication(trackApp);
			model.addAttribute("technicalRound",newTechnicalRound);
		}else {
			model.addAttribute("technicalRound",technicalRound);
		}
		return "admin/technical-round1";
	}
	
	@PostMapping("/technical-round/update")
	public String updateTechnicalRound(@Valid @ModelAttribute("technicalRound") TechnicalRound technicalRound,BindingResult result,
			Model model,HttpSession session,Principal principal) {
		try {
			if(result.hasErrors()) {
				model.addAttribute("appendClass", "interviews");
				model.addAttribute("title", "Corpseed Dashboard | Technical Round");
				model.addAttribute("technicalRound", technicalRound);
				session.setAttribute("message", new Message("Please fill out the blank fields !!", "alert-danger"));
				return "admin/technical-round1";
			}			
			TechnicalRound findTechnicalRound=this.trackAppService.findTechnicalRoundByTrackApplication(technicalRound.getTrackApplication());
			if(findTechnicalRound!=null) {
				technicalRound.setId(findTechnicalRound.getId());
				technicalRound.setUuid(findTechnicalRound.getUuid());
				technicalRound.setDateTime(findTechnicalRound.getDateTime());
				technicalRound.setInterviewMode(findTechnicalRound.getInterviewMode());
			}
			
			TechnicalRound saveTechnicalRound = this.trackAppService.saveTechnicalRound(technicalRound);			
			if(saveTechnicalRound==null) {
				model.addAttribute("appendClass", "interviews");
				model.addAttribute("title", "Corpseed Dashboard | Technical Round");
				model.addAttribute("technicalRound", technicalRound);
				session.setAttribute("message", new Message("Data not saved,Please try again later !! ", "alert-danger"));
				return "admin/technical-round1";
			}
			if(saveTechnicalRound.getStatus().equals("1")) {
				TrackApplication trackApp=saveTechnicalRound.getTrackApplication();
				if(!trackApp.getStatus().equals("1")) {
					trackApp.setStatus("1");
					this.trackAppService.saveTrackApplication(trackApp);
				}
				JobApplication jobApp=trackApp.getJobApplication();
				if(!jobApp.getApplicationStatus().equals("1")) {
					jobApp.setApplicationStatus("1");
					this.jobApplicationService.saveJobApplication(jobApp);
				}
			}else if(saveTechnicalRound.getStatus().equals("2")) {
				TrackApplication trackApp=saveTechnicalRound.getTrackApplication();
				if(!trackApp.getStatus().equals("2")) {
					trackApp.setStatus("2");
					this.trackAppService.saveTrackApplication(trackApp);
				}
				JobApplication jobApp=trackApp.getJobApplication();
				if(!jobApp.getApplicationStatus().equals("2")) {
					jobApp.setApplicationStatus("2");
					this.jobApplicationService.saveJobApplication(jobApp);
				}
			}else if(saveTechnicalRound.getStatus().equals("3")) {
				TrackApplication trackApp=saveTechnicalRound.getTrackApplication();
				if(!trackApp.getStatus().equals("3")) {
					trackApp.setStatus("3");
					this.trackAppService.saveTrackApplication(trackApp);
				}
				JobApplication jobApp=trackApp.getJobApplication();
				if(!jobApp.getApplicationStatus().equals("4")) {
					jobApp.setHoldBy(this.commonServices.getLoginedUser(principal));
					jobApp.setHoldRound("Technical");
					jobApp.setApplicationStatus("4");
					this.jobApplicationService.saveJobApplication(jobApp);
				}
			}
			User user=this.userService.getUserByUuid(saveTechnicalRound.getTrackApplication().getAddedByUuid());
			User interviewer=saveTechnicalRound.getTrackApplication().getUser();
			if(saveTechnicalRound.getStatus().equals("2")) {
				List<EmailScheduler> emailScheduler=new ArrayList<>();
				String message="<p>Hi <b>"+saveTechnicalRound.getTrackApplication().getJobApplication().getName()+"</b>,</p>"+
						"<p>It was a pleasure to meet you at your interview earlier this week. We were impressed with your experience, "
						+"professionalism, and approach, but we have chosen to go with another candidate.</p>"
						+"<p>I know you will be disappointed with this news. The interview panel enjoyed your presentation and valued your insights into our strategy.</p>"
						+"<p>We are undergoing a transformation and are seeking individuals with strong background. I am sure that your knowledge, experience, and qualifications will help you find a suitable position to achieve your goals.</p>"+
						"<p>Sincerely,</p><p>HR Team</p>";
				emailScheduler.add(new EmailScheduler(0, saveTechnicalRound.getTrackApplication().getJobApplication().getEmail(),"empty", "Notification | Technical Round", message, 2));
				String messageHr="<p>Hi <b>"+user.getFirstName()+" "+user.getLastName()+"</b>,</p><p>You have received an update for "+saveTechnicalRound.getTrackApplication().getJobApplication().getName()+" (Technical Round) in reference to <b>"+saveTechnicalRound.getTrackApplication().getJobApplication().getJobs().getPosition()
						+"</b> by <b>"+interviewer.getFirstName()+" "+interviewer.getLastName()
						+"</b>. Candidate not qualified in this round.</p><p>Feedback for the round is as follow: </p>"+
						"<p>"+saveTechnicalRound.getComment()+"</p><br/><p><b>Regards,</b></p><p>Corpseed Updates</p>";
				emailScheduler.add(new EmailScheduler(0, user.getEmail(),"empty", "Interviewer feedback(Technical Round)", messageHr, 2));
				this.emailSchedulerService.saveAllEmail(emailScheduler);
			}else if(saveTechnicalRound.getStatus().equals("3")) {
				List<EmailScheduler> emailScheduler=new ArrayList<>();
				String message="<p>Hi <b>"+saveTechnicalRound.getTrackApplication().getJobApplication().getName()+"</b>,</p>"+
						"<p>Thank you for taking the time to submit your application for the role of <b>"+saveTechnicalRound.getTrackApplication().getJobApplication().getJobs().getPosition()+"</b> with Corpseed."
						+ " We have throughly reviewed your application and understand the effort you have taken to apply. We appricate your interest in this position. "
						+ "Due to the current circumstances the position went on hold and we are unable to move forward at this stage, but we would like to stay in touch and potentially "
						+ "re-connect in the future.</p>"
				+"<p>If you would prefer not to be contacted, please reply to this email and we will update our records accordingly.</p>"
				+"<p>We'll keep you updated about our organization and upcoming roles, and of course, we'll never share your details with any third parties.</p>"+
				"<p>Sincerely,</p><p>HR Team</p>";
				emailScheduler.add(new EmailScheduler(0, saveTechnicalRound.getTrackApplication().getJobApplication().getEmail(),"empty", "Notification | Technical Round", message, 2));
				String messageHr="<p>Hi <b>"+user.getFirstName()+" "+user.getLastName()+"</b>,</p><p>You have received an update for "+saveTechnicalRound.getTrackApplication().getJobApplication().getName()+" (Technical Round) in reference to <b>"+saveTechnicalRound.getTrackApplication().getJobApplication().getJobs().getPosition()
						+"</b> by <b>"+interviewer.getFirstName()+" "+interviewer.getLastName()
						+"</b>. Candidate on-hold in this round.</p><p>Feedback for the round is as follow: </p>"+
						"<p>"+saveTechnicalRound.getComment()+"</p><br/><p><b>Regards,</b></p><p>Corpseed Updates</p>";
				emailScheduler.add(new EmailScheduler(0, user.getEmail(),"empty", "Interviewer feedback(Technical Round)", messageHr, 2));
				
				this.emailSchedulerService.saveAllEmail(emailScheduler);
			}else {			
				//sending status record to hr		
				String message="<p>Hi <b>"+user.getFirstName()+" "+user.getLastName()+"</b>,</p><p>You have received an update for "+saveTechnicalRound.getTrackApplication().getJobApplication().getName()+" (Technical Round) in reference to <b>"+saveTechnicalRound.getTrackApplication().getJobApplication().getJobs().getPosition()
						+"</b> by <b>"+interviewer.getFirstName()+" "+interviewer.getLastName()
				+"</b>. Please update the application to proceed forward for <a href='"+env.getProperty("domain.name")+"/hrm/track-application/technical-round/"+saveTechnicalRound.getTrackApplication().getUuid()+"'>business round</a>.</p><p>Feedback for the round is as follow: </p>"+
				"<p>"+saveTechnicalRound.getComment()+"</p><br/><p><b>Regards,</b></p><p>Corpseed Updates</p>";
				this.emailSchedulerService.saveEmail(new EmailScheduler(0, user.getEmail(),"empty", "Interviewer feedback(Technical Round)", message, 2));
			}
			return "redirect:/admin/interviews/new";
			
		} catch (Exception e) {
			model.addAttribute("appendClass", "interviews");
			model.addAttribute("title", "Corpseed Dashboard | Technical Round");
			model.addAttribute("technicalRound", technicalRound);
			session.setAttribute("message", new Message("Error : " + e.getMessage(), "alert-danger"));
			return "admin/technical-round1";
		}
	}
	@GetMapping("/business-round/{uuid}")
	public String hrScreening(@PathVariable("uuid") String uuid,Model model) {
		TrackApplication trackApp=this.trackAppService.findByUuid(uuid);
		HrScreening hrScreening=trackApp.getHrScreening();
		
		model.addAttribute("appendClass", "interviews");
		model.addAttribute("title", "Corpseed Dashboard | Business Round");		
		if(hrScreening==null) {
			HrScreening newHrScreening=new HrScreening();
			newHrScreening.setTrackApplication(trackApp);
			newHrScreening.setUuid(this.commonServices.getUUID());
			model.addAttribute("hrScreening",newHrScreening);
		}else {
			model.addAttribute("hrScreening",hrScreening);
		}
		
		return "admin/business-round";
	}
	@PostMapping("/business-round/update")
	public String updateHrScreening(@Valid @ModelAttribute("hrScreening") HrScreening hrScreening,BindingResult result,
			Model model,HttpSession session,Principal principal) {		
		try {		
		
			if(result.hasErrors()) {
				model.addAttribute("appendClass", "interviews");
				model.addAttribute("title", "Corpseed Dashboard | Business Round");
				model.addAttribute("hrScreening", hrScreening);
				session.setAttribute("message", new Message("Please fill out the blank fields !!", "alert-danger"));
				return "admin/business-round";
			}		
			HrScreening saveHrScreening = this.trackAppService.saveHrScreening(hrScreening);
			
			TechnicalRound techRound=saveHrScreening.getTrackApplication().getTechnicalRound();
			techRound.setRoundStatus(1);
			this.trackAppService.saveTechnicalRound(techRound);
			
				if(saveHrScreening.getStatus().equals("1")) {
					TrackApplication trackApp=saveHrScreening.getTrackApplication();
					if(!trackApp.getStatus().equals("1")) {
						trackApp.setStatus("1");					
						this.trackAppService.saveTrackApplication(trackApp);
					}
					JobApplication jobApp=trackApp.getJobApplication();
					if(!jobApp.getApplicationStatus().equals("1")) {
						jobApp.setApplicationStatus("1");
						this.jobApplicationService.saveJobApplication(jobApp);
					}
				}else if(saveHrScreening.getStatus().equals("2")) {
					TrackApplication trackApp=saveHrScreening.getTrackApplication();
					if(!trackApp.getStatus().equals("2")) {
						trackApp.setStatus("2");
						this.trackAppService.saveTrackApplication(trackApp);
					}
					JobApplication jobApp=trackApp.getJobApplication();
					if(!jobApp.getApplicationStatus().equals("2")) {
						jobApp.setApplicationStatus("2");
						this.jobApplicationService.saveJobApplication(jobApp);
					}
				}else if(saveHrScreening.getStatus().equals("3")) {
					TrackApplication trackApp=saveHrScreening.getTrackApplication();
					if(!trackApp.getStatus().equals("3")) {
						trackApp.setStatus("3");
						this.trackAppService.saveTrackApplication(trackApp);
					}
					JobApplication jobApp=trackApp.getJobApplication();
					if(!jobApp.getApplicationStatus().equals("4")) {
						jobApp.setHoldBy(this.commonServices.getLoginedUser(principal));
						jobApp.setHoldRound("Business");
						jobApp.setApplicationStatus("4");
						this.jobApplicationService.saveJobApplication(jobApp);
					}
				}
				User user=this.userService.getUserByUuid(saveHrScreening.getTrackApplication().getAddedByUuid());
				User interviewer=saveHrScreening.getTrackApplication().getTechnicalRound().getUser();
				if(saveHrScreening.getStatus().equals("2")) {
					List<EmailScheduler> emailScheduler=new ArrayList<>();
					String message="<p>Hi <b>"+saveHrScreening.getTrackApplication().getJobApplication().getName()+"</b>,</p>"+
							"<p>It was a pleasure to meet you at your interview earlier this week. We were impressed with your experience, "
							+"professionalism, and approach, but we have chosen to go with another candidate.</p>"
							+"<p>I know you will be disappointed with this news. The interview panel enjoyed your presentation and valued your insights into our strategy.</p>"
							+"<p>We are undergoing a transformation and are seeking individuals with strong background. I am sure that your knowledge, experience, and qualifications will help you find a suitable position to achieve your goals.</p>"+
							"<p>Sincerely,</p><p>HR Team</p>";
					emailScheduler.add(new EmailScheduler(0, saveHrScreening.getTrackApplication().getJobApplication().getEmail(),"empty", "Notification | Business Round", message, 2));
					String messageHr="<p>Hi <b>"+user.getFirstName()+" "+user.getLastName()+"</b>,</p><p>You have received an update for "+saveHrScreening.getTrackApplication().getJobApplication().getName()+" (Technical Round) in reference to <b>"+saveHrScreening.getTrackApplication().getJobApplication().getJobs().getPosition()
							+"</b> by <b>"+interviewer.getFirstName()+" "+interviewer.getLastName()
							+"</b>. Candidate not qualified in this round.</p><p>Feedback for the round is as follow: </p>"+
							"<p>"+saveHrScreening.getComment()+"</p><br/><p><b>Regards,</b></p><p>Corpseed Updates</p>";
					emailScheduler.add(new EmailScheduler(0, user.getEmail(),"empty", "Interviewer feedback(Business Round)", messageHr, 2));
					this.emailSchedulerService.saveAllEmail(emailScheduler);
				}else if(saveHrScreening.getStatus().equals("3")) {
					List<EmailScheduler> emailScheduler=new ArrayList<>();
					String message="<p>Hi <b>"+saveHrScreening.getTrackApplication().getJobApplication().getName()+"</b>,</p>"+
							"<p>Thank you for taking the time to submit your application for the role of <b>"+saveHrScreening.getTrackApplication().getJobApplication().getJobs().getPosition()+"</b> with Corpseed."
							+ " We have throughly reviewed your application and understand the effort you have taken to apply. We appricate your interest in this position. "
							+ "Due to the current circumstances the position went on hold and we are unable to move forward at this stage, but we would like to stay in touch and potentially "
							+ "re-connect in the future.</p>"
					+"<p>If you would prefer not to be contacted, please reply to this email and we will update our records accordingly.</p>"
					+"<p>We'll keep you updated about our organization and upcoming roles, and of course, we'll never share your details with any third parties.</p>"+
					"<p>Sincerely,</p><p>HR Team</p>";
					emailScheduler.add(new EmailScheduler(0, saveHrScreening.getTrackApplication().getJobApplication().getEmail(),"empty", "Notification | Technical Round", message, 2));
					String messageHr="<p>Hi <b>"+user.getFirstName()+" "+user.getLastName()+"</b>,</p><p>You have received an update for "+saveHrScreening.getTrackApplication().getJobApplication().getName()+" (Technical Round) in reference to <b>"+saveHrScreening.getTrackApplication().getJobApplication().getJobs().getPosition()
							+"</b> by <b>"+interviewer.getFirstName()+" "+interviewer.getLastName()
							+"</b>. Candidate on-hold in this round.</p><p>Feedback for the round is as follow: </p>"+
							"<p>"+saveHrScreening.getComment()+"</p><br/><p><b>Regards,</b></p><p>Corpseed Updates</p>";
					emailScheduler.add(new EmailScheduler(0, user.getEmail(),"empty", "Interviewer feedback(Business Round)", messageHr, 2));
					this.emailSchedulerService.saveAllEmail(emailScheduler);
				}else {			
					//sending status record to hr
					String message="<p>Hi <b>"+user.getFirstName()+" "+user.getLastName()+"</b>,</p><p>You have received an update for "+saveHrScreening.getTrackApplication().getJobApplication().getName()+" (Technical Round) in reference to <b>"+saveHrScreening.getTrackApplication().getJobApplication().getJobs().getPosition()
							+"</b> by <b>"+interviewer.getFirstName()+" "+interviewer.getLastName()
					+"</b>. Please update the application to proceed forward for <a href='"+env.getProperty("domain.name")+"/hrm/track-application/business-round/"+saveHrScreening.getTrackApplication().getUuid()+"'>HR Manager round</a>.</p><p>Feedback for the round is as follow: </p>"+
					"<p>"+saveHrScreening.getComment()+"</p><br/><p><b>Regards,</b></p><p>Corpseed Updates</p>";
					this.emailSchedulerService.saveEmail(new EmailScheduler(0, user.getEmail(),"empty", "Interviewer feedback(Business Round)", message, 2));
				}								
				return "redirect:/admin/interviews/new";			
			
		} catch (Exception e) {
			e.printStackTrace();
			model.addAttribute("appendClass", "interviews");
			model.addAttribute("title", "Corpseed Dashboard | Business Round");
			model.addAttribute("hrScreening", hrScreening);
			session.setAttribute("message", new Message("Error : " + e.getMessage(), "alert-danger"));
			return "admin/business-round";
		}
	}
	@PostMapping("/filter")
	public String trackApplicationFilter(@RequestParam("status") String status,@RequestParam("department") String department,
			@RequestParam("round") String round,Model model,Principal principal,@RequestParam("redirect") String redirect) {
		try {		
			User user = this.commonServices.getLoginedUser(principal);
			List<TrackApplication> trackApplication=null;
			
			if(department!=null&&department.length()>0&&round!=null&&round.length()>0) {	
				if(round.equals("1"))
				trackApplication=this.trackAppService.findByStatusAndDepartmentAndRoundOne(department,status,user);
				else if(round.equals("2"))
					trackApplication=this.trackAppService.findByStatusAndDepartmentAndRoundSecond(department,status,user);
				else if(round.equals("3"))
					trackApplication=this.trackAppService.findByStatusAndDepartmentAndRoundThird(department,status,user);
			}
			else if(department!=null&&department.length()>0) {
				trackApplication=this.trackAppService.findByStatusAndDepartment(department,status,user);
			}else if(round!=null&&round.length()>0) {
				if(round.equals("1"))
					trackApplication=this.trackAppService.findByStatusAndRoundOne(status,user);
				else if(round.equals("2"))
					trackApplication=this.trackAppService.findByStatusAndRoundSecond(status,user);
				else if(round.equals("3"))
					trackApplication=this.trackAppService.findByStatusAndRoundThird(status,user);
				
			}else
				trackApplication = this.trackAppService.findByStatusAndUser(status,user);
			
		model.addAttribute("title", "Corpseed Dashboard | Interviews");
		model.addAttribute("appendClass", "interviews");
		model.addAttribute("candidates", trackApplication);
		model.addAttribute("login_user", user);
		model.addAttribute("searchStatus", status);
		model.addAttribute("searchDepartment", department);
		model.addAttribute("searchRound", round);

		} catch (Exception e) {e.getMessage();
			e.printStackTrace();
		}
		return "admin/"+redirect;
	}
	@GetMapping("/filter")
	public String trackApplicationNoFilter(Model model) {
		return "redirect:/admin/interviews/new";
	}
}
