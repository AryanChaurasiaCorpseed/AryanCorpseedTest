package com.corpseed.controller;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.corpseed.entity.City;
import com.corpseed.entity.CmsPages;
import com.corpseed.entity.contactentity.Contact;
import com.corpseed.entity.contactentity.ContactAddress;
import com.corpseed.entity.Country;
import com.corpseed.entity.enquiryentity.Enquiry;
import com.corpseed.entity.footerentity.FooterCategory;
import com.corpseed.entity.History;
import com.corpseed.entity.HotTags;
import com.corpseed.entity.Review;
import com.corpseed.entity.serviceentity.Services;
import com.corpseed.entity.SiteMapUrl;
import com.corpseed.entity.State;
import com.corpseed.entity.StaticSeo;
import com.corpseed.entity.User;
import com.corpseed.entity.Visitors;
import com.corpseed.util.Message;
import com.corpseed.serviceImpl.AzureBlobAdapter;
import com.corpseed.serviceImpl.CommonServices;
import com.corpseed.serviceImpl.enqserviceimpl.EnquiryService;
import com.corpseed.serviceImpl.FooterService;
import com.corpseed.serviceImpl.HistoryService;
import com.corpseed.serviceImpl.HotTagService;
import com.corpseed.serviceImpl.IndustryService;
import com.corpseed.serviceImpl.MasterService;
import com.corpseed.serviceImpl.servicesserviceimpl.ServicesService;
import com.corpseed.serviceImpl.SiteMapService;
import com.corpseed.serviceImpl.StaticSeoService;
import com.corpseed.serviceImpl.VisitorsService;

@Controller
@RequestMapping("/admin/master")
public class MasterController {
	
	@Autowired
	private CommonServices commonServices;
	
	@Autowired
	private MasterService masterService;
	
	@Autowired
    AzureBlobAdapter azureAdapter;
	
	@Autowired
	private HotTagService hotTagService;
	
	@Autowired
	private ServicesService servicesService;
	
	@Autowired
	private VisitorsService visitorsService;
	
	@Autowired
	private StaticSeoService staticSeoService;
	
	@Autowired
	private SiteMapService siteMapService;
	
	@Autowired
	private FooterService footerService;

	@Autowired
	private HistoryService historyService;
	
	@Autowired
	private EnquiryService enquiryService;
	
	@Autowired
	private IndustryService industryService;
	
	@GetMapping("/GetStateByCountry")
	public @ResponseBody Map<Long, String> getStateByCountryId(@RequestParam("countryId") String countryId) {
		Country country=this.masterService.getCountryById(countryId);
		return this.masterService.getAllStatesByCountryId(country)
				.stream().collect(Collectors.toMap(s->s.getId(),s->s.getStateName()));
	}

	@GetMapping("/footer")
	public String footer(Model model) {
		model.addAttribute("title", "Corpseed Dashboard | Manage Footer Category and Services");
		model.addAttribute("appendClass", "master");
		model.addAttribute("footer", this.footerService.findAll());
		return "admin/footer";
	}
	
	@GetMapping("/footer/add")
	public String footerAdd(Model model) {
		model.addAttribute("title", "Corpseed Dashboard | Add Footer");
		model.addAttribute("appendClass", "master");
		model.addAttribute("footer", new FooterCategory());
		return "admin/footer-add";
	}

	@GetMapping("/footer/edit/{uuid}")
	public String footerEdit(@PathVariable("uuid") String uuid,Model model) {
		model.addAttribute("title", "Corpseed Dashboard | Edit Footer");
		model.addAttribute("appendClass", "master");
		model.addAttribute("footer", this.footerService.findByUuid(uuid));
		return "admin/footer-edit";
	}
	
	@GetMapping("/footer/delete/{uuid}")
	public String deleteFooter(@PathVariable("uuid") String uuid,Principal principal,HttpServletRequest req) {
		if(this.commonServices.getLoginedUser(principal).getRole().equals("ROLE_ADMIN")) {
			FooterCategory footer=this.footerService.findByUuid(uuid);
			if(footer!=null) {				
				footer.setDeleteStatus(1);
				this.footerService.saveFooter(footer);

				User user=this.commonServices.getLoginedUser(principal);
				
				//adding category in history
				this.historyService.savehistory(new History(0, this.commonServices.getUUID(),
						"Footer", footer.getId(), this.commonServices.getBrowser(req), 
						this.commonServices.getIpAddress(req), user.getId(), user.getFirstName()+" "+user.getLastName()
						, this.commonServices.getToday(), this.commonServices.getTime(),footer.getCategoryName()));		
				
				//updating footer service
				List<com.corpseed.entity.footerentity.FooterService> footerService=footer.getFooterService();
				if(!footerService.isEmpty()) {
					footerService.forEach(fs->fs.setDeleteStatus(1));
					this.footerService.saveFooterService(footerService);
				}
			}
			return "redirect:/admin/master/footer/";
		}else {
			return "error/403";
		}
	}

	@PostMapping("/footer/edit")
	public String updateFooter(@Valid @ModelAttribute("footer") FooterCategory footer,BindingResult result,
			@RequestParam("servicesId") List<String> serviceId,Model model,HttpSession session) {
		try {
			if(result.hasErrors()) {
				model.addAttribute("title", "Corpseed Dashboard | Edit Footer");
				model.addAttribute("appendClass", "master");
				model.addAttribute("footer", result);
				session.setAttribute("message", new Message("Please fill out the blank fields !!", "alert-danger"));
				return "admin/footer-edit";
			}
			FooterCategory fFCategory=this.footerService.findByCategoryNameAndUuidNot(footer.getCategoryName(),footer.getUuid());
			if(fFCategory!=null) {
				model.addAttribute("title", "Corpseed Dashboard | Edd Footer");
				model.addAttribute("appendClass", "master");
				model.addAttribute("footer", result);
				if(fFCategory.getDeleteStatus()==1)
					session.setAttribute("message", new Message("Footer Category exist Trash!!", "alert-danger"));
				else
					session.setAttribute("message", new Message("Category already exist in footer section!!", "alert-danger"));
				
				return "admin/footer-edd";
			}
			FooterCategory saveFooter=this.footerService.saveFooter(footer);
			
			List<com.corpseed.entity.footerentity.FooterService> footerService=new ArrayList<>();
			for (String serviceUuid : serviceId) {
				Services service=this.servicesService.findByUUID(serviceUuid);
				if(service!=null) {
					com.corpseed.entity.footerentity.FooterService fs=this.footerService.findByFooterCategoryAndServices(saveFooter,service);
					if(fs==null)
						footerService.add(new com.corpseed.entity.footerentity.FooterService(0, this.commonServices.getUUID(), service, saveFooter, this.commonServices.getToday()));
					else footerService.add(fs);
				}
			}
		
			if(!footerService.isEmpty()) {
				this.footerService.saveFooterService(footerService);				
			}
			
			List<com.corpseed.entity.footerentity.FooterService> allFooterService=this.footerService.findByFooterCategory(saveFooter);
			allFooterService.removeAll(footerService);
			if(!allFooterService.isEmpty())
				this.footerService.removeAll(allFooterService);
			
			return "redirect:/admin/master/footer";
			
		} catch (Exception e) {
			e.printStackTrace();
			model.addAttribute("title", "Corpseed Dashboard | Add Footer");
			model.addAttribute("appendClass", "master");
			model.addAttribute("footer", result);
			session.setAttribute("message", new Message("Error : "+e.getMessage(), "alert-danger"));
			return "admin/footer-add";
		}
	}
	
	@PostMapping("/footer/add")
	public String saveFooter(@Valid @ModelAttribute("footer") FooterCategory footer,BindingResult result,
			@RequestParam("servicesId") List<String> serviceId,Model model,HttpSession session) {
		try {
			if(result.hasErrors()) {
				model.addAttribute("title", "Corpseed Dashboard | Add Footer");
				model.addAttribute("appendClass", "master");
				model.addAttribute("footer", result);
				session.setAttribute("message", new Message("Please fill out the blank fields !!", "alert-danger"));
				return "admin/footer-add";
			}
			FooterCategory fFCategory=this.footerService.findByCategoryName(footer.getCategoryName());
			if(fFCategory!=null) {
				model.addAttribute("title", "Corpseed Dashboard | Add Footer");
				model.addAttribute("appendClass", "master");
				model.addAttribute("footer", result);
				if(fFCategory.getDeleteStatus()==1)
					session.setAttribute("message", new Message("Footer Category exist in Trash !!", "alert-danger"));
				else
					session.setAttribute("message", new Message("Category already exist in footer section !!", "alert-danger"));
				
				return "admin/footer-add";
			}
			footer.setUuid(this.commonServices.getUUID());
			FooterCategory saveFooter=this.footerService.saveFooter(footer);
			
			List<com.corpseed.entity.footerentity.FooterService> footerService=new ArrayList<>();
			for (String serviceUuid : serviceId) {
				Services service=this.servicesService.findByUUID(serviceUuid);
				if(service!=null)
					footerService.add(new com.corpseed.entity.footerentity.FooterService(0, this.commonServices.getUUID(), service, saveFooter, this.commonServices.getToday()));
			}
		
			if(!footerService.isEmpty())this.footerService.saveFooterService(footerService);
			
			return "redirect:/admin/master/footer";
			
		} catch (Exception e) {
			e.printStackTrace();
			model.addAttribute("title", "Corpseed Dashboard | Add Footer");
			model.addAttribute("appendClass", "master");
			model.addAttribute("footer", result);
			session.setAttribute("message", new Message("Error : "+e.getMessage(), "alert-danger"));
			return "admin/footer-add";
		}
	}
	
	@GetMapping("/category-service")
	@ResponseBody
	public Map<String, String> search(@RequestParam("category") String category) {
		return this.servicesService.findByCategoryName(category)
				.stream().collect(Collectors.toMap(s->s.getUuid(),s->s.getServiceName()));
	}
	
