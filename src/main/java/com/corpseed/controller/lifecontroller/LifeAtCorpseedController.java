package com.corpseed.controller.lifecontroller;

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

import com.corpseed.entity.lifeentity.AwardWinners;
import com.corpseed.entity.lifeentity.CultureGallery;
import com.corpseed.entity.EmployeeReview;
import com.corpseed.entity.lifeentity.Gallery;
import com.corpseed.entity.History;
import com.corpseed.entity.lifeentity.OurCulture;
import com.corpseed.entity.Position;
import com.corpseed.entity.lifeentity.Slideshow;
import com.corpseed.entity.User;
import com.corpseed.util.Message;
import com.corpseed.serviceImpl.AzureBlobAdapter;
import com.corpseed.serviceImpl.CommonServices;
import com.corpseed.serviceImpl.GalleryService;
import com.corpseed.serviceImpl.HistoryService;
import com.corpseed.serviceImpl.PositionService;
import com.corpseed.serviceImpl.SlideShowServices;
import com.corpseed.serviceImpl.UserServices;
import com.corpseed.service.AwardWinnersService;
import com.corpseed.service.CultureGalleryService;
import com.corpseed.service.EmployeeReviewService;
import com.corpseed.service.OurCultureService;

@Controller
@RequestMapping("/admin/life-at-corpseed")
public class LifeAtCorpseedController {
	
	@Autowired
	private CommonServices commonServices;
	
	@Autowired
    AzureBlobAdapter azureAdapter;
	
	@Autowired
	private SlideShowServices slideShowServices;
	
	@Autowired
	private GalleryService galleryService;
	
	@Autowired
	private PositionService positionService;
	
	@Autowired
	private UserServices userService;
	
	@Autowired
	private HistoryService historyService;
	
	@Autowired
	private AwardWinnersService awardWinners;
	
	@Autowired
	private OurCultureService ourCultureService;
	
	@Autowired
	private CultureGalleryService cultureGalleryService;
	
	@Autowired
	private EmployeeReviewService empReviewService;

	@GetMapping("/slideshow")
	public String slideShow(Model model) {
		model.addAttribute("title", "Corpseed Dashboard | Manage Slideshow");
		model.addAttribute("appendClass", "life");
		model.addAttribute("slideshow", this.slideShowServices.getAllSlideShow());
		return "admin/slideshow";
	}
	@GetMapping("/slideshow/add")
	public String addSlideShow(Model model) {
		model.addAttribute("title", "Corpseed Dashboard | Add Slideshow");
		model.addAttribute("appendClass", "life");
		model.addAttribute("slideshow", new Slideshow());
		return "admin/slideshow-add";
	}
	@GetMapping("/slideshow/{uuid}/edit")
	public String editSlideShow(@PathVariable("uuid") String uuid,Model model) {
		model.addAttribute("title", "Corpseed Dashboard | Edit Slideshow");
		model.addAttribute("appendClass", "life");
		Slideshow slideshow=this.slideShowServices.getSlideShowByUuid(uuid);
		if(slideshow!=null) {
			model.addAttribute("slideshow", slideshow);
			return "admin/slideshow-edit";	
		}else return "error/404";
		
	}
	@GetMapping("/slideshow/delete/{uuid}")
	public String deleteSlider(@PathVariable("uuid") String uuid,Principal principal,HttpServletRequest req) {
		if(this.commonServices.getLoginedUser(principal).getRole().equals("ROLE_ADMIN")) {
			Slideshow slideShow=this.slideShowServices.getSlideShowByUuid(uuid);
			if(slideShow!=null) {
				slideShow.setDeleteStatus(1);
				this.slideShowServices.saveSlide(slideShow);
				
				User user=this.commonServices.getLoginedUser(principal);
				
				//adding category in history
				this.historyService.savehistory(new History(0, this.commonServices.getUUID(),
						"SlideShow", slideShow.getId(), this.commonServices.getBrowser(req), 
						this.commonServices.getIpAddress(req), user.getId(), user.getFirstName()+" "+user.getLastName()
						, this.commonServices.getToday(), this.commonServices.getTime(),slideShow.getImageName()));	

			}
			return "redirect:/admin/life-at-corpseed/slideshow";
		}else {
			return "error/403";
		}
	}
	
	@GetMapping("/review/delete/{uuid}")
	public String deleteEmployeeReview(@PathVariable("uuid") String uuid,Principal principal,HttpServletRequest req) {
		if(this.commonServices.getLoginedUser(principal).getRole().equals("ROLE_ADMIN")) {
			EmployeeReview empReview=this.empReviewService.findByUuid(uuid);
			if(empReview!=null) {
				empReview.setDeleteStatus(1);
				this.empReviewService.save(empReview);
				
				User user=this.commonServices.getLoginedUser(principal);
				
				//adding category in history
				this.historyService.savehistory(new History(0, this.commonServices.getUUID(),
						"empReview", empReview.getId(), this.commonServices.getBrowser(req), 
						this.commonServices.getIpAddress(req), user.getId(), user.getFirstName()+" "+user.getLastName()
						, this.commonServices.getToday(), this.commonServices.getTime(),empReview.getUser().getFirstName()));	
								
			}			
			return "redirect:/admin/life-at-corpseed/review";
		}else {
			return "error/403";
		}
	}
	
