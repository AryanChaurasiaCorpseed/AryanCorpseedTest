package com.corpseed.controller.userController;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.corpseed.entity.Api;
import com.corpseed.entity.serviceentity.Services;
import com.corpseed.util.EmailSender;
import com.corpseed.serviceImpl.servicesserviceimpl.ServicesService;

@RestController
public class ApiController{
	
	@Autowired
	private ServicesService servicesService;
	
	@Autowired
	private EmailSender emailSender;
	
	@Value("${crm.token}")
	private String crm_token;
	
	
	@PostMapping("/api/email/send")
	public ResponseEntity<Integer> sendEmail(@RequestParam("to") String to,@RequestParam("cc") String cc,
			@RequestParam("subject") String subject,@RequestParam("message") String message,
			@RequestParam("token") String token) {
		int response=400;
		if(token.equalsIgnoreCase(crm_token)) {
			boolean sendmail = this.emailSender.sendmail(to,cc, subject, message, "NA");
			if(sendmail)response=200;
		}
		
		if(response==200)
			return new ResponseEntity<Integer>(200,HttpStatus.OK);
		else
			return new ResponseEntity<Integer>(400,HttpStatus.BAD_REQUEST);
	}
	
	@GetMapping("/api")
	public Api api(@RequestParam(value = "state", defaultValue = "Delhi") String state, @RequestParam(value = "ctype", defaultValue = "Public Limited Company") String ctype, @RequestParam(value = "capitalAmt", defaultValue = "1000000") Double amount, Double normal, Double moaCharge, Double aoaCharge, Double stampDutyAmt, Double stampDutyAoaAmt, Double stampDutyMoaAmt){
		return new Api(state,ctype,amount, normal, moaCharge, aoaCharge, stampDutyAmt, stampDutyAoaAmt, stampDutyMoaAmt);
	}
	
	@RequestMapping("/api/uploadPhoto")
    public String uploadImage(@RequestParam MultipartFile file){
		return "search";
    }
	@PostMapping("/rating/service")
	public void updateServiceRating(@RequestParam("productNo") String productNo,@RequestParam("totalRating") String totalRating) {
		try {
			if(productNo!=null&&productNo.length()>0&&!productNo.equalsIgnoreCase("NA")) {
				Services service=this.servicesService.findByProductNo(productNo);
				if(service!=null) {
					String x[]=totalRating.split("#");
					service.setRatingUser(x[0]);
					service.setRatingValue(x[1]);
					this.servicesService.saveServices(service);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	@PostMapping("/support/raise-complaint")
	public String raiseComplaint(@RequestParam("iepno") String iepno,@RequestParam("name") String name,
			@RequestParam("mobile") String mobile,@RequestParam("email") String email,@RequestParam("message") String message,
			@RequestParam("postDate") String postDate) {
		try {
			String to="escalation@corpseed.com";
			String desc="<p>Complaint against Invoice/Estimate/Project No :"+iepno+" of "+name+" on date "+postDate+"</p><p>"+message+"</p>";
			boolean sendmail = this.emailSender.sendmail(to,"empty", "Service Complaint : "+mobile, desc, "NA");
			if(sendmail)return("pass");
			else return("fail");
		} catch (Exception e) {
			e.printStackTrace();
			return("fail");
		}
	}
}