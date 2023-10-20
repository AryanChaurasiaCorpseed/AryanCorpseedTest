package com.corpseed.controller;

import java.io.PrintWriter;
import java.security.Principal;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import com.corpseed.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
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

import com.corpseed.entity.History;
import com.corpseed.entity.industryentity.Industry;
import com.corpseed.entity.industryentity.IndustryDetails;
import com.corpseed.entity.SiteMapUrl;
import com.corpseed.entity.User;
import com.corpseed.util.Message;
import com.corpseed.serviceImpl.CommonServices;
import com.corpseed.serviceImpl.HistoryService;
import com.corpseed.serviceImpl.IndustryService;
import com.corpseed.serviceImpl.servicesserviceimpl.ServicesService;
import com.corpseed.serviceImpl.SiteMapService;

@Controller
@RequestMapping(value = "/admin/industry")
public class IndustryController {

	@Autowired
	private CommonServices commonServices;
	
	@Autowired
	private IndustryService industryService;
	
	@Autowired
	private SiteMapService siteMapService;
	
	@Autowired
	private HistoryService historyService;
	
	@Autowired
	private ServicesService servicesService;

	@Autowired
	private ProductService productService;
		
	@GetMapping("/")
	public String industry(Model model) {
		model.addAttribute("title", "Corpseed Dashboard | Manage Industry");
		model.addAttribute("appendClass", "industry");
		model.addAttribute("industry", this.industryService.getAllIndustry());
		return "admin/industry";
	}
	
	@GetMapping("/add")
	public String industryAdd(Model model) {
		model.addAttribute("title", "Corpseed Dashboard | Add Industry");
		model.addAttribute("appendClass", "industry");
		model.addAttribute("industry", new Industry());
		return "admin/industry-add";
	}
	@GetMapping("/edit/{uuid}")
	public String industryEdit(@PathVariable("uuid") String uuid,Model model) {
		model.addAttribute("title", "Corpseed Dashboard | Edit Industry");
		model.addAttribute("appendClass", "industry");
		model.addAttribute("industry", this.industryService.findByUuid(uuid));
		return "admin/industry-edit";
	}
	
	@GetMapping("/delete/{uuid}")
	public String industryDelete(@PathVariable("uuid") String uuid,Model model,Principal principal
			,HttpServletRequest req) {	
		if(this.commonServices.getLoginedUser(principal).getRole().equals("ROLE_ADMIN")) {
			Industry industry=this.industryService.findByUuid(uuid);
			if(industry!=null) {
				industry.setDeleteStatus(1);
				this.industryService.saveIndustry(industry);

				User user=this.commonServices.getLoginedUser(principal);
				
				//adding category in history
				this.historyService.savehistory(new History(0, this.commonServices.getUUID(),
						"Industry", industry.getId(), this.commonServices.getBrowser(req), 
						this.commonServices.getIpAddress(req), user.getId(), user.getFirstName()+" "+user.getLastName()
						, this.commonServices.getToday(), this.commonServices.getTime(),industry.getIndustryName()));		
				
				//updating industry details
				List<IndustryDetails> industryDetails=industry.getIndustryDetails();
				if(!industryDetails.isEmpty()) {
					industryDetails.forEach(ind->ind.setDeleteStatus(1));
					this.industryService.saveAllIndustryDetails(industryDetails);
				}
				//updating sitemap url
				SiteMapUrl siteMap = this.siteMapService.findByTypeAndUid("Industry", industry.getId());
				if(siteMap!=null) {
					siteMap.setStatus(2);
					this.siteMapService.saveUrl(siteMap);
				}	
						
			}
			return "redirect:/admin/industry/";	
		}else {
			return "error/403";
		}
	}
	