	@PostMapping("/slideshow/update")
	public String updateSlideShow(@Valid @ModelAttribute("slideshow") Slideshow slideshow,BindingResult result,
			@RequestParam("image") MultipartFile file,HttpSession session,Model model,Principal principal) {
		model.addAttribute("title", "Corpseed Dashboard | Edit Slideshow");
		model.addAttribute("appendClass", "life");
		try {
			if(result.hasErrors()) {
				model.addAttribute("slideshow", slideshow);
				session.setAttribute("message", new Message("Please fill out the blank fields !!", "alert-danger"));
				return "admin/slideshow-edit";
			}
			
			if(!file.isEmpty()) {
				String name = azureAdapter.upload(file,this.commonServices.getUniqueCode());
				slideshow.setImageName(name);
			}			
			slideshow.setAddedByUUID(this.commonServices.getLoginedUser(principal).getUuid());
			Slideshow saveSlideShow=this.slideShowServices.saveSlide(slideshow);
			if(saveSlideShow==null) {
				model.addAttribute("slideshow", slideshow);
				session.setAttribute("message", new Message("Slide not saved, Please try-again later !! ", "alert-danger"));
				return "admin/slideshow-edit";
			}
			return "redirect:/admin/life-at-corpseed/slideshow/";				
			
		} catch (Exception e) {e.printStackTrace();
			model.addAttribute("slideshow", slideshow);
			session.setAttribute("message", new Message("Error : " + e.getMessage(), "alert-danger"));
			return "admin/slideshow-edit";
		}
	}
	
	@PostMapping("/slideshow/add")
	public String saveSlideShow(@Valid @ModelAttribute("slideshow") Slideshow slideshow,BindingResult result,
			@RequestParam("image") MultipartFile file,HttpSession session,Model model,Principal principal) {
		model.addAttribute("title", "Corpseed Dashboard | Add Slideshow");
		model.addAttribute("appendClass", "life");
		try {
			if(result.hasErrors()) {
				model.addAttribute("slideshow", slideshow);
				session.setAttribute("message", new Message("Please fill out the blank fields !!", "alert-danger"));
				return "admin/slideshow-add";
			}
			
			if(file.isEmpty()) {
				model.addAttribute("slideshow", slideshow);
				session.setAttribute("message", new Message("Please upload slide image !!", "alert-danger"));
				return "admin/slideshow-add";
			}else {
				String name = azureAdapter.upload(file,this.commonServices.getUniqueCode());
				slideshow.setImageName(name);
				slideshow.setUuid(this.commonServices.getUUID());
				slideshow.setAddedByUUID(this.commonServices.getLoginedUser(principal).getUuid());
				Slideshow saveSlideShow=this.slideShowServices.saveSlide(slideshow);
				if(saveSlideShow==null) {
					model.addAttribute("slideshow", slideshow);
					session.setAttribute("message", new Message("Slide not saved, Please try-again later !! ", "alert-danger"));
					return "admin/slideshow-add";
				}
				return "redirect:/admin/life-at-corpseed/slideshow/";
			}
		} catch (Exception e) {e.printStackTrace();
			model.addAttribute("slideshow", slideshow);
			session.setAttribute("message", new Message("Error : " + e.getMessage(), "alert-danger"));
			return "admin/slideshow-add";
		}
	}
	
