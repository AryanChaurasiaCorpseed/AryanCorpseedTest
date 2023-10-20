package com.corpseed.controller.hrmController;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.corpseed.entity.hrmentity.CandidateDocuments;
import com.corpseed.entity.History;
import com.corpseed.entity.hrmentity.InterviewSchedule;
import com.corpseed.entity.hrmentity.JobApplication;
import com.corpseed.entity.hrmentity.Jobs;
import com.corpseed.entity.hrmentity.TrackApplication;
import com.corpseed.entity.User;
import com.corpseed.util.Message;
import com.corpseed.serviceImpl.CandidateDocumentService;
import com.corpseed.serviceImpl.CommonServices;
import com.corpseed.serviceImpl.HistoryService;
import com.corpseed.serviceImpl.IndustryService;
import com.corpseed.serviceImpl.hrmserviceimpl.InterviewScheduleService;
import com.corpseed.serviceImpl.hrmserviceimpl.JobApplicationServices;
import com.corpseed.serviceImpl.hrmserviceimpl.JobServices;
import com.corpseed.serviceImpl.servicesserviceimpl.ServicesService;
import com.corpseed.serviceImpl.hrmserviceimpl.TrackAppService;

@Controller
@RequestMapping("/hrm/jobs")
public class JobsController {

	@Autowired
	private JobServices jobServices;
	
	@Autowired
	private CommonServices commonService;
	
	@Autowired
	private JobApplicationServices jobAppService;
	
	@Autowired
	private HistoryService historyService;
	
	@Autowired
	private InterviewScheduleService interviewService;
	
	@Autowired
	private TrackAppService trackAppService;
	
	@Autowired
	private CandidateDocumentService candDocService;
	
	@Autowired
	private ServicesService servicesService;
	
	@Autowired
	private IndustryService industryService;
	
	@GetMapping("/")
	public String jobs(Model model) {
		model.addAttribute("title", "Corpseed Dashboard | Manage Jobs");
		model.addAttribute("appendClass", "hrm"); 
		model.addAttribute("jobs", this.jobServices.findAllJobsByStatusNot("3"));	
		return "admin/jobs";
	}
	
	@GetMapping("/add")
	public String addJobs(Model model) {
		model.addAttribute("title", "Corpseed Dashboard | Add Jobs");
		model.addAttribute("appendClass", "hrm");
		model.addAttribute("jobs", new Jobs());
		model.addAttribute("services", this.servicesService.getAllServicesByStatus("1"));
		model.addAttribute("industries", this.industryService.getIndustryByDisplsyStatus("1"));
		return "admin/jobs-add";
	}
	
	@GetMapping("/{uuid}/repost")
	public String repostJobs(@PathVariable("uuid") String uuid,Model model) {
		model.addAttribute("title", "Corpseed Dashboard | Add Jobs");
		model.addAttribute("appendClass", "hrm");
		model.addAttribute("services", this.servicesService.getAllServicesByStatus("1"));
		model.addAttribute("industries", this.industryService.getIndustryByDisplsyStatus("1"));
		Jobs job=this.jobServices.getjobByUuid(uuid);
		if(job==null)model.addAttribute("jobs", new Jobs());
		else {
			job.setId(0);
			model.addAttribute("jobs", job);
		}
		return "admin/jobs-add";
	}
	@GetMapping("/position-filled/{uuid}")
	public String positionFilled(@PathVariable("uuid") String uuid) {
		Jobs job=this.jobServices.getjobByUuid(uuid);
		if(job==null) return "error/404";		
		job.setDisplayStatus("3");
		this.jobServices.saveJobs(job);
		
		return "redirect:/hrm/jobs/";
	}
	