	@GetMapping("/pages")
	public String cmsPages(Model model) {
		model.addAttribute("title", "Corpseed Dashboard | Manage CMS Pages");
		model.addAttribute("appendClass", "master");
		model.addAttribute("pages", this.masterService.getAllCmsPages());
		return "admin/pages";
	}
	@GetMapping("/pages/delete/{uuid}")
	public String deleteCmsPages(@PathVariable("uuid") String uuid,Principal principal,HttpServletRequest req) {
		if(this.commonServices.getLoginedUser(principal).getRole().equals("ROLE_ADMIN")) {
			CmsPages pages=this.masterService.getCmsPageByUuid(uuid);
			if(pages!=null) {
				pages.setDeleteStatus(1);
				this.masterService.saveCmsPages(pages);

				User user=this.commonServices.getLoginedUser(principal);
				
				//adding category in history
				this.historyService.savehistory(new History(0, this.commonServices.getUUID(),
						"CMS Page", pages.getId(), this.commonServices.getBrowser(req), 
						this.commonServices.getIpAddress(req), user.getId(), user.getFirstName()+" "+user.getLastName()
						, this.commonServices.getToday(), this.commonServices.getTime(),pages.getTitle()));		
					
			SiteMapUrl siteMap=this.siteMapService.findByTypeAndUid("CMS Page", pages.getId());
			if(siteMap!=null) {
				siteMap.setStatus(2);
				this.siteMapService.saveUrl(siteMap);
			}
			}
			return "redirect:/admin/master/pages/";
		}else {
			return "error/403";
		}
	}
	@GetMapping("/pages/add")
	public String pagesAdd(Model model) {
		model.addAttribute("title", "Corpseed Dashboard | Add CMS Pages");
		model.addAttribute("appendClass", "master");
		model.addAttribute("cmsPages", new CmsPages());
		model.addAttribute("services", this.servicesService.getAllServicesByStatus("1"));
		model.addAttribute("industries", this.industryService.getIndustryByDisplsyStatus("1"));
		return "admin/pages-add";
	}
	
	@GetMapping("/pages/edit/{pageuuid}")
	public String editJobApplication(@PathVariable("pageuuid") String pageuuid,Model model) {
		model.addAttribute("title", "Corpseed Dashboard | Edit CMS Pages");
		model.addAttribute("appendClass", "master");
		model.addAttribute("services", this.servicesService.getAllServicesByStatus("1"));
		model.addAttribute("industries", this.industryService.getIndustryByDisplsyStatus("1"));
		CmsPages cmsPageByUuid = this.masterService.getCmsPageByUuid(pageuuid);
		
		String description=cmsPageByUuid.getDescription();
		while(description.contains("<iframe src=\"https://docs.google.com/gview?url=")||description.contains("<iframe src=\"http://docs.google.com/gview?url=")) {
			description = description.replace("<iframe src=\"http://docs.google.com/gview?url=", "pdfview=");
			description = description.replace("<iframe src=\"https://docs.google.com/gview?url=", "pdfview=");
			description = description.replace("&embedded=true\" frameborder=\"0\" webkitallowfullscreen mozallowfullscreen allowfullscreen style=\"width: 100%; height: 700px;\"></iframe>", "=pdfview");
			description=description.replace("&embedded=true\" frameborder=\"0\" webkitallowfullscreen mozallowfullscreen allowfullscreen style=\"width: 100%; height: 500px;\">","=pdfview");
		}	
		cmsPageByUuid.setDescription(description);
		
		model.addAttribute("cmsPages", cmsPageByUuid);
		model.addAttribute("pageuuid", pageuuid);
		return "admin/pages-edit";
	}
	
	@PostMapping("/pages/update/{pageuuid}")
	public String updateCmsPages(@Valid @ModelAttribute("cmsPages") CmsPages cmsPages,BindingResult result,
			@PathVariable("pageuuid") String pageuuid,HttpSession session,Model model,Principal principal) {
		try {			
			if(result.hasErrors()) {
				model.addAttribute("title", "Corpseed Dashboard | Edit CMS Pages");
				model.addAttribute("appendClass", "master");
				model.addAttribute("cmsPages", cmsPages);
				session.setAttribute("message", new Message("Please fill out the blank fields !!", "alert-danger"));
				model.addAttribute("services", this.servicesService.getAllServicesByStatus("1"));
				model.addAttribute("industries", this.industryService.getIndustryByDisplsyStatus("1"));
				return "admin/pages-edit";
			}
			CmsPages findCmsPages=this.masterService.getCmsPageByUuid(pageuuid);
			CmsPages existCmsPage=this.masterService.findBySlugAndUuidNot(cmsPages.getSlug(),findCmsPages.getUuid());
			if(existCmsPage!=null) {
				model.addAttribute("title", "Corpseed Dashboard | Edit CMS Pages");
				model.addAttribute("appendClass", "master");
				model.addAttribute("cmsPages", cmsPages);
				if(existCmsPage.getDeleteStatus()==1)
					session.setAttribute("message", new Message("CMS Page exist in Trash !!", "alert-danger"));
				else
					session.setAttribute("message", new Message("CMS Page already exist !!", "alert-danger"));
				model.addAttribute("services", this.servicesService.getAllServicesByStatus("1"));
				model.addAttribute("industries", this.industryService.getIndustryByDisplsyStatus("1"));
				return "admin/pages-edit";
			}			
			cmsPages.setId(findCmsPages.getId());
			cmsPages.setUuid(findCmsPages.getUuid());
			cmsPages.setAddedByUUID(this.commonServices.getLoginedUser(principal).getUuid());
			String description=cmsPages.getDescription();
			
			description=description.replace("<div class=\"table-width\">","");
			description=description.replace("</table>\r\n"
					+ "</div>","</table>");

			description=description.replace("<table","<div class='table-width'><table");
			description=description.replace("</table>","</table></div>");
			
			while(description.contains("pdfview=")) {
            	description=description.replace("pdfview=", "<iframe src=\"https://docs.google.com/gview?url=");
            	description=description.replace("=pdfview", "&embedded=true\" frameborder=\"0\" webkitallowfullscreen mozallowfullscreen allowfullscreen style=\"width: 100%; height: 700px;\"></iframe>");
            }
			
			cmsPages.setDescription(description);
			CmsPages saveCmsPages=this.masterService.saveCmsPages(cmsPages);
			if(saveCmsPages==null) {
				model.addAttribute("title", "Corpseed Dashboard | Edit CMS Pages");
				model.addAttribute("appendClass", "master");
				model.addAttribute("cmsPages", cmsPages);
				session.setAttribute("message", new Message("Data Not saved, Please try-again later !!", "alert-danger"));
				model.addAttribute("services", this.servicesService.getAllServicesByStatus("1"));
				model.addAttribute("industries", this.industryService.getIndustryByDisplsyStatus("1"));
				return "admin/pages-edit";
			}
			//if sitemap url exist then update otherwise add
			SiteMapUrl siteMap=this.siteMapService.findByTypeAndUid("CMS Page", saveCmsPages.getId());
			if(siteMap==null)
					this.siteMapService.saveUrl(new SiteMapUrl(0, this.commonServices.getUUID(), saveCmsPages.getId(), "CMS Page", "/pages/"+saveCmsPages.getSlug(), 1, this.commonServices.getToday()));
			else {
				siteMap.setUrl("/pages/"+saveCmsPages.getSlug());
				this.siteMapService.saveUrl(siteMap);
			}
			return "redirect:/admin/master/pages";
			
			
		} catch (Exception e) {e.printStackTrace();
			model.addAttribute("title", "Corpseed Dashboard | Edit CMS Pages");
			model.addAttribute("appendClass", "master");
			model.addAttribute("cmsPages", cmsPages);
			session.setAttribute("message", new Message("Error : " + e.getMessage(), "alert-danger"));
			model.addAttribute("services", this.servicesService.getAllServicesByStatus("1"));
			model.addAttribute("industries", this.industryService.getIndustryByDisplsyStatus("1"));
			return "admin/pages-edit";
		}
	}
	