	//GALLERY SECTION.....................
	@GetMapping("/gallery")
	public String gallery(Model model) {
		model.addAttribute("title", "Corpseed Dashboard | Manage Gallery");
		model.addAttribute("appendClass", "life");
		model.addAttribute("gallery", this.galleryService.getAllGallery());
		return "admin/gallery";
	}
	@GetMapping("/gallery/add")
	public String addGallery(Model model) {
		model.addAttribute("title", "Corpseed Dashboard | Add Gallery");
		model.addAttribute("appendClass", "life");
		model.addAttribute("gallery", new Gallery());
		return "admin/gallery-add";
	}
	@GetMapping("/gallery/{uuid}/edit")
	public String editGallery(@PathVariable("uuid") String uuid,Model model) {
		model.addAttribute("title", "Corpseed Dashboard | Edit Gallery");
		model.addAttribute("appendClass", "life");
		Gallery gallary=this.galleryService.getGalleryByUuid(uuid);
		if(gallary!=null) {
			model.addAttribute("gallery", gallary);
			return "admin/gallery-edit";
		}else {
			return "error/404";
		}
		
	}
	@PostMapping("/gallery/add")
	public String saveGallery(@Valid @ModelAttribute("gallery") Gallery gallery,BindingResult result,
			@RequestParam("file") MultipartFile file,HttpSession session,Model model,Principal principal) {
		model.addAttribute("title", "Corpseed Dashboard | Add Gallery");
		model.addAttribute("appendClass", "life");
		try {			
			if(result.hasErrors()) {
				model.addAttribute("gallery", gallery);
				session.setAttribute("message", new Message("Please fill out the blank fields !!", "alert-danger"));
				return "admin/gallery-add";
			}
			
			if(file.isEmpty()) {
				model.addAttribute("gallery", gallery);
				session.setAttribute("message", new Message("Please upload file !!", "alert-danger"));
				return "admin/gallery-add";
			}else {
				String name = azureAdapter.upload(file,this.commonServices.getUniqueCode());
				gallery.setFileName(name);
				gallery.setUuid(this.commonServices.getUUID());
				gallery.setAddedByUuid(this.commonServices.getLoginedUser(principal).getUuid());
				
				Gallery saveGallery=this.galleryService.saveGallery(gallery);
				if(saveGallery==null) {
					model.addAttribute("gallery", gallery);
					session.setAttribute("message", new Message("Data not saved, Please try-again later !! ", "alert-danger"));
					return "admin/gallery-add";
				}
				return "redirect:/admin/life-at-corpseed/gallery";				
			}
		} catch (Exception e) {e.printStackTrace();
			model.addAttribute("gallery", gallery);
			session.setAttribute("message", new Message("Error : " + e.getMessage(), "alert-danger"));
			return "admin/gallery-add";
		}
	}
	@PostMapping("/gallery/update")
	public String updateGallery(@Valid @ModelAttribute("gallery") Gallery gallery,BindingResult result,
			@RequestParam("file") MultipartFile file,HttpSession session,Model model,Principal principal) {
		model.addAttribute("title", "Corpseed Dashboard | Edit Gallery");
		model.addAttribute("appendClass", "life");
		try {			
			if(result.hasErrors()) {
				model.addAttribute("gallery", gallery);
				session.setAttribute("message", new Message("Please fill out the blank fields !!", "alert-danger"));
				return "admin/gallery-edit";
			}
			
			if(!file.isEmpty()) {
				String name = azureAdapter.upload(file,this.commonServices.getUniqueCode());
				gallery.setFileName(name);
			}
			gallery.setAddedByUuid(this.commonServices.getLoginedUser(principal).getUuid());
			
			Gallery saveGallery=this.galleryService.saveGallery(gallery);
			if(saveGallery==null) {
				model.addAttribute("gallery", gallery);
				session.setAttribute("message", new Message("Data not saved, Please try-again later !! ", "alert-danger"));
				return "admin/gallery-edit";
			}
			return "redirect:/admin/life-at-corpseed/gallery";				
			
		} catch (Exception e) {e.printStackTrace();
			model.addAttribute("gallery", gallery);
			session.setAttribute("message", new Message("Error : " + e.getMessage(), "alert-danger"));
			return "admin/gallery-edit";
		}
	}
	@GetMapping("/gallery/delete/{uuid}")
	public String deleteGallery(@PathVariable("uuid") String uuid,Principal principal,HttpServletRequest req) {
		if(this.commonServices.getLoginedUser(principal).getRole().equals("ROLE_ADMIN")) {
			Gallery findGallery=this.galleryService.getGalleryByUuid(uuid);
			if(findGallery!=null) {
				findGallery.setDeleteStatus(1);
				this.galleryService.saveGallery(findGallery);
				
				User user=this.commonServices.getLoginedUser(principal);
				
				//adding gallery in history
				this.historyService.savehistory(new History(0, this.commonServices.getUUID(),
						"Gallery", findGallery.getId(), this.commonServices.getBrowser(req), 
						this.commonServices.getIpAddress(req), user.getId(), user.getFirstName()+" "+user.getLastName()
						, this.commonServices.getToday(), this.commonServices.getTime(),findGallery.getFileName()));		

			return "redirect:/admin/life-at-corpseed/gallery";
			}else {
				return "error/404";
			}
		}else {
			return "error/403";
		}
	}
	//OUR CULTURE
	@GetMapping("/our-culture")
	public String culture(Model model) {
		model.addAttribute("title", "Corpseed Dashboard | Manage Our Culture");
		model.addAttribute("appendClass", "life");
		model.addAttribute("ourCulture", this.ourCultureService.allCulture());
		return "admin/our-culture";
	}
	
	@GetMapping("/our-culture/add")
	public String addCulture(Model model) {
		model.addAttribute("title", "Corpseed Dashboard | Add Our Culture");
		model.addAttribute("appendClass", "life");
		model.addAttribute("ourCulture", new OurCulture());
		return "admin/add-culture";
	}
	
	@GetMapping("/our-culture/edit/{uuid}")
	public String editCulture(@PathVariable("uuid") String uuid,Model model) {
		model.addAttribute("title", "Corpseed Dashboard | Edit Our Culture");
		model.addAttribute("appendClass", "life");
		model.addAttribute("ourCulture", this.ourCultureService.findByUuid(uuid));
		return "admin/edit-culture";
	}

	@GetMapping("/our-culture/delete/{uuid}")
	public String deleteCulture(@PathVariable("uuid") String uuid,Model model,Principal principal,HttpServletRequest req) {
		if(this.commonServices.getLoginedUser(principal).getRole().equals("ROLE_ADMIN")) {
			OurCulture ourCulture=this.ourCultureService.findByUuid(uuid);
			if(ourCulture!=null) {
				ourCulture.setDeleteStatus(1);
				this.ourCultureService.saveOurCulture(ourCulture);
				
				User user=this.commonServices.getLoginedUser(principal);
				
				//adding category in history
				this.historyService.savehistory(new History(0, this.commonServices.getUUID(),
						"ourCulture", ourCulture.getId(), this.commonServices.getBrowser(req), 
						this.commonServices.getIpAddress(req), user.getId(), user.getFirstName()+" "+user.getLastName()
						, this.commonServices.getToday(), this.commonServices.getTime(),ourCulture.getTitle()));		
								
			}
			
			return "redirect:/admin/life-at-corpseed/our-culture";
		}else {
			return "error/403";
		}
	}
	
