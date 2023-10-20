package com.corpseed.controller;

import java.io.PrintWriter;
import java.security.Principal;
import java.util.List;

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

import com.corpseed.entity.History;
import com.corpseed.entity.newsentity.News;
import com.corpseed.entity.newsentity.NewsCategory;
import com.corpseed.entity.SiteMapUrl;
import com.corpseed.entity.User;
import com.corpseed.util.Message;
import com.corpseed.serviceImpl.AzureBlobAdapter;
import com.corpseed.serviceImpl.CommonServices;
import com.corpseed.serviceImpl.HistoryService;
import com.corpseed.serviceImpl.SiteMapService;
import com.corpseed.serviceImpl.UserServices;
import com.corpseed.service.newsservice.NewsCategoryService;
import com.corpseed.service.newsservice.NewsService;

@Controller
@RequestMapping("/admin/news")
public class NewsController {

	@Autowired
	private NewsService newsService;
	@Autowired
	private NewsCategoryService newsCategoryService;
	@Autowired
	private CommonServices commonServices;
	@Autowired
	private BCryptPasswordEncoder passwordEncoder;
	@Autowired
	private HistoryService historyService;
	@Autowired
	private UserServices userServices;
	@Autowired
	private AzureBlobAdapter azureBlobAdapter;
	@Autowired
	private SiteMapService siteMapService;

	@GetMapping("/")
	public String newsHome(Model model) {
		model.addAttribute("title", "Corpseed Dashboard | Manage News");
		model.addAttribute("appendClass", "news");
		model.addAttribute("news", this.newsService.findAll());		
		return "admin/news";
	}
	
	@GetMapping("/category/")
	public String newsCategory(Model model) {
		model.addAttribute("title", "Corpseed Dashboard | Manage News Category");
		model.addAttribute("appendClass", "news");
		model.addAttribute("newsCategory", this.newsCategoryService.findAll());		
		return "admin/news-category";
	}
	
	@GetMapping("/category/add")
	public String addNewsCategory(Model model) {
		model.addAttribute("title", "Corpseed Dashboard | Add News Category");
		model.addAttribute("appendClass", "news");
		model.addAttribute("newsCategory", new NewsCategory());		
		return "admin/news-category-add";
	}

	@PostMapping("/category/save")
	public String saveNewsCategory(@Valid @ModelAttribute("newsCategory") NewsCategory newsCategory,
			BindingResult result,Model model,HttpSession session,Principal principal) {
		
		try {
			if(result.hasErrors()) {
				model.addAttribute("title", "Corpseed Dashboard | Add News Category");
				model.addAttribute("appendClass", "news");
				model.addAttribute("newsCategory", newsCategory);
				session.setAttribute("message", new Message("Please fill out the blank fields !!", "alert-danger"));
				return "admin/news-category-add";
			}
			
			NewsCategory findNewsCategory=this.newsCategoryService.findNewsCategoryByTitle(newsCategory.getTitle());
			if(findNewsCategory!=null) {
				model.addAttribute("title", "Corpseed Dashboard | Add News Category");
				model.addAttribute("appendClass", "news");
				model.addAttribute("newsCategory", newsCategory);
				session.setAttribute("message", new Message("News Category already existed !!", "alert-danger"));
				return "admin/news-category-add";
			}
			newsCategory.setUser(this.commonServices.getLoginedUser(principal));
			NewsCategory saveNewsCategory=this.newsCategoryService.saveNewsCategory(newsCategory);
			if(saveNewsCategory==null) {
				model.addAttribute("title", "Corpseed Dashboard | Add News Category");
				model.addAttribute("appendClass", "news");
				model.addAttribute("newsCategory", newsCategory);
				session.setAttribute("message", new Message("News Category not saved, Please try-again later !!", "alert-danger"));
				return "admin/news-category-add";
			}			
			return "redirect:/admin/news/category/";
			
		} catch (Exception e) {
			model.addAttribute("title", "Corpseed Dashboard | Add News Category");
			model.addAttribute("appendClass", "news");
			model.addAttribute("newsCategory", newsCategory);
			session.setAttribute("message", new Message("Error : "+e.getMessage(), "alert-danger"));
			return "admin/news-category-add";
		}
	}
	
	@GetMapping("/category/edit/{id}")
	public String editNewsCategory(Model model,@PathVariable("id") long id) {
		model.addAttribute("title", "Corpseed Dashboard | Edit News Category");
		model.addAttribute("appendClass", "news");
		NewsCategory newsCategory=this.newsCategoryService.findById(id);
		model.addAttribute("newsCategory", newsCategory);		
		return "admin/news-category-edit";
	}	
	
