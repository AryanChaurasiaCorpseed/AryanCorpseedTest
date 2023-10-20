package com.corpseed.controller.advicecontroller;

import com.corpseed.controller.userController.*;
import com.corpseed.entity.Category;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.ModelAttribute;

import com.corpseed.serviceImpl.CategoryService;
import com.corpseed.serviceImpl.FooterService;
import com.corpseed.serviceImpl.IndustryService;
import com.corpseed.serviceImpl.MasterService;

import java.util.List;

@ControllerAdvice(assignableTypes = {ApiController.class,HomeController.class,
		UserPressReleaseController.class, UserProductController.class, UserLifeController.class})
public class MenuController {

	@Autowired
	private CategoryService categoryService;
	
	@Autowired
	private MasterService masterService;
	
	@Autowired
	private IndustryService industryService;
	
	@Autowired
	private FooterService footerService;
	
	
	@ModelAttribute
	public void getMenu(Model model) {
		model.addAttribute("startCompany", this.categoryService.getCategoryByCategoryNameAndDisplayStatus("Start Company"));
		model.addAttribute("license", this.categoryService.getCategoryByCategoryNameAndDisplayStatus("License & Certification"));
		model.addAttribute("financial", this.categoryService.getCategoryByCategoryNameAndDisplayStatus("Financial Services"));
		model.addAttribute("compliance", this.categoryService.getCategoryByCategoryNameAndDisplayStatus("Business Compliance"));
		model.addAttribute("changesBusiness", this.categoryService.getCategoryByCategoryNameAndDisplayStatus("Changes In Business"));
		model.addAttribute("pollution", this.categoryService.getCategoryByCategoryNameAndDisplayStatus("Pollution Advisory"));
		model.addAttribute("footerCat", this.masterService.findByDisplayStatus("1"));
		model.addAttribute("industries", this.industryService.getIndustryByDisplsyStatus("1"));
		model.addAttribute("contact", this.masterService.getContactByStatus("1"));
		model.addAttribute("footer", this.footerService.findByDisplayStatus("1"));		
		
	}
	public void setCookies(@CookieValue(value = "username", defaultValue = "Unknown") String username,Model model) {
		try {
		if(!username.equals("Unknown")) {
			model.addAttribute("cookies", "2");
		}			
		} catch (Exception e) {
			// TODO: handle exception
		}
	}

}
