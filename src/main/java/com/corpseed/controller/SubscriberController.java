package com.corpseed.controller;

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

import com.corpseed.entity.History;
import com.corpseed.entity.Subscribers;
import com.corpseed.entity.TriggersList;
import com.corpseed.entity.User;
import com.corpseed.util.EmailSender;
import com.corpseed.util.Message;
import com.corpseed.serviceImpl.CommonServices;
import com.corpseed.serviceImpl.HistoryService;
import com.corpseed.serviceImpl.SubscriberService;
import com.corpseed.serviceImpl.TriggerService;

@Controller
@RequestMapping("/admin/subscriber")
public class SubscriberController {

	@Autowired
	private SubscriberService subscriberService;
	
	@Autowired
	private CommonServices commonService;
	
	@Autowired
	private TriggerService triggerService;
	
	@Autowired
	private EmailSender emailSender;
	
	@Autowired
	private HistoryService historyService;
	
	@GetMapping("/")
	public String subscribers(Model model) {
		model.addAttribute("title", "Corpseed Dashboard | Manage Subscribers");
		model.addAttribute("appendClass", "subscriber");
		model.addAttribute("subscribers",this.subscriberService.getAllSubscriber());
		return "admin/subscribers";
	}
	
	@GetMapping("/triggers/")
	public String subscriberTriggers(Model model) {
		model.addAttribute("title", "Corpseed Dashboard | Manage Triggers");
		model.addAttribute("appendClass", "subscriber");
		model.addAttribute("triggers",triggerService.getAllTriggers());
		return "admin/triggers";
	}
	
	@GetMapping("/trigger/delete/{uuid}")
	public String deleteTrigger(@PathVariable("uuid") String uuid,Principal principal,HttpServletRequest req) {
		if(this.commonService.getLoginedUser(principal).getRole().equals("ROLE_ADMIN")) {
			TriggersList trigger=this.triggerService.findByUuid(uuid);
			if(trigger!=null) {
				trigger.setDeleteStatus(1);
				this.triggerService.saveTrigger(trigger);
				
				User user=this.commonService.getLoginedUser(principal);
				
				//adding subscriber trigger
				this.historyService.savehistory(new History(0, this.commonService.getUUID(),
						"Trigger", trigger.getId(), this.commonService.getBrowser(req), 
						this.commonService.getIpAddress(req), user.getId(), user.getFirstName()+" "+user.getLastName()
						, this.commonService.getToday(), this.commonService.getTime(),trigger.getEmail()));		
			}
			return "redirect:/admin/subscriber/triggers/";
		}else {
			return "error/403";
		}
	}
	
	@GetMapping("/add")
	public String addSubscriber(Model model) {
		model.addAttribute("title", "Corpseed Dashboard | Add Subscriber");
		model.addAttribute("appendClass", "subscriber");
		model.addAttribute("subscriber", new Subscribers());
		return "admin/subscriber-add";
	}
	@GetMapping("/edit/{uuid}")
	public String editSubscriber(@PathVariable("uuid") String uuid,Model model) {
		model.addAttribute("title", "Corpseed Dashboard | Edit Subscriber");
		model.addAttribute("appendClass", "subscriber");
		model.addAttribute("subscriber", this.subscriberService.getSubscriberByUuid(uuid));
		return "admin/subscriber-edit";
	}
	