	@PostMapping("/our-culture/update")
	public String updateOurCulture(@Valid @ModelAttribute("ourCulture") OurCulture ourCulture,BindingResult result,
			HttpSession session,Model model) {
		
		try {
			if(result.hasErrors()) {
				model.addAttribute("title", "Corpseed Dashboard | Edit Our Culture");
				model.addAttribute("appendClass", "life");
				model.addAttribute("ourCulture", ourCulture);
				session.setAttribute("message", new Message("Please fill out the blank fields !!", "alert-danger"));
				return "admin/edit-culture";
			}
			OurCulture findCulture=this.ourCultureService.findByTitleAndUuidNot(ourCulture.getTitle(),ourCulture.getUuid());
			if(findCulture!=null) {
				model.addAttribute("title", "Corpseed Dashboard | Edit Our Culture");
				model.addAttribute("appendClass", "life");
				model.addAttribute("ourCulture", ourCulture);
				if(findCulture.getDeleteStatus()==1)
					session.setAttribute("message", new Message("Our Culture exist in Trash !! ", "alert-danger"));
				else
					session.setAttribute("message", new Message("Our Culture already exist !! ", "alert-danger"));
				
				return "admin/edit-culture";
			}			
			this.ourCultureService.saveOurCulture(ourCulture);			
			return "redirect:/admin/life-at-corpseed/our-culture/";				
			
		} catch (Exception e) {e.printStackTrace();
			model.addAttribute("title", "Corpseed Dashboard | Edit Our Culture");
			model.addAttribute("appendClass", "life");
			model.addAttribute("ourCulture", ourCulture);
			session.setAttribute("message", new Message("Error : " + e.getMessage(), "alert-danger"));
			return "admin/edit-culture";
		}
	}
	
	@PostMapping("/our-culture/add")
	public String saveOurCulture(@Valid @ModelAttribute("ourCulture") OurCulture ourCulture,BindingResult result,
			HttpSession session,Model model) {
		
		try {
			if(result.hasErrors()) {
				model.addAttribute("title", "Corpseed Dashboard | Add Our Culture");
				model.addAttribute("appendClass", "life");
				model.addAttribute("ourCulture", ourCulture);
				session.setAttribute("message", new Message("Please fill out the blank fields !!", "alert-danger"));
				return "admin/add-culture";
			}
			OurCulture findCulture=this.ourCultureService.findByTitle(ourCulture.getTitle());
			if(findCulture!=null) {
				model.addAttribute("title", "Corpseed Dashboard | Add Our Culture");
				model.addAttribute("appendClass", "life");
				model.addAttribute("ourCulture", ourCulture);
				if(findCulture.getDeleteStatus()==1)
					session.setAttribute("message", new Message("Our Culture exist in Trash !! ", "alert-danger"));
				else
					session.setAttribute("message", new Message("Our Culture already exist !! ", "alert-danger"));
				
				return "admin/add-culture";
			}			
			ourCulture.setUuid(this.commonServices.getUUID());
			this.ourCultureService.saveOurCulture(ourCulture);			
			return "redirect:/admin/life-at-corpseed/our-culture/";				
			
		} catch (Exception e) {e.printStackTrace();
			model.addAttribute("title", "Corpseed Dashboard | Add Our Culture");
			model.addAttribute("appendClass", "life");
			model.addAttribute("ourCulture", ourCulture);
			session.setAttribute("message", new Message("Error : " + e.getMessage(), "alert-danger"));
			return "admin/add-culture";
		}
	}
	
	@GetMapping("/our-culture/gallery/{uuid}")
	public String cultureGallery(@PathVariable("uuid") String uuid,Model model) {
		model.addAttribute("title", "Corpseed Dashboard | Manage Our Culture Gallery");
		model.addAttribute("appendClass", "life");
		model.addAttribute("ourCultureUuid", uuid);	
		OurCulture findByUuid = this.ourCultureService.findByUuid(uuid);
		model.addAttribute("cultureGallery", this.cultureGalleryService.findCultureGalleryByCulture(findByUuid,2));
		return "admin/culture-gallery";
	}
	
	@GetMapping("/our-culture/gallery/{uuid}/add")
	public String addCultureGallery(@PathVariable("uuid") String uuid,Model model) {
		model.addAttribute("title", "Corpseed Dashboard | Add Our Culture Gallery");
		model.addAttribute("appendClass", "life");	
		model.addAttribute("ourCultureUuid", uuid);
		model.addAttribute("cultureGallery", new CultureGallery());
		return "admin/culture-gallery-add";
	}
	
	@GetMapping("/our-culture/gallery/{uuid}/edit")
	public String editCultureGallery(@PathVariable("uuid") String uuid,Model model) {
		model.addAttribute("title", "Corpseed Dashboard | Edit Our Culture Gallery");
		model.addAttribute("appendClass", "life");
		model.addAttribute("cultureGallery", this.cultureGalleryService.findByUuid(uuid));
		return "admin/culture-gallery-edit";
	}
	@GetMapping("/our-culture/gallery/delete/{uuid}")
	public String deleteOurCulture(@PathVariable("uuid") String uuid,Principal principal,HttpServletRequest req) {
		if(this.commonServices.getLoginedUser(principal).getRole().equals("ROLE_ADMIN")) {
			CultureGallery cultureGallery=this.cultureGalleryService.findByUuid(uuid);
			if(cultureGallery!=null) {
				cultureGallery.setDeleteStatus(1);
				this.cultureGalleryService.saveCultureGallery(cultureGallery);
				
				User user=this.commonServices.getLoginedUser(principal);
				
				//adding category in history
				this.historyService.savehistory(new History(0, this.commonServices.getUUID(),
						"culture", cultureGallery.getId(), this.commonServices.getBrowser(req), 
						this.commonServices.getIpAddress(req), user.getId(), user.getFirstName()+" "+user.getLastName()
						, this.commonServices.getToday(), this.commonServices.getTime(),cultureGallery.getName()));		
								
			}
			return "redirect:/admin/life-at-corpseed/our-culture/gallery/"+cultureGallery.getOurCulture().getUuid();
		}else {
			return "error/403";
		}
	}
	
