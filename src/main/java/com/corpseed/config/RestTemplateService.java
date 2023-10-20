package com.corpseed.config;

import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;

@Service
public class RestTemplateService {
	
	   @Autowired
	   RestTemplate restTemplate;

	 public ResponseEntity<String> getProductListsu() {
		   LeadDto lead = new LeadDto();
			lead.setName("aryan");
//			lead.setLeadName(leadDTO.getLeadName());
			lead.setLeadDescription("you r the best person");
			lead.setMobileNo("7651959792");
			lead.setEmail("aryan@gmail.com");
			lead.setUrls("kfgsdghkg");
			lead.setCreateDate(new Date());		
			lead.setLastUpdated(new Date());
			lead.setLatestStatusChangeDate(new Date());
//			lead.setClients(cList);
			lead.setSource("whatsapp");
			lead.setPrimaryAddress("gurugram ");
			lead.setDeleted(false);
			lead.setCity("haryana");
			lead.setCategoryId("1");
			lead.setServiceId("2");
			lead.setIndustryId("3");
			lead.setIpAddress("127.000.01");
			lead.setDisplayStatus("1");
			lead.setUuid("1234567");
		   
		   HttpHeaders headers = new HttpHeaders();
		   headers.setContentType(MediaType.APPLICATION_JSON);
		   String url = "http://localhost:9001/leadService/api/v1/lead/createLead";
		   HttpEntity<LeadDto> request = new HttpEntity<LeadDto>(lead, headers);

		   ResponseEntity<String> response = restTemplate.postForEntity( url, request , String.class );
		   return response;
		   
	   }
	   public String saveChangeYouCAEnquiry(String otp,String name,String email,String mobile,
                String city,String packTime, String packPrice,String packageName,
                String location, String postdate,String modifydate) {
		   return null;
		   
	   }
	
}
