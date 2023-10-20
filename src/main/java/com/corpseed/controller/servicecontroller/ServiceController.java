package com.corpseed.controller.servicecontroller;

import java.io.PrintWriter;
import java.security.Principal;
import java.util.*;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import com.corpseed.dto.EnquiryDto;
import com.corpseed.entity.*;
import com.corpseed.entity.serviceentity.*;
import com.corpseed.service.ProductService;
import com.corpseed.service.StateService;
import com.corpseed.service.servicesservice.ServiceCityService;
import com.corpseed.service.servicesservice.ServiceStateService;
import com.corpseed.serviceImpl.*;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.JSONException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.corpseed.entity.couponentity.CouponServices;
import com.corpseed.entity.enquiryentity.Enquiry;
import com.corpseed.entity.orderentity.Orders;
import com.corpseed.util.Message;
import com.corpseed.serviceImpl.couponserviceimpl.CouponServicesService;
import com.corpseed.serviceImpl.enqserviceimpl.EnquiryService;
import com.corpseed.serviceImpl.orderserviceimpl.OrdersService;
import com.corpseed.serviceImpl.servicesserviceimpl.ServiceCardListService;
import com.corpseed.serviceImpl.servicesserviceimpl.ServicesBlogsService;
import com.corpseed.serviceImpl.servicesserviceimpl.ServicesService;
import com.corpseed.service.servicesservice.ServiceBrand;
import com.corpseed.service.TestimonialService;

@Controller
@RequestMapping("/admin/services")
public class ServiceController {

	@Autowired
	private CategoryService categoryService;
	
	@Autowired
	private ServicesService servicesServices;
	
	@Autowired
    private AzureBlobAdapter azureAdapter;
	
	@Autowired
	private CommonServices commonService;
	
	@Autowired
	private BCryptPasswordEncoder passwordEncoder;
	
	@Autowired
	private EnquiryService enquiryService;
	
	@Autowired
	private TriggerService triggerService;
	
	@Autowired
	private SiteMapService siteMapService;
	
	@Autowired
	private ServiceCardListService serviceCardListService;
	
	@Autowired
	private HotTagService hotTagService;	
	
	@Autowired
	private ServicesBlogsService serviceBlogService;
		
	@Autowired
	private OrdersService ordersService;
	
	@Autowired
	private CouponServicesService couponServicesService;
	
	@Autowired
	private FooterService footerService;
	
	@Autowired
	private HistoryService historyService;
	
	@Autowired
	private ServiceBrand serviceBrand;
	
	@Autowired
	private IndustryService industryService;
	
	@Autowired
	private TestimonialService testimonialService;

	@Autowired
	private StateService stateService;

	@Autowired
	private ProductService productService;

	@Autowired
	private MasterService masterService;

	@Autowired
	private ServiceStateService serviceStateService;

	@Autowired
	private ServiceCityService serviceCityService;
	
	@GetMapping("/GetServicesByCategoryWithPackage")
	public @ResponseBody Map<Long, String> getServicesByCategoryExistPackage(@RequestParam("categoryId") String categoryId,
																			 @RequestParam("categoryName") String categoryName) {
		Category categoryById = this.categoryService.getSubCategory(categoryName,categoryId);
//		System.out.println("categoryById="+categoryById);		
		return this.servicesServices.getServicesByCategory(categoryById)
				.stream().filter(s->!s.getServicePackage().isEmpty())
				.collect(Collectors.toMap(s->s.getId(),s->s.getServiceName()));
	}
	
	@GetMapping("/GetSubcategory")
	public @ResponseBody Map<Long, String> getSubCategory(@RequestParam("category") String category) {
		return this.categoryService.getAllSubCategory(category)
				.stream().collect(Collectors.toMap(c->c.getId(),c->c.getSubCategoryName()));
	}
	
	@GetMapping("/GetServicesByCategory")
	public @ResponseBody Map<Long, String> getServicesByCategory(@RequestParam("categoryId") String categoryId,@RequestParam("categoryName") String categoryName) throws JSONException{
		Category categoryById = this.categoryService.getSubCategory(categoryName,categoryId);
//		System.out.println("categoryById="+categoryById);		
		return this.servicesServices.getServicesByCategory(categoryById)
				.stream().collect(Collectors.toMap(s->s.getId(),s->s.getServiceName()));
	}
	
	@GetMapping("/")
	public String services(Model model) {
		model.addAttribute("title", "Corpseed Dashboard | All Services");
		model.addAttribute("appendClass", "services");
		model.addAttribute("services", this.servicesServices.getAllServices());
		model.addAttribute("category", this.categoryService.getAllSubCategory());
		return "admin/services";
	}
	
	@GetMapping("/filter/{uuid}/")
	public String filterServices(@PathVariable("uuid") String uuid,Model model) {				
		model.addAttribute("title", "Corpseed Dashboard | All Services");
		model.addAttribute("appendClass", "services");		
		if(uuid.equalsIgnoreCase("all")) {
			model.addAttribute("services", this.servicesServices.getAllServices());
		}else {
			Category cat=this.categoryService.findByCategoryUUID(uuid);
			
			model.addAttribute("services", this.servicesServices.getServicesByCategory(cat));
		}
		model.addAttribute("category", this.categoryService.getAllSubCategory());
		model.addAttribute("filterUuid", uuid);
		return "admin/services";
	}	
	
	@GetMapping("/add")
	public String addService(Model model) {
		model.addAttribute("title", "Corpseed Dashboard | Add Services");
		model.addAttribute("appendClass", "services");
		model.addAttribute("service", new Services());
		model.addAttribute("services",this.servicesServices.getAllServicesNotInCity(2));
		model.addAttribute("states",this.stateService.findStateByCountryId(1L));
		return "admin/services-add";
	}	
	
	@GetMapping("/delete/{uuid}")
	public @ResponseBody void deleteService(@PathVariable("uuid") String uuid,@RequestParam("password") String password,
			Principal principal,PrintWriter pw,HttpServletRequest req) {
		User user=this.commonService.getLoginedUser(principal);
		if(user.getRole().equals("ROLE_ADMIN")) {
			if(passwordEncoder.matches(password, user.getPassword())) {
				Services service=this.servicesServices.findByUUID(uuid);
				if(service!=null) {
					service.setDeleteStatus(1);
					this.servicesServices.saveServices(service);
					
					//adding category in history
					this.historyService.savehistory(new History(0, this.commonService.getUUID(),
							"Service", service.getId(), this.commonService.getBrowser(req), 
							this.commonService.getIpAddress(req), user.getId(), user.getFirstName()+" "+user.getLastName()
							, this.commonService.getToday(), this.commonService.getTime(),service.getServiceName()));		
								
				//updating hot Tag
				HotTags hotTag=service.getHotTags();
				if(hotTag!=null) {
					hotTag.setDeleteStatus(1);
					this.hotTagService.saveTag(hotTag);
				}
				//updating service blogs
				List<ServiceBlogs> serviceBlogs=service.getServiceBlogs();
				if(!serviceBlogs.isEmpty()) {
					serviceBlogs.forEach(sb->sb.setDeleteStatus(1));				
					this.serviceBlogService.saveServiceBlogs(serviceBlogs);
				}
				//updating service Package
				List<ServicePackage> servicePackage=service.getServicePackage();
				if(!servicePackage.isEmpty()) {
					servicePackage.forEach(sp->sp.setDeleteStatus(1));				
					this.servicesServices.saveServicePackages(servicePackage);
				}
				
				//updating serviceFaq
				List<ServiceFaq> serviceFaq=service.getServiceFaq();
				if(!serviceFaq.isEmpty()) {
					serviceFaq.forEach(sf->sf.setDeleteStatus(1));				
					this.servicesServices.saveServicesFaq(serviceFaq);
				}
				
				//updating service details
				List<ServiceDetails> serviceDetails=service.getServiceDetails();
				if(!serviceDetails.isEmpty()) {			
					serviceDetails.forEach(sd->sd.setDeleteStatus(1));				
					this.servicesServices.saveServicesDetails(serviceDetails);
				}
				
				//updating orders
				List<Orders> orders=service.getOrders();
				if(!orders.isEmpty()) {
					orders.forEach(ord->ord.setDeleteStatus(1));				
					this.ordersService.saveOrders(orders);
				}
				
				//updating Coupon Services
				List<CouponServices> couponService=service.getCouponService();
				if(!couponService.isEmpty()) {
					couponService.forEach(cs->cs.setDeleteStatus(1));				
					this.couponServicesService.saveAllServices(couponService);
				}
				//updating footer service
				com.corpseed.entity.footerentity.FooterService footerService=service.getFooterService();
				if(footerService!=null) {
					footerService.setDeleteStatus(1);
					this.footerService.saveFooterService(footerService);
				}
				//updating sitemap url
				SiteMapUrl siteMap = this.siteMapService.findByTypeAndUid("Service", service.getId());
				if(siteMap!=null) {
					siteMap.setStatus(2);
					this.siteMapService.saveUrl(siteMap);
				}
				}
				pw.write("pass");			
			}else {
				pw.write("fail");
			}
		}else {
			pw.write("fail");
		}
	}
	
