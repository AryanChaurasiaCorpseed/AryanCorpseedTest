package com.corpseed.serviceImpl.enqserviceimpl;

import java.util.List;

import javax.validation.Valid;

import com.corpseed.entity.enquiryentity.EnquiryBlocker;
import com.corpseed.service.EnquiryBlockerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import com.corpseed.entity.enquiryentity.Enquiry;
import com.corpseed.repository.enqrepo.EnquiryRepository;

@Service
public class EnquiryService {
	
	@Autowired
	private EnquiryRepository enquiryRepository;

	@Autowired
	private EnquiryBlockerService enquiryBlockerService;

	@CacheEvict(value = "enquiryByUuid",allEntries = true)
	public Enquiry saveEnquiry(@Valid Enquiry enquiry) {
		return this.enquiryRepository.save(enquiry);
	}

	public long countAllEnquiry() {
		return this.enquiryRepository.count();
	}

	public List<Enquiry> getAllEnquiryByTypeAndStatusAndCategoryId(String type,String status,String id) {
		return this.enquiryRepository.findByTypeAndDisplayStatusAndCategoryId(type,status,id);
	}

	public List<Enquiry> getAllEnquiryByTypeAndStatusAndServiceId(String type, String status, String id) {
		return this.enquiryRepository.findByTypeAndDisplayStatusAndServiceIdAndDeleteStatus(type, status, id,2);
	}

	public List<Enquiry> getAllEnquiryByTypeAndStatusAndIndustryId(String type, String status, String id) {
		return this.enquiryRepository.findByTypeAndDisplayStatusAndIndustryIdAndDeleteStatus(type, status, id,2);
	}
	@CacheEvict(value = "enquiryByUuid",allEntries = true)
	public void deleteEnquiry(long id) {
		this.enquiryRepository.deleteById(id);
	}

	@Cacheable(value = "enquiryByUuid",key ="#enquiryUuid" )
	public Enquiry findByUuid(String enquiryUuid) {
		return this.enquiryRepository.findByUuidAndDeleteStatus(enquiryUuid,2);
	}

	public int findByEmailOrMobile(String email, String mobile) {
		// TODO Auto-generated method stub
		return this.enquiryRepository.findByEmailOrMobile(email,mobile);
	}

	public int findByMobile(String mobile) {
		// TODO Auto-generated method stub
		return this.enquiryRepository.findByMobile(mobile);
	}

	public Enquiry findByIdAndDeleteStatus(long typeId, int dStatus) {
		// TODO Auto-generated method stub
		return this.enquiryRepository.findByIdAndDeleteStatus(typeId,dStatus);
	}

	public List<Enquiry> findByDisplayStatusAndDeleteStatusAndBitrixStatus(String string, int i, int j) {
		// TODO Auto-generated method stub
		return this.enquiryRepository.findByDisplayStatusAndDeleteStatusAndBitrixStatus(string, i, j);
	}

	public void saveAllEnquiry(List<Enquiry> enqTest) {
//		System.out.println("saving enquiry....................");
		this.enquiryRepository.saveAll(enqTest);
//		System.out.println("Enquiry saved......................");
	}

	public boolean isValidRequest(String mobile, String email) {
//		System.out.println("isValidRequest=mobile==="+mobile);
		if(mobile.length()>10)mobile=mobile.substring(mobile.length()-10);
//		System.out.println("mobile==="+mobile);
		EnquiryBlocker enquiryBlocker=enquiryBlockerService.findByMobileOrEmail(mobile,email);
		if(enquiryBlocker==null)
			return false;
		return true;
	}

}
