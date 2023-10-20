package com.corpseed.serviceImpl;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.corpseed.entity.*;
import com.corpseed.entity.contactentity.Contact;
import com.corpseed.entity.contactentity.ContactAddress;
import com.corpseed.entity.couponentity.Coupon;
import com.corpseed.entity.couponentity.CouponServices;
import com.corpseed.entity.documententity.Documents;
import com.corpseed.entity.enquiryentity.Enquiry;
import com.corpseed.entity.footerentity.FooterCategory;
import com.corpseed.entity.footerentity.FooterService;
import com.corpseed.entity.blogentity.BlogFaq;
import com.corpseed.entity.blogentity.BlogServiceCardList;
import com.corpseed.entity.blogentity.Blogs;
import com.corpseed.entity.hrmentity.*;
import com.corpseed.entity.industryentity.Industry;
import com.corpseed.entity.industryentity.IndustryDetails;
import com.corpseed.entity.lifeentity.*;
import com.corpseed.entity.newsentity.News;
import com.corpseed.entity.newsentity.NewsCategory;
import com.corpseed.entity.orderentity.Orders;
import com.corpseed.entity.pressentity.Press;
import com.corpseed.entity.pressentity.PressCategory;
import com.corpseed.entity.serviceentity.*;
import com.corpseed.service.*;
import com.corpseed.service.hrmservice.HrmBlogService;
import com.corpseed.service.newsservice.NewsCategoryService;
import com.corpseed.service.newsservice.NewsService;
import com.corpseed.service.pressservice.PressCategoryService;
import com.corpseed.service.pressservice.PressService;
import com.corpseed.service.servicesservice.ServiceBrand;
import com.corpseed.service.servicesservice.ServiceContactService;
import com.corpseed.service.servicesservice.SubServiceService;
import com.corpseed.serviceImpl.blogserviceimpl.BlogService;
import com.corpseed.serviceImpl.blogserviceimpl.BlogServiceCardService;
import com.corpseed.serviceImpl.couponserviceimpl.CouponService;
import com.corpseed.serviceImpl.couponserviceimpl.CouponServicesService;
import com.corpseed.serviceImpl.enqserviceimpl.EnquiryService;
import com.corpseed.serviceImpl.hrmserviceimpl.*;
import com.corpseed.serviceImpl.orderserviceimpl.OrdersService;
import com.corpseed.serviceImpl.servicesserviceimpl.ServiceCardListService;
import com.corpseed.serviceImpl.servicesserviceimpl.ServicesBlogsService;
import com.corpseed.serviceImpl.servicesserviceimpl.ServicesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.corpseed.repository.HistoryRepository;

@Service
public class HistoryService{

	@Autowired
	private HistoryRepository historyRepository; 
	
	@Autowired
	private CategoryService categoryService; 

	@Autowired
	private SiteMapService siteMapService;
	
	@Autowired
	private BlogService blogService;
	
	@Autowired
	private LawUpdateService lawUpdateService;
	
	@Autowired
	private ServicesService servicesService;
	
	@Autowired
	private OrdersService orderService;
	
	@Autowired
	private HotTagService hotTagService;
	
	@Autowired
	private com.corpseed.serviceImpl.FooterService footerService;
	
	@Autowired
	private CouponServicesService couponServicesService;
	
	@Autowired
	private ServicesBlogsService serviceBlogService;
	
	@Autowired
	private ServiceCardListService serviceCardListService;
	
	@Autowired
	private IndustryService industryService;
	
	@Autowired
	private ServicesBlogsService servicesBlogService;
	
	@Autowired
	private BlogServiceCardService blogServiceCardService;
	
	@Autowired
	private MeetingService meetingService;
	
	@Autowired
	private CouponService couponService;
	
	@Autowired
	private DocumentService documentService;
	
	@Autowired
	private JobServices jobService;
	
	@Autowired
	private JobApplicationServices jobAppService;
	
	@Autowired
	private CandidateDocumentService candDocService;
	
	@Autowired
	private InterviewScheduleService interviewService;
	
	@Autowired
	private TrackAppService trackAppService;
	
	@Autowired
	private JobServices jobServices;
	
	@Autowired
	private MasterService masterService;
	
	@Autowired
	private EnquiryService enquiryService;
	
	@Autowired
	private SlideShowServices slideShowService;
	
	@Autowired
	private GalleryService galleryService;
	
	@Autowired
	private PositionService positionService;
	
	@Autowired
	private SubscriberService subscriberService;
	
	@Autowired
	private TriggerService triggerService;
	
	@Autowired
	private UserServices userService;
	
	@Autowired
	private HrPermissionService hrPermissionService;
	
	@Autowired
	private ForgetPasswordService forgetPasswordService;
	
	@Autowired
	private AzureBlobAdapter azureAdapter;
	
	@Autowired
	private ServiceBrand serviceBrand;
	
	@Autowired
	private HrmBlogService hrmBlogService;
	
	@Autowired
	private EmployeeReviewService empReviewService;
	
	@Autowired
	private AwardWinnersService awardWinnerService;
	
	@Autowired
	private OurCultureService ourCultureService;
	
	@Autowired
	private CultureGalleryService cultureGalleryService;
	
	@Autowired
	private BlogFaqService blogFaqService;
	
	@Autowired
	private NewsCategoryService newsCategoryService;
	
	@Autowired
	private NewsService newsService;

	@Autowired
	private PressCategoryService pressCategoryService;

	@Autowired
	private PressService pressService;

	@Autowired
	private SubServiceService subServiceService;

	@Autowired
	private ServiceContactService serviceContactService;
		 
	public void savehistory(History history) {
		this.historyRepository.save(history);
	}

	public List<History> findAll() {
		// TODO Auto-generated method stub
		return this.historyRepository.findByOrderByIdDesc();
	}

