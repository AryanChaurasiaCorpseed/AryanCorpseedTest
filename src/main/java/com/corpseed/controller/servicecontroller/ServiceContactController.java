package com.corpseed.controller.servicecontroller;

import com.corpseed.entity.*;
import com.corpseed.entity.serviceentity.ServiceContact;
import com.corpseed.entity.serviceentity.Services;
import com.corpseed.util.Message;
import com.corpseed.serviceImpl.CommonServices;
import com.corpseed.serviceImpl.HistoryService;
import com.corpseed.serviceImpl.servicesserviceimpl.ServicesService;
import com.corpseed.serviceImpl.UserServices;
import com.corpseed.service.servicesservice.ServiceContactService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.security.Principal;

@Controller
@RequestMapping("/admin/services/contact/{uuid}")
public class ServiceContactController {

    @Autowired
    private ServiceContactService serviceContactService;

    @Autowired
    private UserServices userServices;

    @Autowired
    private ServicesService servicesService;

    @Autowired
    private CommonServices commonServices;

    @Autowired
    private HistoryService historyService;

    @GetMapping()
    public String fetchAllContacts(@PathVariable("uuid") String uuid, Model model){
        model.addAttribute("title", "Corpseed Dashboard | All Contacts");
        model.addAttribute("appendClass", "services");
        model.addAttribute("contacts", this.serviceContactService.fetchAllServiceContacts(uuid));
        model.addAttribute("uuid",uuid);
        return "admin/service-contacts";
    }

    @GetMapping("/add")
    public String addServiceContact(@PathVariable("uuid") String uuid, Model model){
        model.addAttribute("title", "Corpseed Dashboard | Add Service Contact");
        model.addAttribute("appendClass", "services");
        model.addAttribute("contact", new ServiceContact());
        model.addAttribute("uuid",uuid);
        model.addAttribute("userList",this.userServices.findUserByAccountStatusAndDeleteStatusAndDisplayStatusAndRoleNot(1,2,"1","ROLE_USER"));
        return "admin/service_contact_add";
    }

    @GetMapping("/edit/{id}")
    public String editServiceContact(@PathVariable("uuid") String uuid,@PathVariable("id") long id, Model model){
        model.addAttribute("title", "Corpseed Dashboard | Edit Service Contact");
        model.addAttribute("appendClass", "services");
        model.addAttribute("contact", this.serviceContactService.findServiceContactByServiceAndId(uuid,id));
        model.addAttribute("uuid",uuid);
        model.addAttribute("userList",this.userServices.findUserByAccountStatusAndDeleteStatusAndDisplayStatusAndRoleNot(1,2,"1","ROLE_USER"));
        return "admin/service_contact_edit";
    }

    @PostMapping("/save")
    public String saveServiceContact(@PathVariable("uuid") String uuid,
                                  @Valid @ModelAttribute("contact") ServiceContact serviceContact,
                                  BindingResult result,Model model, HttpSession session){

        Services service=this.servicesService.findByUUID(uuid);

        if(result.hasErrors()){
            setError(model,serviceContact,uuid,session,new Message("Please fill the form !!", "alert-danger"),"Corpseed Dashboard | Add Service Contact");
            return "admin/service_contact_add";
        }
        ServiceContact findServiceContact=this.serviceContactService.findServiceContactByServiceAndUser(service,serviceContact.getUser());

        if(findServiceContact!=null){
            setError(model,serviceContact,uuid,session,new Message("Service Contact already exist !!", "alert-danger"),"Corpseed Dashboard | Add Service Contact");
            return "admin/service_contact_add";
        }
        serviceContact.setServices(service);
        serviceContact.setDeleteStatus(2);
        this.serviceContactService.saveServiceContact(serviceContact);

        return "redirect:/admin/services/contact/"+uuid;
    }

    @PostMapping("/update")
    public String updateServiceContact(@PathVariable("uuid") String uuid,
                                     @Valid @ModelAttribute("contact") ServiceContact serviceContact,
                                     BindingResult result,Model model, HttpSession session){

        Services service=this.servicesService.findByUUID(uuid);

        if(result.hasErrors()){
            setError(model,serviceContact,uuid,session,new Message("Please fill the form !!", "alert-danger"),"Corpseed Dashboard | Add Service Contact");
            return "admin/service_contact_edit";
        }
        ServiceContact findServiceContact=this.serviceContactService.findServiceContactByServiceAndUserAndIdNot(service,serviceContact.getUser(),serviceContact.getId());

        if(findServiceContact!=null){
            setError(model,serviceContact,uuid,session,new Message("Service Contact already exist !!", "alert-danger"),"Corpseed Dashboard | Add Service Contact");
            return "admin/service_contact_edit";
        }
        serviceContact.setServices(service);
        serviceContact.setDeleteStatus(2);
        this.serviceContactService.saveServiceContact(serviceContact);

        return "redirect:/admin/services/contact/"+uuid;
    }

    @PostMapping("/delete/{id}")
    @ResponseBody
    public String deleteServiceContact(@PathVariable("uuid") String uuid, @PathVariable("id") long id, Principal principal, HttpServletRequest req) {
        ServiceContact serviceContact=this.serviceContactService.findServiceContactByServiceAndId(uuid,id);

        if(serviceContact==null)
            return "fail";

        serviceContact.setDeleteStatus(1);
        this.serviceContactService.saveServiceContact(serviceContact);

        User user=this.commonServices.getLoginedUser(principal);

        //adding category in history
        this.historyService.savehistory(new History(0, this.commonServices.getUUID(),
                "ServiceContact", serviceContact.getId(), this.commonServices.getBrowser(req),
                this.commonServices.getIpAddress(req), user.getId(), user.getFirstName()+" "+user.getLastName()
                , this.commonServices.getToday(), this.commonServices.getTime(),serviceContact.getServices().getTitle()));


        return "pass";
    }

    public void setError(Model model,ServiceContact serviceContact,String uuid,HttpSession session,Message message,String title){
        model.addAttribute("title", title);
        model.addAttribute("appendClass", "services");
        model.addAttribute("contact", serviceContact);
        model.addAttribute("uuid",uuid);
        model.addAttribute("userList",this.userServices.findUserByAccountStatusAndDeleteStatusAndDisplayStatusAndRoleNot(1,2,"1","ROLE_USER"));
        session.setAttribute("message", message);
    }

}