	@GetMapping("/delete/{uuid}")
	public String deleteJob(@PathVariable("uuid") String uuid,Principal principal,HttpServletRequest req) {
		if(this.commonService.getLoginedUser(principal).getRole().equals("ROLE_ADMIN")) {
		Jobs job=this.jobServices.getjobByUuid(uuid);
		if(job!=null) {
			job.setDeleteStatus(1);
			this.jobServices.saveJobs(job);

			User user=this.commonService.getLoginedUser(principal);
			
			//adding category in history
			this.historyService.savehistory(new History(0, this.commonService.getUUID(),
					"Job", job.getId(), this.commonService.getBrowser(req), 
					this.commonService.getIpAddress(req), user.getId(), user.getFirstName()+" "+user.getLastName()
					, this.commonService.getToday(), this.commonService.getTime(),job.getJobTitle()));		
			
			//updating job application
			List<JobApplication> jobApplication=job.getJobApplication();
			if(!jobApplication.isEmpty()) {
				List<InterviewSchedule> interviewSchedule=new ArrayList<>();
				List<TrackApplication> trackApp=new ArrayList<>();
				List<CandidateDocuments> candidateDocument=new ArrayList<>();
				
				for (JobApplication jobApp : jobApplication) {
					jobApp.setDeleteStatus(1);
					
					if(!jobApp.getInterviewSchedule().isEmpty())
						interviewSchedule.addAll(jobApp.getInterviewSchedule());
					
					if(jobApp.getTrackApplication()!=null)
						trackApp.add(jobApp.getTrackApplication());
					
					if(!jobApp.getCandidateDocument().isEmpty())
						candidateDocument.addAll(jobApp.getCandidateDocument());					
				}
				if(!interviewSchedule.isEmpty()) {
					interviewSchedule.forEach(is->is.setDeleteStatus(1));
					this.interviewService.saveScheduledInterview(interviewSchedule);
				}
				if(!trackApp.isEmpty()) {
					trackApp.forEach(ta->ta.setDeleteStatus(1));
					this.trackAppService.saveTrackApplications(trackApp);
				}
				if(!candidateDocument.isEmpty()) {
					candidateDocument.forEach(cd->cd.setDeleteStatus(1));
					this.candDocService.saveDocuments(candidateDocument);
				}
				
				this.jobAppService.saveJobApplications(jobApplication);	
			}
			
		}
		
		return "redirect:/hrm/jobs/";
		}else {
			return "error/403";
		}
	}
	@GetMapping("/edit/{jobsUUID}")
	public String editJob(@PathVariable("jobsUUID") String jobsUUID,Model model) {
		model.addAttribute("title", "Corpseed Dashboard | Edit Job");
		model.addAttribute("appendClass", "hrm");
		model.addAttribute("jobsUUID", jobsUUID);
		Jobs getjobByUuid = this.jobServices.getjobByUuid(jobsUUID);
		model.addAttribute("services", this.servicesService.getAllServicesByStatus("1"));
		model.addAttribute("industries", this.industryService.getIndustryByDisplsyStatus("1"));
		String description=getjobByUuid.getDescription();
		while(description.contains("<iframe src=\"https://docs.google.com/gview?url=")||description.contains("<iframe src=\"http://docs.google.com/gview?url=")) {
			description = description.replace("<iframe src=\"http://docs.google.com/gview?url=", "pdfview=");
			description = description.replace("<iframe src=\"https://docs.google.com/gview?url=", "pdfview=");
			description = description.replace("&embedded=true\" frameborder=\"0\" webkitallowfullscreen mozallowfullscreen allowfullscreen style=\"width: 100%; height: 700px;\"></iframe>", "=pdfview");
			description=description.replace("&embedded=true\" frameborder=\"0\" webkitallowfullscreen mozallowfullscreen allowfullscreen style=\"width: 100%; height: 500px;\">","=pdfview");
		}	
		getjobByUuid.setDescription(description);
		
		model.addAttribute("jobs", getjobByUuid);
		return "admin/jobs-edit";
	}
	
