package com.corpseed.controller.lifecontroller;

import com.corpseed.entity.lifeentity.LifeCategory;
import com.corpseed.util.Message;
import com.corpseed.service.lifeatcorpseedservice.LifeCategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.util.List;

@Controller
@RequestMapping("/admin/life/tag")
public class LifeCategoryController {

    @Autowired
    private LifeCategoryService lifeCategoryService;

    @GetMapping()
    public String lifeCategory(Model model){
        List<LifeCategory> lifeCatList=this.lifeCategoryService.findAll();
        model.addAttribute("tags",lifeCatList);
        model.addAttribute("title", "Corpseed Dashboard | Manage Life-at-corpseed Tags");
        model.addAttribute("appendClass", "life");
        return "admin/life-tag";
    }

    @GetMapping("/add")
    public String addLifeTag(Model model){
        model.addAttribute("lifeCategory",new LifeCategory());
        model.addAttribute("title", "Corpseed Dashboard | Add Life-at-corpseed Tag");
        model.addAttribute("appendClass", "life");
        return "admin/life-tag-add";
    }

    @GetMapping("/edit/{id}")
    public String editLifeTag(@PathVariable("id") int id,Model model){
        model.addAttribute("lifeCategory",this.lifeCategoryService.findById(id));
        model.addAttribute("title", "Corpseed Dashboard | Edit Life-at-corpseed Tag");
        model.addAttribute("appendClass", "life");
        return "admin/life-tag-edit";
    }

    @PostMapping("/save")
    public String saveCategory(@Valid @ModelAttribute("lifeCategory") LifeCategory lifeCategory,
                               BindingResult result, Model model, HttpSession session){
        if(result.hasErrors()){
            model.addAttribute("lifeCategory",lifeCategory);
            model.addAttribute("title", "Corpseed Dashboard | Add Life-at-corpseed Tag");
            model.addAttribute("appendClass", "life");
            session.setAttribute("message",new Message("Fill all the blank fields !!","alert-danger"));
            return "admin/life-tag-add";
        }
        LifeCategory findLifeCategory=this.lifeCategoryService.findLifeCategoryBySlug(lifeCategory.getSlug());

        if(findLifeCategory!=null){
            model.addAttribute("lifeCategory",lifeCategory);
            model.addAttribute("title", "Corpseed Dashboard | Add Life-at-corpseed Tag");
            model.addAttribute("appendClass", "life");
            session.setAttribute("message",new Message("Tag already exist !!","alert-danger"));
            return "admin/life-tag-add";
        }

        LifeCategory saveLifeCategory=this.lifeCategoryService.save(lifeCategory);
        if(saveLifeCategory==null){
            model.addAttribute("lifeCategory",lifeCategory);
            model.addAttribute("title", "Corpseed Dashboard | Add Life-at-corpseed Tag");
            model.addAttribute("appendClass", "life");
            session.setAttribute("message",new Message("Something Went-Wrong, Please Try-again later !!","alert-danger"));
            return "admin/life-tag-add";
        }

        return "redirect:/admin/life/tag";
    }

    @PostMapping("/update")
    public String updateCategory(@Valid @ModelAttribute("lifeCategory") LifeCategory lifeCategory,
                               BindingResult result, Model model, HttpSession session){
        if(result.hasErrors()){
            model.addAttribute("lifeCategory",lifeCategory);
            model.addAttribute("title", "Corpseed Dashboard | Edit Life-at-corpseed Tag");
            model.addAttribute("appendClass", "life");
            session.setAttribute("message",new Message("Fill all the blank fields !!","alert-danger"));
            return "admin/life-tag-edit";
        }
        LifeCategory findLifeCategory=this.lifeCategoryService.findLifeCategoryBySlugAndIdNot(lifeCategory.getSlug(),lifeCategory.getId());

        if(findLifeCategory!=null){
            model.addAttribute("lifeCategory",lifeCategory);
            model.addAttribute("title", "Corpseed Dashboard | Edit Life-at-corpseed Tag");
            model.addAttribute("appendClass", "life");
            session.setAttribute("message",new Message("Tag already exist !!","alert-danger"));
            return "admin/life-tag-edit";
        }

        LifeCategory updateLifeCategory=this.lifeCategoryService.update(lifeCategory);
        if(updateLifeCategory==null){
            model.addAttribute("lifeCategory",lifeCategory);
            model.addAttribute("title", "Corpseed Dashboard | Edit Life-at-corpseed Tag");
            model.addAttribute("appendClass", "life");
            session.setAttribute("message",new Message("Something Went-Wrong, Please Try-again later !!","alert-danger"));
            return "admin/life-tag-edit";
        }

        return "redirect:/admin/life/tag";
    }

    @GetMapping("/delete/{id}")
    public String deleteTag(@PathVariable("id") int id){
        LifeCategory lifeCategory=this.lifeCategoryService.findById(id);
        if(lifeCategory==null)
            return "redirect:/admin/life/tag";

        this.lifeCategoryService.deleteLifeCategory(lifeCategory);

        return "redirect:/admin/life/tag";
    }


}