	@PostMapping("/category/update")
	public String updateNews(@Valid @ModelAttribute("newsCategory") NewsCategory newsCategory,
			BindingResult result,Model model,HttpSession session) {
		try {
			if(result.hasErrors()) {
				model.addAttribute("title", "Corpseed Dashboard | Edit News Category");
				model.addAttribute("appendClass", "news");
				model.addAttribute("newsCategory", newsCategory);
				session.setAttribute("message", new Message("Please fill out the blank fields !!", "alert-danger"));
				return "admin/news-category-edit";
			}
			
			NewsCategory findNewsCategory=this.newsCategoryService.findNewsCategoryByTitleAndIdNot(newsCategory.getTitle(),newsCategory.getId());
			if(findNewsCategory!=null) {
				model.addAttribute("title", "Corpseed Dashboard | Edit News Category");
				model.addAttribute("appendClass", "news");
				model.addAttribute("newsCategory", newsCategory);
				session.setAttribute("message", new Message("News Category already existed !!", "alert-danger"));
				return "admin/news-category-edit";
			}
			NewsCategory updateNewsCategory=this.newsCategoryService.updateNewsCategory(newsCategory);
			if(updateNewsCategory==null) {
				model.addAttribute("title", "Corpseed Dashboard | Edit News Category");
				model.addAttribute("appendClass", "news");
				model.addAttribute("newsCategory", newsCategory);
				session.setAttribute("message", new Message("News Category not updated..!!, Please try-again later !!", "alert-danger"));
				return "admin/news-category-edit";
			}
			
			return "redirect:/admin/news/category/";
			
		} catch (Exception e) {
			model.addAttribute("title", "Corpseed Dashboard | Edit News Category");
			model.addAttribute("appendClass", "news");
			model.addAttribute("newsCategory", newsCategory);
			session.setAttribute("message", new Message("Error : "+e.getMessage(), "alert-danger"));
			return "admin/news-category-edit";
		}
	}
	
