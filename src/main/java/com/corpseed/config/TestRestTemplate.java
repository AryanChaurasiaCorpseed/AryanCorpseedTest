package com.corpseed.config;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.azure.core.annotation.Post;

@RestController
public class TestRestTemplate {
	
	   @Autowired
	   RestTemplate restTemplate;
	   
	   @Autowired
	   RestTemplateService restTemplateService;

	   @RequestMapping(value = "/template/products")
	   public String getProductList() {
	      HttpHeaders headers = new HttpHeaders();
	      headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
	      HttpEntity <String> entity = new HttpEntity<String>(headers);
	      
	      return restTemplate.exchange("http://localhost:9001/leadService/api/v1/lead/test", HttpMethod.GET, entity, String.class).getBody();
	   }
	   
	   @RequestMapping(value = "/leadService/api/v1/lead/createLead", method = RequestMethod.POST)
	   public String deleteProduct(@RequestBody LeadDto leadDto) {
	      HttpHeaders headers = new HttpHeaders();
	      headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
	      HttpEntity<LeadDto> entity = new HttpEntity<LeadDto>(leadDto,headers);
	      
	      return restTemplate.exchange(
	    		  "http://localhost:9001/leadService/api/v1/lead/createLead", HttpMethod.POST, entity, String.class).getBody();
	   }
	   
	   @PostMapping(value = "/template/productsm")
	   public ResponseEntity<String> getProductLists(@RequestBody LeadDto leadDto) {
		   
		   HttpHeaders headers = new HttpHeaders();
		   headers.setContentType(MediaType.APPLICATION_JSON);
		   String url = "http://localhost:9001/leadService/api/v1/lead/createLead";
		   HttpEntity<LeadDto> request = new HttpEntity<LeadDto>(leadDto, headers);

		   ResponseEntity<String> response = restTemplate.postForEntity( url, request , String.class );
		   return response;
		   
//           leadDto.setLeadName(name);
//           leadDto.setEmail(email);
//           leadDto.setMobileNo(mobile);
//           leadDto.setCity(city);
//           leadDto.setServiceId(serviceId);
//           leadDto.setDeleted(false);
//           leadDto.setIpAddress(this.commonService.getIpAddress(req));
//           leadDto.setDisplayStatus("1");
//           leadDto.setUrls(url);
//           leadDto.setSource(url)
//           leadDto.setCategoryId(categoryId);
//           System.out.println("hiiii ");
//           testRestTemplate.getProductLists(leadDto);
	   }
	   @PostMapping(value = "/template/productsu")
	   public ResponseEntity<String> getProductListsu() {
		   ResponseEntity<String> res = restTemplateService.getProductListsu();
		   return res;
		   
	   }
	   
	   public String saveChangeYouCAEnquiry(@RequestParam("otp") String otp, @RequestParam("name") String name, @RequestParam("email") String email, @RequestParam("mobile") String mobile,
               @RequestParam("city") String city, @RequestParam("packTime") String packTime, @RequestParam("packPrice") String packPrice, @RequestParam("packageName") String packageName,
               @RequestParam("location") String location, @RequestParam("postDate") String postdate, @RequestParam("modifyDate") String modifydate
, HttpServletRequest req) {
		   return null;
		   
	   }
}