	@PostMapping("/pages/add")
	public String saveCmsPages(@Valid @ModelAttribute("cmsPages") CmsPages cmsPages,BindingResult result,
			HttpSession session,Model model,Principal principal) {
		try {
			model.addAttribute("title", "Corpseed Dashboard | Add CMS Pages");
			model.addAttribute("appendClass", "master");
			if(result.hasErrors()) {
				model.addAttribute("cmsPages", cmsPages);
				session.setAttribute("message", new Message("Please fill out the blank fields !!", "alert-danger"));
				model.addAttribute("services", this.servicesService.getAllServicesByStatus("1"));
				model.addAttribute("industries", this.industryService.getIndustryByDisplsyStatus("1"));
				return "admin/pages-add";
			}
			CmsPages findBySlug = this.masterService.findBySlug(cmsPages.getSlug());
			if(findBySlug!=null) {
				model.addAttribute("title", "Corpseed Dashboard | Add CMS Pages");
				model.addAttribute("appendClass", "master");
				model.addAttribute("cmsPages", cmsPages);
				if(findBySlug.getDeleteStatus()==1)
					session.setAttribute("message", new Message("CMS Page exist in Trash !!", "alert-danger"));
				else
					session.setAttribute("message", new Message("CMS Page Already exist !!", "alert-danger"));
				model.addAttribute("services", this.servicesService.getAllServicesByStatus("1"));
				model.addAttribute("industries", this.industryService.getIndustryByDisplsyStatus("1"));
				return "admin/pages-add";
			}
			cmsPages.setUuid(this.commonServices.getUUID());
			cmsPages.setAddedByUUID(this.commonServices.getLoginedUser(principal).getUuid());
			String description=cmsPages.getDescription();

			description=description.replace("<table","<div class='table-width'><table");
			description=description.replace("</table>","</table></div>");
			
			while(description.contains("pdfview=")) {
            	description=description.replace("pdfview=", "<iframe src=\"https://docs.google.com/gview?url=");
            	description=description.replace("=pdfview", "&embedded=true\" frameborder=\"0\" webkitallowfullscreen mozallowfullscreen allowfullscreen style=\"width: 100%; height: 700px;\"></iframe>");
            }
			
			cmsPages.setDescription(description);
			CmsPages saveCmsPages=this.masterService.saveCmsPages(cmsPages);
			if(saveCmsPages==null) {
				model.addAttribute("cmsPages", cmsPages);
				session.setAttribute("message", new Message("Data Not Saved, Please try-again later !!", "alert-danger"));
				model.addAttribute("services", this.servicesService.getAllServicesByStatus("1"));
				model.addAttribute("industries", this.industryService.getIndustryByDisplsyStatus("1"));
				return "admin/pages-add";
			}
			//add sitemap url
			this.siteMapService.saveUrl(new SiteMapUrl(0, this.commonServices.getUUID(), saveCmsPages.getId(), "CMS Page", "/pages/"+saveCmsPages.getSlug(), 1, this.commonServices.getToday()));
			
			return "redirect:/admin/master/pages";
			
		} catch (Exception e) {
			model.addAttribute("cmsPages", cmsPages);
			session.setAttribute("message", new Message("Error : " + e.getMessage(), "alert-danger"));
			model.addAttribute("services", this.servicesService.getAllServicesByStatus("1"));
			model.addAttribute("industries", this.industryService.getIndustryByDisplsyStatus("1"));
			return "admin/pages-add";
		}
	}

	
	@GetMapping("/enquiry")
	public String enquiry(Model model,@RequestParam("page") Optional<Integer> page, 
		      @RequestParam("size") Optional<Integer> size,@RequestParam("col") Optional<String> col,
		      @RequestParam("ord") Optional<String> ord,@RequestParam("from") Optional<String> from,
		      @RequestParam("to") Optional<String> to) {
		int currentPage = page.orElse(1);
        int pageSize = size.orElse(10);
        String column=col.orElse("id");
        String order=ord.orElse("desc");
        String fromDate=from.orElse("NA");
        String toDate=to.orElse("NA");
        
		model.addAttribute("title", "Corpseed Dashboard | Manage Enquiry");
		model.addAttribute("appendClass", "master");
		
		Pageable pageable=null;
		if(order.equalsIgnoreCase("desc")) {
			pageable=PageRequest.of((currentPage-1), pageSize,Sort.by(column).descending());
			model.addAttribute("order", "asc");
			model.addAttribute("sortTitle", "Descending Order");
		}			
		else {
			pageable=PageRequest.of((currentPage-1), pageSize,Sort.by(column).ascending());
			model.addAttribute("order", "desc");
			model.addAttribute("sortTitle", "Ascending Order");
		}			
		Page<Enquiry> enquiry=null;
		if(fromDate.equalsIgnoreCase("NA")||toDate.equalsIgnoreCase("NA"))
			enquiry=this.masterService.getEnquiry(pageable);
		else {
			enquiry=this.masterService.getEnquiry(fromDate,toDate,pageable);
			model.addAttribute("dateFrom", fromDate);
			model.addAttribute("dateTo",toDate);
			model.addAttribute("dateFilter", true);
		}			
		
		model.addAttribute("cpage", currentPage);
		model.addAttribute("enquiry",enquiry);
		model.addAttribute("column", column);
		
        int totalPages = enquiry.getTotalPages();
        model.addAttribute("totalRecords", enquiry.getTotalElements());
        model.addAttribute("totalPages", totalPages);
        
        int startRange=currentPage-2;
        int endRange=currentPage+2;
        
        if (totalPages > 1) {        	 
             if((endRange-2)==totalPages)startRange=currentPage-4;        
             if(startRange==currentPage)endRange=currentPage+4;
             if(startRange<1) {startRange=1;endRange=startRange+4;}
             if(endRange>totalPages) {endRange=totalPages;startRange=endRange-4;}             
             if(startRange<1)startRange=1;
             
            List<Integer> pageNumbers = IntStream.rangeClosed(startRange, endRange)
                .boxed()
                .collect(Collectors.toList());
            model.addAttribute("pageNumbers", pageNumbers);
        }
		
		return "admin/enquiry";
	}
	
	@GetMapping("/enquiry/edit/{uuid}")
	public String enquiryEdit(@PathVariable("uuid") String uuid,Model model) {
		model.addAttribute("title", "Corpseed Dashboard | Edit Enquiry");
		model.addAttribute("appendClass", "master");
		model.addAttribute("enquiryUuid", uuid);
		model.addAttribute("service", this.masterService.getAllServices());
		model.addAttribute("enquiry", this.masterService.getEnquiryByUuid(uuid));
		return "admin/enquiry-edit";
	}
	@PostMapping("/enquiry/update/{enquiryuuid}")
	public String saveAddress(@Valid @ModelAttribute("enquiry") Enquiry enquiry,BindingResult result,
			@PathVariable("enquiryuuid") String enquiryuuid,HttpSession session,Model model) {
		try {			
			if(result.hasErrors()) {
				model.addAttribute("title", "Corpseed Dashboard | Edit Enquiry");
				model.addAttribute("appendClass", "master");
				model.addAttribute("enquiry", enquiry);
				model.addAttribute("enquiryUuid", enquiryuuid);
				model.addAttribute("service", this.masterService.getAllServices());
				session.setAttribute("message", new Message("Please fill out the blank fields !!", "alert-danger"));
				return "admin/enquiry-edit";
			}
			
				Enquiry findEnquiry=this.masterService.getEnquiryByUuid(enquiryuuid);
				enquiry.setId(findEnquiry.getId());
				enquiry.setUuid(findEnquiry.getUuid());
				
				Enquiry saveEnquiry=this.masterService.saveEnquiry(enquiry);
				if(saveEnquiry==null) {
					model.addAttribute("title", "Corpseed Dashboard | Edit Enquiry");
					model.addAttribute("appendClass", "master");
					model.addAttribute("enquiry", enquiry);
					model.addAttribute("enquiryUuid", enquiryuuid);
					model.addAttribute("service", this.masterService.getAllServices());
					session.setAttribute("message", new Message("Data Not saved, Please try-again later !!", "alert-danger"));
					return "admin/enquiry-edit";
				}else {
					return "redirect:/admin/master/enquiry";
				}
			
		} catch (Exception e) {
			model.addAttribute("title", "Corpseed Dashboard | Edit Enquiry");
			model.addAttribute("appendClass", "master");
			model.addAttribute("enquiry", enquiry);
			model.addAttribute("enquiryUuid", enquiryuuid);
			model.addAttribute("service", this.masterService.getAllServices());
			session.setAttribute("message", new Message("Error : " + e.getMessage(), "alert-danger"));
			return "admin/enquiry-edit";
		}
	}
	@GetMapping("/enquiry/delete/{uuid}")
	public String enquiryDelete(@PathVariable("uuid") String uuid,Principal principal,HttpServletRequest req) {
		if(this.commonServices.getLoginedUser(principal).getRole().equals("ROLE_ADMIN")) {
			Enquiry enquiry=this.masterService.getEnquiryByUuid(uuid);
			if(enquiry!=null) {
				enquiry.setDeleteStatus(1);
				this.enquiryService.saveEnquiry(enquiry);
				
				User user=this.commonServices.getLoginedUser(principal);
				
				//adding enquiry in history
				this.historyService.savehistory(new History(0, this.commonServices.getUUID(),
						"Enquiry", enquiry.getId(), this.commonServices.getBrowser(req), 
						this.commonServices.getIpAddress(req), user.getId(), user.getFirstName()+" "+user.getLastName()
						, this.commonServices.getToday(), this.commonServices.getTime(),enquiry.getType()));		
				
			}
			return "redirect:/admin/master/enquiry";
		}else {
			return "error/403";
		}
	}
	
	@GetMapping("/address")
	public String address(Model model) {
		model.addAttribute("title", "Corpseed Dashboard | Manage Contact Address");
		model.addAttribute("appendClass", "master");
		model.addAttribute("contactAddress", this.masterService.getAllContactAddress());
		return "admin/address";
	}
	
	@GetMapping("/address/edit/{addressuuid}")
	public String editAddress(@PathVariable("addressuuid") String addressuuid,Model model) {
		model.addAttribute("title", "Corpseed Dashboard | Edit Address");
		model.addAttribute("appendClass", "master");
		model.addAttribute("contactAddress", this.masterService.getContactAddressByUuid(addressuuid));
		model.addAttribute("addressuuid", addressuuid);
		return "admin/address-edit";
	}
	@GetMapping("/address/delete/{uuid}")
	public String deleteAddress(@PathVariable("uuid") String uuid,Principal principal,HttpServletRequest req) {
		if(this.commonServices.getLoginedUser(principal).getRole().equals("ROLE_ADMIN")) {
			ContactAddress address=this.masterService.getContactAddressByUuid(uuid);
			if(address!=null) {
				address.setDeleteStatus(1);
				this.masterService.saveAddress(address);
				
				User user=this.commonServices.getLoginedUser(principal);
				
				//adding category in history
				this.historyService.savehistory(new History(0, this.commonServices.getUUID(),
						"Address", address.getId(), this.commonServices.getBrowser(req), 
						this.commonServices.getIpAddress(req), user.getId(), user.getFirstName()+" "+user.getLastName()
						, this.commonServices.getToday(), this.commonServices.getTime(),address.getTitle()));		
				
			}
			return "redirect:/admin/master/address/";
		}else {
			return "error/403";
		}
	}
	@PostMapping("/address/update/{addressuuid}")
	public String saveAddress(@Valid @ModelAttribute("contactAddress") ContactAddress contactAddress,BindingResult result,
			@PathVariable("addressuuid") String addressuuid,HttpSession session,Model model,Principal principal) {
		try {			
			if(result.hasErrors()) {
				model.addAttribute("title", "Corpseed Dashboard | Edit Contact Address");
				model.addAttribute("appendClass", "master");
				model.addAttribute("contactAddress", contactAddress);
				session.setAttribute("message", new Message("Please fill out the blank fields !!", "alert-danger"));
				return "admin/address-edit";
			}
				ContactAddress findAddress=this.masterService.getContactAddressByUuid(addressuuid);
				contactAddress.setId(findAddress.getId());
				contactAddress.setUuid(findAddress.getUuid());
				contactAddress.setAddedByUUID(this.commonServices.getLoginedUser(principal).getUuid());
				
				ContactAddress saveAddress=this.masterService.saveAddress(contactAddress);
				if(saveAddress==null) {
					model.addAttribute("title", "Corpseed Dashboard | Edit Contact Address");
					model.addAttribute("appendClass", "master");
					model.addAttribute("contactAddress", contactAddress);
					session.setAttribute("message", new Message("Data Not saved, Please try-again later !!", "alert-danger"));
					return "admin/address-edit";
				}else {
					return "redirect:/admin/master/address";
				}
			
		} catch (Exception e) {
			model.addAttribute("title", "Corpseed Dashboard | Edit Contact Address");
			model.addAttribute("appendClass", "master");
			model.addAttribute("contactAddress", contactAddress);
			session.setAttribute("message", new Message("Error : " + e.getMessage(), "alert-danger"));
			return "admin/address-edit";
		}
	}
	