	@GetMapping("/edit/{uuid}")
	public String editService(@PathVariable("uuid") String uuid,Model model) {
		model.addAttribute("title", "Corpseed Dashboard | Edit Services");
		model.addAttribute("appendClass", "services");
		model.addAttribute("serviceuuid", uuid);
		model.addAttribute("services",this.servicesServices.getAllServicesNotInCity(2));
		model.addAttribute("states",this.stateService.findStateByCountryId(1L));
		Services service = this.servicesServices.findByUUID(uuid);
		long parentServiceId=0;
		long stateId=0;
		long cityId=0;
//		System.out.println("Service Id="+service.getId());
		if(service.getStateCityServices()==null||service.getStateCityServices().isEmpty()){
//			System.out.println("inside if size="+service.getStateCityServices().size());
			ServiceState serviceState = service.getServiceState();
			ServiceCity serviceCity =null;
			if(serviceState==null) {
				serviceCity = service.getServiceCity();
				if(serviceCity!=null&&(serviceCity.getStateServiceId()==null||serviceCity.getStateServiceId()==0)){
					parentServiceId=serviceCity.getStateCityService().getServices().getId();
					stateId=this.stateService.findByStateName(serviceCity.getServiceStateName().replaceAll("-"," ")).getId();
					cityId=this.masterService.findByCityName(serviceCity.getCityName().replaceAll("-"," ")).getId();
				}else if(serviceCity!=null&&serviceCity.getStateServiceId()!=null&&serviceCity.getStateServiceId()>0){
					parentServiceId=this.servicesServices.findById(serviceCity.getStateServiceId()).getId();
					stateId=this.stateService.findByStateName(serviceCity.getServiceStateName().replaceAll("-"," ")).getId();
					cityId=this.masterService.findByCityName(serviceCity.getCityName().replaceAll("-"," ")).getId();
				}
			}else{
				parentServiceId = serviceState.getStateCityService().getServices().getId();
				stateId=this.stateService.findByStateName(serviceState.getStateName()).getId();
			}
		}
		List<City> allCity=null;
		if(stateId!=0)
			allCity=this.masterService.findAllCityByStateId(this.stateService.findByStateId(stateId));

//		System.out.println(parentServiceId+"\t"+stateId+"\t"+cityId);
		model.addAttribute("allCity",allCity);
		model.addAttribute("parentService",parentServiceId);
		model.addAttribute("activeState",stateId);
		model.addAttribute("activeCity",cityId);
		model.addAttribute("service", service);
		return "admin/services-edit";
	}

	public void stateCityServiceData(Services service,Model model){
		long parentServiceId=0;
		long stateId=0;
		long cityId=0;
//		System.out.println("Service Id="+service.getId());
		if(service.getStateCityServices()==null||service.getStateCityServices().isEmpty()){
//			System.out.println("inside if size="+service.getStateCityServices().size());
			ServiceState serviceState = service.getServiceState();
			ServiceCity serviceCity =null;
			if(serviceState==null) {
				serviceCity = service.getServiceCity();
				if(serviceCity!=null&&(serviceCity.getStateServiceId()==null||serviceCity.getStateServiceId()==0)){
					parentServiceId=serviceCity.getStateCityService().getServices().getId();
					stateId=this.stateService.findByStateName(serviceCity.getServiceStateName().replaceAll("-"," ")).getId();
					cityId=this.masterService.findByCityName(serviceCity.getCityName().replaceAll("-"," ")).getId();
				}else if(serviceCity!=null&&serviceCity.getStateServiceId()!=null&&serviceCity.getStateServiceId()>0){
					parentServiceId=this.servicesServices.findById(serviceCity.getStateServiceId()).getId();
					stateId=this.stateService.findByStateName(serviceCity.getServiceStateName().replaceAll("-"," ")).getId();
					cityId=this.masterService.findByCityName(serviceCity.getCityName().replaceAll("-"," ")).getId();
				}
			}else{
				parentServiceId = serviceState.getStateCityService().getServices().getId();
				stateId=this.stateService.findByStateName(serviceState.getStateName()).getId();
			}
		}
		List<City> allCity=null;
		if(stateId!=0)
			allCity=this.masterService.findAllCityByStateId(this.stateService.findByStateId(stateId));

//		System.out.println(parentServiceId+"\t"+stateId+"\t"+cityId);
		model.addAttribute("allCity",allCity);
		model.addAttribute("parentService",parentServiceId);
		model.addAttribute("activeState",stateId);
		model.addAttribute("activeCity",cityId);
	}

	@Transactional
	@PostMapping("/update/{uuid}")
	public String updateService(@Valid @ModelAttribute("service") Services service,BindingResult result,@PathVariable("uuid") String uuid,
			@RequestParam("categoryName") String categoryName,@RequestParam("subCategoryID") String subCategoryID,
			@RequestParam("legalGuideImage") MultipartFile file,@RequestParam("parentService") Optional<Services> parentService,
								@RequestParam("stateId") Optional<Long> stateId,@RequestParam("cityId") Optional<Long> cityId,
								HttpSession session,Model model,Principal principal) {
		try {
			model.addAttribute("title", "Corpseed Dashboard | Edit Services");
			model.addAttribute("appendClass", "services");
			model.addAttribute("serviceuuid", uuid);
			
			if(result.hasErrors()) {
				model.addAttribute("service", service);
				model.addAttribute("services",this.servicesServices.getAllServicesNotInCity(2));
				model.addAttribute("states",this.stateService.findStateByCountryId(1L));
				session.setAttribute("message", new Message("Please fill out the blank fields !!", "alert-danger"));
				stateCityServiceData(service,model);
				return "admin/services-edit";
			}
						
			if(Double.parseDouble(service.getRatingValue())>5) {
				model.addAttribute("services",this.servicesServices.getAllServicesNotInCity(2));
				model.addAttribute("states",this.stateService.findStateByCountryId(1L));
				model.addAttribute("service", service);
				stateCityServiceData(service,model);
				session.setAttribute("message", new Message("You can rate this service out of 5 !!", "alert-danger"));
				return "admin/services-edit";
			}
			Services findService=this.servicesServices.findByUUID(uuid);
			if(findService==null) {
				model.addAttribute("services",this.servicesServices.getAllServicesNotInCity(2));
				model.addAttribute("states",this.stateService.findStateByCountryId(1L));
				model.addAttribute("service", service);
				stateCityServiceData(service,model);
				session.setAttribute("message", new Message("Data not saved, Please try-again later !!", "alert-danger"));
				return "admin/services-edit";
			}
			Services duplicateProductNo=this.servicesServices.findByProductNoAndUuidNot(service.getProductNo(),findService.getUuid());
			if(duplicateProductNo!=null) {
				model.addAttribute("services",this.servicesServices.getAllServicesNotInCity(2));
				model.addAttribute("states",this.stateService.findStateByCountryId(1L));
				model.addAttribute("service", service);
				stateCityServiceData(service,model);
				session.setAttribute("message", new Message("Product Number already exist !!", "alert-danger"));
				return "admin/services-edit";
			}	
			
			Services existService=this.servicesServices.isServiceDuplicate(service.getSlug(),service.getServiceName(),uuid);
			
			if(existService!=null) {
//				System.out.println("existService======="+existService.getId()+"/"+existService.getUuid());
				model.addAttribute("service", service);
				model.addAttribute("services",this.servicesServices.getAllServicesNotInCity(2));
				model.addAttribute("states",this.stateService.findStateByCountryId(1L));
				stateCityServiceData(service,model);
				if(existService.getDeleteStatus()==1)
					session.setAttribute("message", new Message("Service exist in Trash !!", "alert-danger"));
				else
					session.setAttribute("message", new Message("Either service name or URL already exist !!", "alert-danger"));
				
				return "admin/services-edit";
			}
			Category category=this.categoryService.getSubCategory(categoryName, subCategoryID);
			if(category==null) {
				model.addAttribute("services",this.servicesServices.getAllServicesNotInCity(2));
				model.addAttribute("states",this.stateService.findStateByCountryId(1L));
				model.addAttribute("service", service);
				stateCityServiceData(service,model);
				session.setAttribute("message", new Message("Data Not Saved , Please try-again later !!", "alert-danger"));
				return "admin/services-edit";
			}
							
				if(file.isEmpty()) {
					service.setLegalGuide(findService.getLegalGuide());
				}else {
					String name = azureAdapter.upload(file,0);
					service.setLegalGuide(name);
					
				}
				
				service.setId(findService.getId());
				service.setCategory(category);
				service.setUuid(findService.getUuid());
				service.setAddedByUUID(this.commonService.getLoginedUser(principal).getUuid());
				service.setHotTags(findService.getHotTags());
				Services updateService=this.servicesServices.updateService(service);
				if(updateService==null) {
					model.addAttribute("service", service);
					session.setAttribute("message", new Message("Data not updated, Please try-again later !!", "alert-danger"));
					return "admin/services-edit";
				}
				if(parentService.isPresent()&&parentService.get().getServiceState()==null&&stateId.isPresent()) {
					State state=this.stateService.findByStateId(stateId.get());
					if (state != null)
						if (!cityId.isPresent()) {
							this.serviceStateService.saveServiceState(mapToServiceState(state,parentService.get(),updateService));
						}else {
							City city = this.masterService.findCityByIdAndDeleteStatus(cityId.get(), 2);
							if (city != null)
								this.serviceCityService.saveServiceCity(mapToServiceCity(state, parentService.get(), updateService, city));
						}
				}else if(parentService.isPresent()&&parentService.get().getServiceState()!=null&&cityId.isPresent()){
					State state=this.stateService.findByStateName(parentService.get().getServiceState().getStateName());
					City city = this.masterService.findCityByIdAndDeleteStatus(cityId.get(), 2);
					this.serviceCityService.saveServiceCity(new ServiceCity(0L, city.getCityName().toLowerCase().replaceAll("\\s","-"),
							state.getStateName().toLowerCase().replaceAll("\\s","-"), updateService, null, new Date(),new Date(),parentService.get().getId()));
				}

				//updating sitemap url
				SiteMapUrl siteMapUrl=this.siteMapService.findByTypeAndUid("Service",updateService.getId());
				if(siteMapUrl==null) {
					this.siteMapService.saveUrl(new SiteMapUrl(0, this.commonService.getUUID(), updateService.getId(), "Service", "/service/"+updateService.getSlug(), 1, this.commonService.getToday()));
				}else {
					siteMapUrl.setUrl("/service/"+updateService.getSlug());
					this.siteMapService.saveUrl(siteMapUrl);
				}
				return "redirect:/admin/services/";				
			
		} catch (Exception e) {
			e.printStackTrace();
			model.addAttribute("services",this.servicesServices.getAllServicesNotInCity(2));
			model.addAttribute("states",this.stateService.findStateByCountryId(1L));
			model.addAttribute("service", service);
			stateCityServiceData(service,model);
			session.setAttribute("message", new Message("Error : "+e.getMessage(), "alert-danger"));
			return "admin/services-edit";
		}
	}
	