	@PostMapping("/update/{uuid}")
	public String updateJob(@Valid @ModelAttribute("jobs") Jobs jobs,BindingResult result,
			@PathVariable("uuid") String uuid,Principal principal,Model model,HttpSession session) {
		
		try {
			if(result.hasErrors()) {
				model.addAttribute("title", "Corpseed Dashboard | Edit Jobs");
				model.addAttribute("appendClass", "hrm");
				model.addAttribute("jobs", jobs);
				session.setAttribute("message", new Message("Please fill out the blank fields !!", "alert-danger"));
				model.addAttribute("services", this.servicesService.getAllServicesByStatus("1"));
				model.addAttribute("industries", this.industryService.getIndustryByDisplsyStatus("1"));
				return "admin/jobs-edit";
			}
			
			Jobs existJob=this.jobServices.findBySlugAndUuidNotAndDisplayStatusNot(jobs.getSlug(),"3",jobs.getUuid());
			if(existJob!=null) {
				model.addAttribute("jobs", jobs);
				if(existJob.getDeleteStatus()==1)
					session.setAttribute("message", new Message("Job exist in Trash !!", "alert-danger"));
				else
					session.setAttribute("message", new Message("Duplicate Job Entry, You Can Update existing job !!", "alert-danger"));
				model.addAttribute("services", this.servicesService.getAllServicesByStatus("1"));
				model.addAttribute("industries", this.industryService.getIndustryByDisplsyStatus("1"));
				return "admin/jobs-edit";
			}
				jobs.setAddedByUUID(this.commonService.getLoginedUser(principal).getUuid());
				String summary=jobs.getDescription();
				
				summary=summary.replace("<div class=\"table-width\">","");
				summary=summary.replace("</table>\r\n"
						+ "</div>","</table>");

				summary=summary.replace("<table","<div class='table-width'><table");
				summary=summary.replace("</table>","</table></div>");	
				
				while(summary.contains("pdfview=")) {
					summary=summary.replace("pdfview=", "<iframe src=\"https://docs.google.com/gview?url=");
					summary=summary.replace("=pdfview", "&embedded=true\" frameborder=\"0\" webkitallowfullscreen mozallowfullscreen allowfullscreen style=\"width: 100%; height: 700px;\"></iframe>");
	            }
				
				jobs.setDescription(summary);
				
				Jobs saveJobs=this.jobServices.saveJobs(jobs);
				if(saveJobs==null) {
					model.addAttribute("title", "Corpseed Dashboard | Edit Jobs");
					model.addAttribute("appendClass", "hrm");
					model.addAttribute("jobs", jobs);
					session.setAttribute("message", new Message("Data Not Saved, Please try-again later !! ", "alert-danger"));
					model.addAttribute("services", this.servicesService.getAllServicesByStatus("1"));
					model.addAttribute("industries", this.industryService.getIndustryByDisplsyStatus("1"));
					return "admin/jobs-edit";
				}
				
				return "redirect:/hrm/jobs/";				
			
		} catch (Exception e) {e.printStackTrace();
			model.addAttribute("title", "Corpseed Dashboard | Edit Jobs");
			model.addAttribute("appendClass", "hrm");
			model.addAttribute("jobs", jobs);
			session.setAttribute("message", new Message("Error : " + e.getMessage(), "alert-danger"));
			model.addAttribute("services", this.servicesService.getAllServicesByStatus("1"));
			model.addAttribute("industries", this.industryService.getIndustryByDisplsyStatus("1"));
			return "admin/jobs-edit";
		}
	}
	
