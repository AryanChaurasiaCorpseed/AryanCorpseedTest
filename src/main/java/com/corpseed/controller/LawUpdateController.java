package com.corpseed.controller;

import java.security.Principal;
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

import com.corpseed.entity.enquiryentity.Enquiry;
import com.corpseed.entity.History;
import com.corpseed.entity.LawUpdate;
import com.corpseed.entity.SiteMapUrl;
import com.corpseed.entity.User;
import com.corpseed.util.Message;
import com.corpseed.serviceImpl.CategoryService;
import com.corpseed.serviceImpl.CommonServices;
import com.corpseed.serviceImpl.enqserviceimpl.EnquiryService;
import com.corpseed.serviceImpl.HistoryService;
import com.corpseed.serviceImpl.IndustryService;
import com.corpseed.serviceImpl.LawUpdateService;
import com.corpseed.serviceImpl.servicesserviceimpl.ServicesService;
import com.corpseed.serviceImpl.SiteMapService;
import com.corpseed.serviceImpl.TriggerService;

@Controller
@RequestMapping("/admin/lawupdate")
public class LawUpdateController {

	@Autowired
	private CommonServices commonService;
	
	@Autowired
	private LawUpdateService lawUpdateService;
	
	@Autowired
	private CategoryService catService;
	
	@Autowired
	private EnquiryService enquiryService;
	
	@Autowired
	private TriggerService triggerService;
	
	@Autowired
	private SiteMapService siteMapService;
	
	@Autowired
	private HistoryService historyService;
	
	@Autowired
	private ServicesService servicesService;
	
	@Autowired
	private IndustryService industryService;
	
	@GetMapping("/")
	public String lawUpdate(Model model) {
		model.addAttribute("title", "Corpseed Dashboard | Manage Law Update");
		model.addAttribute("appendClass", "law");
		model.addAttribute("lawUpdate", this.lawUpdateService.getAllLawUpdates());
		return "admin/lawupdate";
	}
	@GetMapping("/add")
	public String lawUpdateAdd(Model model) {
		model.addAttribute("title", "Corpseed Dashboard | Add Law Update");
		model.addAttribute("appendClass", "law");
		model.addAttribute("lawUpdate", new LawUpdate());
		model.addAttribute("subCategory", this.catService.getAllSubCategory());
		model.addAttribute("services", this.servicesService.getAllServicesByStatus("1"));
		model.addAttribute("industries", this.industryService.getIndustryByDisplsyStatus("1"));
		return "admin/lawupdate-add";
	}
	@GetMapping("/delete/{uuid}")
	public String deleteLawUpdate(@PathVariable("uuid") String uuid,Principal principal,HttpServletRequest req) {
		if(this.commonService.getLoginedUser(principal).getRole().equals("ROLE_ADMIN")) {	
			LawUpdate lawUpdate=this.lawUpdateService.getLawUpdateByUuid(uuid);
			if(lawUpdate!=null) {
				lawUpdate.setDeleteStatus(1);
				this.lawUpdateService.saveLawUpdate(lawUpdate);

				User user=this.commonService.getLoginedUser(principal);
				
				//adding category in history
				this.historyService.savehistory(new History(0, this.commonService.getUUID(),
						"Law Update", lawUpdate.getId(), this.commonService.getBrowser(req), 
						this.commonService.getIpAddress(req), user.getId(), user.getFirstName()+" "+user.getLastName()
						, this.commonService.getToday(), this.commonService.getTime(),lawUpdate.getTitle()));		
				
				//updating sitemap url
				SiteMapUrl siteMap=this.siteMapService.findByTypeAndUid("Law Update", lawUpdate.getId());
				if(siteMap!=null) {
					siteMap.setStatus(2);
					this.siteMapService.saveUrl(siteMap);
				}
				
			}
			return "redirect:/admin/lawupdate/";
		}else {
			return "error/403";
		}
	}
	@GetMapping("/edit/{lawuuid}")
	public String lawUpdateEdit(@PathVariable("lawuuid") String lawuuid,Model model) {
		model.addAttribute("title", "Corpseed Dashboard | Edit Law Update");
		model.addAttribute("appendClass", "law");
		model.addAttribute("lawuuid", lawuuid);
		model.addAttribute("subCategory", this.catService.getAllSubCategory());
		LawUpdate lawupdate = this.lawUpdateService.getLawUpdateByUuid(lawuuid);
		model.addAttribute("services", this.servicesService.getAllServicesByStatus("1"));
		model.addAttribute("industries", this.industryService.getIndustryByDisplsyStatus("1"));
		
		String description=lawupdate.getSummary();
		while(description.contains("<iframe src=\"https://docs.google.com/gview?url=")||description.contains("<iframe src=\"http://docs.google.com/gview?url=")) {
			description = description.replace("<iframe src=\"http://docs.google.com/gview?url=", "pdfview=");
			description = description.replace("<iframe src=\"https://docs.google.com/gview?url=", "pdfview=");
			description = description.replace("&embedded=true\" frameborder=\"0\" webkitallowfullscreen mozallowfullscreen allowfullscreen style=\"width: 100%; height: 700px;\"></iframe>", "=pdfview");
			description=description.replace("&embedded=true\" frameborder=\"0\" webkitallowfullscreen mozallowfullscreen allowfullscreen style=\"width: 100%; height: 500px;\">","=pdfview");
		}	
		lawupdate.setSummary(description);
		
		model.addAttribute("lawUpdate", lawupdate);
		return "admin/lawupdate-edit";
	}
	
