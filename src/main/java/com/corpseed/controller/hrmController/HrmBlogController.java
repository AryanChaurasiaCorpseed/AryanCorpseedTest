package com.corpseed.controller.hrmController;

import java.security.Principal;

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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.corpseed.entity.History;
import com.corpseed.entity.hrmentity.HrmBlog;
import com.corpseed.entity.User;
import com.corpseed.util.Message;
import com.corpseed.serviceImpl.AzureBlobAdapter;
import com.corpseed.serviceImpl.CommonServices;
import com.corpseed.serviceImpl.HistoryService;
import com.corpseed.serviceImpl.servicesserviceimpl.ServicesService;
import com.corpseed.serviceImpl.UserServices;
import com.corpseed.service.hrmservice.HrmBlogService;

@Controller
@RequestMapping("/hrm/blogs")
public class HrmBlogController {

	@Autowired
	private HrmBlogService hrmBlogService;
	
	@Autowired
	private UserServices userService;
	
	@Autowired
	private CommonServices commonService;
	
	@Autowired
	private AzureBlobAdapter azureBlobAdapter;
	
	@Autowired
	private HistoryService historyService;
	
	@Autowired
	private ServicesService serviceService;
	
	@GetMapping("/")
	public String blogs(Model model) {
		model.addAttribute("title", "Corpseed Dashboard | Manage Blogs");
		model.addAttribute("appendClass", "hrm"); 
		model.addAttribute("blogs", this.hrmBlogService.allBlogsByDeleteStatus(2));
		return "admin/hrm-blogs";
	}
	
	@GetMapping("/add")
	public String addBlog(Model model) {
		model.addAttribute("title", "Corpseed Dashboard | Add Blog");
		model.addAttribute("appendClass", "hrm"); 
		model.addAttribute("blogs",new HrmBlog());
		model.addAttribute("services", this.serviceService.getAllServicesByStatus("1"));
		model.addAttribute("users",this.userService.findAllNotUserAndAccountStatus());
		return "admin/hrm-add-blog";
	}

	@GetMapping("/edit/{uuid}")
	public String editBlog(@PathVariable("uuid") String uuid,Model model) {
		model.addAttribute("title", "Corpseed Dashboard | Edit Blog");
		model.addAttribute("appendClass", "hrm"); 
		HrmBlog findByUuid = this.hrmBlogService.findByUuid(uuid);
		String description=findByUuid.getDescription();
		while(description.contains("<iframe src=\"https://docs.google.com/gview?url=")||description.contains("<iframe src=\"http://docs.google.com/gview?url=")) {
			description = description.replace("<iframe src=\"http://docs.google.com/gview?url=", "pdfview=");
			description = description.replace("<iframe src=\"https://docs.google.com/gview?url=", "pdfview=");
			description = description.replace("&embedded=true\" frameborder=\"0\" webkitallowfullscreen mozallowfullscreen allowfullscreen style=\"width: 100%; height: 700px;\"></iframe>", "=pdfview");
			description=description.replace("&embedded=true\" frameborder=\"0\" webkitallowfullscreen mozallowfullscreen allowfullscreen style=\"width: 100%; height: 500px;\">","=pdfview");
		}	
		findByUuid.setDescription(description);
		model.addAttribute("blogs",findByUuid);
		model.addAttribute("services", this.serviceService.getAllServicesByStatus("1"));
		model.addAttribute("users",this.userService.findAllNotUserAndAccountStatus());		
		return "admin/hrm-edit-blog";
	}
	