	@PostMapping("/add")
	@Transactional
	public String saveServices(@Valid @ModelAttribute("service") Services service,BindingResult result,
			@RequestParam("categoryName") String categoryName,@RequestParam("subCategoryID") String subCategoryID,
			@RequestParam("legalGuideImage") MultipartFile file,@RequestParam("parentService") Optional<Services> parentService,
							   @RequestParam("stateId") Optional<Long> stateId,@RequestParam("cityId") Optional<Long> cityId,
							   Model model,HttpSession session,Principal principal) {

		model.addAttribute("title", "Corpseed Dashboard | Add Services");
		model.addAttribute("appendClass", "services");			
		try {			
			if(result.hasErrors()) {
				model.addAttribute("services",this.servicesServices.getAllServicesNotInCity(2));
				model.addAttribute("states",this.stateService.findStateByCountryId(1L));
				model.addAttribute("service", service);
				session.setAttribute("message", new Message("Please fill out the blank fields !!", "alert-danger"));
				return "admin/services-add";
			}
			if(Double.parseDouble(service.getRatingValue())>5) {
				model.addAttribute("services",this.servicesServices.getAllServicesNotInCity(2));
				model.addAttribute("states",this.stateService.findStateByCountryId(1L));
				model.addAttribute("service", service);
				session.setAttribute("message", new Message("You can rate this service out of 5 !!", "alert-danger"));
				return "admin/services-add";
			}
			Services duplicateProductNo=this.servicesServices.findByProductNo(service.getProductNo());
			if(duplicateProductNo!=null) {
				model.addAttribute("services",this.servicesServices.getAllServicesNotInCity(2));
				model.addAttribute("states",this.stateService.findStateByCountryId(1L));
				model.addAttribute("service", service);
				session.setAttribute("message", new Message("Product Number already exist !!", "alert-danger"));
				return "admin/services-add";
			}
			Services serviceExist = this.servicesServices.isServiceExist(service.getSlug(),service.getServiceName());
			
			if(serviceExist!=null) {
				model.addAttribute("services",this.servicesServices.getAllServicesNotInCity(2));
				model.addAttribute("states",this.stateService.findStateByCountryId(1L));
				model.addAttribute("service", service);
				if(serviceExist.getDeleteStatus()==1)
					session.setAttribute("message", new Message("Service exist in Trash !!", "alert-danger"));
				else
					session.setAttribute("message", new Message("Either Service name or url already exist !!", "alert-danger"));
				
				return "admin/services-add";
			}
			Category subCategoryExist = this.categoryService.getSubCategory(categoryName, subCategoryID);	
			
			if(subCategoryExist==null) {
				model.addAttribute("services",this.servicesServices.getAllServicesNotInCity(2));
				model.addAttribute("states",this.stateService.findStateByCountryId(1L));
				model.addAttribute("service", service);
				session.setAttribute("message", new Message("Category not found, Please try-again later !!", "alert-danger"));
				return "admin/services-add";				
			}
				
		        service.setUuid(this.commonService.getUUID());
		        service.setCategory(subCategoryExist);
		        
		        if(file.isEmpty()) {
		        	service.setLegalGuide("NA");
		        }else {
		        	String name=azureAdapter.upload(file,0);
		        	service.setLegalGuide(name);
		        	
		        }
		        //getting logined uuid
		        User loginedUser = this.commonService.getLoginedUser(principal);
		        if(loginedUser!=null) {
		        	service.setAddedByUUID(loginedUser.getUuid());
		        }
		        service.setRatingDefaultUser(service.getRatingUser());
		        service.setRatingDefaultValue(service.getRatingValue());
//		        System.out.println("Before save="+service);
				Services saveServices = this.servicesServices.saveServices(service);
//				System.out.println("saveServices="+saveServices);
				if(saveServices==null) {
					model.addAttribute("service", service);
					session.setAttribute("message", new Message("Data not saved, Please try-again later !!", "alert-danger"));
					return "admin/services-add";
				}
				if(parentService.isPresent()&&parentService.get().getServiceState()==null&&stateId.isPresent()) {
					State state=this.stateService.findByStateId(stateId.get());
					if (state != null)
						if (!cityId.isPresent()) {
								this.serviceStateService.saveServiceState(mapToServiceState(state,parentService.get(),saveServices));
						}else {
							City city = this.masterService.findCityByIdAndDeleteStatus(cityId.get(), 2);
							if (city != null)
								this.serviceCityService.saveServiceCity(mapToServiceCity(state, parentService.get(), saveServices, city));
						}
				}else if(parentService.isPresent()&&parentService.get().getServiceState()!=null&&cityId.isPresent()){
					State state=this.stateService.findByStateName(parentService.get().getServiceState().getStateName());
					City city = this.masterService.findCityByIdAndDeleteStatus(cityId.get(), 2);
					this.serviceCityService.saveServiceCity(new ServiceCity(0L, city.getCityName().toLowerCase().replaceAll("\\s","-"),
							state.getStateName().toLowerCase().replaceAll("\\s","-"), saveServices, null, new Date(),new Date(),parentService.get().getId()));
				}
				//getting all enquiry which is related to this service Category
				List<Enquiry> enquiryList=this.enquiryService.getAllEnquiryByTypeAndStatusAndCategoryId("service","1",saveServices.getCategory().getId()+"");
				if(!enquiryList.isEmpty()) {
					this.triggerService.saveTriggers(enquiryList,saveServices.getSlug(),"/service/",saveServices.getServiceName());
				}
				//adding sitemap url
				this.siteMapService.saveUrl(new SiteMapUrl(0, this.commonService.getUUID(),saveServices.getId(),"Service", "/service/"+saveServices.getSlug(), 1, this.commonService.getToday()));
				return "redirect:/admin/services/";
				
		} catch (Exception e) {
			e.printStackTrace();
			model.addAttribute("services",this.servicesServices.getAllServicesNotInCity(2));
			model.addAttribute("states",this.stateService.findStateByCountryId(1L));
			model.addAttribute("service", service);
			session.setAttribute("message", new Message("Error : "+e.getMessage(), "alert-danger"));
			return "admin/services-add";
		}			
	}

	private ServiceCity mapToServiceCity(State state, Services parentService, Services saveServices, City city) {
		ServiceCity serviceCity=new ServiceCity(0L, city.getCityName().toLowerCase().replaceAll("\\s","-"),
				state.getStateName().toLowerCase().replaceAll("\\s","-"), saveServices, null, new Date(),new Date(),0L);
		serviceCity.setStateCityService(mapToStateCityService(null,serviceCity, parentService));
		return serviceCity;
	}

	/*private ServiceState mapToUpdateServiceState(State state, Services parentService, Services saveServices) {
		ServiceState serviceState= new ServiceState(0L,state.getStateName().toLowerCase().replaceAll("\\s","-"),saveServices,null,new Date(),new Date());
		serviceState.setStateCityService(mapToUpdateStateCityService(serviceState,null, parentService));
		return serviceState;
	}

	private StateCityService mapToUpdateStateCityService(ServiceState serviceState,ServiceCity serviceCity,Services parentService){
		return new StateCityService(0L, serviceState, serviceCity, parentService, new Date(), new Date());
	}*/

