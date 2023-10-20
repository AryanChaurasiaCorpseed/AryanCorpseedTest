package com.corpseed.controller.presscontroller;

import com.corpseed.entity.History;
import com.corpseed.entity.SiteMapUrl;
import com.corpseed.entity.User;
import com.corpseed.entity.pressentity.Press;
import com.corpseed.entity.pressentity.PressCategory;
import com.corpseed.service.pressservice.PressCategoryService;
import com.corpseed.service.pressservice.PressService;
import com.corpseed.serviceImpl.*;
import com.corpseed.util.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.io.PrintWriter;
import java.security.Principal;
import java.util.List;


@Controller
@RequestMapping("/press")
public class PressReleaseController {

    @Autowired
    private PressService pressService;

    @Autowired
    private PressCategoryService pressCategoryService;

    @Autowired
    private CommonServices commonServices;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Autowired
    private UserServices userServices;

    @Autowired
    private HistoryService historyService;

    @Autowired
    private AzureBlobAdapter azureBlobAdapter;

    @Autowired
    private SiteMapService siteMapService;


    @Value("${azure_path}")
    private String azure_path;

    @Value("${domain.name}")
    private String domain;

    @GetMapping()
    public String pressHome(Model model) {
        model.addAttribute("title", "Corpseed Dashboard | Manage Press Release");
        model.addAttribute("appendClass", "press");
        model.addAttribute("press", this.pressService.findAll());
        return "admin/press/press";
    }




    @GetMapping("/add")
    public String addNews(Model model) {
        model.addAttribute("title", "Corpseed Dashboard | Add News");
        model.addAttribute("appendClass", "news");
        model.addAttribute("users", this.userServices.findAllNotUserAndAccountStatus());
        model.addAttribute("pressCategory", this.pressCategoryService.findByDisplayStatusAndDeleteStatus("1",2));
        model.addAttribute("press", new Press());
        return "admin/press/press-add";
    }

    @PostMapping("/save")
    public String saveNews(@Valid @ModelAttribute("press") Press press,
                           @RequestParam("categoryId") long categoryId,@RequestParam("file") MultipartFile file,
                           BindingResult result,Model model,Principal principal,HttpSession session) {
        try {
            if(result.hasErrors()) {
                model.addAttribute("title", "Corpseed Dashboard | Add Press");
                model.addAttribute("appendClass", "press");
                model.addAttribute("users", this.userServices.findAllNotUserAndAccountStatus());
                model.addAttribute("pressCategory", this.pressCategoryService.findByDisplayStatusAndDeleteStatus("1",2));
                model.addAttribute("press", press);
                session.setAttribute("message", new Message("Please field the form !!", "alert-danger"));
                return "admin/press/press-add";
            }
            Press findPress=this.pressService.findBySlug(press.getSlug());
            if(findPress!=null) {
                model.addAttribute("title", "Corpseed Dashboard | Add Press");
                model.addAttribute("appendClass", "press");
                model.addAttribute("users", this.userServices.findAllNotUserAndAccountStatus());
                model.addAttribute("pressCategory", this.pressCategoryService.findByDisplayStatusAndDeleteStatus("1",2));
                model.addAttribute("press", press);
                session.setAttribute("message", new Message("Press already existed !!", "alert-danger"));
                return "admin/press/press-add";
            }
            PressCategory pressCategory=this.pressCategoryService.findById(categoryId);
            press.setPressCategory(pressCategory);

            if(!file.isEmpty()) {
                String fileName = this.azureBlobAdapter.upload(file, 0);
                press.setImage(fileName);
            }

//            User author=this.userServices.findByIdAndDeleteStatus(press.getAuthorId(), 2);
//            if(author!=null)press.setAuthorName(author.getFirstName()+' '+author.getLastName());
//            press.setUser(author);

            String description=press.getDescription();
            description=description.replace("<table","<div class='table-width'><table");
            description=description.replace("</table>","</table></div>");

            while(description.contains("pdfview=")) {
                description=description.replace("pdfview=", "<iframe src=\"https://docs.google.com/gview?url=");
                description=description.replace("=pdfview", "&embedded=true\" frameborder=\"0\" webkitallowfullscreen mozallowfullscreen allowfullscreen style=\"width: 100%; height: 700px;\"></iframe>");
            }

            press.setDescription(description);

            Press savePress=this.pressService.savePress(press);
            if(savePress==null) {
                model.addAttribute("title", "Corpseed Dashboard | Add Press");
                model.addAttribute("appendClass", "press");
                model.addAttribute("users", this.userServices.findAllNotUserAndAccountStatus());
                model.addAttribute("pressCategory", this.pressCategoryService.findByDisplayStatusAndDeleteStatus("1",2));
                model.addAttribute("press", press);
                session.setAttribute("message", new Message("Press not saved, Please try-again later !!", "alert-danger"));
                return "admin/press/press-add";
            }

            //adding this url into sitemap
            this.siteMapService.saveUrl(new SiteMapUrl(0, this.commonServices.getUUID(), savePress.getId(), "Press", "/press/"+savePress.getSlug(), 1, this.commonServices.getToday()));

            return "redirect:/press";

        } catch (Exception e) {
            model.addAttribute("title", "Corpseed Dashboard | Add Press");
            model.addAttribute("appendClass", "press");
            model.addAttribute("users", this.userServices.findAllNotUserAndAccountStatus());
            model.addAttribute("pressCategory", this.pressCategoryService.findByDisplayStatusAndDeleteStatus("1",2));
            model.addAttribute("press", press);
            session.setAttribute("message", new Message("Error : "+e.getMessage(), "alert-danger"));
            return "admin/press/press-add";
        }

    }