	@PostMapping("/our-culture/gallery/{uuid}/add")
	public String saveOurCultureGallery(@Valid @ModelAttribute("cultureGallery") CultureGallery cultureGallery,BindingResult result,
			@PathVariable("uuid") String uuid,@RequestParam("file") MultipartFile file,HttpSession session,Model model) {
		
		try {
			if(result.hasErrors()) {
				model.addAttribute("title", "Corpseed Dashboard | Add Our Culture Gallery");
				model.addAttribute("appendClass", "life");
				model.addAttribute("cultureGallery", cultureGallery);
				session.setAttribute("message", new Message("Please fill out the blank fields !!", "alert-danger"));
				return "admin/culture-gallery-add";
			}
			if(file.isEmpty()) {
				model.addAttribute("title", "Corpseed Dashboard | Add Our Culture Gallery");
				model.addAttribute("appendClass", "life");
				model.addAttribute("cultureGallery", cultureGallery);
				session.setAttribute("message", new Message("Upload Image !!", "alert-danger"));
				return "admin/culture-gallery-add";
			}
			OurCulture ourCulture=this.ourCultureService.findByUuid(uuid);
			if(ourCulture==null) {
				model.addAttribute("title", "Corpseed Dashboard | Add Our Culture Gallery");
				model.addAttribute("appendClass", "life");
				model.addAttribute("cultureGallery", cultureGallery);
				session.setAttribute("message", new Message("Please Try-again later !!", "alert-danger"));
				return "admin/culture-gallery-add";
			}
			
			String name = this.azureAdapter.upload(file,0);
			cultureGallery.setUuid(this.commonServices.getUUID());
			cultureGallery.setName(name);
			cultureGallery.setOurCulture(ourCulture);
			
			this.cultureGalleryService.saveCultureGallery(cultureGallery);			
			return "redirect:/admin/life-at-corpseed/our-culture/gallery/"+uuid;				
			
		} catch (Exception e) {e.printStackTrace();
			model.addAttribute("title", "Corpseed Dashboard | Add Our Culture Gallery");
			model.addAttribute("appendClass", "life");
			model.addAttribute("cultureGallery", cultureGallery);
			session.setAttribute("message", new Message("Error : " + e.getMessage(), "alert-danger"));
			return "admin/culture-gallery-add";
		}
	}
	
	@PostMapping("/our-culture/gallery/{uuid}/update")
	public String updateOurCultureGallery(@Valid @ModelAttribute("cultureGallery") CultureGallery cultureGallery,BindingResult result,
			@PathVariable("uuid") String uuid,@RequestParam("file") MultipartFile file,HttpSession session,Model model) {
		
		try {
			if(result.hasErrors()) {
				model.addAttribute("title", "Corpseed Dashboard | Edit Our Culture Gallery");
				model.addAttribute("appendClass", "life");
				model.addAttribute("cultureGallery", cultureGallery);
				session.setAttribute("message", new Message("Please fill out the blank fields !!", "alert-danger"));
				return "admin/culture-gallery-edit";
			}			
			if(!file.isEmpty())	{		
				String name = this.azureAdapter.upload(file,0);
				cultureGallery.setName(name);
			}		
			
			this.cultureGalleryService.saveCultureGallery(cultureGallery);			
			return "redirect:/admin/life-at-corpseed/our-culture/gallery/"+cultureGallery.getOurCulture().getUuid();				
			
		} catch (Exception e) {e.printStackTrace();
			model.addAttribute("title", "Corpseed Dashboard | Edit Our Culture Gallery");
			model.addAttribute("appendClass", "life");
			model.addAttribute("cultureGallery", cultureGallery);
			session.setAttribute("message", new Message("Error : " + e.getMessage(), "alert-danger"));
			return "admin/culture-gallery-edit";
		}
	}
	//employee review
	@GetMapping("/review")
	public String review(Model model) {
		model.addAttribute("title", "Corpseed Dashboard | Manage Employee Review");
		model.addAttribute("appendClass", "life");
		model.addAttribute("reviews", this.empReviewService.allReview());
		return "admin/employee-reviews";
	}	
	@GetMapping("/review/add")
	public String addReview(Model model) {
		model.addAttribute("title", "Corpseed Dashboard | Add Employee Review");
		model.addAttribute("appendClass", "life");
		model.addAttribute("users", this.userService.findAllNotUserAndAccountStatus());
		model.addAttribute("review",new EmployeeReview());
		return "admin/emp-review-add";
	}
	
	@GetMapping("/review/edit/{uuid}")
	public String editReview(Model model,@PathVariable("uuid") String uuid) {
		model.addAttribute("title", "Corpseed Dashboard | Edit Employee Review");
		model.addAttribute("appendClass", "life");
		model.addAttribute("users", this.userService.findAllNotUserAndAccountStatus());
		model.addAttribute("review",this.empReviewService.findByUuid(uuid));
		return "admin/emp-review-edit";
	}
	
