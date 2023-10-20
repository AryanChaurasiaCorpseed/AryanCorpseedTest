package com.corpseed.controller;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.springframework.core.env.Environment;
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

import com.corpseed.entity.couponentity.Coupon;
import com.corpseed.entity.couponentity.CouponServices;
import com.corpseed.entity.History;
import com.corpseed.entity.serviceentity.Services;
import com.corpseed.entity.User;
import com.corpseed.util.Message;
import com.corpseed.serviceImpl.CommonServices;
import com.corpseed.serviceImpl.couponserviceimpl.CouponService;
import com.corpseed.serviceImpl.couponserviceimpl.CouponServicesService;
import com.corpseed.serviceImpl.HistoryService;
import com.corpseed.serviceImpl.servicesserviceimpl.ServicesService;

@Controller
@RequestMapping("/admin/coupons")
public class CouponController {

	@Autowired
	private CouponService couponService;
	
	@Autowired
	private CommonServices commonService;
	
	@Autowired
	private ServicesService servicesService;
	
	@Autowired
	private Environment env;
	
	@Autowired
	private CouponServicesService couponServicesService;
	
	@Autowired
	private HistoryService historyService;
	
	@GetMapping("/")
	public String coupons(Model model) {
		model.addAttribute("title", "Corpseed Dashboard | Manage Coupons");
		model.addAttribute("appendClass", "coupons");
		model.addAttribute("coupon", this.couponService.getAllCoupons());
		return "admin/coupons";
	}
	
	@GetMapping("/add")
	public String addCoupon(Model model) {
		model.addAttribute("services", this.servicesService.findByDisplayStatusAndPackageNotEmpty("1"));
		model.addAttribute("title", "Corpseed Dashboard | Add Coupons");
		model.addAttribute("appendClass", "coupons");
		model.addAttribute("coupon", new Coupon());
		return "admin/coupons-add";
	}
	
	@GetMapping("/edit/{couponUUID}")
	public String editCoupon(@PathVariable("couponUUID") String couponUUID,Model model) {
		Coupon coupon=this.couponService.getCouponByUuid(couponUUID);
		model.addAttribute("title", "Corpseed Dashboard | Edit Coupon");
		model.addAttribute("appendClass", "coupons");
		model.addAttribute("couponUUID", couponUUID);
		model.addAttribute("coupon", coupon);

		model.addAttribute("services", this.servicesService.findByDisplayStatusAndPackageNotEmpty("1"));
		model.addAttribute("existServices", coupon.getCouponServices()
				.stream().map(CouponServices::getServices)
				.collect(Collectors.toList()));
		return "admin/coupons-edit";
	}