	private ServiceState mapToServiceState(State state, Services parentService, Services saveServices) {
		ServiceState serviceState= new ServiceState(0L,state.getStateName().toLowerCase().replaceAll("\\s","-"),saveServices,null,new Date(),new Date());
		serviceState.setStateCityService(mapToStateCityService(serviceState,null, parentService));
		return serviceState;
	}
	private StateCityService mapToStateCityService(ServiceState serviceState,ServiceCity serviceCity,Services parentService){
		return new StateCityService(0L, serviceState, serviceCity, parentService, new Date(), new Date());
	}

	@GetMapping("/packages/{serviceUUID}")
	public String servicePackage(@PathVariable(name="serviceUUID") String serviceUUID,Model model) {
		model.addAttribute("title", "Corpseed Dashboard | Services Packages");
		model.addAttribute("appendClass", "services");
		model.addAttribute("serviceUUID", serviceUUID);
		Services service = this.servicesServices.findByUUID(serviceUUID);
		model.addAttribute("servicePackages", this.servicesServices.getPackagesByServices(service));
		return "admin/packages";
	}
	@GetMapping("/packages/delete/{uuid}")
	public String deletePackages(@PathVariable("uuid") String uuid,Principal principal,HttpServletRequest req) {
		if(this.commonService.getLoginedUser(principal).getRole().equals("ROLE_ADMIN")) {
			ServicePackage servicePackage=this.servicesServices.getPackageByUuid(uuid);
			if(servicePackage!=null) {
			servicePackage.setDeleteStatus(1);
			this.servicesServices.saveServicePackage(servicePackage);
			
			User user=this.commonService.getLoginedUser(principal);
			
			//adding category in history
			this.historyService.savehistory(new History(0, this.commonService.getUUID(),
					"Service Package", servicePackage.getId(), this.commonService.getBrowser(req), 
					this.commonService.getIpAddress(req), user.getId(), user.getFirstName()+" "+user.getLastName()
					, this.commonService.getToday(), this.commonService.getTime(),servicePackage.getPackageName()));	
			}
			
			return "redirect:/admin/services/packages/"+servicePackage.getServices().getUuid();
		}else {
			return "error/403";
		}
	}
	@GetMapping("/packages/add/{serviceUUID}")
	public String addPackage(@PathVariable("serviceUUID") String serviceUUID,Model model) {
		model.addAttribute("title", "Corpseed Dashboard | Add Service Package");
		model.addAttribute("appendClass", "services");
		model.addAttribute("serviceUUID", serviceUUID);
		model.addAttribute("servicePackages", new ServicePackage());
		return "admin/packages-add";
	}
	
	@GetMapping("/packages/edit/{packageUUID}")
	public String editPackage(@PathVariable("packageUUID") String packageUUID,Model model) {
		model.addAttribute("title", "Corpseed Dashboard | Edit Service Package");
		model.addAttribute("appendClass", "services");
		model.addAttribute("packageUUID", packageUUID);
		ServicePackage servicePackage=this.servicesServices.getPackageByUuid(packageUUID);
		model.addAttribute("serviceUUID", servicePackage.getServices().getUuid());
		model.addAttribute("servicePackages", servicePackage);
		return "admin/packages-edit";
	}
	
	@PostMapping("/packages/update/{packageUUID}")
	public String updatePackage(@Valid @ModelAttribute("servicePackages") ServicePackage servicePackages,BindingResult result,
			@RequestParam("serviceUUID") String serviceUUID,@PathVariable("packageUUID") String packageUUID,
			Model model,HttpSession session,Principal principal) {
		
		try {	
			
			if(result.hasErrors()) {
				model.addAttribute("title", "Corpseed Dashboard | Edit Service package");
				model.addAttribute("appendClass", "services");
				model.addAttribute("servicePackages", servicePackages);
				model.addAttribute("packageUUID", packageUUID);
				model.addAttribute("serviceUUID", serviceUUID);
				session.setAttribute("message", new Message("Please fill out the blank fields !!", "alert-danger"));
				return "admin/packages-edit";
			}
			Services service=this.servicesServices.findByUUID(serviceUUID);
			ServicePackage existPackage=this.servicesServices.findByPackageNameAndServicesAndUuidNot(servicePackages.getPackageName(),service,packageUUID);
			if(existPackage!=null) {
				model.addAttribute("title", "Corpseed Dashboard | Edit Service package");
				model.addAttribute("appendClass", "services");
				model.addAttribute("servicePackages", servicePackages);
				model.addAttribute("packageUUID", packageUUID);
				model.addAttribute("serviceUUID", serviceUUID);
				if(existPackage.getDeleteStatus()==1)
					session.setAttribute("message", new Message("Package exist in Trash !!", "alert-danger"));
				else
					session.setAttribute("message", new Message("Package already exist !!", "alert-danger"));
				
				return "admin/packages-edit";
			}
				ServicePackage findPackage=this.servicesServices.getPackageByUuid(packageUUID);
				servicePackages.setId(findPackage.getId());
				servicePackages.setServices(service);
				servicePackages.setUuid(findPackage.getUuid());
				servicePackages.setAddedByUUID(this.commonService.getLoginedUser(principal).getUuid());
				
				ServicePackage saveServicePackage = this.servicesServices.saveServicePackage(servicePackages);
				
				if(saveServicePackage==null) {
					model.addAttribute("title", "Corpseed Dashboard | Edit Service package");
					model.addAttribute("appendClass", "services");
					model.addAttribute("servicePackages", servicePackages);
					model.addAttribute("packageUUID", packageUUID);
					model.addAttribute("serviceUUID", serviceUUID);
					session.setAttribute("message", new Message("Data Not Saved, Please try-again later !!", "alert-danger"));
					return "admin/packages-edit";
				}else {
					return "redirect:/admin/services/packages/"+serviceUUID;
				}
		} catch (Exception e) {
			model.addAttribute("title", "Corpseed Dashboard | Edit Service package");
			model.addAttribute("appendClass", "services");
			model.addAttribute("servicePackages", servicePackages);
			model.addAttribute("packageUUID", packageUUID);
			model.addAttribute("serviceUUID", serviceUUID);
			session.setAttribute("message", new Message("Error : "+e.getMessage(), "alert-danger"));
			return "admin/packages-edit";
		}
	}
	
	@PostMapping("/packages/add/{serviceUUID}")
	public String savePackage(@Valid @ModelAttribute("servicePackages") ServicePackage servicePackages,BindingResult result,
			@PathVariable("serviceUUID") String serviceUUID,Model model,Principal principal,HttpSession session) {
		
		try {
			model.addAttribute("title", "Corpseed Dashboard | Add Service Package");
			model.addAttribute("appendClass", "services");
			
			if(result.hasErrors()) {
				model.addAttribute("servicePackages", servicePackages);
				session.setAttribute("message", new Message("Please fill out the blank fields !!", "alert-danger"));
				return "admin/packages-add";
			}
			Services service=this.servicesServices.findByUUID(serviceUUID);
			
			//checking package already existed or not
			ServicePackage existPackage=this.servicesServices.isServicePackageExist(servicePackages.getPackageName(),service);
			if(existPackage!=null) {
				model.addAttribute("servicePackages", servicePackages);
				if(existPackage.getDeleteStatus()==1)
					session.setAttribute("message", new Message("Package exist in Trash !!", "alert-danger"));
				else
					session.setAttribute("message", new Message("Duplicate package name !!", "alert-danger"));
					
				return "admin/packages-add";
			}
			servicePackages.setUuid(this.commonService.getUUID());
			servicePackages.setServices(service);
			servicePackages.setAddedByUUID(this.commonService.getLoginedUser(principal).getUuid());
			
			ServicePackage savePackage=this.servicesServices.saveServicePackage(servicePackages);
			
			if(savePackage==null) {
				model.addAttribute("servicePackages", servicePackages);
				session.setAttribute("message", new Message("Data Not Saved, Please try-again later !!", "alert-danger"));
				return "admin/packages-add";
			}
			return "redirect:/admin/services/packages/{serviceUUID}";
					
		} catch (Exception e) {
			e.printStackTrace();
			model.addAttribute("servicePackages", servicePackages);
			session.setAttribute("message", new Message("Error : ", "alert-danger"));
			return "admin/packages-add";
		}
	} 
	
	@GetMapping("/faq/{serviceUUID}")
	public String serviceFaqs(@PathVariable(name="serviceUUID") String serviceUUID,Model model) {
		model.addAttribute("title", "Corpseed Dashboard | Services FAQ`s");
		model.addAttribute("appendClass", "services");
		model.addAttribute("serviceUUID", serviceUUID);
		Services service=this.servicesServices.findByUUID(serviceUUID);
		model.addAttribute("faq", this.servicesServices.getAllServicesFaq(service));
		return "admin/faqs";
	}
	@GetMapping("/faq/add/{serviceUUID}")
	public String addFaq(@PathVariable("serviceUUID") String serviceUUID,Model model) {
		model.addAttribute("title", "Corpseed Dashboard | Add FAQ`s");
		model.addAttribute("appendClass", "services");
		model.addAttribute("serviceUUID", serviceUUID);
		model.addAttribute("faq", new ServiceFaq());
		return "admin/faqs-add";
	}
	
