package com.corpseed.serviceImpl;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;

import com.corpseed.entity.serviceentity.ServiceBrands;
import com.corpseed.entity.serviceentity.Services;
import com.corpseed.repository.servicerepo.ServiceBrandRepo;
import com.corpseed.service.servicesservice.ServiceBrand;

@Service
public class BrandServiceImpl implements ServiceBrand {

	@Autowired
	private ServiceBrandRepo serviceBrandRepo;

	@Override
	@Caching(evict = {
		    @CacheEvict(value = "serviceByUuid", allEntries = true),
		    @CacheEvict(value = "serviceBySlug", allEntries = true),
		    @CacheEvict(value = "serviceDt", allEntries = true),
		    @CacheEvict(value = "findServiceByName", allEntries = true),
		    @CacheEvict(value = "servicePackage", allEntries = true),
		    @CacheEvict(value = "serviceById", allEntries = true),
		    @CacheEvict(value = "allMenuCategory", allEntries = true),
		    @CacheEvict(value = "serviceBrands", allEntries = true),
		    @CacheEvict(value = "top5services", allEntries = true)
		})
	public ServiceBrands saveServiceBrand(@Valid ServiceBrands brand) {
		// TODO Auto-generated method stub
		return this.serviceBrandRepo.save(brand);
	}

	@Override
	public ServiceBrands getServiceBrand(String uuid,int dStatus) {
		// TODO Auto-generated method stub
		return this.serviceBrandRepo.findByUuidAndDeleteStatus(uuid,dStatus);
	}

	@Override
	public ServiceBrands getServiceBrandByIdAndStatus(long typeId, int dStatus) {
		// TODO Auto-generated method stub
		return this.serviceBrandRepo.findByIdAndDeleteStatus(typeId,dStatus);
	}

	@Override
	@Caching(evict = {
		    @CacheEvict(value = "serviceByUuid", allEntries = true),
		    @CacheEvict(value = "serviceBySlug", allEntries = true),
		    @CacheEvict(value = "serviceDt", allEntries = true),
		    @CacheEvict(value = "findServiceByName", allEntries = true),
		    @CacheEvict(value = "servicePackage", allEntries = true),
		    @CacheEvict(value = "serviceById", allEntries = true),
		    @CacheEvict(value = "allMenuCategory", allEntries = true),
		    @CacheEvict(value = "serviceBrands", allEntries = true),
		    @CacheEvict(value = "top5services", allEntries = true)
		})
	public void deleteServiceBrand(ServiceBrands brand) {
		this.serviceBrandRepo.delete(brand);
	}

	@Override
	@Cacheable(value = "serviceBrands")
	public List<ServiceBrands> findByServicesAndDeleteStatus(Services service, int dStatus) {
		// TODO Auto-generated method stub
		return this.serviceBrandRepo.findByServicesAndDeleteStatus(service,dStatus);
	}

	

}