	@PostMapping("/review/add")
	public String saveReview(@Valid @ModelAttribute("review") EmployeeReview review,
			BindingResult result,Model model,HttpSession session,
			@RequestParam("file") MultipartFile file) {
		try {			
			if(result.hasErrors()) {
				model.addAttribute("title", "Corpseed Dashboard | Add Employee Review");
				model.addAttribute("appendClass", "life");
				model.addAttribute("review", review);
				model.addAttribute("users", this.userService.findAllNotUserAndAccountStatus());
				session.setAttribute("message", new Message("Please fill out the blank fields !!", "alert-danger"));
				return "admin/emp-review-add";
			}
			if(review.getUser()==null) {
				model.addAttribute("title", "Corpseed Dashboard | Add Employee Review");
				model.addAttribute("appendClass", "life");
				model.addAttribute("review", review);
				model.addAttribute("users", this.userService.findAllNotUserAndAccountStatus());
				session.setAttribute("message", new Message("Please select user !!", "alert-danger"));
				return "admin/emp-review-add";
			}
			
			EmployeeReview findReview=this.empReviewService.findByUser(review.getUser());
			if(findReview!=null) {
				model.addAttribute("title", "Corpseed Dashboard | Add Employee Review");
				model.addAttribute("appendClass", "life");
				model.addAttribute("review", review);
				model.addAttribute("users", this.userService.findAllNotUserAndAccountStatus());
				session.setAttribute("message", new Message("Review already added !!", "alert-danger"));
				return "admin/emp-review-add";
			}
			if(!file.isEmpty()) {
				String fileName = this.azureAdapter.upload(file, this.commonServices.getUniqueCode());
				review.setReviewImage(fileName);
			}
			
			review.setUuid(this.commonServices.getUUID());
			this.empReviewService.save(review);
			
			return "redirect:/admin/life-at-corpseed/review";
			
		} catch (Exception e) {
			model.addAttribute("title", "Corpseed Dashboard | Add Employee Review");
			model.addAttribute("appendClass", "life");
			model.addAttribute("review", review);
			model.addAttribute("users", this.userService.findAllNotUserAndAccountStatus());
			session.setAttribute("message", new Message("Error : "+e.getMessage(), "alert-danger"));
			return "admin/emp-review-add";
		}
	}
	@PostMapping("/review/update")
	public String updateReview(@Valid @ModelAttribute("review") EmployeeReview review,
			BindingResult result,Model model,HttpSession session,
			@RequestParam("file") MultipartFile file) {
		try {			
			if(result.hasErrors()) {
				model.addAttribute("title", "Corpseed Dashboard | Edit Employee Review");
				model.addAttribute("appendClass", "life");
				model.addAttribute("review", review);
				model.addAttribute("users", this.userService.findAllNotUserAndAccountStatus());
				session.setAttribute("message", new Message("Please fill out the blank fields !!", "alert-danger"));
				return "admin/emp-review-edit";
			}
			EmployeeReview findReview=this.empReviewService.findByUserAndUuidNot(review.getUser(),review.getUuid());
			if(findReview!=null) {
				model.addAttribute("title", "Corpseed Dashboard | Edit Employee Review");
				model.addAttribute("appendClass", "life");
				model.addAttribute("review", review);
				model.addAttribute("users", this.userService.findAllNotUserAndAccountStatus());
				session.setAttribute("message", new Message("Review already added !!", "alert-danger"));
				return "admin/emp-review-edit";
			}
			
			if(review.getUser()==null) {
				model.addAttribute("title", "Corpseed Dashboard | Add Employee Review");
				model.addAttribute("appendClass", "life");
				model.addAttribute("review", review);
				model.addAttribute("users", this.userService.findAllNotUserAndAccountStatus());
				session.setAttribute("message", new Message("Please select user !!", "alert-danger"));
				return "admin/emp-review-edit";
			}
			
			if(!file.isEmpty()) {
				if(review.getReviewImage()!=null&&review.getReviewImage().length()>0)
					this.azureAdapter.deleteFile(review.getReviewImage());
				
				String fileName = this.azureAdapter.upload(file, this.commonServices.getUniqueCode());
				review.setReviewImage(fileName);
			}
			
			review.setUuid(this.commonServices.getUUID());
			this.empReviewService.save(review);
			
			return "redirect:/admin/life-at-corpseed/review";
			
		} catch (Exception e) {
			model.addAttribute("title", "Corpseed Dashboard | Edit Employee Review");
			model.addAttribute("appendClass", "life");
			model.addAttribute("review", review);
			model.addAttribute("users", this.userService.findAllNotUserAndAccountStatus());
			session.setAttribute("message", new Message("Error : "+e.getMessage(), "alert-danger"));
			return "admin/emp-review-edit";
		}
	}
	//award winners.................
	@GetMapping("/award-winners")
	public String award(Model model) {
		model.addAttribute("title", "Corpseed Dashboard | Manage Award Winners");
		model.addAttribute("appendClass", "life");
		model.addAttribute("awardWinners", this.awardWinners.allAwardWinners());
		return "admin/award-winners";
	}
	
	@GetMapping("/award-winners/add")
	public String addAward(Model model) {
		model.addAttribute("title", "Corpseed Dashboard | Add Award Winner");
		model.addAttribute("appendClass", "life");
		model.addAttribute("users", this.userService.findAllNotUserAndAccountStatus());
		model.addAttribute("awardWinner",new AwardWinners());
		return "admin/award-add";
	}
	
	@GetMapping("/award-winners/edit/{uuid}")
	public String editAward(@PathVariable("uuid") String uuid,Model model) {
		model.addAttribute("title", "Corpseed Dashboard | Edit Award Winner");
		model.addAttribute("appendClass", "life");
		model.addAttribute("users", this.userService.findAllNotUserAndAccountStatus());
		model.addAttribute("awardWinner",this.awardWinners.findByUuid(uuid));
		return "admin/award-edit";
	}
	