	@PostMapping("/update/{lawuuid}")
	public String updateLawUpdate(@Valid @ModelAttribute("lawUpdate") LawUpdate lawUpdate,BindingResult result,
			@PathVariable("lawuuid") String lawuuid,HttpSession session,Model model,Principal principal) {
		try {
			if(result.hasErrors()) {
				model.addAttribute("title", "Corpseed Dashboard | Edit Law Update");
				model.addAttribute("appendClass", "law");
				model.addAttribute("subCategory", this.catService.getAllSubCategory());
				model.addAttribute("lawUpdate", lawUpdate);
				session.setAttribute("message", new Message("Please fill out the blank fields !!", "alert-danger"));
				model.addAttribute("services", this.servicesService.getAllServicesByStatus("1"));
				model.addAttribute("industries", this.industryService.getIndustryByDisplsyStatus("1"));
				return "admin/lawupdate-edit";
			}
			if(lawUpdate.getCategory()==null) {
				model.addAttribute("subCategory", this.catService.getAllSubCategory());
				model.addAttribute("title", "Corpseed Dashboard | Edit Law Update");
				model.addAttribute("appendClass", "law");
				model.addAttribute("lawUpdate", lawUpdate);
				session.setAttribute("message", new Message("Please select category !!", "alert-danger"));
				model.addAttribute("services", this.servicesService.getAllServicesByStatus("1"));
				model.addAttribute("industries", this.industryService.getIndustryByDisplsyStatus("1"));
				return "admin/lawupdate-edit";
			}
			LawUpdate findLawUpdate=this.lawUpdateService.getLawUpdateByUuid(lawuuid);
			lawUpdate.setId(findLawUpdate.getId());
			lawUpdate.setUuid(findLawUpdate.getUuid());
			lawUpdate.setAddedByUUID(this.commonService.getLoginedUser(principal).getUuid());
			String summary=lawUpdate.getSummary();

			summary=summary.replace("<div class=\"table-width\">","");
			summary=summary.replace("</table>\r\n"
					+ "</div>","</table>");
			
			summary=summary.replace("<table","<div class='table-width'><table");
			summary=summary.replace("</table>","</table></div>");

			while(summary.contains("pdfview=")) {
				summary=summary.replace("pdfview=", "<iframe src=\"https://docs.google.com/gview?url=");
				summary=summary.replace("=pdfview", "&embedded=true\" frameborder=\"0\" webkitallowfullscreen mozallowfullscreen allowfullscreen style=\"width: 100%; height: 700px;\"></iframe>");
            }
			
			lawUpdate.setSummary(summary);
			LawUpdate saveLawUpdate=this.lawUpdateService.saveLawUpdate(lawUpdate);
			
			if(saveLawUpdate==null) {
				model.addAttribute("title", "Corpseed Dashboard | Edit Law Update");
				model.addAttribute("subCategory", this.catService.getAllSubCategory());
				model.addAttribute("appendClass", "law");
				model.addAttribute("lawUpdate", lawUpdate);
				session.setAttribute("message", new Message("Please fill out the blank fields !!", "alert-danger"));
				model.addAttribute("services", this.servicesService.getAllServicesByStatus("1"));
				model.addAttribute("industries", this.industryService.getIndustryByDisplsyStatus("1"));
				return "admin/lawupdate-edit";
			}
			//if law update url exist in sitemap then update otherwise add
			SiteMapUrl siteMap=this.siteMapService.findByTypeAndUid("Law Update", saveLawUpdate.getId());
			if(siteMap==null) {
				this.siteMapService.saveUrl(new SiteMapUrl(0, this.commonService.getUUID(), saveLawUpdate.getId(), "Law Update", "/law-update/"+saveLawUpdate.getSlug(), 1, this.commonService.getToday()));
			}else {
				siteMap.setUrl("/law-update/"+saveLawUpdate.getSlug());
				this.siteMapService.saveUrl(siteMap);
			}
			return "redirect:/admin/lawupdate/";
		
			
		} catch (Exception e) {
			model.addAttribute("subCategory", this.catService.getAllSubCategory());
			model.addAttribute("title", "Corpseed Dashboard | Edit Law Update");
			model.addAttribute("appendClass", "law");
			model.addAttribute("lawUpdate", lawUpdate);
			session.setAttribute("message", new Message("Error : " + e.getMessage(), "alert-danger"));
			model.addAttribute("services", this.servicesService.getAllServicesByStatus("1"));
			model.addAttribute("industries", this.industryService.getIndustryByDisplsyStatus("1"));
			return "admin/lawupdate-edit";
		}
	}
	
