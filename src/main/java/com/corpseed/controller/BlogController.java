package com.corpseed.controller;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import com.corpseed.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
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

import com.corpseed.entity.blogentity.BlogFaq;
import com.corpseed.entity.blogentity.BlogServiceCardList;
import com.corpseed.entity.blogentity.Blogs;
import com.corpseed.entity.Category;
import com.corpseed.entity.enquiryentity.Enquiry;
import com.corpseed.entity.History;
import com.corpseed.entity.serviceentity.ServiceBlogs;
import com.corpseed.entity.serviceentity.Services;
import com.corpseed.entity.SiteMapUrl;
import com.corpseed.entity.User;
import com.corpseed.util.Message;
import com.corpseed.serviceImpl.AzureBlobAdapter;
import com.corpseed.serviceImpl.blogserviceimpl.BlogService;
import com.corpseed.serviceImpl.blogserviceimpl.BlogServiceCardService;
import com.corpseed.serviceImpl.CategoryService;
import com.corpseed.serviceImpl.CommonServices;
import com.corpseed.serviceImpl.enqserviceimpl.EnquiryService;
import com.corpseed.serviceImpl.HistoryService;
import com.corpseed.serviceImpl.IndustryService;
import com.corpseed.serviceImpl.servicesserviceimpl.ServicesBlogsService;
import com.corpseed.serviceImpl.servicesserviceimpl.ServicesService;
import com.corpseed.serviceImpl.SiteMapService;
import com.corpseed.serviceImpl.TriggerService;
import com.corpseed.serviceImpl.UserServices;
import com.corpseed.service.BlogFaqService;

@Controller
@RequestMapping("/admin/blogs")
public class BlogController {
	
	@Autowired
	private CommonServices commonService;
	
	@Autowired
	private BlogService blogService;
	
	@Autowired
	private ServicesService serviceService;
	
	@Autowired
	private CategoryService categoryService;
	
	@Autowired
	private EnquiryService enquiryService;
	
	@Autowired
	private TriggerService triggerService;
		
	@Autowired
	private ServicesBlogsService servicesBlogService;
	
	@Autowired
    AzureBlobAdapter azureAdapter;
	
	@Autowired
	private SiteMapService siteMapService;
	
	@Autowired
	private UserServices userService;
	
	@Autowired
	private BlogServiceCardService blogServiceCardService;
	
	@Autowired
	private HistoryService historyService;
	
	@Autowired
	private Environment env;
	
	@Autowired
	private IndustryService industryService;
	
	@Autowired
	private BlogFaqService blogFaqService;

	@Autowired
	private ProductService productService;
	
	@Value("${rssFeed.path}")
	private String rssFeed;
	
	@Value("${azure_path}")
	private String azure_path;
	
	@Value("${domain.name}")
	private String domain;

	@GetMapping("/")
	public String allBlogs(Model model) {
		model.addAttribute("title", "Corpseed Dashboard | Manage Blogs");
		model.addAttribute("appendClass", "blogs");
		model.addAttribute("blogs", blogService.getAllBlogs());		
		return "admin/blogs";
	}
	
	@GetMapping("/faq/{uuid}")
	public String allBlogsFaq(@PathVariable("uuid") String uuid,Model model) {
		model.addAttribute("title", "Corpseed Dashboard | Manage Blogs FAQ'S");
		model.addAttribute("appendClass", "blogs");
		model.addAttribute("bloguuid", uuid);
		model.addAttribute("faq", this.blogFaqService.allBlogFaq(this.blogService.getBlogByUuid(uuid)));
		return "admin/blog-faq";
	}
	
	@GetMapping("/faq/add/{uuid}")
	public String addBlogFaq(@PathVariable("uuid") String uuid,Model model) {
		model.addAttribute("title", "Corpseed Dashboard | Add Blog FAQ");
		model.addAttribute("appendClass", "blogs");
		model.addAttribute("faq", new BlogFaq());
		model.addAttribute("bloguuid", uuid);
		return "admin/blog-faq-add";
	}
	
	@GetMapping("/faq/edit/{faqUUID}")
	public String editFaq(@PathVariable("faqUUID") String faqUUID,Model model) {
		model.addAttribute("title", "Corpseed Dashboard | Edit FAQ`s");
		model.addAttribute("appendClass", "blogs");
		model.addAttribute("faq", this.blogFaqService.getBlogFaqByUUID(faqUUID));
		return "admin/blog-faq-edit";
	}
	
