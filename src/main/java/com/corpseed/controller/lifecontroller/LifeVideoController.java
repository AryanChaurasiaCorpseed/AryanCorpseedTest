package com.corpseed.controller.lifecontroller;

import com.corpseed.entity.lifeentity.LifeVideo;
import com.corpseed.util.Message;
import com.corpseed.service.lifeatcorpseedservice.LifeVideoService;
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

@Controller
@RequestMapping("/admin/life/video")
public class LifeVideoController {

    @Autowired
    private LifeVideoService lifeVideoService;

    @Autowired
    private AzureBlobAdapter azureBlobAdapter;

    @Autowired
    private CommonServices commonServices;

    @GetMapping()
    public String lifeVideo(Model model){
        List<LifeVideo> lifeVideos=this.lifeVideoService.findAll();
        model.addAttribute("lifeVideos",lifeVideos);
        model.addAttribute("title", "Corpseed Dashboard | Manage Life-at-corpseed Video");
        model.addAttribute("appendClass", "life");
        return "admin/life-video";
    }

    @GetMapping("/add")
    public String addLifeTag(Model model){
        model.addAttribute("lifeVideo",new LifeVideo());
        model.addAttribute("title", "Corpseed Dashboard | Add Life-at-corpseed Video");
        model.addAttribute("appendClass", "life");
        return "admin/life-video-add";
    }

    @GetMapping("/edit/{id}")
    public String editLifeTag(@PathVariable("id") int id, Model model){
        model.addAttribute("lifeVideo",this.lifeVideoService.findById(id));
        model.addAttribute("title", "Corpseed Dashboard | Edit Life-at-corpseed Video");
        model.addAttribute("appendClass", "life");
        return "admin/life-video-edit";
    }

    @PostMapping("/save")
    public String saveCategory(@Valid @ModelAttribute("lifeCategory") LifeVideo lifeVideo,
                               BindingResult result,
                               @RequestParam("file") MultipartFile file,
                               Model model, HttpSession session){
        if(result.hasErrors()){
            model.addAttribute("lifeVideo",lifeVideo);
            model.addAttribute("title", "Corpseed Dashboard | Add Life-at-corpseed Video");
            model.addAttribute("appendClass", "life");
            session.setAttribute("message",new Message("Fill all the blank fields !!","alert-danger"));
            return "admin/life-tag-add";
        }
        if(!file.isEmpty()){
            String fileName = this.azureBlobAdapter.upload(file, this.commonServices.getUniqueCode());
            lifeVideo.setCoverImage(fileName);
        }
        LifeVideo saveLifeVideo=this.lifeVideoService.save(lifeVideo);
        if(saveLifeVideo==null){
            model.addAttribute("lifeVideo",lifeVideo);
            model.addAttribute("title", "Corpseed Dashboard | Add Life-at-corpseed Video");
            model.addAttribute("appendClass", "life");
            session.setAttribute("message",new Message("Something Went-Wrong, Please Try-again later !!","alert-danger"));
            return "admin/life-video-add";
        }

        return "redirect:/admin/life/video";
    }

    @PostMapping("/update")
    public String updateCategory(@Valid @ModelAttribute("lifeVideo") LifeVideo lifeVideo,
                                 BindingResult result,
                                 @RequestParam("file") MultipartFile file,
                                 Model model, HttpSession session){
        if(result.hasErrors()){
            model.addAttribute("lifeVideo",lifeVideo);
            model.addAttribute("title", "Corpseed Dashboard | Edit Life-at-corpseed Video");
            model.addAttribute("appendClass", "life");
            session.setAttribute("message",new Message("Fill all the blank fields !!","alert-danger"));
            return "admin/life-video-edit";
        }

        if(!file.isEmpty()){
            String filename = this.azureBlobAdapter.upload(file, this.commonServices.getUniqueCode());
            if(lifeVideo.getCoverImage()!=null)
                this.azureBlobAdapter.deleteFile(lifeVideo.getCoverImage());

            lifeVideo.setCoverImage(filename);
        }

        LifeVideo updateLifeVideo=this.lifeVideoService.update(lifeVideo);
        if(updateLifeVideo==null){
            model.addAttribute("lifeVideo",lifeVideo);
            model.addAttribute("title", "Corpseed Dashboard | Edit Life-at-corpseed Video");
            model.addAttribute("appendClass", "life");
            session.setAttribute("message",new Message("Something Went-Wrong, Please Try-again later !!","alert-danger"));
            return "admin/life-video-edit";
        }

        return "redirect:/admin/life/video";
    }

    @GetMapping("/delete/{id}")
    public String deleteTag(@PathVariable("id") int id){
        LifeVideo lifeVideo=this.lifeVideoService.findById(id);
        if(lifeVideo==null)
            return "redirect:/admin/life/video";

        this.lifeVideoService.deleteLifeVideo(lifeVideo);

        return "redirect:/admin/life/video";
    }

}
