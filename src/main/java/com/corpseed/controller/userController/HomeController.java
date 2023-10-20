package com.corpseed.controller.userController;

import com.corpseed.config.RestTemplateService;
import com.corpseed.entity.*;
import com.corpseed.entity.couponentity.Coupon;
import com.corpseed.entity.couponentity.CouponServices;
import com.corpseed.entity.enquiryentity.Enquiry;
import com.corpseed.entity.hrmentity.JobApplication;
import com.corpseed.entity.hrmentity.Jobs;
import com.corpseed.entity.industryentity.Industry;
import com.corpseed.entity.newsentity.News;
import com.corpseed.entity.newsentity.NewsCategory;
import com.corpseed.entity.orderentity.Orders;
import com.corpseed.entity.schedulerentity.EmailScheduler;
import com.corpseed.entity.serviceentity.*;
import com.corpseed.entity.blogentity.BlogFaq;
import com.corpseed.entity.blogentity.Blogs;
import com.corpseed.entity.hrmentity.HrmBlog;
import com.corpseed.service.hrmservice.HrmBlogService;
import com.corpseed.service.newsservice.NewsCategoryService;
import com.corpseed.service.newsservice.NewsService;
import com.corpseed.service.pressservice.PressService;
import com.corpseed.service.servicesservice.*;
import com.corpseed.serviceImpl.blogserviceimpl.BlogService;
import com.corpseed.serviceImpl.blogserviceimpl.BlogServiceCardService;
import com.corpseed.serviceImpl.couponserviceimpl.CouponService;
import com.corpseed.serviceImpl.couponserviceimpl.CouponServicesService;
import com.corpseed.serviceImpl.enqserviceimpl.EnquiryService;
import com.corpseed.serviceImpl.hrmserviceimpl.JobApplicationServices;
import com.corpseed.serviceImpl.hrmserviceimpl.JobServices;
import com.corpseed.serviceImpl.orderserviceimpl.OrdersService;
import com.corpseed.serviceImpl.servicesserviceimpl.ServicesService;
import com.corpseed.util.EmailSender;
import com.corpseed.util.Helper;
import com.corpseed.util.Message;
import com.corpseed.serviceImpl.*;
import com.corpseed.service.*;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import eu.bitwalker.useragentutils.Browser;
import eu.bitwalker.useragentutils.OperatingSystem;
import eu.bitwalker.useragentutils.UserAgent;
import eu.bitwalker.useragentutils.Version;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.io.PrintWriter;
import java.text.DecimalFormat;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Controller
public class HomeController {

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private ServicesService servicesService;

    @Autowired
    private CommonServices commonService;

    @Autowired
    private EnquiryService enquiryService;

    @Autowired
    private MasterService masterService;

    @Autowired
    private BlogService blogService;

    @Autowired
    private MeetingService meetingService;

    @Autowired
    private JobServices jobService;

    @Autowired
    private JobApplicationServices jobAppService;

    @Autowired
    private IndustryService industryService;

    @Autowired
    AzureBlobAdapter azureAdapter;

    @Autowired
    private OrdersService orderService;

    @Autowired
    private TransactionService txsService;

    @Autowired
    private LawUpdateService lawUpdateService;

    @Autowired
    private SubscriberService subscribeService;

    @Autowired
    private CouponService couponService;

    @Autowired
    private CouponServicesService couponServicesService;

    @Autowired
    private HotTagService hotTagService;

    @Autowired
    private VisitorsService visitorsService;

    @Autowired
    private EmailSender emailSender;

    @Autowired
    private ForgetPasswordService forgetPasswordService;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Autowired
    private TrandingSearchService trandingService;

    @Autowired
    private StaticSeoService staticSeoService;

    @Autowired
    private Environment env;

    @Autowired
    private UserServices userService;

    @Autowired
    private EmailSchedulerService emailScheduler;

    @Autowired
    private ServiceBrand serviceBrand;

    @Autowired
    private BlogFaqService blogFaqService;

    @Autowired
    private TestimonialService testimonialService;

    @Autowired
    private NewsCategoryService newsCategoryService;

    @Autowired
    private NewsService newsService;

    @Autowired
    private FeedbackService feedbackService;

    @Autowired
    private OTPService otpService;

    @Autowired
    private SubServiceService subServiceService;

    @Autowired
    private ServiceContactService serviceContactService;

    @Autowired
    private BlogServiceCardService blogServiceCardService;

    @Autowired
    private PressService pressService;

    @Autowired
    private ServiceStateService serviceStateService;

    @Autowired
    private ServiceCityService serviceCityService;

    @Autowired
    private StateCityMapService stateCityMapService;
    
	   @Autowired
	   RestTemplateService restTemplateService;

    @Value("${domain.name}")
    private String domain;

    @GetMapping("/")
    public String home(Model model) {
        StaticSeo staticSeo = this.staticSeoService.findByPage("home");
        if (staticSeo != null) {
            model.addAttribute("title", staticSeo.getMetaTitle());
            model.addAttribute("metaDescription", staticSeo.getMetaDescription());
            model.addAttribute("metaKeyword", staticSeo.getMetaKeyword());
        }
        model.addAttribute("blogs",this.blogService.findTop5ByDisplayStatusAndDeleteStatusOrderByIdDesc("1",2));
        model.addAttribute("reviews", this.masterService.findByShowHomeStatusAndDisplayStatus("1", "1"));
        model.addAttribute("hotTags", this.hotTagService.findTop4ByDisplayStatus("1"));
        model.addAttribute("cardCategory", this.categoryService.findTop4ByShowHomeStatusAndDisplayStatus("1", "1"));
        model.addAttribute("pressRelease",this.pressService.findTop5ByDisplayStatusAndDeleteStatusOrderByIdDesc("1",2));
        return "users/index";
    }

    @GetMapping("/about-us")
    public String aboutUs(Model model) {
        StaticSeo staticSeo = this.staticSeoService.findByPage("about-us");
        if (staticSeo != null) {
            model.addAttribute("title", staticSeo.getMetaTitle());
            model.addAttribute("metaDescription", staticSeo.getMetaDescription());
            model.addAttribute("metaKeyword", staticSeo.getMetaKeyword());
        }
        return "users/about";
    }

    @GetMapping("/contact-us")
    public String contactUs(Model model) {
        StaticSeo staticSeo = this.staticSeoService.findByPage("contact-us");
        if (staticSeo != null) {
            model.addAttribute("title", staticSeo.getMetaTitle());
            model.addAttribute("metaDescription", staticSeo.getMetaDescription());
            model.addAttribute("metaKeyword", staticSeo.getMetaKeyword());
        }
        model.addAttribute("address", this.masterService.findContactByStatus("1"));
        return "users/contact";
    }

    @GetMapping("/industry/{slug}")
    public String industry(@PathVariable("slug") String slug, Model model) {

        Industry industryBySlug = this.industryService.getIndustryBySlug(slug);

        if (industryBySlug == null)
            return "error/404";

        model.addAttribute("title", industryBySlug.getMetaTitle());
        model.addAttribute("metaDescription", industryBySlug.getMetaDescription());
        model.addAttribute("metaKeyword", industryBySlug.getMetaKeyword());
        model.addAttribute("industry", industryBySlug);

        String str[]= Helper.findFirstAndLastName(industryBySlug.getTitle());
        model.addAttribute("industryFirstName",str[0]);
        model.addAttribute("industryLastName",str[1]);

        return "users/industry";
    }

    @PostMapping("/enquiry/consult-now")
    @ResponseBody
    public String saveConsultNowEnquiry(@RequestParam("otp") String otp, @RequestParam("name") String name, @RequestParam("email") String email,
                                        @RequestParam("mobile") String mobile, @RequestParam("city") String city,
                                        @RequestParam("location") String location, @RequestParam("postDate") String postdate,
                                        @RequestParam("modifyDate") String modifydate, @RequestParam("message") String message,
                                        @RequestParam("CategoryId") String CategoryId, HttpServletRequest req) {

        boolean matches = mobile.substring(mobile.length() - 10).matches("[6-9][0-9]{9}");
        //validate otp again
        boolean validOtp = otpService.isValidOtp(mobile, otp);
        if (!matches || !validOtp)
            return "fail";

        try {
            Enquiry enquiry = new Enquiry();

            enquiry.setName(name);
            enquiry.setEmail(email);
            enquiry.setMobile(mobile);
            enquiry.setCity(city);
            enquiry.setPostDate(postdate);
            enquiry.setModifyDate(modifydate);
            enquiry.setUuid(this.commonService.getUUID());
            enquiry.setType("Consult-Now");
            enquiry.setMessage(message);
            enquiry.setServiceId("NA");
            enquiry.setUrl(location);
            enquiry.setIpAddress(this.commonService.getIpAddress(req));
            enquiry.setDisplayStatus("1");
            enquiry.setBitrixStatus(2);
            enquiry.setIndustryId("NA");
            enquiry.setCategoryId(CategoryId);
            Enquiry saveEnquiry = null;

            boolean isValidRequest = enquiryService.isValidRequest(mobile, email);
            if (!isValidRequest)
                saveEnquiry = this.enquiryService.saveEnquiry(enquiry);

            if (saveEnquiry == null) {
                return ("fail");
            } else {
                return ("pass");
            }

        } catch (Exception e) {
            e.printStackTrace();
            return ("fail");
        }
    }

    @PostMapping("/enquiry/industry")
    @ResponseBody
    public String saveIndustryEnquiry(@RequestParam("otp") String otp, @RequestParam("name") String name, @RequestParam("email") String email, @RequestParam("mobile") String mobile,
                                      @RequestParam("city") String city, @RequestParam("pageUrl") String pageurl, @RequestParam("postDate") String postdate,
                                      @RequestParam("modifyDate") String modifydate,
                                      @RequestParam("message") String message, @RequestParam("industryId") String industryId,
                                      HttpServletRequest req) {

        boolean matches = mobile.substring(mobile.length() - 10).matches("[6-9][0-9]{9}");
        //validate otp again
        boolean validOtp = otpService.isValidOtp(mobile, otp);
        if (!matches || !validOtp)
            return "fail";

        try {
            Enquiry enquiry = new Enquiry();

            enquiry.setName(name);
            enquiry.setEmail(email);
            enquiry.setMobile(mobile);
            enquiry.setCity(city);
            enquiry.setPostDate(postdate);
            enquiry.setModifyDate(modifydate);
            enquiry.setUuid(this.commonService.getUUID());
            enquiry.setType("Industry");
            enquiry.setMessage(message);
            enquiry.setServiceId("NA");
            enquiry.setUrl(pageurl);
            enquiry.setIpAddress(this.commonService.getIpAddress(req));
            enquiry.setDisplayStatus("1");
            enquiry.setBitrixStatus(2);
            enquiry.setIndustryId(industryId);
            enquiry.setCategoryId("NA");

            Enquiry saveEnquiry = null;
            boolean isValidRequest = enquiryService.isValidRequest(mobile, email);
            if (!isValidRequest)
                saveEnquiry = this.enquiryService.saveEnquiry(enquiry);

            if (saveEnquiry == null) {
                return ("fail");
            } else {
                return ("pass");
            }

        } catch (Exception e) {
            e.printStackTrace();
            return ("fail");
        }
    }

    @GetMapping("/book-meeting")
    public String bookMetting(Model model) {
        StaticSeo staticSeo = this.staticSeoService.findByPage("book-meeting");
        if (staticSeo != null) {
            model.addAttribute("title", staticSeo.getMetaTitle());
            model.addAttribute("metaDescription", staticSeo.getMetaDescription());
            model.addAttribute("metaKeyword", staticSeo.getMetaKeyword());
        }
        model.addAttribute("meeting", new Meeting());
        return "users/book-metting";
    }

    @PostMapping("/book-meeting")
    public String bookMettingSave(@Valid @ModelAttribute("meeting") Meeting meeting,
                                  BindingResult result, @RequestParam("otp") String otp, Model model, HttpSession session) {

        StaticSeo staticSeo = this.staticSeoService.findByPage("book-meeting");
        if (staticSeo != null) {
            model.addAttribute("title", staticSeo.getMetaTitle());
            model.addAttribute("metaDescription", staticSeo.getMetaDescription());
            model.addAttribute("metaKeyword", staticSeo.getMetaKeyword());
        }

        try {
            if (result.hasErrors()) {
                model.addAttribute("meeting", meeting);
                session.setAttribute("message", new Message("Please fill out the blank fields !!", "alert-danger"));
                return "users/book-metting";
            }

            boolean matches = meeting.getMobile().substring(meeting.getMobile().length() - 10).matches("[6-9][0-9]{9}");
            //validate otp again
            boolean validOtp = otpService.isValidOtp(meeting.getMobile(), otp);

            if (!matches || !validOtp) {
                model.addAttribute("meeting", meeting);
                session.setAttribute("message", new Message("Either Invalid Mobile Number OR Invalid OTP!!", "alert-danger"));
                return "users/book-meeting";
            }

            meeting.setUuid(this.commonService.getUUID());
            Meeting saveMeeting = null;
            boolean isValidRequest = enquiryService.isValidRequest(meeting.getMobile(), meeting.getEmail());
            if (!isValidRequest)
                saveMeeting = this.meetingService.bookMeeting(meeting);

            if (saveMeeting == null) {
                model.addAttribute("meeting", meeting);
                session.setAttribute("message", new Message("Data Not Saved, Please try-again later !!", "alert-danger"));
                return "users/book-metting";
            } else {

                String msg = "<p>Full Name : " + saveMeeting.getName()
                        + "<br/>Email Address : " + saveMeeting.getEmail()
                        + "<br/>Phone : " + saveMeeting.getMobile()
                        + "<br/>City : " + saveMeeting.getCity()
                        + "<br/>Meeting Date-Time : " + saveMeeting.getDateTime()
                        + "</p><p>Message : <br/>" + saveMeeting.getMessage() + "</p>";
                this.emailSender.sendmail("info@corpseed.com", "empty", "Book meeting", msg, "NA");

                int enqCount = this.meetingService.findByEmailOrMobile(meeting.getEmail(), meeting.getMobile());
                String title = "";
                if (enqCount > 1) title = "(Duplicate-" + (enqCount - 1) + ") - ";
                title += "Meeting on " + meeting.getDateTime();
                String POST_URL = env.getProperty("Bitrix24") + "crm.lead.add.json";
                String POST_PARAMS = "FIELDS[TITLE]=" + title + "&FIELDS[NAME]=" + meeting.getName() + "&FIELDS[EMAIL][0][VALUE]=" + meeting.getEmail() + "&FIELDS[PHONE][0][VALUE]=" + meeting.getMobile() + "&FIELDS[COMMENTS]=" + meeting.getMessage();
                this.commonService.callPostURL(POST_URL, POST_PARAMS);

                model.addAttribute("meeting", new Meeting());
                session.setAttribute("message", new Message("Your Meeting request has been received for " + meeting.getDateTime() + ", Our legal advisor will contact you shortly..!!", "alert-success"));
                return "users/book-metting";
            }

        } catch (Exception e) {
            e.printStackTrace();
            model.addAttribute("meeting", meeting);
            session.setAttribute("message", new Message("Something went wrong, Please try-again later !!", "alert-danger"));
            return "users/book-metting";
        }
    }