	@PostMapping("/faq/add/{bloguuid}")
	public String saveFaq(@Valid @ModelAttribute("faq") BlogFaq faq,BindingResult result,
			@PathVariable("bloguuid") String bloguuid,Model model,Principal principal,HttpSession session) {
		model.addAttribute("title", "Corpseed Dashboard | Add FAQ`s");
		model.addAttribute("appendClass", "blogs");
		try {			
			
			if(result.hasErrors()) {
				model.addAttribute("faq", faq);
				session.setAttribute("message", new Message("Please fill out the blank fields !!", "alert-danger"));
				return "admin/blog-faq-add";
			}
			Blogs blog=this.blogService.getBlogByUuid(bloguuid);
			
			//checking package already existed or not
			BlogFaq existFaq=this.blogFaqService.isBlogFaqExist(faq.getTitle(),blog);
			if(existFaq!=null) {
				model.addAttribute("faq", faq);
				if(existFaq.getDeleteStatus()==1)
					session.setAttribute("message", new Message("Blog faq exist in Trash !!", "alert-danger"));
				else
					session.setAttribute("message", new Message("Blog faq already exist !!", "alert-danger"));
				
				return "admin/blog-faq-add";
			}
				faq.setUuid(this.commonService.getUUID());
				faq.setBlogs(blog);
				faq.setAddedByUUID(this.commonService.getLoginedUser(principal).getUuid());
			
				String description=faq.getDescription();

				description=description.replace("<table","<div class='table-width'><table");
				description=description.replace("</table>","</table></div>");
				
				faq.setDescription(description);
				
			BlogFaq saveFaq=this.blogFaqService.saveBlogFaq(faq);
			
			if(saveFaq==null) {
				model.addAttribute("faq", faq);
				session.setAttribute("message", new Message("Data Not Saved, Please try-again later !!", "alert-danger"));
				return "admin/blog-faq-add";
			}else {
				return "redirect:/admin/blogs/faq/"+bloguuid;
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			model.addAttribute("faq", faq);
			session.setAttribute("message", new Message("Error : "+e.getMessage(), "alert-danger"));
			return "admin/blog-faq-add";
		}
	}
	
	@GetMapping("/faq/delete/{uuid}")
	public String deleteFaq(@PathVariable("uuid") String uuid,Principal principal,HttpServletRequest req) {
		if(this.commonService.getLoginedUser(principal).getRole().equals("ROLE_ADMIN")) {
			BlogFaq blogFaq=this.blogFaqService.getBlogFaqByUUID(uuid);
			
			if(blogFaq!=null) {
				blogFaq.setDeleteStatus(1);
				this.blogFaqService.saveBlogFaq(blogFaq);
				User user=this.commonService.getLoginedUser(principal);
				//adding category in history
				this.historyService.savehistory(new History(0, this.commonService.getUUID(),
						"Blog FAQ", blogFaq.getId(), this.commonService.getBrowser(req), 
						this.commonService.getIpAddress(req), user.getId(), user.getFirstName()+" "+user.getLastName()
						, this.commonService.getToday(), this.commonService.getTime(),blogFaq.getTitle()));		
			
			}
//			this.servicesServices.deleteServiceFaq(serviceFaq.getId());
			
			return "redirect:/admin/blogs/faq/"+blogFaq.getBlogs().getUuid();
		}else {
			return "error/403";
		}
	}
	
	@PostMapping("/faq/edit")
	public String updateFaq(@Valid @ModelAttribute("faq") BlogFaq faq,BindingResult result,
			Model model,Principal principal,HttpSession session) {
		model.addAttribute("title", "Corpseed Dashboard | Edit FAQ`s");
		model.addAttribute("appendClass", "blogs");
		try {			
			
			if(result.hasErrors()) {
				model.addAttribute("faq", faq);
				session.setAttribute("message", new Message("Please fill out the blank fields !!", "alert-danger"));
				return "admin/blog-faq-edit";
			}			
			//checking package already existed or not
			BlogFaq existFaq=this.blogFaqService.isEditBlogFaqExist(faq.getTitle(),faq.getBlogs(),faq.getUuid());
			if(existFaq!=null) {
				model.addAttribute("faq", faq);
				if(existFaq.getDeleteStatus()==1)
					session.setAttribute("message", new Message("Blog faq exist in Trash !!", "alert-danger"));
				else
					session.setAttribute("message", new Message("Blog faq already exist !!", "alert-danger"));
				
				return "admin/blog-faq-edit";
			}
				faq.setAddedByUUID(this.commonService.getLoginedUser(principal).getUuid());
			
				String description=faq.getDescription();

				description=description.replace("<table","<div class='table-width'><table");
				description=description.replace("</table>","</table></div>");
				
				faq.setDescription(description);
				
			BlogFaq saveFaq=this.blogFaqService.saveBlogFaq(faq);
			
			if(saveFaq==null) {
				model.addAttribute("faq", faq);
				session.setAttribute("message", new Message("Data Not Saved, Please try-again later !!", "alert-danger"));
				return "admin/blog-faq-edit";
			}else {
				return "redirect:/admin/blogs/faq/"+faq.getBlogs().getUuid();
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			model.addAttribute("faq", faq);
			session.setAttribute("message", new Message("Error : "+e.getMessage(), "alert-danger"));
			return "admin/blog-faq-edit";
		}
	}
	
	@GetMapping("/add")
	public String addBlog(Model model) {
		model.addAttribute("title", "Corpseed Dashboard | Add Blog");
		model.addAttribute("appendClass", "blogs");
		model.addAttribute("blogs", new Blogs());
		model.addAttribute("services", this.serviceService.getAllServicesByStatus("1"));
		model.addAttribute("industries", this.industryService.getIndustryByDisplsyStatus("1"));
		model.addAttribute("category", this.categoryService.getAllSubCategory());
		model.addAttribute("users", this.userService.findAllNotUserAndAccountStatus());
		return "admin/blogs-add";
	}
	@GetMapping("/edit/{blogUUID}")
	public String editPackage(@PathVariable("blogUUID") String blogUUID,Model model) {
		model.addAttribute("title", "Corpseed Dashboard | Edit Blog");
		model.addAttribute("appendClass", "blogs");
		model.addAttribute("products", this.productService.getAllProducts());
		List<Services> serviceList=this.serviceService.getAllServicesByStatus("1");
		model.addAttribute("services1", serviceList);
		List<Services> serviceList1=new ArrayList<>();
		serviceList1.addAll(serviceList);
		Blogs blogs=this.blogService.getBlogByUuid(blogUUID);
		for (ServiceBlogs sBlogs : blogs.getServiceBlogs()) {
			serviceList1.remove(sBlogs.getServices());
		}
		model.addAttribute("services", serviceList1);
		
		blogs.getBlogServiceCardLists()
				.stream().forEach(bs->serviceList.remove(bs.getService()));

		model.addAttribute("cardService", serviceList);
		model.addAttribute("category", this.categoryService.getAllSubCategory());
		model.addAttribute("users", this.userService.findAllNotUserAndAccountStatus());
		
		String description=blogs.getDescription();
		while(description.contains("<iframe src=\"https://docs.google.com/gview?url=")||description.contains("<iframe src=\"http://docs.google.com/gview?url=")) {
			description = description.replace("<iframe src=\"http://docs.google.com/gview?url=", "pdfview=");
			description = description.replace("<iframe src=\"https://docs.google.com/gview?url=", "pdfview=");
			description = description.replace("&embedded=true\" frameborder=\"0\" webkitallowfullscreen mozallowfullscreen allowfullscreen style=\"width: 100%; height: 700px;\"></iframe>", "=pdfview");
			description=description.replace("&embedded=true\" frameborder=\"0\" webkitallowfullscreen mozallowfullscreen allowfullscreen style=\"width: 100%; height: 500px;\">","=pdfview");
		}	
				
		blogs.setDescription(description);		
		
		model.addAttribute("blogs", blogs);
		return "admin/blogs-edit";
	} 
	@GetMapping("/delete/{uuid}")
	public String deleteBlogs(@PathVariable("uuid") String uuid,Principal principal,HttpServletRequest req) {	
		Blogs blogs=this.blogService.getBlogByUuid(uuid);		
		if(blogs!=null&&this.commonService.getUserRole(principal).equals("ROLE_ADMIN")) {
			blogs.setDeleteStatus(1);
			this.blogService.saveBlogs(blogs);
			
			User user=this.commonService.getLoginedUser(principal);
			
			//adding category in history
			this.historyService.savehistory(new History(0, this.commonService.getUUID(),
					"Blogs", blogs.getId(), this.commonService.getBrowser(req), 
					this.commonService.getIpAddress(req), user.getId(), user.getFirstName()+" "+user.getLastName()
					, this.commonService.getToday(), this.commonService.getTime(),blogs.getTitle()));		

			
			//updating service blogs
			List<ServiceBlogs> serviceBlogs=blogs.getServiceBlogs();
			if(!serviceBlogs.isEmpty()) {
				serviceBlogs.forEach(sb->sb.setDeleteStatus(1));
				this.servicesBlogService.saveServiceBlogs(serviceBlogs);
			}
			//updating service blog card list
			List<BlogServiceCardList> blogServiceCardList=blogs.getBlogServiceCardLists();
			if(!blogServiceCardList.isEmpty()) {
				blogServiceCardList.forEach(bscl->bscl.setDeleteStatus(1));
				this.blogServiceCardService.saveAll(blogServiceCardList);
			}
			//updating sitemap url
			SiteMapUrl siteMap = this.siteMapService.findByTypeAndUid("Blog", blogs.getId());
			if(siteMap!=null) {
				siteMap.setStatus(2);
				this.siteMapService.saveUrl(siteMap);
			}			
		}else {
			return "error/403";
		}
		return "redirect:/admin/blogs/";
	}
	@PostMapping("/add")
	public String saveBlog(@Valid @ModelAttribute("blogs") Blogs blogs,BindingResult result,@RequestParam("categoryUuid") String categoryUuid,HttpSession session,Model model,Principal principal
			,@RequestParam("serviceCardId") List<String> serviceCardId,@RequestParam("file") MultipartFile file) {
		try {
			model.addAttribute("title", "Corpseed Dashboard | Add Blogs");
			model.addAttribute("appendClass", "blogs");
			model.addAttribute("users", this.userService.findAllNotUserAndAccountStatus());
			
			if(result.hasErrors()) {
				model.addAttribute("industries", this.industryService.getIndustryByDisplsyStatus("1"));
				model.addAttribute("services", this.serviceService.getAllServicesByStatus("1"));
				model.addAttribute("category", this.categoryService.getAllSubCategory());
				model.addAttribute("blogs", blogs);
				model.addAttribute("products", this.productService.getAllProducts());
				session.setAttribute("message", new Message("Please fill out the blank fields !!", "alert-danger"));
				return "admin/blogs-add";
			}
			if(blogs.getServiceName()==null||blogs.getServiceName().length()<=0) {
				model.addAttribute("services", this.serviceService.getAllServicesByStatus("1"));
				model.addAttribute("industries", this.industryService.getIndustryByDisplsyStatus("1"));
				model.addAttribute("category", this.categoryService.getAllSubCategory());
				model.addAttribute("blogs", blogs);
				model.addAttribute("products", this.productService.getAllProducts());
				session.setAttribute("message", new Message("Please select atleast on service !!", "alert-danger"));
				return "admin/blogs-add";
			}
			
			if(!file.isEmpty()) {
				String name = azureAdapter.upload(file,0);
				blogs.setImage(name);
			}
			Blogs findBlogs=this.blogService.findBySlugOrTitle(blogs.getSlug(),blogs.getTitle());
			if(findBlogs!=null) {
				model.addAttribute("services", this.serviceService.getAllServicesByStatus("1"));
				model.addAttribute("industries", this.industryService.getIndustryByDisplsyStatus("1"));
				model.addAttribute("category", this.categoryService.getAllSubCategory());
				model.addAttribute("blogs", blogs);
				model.addAttribute("products", this.productService.getAllProducts());
				if(findBlogs.getDeleteStatus()==1)
					session.setAttribute("message", new Message("Blog Exist in Trash !!", "alert-danger"));
				else
					session.setAttribute("message", new Message("URL Already Exist !!", "alert-danger"));
					
				return "admin/blogs-add";
			}
			User user=this.commonService.getLoginedUser(principal);
			String loginuuid=user.getUuid();
			blogs.setUuid(this.commonService.getUUID());
			blogs.setAddedByUUID(loginuuid);
			User author=this.userService.getUserByUuid(blogs.getPostedByUuid());
			blogs.setPostedByName(author.getFirstName()+" "+author.getLastName());
			Category categoryById = this.categoryService.findByCategoryUUID(categoryUuid);
			blogs.setCategory(categoryById);
			
			String description=blogs.getDescription();

			description=description.replace("<table","<div class='table-width'><table");
			description=description.replace("</table>","</table></div>");
		
			while(description.contains("pdfview=")) {
            	description=description.replace("pdfview=", "<iframe src=\"https://docs.google.com/gview?url=");
            	description=description.replace("=pdfview", "&embedded=true\" frameborder=\"0\" webkitallowfullscreen mozallowfullscreen allowfullscreen style=\"width: 100%; height: 700px;\"></iframe>");
            }            
			blogs.setDescription(description);
			Blogs saveBlog=this.blogService.saveBlogs(blogs);
			
			List<ServiceBlogs> serviceBlogList=new ArrayList<>();
			String x[]=blogs.getServiceName().split(",");	
			  if(x!=null&&x.length>0) { 
				  for(int i=0;i<x.length;i++) { 
					  Services serviceById = this.serviceService.findByIdAndDisplayStatus(Long.parseLong(x[i]),"1");
					  	if(serviceById!=null) {
					  		serviceBlogList.add(new ServiceBlogs(0, this.commonService.getUUID(), 
					  				saveBlog, serviceById));
					  	//getting all enquiry which is related to this service Category
							List<Enquiry> enquiryList=this.enquiryService.getAllEnquiryByTypeAndStatusAndCategoryId("service","1",saveBlog.getCategory().getId()+"");
							if(!enquiryList.isEmpty()) {
								this.triggerService.saveTriggers(enquiryList,saveBlog.getSlug(),"/knowledge-centre/",saveBlog.getTitle());
							}
					  	}
				  }
			  }
			  if(!serviceBlogList.isEmpty()) {
			        this.servicesBlogService.saveServiceBlogs(serviceBlogList);			  
			} 
			 //adding this url into sitemap
			  this.siteMapService.saveUrl(new SiteMapUrl(0, this.commonService.getUUID(), saveBlog.getId(), "Blog", "/knowledge-centre/"+saveBlog.getSlug(), 1, this.commonService.getToday()));
			
			  List<BlogServiceCardList> blogServiceCardList=new ArrayList<>();
				String today=this.commonService.getToday();
				for (String string : serviceCardId) {
					if(!string.equalsIgnoreCase("NA")) {
						Services eservice=this.serviceService.findByIdAndDisplayStatus(Long.parseLong(string), "1");
						blogServiceCardList.add(new BlogServiceCardList(0, this.commonService.getUUID(), eservice, saveBlog, today,2));
					}
				}
				if(!blogServiceCardList.isEmpty())
					this.blogServiceCardService.saveAll(blogServiceCardList);
				
			  return "redirect:/admin/blogs/";
			
		} catch (Exception e) {
			e.printStackTrace();
			model.addAttribute("products", this.productService.getAllProducts());
			model.addAttribute("services", this.serviceService.getAllServicesByStatus("1"));
			model.addAttribute("industries", this.industryService.getIndustryByDisplsyStatus("1"));
			model.addAttribute("category", this.categoryService.getAllSubCategory());
			model.addAttribute("blogs", blogs);
			session.setAttribute("message", new Message("Error : "+e.getMessage(), "alert-danger"));
			return "admin/blogs-add";
		}
			
	}	
	@PostMapping("/update")
	public String updateBlog(@Valid @ModelAttribute("blogs") Blogs blogs,BindingResult result,
			@RequestParam("categoryUuid") String categoryUuid,HttpSession session,Model model,Principal principal,
			@RequestParam("serviceCardId") List<String> serviceCardId,@RequestParam("file") MultipartFile file) {
		try {			
			model.addAttribute("title", "Corpseed Dashboard | Edit Blogs");
			model.addAttribute("appendClass", "blogs");			
			model.addAttribute("users", this.userService.findAllNotUserAndAccountStatus());
			if(result.hasErrors()) {
				model.addAttribute("products", this.productService.getAllProducts());
				model.addAttribute("services", this.serviceService.getAllServicesByStatus("1"));
				model.addAttribute("industries", this.industryService.getIndustryByDisplsyStatus("1"));
				model.addAttribute("category", this.categoryService.getAllSubCategory());
				model.addAttribute("blogs", blogs);
				session.setAttribute("message", new Message("Please fill out the blank fields !!", "alert-danger"));
				return "admin/blogs-edit";
			}
			if(blogs.getServiceName()==null||blogs.getServiceName().length()<=0) {
				model.addAttribute("products", this.productService.getAllProducts());
				model.addAttribute("services", this.serviceService.getAllServicesByStatus("1"));
				model.addAttribute("industries", this.industryService.getIndustryByDisplsyStatus("1"));
				model.addAttribute("category", this.categoryService.getAllSubCategory());
				model.addAttribute("blogs", blogs);
				session.setAttribute("message", new Message("Please select atleast on service !!", "alert-danger"));
				return "admin/blogs-edit";
			}
			Blogs findBlog=this.blogService.findBySlugOrTitleAndUuidNot(blogs.getSlug(),blogs.getTitle(),blogs.getUuid());
			if(findBlog!=null) {
				model.addAttribute("products", this.productService.getAllProducts());
				model.addAttribute("services", this.serviceService.getAllServicesByStatus("1"));
				model.addAttribute("industries", this.industryService.getIndustryByDisplsyStatus("1"));
				model.addAttribute("category", this.categoryService.getAllSubCategory());
				model.addAttribute("blogs", blogs);
				if(findBlog.getDeleteStatus()==1)
					session.setAttribute("message", new Message("Blog Exist in Trash !!", "alert-danger"));
				else
					session.setAttribute("message", new Message("URL Already Exist !!", "alert-danger"));
					
				return "admin/blogs-edit";
			}
				
			if(!file.isEmpty()) {				
				this.azureAdapter.deleteFile(blogs.getImage());
				String name = azureAdapter.upload(file,0);
				blogs.setImage(name);
			}	
			User user=this.commonService.getLoginedUser(principal);
			String loginuuid=user.getUuid();			
			blogs.setAddedByUUID(loginuuid);
			User author=this.userService.getUserByUuid(blogs.getPostedByUuid());
			blogs.setPostedByName(author.getFirstName()+" "+author.getLastName());
			Category categoryById = this.categoryService.findByCategoryUUID(categoryUuid);
			blogs.setCategory(categoryById);
			
			String description=blogs.getDescription();
			
			description=description.replace("<div class=\"table-width\">","");
			description=description.replace("</table>\r\n"
					+ "</div>","</table>");
			
			
			description=description.replace("<table","<div class='table-width'><table");
			description=description.replace("</table>","</table></div>");

			while(description.contains("pdfview=")) {
            	description=description.replace("pdfview=", "<iframe src=\"https://docs.google.com/gview?url=");
            	description=description.replace("=pdfview", "&embedded=true\" frameborder=\"0\" webkitallowfullscreen mozallowfullscreen allowfullscreen style=\"width: 100%; height: 700px;\"></iframe>");
            }
			
			blogs.setDescription(description);
			
			Blogs saveBlog=this.blogService.saveBlogs(blogs);
			
			List<ServiceBlogs> serviceBlogList=new ArrayList<>();
			String x[]=blogs.getServiceName().split(",");	
			
			  if(x!=null&&x.length>0) { 
				  for(int i=0;i<x.length;i++) { 
					  Services serviceById = this.serviceService.findByIdAndDisplayStatus(Long.parseLong(x[i]),"1");
					  	if(serviceById!=null) {
					  		ServiceBlogs serviceBlog=this.servicesBlogService.findByBlogsAndServices(saveBlog,serviceById);
					  		if(serviceBlog!=null) {
					  			serviceBlogList.add(serviceBlog);					  			
					  		}else {
					  			serviceBlogList.add(new ServiceBlogs(0,this.commonService.getUUID(), 
						  				saveBlog, serviceById));
					  		}
					  	}
				  }
			  }
			  List<ServiceBlogs> saveServiceBlogs = this.servicesBlogService.saveServiceBlogs(serviceBlogList);	
			  if(saveServiceBlogs.isEmpty()) {
				  model.addAttribute("products", this.productService.getAllProducts());
				  model.addAttribute("services", this.serviceService.getAllServicesByStatus("1"));
				  model.addAttribute("industries", this.industryService.getIndustryByDisplsyStatus("1"));
				    model.addAttribute("category", this.categoryService.getAllSubCategory());
					model.addAttribute("blogs", blogs);
					session.setAttribute("message", new Message("Service list is empty !!", "alert-danger"));
					return "admin/blogs-edit";
			  }				 
				  //get all serviceBlogs whose blogId is this and delete all except saved list
//				 List<ServiceBlogs> allServiceBlogs=this.servicesBlogService.findByBlogsAndServicesNotIn(saveBlog,servicesList);
				 List<ServiceBlogs>	 allServiceBlogs=this.servicesBlogService.findByBlogs(saveBlog);
				 allServiceBlogs.removeAll(saveServiceBlogs);
				 
				 if(!allServiceBlogs.isEmpty())
				 this.servicesBlogService.deleteAlls(allServiceBlogs);	
				 
				 //checking sitemap url existed or not if not then insert or update
				 SiteMapUrl siteMap=this.siteMapService.findByTypeAndUid("Blog", saveBlog.getId());
				 if(siteMap==null) {
					 this.siteMapService.saveUrl(new SiteMapUrl(0, this.commonService.getUUID(), saveBlog.getId(), "Blog", "/knowledge-centre/"+saveBlog.getSlug(), 1, this.commonService.getToday()));
				 }else {
					 siteMap.setUrl("/knowledge-centre/"+saveBlog.getSlug());
					 this.siteMapService.saveUrl(siteMap);
				 }
				 
				 List<BlogServiceCardList> blogServiceCardList=new ArrayList<>();
					String today=this.commonService.getToday();
					for (String string : serviceCardId) {
						if(!string.equalsIgnoreCase("NA")) {
							Services eservice=this.serviceService.findByIdAndDisplayStatus(Long.parseLong(string), "1");
							BlogServiceCardList bcList=this.blogServiceCardService.findByBlogsAndService(saveBlog,eservice);
							if(bcList!=null) {
								blogServiceCardList.add(bcList);
							}else{
								blogServiceCardList.add(new BlogServiceCardList(0, this.commonService.getUUID(), eservice, saveBlog, today,2));
							}							
						}
					}
				 
					if(!blogServiceCardList.isEmpty())
						blogServiceCardList=this.blogServiceCardService.saveAll(blogServiceCardList);
					
					List<BlogServiceCardList> allBlogServiceCard=this.blogServiceCardService.findByBlogs(saveBlog);
					allBlogServiceCard.removeAll(blogServiceCardList);
					
					if(!allBlogServiceCard.isEmpty())
					this.blogServiceCardService.removeAll(allBlogServiceCard); 
				  return "redirect:/admin/blogs/";
			  
		} catch (Exception e) {
			e.printStackTrace();
			model.addAttribute("products", this.productService.getAllProducts());
			model.addAttribute("services", this.serviceService.getAllServicesByStatus("1"));
			model.addAttribute("industries", this.industryService.getIndustryByDisplsyStatus("1"));
			model.addAttribute("category", this.categoryService.getAllSubCategory());
			model.addAttribute("blogs", blogs);
			session.setAttribute("message", new Message("Error : "+e.getMessage(), "alert-danger"));
			return "admin/blogs-edit";
		}
			
	}
	@PostMapping("/author")
	@ResponseBody
	public String getAuthor(@RequestParam("uuid") String uuid) {
		String author="";
		if(uuid!=null&&uuid.length()>0) {
			User user=this.userService.getUserByUuid(uuid);
			if(user!=null)
			author="<div class=\"author\">\r\n"
					+ "                            <div class=\"img-author\">\r\n"
					+ "                                <img src=\""+env.getProperty("azure_path")+user.getProfilePicture()+"\" alt=\""+user.getProfilePicture()+"\">\r\n"
					+ "                            </div>\r\n"
					+ "                            <div class=\"author-post mt-2\">\r\n"
					+ "                                <div class=\"author-name\">\r\n"
					+ "                                    <h6>"+user.getFirstName()+" "+user.getLastName()+"</h6>\r\n"
					+ "                                </div>\r\n"
					+ "                                <div class=\"author-bio\">"+user.getAboutMe()+"</div>\r\n"
					+ "                                <div class=\"post-date small text-muted\">\r\n"
					+ "                                    Published <span>"+this.commonService.getToday()+"</span>\r\n"
					+ "                                </div>\r\n"
					+ "                            </div>\r\n"
					+ "                        </div>";
		}
		return author;
	}	
}
