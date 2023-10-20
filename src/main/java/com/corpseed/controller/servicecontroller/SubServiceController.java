package com.corpseed.controller.servicecontroller;

import com.corpseed.entity.History;
import com.corpseed.entity.serviceentity.Services;
import com.corpseed.entity.serviceentity.SubService;
import com.corpseed.entity.User;
import com.corpseed.util.Message;
import com.corpseed.serviceImpl.AzureBlobAdapter;
import com.corpseed.serviceImpl.CommonServices;
import com.corpseed.serviceImpl.HistoryService;
import com.corpseed.serviceImpl.servicesserviceimpl.ServicesService;
import com.corpseed.service.servicesservice.SubServiceService;
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
import java.util.Optional;

@Controller
@RequestMapping("/admin/services/subservice/{uuid}")
public class SubServiceController {

    @Autowired
    private SubServiceService subServiceService;

    @Autowired
    private ServicesService servicesService;

    @Autowired
    private CommonServices commonServices;

    @Autowired
    private HistoryService historyService;

    @Autowired
    private AzureBlobAdapter azureBlobAdapter;

    @GetMapping()
    public String fetchAllSubService(@PathVariable("uuid") String uuid, Model model){
//        System.out.println("all sub service with uuid==="+uuid);
        model.addAttribute("title", "Corpseed Dashboard | All Sub-Services");
        model.addAttribute("appendClass", "services");
        model.addAttribute("services", this.subServiceService.fetchAllSubServices(uuid));
        model.addAttribute("uuid",uuid);
        return "admin/sub-services";
    }

    @GetMapping("/add")
    public String addSubService(@PathVariable("uuid") String uuid, Model model){
        model.addAttribute("title", "Corpseed Dashboard | Add Sub-Service");
        model.addAttribute("appendClass", "services");
        model.addAttribute("subService", new SubService());
        model.addAttribute("uuid",uuid);
        model.addAttribute("serviceList", this.servicesService.getAllServicesByStatus("1"));
        return "admin/sub_service_add";
    }

    @GetMapping("/edit/{id}")
    public String editSubService(@PathVariable("uuid") String uuid,@PathVariable("id") long id, Model model){
        model.addAttribute("title", "Corpseed Dashboard | Edit Sub-Service");
        model.addAttribute("appendClass", "services");
        model.addAttribute("subService", new SubService());
        model.addAttribute("serviceList",null);
        model.addAttribute("uuid",uuid);
        model.addAttribute("serviceList", this.servicesService.getAllServicesByStatus("1"));
        model.addAttribute("subService",this.subServiceService.findSubServiceByUuidAndId(uuid,id));


        return "admin/sub_service_edit";
    }

    @PostMapping("/save")
    public String saveSubServices(@PathVariable("uuid") String uuid,
                                  @RequestParam("services") Optional<Long> serviceId,
                                  @Valid @ModelAttribute("subService") SubService subService,
                                  BindingResult result,@RequestParam("file") MultipartFile file,
                                  Model model, HttpSession session){
        Services service=this.servicesService.findByUUID(uuid);

        if(result.hasErrors()||!serviceId.isPresent()||service==null||file.isEmpty()){
            model.addAttribute("title", "Corpseed Dashboard | Add Sub-Service");
            model.addAttribute("appendClass", "services");
            model.addAttribute("subService", subService);
            model.addAttribute("serviceList",null);
            model.addAttribute("uuid",uuid);
            model.addAttribute("serviceList", this.servicesService.getAllServicesByStatus("1"));
            session.setAttribute("message", new Message("Please fill the form !!", "alert-danger"));
            return "admin/sub_service_add";
        }
//        System.out.println("add service id=="+serviceId.get());
        Services addService = this.servicesService.findByIdAndDisplayStatus(serviceId.get(), "1");
//        System.out.println("add Service=="+addService.getId()+"#"+uuid);
        SubService findSubService=this.subServiceService.findSubServiceByParentServiceUuidAndService(uuid,addService);

        if(findSubService!=null||addService==null){
            model.addAttribute("title", "Corpseed Dashboard | Add Sub-Service");
            model.addAttribute("appendClass", "services");
            model.addAttribute("subService", subService);
            model.addAttribute("serviceList",null);
            model.addAttribute("uuid",uuid);
            model.addAttribute("serviceList", this.servicesService.getAllServicesByStatus("1"));
            session.setAttribute("message", new Message("Service already exist !!", "alert-danger"));
            return "admin/sub_service_add";
        }

        String fileName = this.azureBlobAdapter.upload(file, this.commonServices.getUniqueCode());
        subService.setImage(fileName);
        subService.setDeleteStatus(2);
        subService.setParentServiceSlug(service.getSlug());
        subService.setParentServiceUuid(service.getUuid());
        subService.setServices(addService);

        this.subServiceService.saveSubService(subService);

        return "redirect:/admin/services/subservice/"+uuid;
    }

