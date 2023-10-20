package com.corpseed.controller;

import java.io.PrintWriter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.corpseed.entity.Feedback;
import com.corpseed.service.FeedbackService;

@Controller
@RequestMapping("/admin/feedback")
public class FeedbackController {

	@Autowired
	private FeedbackService feedbackService;
	
	@GetMapping("/")
	public String feedbackHome(Model model) {
		model.addAttribute("title", "Corpseed Dashboard | Manage Blogs");
		model.addAttribute("appendClass", "master");
		model.addAttribute("feedbacks", this.feedbackService.findAll());	
		return "admin/feedback";
	}
	
	@GetMapping("/{slug}")
	public String feedbackByType(Model model,@PathVariable("slug") String slug) {
		model.addAttribute("title", "Corpseed Dashboard | Manage Blogs");
		model.addAttribute("appendClass", "master");
		model.addAttribute("feedbacks", this.feedbackService.findByType(slug));	
		model.addAttribute("slug", slug);
		return "admin/feedback";
	}
	
	@GetMapping("/view/{id}")
	public @ResponseBody void viewFeedback(@PathVariable("id") long id,PrintWriter pw) {
		Feedback feedback=this.feedbackService.findById(id);
		String response="";
		if(feedback!=null) {
			response="<p>"+feedback.getRatingValue()+"</p><p>Feedback Location : <a href='"+feedback.getFeedbackUrl()+"' target='_blank'>"+feedback.getFeedbackUrl()+"</a></p><p>"+feedback.getComment()+"</p>";
		}
		pw.write(response);
	}
	
	@GetMapping("/delete/{id}")
	public String deleteFeedback(@PathVariable("id") long id) {
		Feedback feedback=this.feedbackService.findById(id);
		if(feedback!=null)
			this.feedbackService.deleteFeedback(feedback);
		
		return "redirect:/admin/feedback/";
	}
}