	public History findByUuid(String uuid) {
		// TODO Auto-generated method stub
		return this.historyRepository.findByUuid(uuid);
	}
	public void deleteData(History history) {
		switch (history.getType()) {
		case "Category":
			deleteCategory(history);
			break;
			
		case "News Category":
			deleteNewsCategory(history);
			break;
			
		case "News":
			deleteNews(history);
			break;

		case "PressCategory":
			deletePressCategory(history);
			break;

		case "Press":
			deletePress(history);
			break;
			
		case "Service":
			deleteService(history);
			break;

		case "SubService":
			deleteSubService(history);
			break;

		case "ServiceContact":
			deleteServiceContact(history);
			break;
			
		case "Service FAQ":
			deleteServiceFAQ(history);
			break;
			
		case "Service Details":
			deleteServiceDetails(history);
			break;
			
		case "Service Package":
			deleteServicePackage(history);
			break;
			
		case "Service Brand":
			deleteServiceBrand(history);
			break;
			
		case "Industry":
			deleteIndustry(history);
			break;
			
		case "Industry Details":
			deleteIndustryDetails(history);
			break;
			
		case "Blogs":
			deleteBlogs(history);
			break;
			
		case "Blog FAQ":
			deleteBlogsFaq(history);
			break;
			
		case "Hrm Blogs":
			deleteHrmBlogs(history);
			break;
			
		case "Meeting":
			deleteMeeting(history);
			break;
			
		case "Coupon":
			deleteCoupon(history);
			break;
			
		case "Document":
			deleteDocument(history);
			break;
			
		case "Job":
			deleteJob(history);
			break;
			
		case "Job Application":
			deleteJobApplication(history);
			break;
			
		case "CMS Page":
			deleteCMSPage(history);
			break;
			
		case "Footer":
			deleteFooter(history);
			break;
			
		case "Enquiry":
			deleteEnquiry(history);
			break;
			
		case "Address":
			deleteAddress(history);
			break;
			
		case "Country":
			deleteCountry(history);
			break;
			
		case "State":
			deleteState(history);
			break;
			
		case "City":
			deleteCity(history);
			break;
			
		case "Contact":
			deleteContact(history);
			break;
			
		case "Review":
			deleteReview(history);
			break;
			
		case "Hot Tag":
			deleteHotTag(history);
			break;
			
		case "Law Update":
			deleteLawUpdate(history);
			break;
			
		case "SlideShow":
			deleteSlideShow(history);
			break;
			
		case "Gallery":
			deleteGallery(history);
			break;
			
		case "Position":
			deletePosition(history);
			break;
			
		case "Subscriber":
			deleteSubscriber(history);
			break;
			
		case "Trigger":
			deleteTrigger(history);
			break;
			
		case "User":
			deleteUser(history);
			break;

		case "empReview":
			deleteEmpReview(history);
			break;
			
		case "award":
			deleteAwardWinner(history);
			break;
			
		case "culture":
			deleteCulture(history);
			break;
			
		case "ourCulture":
			deleteOurCulture(history);
			break;
			
		default:
			break;
		}	
	}
	private void deleteBlogsFaq(History history) {
		BlogFaq blogFaq=this.blogFaqService.getBlogFaqByIdAndStatus(history.getTypeId(),1);
		if(blogFaq!=null)
			this.blogFaqService.deleteBlogFaq(blogFaq);
		this.historyRepository.delete(history);
	}

	private void deleteServiceBrand(History history) {
		ServiceBrands brand=this.serviceBrand.getServiceBrandByIdAndStatus(history.getTypeId(),1);
		if(brand!=null)
			this.serviceBrand.deleteServiceBrand(brand);
		
		//deleting history
		this.historyRepository.delete(history);		
	}

	private void deleteNews(History history) {
		News news=this.newsService.findByIdAndDeleteStatus(history.getTypeId(), 1);
		if(news!=null)
			this.newsService.deleteNews(news);
		//deleting history
		this.historyRepository.delete(history);		
	}

	private void deletePress(History history) {
		Press press=this.pressService.findByIdAndDeleteStatus(history.getTypeId(), 1);
		if(press!=null)
			this.pressService.deletePress(press);
		//deleting history
		this.historyRepository.delete(history);
	}
	
	private void deleteNewsCategory(History history) {
		NewsCategory newsCategory=this.newsCategoryService.findByIdAndDeleteStatus(history.getTypeId(), 1);
		if(newsCategory!=null)
			this.newsCategoryService.deleteNewsCategory(newsCategory);
		//deleting history
		this.historyRepository.delete(history);		
	}

	private void deletePressCategory(History history) {
		PressCategory pressCategory=this.pressCategoryService.findByIdAndDeleteStatus(history.getTypeId(), 1);
		if(pressCategory!=null)
			this.pressCategoryService.deletePressCategory(pressCategory);
		//deleting history
		this.historyRepository.delete(history);
	}
	
	private void deleteCategory(History history) {
		Category category=this.categoryService.findByIdAndDeleteStatus(history.getTypeId(), 1);
		if(category!=null)
			this.categoryService.deleteCategory(category);
		//deleting history
		this.historyRepository.delete(history);		
	}
	private void deleteUser(History history) {
		User deleteUser=this.userService.findByIdAndDeleteStatus(history.getTypeId(),1);
		if(deleteUser!=null) {
			String dp=deleteUser.getProfilePicture();
			if(dp!=null&&!dp.equalsIgnoreCase("profile.png")&&!dp.equalsIgnoreCase("NA")&&dp.length()>0) {
				this.azureAdapter.deleteFile(dp);
			}
			if(deleteUser.getRole().equalsIgnoreCase("ROLE_USER")) {
				List<JobApplication> jobApp=this.jobAppService.findByEmail(deleteUser.getEmail());
				for (JobApplication jobApplication : jobApp) {
					String resume=jobApplication.getAttachedFile();
					if(resume!=null&&!resume.equalsIgnoreCase("NA")&&resume.length()>0) {
						this.azureAdapter.deleteFile(resume);
					}		
					this.jobAppService.deleteJobApplication(jobApplication.getId());
				}
			}
			this.userService.deleteUser(deleteUser.getId());
		}
		//deleting history
		this.historyRepository.delete(history);
	}

	private void deleteTrigger(History history) {
		TriggersList trigger=this.triggerService.findByIdAndDeleteStatus(history.getTypeId(),1);
		if(trigger!=null) {
			this.triggerService.deleteById(trigger.getId());
		}	
		//deleting history
		this.historyRepository.delete(history);
	}

	private void deleteSubscriber(History history) {
		Subscribers subscriber=this.subscriberService.findByIdAndDeleteStatus(history.getTypeId(),1);
		if(subscriber!=null) {
			this.subscriberService.deleteById(subscriber.getId());
		}
		//deleting history
		this.historyRepository.delete(history);
	}
	private void deletePosition(History history) {
		Position position=this.positionService.findByIdAndDeleteStatus(history.getTypeId(),1);
		if(position!=null) {
			this.positionService.deletePosition(position.getId());
		}	
		//deleting history
		this.historyRepository.delete(history);
	}
	private void deleteGallery(History history) {
		Gallery findGallery=this.galleryService.findByIdAndDeleteStatus(history.getTypeId(),1);
		if(findGallery!=null) {
			String docName=findGallery.getFileName();
			if(docName!=null&&!docName.equalsIgnoreCase("NA")&&docName.length()>0) {
				this.azureAdapter.deleteFile(docName);
			}	
			this.galleryService.deleteGallery(findGallery.getId());
		}
		//deleting history
		this.historyRepository.delete(history);
	}
	private void deleteSlideShow(History history) {
		Slideshow slideShow=this.slideShowService.findByIdAndDeleteStatus(history.getTypeId(),1);
		if(slideShow!=null) {
			String docName=slideShow.getImageName();
			if(docName!=null&&!docName.equalsIgnoreCase("NA")&&docName.length()>0) {
				this.azureAdapter.deleteFile(docName);
			}		
			this.slideShowService.deleteSlideShow(slideShow.getId());
		}
		//deleting history
		this.historyRepository.delete(history);
	}
	private void deleteLawUpdate(History history) {
		LawUpdate lawUpdate=this.lawUpdateService.findByIdAndDeleteStatus(history.getTypeId(),1);
		if(lawUpdate!=null) {
			this.siteMapService.deleteUrl("Law Update",lawUpdate.getId());
			this.lawUpdateService.deleteLawUpdate(lawUpdate.getId());
		}
		//deleting history
		this.historyRepository.delete(history);
	}
	private void deleteHotTag(History history) {
		HotTags hotTags=this.hotTagService.findHotTagByIdAndStatus(history.getTypeId(),1);
		if(hotTags!=null) {
			this.hotTagService.deleteTag(hotTags.getId());
		}
		//deleting history
		this.historyRepository.delete(history);
	}

