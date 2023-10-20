package com.corpseed.controller;

import com.corpseed.entity.History;
import com.corpseed.entity.SiteMapUrl;
import com.corpseed.entity.User;
import com.corpseed.entity.productentity.Product;
import com.corpseed.entity.serviceentity.Services;
import com.corpseed.service.ProductService;
import com.corpseed.serviceImpl.*;
import com.corpseed.serviceImpl.servicesserviceimpl.ServicesService;
import com.corpseed.util.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.security.Principal;
import java.util.ArrayList;
import java.util.List;


@Controller
@RequestMapping("/admin/product")
public class ProductController {

    @Autowired
    private ProductService productService;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private ServicesService serviceService;

    @Autowired
    AzureBlobAdapter azureAdapter;

    @Autowired
    private UserServices userService;

    @Autowired
    private CommonServices commonService;

    @Autowired
    private HistoryService historyService;

    @Autowired
    private SiteMapService siteMapService;

    @Autowired
    private IndustryService industryService;


//    @GetMapping("/add")
//    public String addBlog(Model model) {
//
//        Product product = new Product(serviceName);
//        model.addAttribute("product",product);
//        return "admin/product-add";
//    }
////
//    @PostMapping("/add")
//    public ResponseEntity<String> saveProduct(@ModelAttribute Product product) {
//        System.out.println(product);
//
//        productService.saveProduct(product);
//        return ResponseEntity.ok("Product created");
//    }

    @GetMapping()
    public String allProducts(Model model) {
        model.addAttribute("title", "Corpseed Dashboard | Manage Products");
        model.addAttribute("appendClass", "product");
        model.addAttribute("product", productService.getAllProducts());
        return "admin/product/product";
    }

    @GetMapping("/add")
    public String addBlog(Model model) {

        List<Product> allproducts = this.productService.getAllProducts();

        model.addAttribute("title", "Corpseed Dashboard | Add Product");
        model.addAttribute("appendClass", "product");
        model.addAttribute("product", new Product());
        model.addAttribute("products", allproducts);

        model.addAttribute("services", this.serviceService.getAllServicesByStatus("1"));
        model.addAttribute("category", this.categoryService.getAllSubCategory());
        model.addAttribute("users", this.userService.findAllNotUserAndAccountStatus());
        return "admin/product/product-add";
    }

    @PostMapping("/add")
    public String saveProduct(@Valid @ModelAttribute("product") Product product,
                              BindingResult result, HttpSession session, Model model,
                              @RequestParam("file") MultipartFile file) {
        try {

            if (result.hasErrors()) {
                model.addAttribute("title", "Corpseed Dashboard | Add Product");
                model.addAttribute("appendClass", "product");
                model.addAttribute("users", this.userService.findAllNotUserAndAccountStatus());
                model.addAttribute("services", this.serviceService.getAllServicesByStatus("1"));
                model.addAttribute("category", this.categoryService.getAllSubCategory());
                model.addAttribute("product", product);
                session.setAttribute("message", new Message("Please fill out the blank fields !!", "alert-danger"));
                return "admin/product/product-add";
            }

            Product findProduct = this.productService.findBySlug(product.getSlug());

            if (findProduct != null) {
                model.addAttribute("title", "Corpseed Dashboard | Add Product");
                model.addAttribute("appendClass", "product");
                model.addAttribute("users", this.userService.findAllNotUserAndAccountStatus());
                model.addAttribute("services", this.serviceService.getAllServicesByStatus("1"));
                model.addAttribute("category", this.categoryService.getAllSubCategory());
                model.addAttribute("product", product);
                if (findProduct.getDeleteStatus() == 1)
                    session.setAttribute("message", new Message("Product Exist in Trash !!", "alert-danger"));
                else
                    session.setAttribute("message", new Message("Product Already Exist !!", "alert-danger"));
                return "admin/product/product-add";
            }

            if (!file.isEmpty()) {
                String name = azureAdapter.upload(file, 0);
                product.setImage(name);
            }
            productService.saveProduct(product);
            return "redirect:/admin/product";

        } catch (Exception e) {
            e.printStackTrace();
            model.addAttribute("title", "Corpseed Dashboard | Add Product");
            model.addAttribute("appendClass", "product");
            model.addAttribute("users", this.userService.findAllNotUserAndAccountStatus());
            model.addAttribute("services", this.serviceService.getAllServicesByStatus("1"));
            model.addAttribute("category", this.categoryService.getAllSubCategory());
            model.addAttribute("product", product);
            session.setAttribute("message", new Message("Error : " + e.getMessage(), "alert-danger"));
            return "admin/product/product-add";
        }
    }