	@PostMapping("/add")
	public String saveBlog(@Valid @ModelAttribute("blogs") HrmBlog blog,BindingResult result,
			@RequestParam("file") MultipartFile file,Model model,HttpSession session) {
		try {
			if(result.hasErrors()) {
				model.addAttribute("title", "Corpseed Dashboard | Add Blog");
				model.addAttribute("appendClass", "hrm"); 
				model.addAttribute("blogs",blog);
				session.setAttribute("message", new Message("Please fill out the blank fields !!", "alert-danger"));
				model.addAttribute("users",this.userService.findAllNotUserAndAccountStatus());
				return "admin/hrm-add-blog";
			}
			
			HrmBlog findBlog=this.hrmBlogService.findBySlug(blog.getSlug());
			if(findBlog!=null) {
				model.addAttribute("title", "Corpseed Dashboard | Add Blog");
				model.addAttribute("appendClass", "hrm"); 
				model.addAttribute("blogs",blog);
				session.setAttribute("message", new Message("Blog already exist !!", "alert-danger"));
				model.addAttribute("users",this.userService.findAllNotUserAndAccountStatus());
				return "admin/hrm-add-blog";
			}
			
			String summary=blog.getDescription();

			summary=summary.replace("<table","<div class='table-width'><table");
			summary=summary.replace("</table>","</table></div>");
			
			while(summary.contains("pdfview=")) {
				summary=summary.replace("pdfview=", "<iframe src=\"https://docs.google.com/gview?url=");
				summary=summary.replace("=pdfview", "&embedded=true\" frameborder=\"0\" webkitallowfullscreen mozallowfullscreen allowfullscreen style=\"width: 100%; height: 700px;\"></iframe>");
            }
			
			blog.setDescription(summary);
			
			if(!file.isEmpty()) {
				String fileName = this.azureBlobAdapter.upload(file, 0);
				blog.setImage(fileName);
			}
			blog.setUuid(this.commonService.getUUID());
			
			this.hrmBlogService.saveBlog(blog);
			
			return "redirect:/hrm/blogs/";
			
		} catch (Exception e) {
			model.addAttribute("title", "Corpseed Dashboard | Add Blog");
			model.addAttribute("appendClass", "hrm"); 
			model.addAttribute("blogs",blog);
			session.setAttribute("message", new Message("Error : "+e.getMessage(), "alert-danger"));
			model.addAttribute("users",this.userService.findAllNotUserAndAccountStatus());
			return "admin/hrm-add-blog";
		}
	}
	@PostMapping("/update")
	public String updateBlog(@Valid @ModelAttribute("blogs") HrmBlog blog,BindingResult result,
			@RequestParam("file") MultipartFile file,Model model,HttpSession session) {
		try {
			if(result.hasErrors()) {
				model.addAttribute("title", "Corpseed Dashboard | Edit Blog");
				model.addAttribute("appendClass", "hrm"); 
				model.addAttribute("blogs",blog);
				session.setAttribute("message", new Message("Please fill out the blank fields !!", "alert-danger"));
				model.addAttribute("users",this.userService.findAllNotUserAndAccountStatus());
				return "admin/hrm-edit-blog";
			}
			
			HrmBlog findBlog=this.hrmBlogService.findBySlugAndUuidNot(blog.getSlug(),blog.getUuid());
			if(findBlog!=null) {
				model.addAttribute("title", "Corpseed Dashboard | Edit Blog");
				model.addAttribute("appendClass", "hrm"); 
				model.addAttribute("blogs",blog);
				session.setAttribute("message", new Message("Blog already exist !!", "alert-danger"));
				model.addAttribute("users",this.userService.findAllNotUserAndAccountStatus());
				return "admin/hrm-edit-blog";
			}
			String summary=blog.getDescription();
			
			summary=summary.replace("<div class=\"table-width\">","");
			summary=summary.replace("</table>\r\n"
					+ "</div>","</table>");

			summary=summary.replace("<table","<div class='table-width'><table");
			summary=summary.replace("</table>","</table></div>");	
			
			while(summary.contains("pdfview=")) {
				summary=summary.replace("pdfview=", "<iframe src=\"https://docs.google.com/gview?url=");
				summary=summary.replace("=pdfview", "&embedded=true\" frameborder=\"0\" webkitallowfullscreen mozallowfullscreen allowfullscreen style=\"width: 100%; height: 700px;\"></iframe>");
            }
			
			blog.setDescription(summary);
			
			if(!file.isEmpty()) {
				String fileName = this.azureBlobAdapter.upload(file, 0);
				blog.setImage(fileName);
			}
			
			this.hrmBlogService.saveBlog(blog);
			
			return "redirect:/hrm/blogs/";
			
		} catch (Exception e) {
			model.addAttribute("title", "Corpseed Dashboard | Add Blog");
			model.addAttribute("appendClass", "hrm"); 
			model.addAttribute("blogs",blog);
			session.setAttribute("message", new Message("Error : "+e.getMessage(), "alert-danger"));
			model.addAttribute("users",this.userService.findAllNotUserAndAccountStatus());
			return "admin/hrm-edit-blog";
		}
	}
	@GetMapping("/delete/{uuid}")
	public String deleteBlogs(@PathVariable("uuid") String uuid,Principal principal,HttpServletRequest req) {	
		HrmBlog blogs = this.hrmBlogService.findByUuid(uuid);		
		if(blogs!=null&&this.commonService.getUserRole(principal).equals("ROLE_ADMIN")) {
			blogs.setDeleteStatus(1);
			this.hrmBlogService.saveBlog(blogs);
			
			User user=this.commonService.getLoginedUser(principal);
			
			//adding category in history
			this.historyService.savehistory(new History(0, this.commonService.getUUID(),
					"Hrm Blogs", blogs.getId(), this.commonService.getBrowser(req), 
					this.commonService.getIpAddress(req), user.getId(), user.getFirstName()+" "+user.getLastName()
					, this.commonService.getToday(), this.commonService.getTime(),blogs.getTitle()));		
		}else
			return "error/403";
		
		return "redirect:/hrm/blogs/";
	}
}