	private void deleteAwardWinner(History history) {
		AwardWinners awardWinner=this.awardWinnerService.findByIdAndStatus(history.getTypeId(), 1);
		if(awardWinner!=null) {
			this.awardWinnerService.deleteAwardWinner(awardWinner);
		}
		//deleting history
		this.historyRepository.delete(history);
	}
	
	private void deleteOurCulture(History history) {
		OurCulture ourCulture=this.ourCultureService.findByIdAndStatus(history.getTypeId(), 1);
		if(ourCulture!=null) {
			this.ourCultureService.deleteOurCulture(ourCulture);
		}
		//deleting history
		this.historyRepository.delete(history);
	}
	
	private void deleteCulture(History history) {
		CultureGallery cultureGallery=this.cultureGalleryService.findByIdAndStatus(history.getTypeId(), 1);
		if(cultureGallery!=null) {
			this.cultureGalleryService.deleteCultureGallery(cultureGallery);
		}
		//deleting history
		this.historyRepository.delete(history);
	}
	
	private void deleteEmpReview(History history) {
		EmployeeReview empReview=this.empReviewService.findByIdAndDeleteStatus(history.getTypeId(), 1);
		if(empReview!=null) {
			this.empReviewService.deleteReview(empReview);
		}
		//deleting history
		this.historyRepository.delete(history);
	}
	
	private void deleteReview(History history) {
		Review review=this.masterService.findReviewByIdAndStatus(history.getTypeId(),1);			
		if(review!=null) {
			this.masterService.deleteReview(review.getId());
		}
		//deleting history
		this.historyRepository.delete(history);
	}	
	private void deleteContact(History history) {
		Contact contact=this.masterService.findContactByIdAndStatus(history.getTypeId(),1);
		if(contact!=null) {
			this.masterService.deleteContact(contact.getId());
		}
		//deleting history
		this.historyRepository.delete(history);
	}
	private void deleteCity(History history) {
		City city=this.masterService.findCityByIdAndDeleteStatus(history.getTypeId(),1);
		if(city!=null) {
			this.masterService.deleteCity(city.getId());
		}	
		//deleting history
		this.historyRepository.delete(history);
	}

	private void deleteState(History history) {
		State state=this.masterService.findStateByIdAndDeleteStatus(history.getTypeId(),1);
		if(state!=null) {
			this.masterService.deleteState(state.getId());
		}
		//deleting history
		this.historyRepository.delete(history);
	}

	private void deleteCountry(History history) {
		Country country=this.masterService.findCountryByIdAndDeleteStatus(history.getTypeId(),1);
		if(country!=null) {
			this.masterService.deleteCountry(country.getId());
		}
		//deleting history
		this.historyRepository.delete(history);
	}
	private void deleteAddress(History history) {
		ContactAddress address=this.masterService.findAddressByIdAndDeleteStatus(history.getTypeId(), 1);
		if(address!=null) {
			this.masterService.deleteContactAddress(address.getId());
		}
		//deleting history
		this.historyRepository.delete(history);
	}
	private void deleteEnquiry(History history) {
		Enquiry enquiry=this.enquiryService.findByIdAndDeleteStatus(history.getTypeId(),1);
		if(enquiry!=null) {	
			this.enquiryService.deleteEnquiry(enquiry.getId());
		}
		//deleting history
		this.historyRepository.delete(history);
	}

	private void deleteFooter(History history) {
		FooterCategory footer=this.footerService.findByIdAndDeleteStatus(history.getTypeId(),1);
		if(footer!=null) {	
			this.footerService.deleteFooter(footer.getId());
		}
		//deleting history
		this.historyRepository.delete(history);
	}

	private void deleteCMSPage(History history) {
		CmsPages pages=this.masterService.findByIdAndDeleteStatus(history.getTypeId(),1);
		if(pages!=null) {
			this.masterService.deleteCmsPages(pages.getId());
		}
		//deleting history
		this.historyRepository.delete(history);
	}
	private void deleteJobApplication(History history) {
		JobApplication jobApp=this.jobAppService.findByIdAndDeleteStatus(history.getTypeId(),1);
		if(jobApp!=null) {
			String resume=jobApp.getAttachedFile();
			if(resume!=null&&!resume.equalsIgnoreCase("NA")&&resume.length()>0) {
				this.azureAdapter.deleteFile(resume);
			}		
			this.jobAppService.deleteJobApplication(jobApp.getId());
		}
		//deleting history
		this.historyRepository.delete(history);
	}
	private void deleteJob(History history) {
		Jobs job=this.jobService.findByIdAndDeleteStatus(history.getTypeId(),1);
		if(job!=null) {
			List<JobApplication> jobApp=job.getJobApplication();
			for (JobApplication jobApplication : jobApp) {
				String resume=jobApplication.getAttachedFile();
				if(resume!=null&&!resume.equalsIgnoreCase("NA")&&resume.length()>0) {
					this.azureAdapter.deleteFile(resume);
				}
			}
			
			this.jobService.deleteJob(job.getId());
		}
		//deleting history
		this.historyRepository.delete(history);
	}

	private void deleteDocument(History history) {
		Documents document=this.documentService.findByIdAndDeleteStatus(history.getTypeId(),1);
		if(document!=null) {
			String docName=document.getFileName();
			if(docName!=null&&!docName.equalsIgnoreCase("NA")&&docName.length()>0) {
				this.azureAdapter.deleteFile(docName);
			}
			this.documentService.deleteDocument(document.getId());
		}
		//deleting history
		this.historyRepository.delete(history);
	}

	private void deleteCoupon(History history) {
		Coupon coupon=this.couponService.findByIdAndDeleteStatus(history.getTypeId(),1);
		if(coupon!=null) {
			this.couponService.deleteCoupon(coupon.getId());
		}
		//deleting history
		this.historyRepository.delete(history);
	}

