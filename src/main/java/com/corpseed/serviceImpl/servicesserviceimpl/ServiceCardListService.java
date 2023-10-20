package com.corpseed.serviceImpl.servicesserviceimpl;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;

import com.corpseed.entity.serviceentity.ServiceCardList;
import com.corpseed.entity.serviceentity.ServiceDetails;
import com.corpseed.entity.serviceentity.Services;
import com.corpseed.repository.servicerepo.ServiceCardListRepo;

@Service
public class ServiceCardListService {

	@Autowired
	private ServiceCardListRepo serviceCardListRepo;

	@Caching(evict = {
		    @CacheEvict(value = "serviceByUuid", allEntries = true),
		    @CacheEvict(value = "serviceBySlug", allEntries = true),
		    @CacheEvict(value = "serviceDt", allEntries = true),
		    @CacheEvict(value = "findServiceByName", allEntries = true),
		    @CacheEvict(value = "servicePackage", allEntries = true),
		    @CacheEvict(value = "serviceById", allEntries = true),
		    @CacheEvict(value = "allMenuCategory", allEntries = true)
		})
	public void saveAll(List<ServiceCardList> serviceCardList) {
		if(!serviceCardList.isEmpty())
			this.serviceCardListRepo.saveAll(serviceCardList);
	}

	public List<ServiceCardList> findByServiceDetails(@Valid ServiceDetails details) {
		// TODO Auto-generated method stub
		return this.serviceCardListRepo.findByServiceDetails(details);
	}

	@Caching(evict = {
		    @CacheEvict(value = "serviceByUuid", allEntries = true),
		    @CacheEvict(value = "serviceBySlug", allEntries = true),
		    @CacheEvict(value = "serviceDt", allEntries = true),
		    @CacheEvict(value = "findServiceByName", allEntries = true),
		    @CacheEvict(value = "servicePackage", allEntries = true),
		    @CacheEvict(value = "serviceById", allEntries = true),
		    @CacheEvict(value = "allMenuCategory", allEntries = true)
		})
	public void deleteAll(List<ServiceCardList> allScardList) {
		if(!allScardList.isEmpty())
		this.serviceCardListRepo.deleteInBatch(allScardList);
	}

	public ServiceCardList findByServiceDetailsAndService(ServiceDetails saveServiceDetails, Services eservice) {
		// TODO Auto-generated method stub
		return this.serviceCardListRepo.findByServiceDetailsAndService(saveServiceDetails, eservice);
	}
}