	@GetMapping("/faq/edit/{faqUUID}")
	public String editFaq(@PathVariable("faqUUID") String faqUUID,Model model) {
		model.addAttribute("title", "Corpseed Dashboard | Edit FAQ`s");
		model.addAttribute("appendClass", "services");
		model.addAttribute("faqUUID", faqUUID);
		ServiceFaq faq=this.servicesServices.getServiceFaqByUUID(faqUUID);
		
		model.addAttribute("serviceUUID", faq.getServices().getUuid());
		model.addAttribute("faq", faq);
		return "admin/faqs-edit";
	}
	@GetMapping("/faq/delete/{uuid}")
	public String deleteFaq(@PathVariable("uuid") String uuid,Principal principal,HttpServletRequest req) {
		if(this.commonService.getLoginedUser(principal).getRole().equals("ROLE_ADMIN")) {
			ServiceFaq serviceFaq=this.servicesServices.getServiceFaqByUUID(uuid);
			if(serviceFaq!=null) {
				serviceFaq.setDeleteStatus(1);
				this.servicesServices.saveServiceFaq(serviceFaq);
				User user=this.commonService.getLoginedUser(principal);
				//adding category in history
				this.historyService.savehistory(new History(0, this.commonService.getUUID(),
						"Service FAQ", serviceFaq.getId(), this.commonService.getBrowser(req), 
						this.commonService.getIpAddress(req), user.getId(), user.getFirstName()+" "+user.getLastName()
						, this.commonService.getToday(), this.commonService.getTime(),serviceFaq.getTitle()));		
			}
			
			return "redirect:/admin/services/faq/"+serviceFaq.getServices().getUuid();
		}else {
			return "error/403";
		}
	}
	@PostMapping("/faq/edit/{faqUUID}")
	public String updateFaq(@Valid @ModelAttribute("faq") ServiceFaq faq,BindingResult result,@RequestParam("serviceUUID") String serviceUUID,
			@PathVariable("faqUUID") String faqUUID,Model model,HttpSession session,Principal principal) {
		
		try {	
			
			if(result.hasErrors()) {
				model.addAttribute("title", "Corpseed Dashboard | Edit FAQ`s");
				model.addAttribute("appendClass", "services");
				model.addAttribute("faq", faq);
				model.addAttribute("faqUUID", faqUUID);
				model.addAttribute("serviceUUID", serviceUUID);
				session.setAttribute("message", new Message("Please fill out the blank fields !!", "alert-danger"));
				return "admin/faqs-edit";
			}
			Services service=this.servicesServices.findByUUID(serviceUUID);
			ServiceFaq existFaq=this.servicesServices.findByTitleAndServiceNotUuid(faq.getTitle(),service,faqUUID);
			if(existFaq!=null) {
				model.addAttribute("title", "Corpseed Dashboard | Edit FAQ`s");
				model.addAttribute("appendClass", "services");
				model.addAttribute("faq", faq);
				model.addAttribute("faqUUID", faqUUID);
				model.addAttribute("serviceUUID", serviceUUID);
				if(existFaq.getDeleteStatus()==1)
					session.setAttribute("message", new Message("Service Faq's exist in Trash !!", "alert-danger"));
				else
					session.setAttribute("message", new Message("Service Faq's already exist !!", "alert-danger"));
				
				return "admin/faqs-edit";
			}
				ServiceFaq findFaq=this.servicesServices.getServiceFaqByUUID(faqUUID);
				faq.setId(findFaq.getId());
				faq.setServices(service);
				faq.setUuid(findFaq.getUuid());
				faq.setAddedByUUID(this.commonService.getLoginedUser(principal).getUuid());
				
				String description=faq.getDescription();

				description=description.replace("<div class=\"table-width\">","");
				description=description.replace("</table>\r\n"
						+ "</div>","</table>");
				
				description=description.replace("<table","<div class='table-width'><table");
				description=description.replace("</table>","</table></div>");
				
				faq.setDescription(description);
				
				ServiceFaq saveServiceFaq = this.servicesServices.saveServiceFaq(faq);
				
				if(saveServiceFaq==null) {
					model.addAttribute("title", "Corpseed Dashboard | Edit FAQ`s");
					model.addAttribute("appendClass", "services");
					model.addAttribute("faq", faq);
					model.addAttribute("faqUUID", faqUUID);
					model.addAttribute("serviceUUID", serviceUUID);
					session.setAttribute("message", new Message("Data Not Saved, Please try-again later !!", "alert-danger"));
					return "admin/faqs-edit";
				}else {
					return "redirect:/admin/services/faq/"+serviceUUID;
				}				
			
		} catch (Exception e) {
			model.addAttribute("title", "Corpseed Dashboard | Edit FAQ`s");
			model.addAttribute("appendClass", "services");
			model.addAttribute("faq", faq);
			model.addAttribute("faqUUID", faqUUID);
			model.addAttribute("serviceUUID", serviceUUID);
			session.setAttribute("message", new Message("Error : "+e.getMessage(), "alert-danger"));
			return "admin/faqs-edit";
		}
	}
	