	private void deleteMeeting(History history) {
		Meeting meeting=this.meetingService.findByIdAndDeleteStatus(history.getTypeId(),1);
		if(meeting!=null) {
			this.meetingService.deleteMeeting(meeting.getId());
		}
		//deleting history
		this.historyRepository.delete(history);
	}
 
	private void deleteHrmBlogs(History history) {
	HrmBlog blogs = this.hrmBlogService.findByIdAndDeleteStatus(history.getTypeId(),1);
		if(blogs!=null) {
			this.siteMapService.deleteUrl("Blog",blogs.getId());
			
			if(blogs.getImage()!=null&&blogs.getImage().length()>0)
				this.azureAdapter.deleteFile(blogs.getImage());
			
			this.hrmBlogService.deleteBlog(blogs);
		}
		//deleting history
		this.historyRepository.delete(history);
	}
	
	private void deleteBlogs(History history) {
		Blogs blogs=this.blogService.findByIdAndDeleteStatus(history.getTypeId(),1);
		if(blogs!=null) {
			this.siteMapService.deleteUrl("Blog",blogs.getId());
			
			if(blogs.getImage()!=null&&blogs.getImage().length()>0)
				this.azureAdapter.deleteFile(blogs.getImage());
			
			this.blogService.deleteBlog(blogs);
		}
		//deleting history
		this.historyRepository.delete(history);
	}

	private void deleteIndustryDetails(History history) {
		IndustryDetails industryDetails=this.industryService.findDetailsByIdAndDeleteStatus(history.getTypeId(),1);
		if(industryDetails!=null) {
			this.industryService.deleteIndustryDetails(industryDetails);
		}
		//deleting history
		this.historyRepository.delete(history);
	}

	private void deleteIndustry(History history) {
		Industry industry=this.industryService.findByIdAndDeleteStatus(history.getTypeId(),1);
		if(industry!=null) {
			this.siteMapService.deleteUrl("Industry",industry.getId());
			this.industryService.deleteIndustry(industry);
		}
		//deleting history
		this.historyRepository.delete(history);
	}

	private void deleteServicePackage(History history) {
		ServicePackage servicePackage=this.servicesService.findPackageByIdAndStatus(history.getTypeId(),1);
		if(servicePackage!=null) {
			this.servicesService.deleteServicePackage(servicePackage);
		}
		//deleting history
		this.historyRepository.delete(history);		
	}
	private void deleteServiceDetails(History history) {
		ServiceDetails serviceDetails=this.servicesService.findServiceDetailsByIdAndStatus(history.getTypeId(),1);
		if(serviceDetails!=null) {
			this.servicesService.deleteServiceDetails(serviceDetails);
		}
		//deleting history
		this.historyRepository.delete(history);
	}
	private void deleteServiceFAQ(History history) {
		ServiceFaq serviceFaq=this.servicesService.findFaqByIdAndDeleteStatus(history.getTypeId(),1);
		if(serviceFaq!=null) {
			this.servicesService.deleteServiceFaq(serviceFaq);
		}
		//deleting history
		this.historyRepository.delete(history);
	}
	
	private void deleteService(History history) {
		Services service=this.servicesService.findByIdAndDeleteStatus(history.getTypeId(),1);
		if(service!=null) {
			if(!service.getLegalGuide().equals("NA")) {
				this.azureAdapter.deleteFile(service.getLegalGuide());
			}
			this.siteMapService.deleteUrl("Service",service.getId());
			this.servicesService.deleteService(service);
		}
		//deleting history
		this.historyRepository.delete(history);
	}

	private void deleteSubService(History history) {
		SubService subService=this.subServiceService.findSubServiceByIdAndDeleteStatus(history.getTypeId(),1);
		if(subService!=null){
			this.azureAdapter.deleteFile(subService.getImage());
			this.subServiceService.deleteSubService(subService);
		}
		//deleting history
		this.historyRepository.delete(history);
	}

	private void deleteServiceContact(History history) {
		ServiceContact serviceContact=this.serviceContactService.findServiceContactByIdAndDeleteStatus(history.getTypeId(),1);
		if(serviceContact!=null)
			this.serviceContactService.deleteServiceContact(serviceContact);
		//deleting history
		this.historyRepository.delete(history);
	}

	public void restoreData(History history,User user) {
		switch (history.getType()) {
		case "Category":
			restoreCategory(history,user);
			break;
		
		case "News Category":
			restoreNewsCategory(history);
			break;

		case "PressCategory":
			restorePressCategory(history);
			break;
			
		case "News":
			restoreNews(history);
			break;

		case "Press":
			restorePress(history);
			break;
			
		case "Service":
			restoreService(history);
			break;

		case "SubService":
			restoreSubService(history);
			break;

		case "ServiceContact":
			restoreServiceContact(history);
			break;
			
		case "Service FAQ":
			restoreServiceFAQ(history);
			break;
			
		case "Service Details":
			restoreServiceDetails(history);
			break;
			
		case "Service Package":
			restoreServicePackage(history);
			break;
			
		case "Service Brand":
			restoreServiceBrand(history);
			break;
			
		case "Industry":
			restoreIndustry(history);
			break;
			
		case "Industry Details":
			restoreIndustryDetails(history);
			break;
			
		case "Blogs":
			restoreBlogs(history);
			break;
			
		case "Blog FAQ":
			restoreBlogsFaq(history);
			break;
			
		case "Hrm Blogs":
			restoreHrmBlogs(history);
			break;
			
		case "Meeting":
			restoreMeeting(history);
			break;
			
		case "Coupon":
			restoreCoupon(history);
			break;
			
		case "Document":
			restoreDocument(history);
			break;
			
		case "Job":
			restoreJob(history);
			break;
			
		case "Job Application":
			restoreJobApplication(history);
			break;
			
		case "CMS Page":
			restoreCMSPage(history);
			break;
			
		case "Footer":
			restoreFooter(history);
			break;
			
		case "Enquiry":
			restoreEnquiry(history);
			break;
			
		case "Address":
			restoreAddress(history);
			break;
			
		case "Country":
			restoreCountry(history);
			break;
			
		case "State":
			restoreState(history);
			break;
			
		case "City":
			restoreCity(history);
			break;
			
		case "Contact":
			restoreContact(history);
			break;
			
		case "Review":
			restoreReview(history);
			break;
			
		case "Hot Tag":
			restoreHotTag(history);
			break;
			
		case "Law Update":
			restoreLawUpdate(history);
			break;
			
		case "SlideShow":
			restoreSlideShow(history);
			break;
			
		case "Gallery":
			restoreGallery(history);
			break;
			
		case "Position":
			restorePosition(history);
			break;
			
		case "Subscriber":
			restoreSubscriber(history);
			break;
			
		case "Trigger":
			restoreTrigger(history);
			break;
			
		case "User":
			restoreUser(history);
			break;
			
		case "empReview":
			restoreEmpReview(history);
			break;
			
		case "award":
			restoreAwardWinner(history);
			break;
			
		case "culture":
			restoreCulture(history);
			break;
			
		case "ourCulture":
			restoreOurCulture(history);
			break;

		default:System.out.println("Value doesn't exist....................");
			break;
		}	
	}

