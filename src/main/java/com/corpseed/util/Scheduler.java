package com.corpseed.util;

import java.util.ArrayList;
import java.util.List;

import com.corpseed.entity.*;
import com.corpseed.entity.enquiryentity.Enquiry;
import com.corpseed.entity.hrmentity.HrScreening;
import com.corpseed.entity.hrmentity.TechnicalRound;
import com.corpseed.entity.hrmentity.TrackApplication;
import com.corpseed.entity.industryentity.Industry;
import com.corpseed.entity.schedulerentity.EmailScheduler;
import com.corpseed.entity.serviceentity.Services;
import com.corpseed.service.OTPService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.corpseed.serviceImpl.blogserviceimpl.BlogService;
import com.corpseed.serviceImpl.CommonServices;
import com.corpseed.serviceImpl.enqserviceimpl.EnquiryService;
import com.corpseed.serviceImpl.HistoryService;
import com.corpseed.serviceImpl.IndustryService;
import com.corpseed.serviceImpl.servicesserviceimpl.ServicesService;
import com.corpseed.serviceImpl.hrmserviceimpl.TrackAppService;
import com.corpseed.serviceImpl.UserServices;
import com.corpseed.service.EmailSchedulerService;
import com.corpseed.service.newsservice.NewsService;

@Component
public class Scheduler {

	Logger log=LoggerFactory.getLogger(Scheduler.class);
	
	@Autowired
	private CommonServices commonServices;
	
	@Autowired
	private Environment env;
		
	@Autowired
	private ServicesService servicesService;
	
	@Autowired
	private EnquiryService enquiryService;
	
	@Autowired
	private CommonServices commonService;
	
	@Autowired
	private IndustryService industryService;
	
	@Autowired
	private HistoryService historyService;
		
	@Autowired
	private EmailSchedulerService emailService;
	
	@Autowired
	private EmailSender emailSender;
	
	@Autowired
	private TrackAppService trackAppService;
	
	@Autowired
	private UserServices userServices;
	
	@Autowired
	private BlogService blogService;
	
	@Autowired
	private NewsService newsService;

	@Autowired
	private OTPService optService;

	@Scheduled(fixedDelay = 5000)
	public void bitrixPush() {
		try {
			List<Enquiry> enquiry=this.enquiryService.findByDisplayStatusAndDeleteStatusAndBitrixStatus("1",2,2);
			if(!enquiry.isEmpty()) {				
				String POST_URL=env.getProperty("Bitrix24")+"crm.lead.add.json";
				for (Enquiry enq : enquiry) {
					String title="";
					int enqCount = this.enquiryService.findByEmailOrMobile(enq.getEmail(),enq.getMobile());						
					if(enqCount>1)title="(Duplicate-"+(enqCount-1)+") - ";
					if(enq.getType().equalsIgnoreCase("service")||enq.getType().equalsIgnoreCase("Chat")||enq.getType().equalsIgnoreCase("Service Details")) {
						
						if(enq.getServiceId()!=null&&!enq.getServiceId().equalsIgnoreCase("NA")&&enq.getServiceId().length()>0) {
							Services service=this.servicesService.findById(Long.parseLong(enq.getServiceId()));
							if(service!=null)title+=service.getServiceName()+" - ";
						} 
						title+="Service enquiry";
					}else if(enq.getType().equalsIgnoreCase("Industry")||enq.getType().equalsIgnoreCase("Industry Details")) {
						if(enq.getIndustryId()!=null&&!enq.getIndustryId().equalsIgnoreCase("NA")&&enq.getIndustryId().length()>0) {
							Industry industry=this.industryService.findByIndustryIdAndStatusNot(Long.parseLong(enq.getIndustryId()), "3");
							if(industry!=null)title+=industry.getIndustryName()+" - ";
						}
						title+="Industry enquiry";
					}else if(enq.getType().equalsIgnoreCase("Consult-Now")) {						
						title+="Consult Now";
					}else if(enq.getType().equalsIgnoreCase("Partner")) {						
						title+="Become a partner";
					}else if(enq.getType().equalsIgnoreCase("Call Back")) {						
						title+=enq.getMessage();
					}else if(enq.getType().equalsIgnoreCase("knowledge-centre")) {						
						title+=enq.getMessage()+" - Blog enquiry";
					}else if(enq.getType().equalsIgnoreCase("Contact-Us")) {						
						title+="Contact-Us";
					}else if(enq.getType().equalsIgnoreCase("IVR Call")) {						
						title+="IVR Call "+enq.getMobile();
					}else if(enq.getType().equalsIgnoreCase("Change Your CA")) {						
						title+="Change Your CA";
					}else {
						title+=enq.getType()+" - enquiry";
					}
										
					String POST_PARAMS="";
					String comment=enq.getMessage();
					if(comment==null||comment.length()<=0)comment="NA";
					if(enq.getEmail()!=null&&!enq.getEmail().equalsIgnoreCase("NA")&&enq.getEmail().length()>0)
						POST_PARAMS = "FIELDS[TITLE]="+title+"&FIELDS[NAME]="+enq.getName()+"&FIELDS[EMAIL][0][VALUE]="+enq.getEmail()+"&FIELDS[PHONE][0][VALUE]="+enq.getMobile()+"&FIELDS[COMMENTS]="+comment;
					else
						POST_PARAMS = "FIELDS[TITLE]="+title+"&FIELDS[NAME]="+enq.getName()+"&FIELDS[PHONE][0][VALUE]="+enq.getMobile()+"&FIELDS[COMMENTS]="+comment;
					int response = this.commonService.callPostURL(POST_URL, POST_PARAMS);
					
					if(response==200) {	
						enq.setBitrixStatus(1);
						this.enquiryService.saveEnquiry(enq);
					}
				}				
					
			}			
		} catch (Exception e) {
			log.error(e.getMessage());
			e.printStackTrace();
		}		
	}
	@Scheduled(cron = "0 0 1 * * ?",zone="IST")
	private void cleanTrash() {
		try {
		String today=this.commonServices.getToday();
		today=this.commonServices.dateBeforeDays(90); 
		List<History> history=this.historyService.findByDateBefore(today);
		if(!history.isEmpty()) {
			for (History h : history) {
				this.historyService.deleteData(h);
				Thread.sleep(1000);
			}
		}
		}catch(Exception e) {
			log.error(e.getMessage());
			e.printStackTrace();
		}
	}
	