    @GetMapping("/law-update/{slug}")
    public String lawUpdateOpen(@PathVariable("slug") String slug, Model model) {
        LawUpdate lawUpdate = this.lawUpdateService.getLawUpdateBySlug(slug);
        if (lawUpdate != null) {
            model.addAttribute("title", lawUpdate.getMetaTitle());
            model.addAttribute("metaDescription", lawUpdate.getMetaDescription());
            model.addAttribute("metaKeyword", lawUpdate.getMetaKeyword());
            model.addAttribute("lawupdate", lawUpdate);
            return "users/law-detail";
        } else {
            return "error/404";
        }

    }

    @PostMapping("/law-update/search")
    public String lawUpdateSearch(@RequestParam("fromDate") String fromDate, @RequestParam("toDate") String toDate,
                                  @RequestParam("department") String department, Model model, @RequestParam("page") Optional<Integer> page,
                                  @RequestParam("size") Optional<Integer> size) {
        int currentPage = page.orElse(1);
        int pageSize = size.orElse(6);


        StaticSeo staticSeo = this.staticSeoService.findByPage("law-update");
        if (staticSeo != null) {
            model.addAttribute("title", staticSeo.getMetaTitle());
            model.addAttribute("metaDescription", staticSeo.getMetaDescription());
            model.addAttribute("metaKeyword", staticSeo.getMetaKeyword());
        }

        if (fromDate != null && toDate != null && department != null && fromDate.length() > 0 && toDate.length() > 0 && department.length() > 0) {
            Pageable pageable = PageRequest.of(currentPage - 1, pageSize, Sort.by("id").descending());
            Page<LawUpdate> lawUpdateByDateAndDepartment = this.lawUpdateService.getLawUpdateByDateAndDepartment(fromDate, toDate, department, pageable);
            model.addAttribute("lawupdate", lawUpdateByDateAndDepartment);

            model.addAttribute("lawFromDate", fromDate);
            model.addAttribute("lawToDate", toDate);
            model.addAttribute("lawDepartment", department);
            model.addAttribute("cpage", currentPage);
            model.addAttribute("lawupdate", lawUpdateByDateAndDepartment);
            model.addAttribute("dataFilter", true);

            int totalPages = lawUpdateByDateAndDepartment.getTotalPages();
            int startRange = currentPage - 2;
            int endRange = currentPage + 2;

            if (totalPages > 1) {
                if ((endRange - 2) == totalPages) startRange = currentPage - 4;
                if (startRange == currentPage) endRange = currentPage + 4;
                if (startRange < 1) {
                    startRange = 1;
                    endRange = startRange + 4;
                }
                if (endRange > totalPages) {
                    endRange = totalPages;
                    startRange = endRange - 4;
                }
                if (startRange < 1) startRange = 1;

                List<Integer> pageNumbers = IntStream.rangeClosed(startRange, endRange)
                        .boxed()
                        .collect(Collectors.toList());
                model.addAttribute("pageNumbers", pageNumbers);
            }
            model.addAttribute("totalPages", totalPages);

            return "users/law-updates";
        } else {
            return "error/404";
        }
    }

    @GetMapping("/law-update")
    public String lawUpdate(Model model, @RequestParam("page") Optional<Integer> page,
                            @RequestParam("size") Optional<Integer> size, @RequestParam("from") Optional<String> from,
                            @RequestParam("to") Optional<String> to, @RequestParam("dept") Optional<String> dept) {
        int currentPage = page.orElse(1);
        int pageSize = size.orElse(6);
        String fromDate = from.orElse("NA");
        String toDate = to.orElse("NA");
        String department = dept.orElse("NA");

        StaticSeo staticSeo = this.staticSeoService.findByPage("law-update");
        if (staticSeo != null) {
            model.addAttribute("title", staticSeo.getMetaTitle());
            model.addAttribute("metaDescription", staticSeo.getMetaDescription());
            model.addAttribute("metaKeyword", staticSeo.getMetaKeyword());
        }
        Pageable pageable = PageRequest.of((currentPage - 1), pageSize, Sort.by("id").descending());

        Page<LawUpdate> lawUpdateByStatus = null;
        if (!fromDate.equalsIgnoreCase("NA") && !toDate.equalsIgnoreCase("NA")) {
            lawUpdateByStatus = this.lawUpdateService.getLawUpdateBySearch("1", fromDate, toDate, department, pageable);
            model.addAttribute("lawFromDate", fromDate);
            model.addAttribute("lawToDate", toDate);
            model.addAttribute("lawDepartment", department);
            model.addAttribute("dataFilter", true);
        } else
            lawUpdateByStatus = this.lawUpdateService.getLawUpdateByStatus("1", pageable);

        model.addAttribute("cpage", currentPage);

        model.addAttribute("lawupdate", lawUpdateByStatus);

        int totalPages = lawUpdateByStatus.getTotalPages();
        int startRange = currentPage - 2;
        int endRange = currentPage + 2;

        if (totalPages > 1) {
            if ((endRange - 2) == totalPages) startRange = currentPage - 4;
            if (startRange == currentPage) endRange = currentPage + 4;
            if (startRange < 1) {
                startRange = 1;
                endRange = startRange + 4;
            }
            if (endRange > totalPages) {
                endRange = totalPages;
                startRange = endRange - 4;
            }
            if (startRange < 1) startRange = 1;

            List<Integer> pageNumbers = IntStream.rangeClosed(startRange, endRange)
                    .boxed()
                    .collect(Collectors.toList());
            model.addAttribute("pageNumbers", pageNumbers);
        }
        model.addAttribute("totalPages", totalPages);

        return "users/law-updates";
    }

    @GetMapping({"/knowledge-centre", "/knowledge-centre/author/{userSlug}"})
    public String blogs(Model model, @RequestParam("page") Optional<Integer> page,
                        @PathVariable("userSlug") Optional<String> userSlug,
                        @RequestParam("size") Optional<Integer> size,
                        @RequestParam("filter") Optional<String> filter,
                        HttpServletRequest req) {

        int currentPage = page.orElse(1);
        int pageSize = size.orElse(6);
        String slug = filter.orElse("NA");
        String uSlug = userSlug.orElse("NA");
        Category category = null;
        User user = null;
        if (!uSlug.equalsIgnoreCase("NA"))
            user = this.userService.findUserBySlug(uSlug);

        if (!slug.equalsIgnoreCase("NA")) {
            category = this.categoryService.findBySlug(slug);
        }

        StaticSeo staticSeo = this.staticSeoService.findByPage("knowledge-centre");
        if (staticSeo != null) {
            model.addAttribute("title", staticSeo.getMetaTitle());
            model.addAttribute("metaDescription", staticSeo.getMetaDescription());
            model.addAttribute("metaKeyword", staticSeo.getMetaKeyword());
        }
        Page<Blogs> allBlogsByStatus = null;
        Pageable pageable = PageRequest.of(currentPage - 1, pageSize, Sort.by("id").descending());
        if (category == null) {
            if (user != null)
                allBlogsByStatus = this.blogService.findBlogsByStatusAndUserUuid(pageable, user.getUuid());
            else
                allBlogsByStatus = this.blogService.getAllBlogsByStatus(pageable);
        } else {
            if (user != null)
                allBlogsByStatus = this.blogService.findBlogsByCategoryAndStatusAndUserUuid(category, pageable, user.getUuid());
            else
                allBlogsByStatus = this.blogService.getAllBlogsByCategoryAndStatus(category, pageable);
        }

        model.addAttribute("cpage", currentPage);
        model.addAttribute("blogs", allBlogsByStatus);

        int totalPages = allBlogsByStatus.getTotalPages();
        int startRange = currentPage - 2;
        int endRange = currentPage + 2;

        if (totalPages > 1) {
            if ((endRange - 2) == totalPages) startRange = currentPage - 4;
            if (startRange == currentPage) endRange = currentPage + 4;
            if (startRange < 1) {
                startRange = 1;
                endRange = startRange + 4;
            }
            if (endRange > totalPages) {
                endRange = totalPages;
                startRange = endRange - 4;
            }
            if (startRange < 1) startRange = 1;

            List<Integer> pageNumbers = IntStream.rangeClosed(startRange, endRange)
                    .boxed()
                    .collect(Collectors.toList());
            model.addAttribute("pageNumbers", pageNumbers);
        }
        model.addAttribute("url", req.getRequestURI());
        model.addAttribute("topCategory", this.categoryService.findTop10ByVisited());
        model.addAttribute("totalPages", totalPages);
        model.addAttribute("hotTags", this.hotTagService.findTop4ByDisplayStatus("1"));
        model.addAttribute("topBlogs", this.blogService.findTop5ByDisplayStatusAndDeleteStatusOrderByVisitedDesc("1", 2));
        return "users/blogs";
    }



    @GetMapping("/profile/{userSlug}")
    public String blogContact(@PathVariable("userSlug") String userSlug, Model model) {
        User user = userService.findUserBySlug(userSlug);
        if (user == null) return "error/404";

        model.addAttribute("articles", this.blogService.findTop5ByDisplayStatusAndDeleteStatusAndPostedByUuidOrderByIdDesc("1", 2, user.getUuid()));
        model.addAttribute("title", user.getMetaTitle());
        model.addAttribute("metaDescription", user.getMetaDescription());
        model.addAttribute("metaKeyword", user.getMetaKeyword());
        model.addAttribute("user", user);

        return "users/service-contact";
    }

    @GetMapping("/knowledge-centre/{slug}")
    public String blogDetails(@PathVariable("slug") String slug, Model model, HttpServletRequest req) {
        Blogs blog = this.blogService.findBySlug(slug);
        if (blog == null)
            return "error/404";

        String jsonString = "{'@context': 'https://schema.org','@type': 'FAQPage','mainEntity': [";
        List<BlogFaq> allBlogFaq = this.blogFaqService.allBlogFaq(blog);
        int size = allBlogFaq.size();
        for (BlogFaq sf : allBlogFaq) {
            size--;
            jsonString += "{'@type':'Question','name':'" + sf.getTitle().replace("'", "") + "','acceptedAnswer':{'@type':'Answer','text':'" + sf.getDescription().replace("'", "") + "'}}";
            if (size > 0) jsonString += ",";
        }
        jsonString += "]}";

        JsonObject jsonObject = (JsonObject) JsonParser.parseString(jsonString);

        model.addAttribute("faqs1", jsonObject);

        model.addAttribute("faqs", allBlogFaq);
        model.addAttribute("title", blog.getMetaTitle());
        model.addAttribute("metaDescription", blog.getMetaDescription());
        model.addAttribute("metaKeyword", blog.getMetaKeyword());
        model.addAttribute("blogs", blog);
        model.addAttribute("blogUser", this.userService.getUserByUuid(blog.getPostedByUuid()));
        model.addAttribute("legalBlog", this.blogService.findTop8ByCategoryAndDisplayStatusAndUuidNotOrderByIdDesc(blog.getCategory(), "1", blog.getUuid()));
        model.addAttribute("feedback", this.feedbackService.findByIpAndUrl(this.commonService.getIpAddress(req), domain + "/knowledge-centre/" + slug));
        model.addAttribute("blogServiceCardLists", this.blogServiceCardService.findBlogServiceCardByBlog(blog));

        model.addAttribute("topNews", this.newsService.findTop5ByDisplayStatusAndDeleteStatusOrderByVisitedDesc("1", 2));
        model.addAttribute("latestNews", this.newsService.findTop5ByDisplayStatusAndDeleteStatusOrderByIdDesc("1", 2));
        model.addAttribute("topBlog", this.blogService.findTop5ByDisplayStatusAndDeleteStatusOrderByVisitedDesc("1", 2));
        model.addAttribute("latestBlog", this.blogService.findTop5ByDisplayStatusAndDeleteStatusOrderByIdDesc("1", 2));

        return "users/blog-detail";

    }