	@PostMapping("/add")
	public String saveIndustry(@Valid @ModelAttribute("industry") Industry industry,BindingResult result,
			HttpSession session,Model model,Principal principal) {
		try {			
			if(result.hasErrors()) {
				model.addAttribute("title", "Corpseed Dashboard | Add Industry");
				model.addAttribute("appendClass", "industry");
				model.addAttribute("industry", industry);
				session.setAttribute("message", new Message("Please fill out the blank fields !!", "alert-danger"));
				return "admin/industry-add";
			}
			
			Industry existIndustry=this.industryService.findBySlug(industry.getSlug());
			if(existIndustry!=null) {
				model.addAttribute("title", "Corpseed Dashboard | Add Industry");
				model.addAttribute("appendClass", "industry");
				model.addAttribute("industry", industry);
				if(existIndustry.getDeleteStatus()==1)
					session.setAttribute("message", new Message("industry Exist in Trash !!", "alert-danger"));
				else
					session.setAttribute("message", new Message("URL Already Exist !!", "alert-danger"));
				
				return "admin/industry-add";	
			}
				industry.setUuid(this.commonServices.getUUID());
				industry.setAddedByUUID(this.commonServices.getLoginedUser(principal).getUuid());
							
				Industry saveIndustry=this.industryService.saveIndustry(industry);
					
				this.siteMapService.saveUrl(new SiteMapUrl(0, this.commonServices.getUUID(),saveIndustry.getId(), "Industry", "/industry/"+saveIndustry.getSlug(), 1, this.commonServices.getToday()));
				return "redirect:/admin/industry/";				
						
		} catch (Exception e) {
			model.addAttribute("title", "Corpseed Dashboard | Add Industry");
			model.addAttribute("appendClass", "industry");
			model.addAttribute("industry", industry);
			session.setAttribute("message", new Message("Error : " + e.getMessage(), "alert-danger"));
			return "admin/industry-add";
		}
	}
	@PostMapping("/update/{uuid}")
	public String updateIndustry(@Valid @ModelAttribute("industry") Industry industry,BindingResult result,
			@PathVariable("uuid") String uuid,HttpSession session,Model model,Principal principal) {
		try {			
			if(result.hasErrors()) {
				model.addAttribute("title", "Corpseed Dashboard | Edit Industry");
				model.addAttribute("appendClass", "industry");
				model.addAttribute("industry", industry);				
				session.setAttribute("message", new Message("Please fill out the blank fields !!", "alert-danger"));
				return "admin/industry-edit";
			}
			Industry findIndustry=this.industryService.findByUuid(uuid);
			if(findIndustry==null) {
				return "error/404";
			}
				Industry existIndustry=this.industryService.findBySlugAndUuidNot(industry.getSlug(),uuid);
				if(existIndustry!=null) {
					model.addAttribute("title", "Corpseed Dashboard | Edit Industry");
					model.addAttribute("appendClass", "industry");
					model.addAttribute("industry", industry);
					if(existIndustry.getDeleteStatus()==1)
						session.setAttribute("message", new Message("Industry Exist in Trash !!", "alert-danger"));
					else
						session.setAttribute("message", new Message("URL Already Exist !!", "alert-danger"));
						
					return "admin/industry-edit";
				}
					industry.setId(findIndustry.getId());
					industry.setUuid(findIndustry.getUuid());
					industry.setAddedByUUID(this.commonServices.getLoginedUser(principal).getUuid());
								
					Industry saveIndustry=this.industryService.saveIndustry(industry);
						//adding sitemap
						SiteMapUrl siteMap=this.siteMapService.findByTypeAndUid("Industry", saveIndustry.getId());
						if(siteMap==null) {
							this.siteMapService.saveUrl(new SiteMapUrl(0, this.commonServices.getUUID(), saveIndustry.getId(), "Industry", "/industry/"+saveIndustry.getSlug(), 1, this.commonServices.getToday()));
						}else {
							siteMap.setUrl("/industry/"+saveIndustry.getSlug());
							this.siteMapService.saveUrl(siteMap);
						}
						return "redirect:/admin/industry/";
					
		} catch (Exception e) {
			model.addAttribute("title", "Corpseed Dashboard | Edit Industry");
			model.addAttribute("appendClass", "industry");
			model.addAttribute("industry", industry);
			session.setAttribute("message", new Message("Error : " + e.getMessage(), "alert-danger"));
			return "admin/industry-edit";
		}
	}
	
	@GetMapping("/details/{industryUUID}")
	public String serviceDetails(@PathVariable(name="industryUUID") String industryUUID,Model model) {
		model.addAttribute("title", "Corpseed Dashboard | Industry Details");
		model.addAttribute("appendClass", "industry");
		model.addAttribute("industryUUID", industryUUID);
		Industry industry=this.industryService.findByUuid(industryUUID);
		model.addAttribute("detail", this.industryService.getAllIndustryDetails(industry));
		return "admin/industrydetails";
	}
	@GetMapping("/details/add/{industryUUID}")
	public String addDetails(@PathVariable("industryUUID") String industryUUID,Model model) {
		model.addAttribute("title", "Corpseed Dashboard | Add Industry");
		model.addAttribute("appendClass", "industry");
		model.addAttribute("products", this.productService.getAllProducts());
		model.addAttribute("industryUUID", industryUUID);
		model.addAttribute("details", new IndustryDetails());
		model.addAttribute("services", this.servicesService.getAllServicesByStatus("1"));
		model.addAttribute("industries", this.industryService.getIndustryByDisplsyStatus("1"));
		return "admin/industrydetails-add";
	}
	