	private void restoreBlogsFaq(History history) {
		BlogFaq blogFaq=this.blogFaqService.getBlogFaqByIdAndStatus(history.getTypeId(), 1);
		if(blogFaq!=null) {
			blogFaq.setDeleteStatus(2);
			this.blogFaqService.saveBlogFaq(blogFaq);
			this.historyRepository.delete(history);
		}
	}

	private void restoreServiceBrand(History history) {
		ServiceBrands brand=this.serviceBrand.getServiceBrandByIdAndStatus(history.getTypeId(), 1);
		if(brand!=null) {
			brand.setDeleteStatus(2);
			this.serviceBrand.saveServiceBrand(brand);
			
			this.historyRepository.delete(history);
		}
	}

	private void restoreUser(History history) {
		User deleteUser=this.userService.findByIdAndDeleteStatus(history.getTypeId(),1);
		if(deleteUser!=null) {
			deleteUser.setDeleteStatus(2);
			this.userService.saveUser(deleteUser);
			
			//updating position
			Position position=deleteUser.getPosition();
			if(position!=null){
				position.setDeleteStatus(2);
				this.positionService.savePosition(position);
			}					
			
			//updating candidate document
			List<CandidateDocuments> candidateDoc=deleteUser.getDocList();
			if(!candidateDoc.isEmpty()) {
				candidateDoc.forEach(cd->cd.setDeleteStatus(2));
				this.candDocService.saveDocuments(candidateDoc);
			}
			
			//updating permissions
			List<HrPermissions> hrPermission=deleteUser.getHrPermissions();
			if(!hrPermission.isEmpty()) {
				hrPermission.forEach(hp->hp.setDeleteStatus(2));
				this.hrPermissionService.saveHrPermissions(hrPermission);
			}	
			
			//updating forget password
			List<ForgetPassword> forgetPassword=deleteUser.getForgetPassword();
			if(!forgetPassword.isEmpty()) {
				forgetPassword.forEach(fp->fp.setDeleteStatus(2));
				this.forgetPasswordService.saveForgetPasswordLinks(forgetPassword);
			}
			
			//deleting history
			this.historyRepository.delete(history);
		}
	}

	private void restoreTrigger(History history) {
		TriggersList trigger=this.triggerService.findByIdAndDeleteStatus(history.getTypeId(),1);
		if(trigger!=null) {
			trigger.setDeleteStatus(2);
			this.triggerService.saveTrigger(trigger);
			
			//deleting history
			this.historyRepository.delete(history);
		}	
	}

	private void restoreSubscriber(History history) {
		Subscribers subscriber=this.subscriberService.findByIdAndDeleteStatus(history.getTypeId(),1);
		if(subscriber!=null) {
			subscriber.setDeleteStatus(2);
			this.subscriberService.saveSubscriber(subscriber);
			
			//deleting history
			this.historyRepository.delete(history);
		}
	}

	private void restorePosition(History history) {
		Position position=this.positionService.findByIdAndDeleteStatus(history.getTypeId(),1);
		if(position!=null) {
			position.setDeleteStatus(2);
			this.positionService.savePosition(position);
			
			//deleting history
			this.historyRepository.delete(history);
		}	
	}

	private void restoreGallery(History history) {
		Gallery findGallery=this.galleryService.findByIdAndDeleteStatus(history.getTypeId(),1);
		if(findGallery!=null) {
			findGallery.setDeleteStatus(2);
			this.galleryService.saveGallery(findGallery);
			
			//deleting history
			this.historyRepository.delete(history);
		}
	}

	private void restoreSlideShow(History history) {
		Slideshow slideShow=this.slideShowService.findByIdAndDeleteStatus(history.getTypeId(),1);
		if(slideShow!=null) {
			slideShow.setDeleteStatus(2);
			this.slideShowService.saveSlide(slideShow);
			
			//deleting history
			this.historyRepository.delete(history);
		}
	}

	private void restoreLawUpdate(History history) {
		LawUpdate lawUpdate=this.lawUpdateService.findByIdAndDeleteStatus(history.getTypeId(),1);
		if(lawUpdate!=null) {
			lawUpdate.setDeleteStatus(2);
			this.lawUpdateService.saveLawUpdate(lawUpdate);
			
			//updating sitemap url
			SiteMapUrl siteMap=this.siteMapService.findByTypeAndUid("Law Update", lawUpdate.getId());
			if(siteMap!=null) {
				siteMap.setStatus(1);
				this.siteMapService.saveUrl(siteMap);
			}
			
			//deleting history
			this.historyRepository.delete(history);
		}
	}

	private void restoreHotTag(History history) {
		HotTags hotTags=this.hotTagService.findHotTagByIdAndStatus(history.getTypeId(),1);
		if(hotTags!=null) {
			hotTags.setDeleteStatus(2);
			this.hotTagService.saveTag(hotTags);
			
			//deleting history
			this.historyRepository.delete(history);
		}
	}

	private void restoreEmpReview(History history) {
		EmployeeReview empReview=this.empReviewService.findByIdAndDeleteStatus(history.getTypeId(),1);
		if(empReview!=null) {
			empReview.setDeleteStatus(2);
			this.empReviewService.save(empReview);
			
			//deleting history
			this.historyRepository.delete(history);	
		}
	}
	
	private void restoreAwardWinner(History history) {
		AwardWinners awardWinner=this.awardWinnerService.findByIdAndStatus(history.getTypeId(),1);
		if(awardWinner!=null) {
			awardWinner.setDeleteStatus(2);
			this.awardWinnerService.saveAward(awardWinner);
			
			//deleting history
			this.historyRepository.delete(history);	
		}
	}
	
	private void restoreOurCulture(History history) {
		OurCulture ourCulture=this.ourCultureService.findByIdAndStatus(history.getTypeId(),1);
		if(ourCulture!=null) {
			ourCulture.setDeleteStatus(2);
			this.ourCultureService.saveOurCulture(ourCulture);
			
			//deleting history
			this.historyRepository.delete(history);	
		}
	}
	
	private void restoreCulture(History history) {
		CultureGallery cultureGallery=this.cultureGalleryService.findByIdAndStatus(history.getTypeId(),1);
		if(cultureGallery!=null) {
			cultureGallery.setDeleteStatus(2);
			this.cultureGalleryService.saveCultureGallery(cultureGallery);
			
			//deleting history
			this.historyRepository.delete(history);	
		}
	}
	
