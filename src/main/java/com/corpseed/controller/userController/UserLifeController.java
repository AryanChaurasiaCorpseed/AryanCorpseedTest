package com.corpseed.controller.userController;

import com.corpseed.entity.StaticSeo;
import com.corpseed.entity.hrmentity.HrmBlog;
import com.corpseed.entity.lifeentity.LifeCategory;
import com.corpseed.entity.lifeentity.LifeUser;
import com.corpseed.service.FeedbackService;
import com.corpseed.service.hrmservice.HrmBlogService;
import com.corpseed.service.lifeatcorpseedservice.LifeCategoryService;
import com.corpseed.service.lifeatcorpseedservice.LifeUserService;
import com.corpseed.service.lifeatcorpseedservice.LifeVideoService;
import com.corpseed.service.newsservice.NewsService;
import com.corpseed.serviceImpl.CommonServices;
import com.corpseed.serviceImpl.StaticSeoService;
import com.corpseed.serviceImpl.blogserviceimpl.BlogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
@RequestMapping("/life-at-corpseed")
public class UserLifeController {

    @Autowired
    private StaticSeoService staticSeoService;

    @Autowired
    private LifeCategoryService lifeCategoryService;

    @Autowired
    private LifeUserService lifeUserService;

    @Autowired
    private LifeVideoService lifeVideoService;

    @Autowired
    private HrmBlogService hrmBlogService;

    @Autowired
    private CommonServices commonServices;

    @Autowired
    private FeedbackService feedbackService;

    @Autowired
    private NewsService newsService;

    @Autowired
    private BlogService blogService;


    @GetMapping()
    public String lifeAtCorpseed(Model model) {
        try {
            StaticSeo staticSeo=this.staticSeoService.findByPage("life-at-corpseed");
            if(staticSeo!=null) {
                model.addAttribute("title",staticSeo.getMetaTitle());
                model.addAttribute("metaDescription",staticSeo.getMetaDescription());
                model.addAttribute("metaKeyword",staticSeo.getMetaKeyword());
            }
            model.addAttribute("lifeVideo",this.lifeVideoService.findByDisplayStatusAndDeleteStatus("1",2));
            model.addAttribute("activeTag",null);
            model.addAttribute("tags",this.lifeCategoryService.findByDisplayStatusAndDeleteStatus("1",2));
            model.addAttribute("lifeUsers",this.lifeUserService.findTop4LifeUserByDisplayStatusAndDeleteStatus("1",2));
            model.addAttribute("blogs",this.hrmBlogService.findTop6ByDisplayStatusAndDeleteStatus(1,2));

        }catch(Exception e) {
            e.printStackTrace();
        }
        return "users/life";
    }

    @GetMapping("/tag/{tag}")
    public String lifeAtCorpseedTag(@PathVariable("tag") String tag, Model model) {
        StaticSeo staticSeo=this.staticSeoService.findByPage("life-at-corpseed-tag");
        if(staticSeo!=null) {
            model.addAttribute("title",staticSeo.getMetaTitle());
            model.addAttribute("metaDescription",staticSeo.getMetaDescription());
            model.addAttribute("metaKeyword",staticSeo.getMetaKeyword());
        }
        LifeCategory lifeCategory=this.lifeCategoryService.findLifeCategoryBySlug(tag);
        if(lifeCategory==null)
            return "error/404";

        List<LifeUser> lifeUserList=this.lifeUserService.findTop4ByLifeCategoriesAndDeleteStatusAndDisplayStatus(lifeCategory,"1",2);
        model.addAttribute("lifeUsers",lifeUserList);
        model.addAttribute("activeTag",tag);
        model.addAttribute("tags",this.lifeCategoryService.findByDisplayStatusAndDeleteStatus("1",2));

        return "users/life-tag";
    }

    @GetMapping("/{slug}")
    public String lifeAtCorpseedProfile(@PathVariable("slug") String slug, Model model) {
        StaticSeo staticSeo=this.staticSeoService.findByPage("life-at-corpseed-tag");
        if(staticSeo!=null) {
            model.addAttribute("title",staticSeo.getMetaTitle());
            model.addAttribute("metaDescription",staticSeo.getMetaDescription());
            model.addAttribute("metaKeyword",staticSeo.getMetaKeyword());
        }

        LifeUser lifeUser=this.lifeUserService.findBySlug(slug);
        if(lifeUser==null)
            return "error/404";

        List<LifeUser> lifeUserList=this.lifeUserService.findTop4ByLifeCategoriesInAndDeleteStatusAndDisplayStatus(lifeUser.getLifeCategories(),"1",2);
        model.addAttribute("lifeUser",lifeUser);
        model.addAttribute("lifeUsers",lifeUserList);
        model.addAttribute("activeTag",null);
        model.addAttribute("tags",this.lifeCategoryService.findByDisplayStatusAndDeleteStatus("1",2));

        return "users/life-user-details";
    }
    @GetMapping("/blog/{slug}")
    public String lifeAtCorpseedBlog(@PathVariable("slug") String slug, Model model, HttpServletRequest req) {

        HrmBlog hrmBlog=this.hrmBlogService.findBySlug(slug);
        if(hrmBlog==null)
            return "error/404";

        model.addAttribute("title", hrmBlog.getMetaTitle());
        model.addAttribute("metaDescription", hrmBlog.getMetaDescription());
        model.addAttribute("metaKeyword", hrmBlog.getMetaKeyword());
        model.addAttribute("blogs", hrmBlog);
//        model.addAttribute("legalBlog", this.hrmBlogService.findTop8ByCategoryAndDisplayStatusAndUuidNotOrderByIdDesc(hrmBlog.getCategory(), "1", blog.getUuid()));
        model.addAttribute("feedback", this.feedbackService.findByIpAndUrl(this.commonServices.getIpAddress(req), "https://www.corpseed.com//knowledge-centre/" + slug));

        model.addAttribute("topNews", this.newsService.findTop5ByDisplayStatusAndDeleteStatusOrderByVisitedDesc("1", 2));
        model.addAttribute("latestNews", this.newsService.findTop5ByDisplayStatusAndDeleteStatusOrderByIdDesc("1", 2));
        model.addAttribute("topBlog", this.blogService.findTop5ByDisplayStatusAndDeleteStatusOrderByVisitedDesc("1", 2));
        model.addAttribute("latestBlog", this.blogService.findTop5ByDisplayStatusAndDeleteStatusOrderByIdDesc("1", 2));

        return "users/life-blog";
    }
}