	@PostMapping("/faq/add/{serviceUUID}")
	public String saveFaq(@Valid @ModelAttribute("faq") ServiceFaq faq,BindingResult result,
			@PathVariable("serviceUUID") String serviceUUID,Model model,Principal principal,HttpSession session) {
		model.addAttribute("title", "Corpseed Dashboard | Add FAQ`s");
		model.addAttribute("appendClass", "services");
		try {			
			
			if(result.hasErrors()) {
				model.addAttribute("faq", faq);
				session.setAttribute("message", new Message("Please fill out the blank fields !!", "alert-danger"));
				return "admin/faqs-add";
			}
			Services service=this.servicesServices.findByUUID(serviceUUID);
			
			//checking package already existed or not
			ServiceFaq existFaq=this.servicesServices.isServiceFaqExist(faq.getTitle(),service);
			if(existFaq!=null) {
				model.addAttribute("faq", faq);
				if(existFaq.getDeleteStatus()==1)
					session.setAttribute("message", new Message("Service faq exist in Trash !!", "alert-danger"));
				else
					session.setAttribute("message", new Message("Service faq already exist !!", "alert-danger"));
				
				return "admin/faqs-add";
			}
				faq.setUuid(this.commonService.getUUID());
				faq.setServices(service);
				faq.setAddedByUUID(this.commonService.getLoginedUser(principal).getUuid());
			
				String description=faq.getDescription();

				description=description.replace("<table","<div class='table-width'><table");
				description=description.replace("</table>","</table></div>");
				
				faq.setDescription(description);
				
			ServiceFaq saveFaq=this.servicesServices.saveServiceFaq(faq);
			
			if(saveFaq==null) {
				model.addAttribute("faq", faq);
				session.setAttribute("message", new Message("Data Not Saved, Please try-again later !!", "alert-danger"));
				return "admin/faqs-add";
			}else {
				return "redirect:/admin/services/faq/{serviceUUID}";
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			model.addAttribute("faq", faq);
			session.setAttribute("message", new Message("Error : "+e.getMessage(), "alert-danger"));
			return "admin/faqs-add";
		}
	}
	
	@GetMapping("/details/{serviceUUID}")
	public String serviceDetails(@PathVariable(name="serviceUUID") String serviceUUID,Model model) {
		model.addAttribute("title", "Corpseed Dashboard | Services Details");
		model.addAttribute("appendClass", "services");
		model.addAttribute("serviceUUID", serviceUUID);
		Services service=this.servicesServices.findByUUID(serviceUUID);
		model.addAttribute("detail", this.servicesServices.getAllServiceDetails(service));
		return "admin/details";
	}

	@GetMapping("/details/add/{serviceUUID}")
	public String addDetails(@PathVariable("serviceUUID") String serviceUUID,Model model) {
		model.addAttribute("title", "Corpseed Dashboard | Add Details");
		model.addAttribute("appendClass", "services");
		model.addAttribute("products", this.productService.getAllProducts());
		model.addAttribute("services", this.servicesServices.getAllServicesByStatus("1"));
		model.addAttribute("industries", this.industryService.getIndustryByDisplsyStatus("1"));

		model.addAttribute("serviceUUID", serviceUUID);
		model.addAttribute("details", new ServiceDetails());
		return "admin/details-add";
	}
	
	@GetMapping("/details/edit/{detailsUUID}")
	public String editDetails(@PathVariable("detailsUUID") String detailsUUID,Model model) {
		model.addAttribute("title", "Corpseed Dashboard | Edit Service Details");
		model.addAttribute("appendClass", "services");
		model.addAttribute("detailsUUID", detailsUUID);
		model.addAttribute("products", this.productService.getAllProducts());
		ServiceDetails details=this.servicesServices.getServiceDetailsByUuid(detailsUUID);

		model.addAttribute("serviceUUID", details.getServices().getUuid());
		List<Services> allServicesByStatus = this.servicesServices.getAllServicesByStatus("1");
		for (ServiceCardList sCardList : details.getServiceCardLists()) {
			allServicesByStatus.remove(sCardList.getService());
		}		
		model.addAttribute("services", allServicesByStatus);
		model.addAttribute("industries", this.industryService.getIndustryByDisplsyStatus("1"));
		
		String description=details.getDescription();
		while(description.contains("<iframe src=\"https://docs.google.com/gview?url=")||description.contains("<iframe src=\"http://docs.google.com/gview?url=")) {
			description = description.replace("<iframe src=\"http://docs.google.com/gview?url=", "pdfview=");
			description = description.replace("<iframe src=\"https://docs.google.com/gview?url=", "pdfview=");
			description = description.replace("&embedded=true\" frameborder=\"0\" webkitallowfullscreen mozallowfullscreen allowfullscreen style=\"width: 100%; height: 700px;\"></iframe>", "=pdfview");
			description=description.replace("&embedded=true\" frameborder=\"0\" webkitallowfullscreen mozallowfullscreen allowfullscreen style=\"width: 100%; height: 500px;\">","=pdfview");
		}
		details.setDescription(description);	
		
		model.addAttribute("details", details);
		return "admin/details-edit";
	}
	
	/* brand start */
	@GetMapping("/brands/{uuid}")
	public String brands(@PathVariable("uuid") String uuid,Model model) {
		model.addAttribute("title", "Corpseed Dashboard | Service Brands");
		model.addAttribute("appendClass", "services");
		Services findByUUID = this.servicesServices.findByUUID(uuid);
				
		model.addAttribute("brands",this.serviceBrand.findByServicesAndDeleteStatus(findByUUID,2));
		model.addAttribute("serviceUuid", findByUUID.getUuid());
		
		return "admin/brands";
	}
	
	@GetMapping("/brand/add/{uuid}")
	public String addBrand(@PathVariable("uuid") String uuid,Model model) {
		model.addAttribute("title", "Corpseed Dashboard | Add Service Brand");
		model.addAttribute("appendClass", "services");
		model.addAttribute("brand",new ServiceBrands());
		model.addAttribute("serviceUuid", uuid);
		
		return "admin/brand-add";
	}

	@GetMapping("/brand/edit/{uuid}")
	public String editBrand(@PathVariable("uuid") String uuid,Model model) {
		model.addAttribute("title", "Corpseed Dashboard | Edit Service Brand");
		model.addAttribute("appendClass", "services");
		model.addAttribute("brand",this.serviceBrand.getServiceBrand(uuid,2));
		
		return "admin/brand-edit";
	}

	@GetMapping("/brand/delete/{uuid}")
	public String deleteBrand(@PathVariable("uuid") String uuid,Model model,Principal principal,
			HttpServletRequest req) {
		
		ServiceBrands brand=this.serviceBrand.getServiceBrand(uuid, 2);
		if(brand==null)
			return "error/404";
		else {
			String serviceUuid=brand.getServices().getUuid();
			brand.setDeleteStatus(1);
			this.serviceBrand.saveServiceBrand(brand);
			
			User user=this.commonService.getLoginedUser(principal);
			//adding category in history
			this.historyService.savehistory(new History(0, this.commonService.getUUID(),
					"Service Brand", brand.getId(), this.commonService.getBrowser(req), 
					this.commonService.getIpAddress(req), user.getId(), user.getFirstName()+" "+user.getLastName()
					, this.commonService.getToday(), this.commonService.getTime(),brand.getTitle()));
			
			return "redirect:/admin/services/brands/"+serviceUuid;
		}		
	}
	
	@PostMapping("/brand/add/{uuid}")
	public String saveBrand(@PathVariable("uuid") String uuid,Model model,
			@Valid @ModelAttribute("brand") ServiceBrands brand,BindingResult result,
			@RequestParam("icon") MultipartFile icon,HttpSession session) {
		
		try {
			if(result.hasErrors()||icon.isEmpty()) {
				model.addAttribute("title", "Corpseed Dashboard | Add Service Brand");
				model.addAttribute("appendClass", "services");
				session.setAttribute("message", new Message("Please fill out the blank fields !!", "alert-danger"));
				model.addAttribute("brand",brand);
				model.addAttribute("serviceUuid", uuid);
				
				return "admin/brand-add";
			}
			Services service=this.servicesServices.findByUUID(uuid);
			if(service==null) return "error/404";
			
			if(this.azureAdapter.isFileExist(icon.getOriginalFilename()))
				brand.setBrands(icon.getOriginalFilename());
			else {
				String iconName = this.azureAdapter.upload(icon, 0);
				brand.setBrands(iconName);
			}
			brand.setUuid(this.commonService.getUUID());
			brand.setServices(service);
			ServiceBrands saveServiceBrand = this.serviceBrand.saveServiceBrand(brand);
			if(saveServiceBrand==null) {
				model.addAttribute("title", "Corpseed Dashboard | Add Service Brand");
				model.addAttribute("appendClass", "services");
				session.setAttribute("message", new Message("Service Brand not saved !!", "alert-danger"));
				model.addAttribute("brand",brand);
				model.addAttribute("serviceUuid", uuid);
				
				return "admin/brand-add";
			}
			
			return "redirect:/admin/services/brands/"+uuid;
			
		} catch (Exception e) {
			e.printStackTrace();
			model.addAttribute("title", "Corpseed Dashboard | Add Service Brand");
			model.addAttribute("appendClass", "services");
			session.setAttribute("message", new Message("Error : "+e.getMessage(), "alert-danger"));
			model.addAttribute("brand",brand);
			model.addAttribute("serviceUuid", uuid);
			
			return "admin/brand-add";
		}		
	}
	
	@PostMapping("/brand/update")
	public String updateBrand(Model model,
			@Valid @ModelAttribute("brand") ServiceBrands brand,BindingResult result,
			@RequestParam("icon") MultipartFile icon,HttpSession session) {
		
		try {
			if(result.hasErrors()) {
				model.addAttribute("title", "Corpseed Dashboard | Edit Service Brand");
				model.addAttribute("appendClass", "services");
				session.setAttribute("message", new Message("Please fill out the blank fields !!", "alert-danger"));
				model.addAttribute("brand",brand);
				
				return "admin/brand-edit";
			}
			if(!icon.isEmpty())
			if(this.azureAdapter.isFileExist(icon.getOriginalFilename()))
				brand.setBrands(icon.getOriginalFilename());
			else {
				String iconName = this.azureAdapter.upload(icon, 0);
				brand.setBrands(iconName);
			}
			
			ServiceBrands saveServiceBrand = this.serviceBrand.saveServiceBrand(brand);
			if(saveServiceBrand==null) {
				model.addAttribute("title", "Corpseed Dashboard | Edit Service Brand");
				model.addAttribute("appendClass", "services");
				session.setAttribute("message", new Message("Service Brand not saved !!", "alert-danger"));
				model.addAttribute("brand",brand);
				
				return "admin/brand-edit";
			}
			
			return "redirect:/admin/services/brands/"+saveServiceBrand.getServices().getUuid();
			
		} catch (Exception e) {
			e.printStackTrace();
			model.addAttribute("title", "Corpseed Dashboard | Edit Service Brand");
			model.addAttribute("appendClass", "services");
			session.setAttribute("message", new Message("Error : "+e.getMessage(), "alert-danger"));
			model.addAttribute("brand",brand);
			
			return "admin/brand-edit";
		}		
	}
	
	/* brand end */
	
	@GetMapping("/details/delete/{uuid}")
	public String deleteDelete(@PathVariable("uuid") String uuid,Principal principal,HttpServletRequest req) {
		if(this.commonService.getLoginedUser(principal).getRole().equals("ROLE_ADMIN")) {
			ServiceDetails serviceDetails=this.servicesServices.getServiceDetailsByUuid(uuid);		
			if(serviceDetails!=null) {
				serviceDetails.setDeleteStatus(1);
				this.servicesServices.saveServiceDetails(serviceDetails);
				
				User user=this.commonService.getLoginedUser(principal);
				//adding category in history
				this.historyService.savehistory(new History(0, this.commonService.getUUID(),
						"Service Details", serviceDetails.getId(), this.commonService.getBrowser(req), 
						this.commonService.getIpAddress(req), user.getId(), user.getFirstName()+" "+user.getLastName()
						, this.commonService.getToday(), this.commonService.getTime(),serviceDetails.getTabName()));
				
				//updating service card list
				List<ServiceCardList> serviceCardLists=serviceDetails.getServiceCardLists();
				if(!serviceCardLists.isEmpty()) {
					serviceCardLists.forEach(sd->sd.setDeleteStatus(1));
					this.serviceCardListService.saveAll(serviceCardLists);
				} 
						
			}
			
			return "redirect:/admin/services/details/"+serviceDetails.getServices().getUuid();
		}else {
			return "error/403";
		}
	}
	@PostMapping("/details/edit/{detailsUUID}")
	public String updateDetails(@Valid @ModelAttribute("details") ServiceDetails details,BindingResult result,@RequestParam("serviceUUID") String serviceUUID,
			@PathVariable("detailsUUID") String detailsUUID,@RequestParam("servicesId") List<String> servicesId,Model model,HttpSession session,Principal principal) {
		
		try {	
			
			if(result.hasErrors()) {
				model.addAttribute("title", "Corpseed Dashboard | Edit Service details");
				model.addAttribute("appendClass", "services");
				model.addAttribute("details", details);
				model.addAttribute("detailsUUID", detailsUUID);
				model.addAttribute("serviceUUID", serviceUUID);
				model.addAttribute("products", this.productService.getAllProducts());
				model.addAttribute("services", this.servicesServices.getAllServicesByStatus("1"));
				model.addAttribute("industries", this.industryService.getIndustryByDisplsyStatus("1"));
				session.setAttribute("message", new Message("Please fill out the blank fields !!", "alert-danger"));
				return "admin/details-edit";
			}
			Services service=this.servicesServices.findByUUID(serviceUUID);
			ServiceDetails existDetails=this.servicesServices.findByTabNameAndServicesAndUuidNot(details.getTabName(),service,detailsUUID);
			ServiceDetails existDetails1=this.servicesServices.findByDisplayOrderAndServicesAndUuidNot(details.getDisplayOrder(),service,detailsUUID);
			if(existDetails!=null||existDetails1!=null) {
				model.addAttribute("title", "Corpseed Dashboard | Edit Service details");
				model.addAttribute("appendClass", "services");
				model.addAttribute("details", details);
				model.addAttribute("products", this.productService.getAllProducts());
				model.addAttribute("detailsUUID", detailsUUID);
				model.addAttribute("serviceUUID", serviceUUID);
				model.addAttribute("services", this.servicesServices.getAllServicesByStatus("1"));
				model.addAttribute("industries", this.industryService.getIndustryByDisplsyStatus("1"));
				if(existDetails.getDeleteStatus()==1)
					session.setAttribute("message", new Message("Service Details exist in Trash !!", "alert-danger"));
				else
					session.setAttribute("message", new Message("Either Tab Name Or Service Details already exist !!", "alert-danger"));
				
				return "admin/details-edit";
			}
				ServiceDetails findDetails=this.servicesServices.getServiceDetailsByUuid(detailsUUID);
				details.setId(findDetails.getId());
				details.setServices(service);
				details.setUuid(findDetails.getUuid());
				details.setAddedByUUID(this.commonService.getLoginedUser(principal).getUuid());
				
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
                    +"<h1>Start your <b class='text-primary'>"+service.getServiceName()+"</b> today!</h1>"
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
				ServiceDetails saveServiceDetails = this.servicesServices.saveServiceDetails(details);
				
				if(saveServiceDetails==null) {
					model.addAttribute("title", "Corpseed Dashboard | Edit Service details");
					model.addAttribute("appendClass", "services");
					model.addAttribute("details", details);
					model.addAttribute("products", this.productService.getAllProducts());
					model.addAttribute("detailsUUID", detailsUUID);
					model.addAttribute("serviceUUID", serviceUUID);
					model.addAttribute("services", this.servicesServices.getAllServicesByStatus("1"));
					model.addAttribute("industries", this.industryService.getIndustryByDisplsyStatus("1"));
					session.setAttribute("message", new Message("Data Not Saved, Please try-again later !!", "alert-danger"));
					return "admin/details-edit";
				}
				List<ServiceCardList> serviceCardList=new ArrayList<>();
				String today=this.commonService.getToday();
				for (String string : servicesId) {
					if(!string.equalsIgnoreCase("NA")) {
						Services eservice=this.servicesServices.findByIdAndDisplayStatus(Long.parseLong(string), "1");
						ServiceCardList scList=this.serviceCardListService.findByServiceDetailsAndService(saveServiceDetails,eservice);
						if(scList!=null) {
							serviceCardList.add(scList);
						}else{
							serviceCardList.add(new ServiceCardList(0, this.commonService.getUUID(), eservice, details, today));
						}							
					}
				}
				this.serviceCardListService.saveAll(serviceCardList);				
				
				List<ServiceCardList> allScardList=this.serviceCardListService.findByServiceDetails(details);
				allScardList.removeAll(serviceCardList);					
				this.serviceCardListService.deleteAll(allScardList);
				
				return "redirect:/admin/services/details/"+serviceUUID;
			
		} catch (Exception e) {e.printStackTrace();
			model.addAttribute("title", "Corpseed Dashboard | Edit Service details");
			model.addAttribute("appendClass", "services");
			model.addAttribute("details", details);
			model.addAttribute("products", this.productService.getAllProducts());
			model.addAttribute("detailsUUID", detailsUUID);
			model.addAttribute("serviceUUID", serviceUUID);
			model.addAttribute("industries", this.industryService.getIndustryByDisplsyStatus("1"));
			model.addAttribute("services", this.servicesServices.getAllServicesByStatus("1"));
			session.setAttribute("message", new Message("Error : "+e.getMessage(), "alert-danger"));
			return "admin/details-edit";
		}
	}
	@GetMapping("/details/update-form-status")
	@ResponseBody
	public void updateServiceDetails(@RequestParam("uuid") String uuid,@RequestParam("status") String status,PrintWriter pw) {
		ServiceDetails servicedetails=this.servicesServices.getServiceDetailsByUuid(uuid);
		if(servicedetails!=null) {
			servicedetails.setFormShowStatus(status);
			ServiceDetails saveServiceDetails = this.servicesServices.saveServiceDetails(servicedetails);
			if(saveServiceDetails!=null)pw.write("pass");
			else pw.write("fail");
		}else pw.write("fail");
	}
	
	@PostMapping("/details/add/{serviceUUID}")
	public String saveDetails(@Valid @ModelAttribute("details") ServiceDetails details,BindingResult result,
			@PathVariable("serviceUUID") String serviceUUID,@RequestParam("servicesId") List<String> servicesId,Model model,Principal principal,HttpSession session) {
		
		try {
			model.addAttribute("title", "Corpseed Dashboard | Add Details");
			model.addAttribute("appendClass", "services");
			if(result.hasErrors()) {
				model.addAttribute("products", this.productService.getAllProducts());
				model.addAttribute("services", this.servicesServices.getAllServicesByStatus("1"));
				model.addAttribute("industries", this.industryService.getIndustryByDisplsyStatus("1"));
				model.addAttribute("details", details);
				session.setAttribute("message", new Message("Please fill out the blank fields !!", "alert-danger"));
				return "admin/details-add";
			}
			Services service=this.servicesServices.findByUUID(serviceUUID);
		
			//checking details already existed or not
			ServiceDetails existDetails=this.servicesServices.isServiceDetailsExist(details.getTabName(),service);
			ServiceDetails existDetails1=this.servicesServices.findByDisplayOrderAndServices(details.getDisplayOrder(),service);
			
			if(existDetails!=null||existDetails1!=null) {
				model.addAttribute("products", this.productService.getAllProducts());
				model.addAttribute("services", this.servicesServices.getAllServicesByStatus("1"));
				model.addAttribute("industries", this.industryService.getIndustryByDisplsyStatus("1"));
				model.addAttribute("details", details);
				if(existDetails.getDeleteStatus()==1)
					session.setAttribute("message", new Message("Service details exist in Trash !!", "alert-danger"));
				else
					session.setAttribute("message", new Message("Either tab name or display order is already exist !!", "alert-danger"));
					
				model.addAttribute("industries", this.industryService.getIndustryByDisplsyStatus("1"));
				model.addAttribute("services", this.servicesServices.getAllServicesByStatus("1"));
				return "admin/details-add";
			}
				details.setUuid(this.commonService.getUUID());
				details.setServices(service);
				details.setAddedByUUID(this.commonService.getLoginedUser(principal).getUuid());
				String description=details.getDescription();

				description=description.replace("<table","<div class='table-width'><table");
				description=description.replace("</table>","</table></div>");
				
				
					if(!description.contains("playVideo(")&&description.contains("<iframe")) {
					
					String description1=description.substring(0,description.indexOf("<iframe"));
					description1+="<div class='text-center mt-2'><div class='vediosec' id='"+details.getUuid()+"' onclick=\"playVideo('"+details.getUuid()+"','"+(details.getUuid()+1)+"')\">"
							+"<img src='/assets/img/logo.png' class='img-fluid' alt='Corpseed' width='151' height='28'>"
					+"<div class='playbtn'>"
                    +"<h1>Start your <b class='text-primary'>"+service.getServiceName()+"</b> today!</h1>"
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
			ServiceDetails saveDetails=this.servicesServices.saveServiceDetails(details);
			 
			if(saveDetails==null) {
				model.addAttribute("products", this.productService.getAllProducts());
				model.addAttribute("services", this.servicesServices.getAllServicesByStatus("1"));
				model.addAttribute("industries", this.industryService.getIndustryByDisplsyStatus("1"));
				model.addAttribute("details", details);
				session.setAttribute("message", new Message("Data Not Saved, Please try-again later !!", "alert-danger"));
				model.addAttribute("industries", this.industryService.getIndustryByDisplsyStatus("1"));
				model.addAttribute("services", this.servicesServices.getAllServicesByStatus("1"));
				return "admin/details-add";
			}else {
				List<ServiceCardList> serviceCardList=new ArrayList<>();
				String today=this.commonService.getToday();
				for (String string : servicesId) {
					if(!string.equalsIgnoreCase("NA")) {
						Services eservice=this.servicesServices.findByIdAndDisplayStatus(Long.parseLong(string), "1");
						serviceCardList.add(new ServiceCardList(0, this.commonService.getUUID(), eservice, details, today));
					}
				}
				this.serviceCardListService.saveAll(serviceCardList);
							
				return "redirect:/admin/services/details/{serviceUUID}";
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			model.addAttribute("products", this.productService.getAllProducts());
			model.addAttribute("services", this.servicesServices.getAllServicesByStatus("1"));
			model.addAttribute("industries", this.industryService.getIndustryByDisplsyStatus("1"));
			model.addAttribute("details", details);
			session.setAttribute("message", new Message("Error : "+e.getMessage(), "alert-danger"));
			model.addAttribute("industries", this.industryService.getIndustryByDisplsyStatus("1"));
			model.addAttribute("services", this.servicesServices.getAllServicesByStatus("1"));
			return "admin/details-add";
		}
	}
	
	//testmonial
	@GetMapping("/testimonial/{uuid}")
	public String testimonial(@PathVariable("uuid") String uuid,Model model) {
		model.addAttribute("title", "Corpseed | Testmonial");
		model.addAttribute("appendClass", "services");
		Services service = this.servicesServices.findByUUID(uuid);
				
		model.addAttribute("testimonials",this.testimonialService.findByServicesAndDeleteStatus(service,2));
		model.addAttribute("serviceUuid", uuid);
		
		return "admin/testimonial";
	}
	
	@GetMapping("/testimonial/add/{serviceUuid}")
	public String addTestimonial(Model model,@PathVariable("serviceUuid") String serviceUuid) {
		model.addAttribute("title", "Corpseed | Testmonial");
		model.addAttribute("appendClass", "services");
		model.addAttribute("serviceUuid", serviceUuid);
		model.addAttribute("testimonial", new Testimonial());
		return "admin/add-testimonial";
	}
	
	@PostMapping("/testimonial/save")
	public String saveTestimonial(@Valid @ModelAttribute("testimonial") Testimonial testimonial,
			BindingResult result,@RequestParam("serviceUuid") String serviceUuid,Model model,
			HttpSession session,@RequestParam("file") MultipartFile file) {
		
		if(result.hasErrors()) {
			model.addAttribute("title", "Corpseed Dashboard | Add Testimonial");
			model.addAttribute("appendClass", "services");
			model.addAttribute("testimonial", testimonial);
			session.setAttribute("message", new Message("Please fill out the blank fields !!", "alert-danger"));
			return "admin/add-testimonial";
		}
		
		Services service=this.servicesServices.findServiceByUuidAndDeleteStatus(serviceUuid,2);
		if(service==null) {
			model.addAttribute("title", "Corpseed Dashboard | Add Testimonial");
			model.addAttribute("appendClass", "services");
			model.addAttribute("testimonial", testimonial);
			session.setAttribute("message", new Message("Service Not found, please try again later !!", "alert-danger"));
			return "admin/add-testimonial";
		}
		
		testimonial.setServices(service);
		
		if(!file.isEmpty()) {
			String name = this.azureAdapter.upload(file, this.commonService.getUniqueCode());
			testimonial.setProfilePicture(name);
		}else testimonial.setProfilePicture("profile.png");
		
		Testimonial saveTestimonial = this.testimonialService.saveTestimonial(testimonial);
		if(saveTestimonial==null) {
			model.addAttribute("title", "Corpseed Dashboard | Add Testimonial");
			model.addAttribute("appendClass", "services");
			model.addAttribute("testimonial", testimonial);
			session.setAttribute("message", new Message("Testimonial not saved, Please try-again later !!", "alert-danger"));
			return "admin/add-testimonial";
		}
		
		return "redirect:/admin/services/testimonial/"+serviceUuid;
	}
	
	@GetMapping("/testimonial/edit/{id}")
	public String editTestimonial(@PathVariable("id") long id,Model model) {
		Testimonial testimonial=this.testimonialService.findTestimonialById(id); 
		model.addAttribute("title", "Corpseed | Testmonial");
		model.addAttribute("appendClass", "services");
		model.addAttribute("testimonial", testimonial);
		return "admin/edit-testimonial";
	}
	
	@PostMapping("/testimonial/update")
	public String updateTestimonial(@Valid @ModelAttribute("testimonial") Testimonial testimonial,
			BindingResult result,Model model,HttpSession session,
			@RequestParam("file") MultipartFile file) {
		
		if(result.hasErrors()) {
			model.addAttribute("title", "Corpseed Dashboard | Edit Testimonial");
			model.addAttribute("appendClass", "services");
			model.addAttribute("testimonial", testimonial);
			session.setAttribute("message", new Message("Please fill out the blank fields !!", "alert-danger"));
			return "admin/edit-testimonial";
		}
		
		Services service=testimonial.getServices();
		if(service==null) {
			model.addAttribute("title", "Corpseed Dashboard | Edit Testimonial");
			model.addAttribute("appendClass", "services");
			model.addAttribute("testimonial", testimonial);
			session.setAttribute("message", new Message("Service Not found, please try again later !!", "alert-danger"));
			return "admin/edit-testimonial";
		}
				
		if(!file.isEmpty()) {
			String name = this.azureAdapter.upload(file, this.commonService.getUniqueCode());
			testimonial.setProfilePicture(name);
		}
		
		Testimonial saveTestimonial = this.testimonialService.saveTestimonial(testimonial);
		if(saveTestimonial==null) {
			model.addAttribute("title", "Corpseed Dashboard | Edit Testimonial");
			model.addAttribute("appendClass", "services");
			model.addAttribute("testimonial", testimonial);
			session.setAttribute("message", new Message("Testimonial not saved, Please try-again later !!", "alert-danger"));
			return "admin/edit-testimonial";
		}
		
		return "redirect:/admin/services/testimonial/"+service.getUuid();
	}
	
	@GetMapping("/testimonial/delete/{id}")
	public String deleteTestimonial(@PathVariable("id") long id) {
		Testimonial testimonial=this.testimonialService.findTestimonialById(id);
		String serviceUuid=testimonial.getServices().getUuid();
		if(testimonial!=null)
			this.testimonialService.deleteTestimonial(testimonial);

		return "redirect:/admin/services/testimonial/"+serviceUuid;
	}

	@GetMapping("/citybystate")
	public @ResponseBody Map<Long, String> getCityByStateId(@RequestParam("stateId") Long stateId) {
		State state=this.masterService.findStateById(stateId);
		if(state!=null)
		return this.masterService.findAllCityByStateId(state)
				.stream().collect(Collectors.toMap(City::getId, City::getCityName));
		return null;
	}

	@GetMapping("/isStateService")
	public @ResponseBody Map<Long, String> isStateService(@RequestParam("serviceId") long serviceId) {
		Services services=this.servicesServices.findByIdAndDeleteStatus(serviceId,2);
		if(services!=null&&services.getServiceState()!=null)
			return this.masterService.findAllCityByStateId(this.stateService.findByStateName(services.getServiceState().getStateName()))
					.stream().collect(Collectors.toMap(City::getId, City::getCityName));
		return null;
	}

	/*@PostMapping(value = "/enquiry",consumes = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public String importEnquiry(@RequestBody List<EnquiryDto> enquiry){
//		System.out.println(enquiry);
		enquiry.forEach(e-> System.out.println(e.getCreated_Time()+"\t"+e.getFull_Name()+"\t"+e.getEmail()+"\t"+e.getPhone_Number()+"\t"+e.getStreet_Address()));
		return "pass";
	}*/

	@Transactional
	@PostMapping("/enquiry")
	@ResponseBody
	public String importEnquiry(@RequestParam String enquiry) throws JsonProcessingException {
//		System.out.println(enquiry);
		ObjectMapper mapper = new ObjectMapper();
		List<EnquiryDto> enquiryDtoList = mapper.readValue(enquiry, new TypeReference<>() {
		});
		this.enquiryService.saveAllEnquiry(enquiryDtoList.stream().map(e->mapToEnquiry(e)).collect(Collectors.toList()));
		return "pass";
	}

	public Enquiry mapToEnquiry(EnquiryDto e) {
		return new Enquiry(0,commonService.getUUID(),e.getPlatform(),e.getAd_Name()+"\nAddress :"+e.getStreet_Address(),
		e.getFull_Name(),e.getEmail(),e.getPhone_Number(),e.getState(),"NA","NA","NA","NA","NA","1",e.getCreated_Time().toString(),
		this.commonService.getDateTime(),1,2,2);
	}
}