	private void restoreReview(History history) {
		Review review=this.masterService.findReviewByIdAndStatus(history.getTypeId(),1);			
		if(review!=null) {
			review.setDeleteStatus(2);
			this.masterService.saveReview(review);
			
			//deleting history
			this.historyRepository.delete(history);	
		}
	}

	private void restoreContact(History history) {
		Contact contact=this.masterService.findContactByIdAndStatus(history.getTypeId(),1);
		if(contact!=null) {
			contact.setDeleteStatus(2);
			this.masterService.saveContact(contact);
			
			//deleting history
			this.historyRepository.delete(history);	
		}
	}

	private void restoreCity(History history) {
		City city=this.masterService.findCityByIdAndDeleteStatus(history.getTypeId(),1);
		if(city!=null) {
			city.setDeleteStatus(2);
			this.masterService.saveCity(city);
			
			//deleting history
			this.historyRepository.delete(history);	
		}		
	}

	private void restoreState(History history) {
		State state=this.masterService.findStateByIdAndDeleteStatus(history.getTypeId(),1);
		if(state!=null) {
			state.setDeleteStatus(2);
			this.masterService.saveState(state);
			
			//updating city
			List<City> city=state.getListCity();
			if(!city.isEmpty()) {
				city.forEach(c->c.setDeleteStatus(2));
				this.masterService.saveCities(city);
			}
			//deleting history
			this.historyRepository.delete(history);	
		}
	}

	private void restoreCountry(History history) {
		Country country=this.masterService.findCountryByIdAndDeleteStatus(history.getTypeId(),1);
		if(country!=null) {
			country.setDeleteStatus(2);
			this.masterService.saveCountry(country);
			
			//updating state
			List<State> state=country.getState();
			List<City> cities=new ArrayList<>();
			if(!state.isEmpty()) {
				for (State s : state) {
					s.setDeleteStatus(2);
					if(!s.getListCity().isEmpty())
						cities.addAll(s.getListCity());
				}	
				if(!cities.isEmpty()) {
					cities.forEach(c->c.setDeleteStatus(2));
					this.masterService.saveCities(cities);
				}
				this.masterService.saveStates(state);					
			}
			//deleting history
			this.historyRepository.delete(history);	
		}
	}

	private void restoreAddress(History history) {
		ContactAddress address=this.masterService.findAddressByIdAndDeleteStatus(history.getTypeId(), 1);
		if(address!=null) {
			address.setDeleteStatus(1);
			this.masterService.saveAddress(address);
			
			//deleting history
			this.historyRepository.delete(history);	
		}
	}

	private void restoreEnquiry(History history) {
		Enquiry enquiry=this.enquiryService.findByIdAndDeleteStatus(history.getTypeId(),1);
		if(enquiry!=null) {
			enquiry.setDeleteStatus(2);
			this.enquiryService.saveEnquiry(enquiry);
			
			//deleting history
			this.historyRepository.delete(history);			
		}
	}

	private void restoreFooter(History history) {
		FooterCategory footer=this.footerService.findByIdAndDeleteStatus(history.getTypeId(),1);
		if(footer!=null) {				
			footer.setDeleteStatus(2);
			this.footerService.saveFooter(footer);
			
			//updating footer service
			List<FooterService> footerService=footer.getFooterService();
			if(!footerService.isEmpty()) {
				footerService.forEach(fs->fs.setDeleteStatus(2));
				this.footerService.saveFooterService(footerService);
			}
			//deleting history
			this.historyRepository.delete(history);			
		}
	}

	private void restoreCMSPage(History history) {
		CmsPages pages=this.masterService.findByIdAndDeleteStatus(history.getTypeId(),1);
		if(pages!=null) {
			pages.setDeleteStatus(2);
			this.masterService.saveCmsPages(pages);
				
		SiteMapUrl siteMap=this.siteMapService.findByTypeAndUid("CMS Page", pages.getId());
		if(siteMap!=null) {
			siteMap.setStatus(1);
			this.siteMapService.saveUrl(siteMap);
		}
		//deleting history
		this.historyRepository.delete(history);		
		
		}
	}

	private void restoreJobApplication(History history) {
		JobApplication jobApp=this.jobAppService.findByIdAndDeleteStatus(history.getTypeId(),1);
		if(jobApp!=null) {
			jobApp.setDeleteStatus(2);
			this.jobAppService.saveJobApplication(jobApp);
			
			//updating scheduled interview
			List<InterviewSchedule> interviewSchedule=jobApp.getInterviewSchedule();
			if(!interviewSchedule.isEmpty()) {
				interviewSchedule.forEach(is->is.setDeleteStatus(2));
				this.interviewService.saveScheduledInterview(interviewSchedule);
			}
			//updating track application
			TrackApplication trackApp=jobApp.getTrackApplication();
			if(trackApp!=null) {
				trackApp.setDeleteStatus(2);
				this.trackAppService.saveTrackApplication(trackApp);					
			}
			//updating candidate documents
			List<CandidateDocuments> candidateDocument=jobApp.getCandidateDocument();
			if(!candidateDocument.isEmpty()) {
				candidateDocument.forEach(cd->cd.setDeleteStatus(2));
				this.candDocService.saveDocuments(candidateDocument);
			}
			
			//deleting history
			this.historyRepository.delete(history);		
		}
	}

	private void restoreJob(History history) {
		Jobs job=this.jobService.findByIdAndDeleteStatus(history.getTypeId(),1);
		if(job!=null) {
			job.setDeleteStatus(2);
			this.jobServices.saveJobs(job);
			
			//updating job application
			List<JobApplication> jobApplication=job.getJobApplication();
			if(!jobApplication.isEmpty()) {
				List<InterviewSchedule> interviewSchedule=new ArrayList<>();
				List<TrackApplication> trackApp=new ArrayList<>();
				List<CandidateDocuments> candidateDocument=new ArrayList<>();
				
				for (JobApplication jobApp : jobApplication) {
					jobApp.setDeleteStatus(2);
					
					if(!jobApp.getInterviewSchedule().isEmpty())
						interviewSchedule.addAll(jobApp.getInterviewSchedule());
					
					if(jobApp.getTrackApplication()!=null)
						trackApp.add(jobApp.getTrackApplication());
					
					if(!jobApp.getCandidateDocument().isEmpty())
						candidateDocument.addAll(jobApp.getCandidateDocument());					
				}
				if(!interviewSchedule.isEmpty()) {
					interviewSchedule.forEach(is->is.setDeleteStatus(2));
					this.interviewService.saveScheduledInterview(interviewSchedule);
				}
				if(!trackApp.isEmpty()) {
					trackApp.forEach(ta->ta.setDeleteStatus(2));
					this.trackAppService.saveTrackApplications(trackApp);
				}
				if(!candidateDocument.isEmpty()) {
					candidateDocument.forEach(cd->cd.setDeleteStatus(2));
					this.candDocService.saveDocuments(candidateDocument);
				}
				
				this.jobAppService.saveJobApplications(jobApplication);	
			}
			//deleting history
			this.historyRepository.delete(history);			
		}
	}