	@PostMapping("/address/add")
	public String saveAddress(@Valid @ModelAttribute("contactAddress") ContactAddress contactAddress,BindingResult result,
			HttpSession session,Model model,Principal principal) {
		try {
			model.addAttribute("title", "Corpseed Dashboard | Add Contact Address");
			model.addAttribute("appendClass", "master");
			if(result.hasErrors()) {
				model.addAttribute("contactAddress", contactAddress);
				session.setAttribute("message", new Message("Please fill out the blank fields !!", "alert-danger"));
				return "admin/address-add";
			}
				contactAddress.setUuid(this.commonServices.getUUID());
				contactAddress.setAddedByUUID(this.commonServices.getLoginedUser(principal).getUuid());
				
				ContactAddress saveAddress=this.masterService.saveAddress(contactAddress);
				if(saveAddress==null) {
					model.addAttribute("contactAddress", contactAddress);
					session.setAttribute("message", new Message("Data Not Saved, Please try-again later !!", "alert-danger"));
					return "admin/address-add";
				}else {
					return "redirect:/admin/master/address";
				}
			
		} catch (Exception e) {
			model.addAttribute("contactAddress", contactAddress);
			session.setAttribute("message", new Message("Error : " + e.getMessage(), "alert-danger"));
			return "admin/address-add";
		}
	}
	@GetMapping("/address/add")
	public String addAddress(Model model) {
		model.addAttribute("title", "Corpseed Dashboard | Add Contact Address");
		model.addAttribute("appendClass", "master");
		model.addAttribute("contactAddress", new ContactAddress());
		return "admin/address-add";
	}
	@GetMapping("/country")
	public String country(Model model) {
		model.addAttribute("title", "Corpseed Dashboard | Manage Country");
		model.addAttribute("country", this.masterService.getAllCountry());
		model.addAttribute("appendClass", "master");
		
		return "admin/country";
	}
	@GetMapping("/country/add")
	public String countryAdd(Model model) {
		model.addAttribute("title", "Corpseed Dashboard | Add Country");
		model.addAttribute("appendClass", "master");
		model.addAttribute("country", new Country());
		return "admin/country-add";
	}
	@GetMapping("/country/delete/{uuid}")
	public String deleteCountry(@PathVariable("uuid") String uuid,Principal principal,HttpServletRequest req) {
		if(this.commonServices.getLoginedUser(principal).getRole().equals("ROLE_ADMIN")) {
			Country country=this.masterService.getCountryByUuid(uuid);
			if(country!=null) {
				country.setDeleteStatus(1);
				this.masterService.saveCountry(country);

				User user=this.commonServices.getLoginedUser(principal);
				
				//adding category in history
				this.historyService.savehistory(new History(0, this.commonServices.getUUID(),
						"Country", country.getId(), this.commonServices.getBrowser(req), 
						this.commonServices.getIpAddress(req), user.getId(), user.getFirstName()+" "+user.getLastName()
						, this.commonServices.getToday(), this.commonServices.getTime(),country.getCountryName()));		
				
				//updating state
				List<State> state=country.getState();
				List<City> cities=new ArrayList<>();
				if(!state.isEmpty()) {
					for (State s : state) {
						s.setDeleteStatus(1);
						if(!s.getListCity().isEmpty())
							cities.addAll(s.getListCity());
					}	
					if(!cities.isEmpty()) {
						cities.forEach(c->c.setDeleteStatus(1));
						this.masterService.saveCities(cities);
					}
					this.masterService.saveStates(state);					
				}
			}			
			return "redirect:/admin/master/country/";
		}else {
			return "error/403";
		}
	}
	@GetMapping("/country/edit/{countryuuid}")
	public String editCountry(@PathVariable("countryuuid") String countryuuid,Model model) {
		model.addAttribute("title", "Corpseed Dashboard | Edit Country");
		model.addAttribute("appendClass", "master");
		model.addAttribute("country", this.masterService.getCountryByUuid(countryuuid));
		model.addAttribute("countryuuid", countryuuid);
		return "admin/country-edit";
	}
	
	@PostMapping("/country/update/{countryuuid}")
	public String saveCountry(@Valid @ModelAttribute("country") Country country,BindingResult result,
			@PathVariable("countryuuid") String countryuuid,HttpSession session,Model model,Principal principal) {
		try {
			if(result.hasErrors()) {
				model.addAttribute("title", "Corpseed Dashboard | Edit Country");
				model.addAttribute("appendClass", "master");
				model.addAttribute("country", country);
				session.setAttribute("message", new Message("Please fill out the blank fields !!", "alert-danger"));
				return "admin/country-edit";
			}
			Country existCountry=this.masterService.findByCountryCodeAndShortNameAndUuidNot(country.getCountryCode(),country.getShortName(),countryuuid);
			if(existCountry!=null) {
				model.addAttribute("title", "Corpseed Dashboard | Edit Country");
				model.addAttribute("appendClass", "master");
				model.addAttribute("country", country);
				if(existCountry.getDeleteStatus()==1)
					session.setAttribute("message", new Message("Country exist in Trash !!", "alert-danger"));
				else
					session.setAttribute("message", new Message("Country already exist !!", "alert-danger"));
				
				return "admin/country-edit";	
			}
				Country findCountry=this.masterService.getCountryByUuid(countryuuid);
				country.setId(findCountry.getId());
				country.setUuid(findCountry.getUuid());
				country.setAddedByUUID(this.commonServices.getLoginedUser(principal).getUuid());
				
				Country saveCountry=this.masterService.saveCountry(country);
				if(saveCountry==null) {
					model.addAttribute("title", "Corpseed Dashboard | Edit Country");
					model.addAttribute("appendClass", "master");
					model.addAttribute("country", country);
					session.setAttribute("message", new Message("Data Not saved, Please try-again later !!", "alert-danger"));
					return "admin/country-edit";
				}else {
					return "redirect:/admin/master/country";
				}
		} catch (Exception e) {
			model.addAttribute("title", "Corpseed Dashboard | Edit Country");
			model.addAttribute("appendClass", "master");
			model.addAttribute("country", country);
			session.setAttribute("message", new Message("Error : " + e.getMessage(), "alert-danger"));
			return "admin/country-edit";
		}
	}
	
	@PostMapping("/country/add")
	public String saveCountry(@Valid @ModelAttribute("country") Country country,BindingResult result,
			HttpSession session,Model model,Principal principal) {
		try {
			model.addAttribute("title", "Corpseed Dashboard | Add Country");
			model.addAttribute("appendClass", "master");
			if(result.hasErrors()) {
				model.addAttribute("country", country);
				session.setAttribute("message", new Message("Please fill out the blank fields !!", "alert-danger"));
				return "admin/country-add";
			}
			Country existCountry=this.masterService.findByCountryCodeAndShortName(country.getCountryCode(),country.getShortName());
			if(existCountry==null) {
				country.setUuid(this.commonServices.getUUID());
				country.setAddedByUUID(this.commonServices.getLoginedUser(principal).getUuid());
				
				Country saveCountry=this.masterService.saveCountry(country);
				if(saveCountry==null) {
					model.addAttribute("country", country);
					session.setAttribute("message", new Message("Data Not Saved, Please try-again later !!", "alert-danger"));
					return "admin/country-add";
				}else {
					return "redirect:/admin/master/country";
				}
			}else {
				model.addAttribute("country", country);
				session.setAttribute("message", new Message("Country already exist !!", "alert-danger"));
				return "admin/country-add";				
			}		
			
		} catch (Exception e) {
			model.addAttribute("country", country);
			session.setAttribute("message", new Message("Error : " + e.getMessage(), "alert-danger"));
			return "admin/country-add";
		}
	}
	@GetMapping("/state")
	public String state(Model model) {
		model.addAttribute("title", "Corpseed Dashboard | Manage State");
		model.addAttribute("appendClass", "master");
		model.addAttribute("state", this.masterService.getAllStates());
		return "admin/state";
	}
	@GetMapping("/state/add")
	public String stateAdd(Model model) {
		model.addAttribute("title", "Corpseed Dashboard | Add State");
		model.addAttribute("appendClass", "master");
		model.addAttribute("country", this.masterService.getAllCountry());
		model.addAttribute("state", new State());
		return "admin/state-add";
	}
	
	@GetMapping("/state/delete/{uuid}")
	public String deleteState(@PathVariable("uuid") String uuid,Principal principal,HttpServletRequest req) {
		if(this.commonServices.getLoginedUser(principal).getRole().equals("ROLE_ADMIN")) {
			State state=this.masterService.getStateByUuid(uuid);
			if(state!=null) {
				state.setDeleteStatus(1);
				this.masterService.saveState(state);
				
				User user=this.commonServices.getLoginedUser(principal);
				
				//adding category in history
				this.historyService.savehistory(new History(0, this.commonServices.getUUID(),
						"State", state.getId(), this.commonServices.getBrowser(req), 
						this.commonServices.getIpAddress(req), user.getId(), user.getFirstName()+" "+user.getLastName()
						, this.commonServices.getToday(), this.commonServices.getTime(),state.getStateName()));		
				
				//updating city
				List<City> city=state.getListCity();
				if(!city.isEmpty()) {
					city.forEach(c->c.setDeleteStatus(1));
					this.masterService.saveCities(city);
				}
				
			}			
			return "redirect:/admin/master/state/";
		}else {
			return "error/403";
		}
	}
	
	@GetMapping("/state/edit/{stateuuid}")
	public String editState(@PathVariable("stateuuid") String stateuuid,Model model) {
		model.addAttribute("title", "Corpseed Dashboard | Edit State");
		model.addAttribute("appendClass", "master");
		model.addAttribute("country", this.masterService.getAllCountry());
		model.addAttribute("state", this.masterService.getStateByUuid(stateuuid));
		model.addAttribute("stateuuid", stateuuid);
		return "admin/state-edit";
	}
	