	@GetMapping("/details/edit/{detailsUUID}")
	public String editDetails(@PathVariable("detailsUUID") String detailsUUID,Model model) {
		model.addAttribute("title", "Corpseed Dashboard | Edit Industry Details");
		model.addAttribute("appendClass", "industry");
		model.addAttribute("products", this.productService.getAllProducts());
		model.addAttribute("detailsUUID", detailsUUID);
		IndustryDetails details=this.industryService.getIndustryDetailsByUuid(detailsUUID);
		model.addAttribute("services", this.servicesService.getAllServicesByStatus("1"));
		model.addAttribute("industries", this.industryService.getIndustryByDisplsyStatus("1"));
		
		String description=details.getDescription();
		while(description.contains("<iframe src=\"https://docs.google.com/gview?url=")||description.contains("<iframe src=\"http://docs.google.com/gview?url=")) {
			description = description.replace("<iframe src=\"http://docs.google.com/gview?url=", "pdfview=");
			description = description.replace("<iframe src=\"https://docs.google.com/gview?url=", "pdfview=");
			description = description.replace("&embedded=true\" frameborder=\"0\" webkitallowfullscreen mozallowfullscreen allowfullscreen style=\"width: 100%; height: 700px;\"></iframe>", "=pdfview");
			description=description.replace("&embedded=true\" frameborder=\"0\" webkitallowfullscreen mozallowfullscreen allowfullscreen style=\"width: 100%; height: 500px;\">","=pdfview");
		}
		details.setDescription(description);		
		
		model.addAttribute("industryUUID", details.getIndustry().getUuid());
		model.addAttribute("details", details);
		return "admin/industrydetails-edit";
	}
	@GetMapping("/details/delete/{uuid}")
	public String deleteDetails(@PathVariable("uuid") String uuid,Principal principal,HttpServletRequest req) {
		if(this.commonServices.getLoginedUser(principal).getRole().equals("ROLE_ADMIN")) {
			IndustryDetails industryDetails=this.industryService.getIndustryDetailsByUuid(uuid);			
			if(industryDetails!=null) {
				industryDetails.setDeleteStatus(1);
				this.industryService.saveIndustryDetails(industryDetails);
				
				User user=this.commonServices.getLoginedUser(principal);
				
				//adding category in history
				this.historyService.savehistory(new History(0, this.commonServices.getUUID(),
						"Industry Details", industryDetails.getId(), this.commonServices.getBrowser(req), 
						this.commonServices.getIpAddress(req), user.getId(), user.getFirstName()+" "+user.getLastName()
						, this.commonServices.getToday(), this.commonServices.getTime(),industryDetails.getTabName()));
			}
			return "redirect:/admin/industry/details/"+industryDetails.getIndustry().getUuid();
		}else {
			return "error/403";
		}
	}
	@PostMapping("/details/edit/{detailsUUID}")
	public String updateDetails(@Valid @ModelAttribute("details") IndustryDetails details,BindingResult result,@RequestParam("industryUUID") String industryUUID,
			@PathVariable("detailsUUID") String detailsUUID,Model model,HttpSession session,Principal principal) {
		
		try {
			if(result.hasErrors()) {
				model.addAttribute("title", "Corpseed Dashboard | Edit industry details");
				model.addAttribute("appendClass", "industry");
				model.addAttribute("products", this.productService.getAllProducts());
				model.addAttribute("details", details);
				model.addAttribute("detailsUUID", detailsUUID);
				model.addAttribute("industryUUID", industryUUID);
				session.setAttribute("message", new Message("Please fill out the blank fields !!", "alert-danger"));
				model.addAttribute("services", this.servicesService.getAllServicesByStatus("1"));
				model.addAttribute("industries", this.industryService.getIndustryByDisplsyStatus("1"));
				return "admin/industrydetails-edit";
			}
			Industry industry=this.industryService.findByUuid(industryUUID);
			IndustryDetails existDetails=this.industryService.findByTabNameAndIndustryAndUuidNot(details.getTabName(),industry,detailsUUID);
			IndustryDetails existDetails1=this.industryService.findByDisplayOrderAndIndustryAndUuidNot(details.getDisplayOrder(),industry,detailsUUID);
			if(existDetails!=null||existDetails1!=null) {
				model.addAttribute("title", "Corpseed Dashboard | Edit Service details");
				model.addAttribute("appendClass", "industry");
				model.addAttribute("products", this.productService.getAllProducts());
				model.addAttribute("details", details);
				model.addAttribute("detailsUUID", detailsUUID);
				model.addAttribute("industryUUID", industryUUID);
				if(existDetails.getDeleteStatus()==1)
					session.setAttribute("message", new Message("Industry Details exist in Trash !!", "alert-danger"));
				else
					session.setAttribute("message", new Message("Either tab name or display order is duplicate !!", "alert-danger"));
				
				model.addAttribute("services", this.servicesService.getAllServicesByStatus("1"));
				model.addAttribute("industries", this.industryService.getIndustryByDisplsyStatus("1"));
				
				return "admin/industrydetails-edit";
			}
				IndustryDetails findDetails=this.industryService.getIndustryDetailsByUuid(detailsUUID);
				details.setId(findDetails.getId());
				details.setFormShowStatus(findDetails.getFormShowStatus());
				details.setIndustry(industry);
				details.setUuid(findDetails.getUuid());
				details.setAddedByUUID(this.commonServices.getLoginedUser(principal).getUuid());
				String description=details.getDescription();
				
				description=description.replace("<div class=\"table-width\">","");
				description=description.replace("</table>\r\n"
						+ "</div>","</table>");

				description=description.replace("<table","<div class='table-width'><table");
				description=description.replace("</table>","</table></div>");
				
				if(!description.contains("playVideo(")&&description.contains("<iframe")) {
					
					String description1=description.substring(0,description.indexOf("<iframe"));
					description1+="<div class='text-center mt-2'><div class='vediosec' id='"+findDetails.getUuid()+"' onclick=\"playVideo('"+findDetails.getUuid()+"','"+(findDetails.getUuid()+1)+"')\">"
							+"<img src='/assets/img/logo.png' class='img-fluid' alt='Corpseed' width='151' height='28'>"
					+"<div class='playbtn '>"
                    +"<h1>Start your <b class='text-primary'>"+industry.getIndustryName()+"</b> today!</h1>"
                        +"<a href='javascript:void(0)'>"
                       +"<svg width='103' height='103' viewBox='0 0 103 103' fill='none' xmlns='http://www.w3.org/2000/svg'>"
                            +"<g filter=\"url('#filter0_d')\">"
                                +"<ellipse cx='51.3134' cy='47.5' rx='26.3134' ry='26.5' fill='white'></ellipse>"
                                +"</g>"
                                +"<path d='M48 40L58.68 47.1808L48 54.3617V40Z' stroke='black' stroke-width='2' stroke-linecap='round' stroke-linejoin='round'></path>"
                                +"<defs>"
                                +"<filter id='filter0_d' x='0' y='0' width='102.627' height='103' filterUnits='userSpaceOnUse' color-interpolation-filters='sRGB'>"
                                +"<feFlood flood-opacity='0' result='BackgroundImageFix'></feFlood>"
                                +"<feColorMatrix in='SourceAlpha' type='matrix' values='0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 127 0'></feColorMatrix>"
                                +"<feOffset dy='4'></feOffset>"
                                +"<feGaussianBlur stdDeviation='12.5'></feGaussianBlur>"
                                +"<feColorMatrix type='matrix' values='0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0.15 0'></feColorMatrix>"
                                +"<feBlend mode='normal' in2='BackgroundImageFix' result='effect1_dropShadow'></feBlend>"
                                +"<feBlend mode='normal' in='SourceGraphic' in2='effect1_dropShadow' result='shape'></feBlend>"
                                +"</filter>"
                                +"</defs>"
                                +"</svg>"
                            +"</a>"
                        +"</div>"
                    +"</div></div>"
                +"<div id='"+(findDetails.getUuid()+1)+"' style='display:none'>";
                
                String iFrame=description.substring(description.indexOf("<iframe"),(description.indexOf("</iframe>")+9));
                int lindex=iFrame.indexOf("width=")-2;
                if(lindex<0)lindex=iFrame.indexOf("style=")-2;
                String src="#";
                if(lindex>4)
                	src=iFrame.substring((iFrame.indexOf("src=")+5),lindex)+"?autoplay=1&mute=1&enablejsapi=1";
                
                description1+="<iframe width='640' height='360' src='"+src+"' allow='autoplay' frameborder='0'></iframe></div>";
                description1+=description.substring((description.indexOf("</iframe>")+9));
                details.setDescription(description1);					    
				}else details.setDescription(description);	
			    			    
			    description=details.getDescription();
			    
			    while(description.contains("pdfview=")) {
                	description=description.replace("pdfview=", "<iframe src=\"https://docs.google.com/gview?url=");
                	description=description.replace("=pdfview", "&embedded=true\" frameborder=\"0\" webkitallowfullscreen mozallowfullscreen allowfullscreen style=\"width: 100%; height: 700px;\"></iframe>");
                }
			    description=description.replace("left: 0; width: 100%; height: 0; position: relative; padding-bottom: 56.25%;", "");
			    details.setDescription(description);
				this.industryService.saveIndustryDetails(details);
				
				return "redirect:/admin/industry/details/"+industryUUID;
					
		} catch (Exception e) {
			e.printStackTrace();
			model.addAttribute("title", "Corpseed Dashboard | Edit Service details");
			model.addAttribute("appendClass", "industry");
			model.addAttribute("products", this.productService.getAllProducts());
			model.addAttribute("details", details);
			model.addAttribute("detailsUUID", detailsUUID);
			model.addAttribute("industryUUID", industryUUID);
			session.setAttribute("message", new Message("Error : "+e.getMessage(), "alert-danger"));
			model.addAttribute("services", this.servicesService.getAllServicesByStatus("1"));
			model.addAttribute("industries", this.industryService.getIndustryByDisplsyStatus("1"));
			return "admin/industrydetails-edit";
		}
	}
	
