package com.corpseed.controller;

import java.io.PrintWriter;
import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
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

import com.corpseed.entity.blogentity.Blogs;
import com.corpseed.entity.Category;
import com.corpseed.entity.footerentity.FooterService;
import com.corpseed.entity.History;
import com.corpseed.entity.HotTags;
import com.corpseed.entity.LawUpdate;
import com.corpseed.entity.orderentity.Orders;
import com.corpseed.entity.serviceentity.Services;
import com.corpseed.entity.SiteMapUrl;
import com.corpseed.entity.User;
import com.corpseed.util.Message;
import com.corpseed.serviceImpl.AzureBlobAdapter;
import com.corpseed.serviceImpl.blogserviceimpl.BlogService;
import com.corpseed.serviceImpl.CategoryService;
import com.corpseed.serviceImpl.CommonServices;
import com.corpseed.serviceImpl.HistoryService;
import com.corpseed.serviceImpl.HotTagService;
import com.corpseed.serviceImpl.LawUpdateService;
import com.corpseed.serviceImpl.orderserviceimpl.OrdersService;
import com.corpseed.serviceImpl.servicesserviceimpl.ServicesService;
import com.corpseed.serviceImpl.SiteMapService;
import com.corpseed.serviceImpl.UserServices;

@Controller
@RequestMapping("/admin/category")
public class CategoryController {
  
	@Autowired
	private CategoryService categoryService;
	
	@Autowired
	private UserServices userservices;
	
	@Autowired
	private CommonServices commonService;
	
	@Autowired
	private BCryptPasswordEncoder passwordEncoder;
	
	@Autowired
	private BlogService blogService;
	
	@Autowired
	private ServicesService servicesService;
	
	@Autowired
	private LawUpdateService lawUpdateService;
	
	@Autowired
	private HistoryService historyService;
		
	@Autowired
	private SiteMapService siteMapService;
	
	@Autowired
	private HotTagService hotTagService;
	
	@Autowired
	private OrdersService orderService;
	
	@Autowired
    AzureBlobAdapter azureAdapter;
		
	@GetMapping("/")
	public String allCategory(Model model) {
		model.addAttribute("title", "Corpseed Dashboard | All category");
		model.addAttribute("appendClass", "category");
		model.addAttribute("category", this.categoryService.getAllSubCategory());
		return "admin/category";
	}
	
	@GetMapping("/add")
	public String addCategory(Model model) {
		model.addAttribute("title", "Corpseed Dashboard | Add category");
		model.addAttribute("appendClass", "category");
		model.addAttribute("category", new Category());		
		return "admin/category-add";
	}
	@GetMapping("/edit/{uuid}")
	public String editCategory(@PathVariable("uuid") String uuid,Model model) {
		model.addAttribute("title", "Corpseed Dashboard | Edit category");
		model.addAttribute("appendClass", "category");
		model.addAttribute("category", this.categoryService.findByCategoryUUID(uuid));
		return "admin/category-edit";
	}
	@GetMapping("/delete/{uuid}")
	public @ResponseBody void deleteCategory(@PathVariable("uuid") String uuid,@RequestParam("password") String password,Principal principal,PrintWriter pw
			,HttpServletRequest req) {
		User user=this.commonService.getLoginedUser(principal);
		if(user.getRole().equals("ROLE_ADMIN")) {
			if(passwordEncoder.matches(password, user.getPassword())) {
				Category category=this.categoryService.findByCategoryUUID(uuid);
//					this.categoryService.deleteCategory(category.getId());	
					if(category!=null) {
					category.setDeleteStatus(1);
					//updating category
					this.categoryService.saveCategory(category, user);
					
					//adding category in history
					this.historyService.savehistory(new History(0, this.commonService.getUUID(),
							"Category", category.getId(), this.commonService.getBrowser(req), 
							this.commonService.getIpAddress(req), user.getId(), user.getFirstName()+" "+user.getLastName()
							, this.commonService.getToday(), this.commonService.getTime(),category.getTitle()));		
						
					List<SiteMapUrl> siteMap=new ArrayList<>();
					
					//updating services
					List<Services> services=category.getServices();
					List<HotTags> hotTags=new ArrayList<>();
					List<Orders> orders=new ArrayList<>();
					List<FooterService> footerServices=new ArrayList<>();
					if(!services.isEmpty()) {						
						for (Services s : services) {
							s.setDeleteStatus(1);
							//updating hot Tag
							HotTags hotTag=s.getHotTags();
							if(hotTag!=null) {
								hotTags.add(hotTag);
							}
							if(!s.getOrders().isEmpty()) {
								orders.addAll(s.getOrders());
							}
							if(s.getFooterService()!=null) {
								footerServices.add(s.getFooterService());
							}
							SiteMapUrl siteMap1 = this.siteMapService.findByTypeAndUid("Service", s.getId());
							if(siteMap1!=null)
							siteMap.add(siteMap1);
						}
						if(!hotTags.isEmpty()) {
							hotTags.forEach(ht->ht.setDeleteStatus(1));
							this.hotTagService.saveTags(hotTags);
						}
						if(!orders.isEmpty()) {
							orders.forEach(ord->ord.setDeleteStatus(1));
							this.orderService.saveOrders(orders);
						}
						this.servicesService.saveAllServices(services);
					}
					
					
					//updating blogs
					List<Blogs> blogs=category.getBlogs();
					
					if(!blogs.isEmpty()) {
						for (Blogs b : blogs) {
							b.setDeleteStatus(1);					
							
							//updating sitemap url
							SiteMapUrl siteMap1 = this.siteMapService.findByTypeAndUid("Blog", b.getId());
							if(siteMap1!=null) {
								siteMap.add(siteMap1);
							}	
							
						}
						this.blogService.saveAllBlogs(blogs);							
					}
					
					//updating lawUpdate
					List<LawUpdate> lawUpdate=category.getLawUpdate();
					if(!lawUpdate.isEmpty()) {
						lawUpdate.forEach(l->l.setDeleteStatus(1));					
						this.lawUpdateService.saveAllLawUpdate(lawUpdate);
					}
					
					if(!siteMap.isEmpty()) {
						siteMap.forEach(sm->sm.setStatus(2));
						this.siteMapService.saveSiteMap(siteMap);
					}					
					
				pw.write("pass");	
					}
			}else {
				pw.write("fail");
			}
		}else {
			pw.write("fail");
		}
	}
	
