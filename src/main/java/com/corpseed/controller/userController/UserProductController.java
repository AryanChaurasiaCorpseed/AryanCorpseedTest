package com.corpseed.controller.userController;

import com.corpseed.entity.StaticSeo;
import com.corpseed.entity.productentity.Product;
import com.corpseed.entity.serviceentity.Services;
import com.corpseed.service.FeedbackService;
import com.corpseed.service.ProductService;
import com.corpseed.serviceImpl.CommonServices;
import com.corpseed.serviceImpl.StaticSeoService;
import com.corpseed.serviceImpl.blogserviceimpl.BlogService;
import com.corpseed.serviceImpl.servicesserviceimpl.ServicesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/product")
public class UserProductController {

    @Autowired
    private StaticSeoService staticSeoService;

    @Autowired
    private ProductService productService;

    @Autowired
    private BlogService blogService;

    @Autowired
    private FeedbackService feedbackService;

    @Autowired
    private CommonServices commonServices;

    @Autowired
    private ServicesService servicesService;

    @Value("${domain.name}")
    private String domain;

    @Value("${azure_path}")
    private String blobPath;

    @GetMapping("/all")
    public String allProducts(Model model){

        model.addAttribute("title", "Corpseed | All Products");

        StaticSeo staticSeo = this.staticSeoService.findByPage("product-all");
        if (staticSeo != null) {
            model.addAttribute("title", staticSeo.getMetaTitle());
            model.addAttribute("metaDescription", staticSeo.getMetaDescription());
            model.addAttribute("metaKeyword", staticSeo.getMetaKeyword());
        }


        List<Services> productServices=this.servicesService.findAllProductServicesByDeleteStatus(2,"1");
        List<Product> top12Product = this.productService.getTopServiceNameByVisitedStatus(2);
        model.addAttribute("activeService",null);
        model.addAttribute("productServices",productServices);
        model.addAttribute("top12Product",top12Product);

        return "users/product/product-all";
    }

    @GetMapping("/service/{slug}")
    public String productDetails(@PathVariable("slug") String slug, Model model){
        Services services=this.servicesService.findBySlug(slug);
        if(services==null)
            return "error/404";

        model.addAttribute("title", services.getMetaTitle());
        model.addAttribute("metaDescription", services.getMetaDescription());
        model.addAttribute("metaKeyword", services.getMetaKeyword());

        List<Services> productServices=this.servicesService.findAllProductServicesByDeleteStatus(2,"1");
        Product product = this.productService.findBySlugAndDeleteStatus(slug);

//      List<Product> top12Product = this.productService.getTopServiceNameByVisitedStatus(2);
        if(product!=null) {
        model.addAttribute("title", product.getMetaTitle());
        model.addAttribute("metaDescription", product.getMetaDescription());
        model.addAttribute("metaKeyword", product.getMetaKeyword());
        model.addAttribute("activeService",services);
        model.addAttribute("productServices",productServices);
        model.addAttribute("top12Product",services.getProductList());
        }
        return "users/product/product-all";
    }

    @GetMapping("/{slug}")
    public String getProductSlug(@PathVariable("slug") String slug, Model model, HttpServletRequest req)
    {
        Product product = this.productService.findBySlugAndDeleteStatus(slug);
        List<Product> top4Product = this.productService.getTopServiceNameByVisitedStatus(2);
        if (product == null)
            return "error/404";

        model.addAttribute("title", product.getMetaTitle());
        model.addAttribute("metaDescription", product.getMetaDescription());
        model.addAttribute("metaKeyword", product.getMetaKeyword());
        model.addAttribute("products", product);
        model.addAttribute("activeService", slug);
        model.addAttribute("categoryId", product.getId());
        model.addAttribute("topBlog", this.blogService.findTop5ByDisplayStatusAndDeleteStatusOrderByVisitedDesc("1", 2));
        model.addAttribute("latestBlog", this.blogService.findTop5ByDisplayStatusAndDeleteStatusOrderByIdDesc("1", 2));
        model.addAttribute("feedback", this.feedbackService.findByIpAndUrl(this.commonServices.getIpAddress(req), domain + "/press/" + slug));
        model.addAttribute("topproduct", top4Product);

        return "users/product/product-details";
    }

    @PostMapping("/visitor")
    @ResponseBody
    public void savePressVisitors(@RequestParam("slug") String slug) {
        Product product = this.productService.findBySlug(slug);
        if (product != null) {
            product.setVisited(product.getVisited()+1);
            this.productService.saveProduct(product);
        }

    }

    @GetMapping("/product-card")
    @ResponseBody
    public String geProductCardDetails(@RequestParam("slugs") String slugs, @RequestParam("location") String location) {
        StringBuffer data=new StringBuffer();

        if(slugs!=null&&slugs.length()>0) {
            List<String> slugList = Arrays.asList(slugs.split("#"))
                    .stream().collect(Collectors.toList());

            List<Product> productList = this.productService.findBySlugIn(slugList);

            if (productList != null&&!productList.isEmpty()) {
                data.append("<div class=\"product-cards\">\n" +
                        "            <div class=\"row justify-content-center\">\n" +
                        "                <div class=\"col-xl-12\">                  \n" +
                        "                    <div class=\"owl-carousel owl-theme productSlider\">");
                productList.forEach(p-> data.append("<div class=\"card\">\n" +
                        "                            <div class=\"card-body min-195\" style='padding: 0.5rem;'><a href=\""+domain+"/product/"+p.getSlug()+"\" class=\"text-black\" href=\"#\">\n" +
                        "                                <div class=\"service-image-box\"><img class=\"card-bodyImg card-img-top\" src=\"" + blobPath+p.getImage() + "\" alt=\"img\"></div>\n" +
                        "                                <span class=\"card-title text-left line-2\">" + p.getTitle() + "</span>\n" +
                        "                            </a></div>\n" +
                        "                        </div>\n"));
                data.append("</div>\n" +
                        "                </div>\n" +
                        "            </div>\n" +
                        "        </div>");
            }
        }
        return data.toString();
    }
}