	@GetMapping("/delete/{uuid}")
	public String deleteCoupon(@PathVariable("uuid") String uuid,Principal principal,HttpServletRequest req) {
		if(this.commonService.getLoginedUser(principal).getRole().equals("ROLE_ADMIN")) {
			Coupon coupon=this.couponService.getCouponByUuid(uuid);
			if(coupon!=null) {
				coupon.setDeleteStatus(1);
				this.couponService.saveCoupon(coupon);

				User user=this.commonService.getLoginedUser(principal);
				
				//adding category in history
				this.historyService.savehistory(new History(0, this.commonService.getUUID(),
						"Coupon", coupon.getId(), this.commonService.getBrowser(req), 
						this.commonService.getIpAddress(req), user.getId(), user.getFirstName()+" "+user.getLastName()
						, this.commonService.getToday(), this.commonService.getTime(),coupon.getTitle()));		
				
				//updating coupon service				
				List<CouponServices> couponServices=coupon.getCouponServices();
				if(!couponServices.isEmpty()) {
					couponServices.forEach(cs->cs.setDeleteStatus(1));
					this.couponServicesService.saveAllServices(couponServices);
				}
				
			}
			return "redirect:/admin/coupons/";
		}else {
			return "error/403";
		}
	}
	@PostMapping("/add")
	public String saveCoupon(@Valid @ModelAttribute("coupon") Coupon coupon,BindingResult result,
			@RequestParam("serviceList") List<String> serviceList,HttpSession session,Principal principal,Model model) {
		try {
			model.addAttribute("title", "Corpseed Dashboard | Add Coupons");
			model.addAttribute("appendClass", "coupons");
			model.addAttribute("services", this.servicesService.findByDisplayStatusAndPackageNotEmpty("1"));
			
			if(result.hasErrors()) {
				model.addAttribute("coupon", coupon);
				session.setAttribute("message", new Message("Please fill out the blank fields !!", "alert-danger"));
				return "admin/coupons-add";
			}		
			if(coupon.getType().equalsIgnoreCase("percentage")&&(coupon.getMaximumDiscount()==null||coupon.getMaximumDiscount().length()<=0||coupon.getMaximumDiscount().equals("0"))) {
				model.addAttribute("coupon", coupon);
				session.setAttribute("message", new Message("Please enter maximum discount price greater than 0 !!", "alert-danger"));
				return "admin/coupons-add";
			}
			Coupon findCoupon=this.couponService.findByTitle(coupon.getTitle());
			if(findCoupon!=null) {
				model.addAttribute("coupon", coupon);
				if(findCoupon.getDeleteStatus()==1)
					session.setAttribute("message", new Message("Coupon exist in Trash !!", "alert-danger"));
				else
					session.setAttribute("message", new Message("Coupon already exist, Please add or update services !!", "alert-danger"));
				
				return "admin/coupons-add";
			}
			if(serviceList.isEmpty()) {
				model.addAttribute("services", this.servicesService.getAllServicesByStatus("1"));
				model.addAttribute("coupon", coupon);
				session.setAttribute("message", new Message("Please select atleast on service !!", "alert-danger"));
				return "admin/coupons-add";
			}
			String loginuuid=this.commonService.getLoginedUser(principal).getUuid();
				String maxDiscount=coupon.getMaximumDiscount();
				if(coupon.getType().equals("fixed"))maxDiscount="0";
				coupon.setUuid(this.commonService.getUUID());
				coupon.setAddedByUUID(loginuuid);
				coupon.setMaximumDiscount(maxDiscount);
				coupon.setCouponPushStatus(1);
				Coupon saveCoupon = this.couponService.saveCoupon(coupon);
			
				List<CouponServices> couponServiceList=new ArrayList<>();					
				String serviceLst="";
				if(coupon.getServiceType().equals("selected"))
				if(!serviceList.isEmpty()) {
					for (String id : serviceList) {
						//getting service object
						Services services=this.servicesService.getServiceById(id);
						serviceLst+=services.getProductNo()+",";
						
						couponServiceList.add(new CouponServices(0, this.commonService.getUUID(), services, saveCoupon));
					}
					//saving all coupon's services
					this.couponServicesService.saveAllServices(couponServiceList);
				}
				
				String token=this.env.getProperty("crm.token");
				String POST_URL=env.getProperty("crm.domain.name")+"update-coupon.html";
				String POST_PARAMS = "uuid="+saveCoupon.getUuid()+"&title="+saveCoupon.getTitle()+"&value="+saveCoupon.getValue()+"&type="+saveCoupon.getType()+"&startDate="+saveCoupon.getStartDate()+"&endDate="+saveCoupon.getEndDate()+"&displayStatus="
						+saveCoupon.getDisplayStatus()+"&addedByUUID="+saveCoupon.getAddedByUUID()+"&postDate="+saveCoupon.getPostDate()+"&modifyDate="+saveCoupon.getModifyDate()+"&maximumDiscount="
						+saveCoupon.getMaximumDiscount()+"&token="+token+"&serviceType="+saveCoupon.getServiceType()+"&services="+serviceLst;
				int response = this.commonService.callPostURL(POST_URL, POST_PARAMS);
				
				if(response==200&&saveCoupon.getCouponPushStatus()==2) {
					saveCoupon.setCouponPushStatus(1);
					this.couponService.saveCoupon(saveCoupon);
				}else if(response!=200&&saveCoupon.getCouponPushStatus()==1) {
					saveCoupon.setCouponPushStatus(2);
					this.couponService.saveCoupon(saveCoupon);
				}				
				
				return "redirect:/admin/coupons/";							
			
		} catch (Exception e) {
			model.addAttribute("services", this.servicesService.getAllServicesByStatus("1"));
			model.addAttribute("coupon", coupon);
			session.setAttribute("message", new Message("Error : " + e.getMessage(), "alert-danger"));
			return "admin/coupons-add";
		}
	}
	@PostMapping("/update")
	public String updateCoupon(@Valid @ModelAttribute("coupon") Coupon coupon,BindingResult result,
			@RequestParam("serviceList") List<String> serviceName,HttpSession session,Principal principal,Model model) {
		try {		
			List<Services> existServices=new ArrayList<>();
			if(result.hasErrors()) {
				model.addAttribute("title", "Corpseed Dashboard | Edit Coupons");
				model.addAttribute("appendClass", "coupons");	
				if(coupon.getServiceType().equals("selected"))
					for (String id : serviceName) {
						existServices.add(this.servicesService.getServiceById(id));
					}				
				model.addAttribute("existServices", existServices);
				model.addAttribute("services", this.servicesService.findByDisplayStatusAndPackageNotEmpty("1"));
				model.addAttribute("coupon", coupon);
				session.setAttribute("message", new Message("Please fill out the blank fields !!", "alert-danger"));
				return "admin/coupons-edit";
			}	
			if(coupon.getType().equalsIgnoreCase("percentage")&&(coupon.getMaximumDiscount()==null||coupon.getMaximumDiscount().length()<=0||coupon.getMaximumDiscount().equals("0"))) {
				model.addAttribute("title", "Corpseed Dashboard | Edit Coupons");
				model.addAttribute("appendClass", "coupons");			
				if(coupon.getServiceType().equals("selected"))
				for (String id : serviceName) {
					existServices.add(this.servicesService.getServiceById(id));
				}
				model.addAttribute("existServices", existServices);
				model.addAttribute("services", this.servicesService.findByDisplayStatusAndPackageNotEmpty("1"));
				model.addAttribute("coupon", coupon);
				session.setAttribute("message", new Message("Please enter maximum discount price greater than 0 !!", "alert-danger"));
				return "admin/coupons-edit";
			}
			if(serviceName.isEmpty()) {
				model.addAttribute("title", "Corpseed Dashboard | Edit Coupons");
				model.addAttribute("appendClass", "coupons");			
				if(coupon.getServiceType().equals("selected"))
				for (String id : serviceName) {
					existServices.add(this.servicesService.getServiceById(id));
				}
				model.addAttribute("existServices", existServices);
				model.addAttribute("services", this.servicesService.findByDisplayStatusAndPackageNotEmpty("1"));
				model.addAttribute("coupon", coupon);
				session.setAttribute("message", new Message("Please select atleast on service !!", "alert-danger"));
				return "admin/coupons-edit";
			}	
			Coupon existCoupon = this.couponService.findByTitleAndUuidNot(coupon.getTitle(),coupon.getUuid());
			if(existCoupon!=null) {
				model.addAttribute("title", "Corpseed Dashboard | Edit Coupons");
				model.addAttribute("appendClass", "coupons");			
				if(coupon.getServiceType().equals("selected"))
				for (String id : serviceName) {
					existServices.add(this.servicesService.getServiceById(id));
				}
				model.addAttribute("existServices", existServices);
				model.addAttribute("services", this.servicesService.findByDisplayStatusAndPackageNotEmpty("1"));
				model.addAttribute("coupon", coupon);
				if(existCoupon.getDeleteStatus()==1)
					session.setAttribute("message", new Message("Coupon exist in Trash !!", "alert-danger"));
				else
					session.setAttribute("message", new Message("Coupon already exist,Please add or update services !!", "alert-danger"));
					
				return "admin/coupons-edit";
			}			
			coupon.setAddedByUUID(this.commonService.getLoginedUser(principal).getUuid());
			if(coupon.getType().equals("fixed"))coupon.setMaximumDiscount("0");
			//saving coupons				
			Coupon saveCoupon=this.couponService.saveCoupon(coupon);
			
			List<CouponServices> couponServiceList=new ArrayList<>();
			String serviceLst="";
			if(coupon.getServiceType().equals("selected")) {
				
			for (String id : serviceName) {
				Services serviceById = this.servicesService.getServiceById(id);
				serviceLst+=serviceById.getProductNo()+",";
				CouponServices couponService=this.couponServicesService.findByCouponAndServices(saveCoupon, serviceById);
				if(couponService==null) {
					couponServiceList.add(new CouponServices(0, this.commonService.getUUID(), serviceById, saveCoupon));
				}else {
					couponServiceList.add(couponService);
				}
			}}
			if(!couponServiceList.isEmpty())
			this.couponServicesService.saveAllServices(couponServiceList);
			
			List<CouponServices> allCouponServiceList=this.couponServicesService.findByCoupon(saveCoupon);
			allCouponServiceList.removeAll(couponServiceList);
			if(!allCouponServiceList.isEmpty())
				this.couponServicesService.deleteAll(allCouponServiceList);
			
			String token=this.env.getProperty("crm.token");
			String POST_URL=env.getProperty("crm.domain.name")+"update-coupon.html";
			String POST_PARAMS = "uuid="+saveCoupon.getUuid()+"&title="+saveCoupon.getTitle()+"&value="+saveCoupon.getValue()+"&type="+saveCoupon.getType()+"&startDate="+saveCoupon.getStartDate()+"&endDate="+saveCoupon.getEndDate()+"&displayStatus="
					+saveCoupon.getDisplayStatus()+"&addedByUUID="+saveCoupon.getAddedByUUID()+"&postDate="+saveCoupon.getPostDate()+"&modifyDate="+saveCoupon.getModifyDate()+"&maximumDiscount="
					+saveCoupon.getMaximumDiscount()+"&token="+token+"&serviceType="+saveCoupon.getServiceType()+"&services="+serviceLst;
			int response = this.commonService.callPostURL(POST_URL, POST_PARAMS);
			
			if(response==200&&saveCoupon.getCouponPushStatus()==2) {
				saveCoupon.setCouponPushStatus(1);
				this.couponService.saveCoupon(saveCoupon);
			}else if(response!=200&&saveCoupon.getCouponPushStatus()==1) {
				saveCoupon.setCouponPushStatus(2);
				this.couponService.saveCoupon(saveCoupon);
			}
			
			return "redirect:/admin/coupons/";	
		} catch (Exception e) {	
			e.printStackTrace();
			List<Services> existServices=new ArrayList<>();
			model.addAttribute("title", "Corpseed Dashboard | Edit Coupons");
			model.addAttribute("appendClass", "coupons");			
			if(coupon.getServiceType().equals("selected"))
			for (String id : serviceName) {
				existServices.add(this.servicesService.getServiceById(id));
			}
			model.addAttribute("existServices", existServices);
			model.addAttribute("services", this.servicesService.findByDisplayStatusAndPackageNotEmpty("1"));
			model.addAttribute("coupon", coupon);
			session.setAttribute("message", new Message("Error : " + e.getMessage(), "alert-danger"));
			return "admin/coupons-edit";
		}
	}
	
}