	@PostMapping("/state/update/{stateuuid}")
	public String updateState(@Valid @ModelAttribute("state") State state,BindingResult result,
			@PathVariable("stateuuid") String stateuuid,@RequestParam("countryId") String countryId,
			HttpSession session,Model model,Principal principal) {
		try {			
			if(result.hasErrors()) {
				model.addAttribute("title", "Corpseed Dashboard | Edit State");
				model.addAttribute("appendClass", "master");
				model.addAttribute("country", this.masterService.getAllCountry());
				model.addAttribute("state", state);				
				session.setAttribute("message", new Message("Please fill out the blank fields !!", "alert-danger"));
				return "admin/state-edit";
			}
			//getting country object
			Country country=this.masterService.getCountryById(countryId);
			
			State existState=this.masterService.findByStateNameAndCountryAndUuidNot(state.getStateName(),country,stateuuid);
			if(existState!=null||country==null) {
				model.addAttribute("title", "Corpseed Dashboard | Edit State");
				model.addAttribute("appendClass", "master");
				model.addAttribute("country", this.masterService.getAllCountry());
				model.addAttribute("state", state);
				if(existState.getDeleteStatus()==1)
					session.setAttribute("message", new Message("State exist in Trash !!", "alert-danger"));
				else
					session.setAttribute("message", new Message("Either Country not found or State already exist !!", "alert-danger"));
				
				return "admin/state-edit";	
			}
				State findState=this.masterService.getStateByUuid(stateuuid);
				state.setId(findState.getId());
				state.setUuid(findState.getUuid());
				state.setAddedByUUID(this.commonServices.getLoginedUser(principal).getUuid());
				state.setCountry(country);
				
				State saveState=this.masterService.saveState(state);
				if(saveState==null) {
					model.addAttribute("title", "Corpseed Dashboard | Edit State");
					model.addAttribute("appendClass", "master");
					model.addAttribute("country", this.masterService.getAllCountry());
					model.addAttribute("state", state);
					session.setAttribute("message", new Message("Data Not Saved, Please try-again later !!", "alert-danger"));
					return "admin/state-edit";
				}
				return "redirect:/admin/master/state";
			
		} catch (Exception e) {
			model.addAttribute("title", "Corpseed Dashboard | Edit State");
			model.addAttribute("appendClass", "master");
			model.addAttribute("country", this.masterService.getAllCountry());
			model.addAttribute("state", state);
			session.setAttribute("message", new Message("Error : " + e.getMessage(), "alert-danger"));
			return "admin/state-edit";
		}
	}
	
	@PostMapping("/state/add")
	public String saveState(@Valid @ModelAttribute("state") State state,BindingResult result,
			@RequestParam("countryId") String countryId,HttpSession session,Model model,Principal principal) {
			model.addAttribute("title", "Corpseed Dashboard | Add State");
			model.addAttribute("appendClass", "master");
			model.addAttribute("country", this.masterService.getAllCountry());
		try {			
			if(result.hasErrors()) {
				model.addAttribute("state", state);
				session.setAttribute("message", new Message("Please fill out the blank fields !!", "alert-danger"));
				return "admin/state-add";
			}
			//getting country object
			Country country=this.masterService.getCountryById(countryId);
			
			State existState=this.masterService.findByStateNameAndCountry(state.getStateName(),country);
			if(existState!=null||country==null) {
				model.addAttribute("state", state);
				if(existState.getDeleteStatus()==1)
					session.setAttribute("message", new Message("State exist in Trash !!", "alert-danger"));
				else
					session.setAttribute("message", new Message("Either Country not found or State already exist !!", "alert-danger"));
				
				return "admin/state-add";
			}
				state.setUuid(this.commonServices.getUUID());
				state.setAddedByUUID(this.commonServices.getLoginedUser(principal).getUuid());
				state.setCountry(country);
				
				State saveState=this.masterService.saveState(state);
				if(saveState==null) {
					model.addAttribute("state", state);
					session.setAttribute("message", new Message("Data Not Saved, Please try-again later !!", "alert-danger"));
					return "admin/state-add";
				}else {
					return "redirect:/admin/master/state";
				}
		} catch (Exception e) {
			model.addAttribute("state", state);
			session.setAttribute("message", new Message("Error : " + e.getMessage(), "alert-danger"));
			return "admin/state-add";
		}
	}
	
	@GetMapping("/city")
	public String city(Model model) {
		model.addAttribute("title", "Corpseed Dashboard | Manage City");
		model.addAttribute("appendClass", "master");
		model.addAttribute("city", this.masterService.getAllCity());
		return "admin/city";
	}

	@GetMapping("/city/add")
	public String cityAdd(Model model) {
		model.addAttribute("title", "Corpseed Dashboard | Add City");
		model.addAttribute("appendClass", "master");
		model.addAttribute("country", this.masterService.getAllCountry());
		model.addAttribute("city", new City());
		return "admin/city-add";
	}

	@GetMapping("/city/delete/{uuid}")
	public String deleteCity(@PathVariable("uuid") String uuid,Principal principal,HttpServletRequest req) {
		if(this.commonServices.getLoginedUser(principal).getRole().equals("ROLE_ADMIN")) {
			City city=this.masterService.getCityByUuid(uuid);
			if(city!=null) {
				city.setDeleteStatus(1);
				this.masterService.saveCity(city);
				
				User user=this.commonServices.getLoginedUser(principal);
				
				//adding category in history
				this.historyService.savehistory(new History(0, this.commonServices.getUUID(),
						"City", city.getId(), this.commonServices.getBrowser(req), 
						this.commonServices.getIpAddress(req), user.getId(), user.getFirstName()+" "+user.getLastName()
						, this.commonServices.getToday(), this.commonServices.getTime(),city.getCityName()));		
			}
			return "redirect:/admin/master/city/";
		}else {
			return "error/403";
		}
	}
	@GetMapping("/city/edit/{cityuuid}")
	public String editCity(@PathVariable("cityuuid") String cityuuid,Model model) {
		model.addAttribute("title", "Corpseed Dashboard | Edit City");
		model.addAttribute("appendClass", "master");
		model.addAttribute("country", this.masterService.getAllCountry());
		model.addAttribute("city", this.masterService.getCityByUuid(cityuuid));
		model.addAttribute("cityuuid", cityuuid);
		return "admin/city-edit";
	}
	
	@PostMapping("/city/update/{cityuuid}")
	public String updateCity(@Valid @ModelAttribute("city") City city,BindingResult result,
			@PathVariable("cityuuid") String cityuuid,@RequestParam("stateId") String stateId,HttpSession session,Model model,Principal principal) {
		try {			
			if(result.hasErrors()) {
				model.addAttribute("title", "Corpseed Dashboard | Edit City");
				model.addAttribute("appendClass", "master");
				model.addAttribute("country", this.masterService.getAllCountry());
				model.addAttribute("city", city);
				session.setAttribute("message", new Message("Please fill out the blank fields !!", "alert-danger"));
				return "admin/city-edit";
			}
			//getting state object
			State state=this.masterService.getStateById(stateId);
			
			City existCity=this.masterService.findByCityNameAndStateAndUuidNot(city.getCityName(),state,cityuuid);
			if(existCity!=null||state==null) {
				model.addAttribute("title", "Corpseed Dashboard | Edit City");
				model.addAttribute("appendClass", "master");
				model.addAttribute("country", this.masterService.getAllCountry());
				model.addAttribute("city", city);
				
				if(existCity.getDeleteStatus()==1)
					session.setAttribute("message", new Message("City exist in Trash !!", "alert-danger"));
				else
					session.setAttribute("message", new Message("Either State not found OR City already exist !!", "alert-danger"));
				
				return "admin/city-edit";
			}
				City findCity=this.masterService.getCityByUuid(cityuuid);
				city.setId(findCity.getId());
				city.setUuid(findCity.getUuid());
				city.setAddedByUUID(this.commonServices.getLoginedUser(principal).getUuid());
				city.setState(state);
				
				City saveCity=this.masterService.saveCity(city);
				if(saveCity==null) {
					model.addAttribute("title", "Corpseed Dashboard | Edit City");
					model.addAttribute("appendClass", "master");
					model.addAttribute("country", this.masterService.getAllCountry());
					model.addAttribute("city", city);
					session.setAttribute("message", new Message("Data Not Saved, Please try-again later !!", "alert-danger"));
					return "admin/city-edit";
				}else {
					return "redirect:/admin/master/city";
				}
		} catch (Exception e) {
			model.addAttribute("title", "Corpseed Dashboard | Edit City");
			model.addAttribute("appendClass", "master");
			model.addAttribute("country", this.masterService.getAllCountry());
			model.addAttribute("city", city);
			session.setAttribute("message", new Message("Error : " + e.getMessage(), "alert-danger"));
			return "admin/city-edit";
		}
	}
	