	@PostMapping("/update/{uuid}")
	public String updateCategory(@Valid @ModelAttribute("category") Category category,BindingResult result,
			@PathVariable("uuid") String uuid,@RequestParam("categoryName") String categoryName,@RequestParam("icons") MultipartFile icons
			,Model model,HttpSession session,Principal principal) {
		model.addAttribute("title", "Corpseed Dashboard | Update category");
		model.addAttribute("appendClass", "category");
		
		try {
			if(result.hasErrors()) {
				model.addAttribute("category", category);
				session.setAttribute("message", new Message("Please fill out the blank fields !!", "alert-danger"));
				return "admin/category-edit";
			}	
			Category updateExist=this.categoryService.findSubCategoryExceptOne(categoryName,category.getSubCategoryName(),uuid);
			if(updateExist!=null) {
				model.addAttribute("category", category);
				if(updateExist.getDeleteStatus()==1)
					session.setAttribute("message", new Message("Category already exist in Trash !! ", "alert-danger"));
				else
					session.setAttribute("message", new Message("Category already exist !! ", "alert-danger"));
					
	        	return "admin/category-edit";
			}
			//	System.out.println(category);
				Category findCategory=this.categoryService.findByCategoryUUID(uuid);
				if(!icons.isEmpty()) {
					String upload = this.azureAdapter.upload(icons, 0);
					category.setIcon(upload);
				}else category.setIcon(findCategory.getIcon());
				
				category.setId(findCategory.getId());
				category.setUuid(findCategory.getUuid());
				category.setAddedByUUID(this.commonService.getLoginedUser(principal).getUuid());
		        Category updateCategory = this.categoryService.updateCategory(category);
		        	
		        SiteMapUrl siteMapUrl=this.siteMapService.findByTypeAndUid("Category",updateCategory.getId());
				if(siteMapUrl==null) {
					this.siteMapService.saveUrl(new SiteMapUrl(0, this.commonService.getUUID(), updateCategory.getId(), "Category", "/category/"+updateCategory.getSlug(), 1, this.commonService.getToday()));
				}else {
					siteMapUrl.setUrl("/category/"+updateCategory.getSlug());
					this.siteMapService.saveUrl(siteMapUrl);
				}
		        
		        return "redirect:/admin/category/";
			 
		} catch (Exception e) {
			model.addAttribute("category", category);
			session.setAttribute("message", new Message("Error : " + e.getMessage(), "alert-danger"));
			return "admin/category-edit";
		}		
	}
	
	@PostMapping("/add")
	public String saveCategory(@Valid @ModelAttribute("category") Category category,BindingResult result,Model model,
			HttpSession session,Principal principle,@RequestParam("icons") MultipartFile icons) {
		model.addAttribute("appendClass", "category");
		model.addAttribute("title", "Corpseed Dashboard | Add category");
		
		try {
			if(result.hasErrors()) {
				model.addAttribute("category", category);
				session.setAttribute("message", new Message("Please fill out the blank fields !!", "alert-danger"));
				return "admin/category-add";
			}
			Category subCategoryExist = this.categoryService.isSubCategoryExist(category.getCategoryName(), category.getSubCategoryName());
			model.addAttribute("category", category);
			if(subCategoryExist!=null) {
			if(subCategoryExist.getDeleteStatus()==1)
				session.setAttribute("message", new Message("Category already exist in Trash !!", "alert-danger"));
			else
				session.setAttribute("message", new Message("Category already exist !!", "alert-danger"));
			}
			
			if(!icons.isEmpty()) {
				String upload = this.azureAdapter.upload(icons, 0);
				category.setIcon(upload);
			}
			
			UUID uuid = UUID.randomUUID();
	        String uuidAsString = uuid.toString();
	        
	        category.setUuid(uuidAsString.replace("-",""));
	        //getting logined user object
	        User userExist = this.userservices.isUserExist(principle.getName());
//	        System.out.println(category);
	        Category saveCategory =this.categoryService.saveCategory(category,userExist);
	        if(saveCategory==null) {
	        	model.addAttribute("category", category);
	        	return "admin/category-add";
	        }
			
			//adding sitemap url
			this.siteMapService.saveUrl(new SiteMapUrl(0, this.commonService.getUUID(),saveCategory.getId(),"Category", "/category/"+saveCategory.getSlug(), 1, this.commonService.getToday()));
			
			return "redirect:/admin/category/";	        
	       
		} catch (Exception e) {
			e.printStackTrace();
			model.addAttribute("category", category);
			session.setAttribute("message", new Message("Error : " + e.getMessage(), "alert-danger"));
			return "admin/category-add";
		}		
	}

}