	@GetMapping("/category/delete/{id}")
	@ResponseBody
	public void deleteNewsCategory(@PathVariable("id") long id,@RequestParam("password") String password,Principal principal,PrintWriter pw
			,HttpServletRequest req) {
		User user=this.commonServices.getLoginedUser(principal);
		if(user.getRole().equals("ROLE_ADMIN")) {
			if(passwordEncoder.matches(password, user.getPassword())) {
				NewsCategory newsCategory=this.newsCategoryService.findById(id);

					if(newsCategory!=null) {
						newsCategory.setDeleteStatus(1);
					//updating category
					this.newsCategoryService.saveNewsCategory(newsCategory);
					
					//adding category in history
					this.historyService.savehistory(new History(0, this.commonServices.getUUID(),
							"News Category", newsCategory.getId(), this.commonServices.getBrowser(req), 
							this.commonServices.getIpAddress(req), user.getId(), user.getFirstName()+" "+user.getLastName()
							, this.commonServices.getToday(), this.commonServices.getTime(),newsCategory.getTitle()));		
											
					//updating services
					List<News> news=newsCategory.getNews();					
					if(!news.isEmpty()) {
						news.stream().forEach(n->n.setDeleteStatus(1));
						this.newsService.saveAllNews(news);
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
	
	@GetMapping("/add")
	public String addNews(Model model) {
		model.addAttribute("title", "Corpseed Dashboard | Add News");
		model.addAttribute("appendClass", "news");
		model.addAttribute("users", this.userServices.findAllNotUserAndAccountStatus());
		model.addAttribute("newsCategory", this.newsCategoryService.findByDisplayStatusAndDeleteStatus("1",2));
		model.addAttribute("news", new News());
		return "admin/news-add";
	}
	
	@PostMapping("/save")
	public String saveNews(@Valid @ModelAttribute("news") News news,
			@RequestParam("categoryId") long categoryId,@RequestParam("file") MultipartFile file,
			BindingResult result,Model model,Principal principal,HttpSession session) {
		try {
			if(result.hasErrors()) {
				model.addAttribute("title", "Corpseed Dashboard | Add News");
				model.addAttribute("appendClass", "news");
				model.addAttribute("users", this.userServices.findAllNotUserAndAccountStatus());
				model.addAttribute("newsCategory", this.newsCategoryService.findByDisplayStatusAndDeleteStatus("1",2));
				model.addAttribute("news", news);
				session.setAttribute("message", new Message("Please field the form !!", "alert-danger"));
				return "admin/news-add";
			}
			News findNews=this.newsService.findBySlug(news.getSlug());
			if(findNews!=null) {
				model.addAttribute("title", "Corpseed Dashboard | Add News");
				model.addAttribute("appendClass", "news");
				model.addAttribute("users", this.userServices.findAllNotUserAndAccountStatus());
				model.addAttribute("newsCategory", this.newsCategoryService.findByDisplayStatusAndDeleteStatus("1",2));
				model.addAttribute("news", news);
				session.setAttribute("message", new Message("News already existed !!", "alert-danger"));
				return "admin/news-add";
			}
			NewsCategory newsCategory=this.newsCategoryService.findById(categoryId);
			news.setNewsCategory(newsCategory);
			
			if(!file.isEmpty()) {
				String fileName = this.azureBlobAdapter.upload(file, 0);
				news.setImage(fileName);
			}
			
			User author=this.userServices.findByIdAndDeleteStatus(news.getAuthorId(), 2);
			if(author!=null)news.setAuthorName(author.getFirstName()+' '+author.getLastName());
			news.setUser(author);
			
			String description=news.getDescription();
			description=description.replace("<table","<div class='table-width'><table");
			description=description.replace("</table>","</table></div>");
			
			while(description.contains("pdfview=")) {
            	description=description.replace("pdfview=", "<iframe src=\"https://docs.google.com/gview?url=");
            	description=description.replace("=pdfview", "&embedded=true\" frameborder=\"0\" webkitallowfullscreen mozallowfullscreen allowfullscreen style=\"width: 100%; height: 700px;\"></iframe>");
            }
			
			news.setDescription(description);
			
			News saveNews=this.newsService.saveNews(news);
			if(saveNews==null) {
				model.addAttribute("title", "Corpseed Dashboard | Add News");
				model.addAttribute("appendClass", "news");
				model.addAttribute("users", this.userServices.findAllNotUserAndAccountStatus());
				model.addAttribute("newsCategory", this.newsCategoryService.findByDisplayStatusAndDeleteStatus("1",2));
				model.addAttribute("news", news);
				session.setAttribute("message", new Message("News not saved, Please try-again later !!", "alert-danger"));
				return "admin/news-add";
			}
			
			//adding this url into sitemap
			this.siteMapService.saveUrl(new SiteMapUrl(0, this.commonServices.getUUID(), saveNews.getId(), "News", "/news/"+saveNews.getSlug(), 1, this.commonServices.getToday()));
					
			return "redirect:/admin/news/";
			
		} catch (Exception e) {
			model.addAttribute("title", "Corpseed Dashboard | Add News");
			model.addAttribute("appendClass", "news");
			model.addAttribute("users", this.userServices.findAllNotUserAndAccountStatus());
			model.addAttribute("newsCategory", this.newsCategoryService.findByDisplayStatusAndDeleteStatus("1",2));
			model.addAttribute("news", news);
			session.setAttribute("message", new Message("Error : "+e.getMessage(), "alert-danger"));
			return "admin/news-add";
		}		
		
	}
	
	@GetMapping("/edit/{id}")
	public String editNews(@PathVariable("id") long id,Model model) {
		model.addAttribute("title", "Corpseed Dashboard | Edit News");
		model.addAttribute("appendClass", "news");
		model.addAttribute("users", this.userServices.findAllNotUserAndAccountStatus());
		model.addAttribute("newsCategory", this.newsCategoryService.findByDisplayStatusAndDeleteStatus("1",2));
		News news=this.newsService.findById(id);
		model.addAttribute("news", news);
		if(news!=null)model.addAttribute("activeCategory", news.getNewsCategory().getId());
		return "admin/news-edit";
	}
	
	@PostMapping("/update")
	public String updateNews(@Valid @ModelAttribute("news") News news,
			@RequestParam("categoryId") long categoryId,@RequestParam("file") MultipartFile file,
			BindingResult result,Model model,HttpSession session) {
		try {
			if(result.hasErrors()) {
				model.addAttribute("title", "Corpseed Dashboard | Edit News");
				model.addAttribute("appendClass", "news");
				model.addAttribute("users", this.userServices.findAllNotUserAndAccountStatus());
				model.addAttribute("newsCategory", this.newsCategoryService.findByDisplayStatusAndDeleteStatus("1",2));
				model.addAttribute("news", news);
				session.setAttribute("message", new Message("Please field the form !!", "alert-danger"));
				return "admin/news-edit";
			}
			News findNews=this.newsService.findBySlugAndIdNot(news.getSlug(),news.getId());
			if(findNews!=null) {
				model.addAttribute("title", "Corpseed Dashboard | Edit News");
				model.addAttribute("appendClass", "news");
				model.addAttribute("users", this.userServices.findAllNotUserAndAccountStatus());
				model.addAttribute("newsCategory", this.newsCategoryService.findByDisplayStatusAndDeleteStatus("1",2));
				model.addAttribute("news", news);
				session.setAttribute("message", new Message("News already existed !!", "alert-danger"));
				return "admin/news-edit";
			}
			NewsCategory newsCategory=this.newsCategoryService.findById(categoryId);
			news.setNewsCategory(newsCategory);
			
			if(!file.isEmpty()) {
				if(news.getImage()!=null&&news.getImage().length()>0) {
					this.azureBlobAdapter.deleteFile(news.getImage());
				}
				String fileName = this.azureBlobAdapter.upload(file, 0);
				news.setImage(fileName);				
			}
			
			User author=this.userServices.findByIdAndDeleteStatus(news.getAuthorId(), 2);
			if(author!=null)news.setAuthorName(author.getFirstName()+' '+author.getLastName());
			news.setUser(author);
			
			String description=news.getDescription();
			
			description=description.replace("<div class=\"table-width\">","");
			description=description.replace("</table>\r\n"
					+ "</div>","</table>");
			
			description=description.replace("<table","<div class='table-width'><table");
			description=description.replace("</table>","</table></div>");
			
			while(description.contains("pdfview=")) {
            	description=description.replace("pdfview=", "<iframe src=\"https://docs.google.com/gview?url=");
            	description=description.replace("=pdfview", "&embedded=true\" frameborder=\"0\" webkitallowfullscreen mozallowfullscreen allowfullscreen style=\"width: 100%; height: 700px;\"></iframe>");
            }
			
			news.setDescription(description);
			
			News saveNews=this.newsService.saveNews(news);
			if(saveNews==null) {
				model.addAttribute("title", "Corpseed Dashboard | Edit News");
				model.addAttribute("appendClass", "news");
				model.addAttribute("users", this.userServices.findAllNotUserAndAccountStatus());
				model.addAttribute("newsCategory", this.newsCategoryService.findByDisplayStatusAndDeleteStatus("1",2));
				model.addAttribute("news", news);
				session.setAttribute("message", new Message("News not saved, Please try-again later !!", "alert-danger"));
				return "admin/news-edit";
			}
			
			//checking sitemap url existed or not if not then insert or update
			 SiteMapUrl siteMap=this.siteMapService.findByTypeAndUid("News", saveNews.getId());
			 if(siteMap==null) {
				 this.siteMapService.saveUrl(new SiteMapUrl(0, this.commonServices.getUUID(), saveNews.getId(), "News", "/news/"+saveNews.getSlug(), 1, this.commonServices.getToday()));
			 }else {
				 siteMap.setUrl("/news/"+saveNews.getSlug());
				 this.siteMapService.saveUrl(siteMap);
			 }			 
			return "redirect:/admin/news/";
			
		} catch (Exception e) {
			model.addAttribute("title", "Corpseed Dashboard | Edit News");
			model.addAttribute("appendClass", "news");
			model.addAttribute("users", this.userServices.findAllNotUserAndAccountStatus());
			model.addAttribute("newsCategory", this.newsCategoryService.findByDisplayStatusAndDeleteStatus("1",2));
			model.addAttribute("news", news);
			session.setAttribute("message", new Message("Error : "+e.getMessage(), "alert-danger"));
			return "admin/news-edit";
		}		
		
	}
	@GetMapping("/delete/{id}")
	public String deleteNews(@PathVariable("id") long id,Principal principal,HttpServletRequest req) {
		if(this.commonServices.getLoginedUser(principal).getRole().equals("ROLE_ADMIN")) {
			News news=this.newsService.findById(id);
			
			if(news!=null) {
				news.setDeleteStatus(1);
				this.newsService.saveNews(news);
				User user=this.commonServices.getLoginedUser(principal);
				//adding category in history
				this.historyService.savehistory(new History(0, this.commonServices.getUUID(),
						"News", news.getId(), this.commonServices.getBrowser(req), 
						this.commonServices.getIpAddress(req), user.getId(), user.getFirstName()+" "+user.getLastName()
						, this.commonServices.getToday(), this.commonServices.getTime(),news.getTitle()));		
			
			}			
			return "redirect:/admin/news/";
		}else {
			return "error/403";
		}
	}
}