    //Here it will take product unique ID and fetch all data in UI table
    @GetMapping("/edit/{id}")
    public String editPackage(@PathVariable("id") Long id, Model model) {
        model.addAttribute("title", "Corpseed Dashboard | Add Product");
        model.addAttribute("appendClass", "product");

        List<Services> serviceList=this.serviceService.getAllServicesByStatus("1");
        model.addAttribute("services1", serviceList);
        List<Services> serviceList1=new ArrayList<>();
        serviceList1.addAll(serviceList);
        model.addAttribute("products", this.productService.getAllProducts());

        Product product = this.productService.getProductById(id);
        model.addAttribute("users", this.userService.findAllNotUserAndAccountStatus());
        model.addAttribute("services", this.serviceService.getAllServicesByStatus("1"));
        model.addAttribute("category", this.categoryService.getAllSubCategory());
        model.addAttribute("product", product);
        return "admin/product/product-edit";
    }

//    @PostMapping("/update")
//    public String updateBlog(@Valid @ModelAttribute("product") Product product, BindingResult result,
//                             HttpSession session, Model model,@RequestParam("file") MultipartFile file) {
//
//        System.out.println(product);
//        try {
//            if (result.hasErrors()) {
//
//                model.addAttribute("title", "Corpseed Dashboard | Add Product");
//                model.addAttribute("appendClass", "product");
//                model.addAttribute("users", this.userService.findAllNotUserAndAccountStatus());
//                model.addAttribute("services", this.serviceService.getAllServicesByStatus("1"));
//                model.addAttribute("category", this.categoryService.getAllSubCategory());
//                model.addAttribute("product", product);
//                session.setAttribute("message", new Message("Please fill out the blank fields !!", "alert-danger"));
//                return "admin/product/product-edit";
//            }
//
//            productService.saveProduct(product);
//
//            return "redirect:/admin/product/product";
//
//        } catch (Exception e) {
//            e.printStackTrace();
//            model.addAttribute("title", "Corpseed Dashboard | Add Product");
//            model.addAttribute("appendClass", "product");
//            model.addAttribute("users", this.userService.findAllNotUserAndAccountStatus());
//            model.addAttribute("services", this.serviceService.getAllServicesByStatus("1"));
//            model.addAttribute("category", this.categoryService.getAllSubCategory());
//            model.addAttribute("product", product);
//            session.setAttribute("message", new Message("Error : " + e.getMessage(), "alert-danger"));
//            return "admin/product/product-add";
//        }
//    }


    @PostMapping("/update")
    public String updateProduct(@Valid @ModelAttribute("product") Product product, Long id ,
                                BindingResult result, HttpSession session, Model model,
                                @RequestParam("file") MultipartFile file, Principal principal) {
        try {
            model.addAttribute("title", "Corpseed Dashboard | Edit Product");
            model.addAttribute("appendClass", "blogs");
            model.addAttribute("users", userService.findAllNotUserAndAccountStatus());

            if (result.hasErrors()) {
                model.addAttribute("services", serviceService.getAllServicesByStatus("1"));
                model.addAttribute("industries", industryService.getIndustryByDisplsyStatus("1"));
                model.addAttribute("category", categoryService.getAllSubCategory());
                model.addAttribute("products", product);
                session.setAttribute("message", new Message("Please fill out the required fields!", "alert-danger"));
                return "admin/product/product-edit";
            }

            Product existingProduct = productService.getProductBySlugAndTitle(product.getSlug(),product.getTitle(),product.getId());
            if (existingProduct == null) {
                session.setAttribute("message", new Message("Product not found!", "alert-danger"));
                return "admin/product/product-edit";
            }
            existingProduct.setName(product.getName());
            existingProduct.setTitle(product.getTitle());
            existingProduct.setSlug(product.getSlug());
            existingProduct.setSummary(product.getSummary());
            existingProduct.setDescription(product.getDescription());
            existingProduct.setMetaTitle(product.getMetaTitle());
            existingProduct.setMetaKeyword(product.getMetaKeyword());
            existingProduct.setMetaDescription(product.getMetaDescription());
            existingProduct.setUser(product.getUser());
            existingProduct.setImage(product.getImage());
            existingProduct.setCategory(product.getCategory());
            existingProduct.setServices(product.getServices());

            if (!file.isEmpty()) {
                String oldImage = existingProduct.getImage();
                azureAdapter.deleteFile(oldImage);

                String newImage = azureAdapter.upload(file, 0);
                existingProduct.setImage(newImage);
            }

            // Save the updated product
            Product savedProduct = productService.saveProduct(existingProduct);
            if (savedProduct == null) {
                session.setAttribute("message", new Message("Failed to update the product!", "alert-danger"));
                return "admin/product/product-edit";
            }

            return "redirect:/admin/product";
        } catch (Exception e) {
            e.printStackTrace();
            model.addAttribute("services", serviceService.getAllServicesByStatus("1"));
            model.addAttribute("industries", industryService.getIndustryByDisplsyStatus("1"));
            model.addAttribute("category", categoryService.getAllSubCategory());
            model.addAttribute("products", product);
            session.setAttribute("message", new Message("Error: " + e.getMessage(), "alert-danger"));
            return "admin/product/product-edit";
        }
    }

    @GetMapping("/delete/{id}")
    public String deleteProduct(@PathVariable("id") Long id, Principal principal, HttpServletRequest req) {
        Product product = this.productService.getProductById(id);

        //Here we are checking the role which got fetch form UI is equal or not with ROLE of Admin
        if (product != null && this.commonService.getUserRole(principal).equals("ROLE_ADMIN")) {
            product.setDeleteStatus(1);
            this.productService.saveProduct(product);

            User user = this.commonService.getLoginedUser(principal);

            //adding category in history
            this.historyService.savehistory(new History(0, this.commonService.getUUID(),
                    "Product", product.getId(), this.commonService.getBrowser(req),
                    this.commonService.getIpAddress(req), user.getId(), user.getFirstName() + " " + user.getLastName()
                    , this.commonService.getToday(), this.commonService.getTime(), product.getTitle()));
        }

        //updating sitemap url
        SiteMapUrl siteMap = this.siteMapService.findByTypeAndUid("Product", product.getId());
       if(siteMap==null)
           return "error/404";

        siteMap.setStatus(2);
        this.siteMapService.saveUrl(siteMap);
        return "redirect:/admin/product";

    }

}