	@PostMapping("/add")
	public String saveJobs(@Valid @ModelAttribute("jobs") Jobs jobs,BindingResult result,Principal principal,Model model,HttpSession session) {
		
		try {
			model.addAttribute("title", "Corpseed Dashboard | Add Jobs");
			model.addAttribute("appendClass", "hrm");
			model.addAttribute("services", this.servicesService.getAllServicesByStatus("1"));
			model.addAttribute("industries", this.industryService.getIndustryByDisplsyStatus("1"));
			
			if(result.hasErrors()) {
				model.addAttribute("jobs", jobs);
				session.setAttribute("message", new Message("Please fill out the blank fields !!", "alert-danger"));
				return "admin/jobs-add";
			}
			Jobs existJob=this.jobServices.findBySlugAndDisplayStatusNot(jobs.getSlug(),"3");
			if(existJob!=null) {
				model.addAttribute("jobs", jobs);
				if(existJob.getDeleteStatus()==1)
					session.setAttribute("message", new Message("Job exist in Trash !!", "alert-danger"));
				else
					session.setAttribute("message", new Message("Duplicate Job Entry, You Can Update existing job !!", "alert-danger"));
				
				return "admin/jobs-add";
			}
			
				jobs.setUuid(this.commonService.getUUID());
				jobs.setAddedByUUID(this.commonService.getLoginedUser(principal).getUuid());
				String summary=jobs.getDescription();

				summary=summary.replace("<table","<div class='table-width'><table");
				summary=summary.replace("</table>","</table></div>");
				
				while(summary.contains("pdfview=")) {
					summary=summary.replace("pdfview=", "<iframe src=\"https://docs.google.com/gview?url=");
					summary=summary.replace("=pdfview", "&embedded=true\" frameborder=\"0\" webkitallowfullscreen mozallowfullscreen allowfullscreen style=\"width: 100%; height: 700px;\"></iframe>");
	            }
				
				jobs.setDescription(summary);
				Jobs saveJobs=this.jobServices.saveJobs(jobs);
				if(saveJobs==null) {
					model.addAttribute("jobs", jobs);
					session.setAttribute("message", new Message("Data Not Saved, Please try-again later !! ", "alert-danger"));
					return "admin/jobs-add";
				}else {
					return "redirect:/hrm/jobs/";
				}
			
		} catch (Exception e) {
			e.printStackTrace();
			model.addAttribute("jobs", jobs);
			session.setAttribute("message", new Message("Error : " + e.getMessage(), "alert-danger"));
			return "admin/jobs-add";
		}
	}
	@GetMapping("/history")
	public String jobsHistory(Model model){
		model.addAttribute("title", "Corpseed Dashboard | Old Jobs History");
		model.addAttribute("appendClass", "hrm");
		model.addAttribute("jobs", this.jobServices.getJobByStatus("3"));		
		return "admin/jobs-history";
	}
	@GetMapping("/history/delete/{uuid}")
	public String deleteJobHistory(@PathVariable("uuid") String uuid,Principal principal,HttpServletRequest req) {
		if(this.commonService.getLoginedUser(principal).getRole().equals("ROLE_ADMIN")) {
		Jobs job=this.jobServices.getjobByUuid(uuid);
		if(job!=null) {
			job.setDeleteStatus(1);
			this.jobServices.saveJobs(job);

			User user=this.commonService.getLoginedUser(principal);
			
			//adding category in history
			this.historyService.savehistory(new History(0, this.commonService.getUUID(),
					"Job", job.getId(), this.commonService.getBrowser(req), 
					this.commonService.getIpAddress(req), user.getId(), user.getFirstName()+" "+user.getLastName()
					, this.commonService.getToday(), this.commonService.getTime(),job.getJobTitle()));		
			
			//updating job application
			List<JobApplication> jobApplication=job.getJobApplication();
			if(!jobApplication.isEmpty()) {
				List<InterviewSchedule> interviewSchedule=new ArrayList<>();
				List<TrackApplication> trackApp=new ArrayList<>();
				List<CandidateDocuments> candidateDocument=new ArrayList<>();
				
				for (JobApplication jobApp : jobApplication) {
					jobApp.setDeleteStatus(1);
					
					if(!jobApp.getInterviewSchedule().isEmpty())
						interviewSchedule.addAll(jobApp.getInterviewSchedule());
					
					if(jobApp.getTrackApplication()!=null)
						trackApp.add(jobApp.getTrackApplication());
					
					if(!jobApp.getCandidateDocument().isEmpty())
						candidateDocument.addAll(jobApp.getCandidateDocument());					
				}
				if(!interviewSchedule.isEmpty()) {
					interviewSchedule.forEach(is->is.setDeleteStatus(1));
					this.interviewService.saveScheduledInterview(interviewSchedule);
				}
				if(!trackApp.isEmpty()) {
					trackApp.forEach(ta->ta.setDeleteStatus(1));
					this.trackAppService.saveTrackApplications(trackApp);
				}
				if(!candidateDocument.isEmpty()) {
					candidateDocument.forEach(cd->cd.setDeleteStatus(1));
					this.candDocService.saveDocuments(candidateDocument);
				}
				
				this.jobAppService.saveJobApplications(jobApplication);	
			}
			
		}
		
		return "redirect:/hrm/jobs/history";
		}else {
			return "error/403";
		}
	}
}
