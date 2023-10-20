package com.corpseed.controller.hrmController;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.corpseed.serviceImpl.hrmserviceimpl.JobReportService;

@Controller
@RequestMapping("/hrm/reports")
public class ReportsController {

	@Autowired
	private JobReportService jobReportService;
	
	@GetMapping("/")
	public String reports(Model model) {
		model.addAttribute("title", "Corpseed Dashboard | Manage Reports");
		model.addAttribute("appendClass", "hrm");
		model.addAttribute("reports", this.jobReportService.findAll()); 
		return "admin/reports";
	}
}
