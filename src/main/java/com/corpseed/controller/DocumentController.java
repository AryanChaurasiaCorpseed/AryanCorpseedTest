package com.corpseed.controller;

import java.security.Principal;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.corpseed.entity.documententity.Documents;
import com.corpseed.entity.History;
import com.corpseed.entity.User;
import com.corpseed.util.Message;
import com.corpseed.serviceImpl.AzureBlobAdapter;
import com.corpseed.serviceImpl.CommonServices;
import com.corpseed.serviceImpl.DocumentService;
import com.corpseed.serviceImpl.HistoryService;

@Controller
@RequestMapping("/admin/document")
public class DocumentController {

	@Autowired
	private CommonServices commonService;
	
	@Autowired
	private DocumentService documentService;
	
	@Autowired
    AzureBlobAdapter azureAdapter;
	
	@Autowired
	private HistoryService historyService;
	
	@GetMapping("/")
	public String documents(Model model) {
		model.addAttribute("title", "Corpseed Dashboard | Manage Document Centre");
		model.addAttribute("appendClass", "document");
		model.addAttribute("docs", this.documentService.getAllDocuments());
		return "admin/document";
	}
	@GetMapping("/add")
	public String addDocument(Model model) {
		model.addAttribute("title", "Corpseed Dashboard | Add Document");
		model.addAttribute("appendClass", "document");
		model.addAttribute("documents", new Documents());
		return "admin/document-add";
	}
	@GetMapping("/delete/{uuid}")
	public String deleteDocument(@PathVariable("uuid") String uuid,Principal principal,HttpServletRequest req) {
		if(this.commonService.getLoginedUser(principal).getRole().equals("ROLE_ADMIN")) {
			Documents document=this.documentService.getDocumentByUuid(uuid);
			if(document!=null) {
				document.setDeleteStatus(1);
				this.documentService.saveDocument(document);
				
				User user=this.commonService.getLoginedUser(principal);
				
				//adding category in history
				this.historyService.savehistory(new History(0, this.commonService.getUUID(),
						"Document", document.getId(), this.commonService.getBrowser(req), 
						this.commonService.getIpAddress(req), user.getId(), user.getFirstName()+" "+user.getLastName()
						, this.commonService.getToday(), this.commonService.getTime(),document.getFileName()));		
								
				
			}
			return "redirect:/admin/document/";
		}else {
			return "error/403";
		}
	}
	@PostMapping("/add")
	public String saveDocuments(@Valid @ModelAttribute("documents") Documents documents,BindingResult result,@RequestParam("fileImage") MultipartFile file
			,Model model,HttpSession session,Principal principal) {
		try {
			model.addAttribute("title", "Corpseed Dashboard | Manage Document Centre");
			model.addAttribute("appendClass", "document");
			if(result.hasErrors()) {
				model.addAttribute("documents", documents);
				model.addAttribute("message", new Message("Please fill out the blank fields !!", "alert-danger"));
				return "admin/document-add";
			}
			if(file.isEmpty()) {
				model.addAttribute("documents", documents);
				session.setAttribute("message", new Message("Please upload file !!", "alert-danger"));
				return "admin/document-add";
			}else {
				String name = azureAdapter.upload(file,this.commonService.getUniqueCode());
				documents.setFileName(name);
				documents.setUuid(this.commonService.getUUID());
				documents.setAddedByUUID(this.commonService.getLoginedUser(principal).getUuid());
				Documents saveDoc=this.documentService.saveDocument(documents);
				if(saveDoc==null) {	
					model.addAttribute("documents", documents);
					session.setAttribute("message", new Message("Please upload file !!", "alert-danger"));
					return "admin/document-add";
				}
				
				return "redirect:/admin/document/";	
			}		
			
		} catch (Exception e) {
			e.printStackTrace();
			model.addAttribute("documents", documents);
			session.setAttribute("message", new Message("Error : "+e.getMessage(), "alert-danger"));
			return "admin/document-add";
		}
	}
}