    @PostMapping("/enquiry/contact-us")
    @ResponseBody
    public String saveContactUsEnquiry(@RequestParam("otp") String otp, @RequestParam("name") String name, @RequestParam("email") String email, @RequestParam("mobile") String mobile,
                                       @RequestParam("city") String city, @RequestParam("message") String message, @RequestParam("postDate") String postdate,
                                       @RequestParam("modifyDate") String modifydate, @RequestParam("location") String location
            , HttpServletRequest req) {

        boolean matches = mobile.substring(mobile.length() - 10).matches("[6-9][0-9]{9}");
        //validate otp again
        boolean validOtp = otpService.isValidOtp(mobile, otp);
//		System.out.println(mobile+"#"+matches+"#"+validOtp);
        if (!matches || !validOtp)
            return "fail";

        try {
            Enquiry enquiry = new Enquiry();

            enquiry.setName(name);
            enquiry.setEmail(email);
            enquiry.setMobile(mobile);
            enquiry.setCity(city);
            enquiry.setPostDate(postdate);
            enquiry.setModifyDate(modifydate);
            enquiry.setUuid(this.commonService.getUUID());
            enquiry.setType("Contact-Us");
            enquiry.setMessage(message);
            enquiry.setServiceId("NA");
            enquiry.setUrl(location);
            enquiry.setIpAddress(this.commonService.getIpAddress(req));
            enquiry.setDisplayStatus("1");
            enquiry.setBitrixStatus(1);
            enquiry.setCategoryId("NA");
            enquiry.setIndustryId("NA");

            Enquiry saveEnquiry = null;
            boolean isValidRequest = enquiryService.isValidRequest(mobile, email);
            if (!isValidRequest)
                saveEnquiry = this.enquiryService.saveEnquiry(enquiry);

            if (saveEnquiry == null) {
                return ("fail");
            } else {

                String msg = "<p>Full Name : " + saveEnquiry.getName()
                        + "<br/>Email Address : " + saveEnquiry.getEmail()
                        + "<br/>Phone : " + saveEnquiry.getMobile()
                        + "<br/>City : " + saveEnquiry.getCity() + "</p>"
                        + "<p>Message : <br/>" + saveEnquiry.getMessage() + "</p>";
                this.emailSender.sendmail("info@corpseed.com", "empty", "Contact Us", msg, "NA");

                int enqCount = this.enquiryService.findByEmailOrMobile(email, mobile);
                String title = "";
                if (enqCount > 1) title = "(Duplicate-" + (enqCount - 1) + ") - ";
                title += "Contact Us ";
                String POST_URL = env.getProperty("Bitrix24") + "crm.lead.add.json";
                String POST_PARAMS = "FIELDS[TITLE]=" + title + "&FIELDS[NAME]=" + name + "&FIELDS[EMAIL][0][VALUE]=" + email + "&FIELDS[PHONE][0][VALUE]=" + mobile + "&FIELDS[COMMENTS]=" + message;
                this.commonService.callPostURL(POST_URL, POST_PARAMS);

                return ("pass");

            }

        } catch (Exception e) {
            e.printStackTrace();
            return ("fail");
        }
    }

    @PostMapping("/enquiry/saveblogenquiry")
    @ResponseBody
    public String saveBlogEnquiry(@RequestParam("otp") String otp, @RequestParam("name") String name, @RequestParam("email") String email, @RequestParam("mobile") String mobile,
                                  @RequestParam("message") String message, @RequestParam("pageurl") String pageurl, @RequestParam("postDate") String postdate,
                                  @RequestParam("modifyDate") String modifydate, @RequestParam("CategoryId") String CategoryId
            , HttpServletRequest req) {

        boolean matches = mobile.substring(mobile.length() - 10).matches("[6-9][0-9]{9}");
        //validate otp again
        boolean validOtp = otpService.isValidOtp(mobile, otp);
        if (!matches || !validOtp)
            return "fail";

        try {
            Enquiry enquiry = new Enquiry();

            enquiry.setName(name);
            enquiry.setEmail(email);
            enquiry.setMobile(mobile);
            enquiry.setCity("NA");
            enquiry.setPostDate(postdate);
            enquiry.setModifyDate(modifydate);
            enquiry.setUuid(this.commonService.getUUID());
            enquiry.setType("knowledge-centre");
            enquiry.setMessage(message);
            enquiry.setServiceId("NA");
            enquiry.setCategoryId(CategoryId);
            enquiry.setIndustryId("NA");
            enquiry.setUrl(pageurl);
            enquiry.setIpAddress(this.commonService.getIpAddress(req));
            enquiry.setDisplayStatus("1");
            enquiry.setBitrixStatus(2);

            Enquiry saveEnquiry = null;
            boolean isValidRequest = enquiryService.isValidRequest(mobile, email);
            if (!isValidRequest)
                saveEnquiry = this.enquiryService.saveEnquiry(enquiry);

            if (saveEnquiry == null) {
                return ("fail");
            } else {
                return ("pass");
            }

        } catch (Exception e) {
            e.printStackTrace();
            return ("fail");
        }
    }

    @PostMapping("/enquiry/savepartnerenquiry")
    @ResponseBody
    public String savePartnerEnquiry(@RequestParam("otp") String otp, @RequestParam("name") String name, @RequestParam("email") String email, @RequestParam("mobile") String mobile,
                                     @RequestParam("message") String message, @RequestParam("postDate") String postdate, @RequestParam("modifyDate") String modifydate,
                                     @RequestParam("location") String location, HttpServletRequest req) {

        boolean matches = mobile.substring(mobile.length() - 10).matches("[6-9][0-9]{9}");
        //validate otp again
        boolean validOtp = otpService.isValidOtp(mobile, otp);
        if (!matches || !validOtp)
            return "fail";

        try {
            Enquiry enquiry = new Enquiry();

            enquiry.setName(name);
            enquiry.setEmail(email);
            enquiry.setMobile(mobile);
            enquiry.setCity("NA");
            enquiry.setPostDate(postdate);
            enquiry.setModifyDate(modifydate);
            enquiry.setUuid(this.commonService.getUUID());
            enquiry.setType("Partner");
            enquiry.setMessage(message);
            enquiry.setServiceId("NA");
            enquiry.setCategoryId("NA");
            enquiry.setIndustryId("NA");
            enquiry.setUrl(location);
            enquiry.setIpAddress(this.commonService.getIpAddress(req));
            enquiry.setDisplayStatus("1");
            enquiry.setBitrixStatus(2);

            Enquiry saveEnquiry = null;
            boolean isValidRequest = enquiryService.isValidRequest(mobile, email);
            if (!isValidRequest)
                saveEnquiry = this.enquiryService.saveEnquiry(enquiry);

            if (saveEnquiry == null) {
                return ("fail");
            } else {
                String msg = "<p>Full Name : " + saveEnquiry.getName()
                        + "<br/>Email Address : " + saveEnquiry.getEmail()
                        + "<br/>Phone : " + saveEnquiry.getMobile() + "</p>"
                        + "<p>Message : <br/>" + saveEnquiry.getMessage() + "</p>";
                this.emailSender.sendmail("info@corpseed.com", "empty", "PARTNER WITH CORPSEED", msg, "NA");

                return ("pass");

            }

        } catch (Exception e) {
            e.printStackTrace();
            return ("fail");
        }
    }

    @PostMapping("/enquiry/callback")
    @ResponseBody
    public String savecallBackEnquiry(@RequestParam("otp") String otp, @RequestParam("name") String name, @RequestParam("email") String email,
                                      @RequestParam("mobile") String mobile, @RequestParam("location") String location,
                                      @RequestParam("page") String page, @RequestParam("whatsApp") int whatsApp, HttpServletRequest req) {

        boolean matches = mobile.substring(mobile.length() - 10).matches("[6-9][0-9]{9}");
        //validate otp again
        boolean validOtp = otpService.isValidOtp(mobile, otp);
//System.out.println(mobile+"#"+matches+"#"+validOtp);
        if (!matches || !validOtp)
            return "fail";

        try {
            Enquiry enquiry = new Enquiry();
            String today = this.commonService.getToday();
            enquiry.setName(name);
            enquiry.setEmail(email);
            enquiry.setMobile(mobile.trim());
            enquiry.setCity("NA");
            enquiry.setPostDate(today);
            enquiry.setModifyDate(today);
            enquiry.setUuid(this.commonService.getUUID());
            enquiry.setType("Call Back");
            enquiry.setMessage("Call Back :- " + page);
            enquiry.setServiceId("NA");
            enquiry.setCategoryId("NA");
            enquiry.setIndustryId("NA");
            enquiry.setUrl(location);
            enquiry.setIpAddress(this.commonService.getIpAddress(req));
            enquiry.setDisplayStatus("1");
            enquiry.setBitrixStatus(2);
            enquiry.setWhatsAppStatus(whatsApp);

            Enquiry saveEnquiry = null;
            boolean isValidRequest = enquiryService.isValidRequest(mobile, email);
            if (!isValidRequest)
                saveEnquiry = this.enquiryService.saveEnquiry(enquiry);

//		System.out.println("saveEnquiry=="+saveEnquiry);

            if (saveEnquiry == null) {
                return ("fail");
            } else {
                return ("pass");

            }
        } catch (Exception e) {
            e.printStackTrace();
            return ("fail");
        }
    }

    @PostMapping("/enquiry/service-details")
    @ResponseBody
    public String saveServiceDetailsEnquiry(@RequestParam("otp") String otp, @RequestParam("uuid") String uuid, @RequestParam("name") String name, @RequestParam("mobile") String mobile, @RequestParam("location") String location,
                                            HttpServletRequest req) {

        boolean matches = mobile.substring(mobile.length() - 10).matches("[6-9][0-9]{9}");
        //validate otp again
        boolean validOtp = otpService.isValidOtp(mobile, otp);
        if (!matches || !validOtp)
            return "fail";

        try {
            Services service = this.servicesService.findByUUID(uuid);

            if (service != null) {
                Enquiry enquiry = new Enquiry();
                String today = this.commonService.getToday();
                enquiry.setName(name);
                enquiry.setEmail("NA");
                enquiry.setMobile(mobile);
                enquiry.setCity("NA");
                enquiry.setPostDate(today);
                enquiry.setModifyDate(today);
                enquiry.setUuid(this.commonService.getUUID());
                enquiry.setType("Service Details");
                enquiry.setMessage(service.getServiceName());
                enquiry.setServiceId(service.getId() + "");
                enquiry.setCategoryId(service.getCategory().getId() + "");
                enquiry.setIndustryId("NA");
                enquiry.setUrl(location);
                enquiry.setIpAddress(this.commonService.getIpAddress(req));
                enquiry.setDisplayStatus("1");
                enquiry.setBitrixStatus(2);

                Enquiry saveEnquiry = null;
                boolean isValidRequest = enquiryService.isValidRequest(mobile, "NA");
                if (!isValidRequest)
                    saveEnquiry = this.enquiryService.saveEnquiry(enquiry);

                if (saveEnquiry == null) {
                    return ("fail");
                } else {
                    return ("pass");
                }
            } else {
                return ("fail");
            }

        } catch (Exception e) {
            e.printStackTrace();
            return ("fail");
        }
    }

    @PostMapping("/enquiry/industry-details")
    @ResponseBody
    public String saveIndustryDetailsEnquiry(@RequestParam("otp") String otp, @RequestParam("uuid") String uuid, @RequestParam("name") String name, @RequestParam("mobile") String mobile, @RequestParam("location") String location,
                                             HttpServletRequest req) {

        boolean matches = mobile.substring(mobile.length() - 10).matches("[6-9][0-9]{9}");
        //validate otp again
        boolean validOtp = otpService.isValidOtp(mobile, otp);
        if (!matches || !validOtp)
            return "fail";

        try {
            Industry industry = this.industryService.findByUuid(uuid);

            if (industry != null) {
                Enquiry enquiry = new Enquiry();
                String today = this.commonService.getToday();
                enquiry.setName(name);
                enquiry.setEmail("NA");
                enquiry.setMobile(mobile);
                enquiry.setCity("NA");
                enquiry.setPostDate(today);
                enquiry.setModifyDate(today);
                enquiry.setUuid(this.commonService.getUUID());
                enquiry.setType("Industry Details");
                enquiry.setMessage(industry.getIndustryName());
                enquiry.setServiceId(industry.getId() + "");
                enquiry.setCategoryId("NA");
                enquiry.setIndustryId(industry.getId() + "");
                enquiry.setUrl(location);
                enquiry.setIpAddress(this.commonService.getIpAddress(req));
                enquiry.setDisplayStatus("1");
                enquiry.setBitrixStatus(2);

                Enquiry saveEnquiry = null;
                boolean isValidRequest = enquiryService.isValidRequest(mobile, "NA");
                if (!isValidRequest)
                    saveEnquiry = this.enquiryService.saveEnquiry(enquiry);

                if (saveEnquiry == null) {
                    return ("fail");
                } else {
                    return ("pass");
                }
            } else {
                return ("fail");
            }

        } catch (Exception e) {
            e.printStackTrace();
            return ("fail");
        }
    }

    @GetMapping("/partner")
    public String becomePartner(Model model) {
        StaticSeo staticSeo = this.staticSeoService.findByPage("partner");
        if (staticSeo != null) {
            model.addAttribute("title", staticSeo.getMetaTitle());
            model.addAttribute("metaDescription", staticSeo.getMetaDescription());
            model.addAttribute("metaKeyword", staticSeo.getMetaKeyword());
        }
        return "users/partner";
    }

    @GetMapping("/change-your-ca")
    public String changeYourCa(Model model) {
        StaticSeo staticSeo = this.staticSeoService.findByPage("change-your-ca");
        if (staticSeo != null) {
            model.addAttribute("title", staticSeo.getMetaTitle());
            model.addAttribute("metaDescription", staticSeo.getMetaDescription());
            model.addAttribute("metaKeyword", staticSeo.getMetaKeyword());
        }
        return "users/change-your-ca";
    }