	@PostMapping("/city/add")
	public String saveCity(@Valid @ModelAttribute("city") City city,BindingResult result,
			@RequestParam("stateId") String stateId,HttpSession session,Model model,Principal principal) {
			model.addAttribute("title", "Corpseed Dashboard | Add City");
			model.addAttribute("appendClass", "master");
			model.addAttribute("country", this.masterService.getAllCountry());
		try {
			if(result.hasErrors()) {
				model.addAttribute("city", city);
				session.setAttribute("message", new Message("Please fill out the blank fields !!", "alert-danger"));
				return "admin/city-add";
			}
			//getting state object
			State state=this.masterService.getStateById(stateId);
			
			City existCity=this.masterService.findByCityNameAndState(city.getCityName(),state);
			if(existCity!=null||state==null) {
				model.addAttribute("city", city);
				if(existCity.getDeleteStatus()==1)
					session.setAttribute("message", new Message("City exist in Trash !!", "alert-danger"));
				else
					session.setAttribute("message", new Message("Either State not found OR City already exist !!", "alert-danger"));
				
				return "admin/city-add";
			}
				city.setUuid(this.commonServices.getUUID());
				city.setAddedByUUID(this.commonServices.getLoginedUser(principal).getUuid());
				city.setState(state);
				
				City saveCity=this.masterService.saveCity(city);
				if(saveCity==null) {
					model.addAttribute("city", city);
					session.setAttribute("message", new Message("Data Not Saved, Please try-again later !!", "alert-danger"));
					return "admin/city-add";
				}
				return "redirect:/admin/master/city";
				
		} catch (Exception e) {
			model.addAttribute("city", city);
			session.setAttribute("message", new Message("Error : " + e.getMessage(), "alert-danger"));
			return "admin/city-add";
		}
	}
	@GetMapping("/contact")
	public String contact(Model model,Principal principal) {
		model.addAttribute("title", "Corpseed Dashboard | Manage Contact");
		model.addAttribute("contact", this.masterService.getAllContact());
		model.addAttribute("appendClass", "master");
		
		return "admin/contact";
	}
	@GetMapping("/contact/add")
	public String contactAdd(Model model) {
		model.addAttribute("title", "Corpseed Dashboard | Add Contact");
		model.addAttribute("appendClass", "master");
		model.addAttribute("contact", new Contact());
		return "admin/contact-add";
	}
	@PostMapping("/contact/add")
	public String saveContact(@Valid @ModelAttribute("contact") Contact contact,BindingResult result,
			HttpSession session,Model model,Principal principal) {
			model.addAttribute("title", "Corpseed Dashboard | Add Contact");
			model.addAttribute("appendClass", "master");
		try {
			
			if(result.hasErrors()) {
				model.addAttribute("contact", contact);
				session.setAttribute("message", new Message("Please fill out the blank fields !!", "alert-danger"));
				return "admin/contact-add";
			}
			Contact existContact=this.masterService.findByMobileOrEmail(contact.getMobile(),contact.getEmail());			
			if(existContact!=null) {
				model.addAttribute("contact", contact);
				if(existContact.getDeleteStatus()==1)
					session.setAttribute("message", new Message("Contact exist in Trash !!", "alert-danger"));
				else
					session.setAttribute("message", new Message("Contact already exist !!", "alert-danger"));
				
				return "admin/contact-add";	
			}
				boolean savePermission=false;
				if(contact.getDisplayStatus().equals("1")) {
					Contact statusContact=this.masterService.getContactByStatus("1");
					if(statusContact!=null)savePermission=false;
					else savePermission=true;
				}else {
					savePermission=true;
				}
				if(!savePermission) {
					model.addAttribute("contact", contact);
					session.setAttribute("message", new Message("Please disable contact used on the page !!", "alert-danger"));
					return "admin/contact-add";
				}
				contact.setUuid(this.commonServices.getUUID());
				contact.setAddedByUuid(this.commonServices.getLoginedUser(principal).getUuid());
				
				Contact saveContact=this.masterService.saveContact(contact);
				if(saveContact==null) {
					model.addAttribute("contact", contact);
					session.setAttribute("message", new Message("Data Not Saved, Please try-again later !!", "alert-danger"));
					return "admin/contact-add";
				}
				return "redirect:/admin/master/contact";
						
				
		} catch (Exception e) {
			model.addAttribute("contact", contact);
			session.setAttribute("message", new Message("Error : " + e.getMessage(), "alert-danger"));
			return "admin/contact-add";
		}
	}
	@GetMapping("/contact/edit/{contactuuid}")
	public String editContact(@PathVariable("contactuuid") String contactuuid,Model model) {
		model.addAttribute("title", "Corpseed Dashboard | Edit Contact");
		model.addAttribute("appendClass", "master");
		model.addAttribute("contact", this.masterService.getContactByUuid(contactuuid));		
		return "admin/contact-edit";
	}
	
	@PostMapping("/contact/update")
	public String updateContact(@Valid @ModelAttribute("contact") Contact contact,BindingResult result,
			HttpSession session,Model model,Principal principal) {
		model.addAttribute("title", "Corpseed Dashboard | Update Contact");
		model.addAttribute("appendClass", "master");
		try {			
			if(result.hasErrors()) {
				model.addAttribute("contact", contact);
				session.setAttribute("message", new Message("Please fill out the blank fields !!", "alert-danger"));
				return "admin/contact-edit";
			}
			Contact existContact1=this.masterService.findByMobileAndUuidNot(contact.getMobile(),contact.getUuid());			
			Contact existContact2=this.masterService.findByEmailAndUuidNot(contact.getEmail(),contact.getUuid());
			if(existContact1!=null||existContact2!=null) {
				model.addAttribute("contact", contact);
				if(existContact1.getDeleteStatus()==1||existContact2.getDeleteStatus()==1)
					session.setAttribute("message", new Message("Contact exist in Trash !!", "alert-danger"));
				else
					session.setAttribute("message", new Message("Contact already exist !!", "alert-danger"));
				
				return "admin/contact-edit";	
			}
				boolean savePermission=false;
				if(contact.getDisplayStatus().equals("1")) {
					Contact statusContact=this.masterService.getContactByStatusAndUuidNot("1",contact.getUuid());
					if(statusContact!=null)savePermission=false;
					else savePermission=true;
				}else {
					savePermission=true;
				}
				if(!savePermission) {
					model.addAttribute("contact", contact);
					session.setAttribute("message", new Message("Please disable contact used on the page !!", "alert-danger"));
					return "admin/contact-edit";
				}
					Contact findContact=this.masterService.getContactByUuid(contact.getUuid());
					if(findContact!=null) {
							contact.setId(findContact.getId());
							contact.setAddedByUuid(this.commonServices.getLoginedUser(principal).getUuid());
							
							Contact saveContact=this.masterService.saveContact(contact);
							if(saveContact==null) {
								model.addAttribute("contact", contact);
								session.setAttribute("message", new Message("Data Not Saved, Please try-again later !!", "alert-danger"));
								return "admin/contact-edit";
							}
							return "redirect:/admin/master/contact";
					}
			return "/admin/master/contact";
		} catch (Exception e) {
			model.addAttribute("contact", contact);
			session.setAttribute("message", new Message("Error : " + e.getMessage(), "alert-danger"));
			return "admin/contact-edit";
		}
	}
	@GetMapping("/contact/delete/{uuid}")
	public String deleteContact(@PathVariable("uuid") String uuid,Principal principal,HttpServletRequest req) {
		if(this.commonServices.getLoginedUser(principal).getRole().equals("ROLE_ADMIN")) {
			Contact contact=this.masterService.getContactByUuid(uuid);
			if(contact!=null) {
				contact.setDeleteStatus(1);
				this.masterService.saveContact(contact);
				
				User user=this.commonServices.getLoginedUser(principal);
				
				//adding category in history
				this.historyService.savehistory(new History(0, this.commonServices.getUUID(),
						"Contact", contact.getId(), this.commonServices.getBrowser(req), 
						this.commonServices.getIpAddress(req), user.getId(), user.getFirstName()+" "+user.getLastName()
						, this.commonServices.getToday(), this.commonServices.getTime(),contact.getName()));		
				
			}
			return "redirect:/admin/master/contact";
		}else {
			return "error/403";
		}
	}
	@GetMapping("/hot-tags")
	public String hotTags(Model model) {
		model.addAttribute("title", "Corpseed Dashboard | Manage Hot tags");
		model.addAttribute("appendClass", "master");
		model.addAttribute("tags",this.hotTagService.findAll());
		return "admin/hot-tags";
	}
	@GetMapping("/hot-tag/add")
	public String addHotTags(Model model) {
		model.addAttribute("services", this.servicesService.getAllServicesByStatus("1"));
		model.addAttribute("title", "Corpseed Dashboard | Add Hot tag");
		model.addAttribute("appendClass", "master");
		model.addAttribute("tags", new HotTags());
		return "admin/hot-tags-add";
	}
	@GetMapping("/hot-tag/edit/{uuid}")
	public String editHotTags(@PathVariable("uuid") String uuid,Model model) {
		model.addAttribute("title", "Corpseed Dashboard | Edit Hot tag");
		model.addAttribute("appendClass", "master");
		model.addAttribute("services", this.servicesService.getAllServicesByStatus("1"));
		HotTags tags=this.hotTagService.findByUuid(uuid);
		model.addAttribute("tags", tags);
		model.addAttribute("serviceuuid", tags.getServices().getUuid());
		
		return "admin/hot-tags-edit";
	}
	@GetMapping("/hot-tag/delete/{uuid}")
	public String deleteTags(@PathVariable("uuid") String uuid,Principal principal,HttpServletRequest req) {
		if(this.commonServices.getLoginedUser(principal).getRole().equals("ROLE_ADMIN")) {
			HotTags hotTags=this.hotTagService.findByUuid(uuid);
			if(hotTags!=null) {
				hotTags.setDeleteStatus(1);
				this.hotTagService.saveTag(hotTags);
				
				User user=this.commonServices.getLoginedUser(principal);
				
				//adding category in history
				this.historyService.savehistory(new History(0, this.commonServices.getUUID(),
						"Hot Tag", hotTags.getId(), this.commonServices.getBrowser(req), 
						this.commonServices.getIpAddress(req), user.getId(), user.getFirstName()+" "+user.getLastName()
						, this.commonServices.getToday(), this.commonServices.getTime(),hotTags.getName()));		
			}
			return "redirect:/admin/master/hot-tags";
		}else {
			return "error/403";
		}
	}
	@PostMapping("/hot-tag/add")
	public String saveHotTags(@Valid @ModelAttribute("tags") HotTags tags,BindingResult result,
			@RequestParam("serviceuuid") String serviceuuid,Principal principal,Model model,HttpSession session) {
		try {
			if(result.hasErrors()) {
				model.addAttribute("title", "Corpseed Dashboard | Add Hot tag");
				model.addAttribute("serviceuuid", serviceuuid);
				model.addAttribute("appendClass", "master");
				model.addAttribute("services", this.servicesService.getAllServicesByStatus("1"));
				model.addAttribute("tags", tags);
				session.setAttribute("message", new Message("Please fill out all the fields properly !!", "alert-danger"));
				return "admin/hot-tags-add";
			}
			if(serviceuuid==null||serviceuuid.length()<=0) {
				model.addAttribute("title", "Corpseed Dashboard | Add Hot tag");
				model.addAttribute("serviceuuid", serviceuuid);
				model.addAttribute("appendClass", "master");
				model.addAttribute("services", this.servicesService.getAllServicesByStatus("1"));
				model.addAttribute("tags", tags);
				session.setAttribute("message", new Message("Please select atleast one service !!", "alert-danger"));
				return "admin/hot-tags-add";
			}
			
			HotTags findtags=this.hotTagService.findByName(tags.getName());
			
			if(findtags!=null) {
				model.addAttribute("title", "Corpseed Dashboard | Add Hot tag");
				model.addAttribute("appendClass", "master");
				model.addAttribute("serviceuuid", serviceuuid);
				model.addAttribute("services", this.servicesService.getAllServicesByStatus("1"));
				model.addAttribute("tags", tags);
				if(findtags.getDeleteStatus()==1)
					session.setAttribute("message", new Message("Tag Name exist in Trash !!", "alert-danger"));
				else
					session.setAttribute("message", new Message("Tag Name already exist !!", "alert-danger"));
				
				return "admin/hot-tags-add";
			}
				Services service=this.servicesService.findByUUID(serviceuuid);
				if(service==null) {
					model.addAttribute("title", "Corpseed Dashboard | Add Hot tag");
					model.addAttribute("appendClass", "master");
					model.addAttribute("serviceuuid", serviceuuid);
					model.addAttribute("services", this.servicesService.getAllServicesByStatus("1"));
					model.addAttribute("tags", tags);
					session.setAttribute("message", new Message("Service not found, Please try-again later !!", "alert-danger"));
					return "admin/hot-tags-add";
				}
					HotTags findservice=this.hotTagService.findByServices(service);
					if(findservice!=null) {
						model.addAttribute("title", "Corpseed Dashboard | Add Hot tag");
						model.addAttribute("appendClass", "master");
						model.addAttribute("serviceuuid", serviceuuid);
						model.addAttribute("services", this.servicesService.getAllServicesByStatus("1"));
						model.addAttribute("tags", tags);
						if(findservice.getDeleteStatus()==1)
							session.setAttribute("message", new Message("Tag exist for this service in Trash !!", "alert-danger"));
						else
							session.setAttribute("message", new Message("Tag already exist for this service !!", "alert-danger"));
						
						return "admin/hot-tags-add";
					}
					tags.setUuid(this.commonServices.getUUID());
					tags.setAddedByUUID(this.commonServices.getLoginedUser(principal).getUuid());
					tags.setServices(service);
					
					HotTags savetag=this.hotTagService.saveTag(tags);
					if(savetag==null) {
						model.addAttribute("title", "Corpseed Dashboard | Add Hot tag");
						model.addAttribute("appendClass", "master");
						model.addAttribute("serviceuuid", serviceuuid);
						model.addAttribute("services", this.servicesService.getAllServicesByStatus("1"));
						model.addAttribute("tags", tags);
						session.setAttribute("message", new Message("Data Not Saved, Please try-again later !!", "alert-danger"));
						return "admin/hot-tags-add";
					}
					return "redirect:/admin/master/hot-tags";
				
		} catch (Exception e) {
			model.addAttribute("title", "Corpseed Dashboard | Add Hot tag");
			model.addAttribute("appendClass", "master");
			model.addAttribute("serviceuuid", serviceuuid);
			model.addAttribute("services", this.servicesService.getAllServicesByStatus("1"));
			model.addAttribute("tags", tags);
			session.setAttribute("message", new Message("Error : "+e.getMessage(), "alert-danger"));
			return "admin/hot-tags-add";
		}
	}
	
