package com.corpseed.controller.lifecontroller;

import com.corpseed.entity.lifeentity.LifeCategory;
import com.corpseed.entity.lifeentity.LifeUser;
import com.corpseed.util.Message;
import com.corpseed.service.lifeatcorpseedservice.LifeCategoryService;
import com.corpseed.service.lifeatcorpseedservice.LifeUserService;
import com.corpseed.serviceImpl.AzureBlobAdapter;
import com.corpseed.serviceImpl.CommonServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/admin/life/user")
public class LifeUserController {

    @Autowired
    private LifeUserService lifeUserService;

    @Autowired
    private AzureBlobAdapter azureBlobAdapter;

    @Autowired
    private CommonServices commonServices;

    @Autowired
    private LifeCategoryService lifeCategoryService;

    @GetMapping()
    public String lifeCategory(Model model){
        List<LifeUser> lifeUserList=this.lifeUserService.findAll();
        model.addAttribute("lifeUsers",lifeUserList);
        model.addAttribute("title", "Corpseed Dashboard | Manage Life-at-corpseed Users");
        model.addAttribute("appendClass", "life");
        return "admin/life-user";
    }

    @GetMapping("/add")
    public String addLifeUser(Model model){
        model.addAttribute("lifeUser",new LifeUser());
        model.addAttribute("title", "Corpseed Dashboard | Add Life-at-corpseed User");
        model.addAttribute("appendClass", "life");
        model.addAttribute("tags",this.lifeCategoryService.findByDisplayStatus("1"));
        return "admin/life-user-add";
    }

    @GetMapping("/edit/{id}")
    public String editLifeUser(@PathVariable("id") int id, Model model){
        LifeUser lifeUser = this.lifeUserService.findById(id);
        List<LifeCategory> lifeCategories = lifeUser.getLifeCategories();
        model.addAttribute("lifeUser",lifeUser);
        model.addAttribute("lifeCategories",lifeCategories);
        List<LifeCategory> lifeCategoryListNotIn = this.lifeCategoryService
                .findByDisplayStatus("1");
        lifeCategoryListNotIn.removeAll(lifeCategories);
        model.addAttribute("tagsNew",lifeCategoryListNotIn);
        model.addAttribute("title", "Corpseed Dashboard | Edit Life-at-corpseed User");
        model.addAttribute("appendClass", "life");

        return "admin/life-user-edit";
    }

    @PostMapping("/save")
    public String saveLifeUser(@Valid @ModelAttribute("lifeUser") LifeUser lifeUser,
                               @RequestParam("file") MultipartFile file,
                               @RequestParam("tags") List<Integer> tags,
                               BindingResult result, Model model, HttpSession session){
        if(result.hasErrors() || tags.isEmpty() || file.isEmpty()){
            model.addAttribute("lifeUser",lifeUser);
            model.addAttribute("title", "Corpseed Dashboard | Add Life-at-corpseed User");
            model.addAttribute("appendClass", "life");
            model.addAttribute("tags",this.lifeCategoryService.findByDisplayStatus("1"));
            session.setAttribute("message",new Message("Fill all the blank fields !!","alert-danger"));
            return "admin/life-user-add";
        }

        LifeUser duplicateLifeUser=this.lifeUserService.findBySlug(lifeUser.getSlug());
        if(duplicateLifeUser!=null){
            model.addAttribute("lifeUser",lifeUser);
            model.addAttribute("title", "Corpseed Dashboard | Add Life-at-corpseed User");
            model.addAttribute("appendClass", "life");
            model.addAttribute("tags",this.lifeCategoryService.findByDisplayStatus("1"));
            session.setAttribute("message",new Message("Duplicate Slug Entry !!","alert-danger"));
            return "admin/life-user-add";
        }

        lifeUser.setLifeCategories(tags.stream().map(this::fetchLifeTag)
                .filter(c -> c != null)
                .collect(Collectors.toList()));

        if(!file.isEmpty()){
            String fileName = this.azureBlobAdapter.upload(file, this.commonServices.getUniqueCode());
            lifeUser.setPictureName(fileName);
        }

        LifeUser saveLifeUser=this.lifeUserService.save(lifeUser);
        if(saveLifeUser==null){
            model.addAttribute("lifeUser",lifeUser);
            model.addAttribute("title", "Corpseed Dashboard | Add Life-at-corpseed User");
            model.addAttribute("appendClass", "life");
            model.addAttribute("tags",this.lifeCategoryService.findByDisplayStatus("1"));
            session.setAttribute("message",new Message("Something Went-Wrong, Please Try-again later !!","alert-danger"));
            return "admin/life-user-add";
        }

        return "redirect:/admin/life/user";
    }