    @PostMapping("/join-our-team/{slug}/submitjob")
    public String jobApply(@Valid @ModelAttribute("jobApp") JobApplication jobApp, BindingResult result,
                           @RequestParam("file") MultipartFile file, @RequestParam("jobId") String jobId,
                           @RequestParam("otp") String otp, Model model, HttpSession session
            , HttpServletRequest req) {
        StaticSeo staticSeo = this.staticSeoService.findByPage("join-our-team");
        if (staticSeo != null) {
            model.addAttribute("title", staticSeo.getMetaTitle());
            model.addAttribute("metaDescription", staticSeo.getMetaDescription());
            model.addAttribute("metaKeyword", staticSeo.getMetaKeyword());
        }

        Jobs job = this.jobService.getJobById(jobId);
        try {
            model.addAttribute("job", job);
            String mobile = jobApp.getMobile();
            boolean matches = mobile.substring(mobile.length() - 10).matches("[6-9][0-9]{9}");
            //validate otp again
            boolean validOtp = otpService.isValidOtp(mobile, otp);
            if (!matches || !validOtp) {
                model.addAttribute("jobApp", jobApp);
                session.setAttribute("message", new Message("Invalid mobile Or OTP !!", "alert-danger"));
                return "users/career-detail";
            }

            if (result.hasErrors()) {
                model.addAttribute("jobApp", jobApp);
                session.setAttribute("message", new Message("Please fill out the blank fields !!", "alert-danger"));
                return "users/career-detail";
            }
            JobApplication findJob = this.jobAppService.findByEmailAndJobsAndPostDateAfter(jobApp.getEmail(), job, this.commonService.dateBeforeDays(90));
            if (findJob != null) {
                model.addAttribute("jobApp", jobApp);
                session.setAttribute("message", new Message("You already applied for this position, Please try after 3 months !!", "alert-danger"));
                return "users/career-detail";
            }
            User corpseedUser = this.userService.findByEmailAndRoleNot(jobApp.getEmail(), "ROLE_USER");
            if (corpseedUser != null) {
                model.addAttribute("jobApp", jobApp);
                session.setAttribute("message", new Message("You already corpseed's employee !!", "alert-danger"));
                return "users/career-detail";
            }
            String message = "<p>Dear " + jobApp.getName() + "</p></p>Thank you for showing interest in working with Corpseed.</p><p>Please login to see application status.</p><p>Login link : <a href='" + env.getProperty("domain.name") + "/login'>www.corpseed.com</p><p>Username : " + jobApp.getEmail() + "</p><p>If you have any questions or need additional information, please don’t hesitate to contact me by email <a href='mailto:hr@corpseed.com'>hr@corpseed.com</a></p><br/><p>Thanks & Regards</p><p>HR Team</p>";

            User user = this.userService.isUserExist(jobApp.getEmail());

            if (user == null) {
                //creating candidate's user account
                String uuid = this.commonService.getUUID();
                String password = this.commonService.getUUID().substring(0, 7);

                String x[] = jobApp.getName().split(" ");
                String firstName = x[0];
                String lastName = ".";

                String jobTitle = job.getJobTitle();
                if (x.length > 1)
                    lastName = jobApp.getName().substring(firstName.length() + 1);

                user = this.userService.saveUser(new User(0, uuid, firstName, lastName, jobApp.getEmail(), this.passwordEncoder.encode(password), jobTitle, "profile.png", jobTitle, "1", "0", this.commonService.getIpAddress(req), this.commonService.getToday(), jobTitle, null, "ROLE_USER", 2, null, null, null, null, 1, "NA", firstName + " " + lastName, firstName + " " + lastName, firstName + " " + lastName));
                message = "<p>Dear " + jobApp.getName() + "</p></p>Thank you for showing interest in working with Corpseed.</p><p>Please login to see application status.</p><p>Login link : <a href='" + env.getProperty("domain.name") + "/login'>www.corpseed.com</p><p>Username : " + jobApp.getEmail() + "</p><p>Password : " + password + "</p><p>If you have any questions or need additional information, please don’t hesitate to contact me by email <a href='mailto:hr@corpseed.com'>hr@corpseed.com</a></p><br/><p>Thanks & Regards</p><p>HR Team</p>";
            }

            jobApp.setUuid(this.commonService.getUUID());
            jobApp.setJobs(job);
            jobApp.setUser(user);
            jobApp.setUploadDocStatus("2");
            jobApp.setUploadDocEmail("2");
            if (file.isEmpty()) {
                model.addAttribute("jobApp", jobApp);
                session.setAttribute("message", new Message("Please upload resume !!", "alert-danger"));
                return "users/career-detail";
            } else {
                String name = azureAdapter.upload(file, this.commonService.getUniqueCode());
                jobApp.setAttachedFile(name);
            }
            JobApplication saveJobApplication = null;
            boolean isValidRequest = enquiryService.isValidRequest(mobile, jobApp.getEmail());
            if (!isValidRequest)
                saveJobApplication = this.jobAppService.saveJobApplication(jobApp);

            if (saveJobApplication == null) {
                model.addAttribute("jobApp", jobApp);
                session.setAttribute("message", new Message("Data not saved,Please try again later !!", "alert-danger"));
                return "users/career-detail";
            } else {
                //sending login details to candidate
                if (user != null) {
                    //add to email_scheduler table
                    this.emailScheduler.saveEmail(new EmailScheduler(0, jobApp.getEmail(), "empty", "Login credential | Corpseed", message, 2));
                }
                return "users/thank-you";
            }
        } catch (Exception e) {
            e.printStackTrace();
            model.addAttribute("jobApp", jobApp);
            session.setAttribute("message", new Message("Error : !!" + e.getMessage(), "alert-danger"));
            model.addAttribute("job", job);
            return "users/career-detail";
        }
    }

    @GetMapping("/join-our-team")
    public String joinOurTeam(Model model) {
        StaticSeo staticSeo = this.staticSeoService.findByPage("join-our-team");
        if (staticSeo != null) {
            model.addAttribute("title", staticSeo.getMetaTitle());
            model.addAttribute("metaDescription", staticSeo.getMetaDescription());
            model.addAttribute("metaKeyword", staticSeo.getMetaKeyword());
        }
        model.addAttribute("jobs", this.jobService.getJobByStatus("1"));
        return "users/career";
    }

    @GetMapping("/join-our-team/{slug}")
    public String joinOurTeam(@PathVariable("slug") String slug, Model model) {
        StaticSeo staticSeo = this.staticSeoService.findByPage("join-our-team");
        if (staticSeo != null) {
            model.addAttribute("title", staticSeo.getMetaTitle());
            model.addAttribute("metaDescription", staticSeo.getMetaDescription());
            model.addAttribute("metaKeyword", staticSeo.getMetaKeyword());
        }
        Jobs job = this.jobService.getJobBySlugAndStatus(slug, "1");
        if (job == null) {
            return "error/404";
        } else {

            model.addAttribute("job", job);
            int position = Integer.parseInt(job.getNoOfPosition()) - Integer.parseInt(job.getPositionFilled());
            model.addAttribute("position", position);
            model.addAttribute("jobApp", new JobApplication());
            return "users/career-detail";
        }
    }

    @GetMapping("/mca-fee-calculator")
    public String feeCalculator(Model model) {
        StaticSeo staticSeo = this.staticSeoService.findByPage("mca-fee-calculator");
        if (staticSeo != null) {
            model.addAttribute("title", staticSeo.getMetaTitle());
            model.addAttribute("metaDescription", staticSeo.getMetaDescription());
            model.addAttribute("metaKeyword", staticSeo.getMetaKeyword());
        }
        return "users/calculator";
    }

    @GetMapping("/payment")
    public String payment(Model model) {
        StaticSeo staticSeo = this.staticSeoService.findByPage("payment");
        if (staticSeo != null) {
            model.addAttribute("title", staticSeo.getMetaTitle());
            model.addAttribute("metaDescription", staticSeo.getMetaDescription());
            model.addAttribute("metaKeyword", staticSeo.getMetaKeyword());
        }
        return "users/online-payment";
    }

    @GetMapping("/category/{slug}")
    public String getCategory(@PathVariable("slug") String slug, Model model) {
        Category category = this.categoryService.findBySlugAndDisplayStatus(slug, "1");
        if (category == null)
            return "error/404";
        else {
            model.addAttribute("catName", category.getSubCategoryName());
            model.addAttribute("title", category.getMetaTitle());
            model.addAttribute("metaDescription", category.getMetaDescription());
            model.addAttribute("metaKeyword", category.getMetaKeyword());
            model.addAttribute("activeService", slug);
            model.addAttribute("categoryId", category.getId());
            model.addAttribute("serviceDt", this.servicesService.findByCategoryAndDisplayStatus(category, "1"));
            model.addAttribute("categoryDt", this.categoryService.getCategoryByNameAndStatus("1"));
            return "users/category";
        }
    }

    @GetMapping("/category/all")
    public String allCategory(Model model) {
        StaticSeo staticSeo = this.staticSeoService.findByPage("category-all");
        if (staticSeo != null) {
            model.addAttribute("title", staticSeo.getMetaTitle());
            model.addAttribute("metaDescription", staticSeo.getMetaDescription());
            model.addAttribute("metaKeyword", staticSeo.getMetaKeyword());
        }
        List<Category> categoryByNameStatus = this.categoryService.getCategoryByNameAndStatus("1");
        if (categoryByNameStatus == null) {
            return "error/404";
        } else {
            //		System.out.println(categoryByNameStatus);
            model.addAttribute("categoryDt", categoryByNameStatus);
            model.addAttribute("cardCategory", this.categoryService.findTop4ByShowHomeStatusAndDisplayStatus("1", "1"));

            return "users/category-all";
        }
    }

    @GetMapping("/service/service-card")
    @ResponseBody
    public String getServiceCard(@RequestParam("slugs") String slugs, @RequestParam("location") String location) {
        String serviceCard = "";
//		System.out.println("slugs="+location.substring(location.lastIndexOf("/")+1));


        if (slugs != null && slugs.length() > 0) {
            String x[] = slugs.split("#");
            serviceCard = "<div class=\"service-view text-left mt-4\"><div class=\"row row-cols-1 justify-content-center row-cols-md-2 row-cols-lg-2\">";
            for (int i = 0; i < x.length; i++) {
                Services service = this.servicesService.findBySlug(x[i]);
                String card = "card";
                String card_footer = "card-footer bg-white";
                String btn = "btn btn-default";
                if (service != null) {

                    List<ServicePackage> servicePackage = this.servicesService.getPackagesByServices(service);

                    /*if (!slug.equalsIgnoreCase("NA") && service.getSlug().equalsIgnoreCase(slug)) {
                        card += " blue";
                        card_footer = "card-footer border-0 bg-none";
                        btn = "btn btn-light";
                    }*/
                    String str[]= Helper.findFirstAndLastName(service.getTitle());
                    serviceCard += "<div class=\"col mb-4 service-view\"><div class=\"" + card + "\"><div class=\"card-body\"><h5 class=\"card-title mb-3 mt-0\"><span class=\"text-primary\">" + str[0] + "</span>"+str[1]+"</h5>";
                    if (!servicePackage.isEmpty() && servicePackage.get(0).getDeleteStatus() == 2)
                        serviceCard += "<h6 class=\"card-title mb-2\">â‚¹ " + servicePackage.get(0).getPackagePrice() + "</h6>";
                    serviceCard += "<div class=\"card-text\"><p>" + service.getSummary() + "</p></div></div>" +
                            "<div class=\"" + card_footer + "\">"+
                            "<div class=\"row\"><div class=\"col-sm-6 text-left\">" +
                            "<a href=\"#\" class=\"first\" onclick=\"playServiceVideo(\'"+service.getVideoUrl()+"\',\'"+service.getServiceName()+"\')\"><img width=\"28\" height=\"28\" src=\"/assets/img/circled-play-video.png\" alt=\"circled-play--v1\">" +
                            "<span>Watch Video</span></a></div><div class=\"col-sm-6\">" +
                            "<a href=\"/service/"+service.getSlug()+"\" class=\""+btn+"\" target=\"_blank\">Explore More&nbsp;" +
                            "<svg width=\"15\" height=\"15\" viewBox=\"0 0 9 10\" fill=\"none\" xmlns=\"http://www.w3.org/2000/svg\">" +
                            "<path d=\"M1.5 7.5L4.5 4.5L1.5 1.5\" stroke=\"#2b63f9\" stroke-linecap=\"square\"></path></svg></a></div></div></div></div></div>";
                } else {
                    Industry industry = this.industryService.findBySlug(x[i]);
                    /*if (!slug.equalsIgnoreCase("NA") && industry.getSlug().equalsIgnoreCase(slug)) {
                        card += " blue";
                        card_footer = "card-footer border-0 bg-none";
                        btn = "btn btn-light";
                    }*/
                    String str[]= Helper.findFirstAndLastName(industry.getIndustryName());
                    serviceCard += "<div class=\"col mb-4\"><div class=\"" + card + "\"><div class=\"card-body\"><h5 class=\"card-title mb-3 mt-0\"><span class=\"text-primary\">" + str[0] + "</span>"+str[1]+"</h5>"+
                            "<div class=\"card-text\"><p>" + industry.getSummary() + "</p></div></div>"+
                            "<div class=\"" + card_footer + "\">"+
                            "<div class=\"row\"><div class=\"col-sm-6 text-left\">" +
                            "<a href=\"javascript:void(0)\" class=\"first\" onclick=\"playServiceVideo(\'"+industry.getVideoUrl()+"\',\'"+industry.getIndustryName()+"\')\"><img width=\"28\" height=\"28\" src=\"/assets/img/circled-play-video.png\" alt=\"circled-play--v1\">" +
                            "<span>Watch Video</span></a></div><div class=\"col-sm-6\">" +
                            "<a href=\"/industry/"+industry.getSlug()+"\" class=\""+btn+"\" target=\"_blank\">Explore More&nbsp;" +
                            "<svg width=\"15\" height=\"15\" viewBox=\"0 0 9 10\" fill=\"none\" xmlns=\"http://www.w3.org/2000/svg\">" +
                            "<path d=\"M1.5 7.5L4.5 4.5L1.5 1.5\" stroke=\"#2b63f9\" stroke-linecap=\"square\"></path></svg></a></div></div></div></div></div>";
                }
            }
            serviceCard += "</div></div>";
        }
        return serviceCard;
    }