	// for noon scheduler
	
	@Scheduled(cron = "0 30 13 * * ?",zone="IST")
	private void updateBlogRssFeed() {
		this.blogService.updateRssFeed();
	}

	@Scheduled(cron = "0 35 13 * * ?",zone="IST")
	private void updateSiteMap() {
		this.blogService.updateSiteMap();
	}
	
	@Scheduled(cron = "0 40 13 * * ?",zone="IST")
	private void updateNewsRssFeed() {
		this.newsService.updateNewsRssFeed();
	}
	
	// for midnight scheduler
	
	@Scheduled(cron = "0 0 1 * * ?",zone="IST")
	private void updateBlogRssFeed1() {
		this.blogService.updateRssFeed();
	}
	
	@Scheduled(cron = "0 5 1 * * ?",zone="IST")
	private void updateSiteMap1() {
		this.blogService.updateSiteMap();
	}
	
	@Scheduled(cron = "0 10 1 * * ?",zone="IST")
	private void updateNewsRssFeed1() {
		this.newsService.updateNewsRssFeed();
	}

	@Scheduled(fixedDelay = 10000)
	private void sendEmail() {
		try {
			List<EmailScheduler> emailTrash=new ArrayList<>();
			List<EmailScheduler> emails=this.emailService.findByStatus(2);
			if(!emails.isEmpty()) {
				emails.forEach(e->{
					if(e.getSendTo()!=null&&!e.getSendTo().equalsIgnoreCase("NA")) {
						boolean sendmail = this.emailSender.sendmail(e.getSendTo(), e.getEmailCC(), e.getSubject(), e.getMessage(), "NA");
						if (sendmail) emailTrash.add(e);
					}
				});
				if(!emailTrash.isEmpty()) {
					emailTrash.forEach(e->e.setStatus(1));
					this.emailService.updateEmails(emailTrash);
				}
//					this.emailService.cleanTrash(emailTrash);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Scheduled(cron = "0 30 18 * * ?",zone="IST")
	private void hrSendEmail() {
		try {
			String today=this.commonService.getToday();
			String message="<p>Hi Ma'am,</p><p>Following interviews has been aligned on "+today+" are:</p>"
					+ "<table border=\"1\" style=\"width:700px;min-width:700px;line-height: 40px;border-spacing: 0;font-family: sans-serif;text-align:center;\">"
					+ "<thead><tr><th>SN</th><th>HR</th><th>Candidate Name</th><th>Round</th><th>Interviewer</th><th>Date-Time</th></tr></thead>"
					+ "<tbody>";
			String email="hr@corpseed.com";	
			
			List<User> hr=this.userServices.findByRole("ROLE_HR_EXECUTIVE");
			boolean flag=false;
			int i=0;
			for (User u : hr) {						
			List<TrackApplication> trackApp=this.trackAppService.findTrackAppByDateAndHrStatus(u.getUuid(),today,2);
			List<HrScreening> screening=this.trackAppService.findHrScreeningByDateAndHrStatus(u.getUuid(),today,2);
			List<TechnicalRound> technical=this.trackAppService.findTechincalByDateAndHrStatus(u.getUuid(),today,2);
//			emails.add(new EmailScheduler(0, email, "empty", "Interview Round Scheduled for "+today, message, 2))
						
			if(!trackApp.isEmpty()) {				
				for (TrackApplication t : trackApp) {
					i++;
					User user=t.getUser();
					User addedBy=this.userServices.getUserByUuid(t.getAddedByUuid());
					message+="<tr><td>"+i+"</td><td>"+addedBy.getFirstName()+" "+addedBy.getLastName()+"</td><td>"+t.getJobApplication().getName()+"</td><td>Technical Round</td>"
							+ "<td>"+user.getFirstName()+" "+user.getLastName()+" : "+user.getDepartment()+"</td>"
									+ "<td>"+t.getDate()+"T"+t.getTime()+"</td></tr>";
					t.setHrEmailStatus(1);
				}
				this.trackAppService.saveTrackApplications(trackApp);
			}
			if(!technical.isEmpty()) {				
				for (TechnicalRound t : technical) {
					i++;
					User user=t.getUser();
					User addedBy=this.userServices.getUserByUuid(t.getAddedByUuid());
					message+="<tr><td>"+i+"</td><td>"+addedBy.getFirstName()+" "+addedBy.getLastName()+"</td><td>"+t.getTrackApplication().getJobApplication().getName()+"</td><td>Business Round</td>"
							+ "<td>"+user.getFirstName()+" "+user.getLastName()+" : "+user.getDepartment()+"</td>"
									+ "<td>"+t.getDateTime()+"</td></tr>";
					t.setHrEmailStatus(1);
				}
				this.trackAppService.saveAllTechnicalRound(technical);
			}
			if(!screening.isEmpty()) {				
				for (HrScreening s : screening) {
					i++;
					User user=s.getUser();
					User addedBy=this.userServices.getUserByUuid(s.getAddedByUuid());
					message+="<tr><td>"+i+"</td><td>"+addedBy.getFirstName()+" "+addedBy.getLastName()+"</td><td>"+s.getTrackApplication().getJobApplication().getName()+"</td><td>HR Manager Round</td>"
							+ "<td>"+user.getFirstName()+" "+user.getLastName()+" : "+user.getDepartment()+"</td>"
									+ "<td>"+s.getDateTime()+"</td></tr>";
					s.setHrEmailStatus(1);
				}
				this.trackAppService.saveAllHrScreening(screening);
			}
			if((!trackApp.isEmpty()||!screening.isEmpty()||!technical.isEmpty())&&!flag)
				flag=true;
			
			}
			
			message+="</tbody></table>";
			
			
			if(flag)
				this.emailService.saveEmail(new EmailScheduler(0, email, "empty", "Interview Round Scheduled on "+today, message, 2));
			
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Scheduled(fixedDelay = 600000)
	public void bitrixPushMissedOtp() {
		String today=this.commonService.getToday();
		String time=this.commonService.getTimeBeforeMinutes(10);
//		System.out.println("missed otp==="+today+"\n"+time);
		try {
			List<OTP> otpList=this.optService.fetchMissedOtp(today,time);
			if(!otpList.isEmpty()) {
				String POST_URL=env.getProperty("Bitrix24")+"crm.lead.add.json";
				for (OTP otp : otpList) {
					String title="";
					int enqCount = this.enquiryService.findByMobile(otp.getMobile());
					if(enqCount>1)title="(Duplicate-"+(enqCount-1)+") - ";

					title+="Missed OTP";

					String POST_PARAMS="";
					String comment=otp.getLocation();
					if(comment==null||comment.length()<=0)comment="NA";
					POST_PARAMS = "FIELDS[TITLE]="+title+"&FIELDS[NAME]="+otp.getName()+"&FIELDS[PHONE][0][VALUE]="+otp.getMobile()+"&FIELDS[COMMENTS]="+comment;
					int response = this.commonService.callPostURL(POST_URL, POST_PARAMS);

					if(response==200) {
						otp.setDeliveryStatus(1);
						this.optService.saveOtp(otp);
					}
				}

			}
		} catch (Exception e) {
			log.error(e.getMessage());
			e.printStackTrace();
		}
	}

}