    private LifeCategory fetchLifeTag(Integer id) {
        return this.lifeCategoryService.findById(id);
    }

    @PostMapping("/update")
    public String updateLifeUser(@Valid @ModelAttribute("lifeUser") LifeUser lifeUser,
                                 @RequestParam("file") MultipartFile file,
                                 @RequestParam("tags") List<Integer> tags,
                                 BindingResult result, Model model, HttpSession session){
        if(result.hasErrors() || tags.isEmpty() || (file.isEmpty())&&lifeUser.getPictureName()==null){
            model.addAttribute("lifeUser",lifeUser);
            model.addAttribute("title", "Corpseed Dashboard | Edit Life-at-corpseed User");
            model.addAttribute("appendClass", "life");
            session.setAttribute("message",new Message("Fill all the blank fields !!","alert-danger"));
            List<LifeCategory> lifeCategories =
                    this.lifeUserService.findById(lifeUser.getId()).getLifeCategories();
            model.addAttribute("lifeCategories",lifeCategories);
            List<LifeCategory> lifeCategoryListNotIn = this.lifeCategoryService
                    .findByDisplayStatus("1");
            lifeCategoryListNotIn.removeAll(lifeCategories);
            model.addAttribute("tagsNew",lifeCategoryListNotIn);

            return "admin/life-user-edit";
        }

        LifeUser duplicateUser=this.lifeUserService.findBySlugAndIdNot(lifeUser.getSlug(),lifeUser.getId());
        if(duplicateUser!=null){
            model.addAttribute("lifeUser",lifeUser);
            model.addAttribute("title", "Corpseed Dashboard | Edit Life-at-corpseed User");
            model.addAttribute("appendClass", "life");
            session.setAttribute("message",new Message("Duplicate Slug Entry !!","alert-danger"));
            List<LifeCategory> lifeCategories =
                    this.lifeUserService.findById(lifeUser.getId()).getLifeCategories();
            model.addAttribute("lifeCategories",lifeCategories);
            List<LifeCategory> lifeCategoryListNotIn = this.lifeCategoryService
                    .findByDisplayStatus("1");
            lifeCategoryListNotIn.removeAll(lifeCategories);
            model.addAttribute("tagsNew",lifeCategoryListNotIn);

            return "admin/life-user-edit";
        }
        lifeUser.setLifeCategories(tags.stream().map(this::fetchLifeTag)
                .filter(c -> c != null)
                .collect(Collectors.toList()));

        if(!file.isEmpty()){
            String filename = this.azureBlobAdapter.upload(file, this.commonServices.getUniqueCode());
            if(lifeUser.getPictureName()!=null)
                this.azureBlobAdapter.deleteFile(lifeUser.getPictureName());

            lifeUser.setPictureName(filename);
        }

        LifeUser updateLifeUser=this.lifeUserService.update(lifeUser);
        if(updateLifeUser==null){
            model.addAttribute("lifeUser",lifeUser);
            model.addAttribute("title", "Corpseed Dashboard | Edit Life-at-corpseed User");
            model.addAttribute("appendClass", "life");
            List<LifeCategory> lifeCategories =
                    this.lifeUserService.findById(lifeUser.getId()).getLifeCategories();
            model.addAttribute("lifeCategories",lifeCategories);
            List<LifeCategory> lifeCategoryListNotIn = this.lifeCategoryService
                    .findByDisplayStatus("1");
            lifeCategoryListNotIn.removeAll(lifeCategories);
            model.addAttribute("tagsNew",lifeCategoryListNotIn);
            session.setAttribute("message",new Message("Something Went-Wrong, Please Try-again later !!","alert-danger"));
            return "admin/life-user-edit";
        }

        return "redirect:/admin/life/user";
    }

    @GetMapping("/delete/{id}")
    public String deleteUser(@PathVariable("id") int id){
        LifeUser lifeUser=this.lifeUserService.findById(id);
        if(lifeUser==null)
            return "redirect:/admin/life/user";

        this.lifeUserService.deleteLifeUser(lifeUser);

        return "redirect:/admin/life/user";
    }
}