    @GetMapping({"/service/{slug}", "/service/{slug}/{subSlug}","/{stateCity}/{slug}"})
    public String service(@PathVariable("slug") String slug,
                          @PathVariable("subSlug") Optional<String> subSlug,
                          @PathVariable("stateCity") Optional<String> stateCity,
                          Model model) {

        Services service = this.servicesService.findBySlug(slug);
        if (service == null) return "error/404";



        if (subSlug.isPresent()) {
            String parentServiceUuid = service.getUuid();
            service = this.servicesService.findBySlug(subSlug.get());
            boolean flag = this.subServiceService.isSubServiceOfService(parentServiceUuid, service);
            if (!flag) return "error/404";
        }
        if (stateCity.isPresent()) {
            ServiceState serviceState = this.serviceStateService.findByStateNameAndServices(stateCity.get(), service);
            if (serviceState == null) {
                ServiceCity serviceCity = this.serviceCityService.findByCityNameAndServices(stateCity.get(), service);
                if (serviceCity == null)
                    return "error/404";
            } else
                model.addAttribute("cityServicesData", this.serviceCityService.findByServiceStateNameAndStateServiceId(stateCity.get(), service.getId()));
        } else {
            List<StateCityService> stateCityServiceList = this.stateCityMapService.findByServices(service);
            model.addAttribute("stateServices", stateCityServiceList.stream().filter(s -> s.getServiceState() != null).collect(Collectors.toList()));
            model.addAttribute("cityServices", stateCityServiceList.stream().filter(s -> s.getServiceCity() != null).collect(Collectors.toList()));
        }

            model.addAttribute("title", service.getMetaTitle());
            model.addAttribute("metaDescription", service.getMetaDescription());
            model.addAttribute("metaKeyword", service.getMetaKeyword());
            model.addAttribute("service", service);
            model.addAttribute("category",this.categoryService.findByService(service));
            model.addAttribute("serviceBrands", this.serviceBrand.findByServicesAndDeleteStatus(service, 2));
            model.addAttribute("serviceDetails", this.servicesService.findServiceDetailsByServicesAndDeleteStatus(service, 2));
            model.addAttribute("reviews", this.masterService.findByShowHomeStatusAndDisplayStatus("1", "1"));
            model.addAttribute("testimonials", this.testimonialService.findTestimonialByServiceAndDeleteStatus(service, 2));

            String jsonString = "{'@context': 'https://schema.org','@type': 'FAQPage','mainEntity': [";
            List<ServiceFaq> serviceFaq = this.servicesService.findFaqByServiceAndStatus(service, 2);
            int size = serviceFaq.size();
            for (ServiceFaq sf : serviceFaq) {
                size--;
                jsonString += "{'@type':'Question','name':'" + sf.getTitle().replace("'", "") + "','acceptedAnswer':{'@type':'Answer','text':'" + sf.getDescription().replace("'", "") + "'}}";
                if (size > 0) jsonString += ",";
            }
            jsonString += "]}";
            JsonObject jsonObject = (JsonObject) JsonParser.parseString(jsonString.toString());

            model.addAttribute("faqs", serviceFaq);
            model.addAttribute("faqs1", jsonObject);
            model.addAttribute("enquiry", new Enquiry());
            model.addAttribute("servicePackage", this.servicesService.getPackagesByServices(service));
            model.addAttribute("subService",this.subServiceService.findByParentServiceUuidAndDeleteStatusAndDisplayStatus(service.getUuid(), 2, 1));
            model.addAttribute("contacts", this.serviceContactService.findByServiceAndDeleteStatusAndDisplayStatus(service, 2, 1));

            String str[]= Helper.findFirstAndLastName(service.getTitle());
            model.addAttribute("serviceFirstName",str[0]);
            model.addAttribute("serviceLastName",str[1]);

            return "users/services";

    }

    @PostMapping("/service/cart-pay")
    public String payCartAmount(Model model) {
        StaticSeo staticSeo = this.staticSeoService.findByPage("cart-pay");
        if (staticSeo != null) {
            model.addAttribute("title", staticSeo.getMetaTitle());
            model.addAttribute("metaDescription", staticSeo.getMetaDescription());
            model.addAttribute("metaKeyword", staticSeo.getMetaKeyword());
        }
        return "users/online-payment";
    }

    @PostMapping("/service/cart")
    public String addToCart(@RequestParam("name") String name, @RequestParam("email") String email,
                            @RequestParam("serEnqMobile") String serEnqMobile, @RequestParam("city") String city,
                            @RequestParam("serviceuuid") String serviceuuid, @RequestParam("pagelocation") String pagelocation,
                            Model model, HttpSession session, HttpServletRequest req) {

        String returnPage = "users/cart";
        StaticSeo staticSeo = this.staticSeoService.findByPage("cart-pay");
        if (staticSeo != null) {
            model.addAttribute("title", staticSeo.getMetaTitle());
            model.addAttribute("metaDescription", staticSeo.getMetaDescription());
            model.addAttribute("metaKeyword", staticSeo.getMetaKeyword());
        }

        try {
            Services service = this.servicesService.findByUUID(serviceuuid);
            if (service == null) {
                returnPage = "error/404";
            } else {
                Enquiry enquiry = new Enquiry();
                String today = this.commonService.getToday();
                String enquuid = this.commonService.getUUID();
                enquiry.setName(name);
                enquiry.setEmail(email);
                enquiry.setMobile(serEnqMobile);
                enquiry.setCity(city);
                enquiry.setPostDate(today);
                enquiry.setModifyDate(today);
                enquiry.setUuid(enquuid);
                enquiry.setType("service");
                enquiry.setMessage(service.getServiceName());
                enquiry.setServiceId(service.getId() + "");
                enquiry.setCategoryId(service.getCategory().getId() + "");
                enquiry.setIndustryId("NA");
                enquiry.setUrl(pagelocation);
                enquiry.setIpAddress(this.commonService.getIpAddress(req));
                enquiry.setDisplayStatus("1");
                enquiry.setBitrixStatus(2);
//			System.out.println(enquiry);

                Enquiry saveEnquiry = this.enquiryService.saveEnquiry(enquiry);
                if (saveEnquiry != null) session.setAttribute("orderEnquiry52412qw", enquuid);

                Orders order = new Orders();
                order.setUuid(this.commonService.getUUID());
                order.setName(name);
                order.setEmail(email);
                order.setMobile(serEnqMobile);
                order.setCity(city);
                order.setPostDate(today);
                order.setModifyDate(today);
                order.setServices(service);
                order.setOrderStatus("1");

                model.addAttribute("orderData", order);

                model.addAttribute("service", service);
                model.addAttribute("packages", this.servicesService.getPackagesByServices(service));

            }
        } catch (Exception u) {
            returnPage = "error/404";
        }
        return returnPage;
    }