    @PostMapping("/update")
    public String updateSubServices(@PathVariable("uuid") String uuid,
                                  @RequestParam("services") Optional<Long> serviceId,
                                  @Valid @ModelAttribute("subService") SubService subService,
                                  BindingResult result,@RequestParam("file") MultipartFile file,
                                  Model model, HttpSession session){
        Services service=this.servicesService.findByUUID(uuid);

        if(result.hasErrors()||!serviceId.isPresent()||service==null){
            model.addAttribute("title", "Corpseed Dashboard | Add Sub-Service");
            model.addAttribute("appendClass", "services");
            model.addAttribute("uuid",uuid);
            model.addAttribute("subService",subService);
            model.addAttribute("serviceList", this.servicesService.getAllServicesByStatus("1"));
            session.setAttribute("message", new Message("Please fill the form !!", "alert-danger"));
            return "admin/sub_service_edit";
        }
        Services serviceById = this.servicesService.findById(serviceId.get());

        SubService findSubService=this.subServiceService.findSubServiceByUuidAndServicesAndIdNot(uuid,serviceById,subService.getId());
        if(findSubService!=null){
            model.addAttribute("title", "Corpseed Dashboard | Add Sub-Service");
            model.addAttribute("appendClass", "services");
            model.addAttribute("subService",subService);
            model.addAttribute("uuid",uuid);
            model.addAttribute("serviceList", this.servicesService.getAllServicesByStatus("1"));
            session.setAttribute("message", new Message("Sub-Services already added !!", "alert-danger"));
            return "admin/sub_service_edit";
        }

        if(!file.isEmpty()){
            this.azureBlobAdapter.deleteFile(subService.getImage());
            String fileName=this.azureBlobAdapter.upload(file,this.commonServices.getUniqueCode());
            subService.setImage(fileName);
        }

        subService.setParentServiceSlug(service.getSlug());
        subService.setServices(serviceById);
        this.subServiceService.updateSubService(subService);

        return "redirect:/admin/services/subservice/"+uuid;
    }

    @PostMapping("/delete/{id}")
    @ResponseBody
    public String deleteSubService(@PathVariable("uuid") String uuid, @PathVariable("id") long id,Principal principal, HttpServletRequest req) {
        Services service=this.servicesService.findByUUID(uuid);
        SubService subService=this.subServiceService.findSubServiceByUuidAndId(uuid,id);

        if(service==null||subService==null)
            return "fail";

            subService.setDeleteStatus(1);
            this.subServiceService.updateSubService(subService);
            User user=this.commonServices.getLoginedUser(principal);

            //adding category in history
            this.historyService.savehistory(new History(0, this.commonServices.getUUID(),
                    "SubService", subService.getId(), this.commonServices.getBrowser(req),
                    this.commonServices.getIpAddress(req), user.getId(), user.getFirstName()+" "+user.getLastName()
                    , this.commonServices.getToday(), this.commonServices.getTime(),subService.getServices().getTitle()));

        return "pass";
    }
}