	@GetMapping("/award-winners/delete/{uuid}")
	public String deleteWinner(@PathVariable("uuid") String uuid,Model model,Principal principal,HttpServletRequest req) {
		if(this.commonServices.getLoginedUser(principal).getRole().equals("ROLE_ADMIN")) {
			AwardWinners awardWinner=this.awardWinners.findByUuid(uuid);
			if(awardWinner!=null) {
				awardWinner.setDeleteStatus(1);
				this.awardWinners.saveAward(awardWinner);
				
				User user=this.commonServices.getLoginedUser(principal);
				
				//adding category in history
				this.historyService.savehistory(new History(0, this.commonServices.getUUID(),
						"award", awardWinner.getId(), this.commonServices.getBrowser(req), 
						this.commonServices.getIpAddress(req), user.getId(), user.getFirstName()+" "+user.getLastName()
						, this.commonServices.getToday(), this.commonServices.getTime(),awardWinner.getImageName()));		
								
			}			
			return "redirect:/admin/life-at-corpseed/award-winners";
		}else {
			return "error/403";
		}
	}
	
	@PostMapping("/award-winners/update")
	public String updateAward(@Valid @ModelAttribute("awardWinner") AwardWinners awardWinner,BindingResult result,
			HttpSession session,Model model,@RequestParam("file") MultipartFile file) {
		
		try {
			if(result.hasErrors()) {
				model.addAttribute("title", "Corpseed Dashboard | Edit Award Winner");
				model.addAttribute("appendClass", "life");
				model.addAttribute("awardWinner", awardWinner);
				model.addAttribute("users", this.userService.findAllNotUserAndAccountStatus());
				session.setAttribute("message", new Message("Please fill out the blank fields !!", "alert-danger"));
				return "admin/award-edit";
			}
			if(!file.isEmpty()) {
				if(awardWinner.getImageName()!=null&&awardWinner.getImageName().length()>0)
					this.azureAdapter.deleteFile(awardWinner.getImageName());
				
				String fileName = this.azureAdapter.upload(file, this.commonServices.getUniqueCode());
				awardWinner.setImageName(fileName);
			}
						
			this.awardWinners.saveAward(awardWinner);			
			return "redirect:/admin/life-at-corpseed/award-winners/";				
			
		} catch (Exception e) {e.printStackTrace();
			model.addAttribute("title", "Corpseed Dashboard | Edit Award Winner");
			model.addAttribute("appendClass", "life");
			model.addAttribute("awardWinner", awardWinner);
			model.addAttribute("users", this.userService.findAllNotUserAndAccountStatus());
			session.setAttribute("message", new Message("Error : " + e.getMessage(), "alert-danger"));
			return "admin/award-edit";
		}
	}
	
	@PostMapping("/award-winners/add")
	public String saveAward(@Valid @ModelAttribute("awardWinner") AwardWinners awardWinner,BindingResult result,
			HttpSession session,Model model,Principal principal,@RequestParam("file") MultipartFile file) {
		model.addAttribute("title", "Corpseed Dashboard | Add Award Winner");
		model.addAttribute("appendClass", "life");
		try {
			if(result.hasErrors()) {
				model.addAttribute("awardWinner", awardWinner);
				model.addAttribute("users", this.userService.findAllNotUserAndAccountStatus());
				session.setAttribute("message", new Message("Please fill out the blank fields !!", "alert-danger"));
				return "admin/award-add";
			}
			if(file.isEmpty()) {
				model.addAttribute("awardWinner", awardWinner);
				model.addAttribute("users", this.userService.findAllNotUserAndAccountStatus());
				session.setAttribute("message", new Message("Please upload image !!", "alert-danger"));
				return "admin/award-add";
			}
		
			String fileName = this.azureAdapter.upload(file, this.commonServices.getUniqueCode());
			awardWinner.setImageName(fileName);
			awardWinner.setUuid(this.commonServices.getUUID());
			this.awardWinners.saveAward(awardWinner);			
			return "redirect:/admin/life-at-corpseed/award-winners/";				
			
		} catch (Exception e) {
			e.printStackTrace();
			model.addAttribute("awardWinner", awardWinner);
			model.addAttribute("users", this.userService.findAllNotUserAndAccountStatus());
			session.setAttribute("message", new Message("Error : " + e.getMessage(), "alert-danger"));
			return "admin/award-add";
		}
	}
	
