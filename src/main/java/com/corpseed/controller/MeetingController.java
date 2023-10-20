package com.corpseed.controller;

import java.security.Principal;

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
import com.corpseed.entity.Meeting;
import com.corpseed.entity.User;
import com.corpseed.util.Message;
import com.corpseed.serviceImpl.CommonServices;
import com.corpseed.serviceImpl.HistoryService;
import com.corpseed.serviceImpl.MeetingService;

@Controller
@RequestMapping("/admin/meeting")
public class MeetingController {

	@Autowired
	private MeetingService meetingService;
	
	@Autowired
	private CommonServices commonService;
	
	@Autowired
	private HistoryService historyService;
	
	@GetMapping("/")
	public String meeting(Model model) {
		model.addAttribute("appendClass", "meeting");
		model.addAttribute("meeting", this.meetingService.getAllMettings());
		return "admin/meeting";
	}
	@GetMapping("/add")
	public String addMeeting(Model model) {
		model.addAttribute("appendClass", "meeting");
		model.addAttribute("meeting", new Meeting());
		return "admin/meeting-add";
	}
	@GetMapping("/edit/{uuid}")
	public String editMeeting(@PathVariable("uuid") String uuid,Model model) {
		Meeting meeting=this.meetingService.getMeetingByUuid(uuid);
		model.addAttribute("meeting", meeting);
		model.addAttribute("appendClass", "meeting");
		return "admin/meeting-edit";
	}
	
	@GetMapping("/delete/{uuid}")
	public String deleteMeeting(@PathVariable("uuid") String uuid,Principal principal,
			HttpServletRequest req) {
		if(this.commonService.getLoginedUser(principal).getRole().equals("ROLE_ADMIN")) {
			Meeting meeting=this.meetingService.getMeetingByUuid(uuid);
			if(meeting!=null) { 
				meeting.setDeleteStatus(1);
				this.meetingService.saveMeeting(meeting);
				
				User user=this.commonService.getLoginedUser(principal);
				
				//adding category in history
				this.historyService.savehistory(new History(0, this.commonService.getUUID(),
						"Meeting", meeting.getId(), this.commonService.getBrowser(req), 
						this.commonService.getIpAddress(req), user.getId(), user.getFirstName()+" "+user.getLastName()
						, this.commonService.getToday(), this.commonService.getTime(),meeting.getName()));		
			}
			return "redirect:/admin/meeting/";
		}else {
			return "error/403";
		}
	}
	
	@PostMapping("/update")
	public String bookMettingUpdate(@Valid @ModelAttribute("meeting") Meeting meeting,BindingResult result,Model model,HttpSession session) {
		
		try {			
			if(result.hasErrors()) {
				model.addAttribute("meeting", meeting);
				session.setAttribute("message", new Message("Please fill out the blank fields !!", "alert-danger"));
				return "admin/meeting-edit";
			}
				Meeting findMeeting=this.meetingService.getMeetingByUuid(meeting.getUuid());
				meeting.setId(findMeeting.getId());
				Meeting saveMeeting=this.meetingService.bookMeeting(meeting);
				
				if(saveMeeting==null) {
					model.addAttribute("meeting", meeting);
					session.setAttribute("message", new Message("Data not saved, Please try-again later !!", "alert-danger"));
					return "admin/meeting-edit";
				}else {
					return "redirect:/admin/meeting/";
				}
						
		} catch (Exception e) {
			model.addAttribute("meeting", meeting);
			session.setAttribute("message", new Message("Error : "+e.getMessage(), "alert-danger"));
			return "admin/meeting-edit";
		}		
	}
	@PostMapping("/book")
	public String bookMettingSave(@Valid @ModelAttribute("meeting") Meeting meeting,BindingResult result,Model model,HttpSession session) {
		
		try {
			if(result.hasErrors()) {
				model.addAttribute("meeting", meeting);
				session.setAttribute("message", new Message("Please fill out the blank fields !!", "alert-danger"));
				return "admin/meeting-add";
			}
				meeting.setUuid(this.commonService.getUUID());
				Meeting saveMeeting=this.meetingService.bookMeeting(meeting);
				
				if(saveMeeting==null) {
					model.addAttribute("meeting", meeting);
					session.setAttribute("message", new Message("Data Not Saved, Please try-again later !!", "alert-danger"));
					return "admin/meeting-add";
				}else {
					return "redirect:/admin/meeting/";
				}
		} catch (Exception e) {
			model.addAttribute("meeting", meeting);
			session.setAttribute("message", new Message("Error"+e.getMessage(), "alert-danger"));
			return "admin/meeting-add";
		}		
	}
}