	private void restoreDocument(History history) {
		Documents document=this.documentService.findByIdAndDeleteStatus(history.getTypeId(),1);	
		if(document!=null) {
			document.setDeleteStatus(2);
			this.documentService.saveDocument(document);
			
			//deleting history
			this.historyRepository.delete(history);
		}
	}

	private void restoreCoupon(History history) {
		Coupon coupon=this.couponService.findByIdAndDeleteStatus(history.getTypeId(),1);
		if(coupon!=null) {
			coupon.setDeleteStatus(2);
			this.couponService.saveCoupon(coupon);
			
			//updating coupon service				
			List<CouponServices> couponServices=coupon.getCouponServices();
			if(!couponServices.isEmpty()) {
				couponServices.forEach(cs->cs.setDeleteStatus(2));
				this.couponServicesService.saveAllServices(couponServices);
			}
			
			//deleting history
			this.historyRepository.delete(history);
		}
	}

	private void restoreMeeting(History history) {
		Meeting meeting=this.meetingService.findByIdAndDeleteStatus(history.getTypeId(),1);
		if(meeting!=null) {
			meeting.setDeleteStatus(2);
			this.meetingService.saveMeeting(meeting);
			
			//deleting history
			this.historyRepository.delete(history);
		}
	}

	private void restoreHrmBlogs(History history) {
		HrmBlog blogs = this.hrmBlogService.findByIdAndDeleteStatus(history.getTypeId(),1);
		
		if(blogs!=null) {
			blogs.setDeleteStatus(2);
			this.hrmBlogService.saveBlog(blogs);			
			
			//deleting history
			this.historyRepository.delete(history);
		}
	}
	
	private void restoreBlogs(History history) {
		Blogs blogs=this.blogService.findByIdAndDeleteStatus(history.getTypeId(),1);
		if(blogs!=null) {
			blogs.setDeleteStatus(2);
			this.blogService.saveBlogs(blogs);
			
			//updating service blogs
			List<ServiceBlogs> serviceBlogs=blogs.getServiceBlogs();
			if(!serviceBlogs.isEmpty()) {
				serviceBlogs.forEach(sb->sb.setDeleteStatus(2));
				this.servicesBlogService.saveServiceBlogs(serviceBlogs);
			}
			//updating service blog card list
			List<BlogServiceCardList> blogServicecardList=blogs.getBlogServiceCardLists();
			if(!blogServicecardList.isEmpty()) {
				blogServicecardList.forEach(bscl->bscl.setDeleteStatus(2));
				this.blogServiceCardService.saveAll(blogServicecardList);
			}
			//updating sitemap url
			SiteMapUrl siteMap = this.siteMapService.findByTypeAndUid("Blog", blogs.getId());
			if(siteMap!=null) {
				siteMap.setStatus(1);
				this.siteMapService.saveUrl(siteMap);
			}
			//deleting history
			this.historyRepository.delete(history);
		}
	}

	private void restoreIndustryDetails(History history) {
		IndustryDetails industryDetails=this.industryService.findDetailsByIdAndDeleteStatus(history.getTypeId(),1);
		if(industryDetails!=null) {
			industryDetails.setDeleteStatus(2);
			this.industryService.saveIndustryDetails(industryDetails);
			
			//deleting history
			this.historyRepository.delete(history);
		}
	}

	private void restoreIndustry(History history) {
		Industry industry=this.industryService.findByIdAndDeleteStatus(history.getTypeId(),1);
		if(industry!=null) {
			industry.setDeleteStatus(2);
			this.industryService.saveIndustry(industry);
			
			//updating industry details
			List<IndustryDetails> industryDetails=industry.getIndustryDetails();
			if(!industryDetails.isEmpty()) {
				industryDetails.forEach(ind->ind.setDeleteStatus(2));
				this.industryService.saveAllIndustryDetails(industryDetails);
			}
			//updating sitemap url
			SiteMapUrl siteMap = this.siteMapService.findByTypeAndUid("Industry", industry.getId());
			if(siteMap!=null) {
				siteMap.setStatus(1);
				this.siteMapService.saveUrl(siteMap);
			}	
			
			//deleting history
			this.historyRepository.delete(history);
		}
	}

	private void restoreServicePackage(History history) {
		ServicePackage servicePackage=this.servicesService.findPackageByIdAndStatus(history.getTypeId(),1);
		if(servicePackage!=null) {
			servicePackage.setDeleteStatus(1);
			this.servicesService.saveServicePackage(servicePackage);
			
			//deleting history
			this.historyRepository.delete(history);
		}
		
	}

	private void restoreServiceDetails(History history) {
		ServiceDetails serviceDetails=this.servicesService.findServiceDetailsByIdAndStatus(history.getTypeId(),1);
		if(serviceDetails!=null) {
			serviceDetails.setDeleteStatus(2);
			this.servicesService.saveServiceDetails(serviceDetails);
			 
			//updating service card list
			List<ServiceCardList> serviceCardLists=serviceDetails.getServiceCardLists();
			if(!serviceCardLists.isEmpty()) {
				serviceCardLists.forEach(sd->sd.setDeleteStatus(2));
				this.serviceCardListService.saveAll(serviceCardLists);
			}
			
			//deleting history
			this.historyRepository.delete(history);
		}
	}

	private void restoreServiceFAQ(History history) {
		ServiceFaq serviceFaq=this.servicesService.findFaqByIdAndDeleteStatus(history.getTypeId(),1);
		if(serviceFaq!=null) {
			serviceFaq.setDeleteStatus(2);
			this.servicesService.saveServiceFaq(serviceFaq);
			
			//deleting history
			this.historyRepository.delete(history);
		}
	}

	private void restoreSubService(History history) {
		SubService subService=this.subServiceService.findSubServiceByIdAndDeleteStatus(history.getTypeId(),1);
		if(subService!=null) {
			subService.setDeleteStatus(2);
			this.subServiceService.updateSubService(subService);

			//deleting history
			this.historyRepository.delete(history);
		}
	}

	private void restoreServiceContact(History history) {
		ServiceContact serviceContact=this.serviceContactService.findServiceContactByIdAndDeleteStatus(history.getTypeId(),1);
		if(serviceContact!=null) {
			serviceContact.setDeleteStatus(2);
			this.serviceContactService.saveServiceContact(serviceContact);

			//deleting history
			this.historyRepository.delete(history);
		}
	}

