package com.corpseed.controller.userController;

import com.corpseed.entity.StaticSeo;
import com.corpseed.entity.blogentity.Blogs;
import com.corpseed.entity.newsentity.News;
import com.corpseed.entity.pressentity.Press;
import com.corpseed.entity.pressentity.PressCategory;
import com.corpseed.service.FeedbackService;
import com.corpseed.service.pressservice.PressCategoryService;
import com.corpseed.service.pressservice.PressService;
import com.corpseed.serviceImpl.CommonServices;
import com.corpseed.serviceImpl.StaticSeoService;
import com.corpseed.serviceImpl.blogserviceimpl.BlogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Controller
@RequestMapping("/press-release")
public class UserPressReleaseController {

    @Autowired
    private PressCategoryService pressCategoryService;

    @Autowired
    private StaticSeoService staticSeoService;

    @Autowired
    private PressService pressService;

    @Autowired
    private BlogService blogService;

    @Autowired
    private FeedbackService feedbackService;

    @Autowired
    private CommonServices commonServices;

    @Value("${domain.name}")
    private String domain;


    @GetMapping()
    public String pressRelease(Model model, @RequestParam("page") Optional<Integer> page,
                               @RequestParam("size") Optional<Integer> size,
                               @RequestParam("filter") Optional<String> filter) {
        int currentPage = page.orElse(1);
        int pageSize = size.orElse(6);
        String slug = filter.orElse("NA");
        PressCategory category = null;
        if (!slug.equalsIgnoreCase("NA")) {
            category = this.pressCategoryService.findBySlug(slug);
        }

        StaticSeo staticSeo = this.staticSeoService.findByPage("press");
        if (staticSeo != null) {
            model.addAttribute("title", staticSeo.getMetaTitle());
            model.addAttribute("metaDescription", staticSeo.getMetaDescription());
            model.addAttribute("metaKeyword", staticSeo.getMetaKeyword());
        }

        Page<Press> allPressByStatus = null;
        Pageable pageable = PageRequest.of(currentPage - 1, pageSize, Sort.by("id").descending());
        if (category == null) {
            allPressByStatus = this.pressService.findByDisplayStatusAndDeleteStatus("1", 2, pageable);
        } else {
            allPressByStatus = this.pressService.findByCategoryAndDisplayStatusAndDeleteStatus(category, "1", 2, pageable);
        }

        model.addAttribute("cpage", currentPage);
        model.addAttribute("press", allPressByStatus);

        int totalPages = allPressByStatus.getTotalPages();

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
            model.addAttribute("topCategory", this.pressCategoryService.findTop10ByVisited());
            model.addAttribute("totalPages", totalPages);
            model.addAttribute("topPress", this.pressService.findTop5ByDisplayStatusAndDeleteStatusOrderByVisitedDesc("1", 2));

            return "users/press/press-release";
    }


    @GetMapping("/{slug}")
    public String learningDetails(@PathVariable("slug") String slug, Model model, HttpServletRequest req) {
//		System.out.println("blog slug-==="+slug);
        Press press = this.pressService.findBySlug(slug);
//		System.out.println("blog object==="+blog);
        if (press == null)
            return "error/404";

        model.addAttribute("title", press.getMetaTitle());
        model.addAttribute("metaDescription", press.getMetaDescription());
        model.addAttribute("metaKeyword", press.getMetaKeyword());
        model.addAttribute("press", press);
//        model.addAttribute("pressUser", this.userService.findByIdAndDeleteStatus(press.getAuthorId(), 2));
        model.addAttribute("topPress", this.pressService.findTop5ByDisplayStatusAndDeleteStatusOrderByVisitedDesc("1", 2));
        model.addAttribute("latestPress", this.pressService.findTop5ByDisplayStatusAndDeleteStatusOrderByIdDesc("1", 2));
        model.addAttribute("topBlog", this.blogService.findTop5ByDisplayStatusAndDeleteStatusOrderByVisitedDesc("1", 2));
        model.addAttribute("latestBlog", this.blogService.findTop5ByDisplayStatusAndDeleteStatusOrderByIdDesc("1", 2));
        model.addAttribute("feedback", this.feedbackService.findByIpAndUrl(this.commonServices.getIpAddress(req), domain + "/press/" + slug));

        return "users/press/press-detail";
    }

    @GetMapping("/search/{value}")
    @ResponseBody
    public Map<String, String> searchPress(@PathVariable("value") String value) {
       return this.pressService.findByTitleContainingAndDeleteStatus(value,2)
               .stream()
               .collect(Collectors.toMap(p->p.getSlug(),p->p.getTitle()));
    }

    @PostMapping("/visitor")
    @ResponseBody
    public void savePressVisitors(@RequestParam("slug") String slug) {

        try {
            Press press = this.pressService.findBySlug(slug);
            if (press != null) {
                press.setVisited(press.getVisited()+1);
                this.pressService.savePress(press);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
