package com.corpseed.controller.presscontroller;

import com.corpseed.entity.History;
import com.corpseed.entity.User;
import com.corpseed.entity.pressentity.Press;
import com.corpseed.entity.pressentity.PressCategory;
import com.corpseed.service.pressservice.PressCategoryService;
import com.corpseed.service.pressservice.PressService;
import com.corpseed.serviceImpl.CommonServices;
import com.corpseed.serviceImpl.HistoryService;
import com.corpseed.util.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.io.PrintWriter;
import java.security.Principal;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/press/category")
public class PressCategoryController {

    @Autowired
    private PressCategoryService pressCategoryService;

    @Autowired
    private CommonServices commonServices;

    @Autowired
    private HistoryService historyService;

    @Autowired
    private PressService pressService;

    @GetMapping()
    public String pressCategory(Model model) {
        model.addAttribute("title", "Corpseed Dashboard | Manage Press Category");
        model.addAttribute("appendClass", "press");
        model.addAttribute("pressCategory", this.pressCategoryService.findAllByDeleteStatus(2));
        return "admin/press/press-category";
    }

    @GetMapping("/add")
    public String addPressCategory(Model model) {
        model.addAttribute("title", "Corpseed Dashboard | Add Press Category");
        model.addAttribute("appendClass", "press");
        model.addAttribute("pressCategory", new PressCategory());
        return "admin/press/press-category-add";
    }

    @PostMapping("/save")
    public String savePressCategory(@Valid @ModelAttribute("pressCategory") PressCategory pressCategory,
                                    BindingResult result, Model model, HttpSession session) {


        try {
            if(result.hasErrors()) {
                model.addAttribute("title", "Corpseed Dashboard | Add Press Category");
                model.addAttribute("appendClass", "press");
                model.addAttribute("pressCategory", pressCategory);
                session.setAttribute("message", new Message("Please fill out the blank fields !!", "alert-danger"));
                return "admin/press/press-category-add";
            }

            PressCategory findPressCategory=this.pressCategoryService.findBySlugOrTitle(pressCategory.getSlug(),pressCategory.getTitle());

            if(findPressCategory!=null)
            {
                model.addAttribute("title", "Corpseed Dashboard | Add Press Category");
                model.addAttribute("appendClass", "press");
                model.addAttribute("pressCategory", pressCategory);
                session.setAttribute("message", new Message("Press Category already existed !!", "alert-danger"));
                return "admin/press/press-category-add";
            }

            PressCategory savePressCategory=this.pressCategoryService.savePressCategory(pressCategory);

            if(savePressCategory==null) {
                model.addAttribute("title", "Corpseed Dashboard | Add Press Category");
                model.addAttribute("appendClass", "press");
                model.addAttribute("pressCategory", pressCategory);
                session.setAttribute("message", new Message("Press Category not saved, Please try-again later !!", "alert-danger"));
                return "admin/press/press-category-add";
            }
            return "redirect:/press/category/";

        } catch (Exception e) {
            model.addAttribute("title", "Corpseed Dashboard | Add Press Category");
            model.addAttribute("appendClass", "press");
            model.addAttribute("pressCategory", pressCategory);
            session.setAttribute("message", new Message("Error : "+e.getMessage(), "alert-danger"));
            return "admin/press/press-category-add";
        }
    }

    @GetMapping("/edit/{id}")
    public String editPressCategory(Model model,@PathVariable("id") long id) {
        model.addAttribute("title", "Corpseed Dashboard | Edit Press Category");
        model.addAttribute("appendClass", "press");
        PressCategory pressCategory=this.pressCategoryService.findById(id);
        model.addAttribute("pressCategory", pressCategory);
        return "admin/press/press-category-edit";
    }

    @PostMapping("/update")
    public String updatePress(@Valid @ModelAttribute("pressCategory") PressCategory pressCategory,
                              BindingResult result,Model model,HttpSession session) {
        try {
            if(result.hasErrors()) {
                model.addAttribute("title", "Corpseed Dashboard | Edit Press Category");
                model.addAttribute("appendClass", "press");
                model.addAttribute("pressCategory", pressCategory);
                session.setAttribute("message", new Message("Please fill out the blank fields !!", "alert-danger"));
                return "admin/press/press-category-edit";
            }

            PressCategory findPressCategory=this.pressCategoryService.findPressCategoryBySlugOrTitleAndIdNot(pressCategory.getSlug(),pressCategory.getTitle(),pressCategory.getId());
            if(findPressCategory!=null) {
                model.addAttribute("title", "Corpseed Dashboard | Edit Press Category");
                model.addAttribute("appendClass", "press");
                model.addAttribute("pressCategory", pressCategory);
                session.setAttribute("message", new Message("Press Category already existed !!", "alert-danger"));
                return "admin/press/press-category-edit";
            }
            PressCategory updatePressCategory=this.pressCategoryService.updatePressCategory(pressCategory);
            if(updatePressCategory==null) {
                model.addAttribute("title", "Corpseed Dashboard | Edit Press Category");
                model.addAttribute("appendClass", "press");
                model.addAttribute("pressCategory", pressCategory);
                session.setAttribute("message", new Message("Press Category not updated..!!, Please try-again later !!", "alert-danger"));
                return "admin/press/press-category-edit";
            }

            return "redirect:/press/category/";

        } catch (Exception e) {
            model.addAttribute("title", "Corpseed Dashboard | Edit Press Category");
            model.addAttribute("appendClass", "press");
            model.addAttribute("pressCategory", pressCategory);
            session.setAttribute("message", new Message("Error : "+e.getMessage(), "alert-danger"));
            return "admin/press/press-category-edit";
        }
    }

    @GetMapping("/delete/{id}")
    @ResponseBody
    public void deletePressCategory(@PathVariable("id") long id, @RequestParam("password") String password, Principal principal, PrintWriter pw
            , HttpServletRequest req) {
        User user=this.commonServices.getLoginedUser(principal);
        if(user.getRole().equals("ROLE_ADMIN")) {
            if(new BCryptPasswordEncoder().matches(password, user.getPassword())) {
                PressCategory pressCategory=this.pressCategoryService.findById(id);
                if(pressCategory!=null) {
                    pressCategory.setDeleteStatus(1);
                    //updating category
                    this.pressCategoryService.savePressCategory(pressCategory);

                    //adding category in history
                    this.historyService.savehistory(new History(0, this.commonServices.getUUID(),
                            "PressCategory", pressCategory.getId(), this.commonServices.getBrowser(req),
                            this.commonServices.getIpAddress(req), user.getId(), user.getFirstName()+" "+user.getLastName()
                            , this.commonServices.getToday(), this.commonServices.getTime(),pressCategory.getTitle()));

                    //updating services
                    List<Press> press=pressCategory.getPress();
                    if(!press.isEmpty()) {
                        press = press.stream().map(p -> {
                            p.setDeleteStatus(1);
                            return p;
                        }).collect(Collectors.toList());
                        this.pressService.saveAllPress(press);
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

}