	private void restoreService(History history) {
		Services service=this.servicesService.findByIdAndDeleteStatus(history.getTypeId(),1);
		if(service!=null) {
			service.setDeleteStatus(2);
			this.servicesService.saveServices(service);
						
		//updating hot Tag
		HotTags hotTag=service.getHotTags();
		if(hotTag!=null) {
			hotTag.setDeleteStatus(2);
			this.hotTagService.saveTag(hotTag);
		}
		//updating service blogs
		List<ServiceBlogs> serviceBlogs=service.getServiceBlogs();
		if(!serviceBlogs.isEmpty()) {
			serviceBlogs.forEach(sb->sb.setDeleteStatus(2));				
			this.serviceBlogService.saveServiceBlogs(serviceBlogs);
		}
		//updating service Package
		List<ServicePackage> servicePackage=service.getServicePackage();
		if(!servicePackage.isEmpty()) {
			servicePackage.forEach(sp->sp.setDeleteStatus(2));				
			this.servicesService.saveServicePackages(servicePackage);
		}
		
		//updating serviceFaq
		List<ServiceFaq> serviceFaq=service.getServiceFaq();
		if(!serviceFaq.isEmpty()) {
			serviceFaq.forEach(sf->sf.setDeleteStatus(2));				
			this.servicesService.saveServicesFaq(serviceFaq);
		}
		
		//updating service details
		List<ServiceDetails> serviceDetails=service.getServiceDetails();
		if(!serviceDetails.isEmpty()) {			
			serviceDetails.forEach(sd->sd.setDeleteStatus(2));				
			this.servicesService.saveServicesDetails(serviceDetails);
		}
		
		//updating orders
		List<Orders> orders=service.getOrders();
		if(!orders.isEmpty()) {
			orders.forEach(ord->ord.setDeleteStatus(2));				
			this.orderService.saveOrders(orders);
		}
		
		//updating Coupon Services
		List<CouponServices> couponService=service.getCouponService();
		if(!couponService.isEmpty()) {
			couponService.forEach(cs->cs.setDeleteStatus(2));				
			this.couponServicesService.saveAllServices(couponService);
		}
		//updating footer service
		FooterService footerService=service.getFooterService();
		if(footerService!=null) {
			footerService.setDeleteStatus(2);
			this.footerService.saveFooterService(footerService);
		}
		//updating sitemap url
		SiteMapUrl siteMap = this.siteMapService.findByTypeAndUid("Service", service.getId());
		if(siteMap!=null) {
			siteMap.setStatus(1);
			this.siteMapService.saveUrl(siteMap);
		}
		
		//deleting history
		this.historyRepository.delete(history);
		
		}
	}

	private void restorePress(History history) {
		Press press=this.pressService.findByIdAndDeleteStatus(history.getTypeId(),1);
		if(press!=null) {
			press.setDeleteStatus(2);
			this.pressService.savePress(press);

			//deleting history
			this.historyRepository.delete(history);

		}
	}

	private void restoreNews(History history) {
		News news=this.newsService.findByIdAndDeleteStatus(history.getTypeId(),1);
		if(news!=null) {
			news.setDeleteStatus(2);
			this.newsService.saveNews(news);
			
			//deleting history
			this.historyRepository.delete(history);
			
		}
	}

	private void restorePressCategory(History history) {
		PressCategory pressCategory=this.pressCategoryService.findByIdAndDeleteStatus(history.getTypeId(),1);
		if(pressCategory!=null) {
			pressCategory.setDeleteStatus(2);
			this.pressCategoryService.savePressCategory(pressCategory);

			List<Press> pressList = pressCategory.getPress()
					.stream().map(p -> {
						p.setDeleteStatus(2);
						return p;
					})
					.collect(Collectors.toList());
			this.pressService.saveAllPress(pressList);
			//deleting history
			this.historyRepository.delete(history);
		}
	}

	private void restoreNewsCategory(History history) {
		NewsCategory newsCategory=this.newsCategoryService.findByIdAndDeleteStatus(history.getTypeId(),1);
		if(newsCategory!=null) {
			newsCategory.setDeleteStatus(2);
			this.newsCategoryService.saveNewsCategory(newsCategory);
			
			List<News> news=newsCategory.getNews();
			if(!news.isEmpty()) {
				news.forEach(n->n.setDeleteStatus(2));
				this.newsService.saveAllNews(news);
			}			
			
			//deleting history
			this.historyRepository.delete(history);
			
		}
	}
	
	private void restoreCategory(History history,User user) {
		Category category=this.categoryService.findByIdAndDeleteStatus(history.getTypeId(),1);
		if(category!=null) {
			category.setDeleteStatus(2);
			this.categoryService.saveCategory(category, user);
			
			List<SiteMapUrl> siteMap=new ArrayList<>();
			//updating blogs
			List<Blogs> blogs=category.getBlogs();
			
			if(!blogs.isEmpty()) {
				for (Blogs b : blogs) {
					b.setDeleteStatus(2);					
					
					//updating sitemap url
					SiteMapUrl siteMap1 = this.siteMapService.findByTypeAndUid("Blog", b.getId());
					if(siteMap!=null) {
						siteMap.add(siteMap1);
					}	
					
				}
				this.blogService.saveAllBlogs(blogs);							
			}
			
			
			//updating services
			List<Services> services=category.getServices();
			List<HotTags> hotTags=new ArrayList<>();
			List<Orders> orders=new ArrayList<>();
			List<FooterService> footerServices=new ArrayList<>();
			if(!services.isEmpty()) {						
				for (Services s : services) {
					s.setDeleteStatus(2);
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
					siteMap.add(siteMap1);
				}
				if(!hotTags.isEmpty()) {
					hotTags.forEach(ht->ht.setDeleteStatus(2));
					this.hotTagService.saveTags(hotTags);
				}
				if(!orders.isEmpty()) {
					orders.forEach(ord->ord.setDeleteStatus(2));
					this.orderService.saveOrders(orders);
				}
				this.servicesService.saveAllServices(services);
			}
			//updating lawUpdate
			List<LawUpdate> lawUpdate=category.getLawUpdate();
			if(!lawUpdate.isEmpty()) {
				lawUpdate.forEach(l->l.setDeleteStatus(2));					
				this.lawUpdateService.saveAllLawUpdate(lawUpdate);
			}
			
			if(!siteMap.isEmpty()) {
				siteMap.forEach(sm->sm.setStatus(12));
				this.siteMapService.saveSiteMap(siteMap);
			}
			
			//deleting history
			this.historyRepository.delete(history);
			
		}
	}

	public List<History> findByDateBefore(String today) {
		// TODO Auto-generated method stub
		return this.historyRepository.findByDateBefore(today);
	}

	
	
}
