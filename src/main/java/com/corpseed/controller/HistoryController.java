package com.corpseed.controller;

import java.security.Principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.corpseed.entity.History;
import com.corpseed.entity.User;
import com.corpseed.serviceImpl.CommonServices;
import com.corpseed.serviceImpl.HistoryService;

@Controller
@RequestMapping("/admin/trash")
public class HistoryController {

	@Autowired
	private HistoryService historyService;
	
	@Autowired
	private CommonServices commonService;
	
	@GetMapping("/")
	public String history(Model model) {
		model.addAttribute("title", "Corpseed Dashboard | Manage trash data");
		model.addAttribute("appendClass", "master");
		model.addAttribute("history",this.historyService.findAll() );
		return "admin/history";
	}
	
	@GetMapping("/restore/{uuid}")
	public String restore(@PathVariable("uuid") String uuid,Principal principal) {
		History history=this.historyService.findByUuid(uuid);
		if(history!=null) {
			User user=this.commonService.getLoginedUser(principal);
			this.historyService.restoreData(history,user);
		}
		return "redirect:/admin/trash/";
	}
	
	@GetMapping("/delete/{uuid}")
	public String delete(@PathVariable("uuid") String uuid) {
		History history=this.historyService.findByUuid(uuid);
		if(history!=null) {
			this.historyService.deleteData(history);
		}
		return "redirect:/admin/trash/";
	}
}