	@PostMapping("/hot-tag/update")
	public String updateHotTags(@Valid @ModelAttribute("tags") HotTags tags,BindingResult result,
			@RequestParam("serviceuuid") String serviceuuid,Principal principal,Model model,HttpSession session) {
		try {
			if(result.hasErrors()) {
				model.addAttribute("title", "Corpseed Dashboard | Edit Hot tag");
				model.addAttribute("serviceuuid", serviceuuid);
				model.addAttribute("appendClass", "master");
				model.addAttribute("services", this.servicesService.getAllServicesByStatus("1"));
				model.addAttribute("tags", tags);
				session.setAttribute("message", new Message("Please fill out all fields properly !!", "alert-danger"));
				return "admin/hot-tags-edit";
			}
			if(serviceuuid==null||serviceuuid.length()<=0) {
				model.addAttribute("title", "Corpseed Dashboard | Edit Hot tag");
				model.addAttribute("serviceuuid", serviceuuid);
				model.addAttribute("appendClass", "master");
				model.addAttribute("services", this.servicesService.getAllServicesByStatus("1"));
				model.addAttribute("tags", tags);
				session.setAttribute("message", new Message("Please select atleast one service !!", "alert-danger"));
				return "admin/hot-tags-edit";
			}
			
			HotTags findtags=this.hotTagService.findByNameAndUuidNot(tags.getName(),tags.getUuid());
			if(findtags!=null) {
				model.addAttribute("title", "Corpseed Dashboard | Edit Hot tag");
				model.addAttribute("appendClass", "master");
				model.addAttribute("serviceuuid", serviceuuid);
				model.addAttribute("services", this.servicesService.getAllServicesByStatus("1"));
				model.addAttribute("tags", tags);
				if(findtags.getDeleteStatus()==1)
					session.setAttribute("message", new Message("Tag exist in Trash !!", "alert-danger"));
				else
					session.setAttribute("message", new Message("Tag already exist !!", "alert-danger"));
				
				return "admin/hot-tags-edit";
			}
				Services service=this.servicesService.findByUUID(serviceuuid);				
				if(service==null) {
					model.addAttribute("title", "Corpseed Dashboard | Edit Hot tag");
					model.addAttribute("serviceuuid", serviceuuid);
					model.addAttribute("appendClass", "master");
					model.addAttribute("services", this.servicesService.getAllServicesByStatus("1"));
					model.addAttribute("tags", tags);
					session.setAttribute("message", new Message("Service not found, Please try-again later !!", "alert-danger"));
					return "admin/hot-tags-edit";
				}
				HotTags findService=this.hotTagService.findByServicesAndUuidNot(service,tags.getUuid());
				if(findService!=null) {
					model.addAttribute("title", "Corpseed Dashboard | Edit Hot tag");
					model.addAttribute("appendClass", "master");
					model.addAttribute("serviceuuid", serviceuuid);
					model.addAttribute("services", this.servicesService.getAllServicesByStatus("1"));
					model.addAttribute("tags", tags);
					if(findService.getDeleteStatus()==1)
						session.setAttribute("message", new Message("Tag exist for this service in Trash !!", "alert-danger"));
					else
						session.setAttribute("message", new Message("Tag already exist for this service !!", "alert-danger"));
					return "admin/hot-tags-edit";
				}
					tags.setAddedByUUID(this.commonServices.getLoginedUser(principal).getUuid());
					tags.setServices(service);
				
					HotTags savetag=this.hotTagService.saveTag(tags);
					if(savetag==null) {
						model.addAttribute("title", "Corpseed Dashboard | Edit Hot tag");
						model.addAttribute("appendClass", "master");
						model.addAttribute("serviceuuid", serviceuuid);
						model.addAttribute("services", this.servicesService.getAllServicesByStatus("1"));
						model.addAttribute("tags", tags);
						session.setAttribute("message", new Message("Data Not Saved, Please try-again later !!", "alert-danger"));
						return "admin/hot-tags-edit";
					}
			return "redirect:/admin/master/hot-tags";
		
				
		} catch (Exception e) {
			model.addAttribute("title", "Corpseed Dashboard | Edit Hot tag");
			model.addAttribute("appendClass", "master");
			model.addAttribute("serviceuuid", serviceuuid);
			model.addAttribute("services", this.servicesService.getAllServicesByStatus("1"));
			model.addAttribute("tags", tags);
			session.setAttribute("message", new Message("Error : "+e.getMessage(), "alert-danger"));
			return "admin/hot-tags-edit";
		}
	}

	@GetMapping("/reviews")
	public String reviews(Model model) {
		model.addAttribute("title", "Corpseed Dashboard | Manage Review");
		model.addAttribute("appendClass", "master");
		model.addAttribute("reviews", this.masterService.getAllReviews());
		return "admin/reviews";
	}
	
	@GetMapping("/review/add")
	public String addReview(Model model) {
		model.addAttribute("title", "Corpseed Dashboard | Add Review");
		model.addAttribute("appendClass", "master");
		model.addAttribute("review", new Review());
		return "admin/review-add";
	}
	@GetMapping("/review/edit/{uuid}")
	public String editReview(@PathVariable("uuid") String uuid,Model model) {
		model.addAttribute("title", "Corpseed Dashboard | Edit Review");
		model.addAttribute("appendClass", "master");
		model.addAttribute("review", this.masterService.findByUuid(uuid));
		return "admin/review-edit";
	}
	
	@GetMapping("/review/delete/{uuid}")
	public String deleteReview(@PathVariable("uuid") String uuid,Principal principal,HttpServletRequest req) {
		if(this.commonServices.getLoginedUser(principal).getRole().equals("ROLE_ADMIN")) {
			Review review=this.masterService.findByUuid(uuid);			
			if(review!=null) {
				review.setDeleteStatus(1);
				this.masterService.saveReview(review);
				
				User user=this.commonServices.getLoginedUser(principal);
				
				//adding category in history
				this.historyService.savehistory(new History(0, this.commonServices.getUUID(),
						"Review", review.getId(), this.commonServices.getBrowser(req), 
						this.commonServices.getIpAddress(req), user.getId(), user.getFirstName()+" "+user.getLastName()
						, this.commonServices.getToday(), this.commonServices.getTime(),review.getName()));		
			}
			return "redirect:/admin/master/reviews";
		}else {
			return "error/403";
		}
	}
	