    @PostMapping("/service/enquiry")
    @ResponseBody
    public String addToEnquiry(@RequestParam("otp") String otp, @RequestParam("url") String url, @RequestParam("name") String name,
                               @RequestParam("email") String email, @RequestParam("mobile") String mobile, @RequestParam("city") String city,
                               @RequestParam("postDate") String postDate, @RequestParam("categoryId") String categoryId, @RequestParam("message") String message,
                               @RequestParam("modifyDate") String modifyDate, @RequestParam("serviceId") String serviceId
            , HttpServletRequest req) {

    	System.out.println("aaaaaaaa");
    	restTemplateService.getProductListsu();
    	System.out.println("bbbbbbbbb");

        boolean matches = mobile.substring(mobile.length() - 10).matches("[6-9][0-9]{9}");
        //validate otp again
        boolean validOtp = otpService.isValidOtp(mobile, otp);
//		System.out.println(mobile+"#"+matches+"#"+validOtp);
        if (!matches || !validOtp)
            return "fail";

        try {
            if (email != null && email.length() > 0 && serviceId != null && serviceId.length() > 0) {

                Enquiry enquiry = new Enquiry();

                enquiry.setName(name);
                enquiry.setEmail(email);
                enquiry.setMobile(mobile);
                enquiry.setCity(city);
                enquiry.setPostDate(postDate);
                enquiry.setModifyDate(modifyDate);
                enquiry.setUuid(this.commonService.getUUID());
                enquiry.setType("service");
                enquiry.setMessage(message);
                enquiry.setServiceId(serviceId);
                enquiry.setCategoryId(categoryId);
                enquiry.setIndustryId("NA");
                enquiry.setUrl(url);
                enquiry.setIpAddress(this.commonService.getIpAddress(req));
                enquiry.setDisplayStatus("1");
                enquiry.setBitrixStatus(2);

                Enquiry saveEnquiry = null;
                boolean isValidRequest = enquiryService.isValidRequest(mobile, email);
                if (!isValidRequest)
                    saveEnquiry = this.enquiryService.saveEnquiry(enquiry);

                if (saveEnquiry == null) {
                    return ("fail");
                } else {
                    return ("pass");
                }
            } else {
                return ("invalid");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ("fail");
        }
    }

    @PostMapping("/enquiry/change-your-ca")
    public @ResponseBody
    String saveChangeYouCAEnquiry(@RequestParam("otp") String otp, @RequestParam("name") String name, @RequestParam("email") String email, @RequestParam("mobile") String mobile,
                                  @RequestParam("city") String city, @RequestParam("packTime") String packTime, @RequestParam("packPrice") String packPrice, @RequestParam("packageName") String packageName,
                                  @RequestParam("location") String location, @RequestParam("postDate") String postdate, @RequestParam("modifyDate") String modifydate
            , HttpServletRequest req) {

        boolean matches = mobile.substring(mobile.length() - 10).matches("[6-9][0-9]{9}");
        //validate otp again
        boolean validOtp = otpService.isValidOtp(mobile, otp);
        if (!matches || !validOtp)
            return "fail";

        try {
            Enquiry enquiry = new Enquiry();

            enquiry.setName(name);
            enquiry.setEmail(email);
            enquiry.setMobile(mobile);
            enquiry.setCity(city);
            enquiry.setPostDate(postdate);
            enquiry.setModifyDate(modifydate);
            enquiry.setUuid(this.commonService.getUUID());
            enquiry.setType("Change Your CA");
            enquiry.setMessage(packageName + "(Rs. " + packPrice + ") - " + packTime);
            enquiry.setServiceId("NA");
            enquiry.setCategoryId("NA");
            enquiry.setIndustryId("NA");
            enquiry.setUrl(location);
            enquiry.setIpAddress(this.commonService.getIpAddress(req));
            enquiry.setDisplayStatus("1");
            enquiry.setBitrixStatus(2);

            Enquiry saveEnquiry = null;
            boolean isValidRequest = enquiryService.isValidRequest(mobile, email);
            if (!isValidRequest)
                saveEnquiry = this.enquiryService.saveEnquiry(enquiry);

            if (saveEnquiry == null) {
                return ("fail");
            } else {
                Subscribers subscriber = this.subscribeService.findByEmail(email);
                if (subscriber == null)
                    this.subscribeService.addSubscriber(new Subscribers(0, this.commonService.getUUID(), saveEnquiry.getEmail(),
                            "1", saveEnquiry.getPostDate(), saveEnquiry.getModifyDate()));

                return ("pass");
            }

        } catch (Exception e) {
            e.printStackTrace();
            return ("fail");
        }
    }

    @PostMapping("/enquiry/save")
    public @ResponseBody
    String saveEnquiry(@RequestParam("otp") String otp, @RequestParam("name") String name, @RequestParam("email") String email, @RequestParam("mobile") String mobile,
                       @RequestParam("url") String url, @RequestParam("postDate") String postdate, @RequestParam("modifyDate") String modifydate,
                       HttpServletRequest req) {

        boolean matches = mobile.substring(mobile.length() - 10).matches("[6-9][0-9]{9}");
        //validate otp again
        boolean validOtp = otpService.isValidOtp(mobile, otp);
        if (!matches || !validOtp)
            return "fail";

        try {
            Enquiry enquiry = new Enquiry();

            enquiry.setName(name);
            enquiry.setEmail(email);
            enquiry.setMobile(mobile);
            enquiry.setCity("NA");
            enquiry.setPostDate(postdate);
            enquiry.setModifyDate(modifydate);
            enquiry.setUuid(this.commonService.getUUID());
            enquiry.setType("start up guide");
            enquiry.setMessage("Download your free legal guide Now");
            enquiry.setServiceId("NA");
            enquiry.setCategoryId("NA");
            enquiry.setIndustryId("NA");
            enquiry.setUrl(url);
            enquiry.setIpAddress(this.commonService.getIpAddress(req));
            enquiry.setDisplayStatus("1");
            enquiry.setBitrixStatus(2);

            Enquiry saveEnquiry = null;
            boolean isValidRequest = enquiryService.isValidRequest(mobile, email);
            if (!isValidRequest)
                saveEnquiry = this.enquiryService.saveEnquiry(enquiry);

            if (saveEnquiry == null) {
                return ("fail");
            } else {

                Subscribers subscriber = this.subscribeService.findByEmail(email);
                if (subscriber == null)
                    this.subscribeService.addSubscriber(new Subscribers(0, this.commonService.getUUID(), saveEnquiry.getEmail(),
                            "1", saveEnquiry.getPostDate(), saveEnquiry.getModifyDate()));

                String msg = "<p>Full Name : " + saveEnquiry.getName()
                        + "<br/>Email Address : " + saveEnquiry.getEmail()
                        + "<br/>Phone : " + saveEnquiry.getMobile() + "</p>"
                        + "<p>Message : <br/>" + saveEnquiry.getMessage() + "</p>";
                this.emailSender.sendmail("info@corpseed.com", "empty", "Start up guide", msg, "NA");

                return ("pass");
            }

        } catch (Exception e) {
            e.printStackTrace();
            return ("fail");
        }
    }

    @PostMapping("/enquiry/save/globalenquiry")
    public @ResponseBody
    String saveGlobalEnquiry(@RequestParam("otp") String otp, @RequestParam("name") String name, @RequestParam("email") String email, @RequestParam("mobile") String mobile,
                       @RequestParam("url") String url, @RequestParam("postDate") String postdate, @RequestParam("modifyDate") String modifydate,
                             @RequestParam("message") String message,HttpServletRequest req) {

        boolean matches = mobile.substring(mobile.length() - 10).matches("[6-9][0-9]{9}");
        //validate otp again
        boolean validOtp = otpService.isValidOtp(mobile, otp);
        if (!matches || !validOtp)
            return "invalid";

        try {
            Enquiry enquiry = new Enquiry();

            enquiry.setName(name);
            enquiry.setEmail(email);
            enquiry.setMobile(mobile);
            enquiry.setCity("NA");
            enquiry.setPostDate(postdate);
            enquiry.setModifyDate(modifydate);
            enquiry.setUuid(this.commonService.getUUID());
            enquiry.setType("Enquiry");
            enquiry.setMessage(message);
            enquiry.setServiceId("NA");
            enquiry.setCategoryId("NA");
            enquiry.setIndustryId("NA");
            enquiry.setUrl(url);
            enquiry.setIpAddress(this.commonService.getIpAddress(req));
            enquiry.setDisplayStatus("1");
            enquiry.setBitrixStatus(2);

            Enquiry saveEnquiry = null;
            boolean isValidRequest = enquiryService.isValidRequest(mobile, email);
            if (!isValidRequest)
                saveEnquiry = this.enquiryService.saveEnquiry(enquiry);

            if (saveEnquiry == null)
                return ("fail");

            return "pass";
        } catch (Exception e) {
            e.printStackTrace();
            return ("fail");
        }
    }

    @GetMapping("/service/cart/coupon-remove")
    @ResponseBody
    public void removeCoupon(@RequestParam("ServiceUuid") String ServiceUuid
            , @RequestParam("array") String array, PrintWriter pw) {
        try {

            if (ServiceUuid != null && !ServiceUuid.equalsIgnoreCase("NA") && ServiceUuid.length() > 0) {

                Services services = this.servicesService.findByUUID(ServiceUuid);
                if (services != null) {
                    double orderAmount = 0;
                    this.commonService.setZero();
                    String pkgIdList[] = array.split(",");
                    for (String string : pkgIdList) {
                        if (string != null && string.length() > 0) {
                            Optional<ServicePackage> packageById = this.servicesService.getPackageById(string);
                            packageById.ifPresent(sPackage -> {
                                this.commonService.sumAmount(Double.parseDouble(sPackage.getPackagePrice()));
                            });
                        }
                    }
                    orderAmount = this.commonService.getOrderAmount();
                    double gst = (orderAmount * 18) / 100;
                    orderAmount += gst;
                    pw.write(orderAmount + "");
                } else {
                    pw.write("invalid");
                }

            } else {
                pw.write("invalid");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @GetMapping("/service/cart/coupon")
    @ResponseBody
    public void applyCoupon(@RequestParam("coupon") String coupon, @RequestParam("ServiceUuid") String ServiceUuid
            , @RequestParam("array") String array, PrintWriter pw) {
        try {
            if (ServiceUuid != null && !ServiceUuid.equalsIgnoreCase("NA") && ServiceUuid.length() > 0) {

                Services services = this.servicesService.findByUUID(ServiceUuid);
                if (services != null) {
                    String startDate = this.commonService.dateAfterDays(1);
                    String endDate = this.commonService.dateBeforeDays(1);

                    boolean applyCoupon = false;
                    Coupon findCoupon = this.couponService.findByTitleAndDisplayStatusAndStartDateBeforeAndEndDateAfter(coupon, "1", startDate, endDate);
                    if (findCoupon != null && findCoupon.getServiceType().equals("selected")) {
                        CouponServices findCouponService = this.couponServicesService.findByCouponAndServices(findCoupon, services);
                        if (findCouponService == null) applyCoupon = false;
                        else applyCoupon = true;
                    } else if (findCoupon != null && findCoupon.getServiceType().equals("all"))
                        applyCoupon = true;

                    if (applyCoupon) {
                        double discount = 0;
                        double orderAmount = 0;
                        this.commonService.setZero();
                        String pkgIdList[] = array.split(",");
                        for (String string : pkgIdList) {
                            Optional<ServicePackage> packageById = this.servicesService.getPackageById(string);
                            packageById.ifPresent(sPackage -> {
                                this.commonService.sumAmount(Double.parseDouble(sPackage.getPackagePrice()));
                            });
                        }
                        orderAmount = this.commonService.getOrderAmount();
                        if (orderAmount > 0) {
                            double gst = (orderAmount * 18) / 100;
                            orderAmount += gst;
                            double discValue = Double.parseDouble(findCoupon.getValue());
                            if (findCoupon.getType().equalsIgnoreCase("percentage")) {
                                discount = (orderAmount * discValue) / 100;
                                if (discount > Double.parseDouble(findCoupon.getMaximumDiscount())) {
                                    discount = Double.parseDouble(findCoupon.getMaximumDiscount());
                                }
                            } else if (findCoupon.getType().equalsIgnoreCase("fixed")) {
                                discount = discValue;
                            }
                            orderAmount -= discount;

                            pw.write(discount + "#" + orderAmount);
                        }

                    } else {
                        pw.write("invalid");
                    }
                }

            } else {
                pw.write("error");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @GetMapping("/chat_boat/category")
    @ResponseBody
    public Map<String, String> allCategory(@RequestParam("mobile") String mobile, @RequestParam("otp") String otp) {
        HashMap<String, String> map = new HashMap<>();

        //validate otp again
        boolean validOtp = otpService.isValidOtp(mobile, otp);
        if (!validOtp) return map;

        List<Category> category = this.categoryService.findByDisplayStatus("1");
        for (Category cat : category) {
            map.put(cat.getUuid(), cat.getSubCategoryName());
        }

        return map;
    }

    @GetMapping("/chat_boat/verify-email")
    @ResponseBody
    public void verifyEmail(@RequestParam("email") String email, PrintWriter pw) {
        boolean addressValid = false;
        if (email != null && !email.equalsIgnoreCase("NA") && email.length() > 0) {
            addressValid = true;
        }
        if (addressValid) pw.write("pass");
        else pw.write("invalid");

    }

    @GetMapping("/chat_boat/category/services")
    @ResponseBody
    public Map<String, String> allServices(@RequestParam("key") String key) {
        HashMap<String, String> map = new HashMap<>();
        Category category = this.categoryService.findByCategoryUUID(key);
        List<Services> services = this.servicesService.findByCategoryAndDisplayStatus(category, "1");
        for (Services service : services) {
            map.put(service.getUuid(), service.getServiceName());
        }
        return map;
    }

    @PostMapping("/chat_boat/save-enquiry")
    @ResponseBody
    public void saveChatEnquiry(@RequestParam("otp") String otp,
                                @RequestParam("categoryUuid") String categoryUuid,
                                @RequestParam("serviceUuid") String serviceUuid, @RequestParam("name") String name
            , @RequestParam("email") String email, @RequestParam("mobile") String mobile
            , @RequestParam("message") String message, @RequestParam("location") String location
            , HttpServletRequest req) {

        boolean matches = mobile.substring(mobile.length() - 10).matches("[6-9][0-9]{9}");
        //validating chat otp
        boolean validOtp = this.otpService.isValidOtp(mobile, otp);
        try {
            Services findByUUID = null;
            Enquiry enquiry = new Enquiry();
            if (categoryUuid != null && !categoryUuid.equals("NA") && categoryUuid.length() > 0) {
                Category findByCategoryUUID = this.categoryService.findByCategoryUUID(categoryUuid);
                if (findByCategoryUUID != null) enquiry.setCategoryId(findByCategoryUUID.getId() + "");
                else enquiry.setCategoryId("NA");
            }
            if (serviceUuid != null && !serviceUuid.equals("NA") && serviceUuid.length() > 0) {
                findByUUID = this.servicesService.findByUUID(serviceUuid);
                if (findByUUID != null) enquiry.setServiceId(findByUUID.getId() + "");
                else enquiry.setServiceId("NA");
            }

            String today = this.commonService.getToday();
            enquiry.setName(name);
            enquiry.setEmail(email);
            enquiry.setMobile(mobile);
            enquiry.setCity("NA");
            enquiry.setPostDate(today);
            enquiry.setModifyDate(today);
            enquiry.setUuid(this.commonService.getUUID());
            enquiry.setType("Chat");
            enquiry.setMessage(message);
            enquiry.setUrl(location);
            enquiry.setIpAddress(this.commonService.getIpAddress(req));
            enquiry.setDisplayStatus("1");
            enquiry.setBitrixStatus(2);
            enquiry.setIndustryId("NA");
            boolean isValidRequest = enquiryService.isValidRequest(mobile, email);
            if (matches && !isValidRequest && validOtp)
                this.enquiryService.saveEnquiry(enquiry);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @GetMapping("/search/service-industry-blog/{value}")
    @ResponseBody
    public String homeSearch(@PathVariable("value") String value) {
        String data = "<h5>Services & Industries</h5><ul>";
        List<Services> services = this.servicesService.findTop4ByServiceName(value);
        List<Industry> industries = this.industryService.findTop1ByIndustryName(value);
        List<Blogs> blogs = this.blogService.findTop5ByTitleContaining(value);
        for (Services s : services) {
            data += "<li><a href='/service/" + s.getSlug() + "' onclick='addTranding(\"" + s.getServiceName() + "\")'>" + s.getServiceName() + "</a></li>";
        }
        for (Industry i : industries) {
            data += "<li><a href='/industry/" + i.getSlug() + "' onclick='addTranding(\"" + i.getIndustryName() + "\")'>" + i.getIndustryName() + "</a></li>";
        }
        data += "</ul>";

        data += "<h5>Blogs</h5><ul>";
        for (Blogs b : blogs) {
            data += "<li><a href='/knowledge-centre/" + b.getSlug() + "' onclick='addTranding(\"" + b.getTitle() + "\")'>" + b.getTitle() + "</a></li>";
        }
        data += "</ul>";
        return data;
    }


    @GetMapping("/search/{value}")
    @ResponseBody
    public Map<String, String> search(@PathVariable("value") String value) {
        HashMap<String, String> map = new HashMap<>();
        List<Services> services = this.servicesService.findByServiceName(value);
        for (Services services2 : services) {
            map.put(services2.getSlug(), services2.getServiceName());
        }
        return map;
    }

    @GetMapping("/search/popular-services-blogs")
    @ResponseBody
    public String searchPopularServicesBlogs() {
        String popular = "";
        List<Services> services = this.servicesService.findTop4ByDisplayStatusAndDeleteStatusOrderByVisitedDesc("1", 2);
        Industry industry = this.industryService.findTop1ByDisplayStatusAndDeleteStatusOrderByVisitedDesc("1", 2);
        List<Blogs> blogs = this.blogService.findTop5ByDisplayStatusAndDeleteStatusOrderByVisitedDesc("1", 2);

        if (!services.isEmpty()) {
            popular += "<h5>Popular Services & Industries</h5><ul>";
            for (Services service : services) {
                popular += "<li><a href='/service/" + service.getSlug() + "' onclick='addTranding(\"" + service.getServiceName() + "\")'>" + service.getServiceName() + "</a></li>";
            }
            if (industry != null)
                popular += "<li><a href='/industry/" + industry.getSlug() + "' onclick='addTranding(\"" + industry.getIndustryName() + "\")'>" + industry.getIndustryName() + "</a></li>";
            popular += "</ul>";
        }
        if (!blogs.isEmpty()) {
            popular += "<h5>Popular Blogs</h5><ul>";
            for (Blogs blog : blogs) {
                popular += "<li><a href='/knowledge-centre/" + blog.getSlug() + "' onclick='addTranding(\"" + blog.getTitle() + "\")'>" + blog.getTitle() + "</a></li>";
            }
            popular += "</ul>";
        }

        return popular;
    }

    @GetMapping("/blogs/{value}")
    @ResponseBody
    public void blogSearch(@PathVariable String value, Model model) {

        List<Blogs> blogs = this.blogService.findByTitle(value);

        model.addAttribute("service", blogs);
        model.addAttribute("type", "findBlog");
    }

    @GetMapping("/city/{value}")
    @ResponseBody
    public HashMap<Long, String> citySearch(@PathVariable String value, Model model) {
        HashMap<Long, String> map = new HashMap<>();
        List<City> city = this.masterService.findByCityNameContaining(value);
        for (City city2 : city) {
            map.put(city2.getId(), city2.getCityName());
        }
        return map;
    }

    @GetMapping("/payment/invoice/{orderuuid}")
    public String showInvoice(@PathVariable("orderuuid") String orderuuid, Model model) {
        StaticSeo staticSeo = this.staticSeoService.findByPage("payment-invoice");
        if (staticSeo != null) {
            model.addAttribute("title", staticSeo.getMetaTitle());
            model.addAttribute("metaDescription", staticSeo.getMetaDescription());
            model.addAttribute("metaKeyword", staticSeo.getMetaKeyword());
        }
        try {
            Orders order = this.orderService.getOrderByUuid(orderuuid);
            model.addAttribute("order", order);
            model.addAttribute("transactions", this.txsService.getTransactionByOrderNo(order.getOrderNo()));
            return "users/invoice";
        } catch (NullPointerException e) {
            return "error/404";
        }
    }

    @PostMapping("/subscribe")
    @ResponseBody
    public String subscribe(@RequestParam("email") String email) {
        if (email != null && email.length() > 0) {
            Subscribers existSubscribe = this.subscribeService.findByEmail(email);
            if (existSubscribe == null) {
                Subscribers subscribe = this.subscribeService.addSubscriber(new Subscribers(0,
                        this.commonService.getUUID(), email, "1", this.commonService.getToday(),
                        this.commonService.getToday()));

                if (subscribe != null)
                    return ("pass");
                else
                    return ("fail");
            } else {
                return ("duplicate");
            }

        } else {
            return ("empty");
        }
    }

    @GetMapping("/subscribe/thanks")
    public String contactUsThanks(Model model) {
        StaticSeo staticSeo = this.staticSeoService.findByPage("subscribe-thanks");
        if (staticSeo != null) {
            model.addAttribute("title", staticSeo.getMetaTitle());
            model.addAttribute("metaDescription", staticSeo.getMetaDescription());
            model.addAttribute("metaKeyword", staticSeo.getMetaKeyword());
        }
        return "users/subscribe-thanks";
    }

    @GetMapping("/enquiry/tranding-search")
    @ResponseBody
    public void updateTrandingSearch(@RequestParam("key") String key) {
        if (key != null && key.length() > 0) {
            TrandingSearch search = this.trandingService.findBySearchName(key);
            if (search != null) {
                search.setSearched(search.getSearched() + 1);
                this.trandingService.saveTrandingSearch(search);
            } else {
                this.trandingService.saveTrandingSearch(new TrandingSearch(0, this.commonService.getUUID(), key, 1));
            }
        }
    }

    @PostMapping("/enquiry/news/visitor")
    @ResponseBody
    public String saveNewsVisitors(@RequestParam("slug") String slug) {

        try {
            News news = this.newsService.findBySlug(slug);
            if (news != null) {
                news.setVisited(news.getVisited() + 1);
                this.newsService.saveNews(news);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    @PostMapping("/enquiry/visitor/save")
    @ResponseBody
    public void saveVisitors(@RequestParam("type") String type, @RequestParam("uuid") String uuid,
                             @RequestParam("location") String location, @RequestParam("title") String title, HttpServletRequest req) {
//        System.out.println("save visitor record called..........................................");
        try {
            UserAgent userAgent = UserAgent.parseUserAgentString(req.getHeader("User-Agent"));
            OperatingSystem operatingSystem = userAgent.getOperatingSystem();
            Browser browser = userAgent.getBrowser();
            String browserName = browser.getName();
            Version browserVersion = userAgent.getBrowserVersion();

//		    System.out.println(req.getRemoteAddr()+"/"+req.getRemoteHost()+"/"+req.getRemotePort()+"/"+req.getRemoteUser());
            String ipAddress = this.commonService.getIpAddress(req);
            String today = this.commonService.getToday();
//		    System.out.println(ipAddress+"//"+uuid+"//"+today+"//"+operatingSystem.getName());
            Visitors findVisitors = this.visitorsService.findByIpAddressAndBlogServiceUuidAndVisitedDate(ipAddress, uuid, today);
            if (findVisitors == null) {
                Visitors visitors = new Visitors();
                visitors.setUuid(this.commonService.getUUID());
                visitors.setType(type);
                visitors.setIpAddress(ipAddress);
                visitors.setVisitedDate(today);
                visitors.setBrowser(browserName + "  Version :" + browserVersion);
                visitors.setUrl(location);
                visitors.setVisited(1);
                visitors.setOperatingSystem(operatingSystem.getName());
                visitors.setBlogServiceUuid(uuid);
                visitors.setTitle(title);
                visitors.setDay(this.commonService.getDay());
                this.visitorsService.saveVisitors(visitors);
            } else {
                findVisitors.setVisited(findVisitors.getVisited() + 1);
                this.visitorsService.saveVisitors(findVisitors);
            }
//			System.out.println("Type="+type);
            if (type.equalsIgnoreCase("blog")) {
                String domain = env.getProperty("domain.name") + "/knowledge-centre/";
                Blogs blog = this.blogService.findBySlug(location.substring(domain.length()));
                if (blog != null) {
                    blog.setVisited(blog.getVisited() + 1);
                    this.blogService.updateBlogsVisitor(blog);
                }
            } else if (type.equalsIgnoreCase("service")) {
                String domain = env.getProperty("domain.name") + "/service/";
                Services service = this.servicesService.findBySlug(location.substring(domain.length()));
//				System.out.println("service="+service);
                if (service != null) {
                    service.setVisited(service.getVisited() + 1);
                    this.servicesService.updateServiceVisitor(service);
                }
            } else if (type.equalsIgnoreCase("industry")) {
                String domain = env.getProperty("domain.name") + "/industry/";
                Industry industry = this.industryService.findBySlug(location.substring(domain.length()));
                if (industry != null) {
                    industry.setVisited(industry.getVisited() + 1);
                    this.industryService.saveIndustry(industry);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @GetMapping("/login")
    public String login(Model model) {
        StaticSeo staticSeo = this.staticSeoService.findByPage("login");
        if (staticSeo != null) {
            model.addAttribute("title", staticSeo.getMetaTitle());
            model.addAttribute("metaDescription", staticSeo.getMetaDescription());
            model.addAttribute("metaKeyword", staticSeo.getMetaKeyword());
        }
        return "login";
    }

    @GetMapping("/forget-password")
    public String forgetPassword(Model model) {
        StaticSeo staticSeo = this.staticSeoService.findByPage("forget-password");
        if (staticSeo != null) {
            model.addAttribute("title", staticSeo.getMetaTitle());
            model.addAttribute("metaDescription", staticSeo.getMetaDescription());
            model.addAttribute("metaKeyword", staticSeo.getMetaKeyword());
        }
        return "forget";
    }

    @PostMapping("/forget-password")
    public String forgetPasswordLink(@RequestParam("email") String email, Model model) {
        StaticSeo staticSeo = this.staticSeoService.findByPage("forget-password");
        if (staticSeo != null) {
            model.addAttribute("title", staticSeo.getMetaTitle());
            model.addAttribute("metaDescription", staticSeo.getMetaDescription());
            model.addAttribute("metaKeyword", staticSeo.getMetaKeyword());
        }
        try {
            if (email == null || email.length() <= 0) {
                model.addAttribute("message", new Message("Please enter a valid email address !!", "text-danger"));
                return "forget";
            }

            User user = this.userService.findByEmailAndDisplayStatus(email, "1");
            if (user == null) {
                model.addAttribute("message", new Message("Either email-address doesn't exist or your account is on hold !!", "text-danger"));
                return "forget";
            } else {
                model.addAttribute("message", new Message("Reset your password clicking on email link !!", "text-success"));
                String domain = env.getProperty("domain.name");
                String link = domain + "/reset-password/" + user.getUuid();
                ForgetPassword forgetPass = this.forgetPasswordService.findByUser(user);
                if (forgetPass != null)
                    this.forgetPasswordService.deactivatePastLink(forgetPass.getId());
                this.forgetPasswordService.saveForgetPasswordLink(new ForgetPassword(0, this.commonService.getUUID(), link, "1", this.commonService.getToday(), user));

                String message = "<p>Hi &nbsp;" + user.getFirstName() + "</p><p>We received a request for a password change on your <a href='" + domain + "'>Corpseed.com</a> account. You can reset your password <a href='" + link + "' style='color:blue'>here</a>.</p>" +
                        "<p>This link will expire in 24 hours.After that,you'll need to submit a new request in order to reset your password. If you don't want to reset it, Simply disregard this email.</p><p>If you need more help or believe this email was sent in error, feel free to <a href='mailto:hello@corpseed.com'>Contact Us</a>.</p>" +
                        "<p>(Please don't reply to this message,It's automated.)</p><p>Thanks,</p><p>corpseed.com</p>";
                this.emailSender.sendmail(user.getEmail(), "empty", "Reset password", message, "NA");
                return "forget";
            }
        } catch (Exception e) {
            e.printStackTrace();
            model.addAttribute("message", new Message("Error : " + e.getMessage(), "text-danger"));
            return "forget";
        }
    }

    @GetMapping("/reset-password/{uuid}")
    public String resetPassword(@PathVariable("uuid") String uuid, Model model) {
        StaticSeo staticSeo = this.staticSeoService.findByPage("reset-password");
        if (staticSeo != null) {
            model.addAttribute("title", staticSeo.getMetaTitle());
            model.addAttribute("metaDescription", staticSeo.getMetaDescription());
            model.addAttribute("metaKeyword", staticSeo.getMetaKeyword());
        }
        User user = this.userService.getUserByUuid(uuid);
        if (user == null) {
            return "error/404";
        } else {
            ForgetPassword forgetpass = this.forgetPasswordService.findByUser(user);
            if (forgetpass == null) {
                return "error/expired";
            }
            if (forgetpass.getDisplayStatus().equals("2")) {
                return "error/expired";
            } else {
                model.addAttribute("uuid", uuid);
                return "reset-password";
            }

        }
    }

    @PostMapping("/reset-password/{uuid}")
    public String updatePassword(@PathVariable("uuid") String uuid, @RequestParam("newPassword") String newPassword
            , @RequestParam("confirmPassword") String confirmPassword, Model model) {
        StaticSeo staticSeo = this.staticSeoService.findByPage("reset-password");
        if (staticSeo != null) {
            model.addAttribute("title", staticSeo.getMetaTitle());
            model.addAttribute("metaDescription", staticSeo.getMetaDescription());
            model.addAttribute("metaKeyword", staticSeo.getMetaKeyword());
        }
        try {

            if (newPassword == null || newPassword.length() <= 0 || confirmPassword == null || confirmPassword.length() <= 0) {
                model.addAttribute("message", new Message("Please fill out the blank fields !!", "text-danger"));
                return "reset-password";
            }
            if (!newPassword.equals(confirmPassword)) {
                model.addAttribute("message", new Message("New Password and confirm password doesn't match !!", "text-danger"));
                return "reset-password";
            }

            User user = this.userService.getUserByUuid(uuid);
            if (user == null) {
                model.addAttribute("message", new Message("User not found.", "text-danger"));
                return "reset-password";
            }
            ForgetPassword forgetPassword = this.forgetPasswordService.findByUserAndDisplayStatus(user, "1");
            if (forgetPassword == null) return "error/expired";

            user.setPassword(passwordEncoder.encode(confirmPassword));

            User saveUser = this.userService.saveUser(user);
            if (saveUser == null) {
                model.addAttribute("message", new Message("Password is not updated.", "text-danger"));
                return "reset-password";
            } else {
                ForgetPassword forgetPass = this.forgetPasswordService.findByUser(saveUser);
                if (forgetPass != null)
                    this.forgetPasswordService.deactivatePastLink(forgetPass.getId());

                model.addAttribute("message", new Message("Successfully password update,login here !!", "text-success"));
                return "login";
            }

        } catch (Exception e) {
            model.addAttribute("message", new Message("Error : !!" + e.getMessage(), "text-danger"));
            return "reset-password";
        }
    }

    @GetMapping("/pages/{slug}")
    public String cmsPage(@PathVariable("slug") String slug, Model model) {
        CmsPages findBySlug = this.masterService.findBySlug(slug);
        if (findBySlug != null) {
            model.addAttribute("page", findBySlug);
            model.addAttribute("title", findBySlug.getMetaTitle());
            model.addAttribute("metaDescription", findBySlug.getMetaDescription());
            model.addAttribute("metaKeyword", findBySlug.getMetaKeyword());
            return "users/cms-page";
        } else return "error/404";
    }

    @GetMapping("/faq")
    public String faq(Model model) {
        StaticSeo staticSeo = this.staticSeoService.findByPage("faq");
        if (staticSeo != null) {
            model.addAttribute("title", staticSeo.getMetaTitle());
            model.addAttribute("metaDescription", staticSeo.getMetaDescription());
            model.addAttribute("metaKeyword", staticSeo.getMetaKeyword());
        }
        return "users/faq";

    }

    @PostMapping("/enquiry/push-bitrix24")
    @ResponseBody
    public void pushOnBitrix(@RequestParam("enqUuid") String enqUuid, PrintWriter pw) {
        try {
//			System.out.println("going to push bitrix data");
            if (enqUuid != null && enqUuid.length() > 0) {
                String uuid[] = enqUuid.split("#");
                List<Enquiry> enqList = new ArrayList<>();
                for (String uuid1 : uuid) {
//				System.out.println("uuiduuid="+uuid1);
                    Enquiry enq = this.enquiryService.findByUuid(uuid1);
                    enq.setBitrixStatus(2);
                    enqList.add(enq);
                }
                if (!enqList.isEmpty())
                    this.enquiryService.saveAllEnquiry(enqList);
                pw.write("pass");
            }
        } catch (Exception e) {
            pw.write("fail");
            e.printStackTrace();
        }
    }

    @PostMapping("/enquiry/ivr-call")
    @ResponseBody
    public void pushIvrCall(@RequestParam("caller_id") String caller_id) {
        try {
            String today = this.commonService.getToday();
            if (caller_id != null && caller_id.length() > 0) {
                this.enquiryService.saveEnquiry(new Enquiry(0, this.commonService.getUUID(), "IVR Call", "IVR Call - " + caller_id, "IVR Call", "NA", caller_id, "NA", "NA", "NA", "NA", "NA", "NA", "1", today, today, 2, 2, 2));

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @GetMapping("/allow-cookie")
    @ResponseBody
    public void setCookies(@CookieValue(value = "username", defaultValue = "Unknown") String username, HttpServletResponse response
            , HttpServletRequest req) {
        try {
            if (username.equals("Unknown")) {
                String ipAddress = this.commonService.getIpAddress(req);
                response.addCookie(new Cookie("username", ipAddress));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @GetMapping("/ticket-support")
    public String openTicketSupportForm() {
        return "users/ticket-support";
    }

    @PostMapping("/summernote/upload")
    @ResponseBody
    public void summernoteUploads(@RequestParam("file") MultipartFile file, PrintWriter pw) {
        try {
            if (!file.isEmpty()) {
                boolean fileExist = this.azureAdapter.isFileExist(file.getOriginalFilename());
                if (!fileExist) {
                    String name = azureAdapter.upload(file, 0);
                    String url = env.getProperty("azure_path") + name;
                    pw.write(url);
                } else {
                    pw.write("exist");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @PostMapping("/ckeditor/upload")
    @ResponseBody
    public String ckeditorUploads(@RequestParam("upload") MultipartFile file, @RequestParam(value = "CKEditorFuncNum") String callback) {
        String name = "";
        String url = "";
        try {
            if (!file.isEmpty()) {
                boolean fileExist = this.azureAdapter.isFileExist(file.getOriginalFilename());
                if (!fileExist) {
                    name = azureAdapter.upload(file, 0);
                    url = env.getProperty("azure_path") + name;
                } else {
                    name = file.getOriginalFilename();
                    url = env.getProperty("azure_path") + name;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        StringBuffer sb = new StringBuffer();
        sb.append("<script type='text/javascript'>window.parent.CKEDITOR.tools.callFunction(");
        sb.append(callback);
        sb.append(",'");
        sb.append(url);
        sb.append("')</script>");
        return sb.toString();
    }

    @PostMapping("/ckeditor/uploadFile")
    @ResponseBody
    public String ckeditorUploadFile(@RequestParam("file") MultipartFile file) {
        String name = "";
        String url = "";
        try {
            if (!file.isEmpty()) {
                boolean fileExist = this.azureAdapter.isFileExist(file.getOriginalFilename());
                if (!fileExist) {
                    name = azureAdapter.upload(file, 0);
                    url = env.getProperty("azure_path") + name;
                } else {
                    name = file.getOriginalFilename();
                    url = env.getProperty("azure_path") + name;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return url;
    }

    @GetMapping("/search/blogs/{value}")
    @ResponseBody
    public Map<String, String> searchBlog(@PathVariable("value") String value) {
        HashMap<String, String> map = new HashMap<>();
        List<Blogs> blogs = this.blogService.findByTitleContaining(value);
        for (Blogs blog : blogs) {
            map.put(blog.getSlug(), blog.getTitle());
        }
        return map;
    }

    @GetMapping("/search/news/{value}")
    @ResponseBody
    public Map<String, String> searchNews(@PathVariable("value") String value) {
        HashMap<String, String> map = new HashMap<>();
        List<News> news = this.newsService.findByTitleContaining(value);
        for (News n : news) {
            map.put(n.getSlug(), n.getTitle());
        }
        return map;
    }

    @PostMapping("/enquiry/send-otp/{mobile}")
    @ResponseBody
    public String sendOtp(@PathVariable("mobile") String mobile, @RequestParam("name") String name,
                          @RequestParam("location") String location) {
//		System.out.println("mobile=="+mobile);
        if (mobile != null && mobile.length() > 0) {
//			boolean matches = mobile.substring(mobile.length()-10).matches("[6-9][0-9]{9}");
            boolean blockedNumber = enquiryService.isValidRequest(mobile, "NA");
//			System.out.println(matches+"isValidRequest======"+isValidRequest);
            if (blockedNumber)
                return "fail";

            String otp = new DecimalFormat("0000").format(new Random().nextInt(9999));

            int otpResponseCode = this.commonService.callGetUrl("https://2factor.in/API/V1/14d7ca52-ed39-11ec-9c12-0200cd936042/SMS/" + mobile + "/" + otp);

            if (otpResponseCode == 200) {
                OTP findOtp = this.otpService.findOtpByMobile(mobile);
                if (findOtp == null)
                    this.otpService.saveOtp(new OTP(mobile, otp, this.commonService.getDateTime(), 1, true, location, name, 2));
                else {
                    findOtp.setOtpCode(otp);
                    findOtp.setOtpTimes(findOtp.getOtpTimes() + 1);
                    findOtp.setDateTime(this.commonService.getDateTime());
                    findOtp.setStatus(true);
                    findOtp.setName(name);
                    findOtp.setDeliveryStatus(2);
                    findOtp.setLocation(location);
                    this.otpService.saveOtp(findOtp);
                }
                return ("pass");
            } else {
                return ("fail");
            }
        } else
            return ("fail");
    }

    @GetMapping("/enquiry/verify-otp/{mobile}/{otp}")
    @ResponseBody
    public String verifyOtp(@PathVariable("mobile") String mobile, @PathVariable("otp") String otp) {
        if (otp != null && otp.length() > 0 && mobile != null && !mobile.equalsIgnoreCase("NA")) {
//			boolean matches = mobile.substring(mobile.length()-10).matches("[6-9][0-9]{9}");
            boolean blockedNumber = enquiryService.isValidRequest(mobile, "NA");
            if (blockedNumber)
                return "fail";

            OTP findOtp = this.otpService.findByMobileContainingAndOtpCodeAndStatus(mobile, otp, true);
            if (findOtp == null)
                return "fail";
            else
                return "pass";
        } else
            return ("fail");
    }

    @GetMapping("/news")
    public String learning(Model model, @RequestParam("page") Optional<Integer> page,
                           @RequestParam("size") Optional<Integer> size, @RequestParam("filter") Optional<String> filter) {
        int currentPage = page.orElse(1);
        int pageSize = size.orElse(6);
        String slug = filter.orElse("NA");
        NewsCategory category = null;
        if (!slug.equalsIgnoreCase("NA")) {
            category = this.newsCategoryService.findBySlug(slug);
        }

        StaticSeo staticSeo = this.staticSeoService.findByPage("news");
        if (staticSeo != null) {
            model.addAttribute("title", staticSeo.getMetaTitle());
            model.addAttribute("metaDescription", staticSeo.getMetaDescription());
            model.addAttribute("metaKeyword", staticSeo.getMetaKeyword());
        }
        Page<News> allNewsByStatus = null;
        Pageable pageable = PageRequest.of(currentPage - 1, pageSize, Sort.by("id").descending());
        if (category == null) {
            allNewsByStatus = this.newsService.findByDisplayStatusAndDeleteStatus("1", 2, pageable);
        } else {
            allNewsByStatus = this.newsService.findByCategoryAndDisplayStatusAndDeleteStatus(category, "1", 2, pageable);
        }

        model.addAttribute("cpage", currentPage);
        model.addAttribute("news", allNewsByStatus);

        int totalPages = 0;
        if (!allNewsByStatus.isEmpty())
            totalPages = allNewsByStatus.getTotalPages();
        int startRange = currentPage - 2;
        int endRange = currentPage + 2;

        if (totalPages > 1) {
            if ((endRange - 2) == totalPages) startRange = currentPage - 4;
            if (startRange == currentPage) endRange = currentPage + 4;
            if (startRange < 1) {
                startRange = 1;
                endRange = startRange + 4;
            }
            if (endRange > totalPages) {
                endRange = totalPages;
                startRange = endRange - 4;
            }
            if (startRange < 1) startRange = 1;

            List<Integer> pageNumbers = IntStream.rangeClosed(startRange, endRange)
                    .boxed()
                    .collect(Collectors.toList());
            model.addAttribute("pageNumbers", pageNumbers);
        }
        model.addAttribute("topCategory", this.newsCategoryService.findTop10ByVisited());
        model.addAttribute("totalPages", totalPages);
        model.addAttribute("topNews", this.newsService.findTop5ByDisplayStatusAndDeleteStatusOrderByVisitedDesc("1", 2));
        return "users/news";
    }

    @GetMapping(value = "/news/{slug}")
    public String learningDetails(@PathVariable("slug") String slug, Model model, HttpServletRequest req) {
//		System.out.println("blog slug-==="+slug);
        News news = this.newsService.findBySlug(slug);
//		System.out.println("blog object==="+blog);
        if (news == null)
            return "error/404";

        model.addAttribute("title", news.getMetaTitle());
        model.addAttribute("metaDescription", news.getMetaDescription());
        model.addAttribute("metaKeyword", news.getMetaKeyword());
        model.addAttribute("news", news);
        model.addAttribute("newsUser", this.userService.findByIdAndDeleteStatus(news.getAuthorId(), 2));
        model.addAttribute("topNews", this.newsService.findTop5ByDisplayStatusAndDeleteStatusOrderByVisitedDesc("1", 2));
        model.addAttribute("latestNews", this.newsService.findTop5ByDisplayStatusAndDeleteStatusOrderByIdDesc("1", 2));
        model.addAttribute("topBlog", this.blogService.findTop5ByDisplayStatusAndDeleteStatusOrderByVisitedDesc("1", 2));
        model.addAttribute("latestBlog", this.blogService.findTop5ByDisplayStatusAndDeleteStatusOrderByIdDesc("1", 2));
        model.addAttribute("feedback", this.feedbackService.findByIpAndUrl(this.commonService.getIpAddress(req), domain + "/news/" + slug));
        return "users/news-details";

    }

    @PostMapping("/feedback/{type}")
    public @ResponseBody
    void saveRating(@PathVariable("type") String type,
                    @RequestParam("comment") String comment, @RequestParam("ratingValue") String ratingValue
            , @RequestParam("location") String location, PrintWriter pw, HttpServletRequest req, HttpSession session) {
        try {
            UserAgent userAgent = UserAgent.parseUserAgentString(req.getHeader("User-Agent"));
            OperatingSystem operatingSystem = userAgent.getOperatingSystem();
            Browser browser = userAgent.getBrowser();
            String browserName = browser.getName();
            Version browserVersion = userAgent.getBrowserVersion();
            String ipAddress = this.commonService.getIpAddress(req);

            Feedback saveFeedback = this.feedbackService.saveFeedback(new Feedback(0, type, ratingValue, comment, this.commonService.getToday(),
                    ipAddress, location, browserName + " " + browserVersion, session.getId(), operatingSystem.getName()));
            if (saveFeedback != null) pw.write("pass");
            else pw.write("fail");
        } catch (Exception e) {
            e.printStackTrace();
            pw.write("fail");
        }
    }

    /*@GetMapping("/{state}/{slug}")
    public String childService(@PathVariable("state") String state,
                                @PathVariable("slug") String slug, Model model) {

//        System.out.println(state+"\t"+slug);

        Services service = this.servicesService.findBySlug(slug);
        if (service == null) return "error/404";

        if (service != null) {
            model.addAttribute("title", service.getMetaTitle());
            model.addAttribute("metaDescription", service.getMetaDescription());
            model.addAttribute("metaKeyword", service.getMetaKeyword());
            model.addAttribute("service", service);
            model.addAttribute("category",this.categoryService.findByService(service));
            model.addAttribute("serviceBrands", this.serviceBrand.findByServicesAndDeleteStatus(service, 2));
            model.addAttribute("serviceDetails", this.servicesService.findServiceDetailsByServicesAndDeleteStatus(service, 2));
            model.addAttribute("reviews", this.masterService.findByShowHomeStatusAndDisplayStatus("1", "1"));
            model.addAttribute("testimonials", this.testimonialService.findTestimonialByServiceAndDeleteStatus(service, 2));


            String jsonString = "{'@context': 'https://schema.org','@type': 'FAQPage','mainEntity': [";
            List<ServiceFaq> serviceFaq = this.servicesService.findFaqByServiceAndStatus(service, 2);
            int size = serviceFaq.size();
            for (ServiceFaq sf : serviceFaq) {
                size--;
                jsonString += "{'@type':'Question','name':'" + sf.getTitle().replace("'", "") + "','acceptedAnswer':{'@type':'Answer','text':'" + sf.getDescription().replace("'", "") + "'}}";
                if (size > 0) jsonString += ",";
            }
            jsonString += "]}";
            JsonObject jsonObject = (JsonObject) JsonParser.parseString(jsonString.toString());

            model.addAttribute("faqs", serviceFaq);
            model.addAttribute("faqs1", jsonObject);
            model.addAttribute("enquiry", new Enquiry());
            model.addAttribute("servicePackage", this.servicesService.getPackagesByServices(service));
            model.addAttribute("subService", this.subServiceService.findByParentServiceUuidAndDeleteStatusAndDisplayStatus(service.getUuid(), 2, 1));
            model.addAttribute("contacts", this.serviceContactService.findByServiceAndDeleteStatusAndDisplayStatus(service, 2, 1));

            return "users/services";
        } else {
            return "error/404";
        }
    }*/

    @GetMapping(value = "/sitemap")
    public String sitemap(Model model) {
        model.addAttribute("title", "Sitemap - Corpseed.com");
        model.addAttribute("metaDescription", "sitemap");
        model.addAttribute("metaKeyword", "sitemap");
        return "users/sitemap";
    }
}