	@PostMapping("/add")
	public String saveLawUpdate(@Valid @ModelAttribute("lawUpdate") LawUpdate lawUpdate,BindingResult result,
			HttpSession session,Model model,Principal principal) {
		try {			
			if(result.hasErrors()) {
				model.addAttribute("subCategory", this.catService.getAllSubCategory());
				model.addAttribute("title", "Corpseed Dashboard | Add Law Update");
				model.addAttribute("appendClass", "law");
				model.addAttribute("lawUpdate", lawUpdate);
				session.setAttribute("message", new Message("Please fill out the blank fields !!", "alert-danger"));
				model.addAttribute("services", this.servicesService.getAllServicesByStatus("1"));
				model.addAttribute("industries", this.industryService.getIndustryByDisplsyStatus("1"));
				return "admin/lawupdate-add";
			}
			if(lawUpdate.getCategory()==null) {
				model.addAttribute("subCategory", this.catService.getAllSubCategory());
				model.addAttribute("title", "Corpseed Dashboard | Add Law Update");
				model.addAttribute("appendClass", "law");
				model.addAttribute("lawUpdate", lawUpdate);
				session.setAttribute("message", new Message("Please select category !!", "alert-danger"));
				model.addAttribute("services", this.servicesService.getAllServicesByStatus("1"));
				model.addAttribute("industries", this.industryService.getIndustryByDisplsyStatus("1"));
				return "admin/lawupdate-add";
			}
			lawUpdate.setUuid(this.commonService.getUUID());
			lawUpdate.setAddedByUUID(this.commonService.getLoginedUser(principal).getUuid());
			String summary=lawUpdate.getSummary();

			summary=summary.replace("<table","<div class='table-width'><table");
			summary=summary.replace("</table>","</table></div>");
			
			while(summary.contains("pdfview=")) {
				summary=summary.replace("pdfview=", "<iframe src=\"https://docs.google.com/gview?url=");
				summary=summary.replace("=pdfview", "&embedded=true\" frameborder=\"0\" webkitallowfullscreen mozallowfullscreen allowfullscreen style=\"width: 100%; height: 700px;\"></iframe>");
            }
			
			lawUpdate.setSummary(summary);
			LawUpdate saveLawUpdate=this.lawUpdateService.saveLawUpdate(lawUpdate);
			
			if(saveLawUpdate==null) {
				model.addAttribute("subCategory", this.catService.getAllSubCategory());
				model.addAttribute("title", "Corpseed Dashboard | Add Law Update");
				model.addAttribute("appendClass", "law");
				model.addAttribute("lawUpdate", lawUpdate);
				session.setAttribute("message", new Message("Please fill out the blank fields !!", "alert-danger"));
				model.addAttribute("services", this.servicesService.getAllServicesByStatus("1"));
				model.addAttribute("industries", this.industryService.getIndustryByDisplsyStatus("1"));
				return "admin/lawupdate-add";
			}
				//getting all enquiry which is related to this service Category
				List<Enquiry> enquiryList=this.enquiryService.getAllEnquiryByTypeAndStatusAndCategoryId("service","1",saveLawUpdate.getCategory().getId()+"");
				if(!enquiryList.isEmpty()) {
					this.triggerService.saveTriggers(enquiryList,saveLawUpdate.getSlug(),"/law-update/",saveLawUpdate.getTitle());
				}
				//saving law update url in sitemap
				this.siteMapService.saveUrl(new SiteMapUrl(0, this.commonService.getUUID(), saveLawUpdate.getId(), "Law Update", "/law-update/"+saveLawUpdate.getSlug(), 1, this.commonService.getToday()));
				return "redirect:/admin/lawupdate/";
			
			
		} catch (Exception e) {
			model.addAttribute("subCategory", this.catService.getAllSubCategory());
			model.addAttribute("title", "Corpseed Dashboard | Add Law Update");
			model.addAttribute("appendClass", "law");
			model.addAttribute("lawUpdate", lawUpdate);
			session.setAttribute("message", new Message("Error " + e.getMessage(), "alert-danger"));
			model.addAttribute("services", this.servicesService.getAllServicesByStatus("1"));
			model.addAttribute("industries", this.industryService.getIndustryByDisplsyStatus("1"));
			return "admin/lawupdate-add";
		}
	}
}