	@PostMapping("/review/update")
	public String updateReview(@Valid @ModelAttribute("review") Review review,BindingResult result,
			Principal principal,@RequestParam("file") MultipartFile file,Model model,HttpSession session) {
		try {
			if(result.hasErrors()) {
				model.addAttribute("title", "Corpseed Dashboard | Edit Review");
				model.addAttribute("appendClass", "master");
				model.addAttribute("review", review);
				session.setAttribute("message", new Message("Please fill out all fields properly !!", "alert-danger"));
				return "admin/review-edit";
			}
			review.setAddedByUUID(this.commonServices.getLoginedUser(principal).getUuid());
			String oldFileName=review.getImage();
			if(!file.isEmpty()) {
				this.azureAdapter.deleteFile(oldFileName);
				String name = azureAdapter.upload(file,this.commonServices.getUniqueCode());
				review.setImage(name);
			}
			Review saveReview=this.masterService.saveReview(review);
			if(saveReview==null) {
				model.addAttribute("title", "Corpseed Dashboard | Edit Review");
				model.addAttribute("appendClass", "master");
				model.addAttribute("review", review);
				session.setAttribute("message", new Message("Data Not Saved, Please try-again later !!", "alert-danger"));
				return "admin/review-edit";
			}
			return "redirect:/admin/master/reviews";			
		} catch (Exception e) {
			model.addAttribute("title", "Corpseed Dashboard | Edit Review");
			model.addAttribute("appendClass", "master");
			model.addAttribute("review", review);
			session.setAttribute("message", new Message("Error : "+e.getMessage(), "alert-danger"));
			return "admin/review-edit";
		}
	}
	@PostMapping("/review/add")
	public String saveReview(@Valid @ModelAttribute("review") Review review,BindingResult result,
			Principal principal,@RequestParam("file") MultipartFile file,Model model,HttpSession session) {
		try {
			if(result.hasErrors()) {
				model.addAttribute("title", "Corpseed Dashboard | Add Review");
				model.addAttribute("appendClass", "master");
				model.addAttribute("review", review);
				session.setAttribute("message", new Message("Please fill out all fields properly !!", "alert-danger"));
				return "admin/review-add";
			}
			review.setUuid(this.commonServices.getUUID());
			review.setAddedByUUID(this.commonServices.getLoginedUser(principal).getUuid());
			if(file.isEmpty()) {
				review.setImage("profile.png");
			}else {
				String name = azureAdapter.upload(file,this.commonServices.getUniqueCode());
				review.setImage(name);
			}
			Review saveReview=this.masterService.saveReview(review);
			if(saveReview==null) {
				model.addAttribute("title", "Corpseed Dashboard | Add Review");
				model.addAttribute("appendClass", "master");
				model.addAttribute("review", review);
				session.setAttribute("message", new Message("Data Not Saved, Please try-again later !!", "alert-danger"));
				return "admin/review-add";
			}
			return "redirect:/admin/master/reviews";
			
		} catch (Exception e) {
			model.addAttribute("title", "Corpseed Dashboard | Add Review");
			model.addAttribute("appendClass", "master");
			model.addAttribute("review", review);
			session.setAttribute("message", new Message("Error : "+e.getMessage(), "alert-danger"));
			return "admin/review-add";
		}
	}
	/* visitors */
	@GetMapping("/visitors")
	public String visitors(Model model,@RequestParam("page") Optional<Integer> page, 
		      @RequestParam("size") Optional<Integer> size,@RequestParam("col") Optional<String> col,
		      @RequestParam("ord") Optional<String> ord) {
		  int currentPage = page.orElse(1);
	      int pageSize = size.orElse(10);
	      String column=col.orElse("id");
	      String order=ord.orElse("desc");
		
		model.addAttribute("title", "Corpseed Dashboard | Manage Visitors");
		model.addAttribute("appendClass", "master");
		Pageable pageable=null;
		if(order.equalsIgnoreCase("desc")) {
			pageable=PageRequest.of((currentPage-1), pageSize,Sort.by(column).descending());
			model.addAttribute("order", "asc");
			model.addAttribute("sortTitle", "Descending Order");
		}			
		else {
			pageable=PageRequest.of((currentPage-1), pageSize,Sort.by(column).ascending());
			model.addAttribute("order", "desc");
			model.addAttribute("sortTitle", "Ascending Order");
		}			
		Page<Visitors> visitors=this.visitorsService.getVisitorsByDeleteStatus(2,pageable);				
		
		model.addAttribute("cpage", currentPage);
		model.addAttribute("visitors",visitors);
		model.addAttribute("column", column);
		
      int totalPages = visitors.getTotalPages();
      model.addAttribute("totalRecords", visitors.getTotalElements());
      model.addAttribute("totalPages", totalPages);
      
      int startRange=currentPage-2;
      int endRange=currentPage+2;
      
      if (totalPages > 1) {        	 
           if((endRange-2)==totalPages)startRange=currentPage-4;        
           if(startRange==currentPage)endRange=currentPage+4;
           if(startRange<1) {startRange=1;endRange=startRange+4;}
           if(endRange>totalPages) {endRange=totalPages;startRange=endRange-4;}             
           if(startRange<1)startRange=1;
           
          List<Integer> pageNumbers = IntStream.rangeClosed(startRange, endRange)
              .boxed()
              .collect(Collectors.toList());
          model.addAttribute("pageNumbers", pageNumbers);
      }		
		return "admin/visitors";
	}
	
	public String enquiry1(Model model,@RequestParam("page") Optional<Integer> page, 
		      @RequestParam("size") Optional<Integer> size,@RequestParam("col") Optional<String> col,
		      @RequestParam("ord") Optional<String> ord,@RequestParam("from") Optional<String> from,
		      @RequestParam("to") Optional<String> to) {
		int currentPage = page.orElse(1);
      int pageSize = size.orElse(10);
      String column=col.orElse("id");
      String order=ord.orElse("desc");
      String fromDate=from.orElse("NA");
      String toDate=to.orElse("NA");
      
		model.addAttribute("title", "Corpseed Dashboard | Manage Enquiry");
		model.addAttribute("appendClass", "master");
		
		Pageable pageable=null;
		if(order.equalsIgnoreCase("desc")) {
			pageable=PageRequest.of((currentPage-1), pageSize,Sort.by(column).descending());
			model.addAttribute("order", "asc");
			model.addAttribute("sortTitle", "Descending Order");
		}			
		else {
			pageable=PageRequest.of((currentPage-1), pageSize,Sort.by(column).ascending());
			model.addAttribute("order", "desc");
			model.addAttribute("sortTitle", "Ascending Order");
		}			
		Page<Enquiry> enquiry=null;
		if(fromDate.equalsIgnoreCase("NA")||toDate.equalsIgnoreCase("NA"))
			enquiry=this.masterService.getEnquiry(pageable);
		else {
			enquiry=this.masterService.getEnquiry(fromDate,toDate,pageable);
			model.addAttribute("dateFrom", fromDate);
			model.addAttribute("dateTo",toDate);
			model.addAttribute("dateFilter", true);
		}			
		
		model.addAttribute("cpage", currentPage);
		model.addAttribute("enquiry",enquiry);
		model.addAttribute("column", column);
		
      int totalPages = enquiry.getTotalPages();
      model.addAttribute("totalRecords", enquiry.getTotalElements());
      model.addAttribute("totalPages", totalPages);
      
      int startRange=currentPage-2;
      int endRange=currentPage+2;
      
      if (totalPages > 1) {        	 
           if((endRange-2)==totalPages)startRange=currentPage-4;        
           if(startRange==currentPage)endRange=currentPage+4;
           if(startRange<1) {startRange=1;endRange=startRange+4;}
           if(endRange>totalPages) {endRange=totalPages;startRange=endRange-4;}             
           if(startRange<1)startRange=1;
           
          List<Integer> pageNumbers = IntStream.rangeClosed(startRange, endRange)
              .boxed()
              .collect(Collectors.toList());
          model.addAttribute("pageNumbers", pageNumbers);
      }
		
		return "admin/enquiry";
	}
	
	/* for static seo */
	@GetMapping("/static-seo")
	public String staticSeo(Model model) {
		model.addAttribute("title", "Corpseed Dashboard | Manage Static SEO");
		model.addAttribute("appendClass", "master");
		model.addAttribute("staticSeo", this.staticSeoService.findAll());
		return "admin/static-seo";
	}
	@GetMapping("/static-seo/add")
	public String addStaticSeo(Model model) {
		model.addAttribute("title", "Corpseed Dashboard | Add Static SEO");
		model.addAttribute("appendClass", "master");
		model.addAttribute("staticSeo", new StaticSeo());
		return "admin/static-seo-add";
	}
	@GetMapping("/static-seo/{uuid}/edit")
	public String editStaticSeo(@PathVariable("uuid") String uuid,Model model) {
		model.addAttribute("title", "Corpseed Dashboard | Edit Static SEO");
		model.addAttribute("appendClass", "master");
		model.addAttribute("staticSeo", this.staticSeoService.findByUuid(uuid));
		return "admin/static-seo-edit";
	}
	@PostMapping("/static-seo/add")
	public String saveStaticSeo(@Valid @ModelAttribute("staticSeo") StaticSeo staticSeo,BindingResult result,Model model) {
		
		try {
			if(result.hasErrors()) {
				model.addAttribute("title", "Corpseed Dashboard | Add Static SEO");
				model.addAttribute("appendClass", "master");
				model.addAttribute("message", new Message("Please fill out the blank fields !!", "alert-danger"));
				model.addAttribute("staticSeo", staticSeo);
				return "admin/static-seo-add";
			}
			StaticSeo findStaticSeo=this.staticSeoService.findByPage(staticSeo.getPage());
			if(findStaticSeo!=null) {
				model.addAttribute("title", "Corpseed Dashboard | Add Static SEO");
				model.addAttribute("appendClass", "master");
				model.addAttribute("staticSeo", staticSeo);
				if(findStaticSeo.getDeleteStatus()==1)
					model.addAttribute("message", new Message("Page exist in Trash !!", "alert-danger"));
				else
					model.addAttribute("message", new Message("Page already exist !!", "alert-danger"));
				
				return "admin/static-seo-add";
			}
			staticSeo.setUuid(this.commonServices.getUUID());
			this.staticSeoService.save(staticSeo);
			
			return "redirect:/admin/master/static-seo";
		} catch (Exception e) {
			e.printStackTrace();
			model.addAttribute("title", "Corpseed Dashboard | Add Static SEO");
			model.addAttribute("appendClass", "master");
			model.addAttribute("message", new Message("Error : "+e.getMessage(), "alert-danger"));
			model.addAttribute("staticSeo", staticSeo);
			return "admin/static-seo-add";
			// TODO: handle exception
		}
	}
	
	@PostMapping("/static-seo/update")
	public String updateStaticSeo(@Valid @ModelAttribute("staticSeo") StaticSeo staticSeo,BindingResult result,Model model) {
		
		try {
			if(result.hasErrors()) {
				model.addAttribute("title", "Corpseed Dashboard | Edit Static SEO");
				model.addAttribute("appendClass", "master");
				model.addAttribute("message", new Message("Please fill out the blank fields !!", "alert-danger"));
				model.addAttribute("staticSeo", staticSeo);
				return "admin/static-seo-edit";
			}
			StaticSeo findStaticSeo=this.staticSeoService.findByPageAndUuidNot(staticSeo.getPage(),staticSeo.getUuid());
			if(findStaticSeo!=null) {
				model.addAttribute("title", "Corpseed Dashboard | Edit Static SEO");
				model.addAttribute("appendClass", "master");
				model.addAttribute("staticSeo", staticSeo);
				
				if(findStaticSeo.getDeleteStatus()==1)
					model.addAttribute("message", new Message("Page exist in Trash !!", "alert-danger"));
				else
					model.addAttribute("message", new Message("Page already exist !!", "alert-danger"));
								
				return "admin/static-seo-edit";
			}
			this.staticSeoService.save(staticSeo);
			
			return "redirect:/admin/master/static-seo";
		} catch (Exception e) {
			e.printStackTrace();
			model.addAttribute("title", "Corpseed Dashboard | Edit Static SEO");
			model.addAttribute("appendClass", "master");
			model.addAttribute("message", new Message("Error : "+e.getMessage(), "alert-danger"));
			model.addAttribute("staticSeo", staticSeo);
			return "admin/static-seo-edit";
			// TODO: handle exception
		}
	}
}