	//position.................
	@GetMapping("/position")
	public String position(Model model) {
		model.addAttribute("title", "Corpseed Dashboard | Manage positions");
		model.addAttribute("appendClass", "life");
		model.addAttribute("positions", this.positionService.getAllPositions());
		return "admin/position";
	}
	@GetMapping("/position/add")
	public String addPosition	(Model model) {
		model.addAttribute("title", "Corpseed Dashboard | Add Position");
		model.addAttribute("appendClass", "life");
		model.addAttribute("users", this.userService.findAllNotUserAndAccountStatus());
		model.addAttribute("position",new Position());
		return "admin/position-add";
	}
	@PostMapping("/position/add")
	public String savePosition(@Valid @ModelAttribute("position") Position position,BindingResult result,
			HttpSession session,Model model,Principal principal) {
		model.addAttribute("title", "Corpseed Dashboard | Add Position");
		model.addAttribute("appendClass", "life");
		try {
			if(result.hasErrors()) {
				model.addAttribute("position", position);
				model.addAttribute("users", this.userService.findAllNotUserAndAccountStatus());
				session.setAttribute("message", new Message("Please fill out the blank fields !!", "alert-danger"));
				return "admin/position-add";
			}
			if(position.getUser()==null) {
				model.addAttribute("position", position);
				model.addAttribute("users", this.userService.findAllNotUserAndAccountStatus());
				session.setAttribute("message", new Message("Please select user !!", "alert-danger"));
				return "admin/position-add";
			}
			Position existPosition=this.positionService.findByUser(position.getUser());
			if(existPosition!=null) {
				model.addAttribute("position", position);
				model.addAttribute("users", this.userService.findAllNotUserAndAccountStatus());
				if(existPosition.getDeleteStatus()==1)
					session.setAttribute("message", new Message("Position exist in Trash !! ", "alert-danger"));
				else
					session.setAttribute("message", new Message("Position already exist !! ", "alert-danger"));
				
				return "admin/position-add";
			}
			long posCount=this.positionService.countAllPositions();
			if(posCount<6) {
				position.setUuid(this.commonServices.getUUID());
				position.setAddedByUuid(this.commonServices.getLoginedUser(principal).getUuid());
				Position savePosition=this.positionService.savePosition(position);
				if(savePosition==null) {
					model.addAttribute("position", position);
					model.addAttribute("users", this.userService.findAllNotUserAndAccountStatus());
					session.setAttribute("message", new Message("Data not saved, Please try-again later !! ", "alert-danger"));
					return "admin/position-add";
				}else {
					return "redirect:/admin/life-at-corpseed/position";
				}
			}else {
				model.addAttribute("position", position);
				model.addAttribute("users", this.userService.findAllNotUserAndAccountStatus());
				session.setAttribute("message", new Message("You can't add more than 6 positions !! ", "alert-danger"));
				return "admin/position-add";
			}
		} catch (Exception e) {e.printStackTrace();
			model.addAttribute("position", position);
			model.addAttribute("users", this.userService.findAllNotUserAndAccountStatus());
			session.setAttribute("message", new Message("Error : " + e.getMessage(), "alert-danger"));
			return "admin/position-add";
		}
	}
	@GetMapping("/position/delete/{uuid}")
	public String deletePosition(@PathVariable("uuid") String uuid,Principal principal,HttpServletRequest req) {
		if(this.commonServices.getLoginedUser(principal).getRole().equals("ROLE_ADMIN")) {
			Position position=this.positionService.findByUuid(uuid);
			if(position!=null) {
				position.setDeleteStatus(1);
				this.positionService.savePosition(position);
				
				User user=this.commonServices.getLoginedUser(principal);
				
				//adding category in history
				this.historyService.savehistory(new History(0, this.commonServices.getUUID(),
						"Position", position.getId(), this.commonServices.getBrowser(req), 
						this.commonServices.getIpAddress(req), user.getId(), user.getFirstName()+" "+user.getLastName()
						, this.commonServices.getToday(), this.commonServices.getTime(),position.getUser().getFirstName()));		

			}
			
			return "redirect:/admin/life-at-corpseed/position";
		}else {
			return "error/403";
		}
	}
	@GetMapping("/position/edit/{uuid}")
	public String editPosition(@PathVariable("uuid") String uuid,Model model) {
		model.addAttribute("title", "Corpseed Dashboard | Update Position");
		model.addAttribute("appendClass", "life");
		model.addAttribute("users", this.userService.findAllNotUserAndAccountStatus());
		model.addAttribute("position",this.positionService.findByUuid(uuid));
		return "admin/position-edit";
	}
	@PostMapping("/position/update")
	public String updatePosition(@Valid @ModelAttribute("position") Position position,BindingResult result,
			HttpSession session,Model model,Principal principal) {
		model.addAttribute("title", "Corpseed Dashboard | Update Position");
		model.addAttribute("appendClass", "life");
		try {	
			
			if(result.hasErrors()) {
				model.addAttribute("position", position);
				model.addAttribute("users", this.userService.findAllNotUserAndAccountStatus());
				session.setAttribute("message", new Message("Please fill out the blank fields !!", "alert-danger"));
				return "admin/position-edit";
			}
			if(position.getUser()==null) {
				model.addAttribute("position", position);
				model.addAttribute("users", this.userService.findAllNotUserAndAccountStatus());
				session.setAttribute("message", new Message("Please select user !!", "alert-danger"));
				return "admin/position-edit";
			}
			Position existPosition=this.positionService.findByUserAndUuidNot(position.getUser(),position.getUuid());
			if(existPosition!=null) {
				model.addAttribute("position", position);
				model.addAttribute("users", this.userService.findAllNotUserAndAccountStatus());
				if(existPosition.getDeleteStatus()==1)
					session.setAttribute("message", new Message("Position exist in Trash !! ", "alert-danger"));
				else
					session.setAttribute("message", new Message("Position already exist !! ", "alert-danger"));
				
				return "admin/position-edit";
			}
				Position findPosition=this.positionService.findByUuid(position.getUuid());
				if(findPosition==null) {
					return "error/404";
				}
				position.setId(findPosition.getId());
				position.setAddedByUuid(this.commonServices.getLoginedUser(principal).getUuid());
				Position savePosition=this.positionService.savePosition(position);
				if(savePosition==null) {
					model.addAttribute("position", position);
					model.addAttribute("users", this.userService.findAllNotUserAndAccountStatus());
					session.setAttribute("message", new Message("Data Not saved, Please try-again later !! ", "alert-danger"));
					return "admin/position-edit";
				}else {
					return "redirect:/admin/life-at-corpseed/position";
				}
		} catch (Exception e) {e.printStackTrace();
			model.addAttribute("position", position);
			model.addAttribute("users", this.userService.findAllNotUserAndAccountStatus());
			session.setAttribute("message", new Message("Error : " + e.getMessage(), "alert-danger"));
			return "admin/position-edit";
		}
	}
}