	@PostMapping("/add")
	public String saveSubscriber(@Valid @ModelAttribute("subscriber") Subscribers subscriber,BindingResult result,
			Model model,HttpSession session) {
		try {
			if(result.hasErrors()) {
				model.addAttribute("title", "Corpseed Dashboard | Add Subscriber");
				model.addAttribute("appendClass", "subscriber");
				session.setAttribute("message", new Message("Please fill out the blank fields", "alert-danger"));
				model.addAttribute("subscriber", subscriber);
				return "admin/subscriber-add";
			}
				Subscribers findSubscribers=this.subscriberService.findByEmail(subscriber.getEmail());
				if(findSubscribers!=null) {
					model.addAttribute("title", "Corpseed Dashboard | Add Subscriber");
					model.addAttribute("appendClass", "subscriber");
					if(findSubscribers.getDeleteStatus()==1)
						session.setAttribute("message", new Message(subscriber.getEmail()+" exist in Trash ", "alert-danger"));
					else
						session.setAttribute("message", new Message(subscriber.getEmail()+" already subscribed", "alert-danger"));
					
					model.addAttribute("subscriber", subscriber);
					return "admin/subscriber-add";
				}
					subscriber.setUuid(this.commonService.getUUID());
					Subscribers saveSubscriber=this.subscriberService.saveSubscriber(subscriber);
					if(saveSubscriber==null) {
						model.addAttribute("title", "Corpseed Dashboard | Add Subscriber");
						model.addAttribute("appendClass", "subscriber");
						model.addAttribute("subscriber", subscriber);
						session.setAttribute("message", new Message("Data Not Saved, Please try-again later !!", "alert-danger"));
						return "admin/subscriber-add";
					}
					return "redirect:/admin/subscriber/";
					
		} catch (Exception e) {
			model.addAttribute("title", "Corpseed Dashboard | Add Subscriber");
			model.addAttribute("appendClass", "subscriber");
			model.addAttribute("subscriber", subscriber);
			session.setAttribute("message", new Message("Error : "+e.getMessage(), "alert-danger"));
			return "admin/subscriber-add";
		}
	}
	@PostMapping("/update")
	public String updateSubscriber(@Valid @ModelAttribute("subscriber") Subscribers subscriber,BindingResult result,
			Model model,HttpSession session) {
		try {
			if(result.hasErrors()) {
				model.addAttribute("title", "Corpseed Dashboard | Edit Subscriber");
				model.addAttribute("appendClass", "subscriber");
				session.setAttribute("message", new Message("Please fill out the blank fields", "alert-danger"));
				model.addAttribute("subscriber", subscriber);
				return "admin/subscriber-edit";
			}
				Subscribers findSubscribers=this.subscriberService.findByEmailAndUuidNot(subscriber.getEmail(),subscriber.getUuid());
				if(findSubscribers!=null) {
					model.addAttribute("title", "Corpseed Dashboard | Edit Subscriber");
					model.addAttribute("appendClass", "subscriber");
					
					if(findSubscribers.getDeleteStatus()==1)
						session.setAttribute("message", new Message(subscriber.getEmail()+" exist in Trash.", "alert-danger"));
					else
						session.setAttribute("message", new Message(subscriber.getEmail()+" already subscribed.", "alert-danger"));
					
					model.addAttribute("subscriber", subscriber);
					return "admin/subscriber-edit";
				}
				findSubscribers=this.subscriberService.getSubscriberByUuid(subscriber.getUuid());
				subscriber.setId(findSubscribers.getId());
				Subscribers saveSubscriber=this.subscriberService.saveSubscriber(subscriber);
				if(saveSubscriber==null) {
					model.addAttribute("title", "Corpseed Dashboard | Edit Subscriber");
					model.addAttribute("appendClass", "subscriber");
					model.addAttribute("subscriber", subscriber);
					session.setAttribute("message", new Message("Data Not Saved, Please try-again later !!", "alert-danger"));
					return "admin/subscriber-edit";
				}
				return "redirect:/admin/subscriber/";
					
		} catch (Exception e) {
			model.addAttribute("title", "Corpseed Dashboard | Edit Subscriber");
			model.addAttribute("appendClass", "subscriber");
			model.addAttribute("subscriber", subscriber);
			session.setAttribute("message", new Message("Error : "+e.getMessage(), "alert-danger"));
			return "admin/subscriber-edit";
		}
	}
	@GetMapping("/delete/{uuid}")
	public String deleteSubscriber(@PathVariable("uuid") String uuid,Principal principal,HttpServletRequest req) {
		if(this.commonService.getLoginedUser(principal).getRole().equals("ROLE_ADMIN")) {
			Subscribers subscriber=this.subscriberService.getSubscriberByUuid(uuid);
			if(subscriber!=null) {
				subscriber.setDeleteStatus(1);
				this.subscriberService.saveSubscriber(subscriber);
				
				User user=this.commonService.getLoginedUser(principal);
				
				//adding category in history
				this.historyService.savehistory(new History(0, this.commonService.getUUID(),
						"Subscriber", subscriber.getId(), this.commonService.getBrowser(req), 
						this.commonService.getIpAddress(req), user.getId(), user.getFirstName()+" "+user.getLastName()
						, this.commonService.getToday(), this.commonService.getTime(),subscriber.getEmail()));		
			}
			return "redirect:/admin/subscriber/";
		}else {
			return "error/403";
		}
	}
	@GetMapping("/trigger/send-all/")
	public String sendAllTrigger() {
		List<TriggersList> triggerList=this.triggerService.getTriggerByStatus("1");
		if(!triggerList.isEmpty()) {
			List<TriggersList> trgList=new ArrayList<>();
		for (TriggersList trigger : triggerList) {
			String message="<a href='"+trigger.getUrl()+"'>"+trigger.getUpdateName()+"</a>";
			boolean sendmail = this.emailSender.sendmail(trigger.getEmail(),"empty", trigger.getUpdateName(),message,"NA");
			if(sendmail) {
				trgList.add(trigger);				
			}
		}
		if(!trgList.isEmpty())
			this.triggerService.deleteByList(trgList);
		}
		return "redirect:/admin/subscriber/triggers/";
	}
	
	@GetMapping("/trigger/send/{uuid}")
	public String sendTrigger(@PathVariable("uuid") String uuid){
		TriggersList trigger=this.triggerService.findByUuid(uuid);
		if(trigger!=null) {
			String message="<a href='"+trigger.getUrl()+"'>"+trigger.getUpdateName()+"</a>";
			boolean sendmail = this.emailSender.sendmail(trigger.getEmail(),"empty", trigger.getUpdateName(),message,"NA");
			if(sendmail)
				triggerService.deleteById(trigger.getId());
		}
		return "redirect:/admin/subscriber/triggers/";
	}
}
