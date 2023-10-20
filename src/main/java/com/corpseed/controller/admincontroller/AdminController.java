package com.corpseed.controller.admincontroller;

import com.corpseed.entity.TrandingSearch;
import com.corpseed.entity.Visitors;
import com.corpseed.serviceImpl.*;
import com.corpseed.serviceImpl.blogserviceimpl.BlogService;
import com.corpseed.serviceImpl.enqserviceimpl.EnquiryService;
import com.corpseed.serviceImpl.servicesserviceimpl.ServicesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/admin")
public class AdminController {
	
	@Autowired
	private UserServices userServices;
	
	@Autowired
	private BlogService blogService;
	
	@Autowired
	private ServicesService servicesService;
	
	@Autowired
	private CommonServices commonService;
	
	@Autowired
	private EnquiryService enquiryService;
	
	@Autowired
	private MasterService master5Service;
	
	@Autowired
	private VisitorsService visitorsService;
	
	@Autowired
	private TrandingSearchService trandingService;
		
	@GetMapping("/login")
	public String login() {
		return "admin/login";
	}
	
	@PostMapping("/doLogin")
	public String doLogin() {
		return "admin/index";
	}
	
	@GetMapping("/")
	public String dashBoard(Model model) {
		model.addAttribute("title", "Corpseed Dashboard | Dashboard");
		model.addAttribute("appendClass", "dashboard");
		model.addAttribute("totalEnquiry", this.enquiryService.countAllEnquiry());
		model.addAttribute("totalUsers", this.userServices.countAllUsers());
		model.addAttribute("totalBlogs", this.blogService.countAllBlogs());
		model.addAttribute("totalServices", this.servicesService.countAllServices());
		model.addAttribute("recentBlog", this.blogService.getRecentFiveBlogs());
				
		return "admin/index";
	}	
	@GetMapping("/GetSixRecentData")
	@ResponseBody
	public Map<String, String> getRecent6RecentData(@RequestParam("dataReq") String dataReq){
		Map<String, String> map = new HashMap<>();
		if(dataReq.equals("1")) {
			map= this.blogService.getRecentSixBlogs()
					.stream().collect(Collectors.toMap(b ->
							this.commonService.timeBetweenDates(
									b.getPostDate()) + "#" + b.getId(), b -> b.getTitle()));
		}
		else if(dataReq.equals("2")) {
			map=this.servicesService.getRecentSixServices()
					.stream().collect(Collectors.toMap(s->
							this.commonService.timeBetweenDates(s.getPostDate())+"#"+s.getId(),
							s->s.getServiceName()));
		}else if(dataReq.equals("3")) {
			map=this.master5Service.getRecentSixCmsPages()
					.stream().collect(Collectors.toMap(c->
							this.commonService.timeBetweenDates(c.getPostDate())+"#"+c.getId(),
							c->c.getTitle()));
		}
	      return map;
	}
	
	@GetMapping("/trandingSearchData")
	@ResponseBody
	public List<TrandingSearch> getTrandingSearchDate(){
		return this.trandingService.findTop5ByOrderBySearchedDesc();
	}	
	
	@GetMapping("/GetVisitorsChartData/{type}")
	@ResponseBody
	public List<Visitors> getVisitorsRecord(@PathVariable("type") String type){
		String date=this.commonService.currentWeekPassedSunday();
		if(type.equals("1")) {
			return this.visitorsService.findVisitorsByDate(date);
		}else if(type.equals("2")) {
			String date1=this.commonService.dateBeforeDaysFromDate(7,date);
			return this.visitorsService.findVisitorsByFromToDate(date1,date);
		}
		return null;
	}
}