	@PostMapping("/details/add/{industryUUID}")
	public String saveDetails(@Valid @ModelAttribute("details") IndustryDetails details,BindingResult result,
			@PathVariable("industryUUID") String industryUUID,Model model,Principal principal,HttpSession session) {
		model.addAttribute("title", "Corpseed Dashboard | Add Details");
		model.addAttribute("appendClass", "industry");		
		try {			
			if(result.hasErrors()) {
				model.addAttribute("details", details);
				model.addAttribute("products", this.productService.getAllProducts());
				session.setAttribute("message", new Message("Please fill out the blank fields !!", "alert-danger"));
				model.addAttribute("services", this.servicesService.getAllServicesByStatus("1"));
				model.addAttribute("industries", this.industryService.getIndustryByDisplsyStatus("1"));
				return "admin/industrydetails-add";
			}
			Industry industry=this.industryService.findByUuid(industryUUID);
			
			//checking details already existed or not
			IndustryDetails existDetails=this.industryService.isIndustryDetailsExist(details.getTabName(),industry);
			IndustryDetails existDetails1=this.industryService.findByDisplayOrderAndIndustry(details.getDisplayOrder(),industry);
			
			if(existDetails!=null||existDetails1!=null) {
				model.addAttribute("details", details);
				model.addAttribute("products", this.productService.getAllProducts());
				if(existDetails.getDeleteStatus()==1)
					session.setAttribute("message", new Message("Industry Details exist in Trash !!", "alert-danger"));
				else
					session.setAttribute("message", new Message("Either tab name or display order is duplicate !!", "alert-danger"));
				
				model.addAttribute("services", this.servicesService.getAllServicesByStatus("1"));	
				model.addAttribute("industries", this.industryService.getIndustryByDisplsyStatus("1"));
				return "admin/industrydetails-add";
			}
				details.setUuid(this.commonServices.getUUID());
				details.setIndustry(industry);
				details.setAddedByUUID(this.commonServices.getLoginedUser(principal).getUuid());
				details.setFormShowStatus("2");
				
				String description=details.getDescription();

				description=description.replace("<table","<div class='table-width'><table");
				description=description.replace("</table>","</table></div>");
				
			    if(!description.contains("playVideo(")&&description.contains("<iframe")) {
					
					String description1=description.substring(0,description.indexOf("<iframe"));
					description1+="<div class='text-center mt-2'><div class='vediosec' id='"+details.getUuid()+"' onclick=\"playVideo('"+details.getUuid()+"','"+(details.getUuid()+1)+"')\">"
							+"<img src='/assets/img/logo.png' class='img-fluid' alt='Corpseed' width='151' height='28'>"
					+"<div class='playbtn '>"
                    +"<h1>Start your <b class='text-primary'>"+industry.getIndustryName()+"</b> today!</h1>"
                        +"<a href='javascript:void(0)'>"
                       +"<svg width='103' height='103' viewBox='0 0 103 103' fill='none' xmlns='http://www.w3.org/2000/svg'>"
                            +"<g filter=\"url('#filter0_d')\">"
                                +"<ellipse cx='51.3134' cy='47.5' rx='26.3134' ry='26.5' fill='white'></ellipse>"
                                +"</g>"
                                +"<path d='M48 40L58.68 47.1808L48 54.3617V40Z' stroke='black' stroke-width='2' stroke-linecap='round' stroke-linejoin='round'></path>"
                                +"<defs>"
                                +"<filter id='filter0_d' x='0' y='0' width='102.627' height='103' filterUnits='userSpaceOnUse' color-interpolation-filters='sRGB'>"
                                +"<feFlood flood-opacity='0' result='BackgroundImageFix'></feFlood>"
                                +"<feColorMatrix in='SourceAlpha' type='matrix' values='0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 127 0'></feColorMatrix>"
                                +"<feOffset dy='4'></feOffset>"
                                +"<feGaussianBlur stdDeviation='12.5'></feGaussianBlur>"
                                +"<feColorMatrix type='matrix' values='0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0.15 0'></feColorMatrix>"
                                +"<feBlend mode='normal' in2='BackgroundImageFix' result='effect1_dropShadow'></feBlend>"
                                +"<feBlend mode='normal' in='SourceGraphic' in2='effect1_dropShadow' result='shape'></feBlend>"
                                +"</filter>"
                                +"</defs>"
                                +"</svg>"
                            +"</a>"
                        +"</div>"
                    +"</div></div>"
                +"<div id='"+(details.getUuid()+1)+"' style='display:none'>";
                
                String iFrame=description.substring(description.indexOf("<iframe"),(description.indexOf("</iframe>")+9));
                int lindex=iFrame.indexOf("style=")-2;
                if(lindex<0)lindex=iFrame.indexOf("width=")-2;
                String src="#";
                if(lindex>4)
                	src=iFrame.substring((iFrame.indexOf("src=")+5),lindex)+"?autoplay=1&mute=1&enablejsapi=1";
                
                description1+="<iframe width='640' height='360' src='"+src+"' allow='autoplay' frameborder='0'></iframe></div>";
                description1+=description.substring((description.indexOf("</iframe>")+9));
                details.setDescription(description1);					    
				}else details.setDescription(description);
				description=details.getDescription();
			    while(description.contains("pdfview=")) {
	            	description=description.replace("pdfview=", "<iframe src=\"https://docs.google.com/gview?url=");
	            	description=description.replace("=pdfview", "&embedded=true\" frameborder=\"0\" webkitallowfullscreen mozallowfullscreen allowfullscreen style=\"width: 100%; height: 700px;\"></iframe>");
	            }
			    description=description.replace("left: 0; width: 100%; height: 0; position: relative; padding-bottom: 56.25%;", "");
			    details.setDescription(description);
				this.industryService.saveIndustryDetails(details);			
				return "redirect:/admin/industry/details/"+industryUUID;
			
		} catch (Exception e) {
			e.printStackTrace();
			model.addAttribute("products", this.productService.getAllProducts());
			model.addAttribute("details", details);
			session.setAttribute("message", new Message("Error : "+e.getMessage(), "alert-danger"));
			model.addAttribute("services", this.servicesService.getAllServicesByStatus("1"));
			model.addAttribute("industries", this.industryService.getIndustryByDisplsyStatus("1"));
			return "admin/industrydetails-add";
		}
	}
	@GetMapping("/details/form-update")
	@ResponseBody
	public void updateIndustryDetails(@RequestParam("uuid") String uuid,@RequestParam("status") String status,PrintWriter pw) {
		IndustryDetails industrydetails=this.industryService.getIndustryDetailsByUuid(uuid);
		if(industrydetails!=null) {
			industrydetails.setFormShowStatus(status);
			IndustryDetails saveIndustryDetails = this.industryService.saveIndustryDetails(industrydetails);
			if(saveIndustryDetails!=null)pw.write("pass");
			else pw.write("fail");
		}else pw.write("fail");
	}
}