    @GetMapping("/edit/{id}")
    public String editNews(@PathVariable("id") long id,Model model) {
        model.addAttribute("title", "Corpseed Dashboard | Edit Press");
        model.addAttribute("appendClass", "press");
        model.addAttribute("users", this.userServices.findAllNotUserAndAccountStatus());
        model.addAttribute("pressCategory", this.pressCategoryService.findByDisplayStatusAndDeleteStatus("1",2));
        Press press=this.pressService.findById(id);
        model.addAttribute("press", press);
        if(press!=null)model.addAttribute("activeCategory", press.getPressCategory().getId());
        return "admin/press/press-edit";
    }

    @PostMapping("/update")
    public String updatePress(@Valid @ModelAttribute("press") Press press,
                              @RequestParam("categoryId") long categoryId,@RequestParam("file") MultipartFile file,
                              BindingResult result,Model model,Principal principal,HttpSession session) {
        try {
            if(result.hasErrors()) {
                model.addAttribute("title", "Corpseed Dashboard | Edit Press");
                model.addAttribute("appendClass", "press");
                model.addAttribute("users", this.userServices.findAllNotUserAndAccountStatus());
                model.addAttribute("pressCategory", this.pressCategoryService.findByDisplayStatusAndDeleteStatus("1",2));
                model.addAttribute("press", press);
                session.setAttribute("message", new Message("Please field the form !!", "alert-danger"));
                return "admin/press/press-edit";
            }
            Press findPress=this.pressService.findBySlugAndIdNot(press.getSlug(),press.getId());
            if(findPress!=null) {
                model.addAttribute("title", "Corpseed Dashboard | Edit Press");
                model.addAttribute("appendClass", "press");
                model.addAttribute("users", this.userServices.findAllNotUserAndAccountStatus());
                model.addAttribute("pressCategory", this.pressCategoryService.findByDisplayStatusAndDeleteStatus("1",2));
                model.addAttribute("press", press);
                session.setAttribute("message", new Message("Press already existed !!", "alert-danger"));
                return "admin/press/press-edit";
            }
            PressCategory pressCategory=this.pressCategoryService.findById(categoryId);
            press.setPressCategory(pressCategory);

            if(!file.isEmpty()) {
                if(press.getImage()!=null&&press.getImage().length()>0) {
                    this.azureBlobAdapter.deleteFile(press.getImage());
                }
                String fileName = this.azureBlobAdapter.upload(file, 0);
                press.setImage(fileName);
            }

//            User author=this.userServices.findByIdAndDeleteStatus(press.getAuthorId(), 2);
//            if(author!=null)press.setAuthorName(author.getFirstName()+' '+author.getLastName());
//            press.setUser(author);

            String description=press.getDescription();

            description=description.replace("<div class=\"table-width\">","");
            description=description.replace("</table>\r\n"
                    + "</div>","</table>");

            description=description.replace("<table","<div class='table-width'><table");
            description=description.replace("</table>","</table></div>");

            while(description.contains("pdfview=")) {
                description=description.replace("pdfview=", "<iframe src=\"https://docs.google.com/gview?url=");
                description=description.replace("=pdfview", "&embedded=true\" frameborder=\"0\" webkitallowfullscreen mozallowfullscreen allowfullscreen style=\"width: 100%; height: 700px;\"></iframe>");
            }

            press.setDescription(description);


            Press savePress=this.pressService.savePress(press);
            if(savePress==null) {
                model.addAttribute("title", "Corpseed Dashboard | Edit Press");
                model.addAttribute("appendClass", "press");
                model.addAttribute("users", this.userServices.findAllNotUserAndAccountStatus());
                model.addAttribute("pressCategory", this.pressCategoryService.findByDisplayStatusAndDeleteStatus("1",2));
                model.addAttribute("press", press);
                session.setAttribute("message", new Message("Press not saved, Please try-again later !!", "alert-danger"));
                return "admin/press/press-edit";
            }

            //checking sitemap url existed or not if not then insert or update
            SiteMapUrl siteMap=this.siteMapService.findByTypeAndUid("Press", savePress.getId());
            if(siteMap==null) {
                this.siteMapService.saveUrl(new SiteMapUrl(0, this.commonServices.getUUID(), savePress.getId(), "Press", "/press/"+savePress.getSlug(), 1, this.commonServices.getToday()));
            }else {
                siteMap.setUrl("/press/"+savePress.getSlug());
                this.siteMapService.saveUrl(siteMap);
            }
            return "redirect:/press/";

        } catch (Exception e) {
            model.addAttribute("title", "Corpseed Dashboard | Edit Press");
            model.addAttribute("appendClass", "press");
            model.addAttribute("users", this.userServices.findAllNotUserAndAccountStatus());
            model.addAttribute("pressCategory", this.pressCategoryService.findByDisplayStatusAndDeleteStatus("1",2));
            model.addAttribute("press", press);
            session.setAttribute("message", new Message("Error : "+e.getMessage(), "alert-danger"));
            return "admin/press/press-edit";
        }

    }



    @GetMapping("/delete/{id}")
    public String deletePress(@PathVariable("id") long id,Principal principal,HttpServletRequest req) {
        if(this.commonServices.getLoginedUser(principal).getRole().equals("ROLE_ADMIN")) {
            Press press=this.pressService.findById(id);

            if(press!=null) {
                press.setDeleteStatus(1);
                this.pressService.savePress(press);
                User user=this.commonServices.getLoginedUser(principal);
                //adding category in history
                this.historyService.savehistory(new History(0, this.commonServices.getUUID(),
                        "Press", press.getId(), this.commonServices.getBrowser(req),
                        this.commonServices.getIpAddress(req), user.getId(), user.getFirstName()+" "+user.getLastName()
                        , this.commonServices.getToday(), this.commonServices.getTime(),press.getTitle()));

            }
            return "redirect:/press";
        }else {
            return "error/403";
        }
    }
}
