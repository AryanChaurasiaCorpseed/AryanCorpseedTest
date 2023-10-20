package com.corpseed.serviceImpl.servicesserviceimpl;

import com.corpseed.entity.Category;
import com.corpseed.entity.serviceentity.ServiceDetails;
import com.corpseed.entity.serviceentity.ServiceFaq;
import com.corpseed.entity.serviceentity.ServicePackage;
import com.corpseed.entity.serviceentity.Services;
import com.corpseed.repository.servicerepo.ServiceDetailsRepository;
import com.corpseed.repository.servicerepo.ServiceFaqRepository;
import com.corpseed.repository.servicerepo.ServicePackageRepo;
import com.corpseed.repository.servicerepo.ServiceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@Service
public class ServicesService {

	@Autowired
	private ServiceRepository serviceRepository;
	
	@Autowired
	private ServicePackageRepo servicePackageRepo;
	
	@Autowired
	private ServiceFaqRepository serviceFaqRepository;
	
	@Autowired
	private ServiceDetailsRepository serviceDetailsRepository;
	
	public Services isServiceExist(String slug, String serviceName) {
		return this.serviceRepository.findBySlugOrServiceName(slug, serviceName); 
	}	     
	@Caching(evict = {
		    @CacheEvict(value = "serviceByUuid", allEntries = true),
		    @CacheEvict(value = "serviceBySlug", allEntries = true),
		    @CacheEvict(value = "serviceDt", allEntries = true),
		    @CacheEvict(value = "findServiceByName", allEntries = true),
		    @CacheEvict(value = "servicePackage", allEntries = true),
		    @CacheEvict(value = "serviceById", allEntries = true),
		    @CacheEvict(value = "serviceDetails", allEntries = true),
		    @CacheEvict(value = "allMenuCategory", allEntries = true),
		    @CacheEvict(value = "top4services", allEntries = true),
		    @CacheEvict(value = "footer", allEntries = true),
		    @CacheEvict(value = "cardCategory", allEntries = true),
			@CacheEvict(value = "serviceContact", allEntries = true),
			@CacheEvict(value = "serviceContactByService", allEntries = true),
			@CacheEvict(value = "faqByServiceAndStatus",allEntries = true),
			@CacheEvict(value = "packagesByService",allEntries = true)
		})
	public Services saveServices(Services service) {
		return this.serviceRepository.save(service);
		
	}

	public List<Services> getAllServices() {
		return serviceRepository.findByDeleteStatusOrderByIdDesc(2);
	}

	public List<Services> getServicesByCategory(Category category) {		
		return serviceRepository.findByCategoryAndDeleteStatus(category,2);
	}

	public Services getServiceById(long serviceId,Category  category) {
		return this.serviceRepository.findByIdAndCategoryAndDeleteStatus(serviceId,category,2);
	}
	@Caching(evict = {
		    @CacheEvict(value = "serviceByUuid", allEntries = true),
		    @CacheEvict(value = "serviceBySlug", allEntries = true),
		    @CacheEvict(value = "serviceDt", allEntries = true),
		    @CacheEvict(value = "findServiceByName", allEntries = true),
		    @CacheEvict(value = "servicePackage", allEntries = true),
		    @CacheEvict(value = "serviceById", allEntries = true),
		    @CacheEvict(value = "serviceDetails", allEntries = true),
			@CacheEvict(value = "packagesByService",allEntries = true)
		})
	public ServicePackage saveServicePackage(ServicePackage servicePackage) {
		return this.servicePackageRepo.save(servicePackage);
	}

	public ServicePackage isServicePackageExist(String packageName, Services service) {		
		return this.servicePackageRepo.findByPackageNameAndServicesAndDeleteStatus(packageName,service,2);
	}

	public List<ServicePackage> getAllServicePackages() {		
		return this.servicePackageRepo.findByDeleteStatus(2);
	}

	public Services getServiceById(String serviceId) {
		return this.serviceRepository.getOne(Long.parseLong(serviceId));
	}

	@Cacheable(value = "packagesByService")
	public List<ServicePackage> getPackagesByServices(Services services) {
		return this.servicePackageRepo.findByServicesAndDeleteStatusAndDisplayStatus(services,2,"1");
	}

	public ServiceFaq isServiceFaqExist(String title, Services service) {
		return this.serviceFaqRepository.findByTitleAndServices(title,service);
	}

	@Caching(evict = {
		    @CacheEvict(value = "serviceByUuid", allEntries = true),
		    @CacheEvict(value = "serviceBySlug", allEntries = true),
		    @CacheEvict(value = "serviceDt", allEntries = true),
		    @CacheEvict(value = "findServiceByName", allEntries = true),
		    @CacheEvict(value = "servicePackage", allEntries = true),
		    @CacheEvict(value = "serviceById", allEntries = true),
		    @CacheEvict(value = "serviceDetails", allEntries = true),
			@CacheEvict(value = "faqByServiceAndStatus",allEntries = true)
		})
	public ServiceFaq saveServiceFaq(@Valid ServiceFaq faq) {
		return this.serviceFaqRepository.save(faq);
	}

	public ServiceDetails isServiceDetailsExist(String tabName, Services service) {
		return this.serviceDetailsRepository.findByTabNameAndServices(tabName,service);
	}

	@Caching(evict = {
		    @CacheEvict(value = "serviceByUuid", allEntries = true),
		    @CacheEvict(value = "serviceBySlug", allEntries = true),
		    @CacheEvict(value = "serviceDt", allEntries = true),
		    @CacheEvict(value = "findServiceByName", allEntries = true),
		    @CacheEvict(value = "servicePackage", allEntries = true),
		    @CacheEvict(value = "serviceById", allEntries = true),
		    @CacheEvict(value = "serviceDetails", allEntries = true)
		})
	public ServiceDetails saveServiceDetails(@Valid ServiceDetails details) {
		return this.serviceDetailsRepository.save(details);
	}

	public List<ServiceDetails> getAllServiceDetails(Services service) {
		return this.serviceDetailsRepository.findByServicesAndDeleteStatus(service,2);
	}

	public List<ServiceFaq> getAllServicesFaq(Services services) {
		return this.serviceFaqRepository.findByServicesAndDeleteStatus(services,2);
	}

	public ServiceDetails findByDisplayOrderAndServices(String displayOrder, Services service) {
		return this.serviceDetailsRepository.findByDisplayOrderAndServices(displayOrder,
				service);
	}

	@Cacheable(value="serviceByUuid",key="#uuid")
	public Services findByUUID(String uuid) {
		return this.serviceRepository.findByUuidAndDeleteStatus(uuid,2);
	}

	@Caching(evict = {
		    @CacheEvict(value = "serviceByUuid", allEntries = true),
		    @CacheEvict(value = "serviceBySlug", allEntries = true),
		    @CacheEvict(value = "serviceDt", allEntries = true),
		    @CacheEvict(value = "findServiceByName", allEntries = true),
		    @CacheEvict(value = "servicePackage", allEntries = true),
		    @CacheEvict(value = "serviceById", allEntries = true),
		    @CacheEvict(value = "serviceDetails", allEntries = true),
		    @CacheEvict(value = "top4services", allEntries = true),
		    @CacheEvict(value = "allMenuCategory", allEntries = true),
		    @CacheEvict(value = "footer", allEntries = true),
		    @CacheEvict(value = "cardCategory", allEntries = true),
			@CacheEvict(value = "serviceContact", allEntries = true),
			@CacheEvict(value = "serviceContactByService", allEntries = true),
			@CacheEvict(value = "faqByServiceAndStatus",allEntries = true),
			@CacheEvict(value = "packagesByService",allEntries = true)
		})
	public Services updateService(@Valid Services service) {
		return this.serviceRepository.save(service);
	}

	public ServiceFaq getServiceFaqByUUID(String faqUUID) {
		return this.serviceFaqRepository.findByUuidAndDeleteStatus(faqUUID,2);
	}

	public ServiceFaq findByTitleAndServiceNotUuid(String title, Services service, String faqUUID) {
		return this.serviceFaqRepository.findByTitleAndServicesAndUuidNot(title, 
				service,faqUUID);
	}

	public ServiceDetails getServiceDetailsByUuid(String detailsUUID) {
		return this.serviceDetailsRepository.findByUuidAndDeleteStatus(detailsUUID,2);
	}

	public ServiceDetails findByTabNameAndServicesAndUuidNot(String tabName, Services service, String detailsUUID) {
		return this.serviceDetailsRepository.findByTabNameAndServicesAndUuidNot(tabName,
				service,detailsUUID);
	}

	public ServiceDetails findByDisplayOrderAndServicesAndUuidNot(String displayOrder, Services service,
			String detailsUUID) {
		return this.serviceDetailsRepository.findByDisplayOrderAndServicesAndUuidNot(displayOrder,
				service,detailsUUID);
	}

	public ServicePackage getPackageByUuid(String packageUUID) {
		return this.servicePackageRepo.findByUuidAndDeleteStatus(packageUUID,2);
	}

	public ServicePackage findByPackageNameAndServicesAndUuidNot(String packageName, Services service,
			String packageUUID) {
		return this.servicePackageRepo.findByPackageNameAndServicesAndUuidNot(packageName
				,service,packageUUID);
	}

	@Caching(evict = {
		    @CacheEvict(value = "serviceByUuid", allEntries = true),
		    @CacheEvict(value = "serviceBySlug", allEntries = true),
		    @CacheEvict(value = "serviceDt", allEntries = true),
		    @CacheEvict(value = "findServiceByName", allEntries = true),
		    @CacheEvict(value = "servicePackage", allEntries = true),
		    @CacheEvict(value = "serviceById", allEntries = true),
		    @CacheEvict(value = "top4services", allEntries = true),
		    @CacheEvict(value = "serviceDetails", allEntries = true),
		    @CacheEvict(value = "footer", allEntries = true),
		    @CacheEvict(value = "cardCategory", allEntries = true),
			@CacheEvict(value = "serviceContact", allEntries = true),
			@CacheEvict(value = "serviceContactByService", allEntries = true),
			@CacheEvict(value = "faqByServiceAndStatus",allEntries = true),
			@CacheEvict(value = "packagesByService",allEntries = true)
		})
	public void deleteService(Services service) {
		this.serviceRepository.delete(service);
	}

	@Caching(evict = {
		    @CacheEvict(value = "serviceByUuid", allEntries = true),
		    @CacheEvict(value = "serviceBySlug", allEntries = true),
		    @CacheEvict(value = "serviceDt", allEntries = true),
		    @CacheEvict(value = "findServiceByName", allEntries = true),
		    @CacheEvict(value = "servicePackage", allEntries = true),
		    @CacheEvict(value = "serviceDetails", allEntries = true),
		    @CacheEvict(value = "serviceById", allEntries = true),
			@CacheEvict(value = "faqByServiceAndStatus",allEntries = true)
		})
	public void deleteServiceFaq(ServiceFaq serviceFaq) {
		this.serviceFaqRepository.delete(serviceFaq);
	}

	@Caching(evict = {
		    @CacheEvict(value = "serviceByUuid", allEntries = true),
		    @CacheEvict(value = "serviceBySlug", allEntries = true),
		    @CacheEvict(value = "serviceDt", allEntries = true),
		    @CacheEvict(value = "findServiceByName", allEntries = true),
		    @CacheEvict(value = "servicePackage", allEntries = true),
		    @CacheEvict(value = "serviceDetails", allEntries = true),
		    @CacheEvict(value = "serviceById", allEntries = true)
		})
	public void deleteServiceDetails(ServiceDetails serviceDetails) {
		this.serviceDetailsRepository.delete(serviceDetails);
	}
	@Caching(evict = {
		    @CacheEvict(value = "serviceByUuid", allEntries = true),
		    @CacheEvict(value = "serviceBySlug", allEntries = true),
		    @CacheEvict(value = "serviceDt", allEntries = true),
		    @CacheEvict(value = "findServiceByName", allEntries = true),
		    @CacheEvict(value = "servicePackage", allEntries = true),
		    @CacheEvict(value = "serviceDetails", allEntries = true),
		    @CacheEvict(value = "serviceById", allEntries = true),
		    @CacheEvict(value = "allMenuCategory", allEntries = true),
			@CacheEvict(value = "packagesByService",allEntries = true)
		})
	public void deleteServicePackage(ServicePackage servicePackage) {
		this.servicePackageRepo.delete(servicePackage);
	}

	public long countAllServices() {
		return this.serviceRepository.count();
	}
	
	@Cacheable(value="serviceBySlug",key = "#slug")
	public Services findBySlug(String slug) {
		return this.serviceRepository.findBySlugAndDeleteStatus(slug,2);
	}

	@Cacheable("serviceDt")
	public List<Services> findByCategoryAndDisplayStatus(Category category, String status) {
		return this.serviceRepository.findByCategoryAndDisplayStatusAndDeleteStatus(category, 
				status,2);
	}

	@Cacheable("findServiceByName")
	public List<Services> findByServiceName(String value) {
		return this.serviceRepository.findByServiceNameIgnoreCaseContainingAndDeleteStatus(value,2);
	}

	public List<Services> getRecentSixServices() {
		return this.serviceRepository.findTop6ByDeleteStatusOrderByIdDesc(2);
	}

	@Cacheable(value = "servicePackage",key = "#packId")
	public Optional<ServicePackage> getPackageById(String packId) {
		return this.servicePackageRepo.findById(Long.parseLong(packId));
		
	}

	public List<Services> findByUuidAndDisplayStatus(String uuid, String status) {
		return this.serviceRepository.findByUuidAndDisplayStatusAndDeleteStatus(uuid,status,2);
	}

	public Services findByIdAndDisplayStatus(long id, String status) {
		return this.serviceRepository.findByIdAndDisplayStatus(id, status);
	}

	public List<Services> getAllServicesByStatus(String status) {
		return this.serviceRepository.findByDisplayStatusAndDeleteStatus(status,2);
	}
	
	public List<ServiceFaq> findAllServicesFaqByServices(Services service) {
		return this.serviceFaqRepository.findByServicesAndDeleteStatus(service,2);
	}

	public List<Services> findByDisplayStatusAndPackageNotEmpty(String status) {
		return this.serviceRepository.findByDisplayStatusAndPackageNotEmpty(status);
	}

	public Services findByProductNoAndUuidNot(String productNo, String uuid) {
		return this.serviceRepository.findByProductNoAndUuidNot(productNo,uuid);
	}

	public Services findByProductNo(String productNo) {
		return this.serviceRepository.findByProductNo(productNo);
	}

	public Services isServiceDuplicate(String slug, String serviceName, String uuid) {
		// TODO Auto-generated method stub
		return this.serviceRepository.findBySlugOrServiceNameAndUuidNotIn(slug,serviceName,uuid);
	}

	@Cacheable(value = "serviceById",key = "#serviceId")
	public Services findById(long serviceId) {
		// TODO Auto-generated method stub
		return this.serviceRepository.findByIdAndDeleteStatus(serviceId,2);
	}

	public List<Services> findAll() {
		// TODO Auto-generated method stub
		return this.serviceRepository.findAll();
	}

	public List<Services> findByCategoryName(String category) {
		// TODO Auto-generated method stub
		return this.serviceRepository.findByCategoryName(category);
	}

	@Caching(evict = {
		    @CacheEvict(value = "serviceByUuid", allEntries = true),
		    @CacheEvict(value = "serviceBySlug", allEntries = true),
		    @CacheEvict(value = "serviceDt", allEntries = true),
		    @CacheEvict(value = "findServiceByName", allEntries = true),
		    @CacheEvict(value = "servicePackage", allEntries = true),
		    @CacheEvict(value = "serviceDetails", allEntries = true),
		    @CacheEvict(value = "serviceById", allEntries = true),
		    @CacheEvict(value = "allMenuCategory", allEntries = true),
		    @CacheEvict(value = "top4services", allEntries = true),
		    @CacheEvict(value = "footer", allEntries = true),
		    @CacheEvict(value = "cardCategory", allEntries = true),
			@CacheEvict(value = "serviceContact", allEntries = true),
			@CacheEvict(value = "serviceContactByService", allEntries = true),
			@CacheEvict(value = "faqByServiceAndStatus",allEntries = true),
			@CacheEvict(value = "packagesByService",allEntries = true)
		})
	public void saveAllServices(List<Services> services) {
		this.serviceRepository.saveAll(services);
	}

	@Caching(evict = {
		    @CacheEvict(value = "serviceByUuid", allEntries = true),
		    @CacheEvict(value = "serviceBySlug", allEntries = true),
		    @CacheEvict(value = "serviceDt", allEntries = true),
		    @CacheEvict(value = "findServiceByName", allEntries = true),
		    @CacheEvict(value = "servicePackage", allEntries = true),
		    @CacheEvict(value = "serviceDetails", allEntries = true),
		    @CacheEvict(value = "serviceById", allEntries = true),
		    @CacheEvict(value = "allMenuCategory", allEntries = true),
		    @CacheEvict(value = "footer", allEntries = true),
			@CacheEvict(value = "packagesByService",allEntries = true)
		})
	public void saveServicePackages(List<ServicePackage> servicePackage) {
		this.servicePackageRepo.saveAll(servicePackage);
	}

	@Caching(evict = {
		    @CacheEvict(value = "serviceByUuid", allEntries = true),
		    @CacheEvict(value = "serviceBySlug", allEntries = true),
		    @CacheEvict(value = "serviceDt", allEntries = true),
		    @CacheEvict(value = "findServiceByName", allEntries = true),
		    @CacheEvict(value = "servicePackage", allEntries = true),
		    @CacheEvict(value = "serviceDetails", allEntries = true),
		    @CacheEvict(value = "serviceById", allEntries = true),
			@CacheEvict(value = "faqByServiceAndStatus",allEntries = true)
		})
	public void saveServicesFaq(List<ServiceFaq> serviceFaq) {
		this.serviceFaqRepository.saveAll(serviceFaq);
	}

	@Caching(evict = {
		    @CacheEvict(value = "serviceByUuid", allEntries = true),
		    @CacheEvict(value = "serviceBySlug", allEntries = true),
		    @CacheEvict(value = "serviceDt", allEntries = true),
		    @CacheEvict(value = "findServiceByName", allEntries = true),
		    @CacheEvict(value = "servicePackage", allEntries = true),
		    @CacheEvict(value = "serviceDetails", allEntries = true),
		    @CacheEvict(value = "serviceById", allEntries = true)
		})
	public void saveServicesDetails(List<ServiceDetails> serviceDetails) {
		this.serviceDetailsRepository.saveAll(serviceDetails);
	}

	public Services findByIdAndDeleteStatus(long typeId, int dStatus) {
		// TODO Auto-generated method stub
		return this.serviceRepository.findByIdAndDeleteStatus(typeId, dStatus);
	}

	public ServiceFaq findFaqByIdAndDeleteStatus(long typeId, int dStatus) {
		// TODO Auto-generated method stub
		return this.serviceFaqRepository.findByIdAndDeleteStatus(typeId,dStatus);
	}

	public ServiceDetails findServiceDetailsByIdAndStatus(long typeId, int dStatus) {
		// TODO Auto-generated method stub
		return this.serviceDetailsRepository.findByIdAndDeleteStatus(typeId,dStatus);
	}

	public ServicePackage findPackageByIdAndStatus(long typeId, int dStatus) {
		// TODO Auto-generated method stub
		return this.servicePackageRepo.findByIdAndDeleteStatus(typeId,dStatus);
	}
	
	@Cacheable(value = "top4services")
	public List<Services> findTop4ByDisplayStatusAndDeleteStatusOrderByVisitedDesc(String string, int i) {
		// TODO Auto-generated method stub
		return this.serviceRepository.findTop4ByDisplayStatusAndDeleteStatusOrderByVisitedDesc(string, i);
	}
	
	@CacheEvict(value = "top4services", allEntries = true)	
	public void updateServiceVisitor(Services service) {
		this.serviceRepository.save(service);
	}

	@Cacheable(value = "faqByServiceAndStatus")
	public List<ServiceFaq> findFaqByServiceAndStatus(Services service, int i) {
		// TODO Auto-generated method stub
		return this.serviceFaqRepository.findByServicesAndDeleteStatusAndDisplayStatus(service, i,"1");
	}
	@Cacheable(value = "serviceDetails")
	public List<ServiceDetails> findServiceDetailsByServicesAndDeleteStatus(Services service, int i) {
		// TODO Auto-generated method stub
		return this.serviceDetailsRepository.findByServicesAndDeleteStatusAndDisplayStatus(service, i,"1");
	}
	@Cacheable("findServiceByName")
	public List<Services> findTop4ByServiceName(String value) {
		// TODO Auto-generated method stub
		return this.serviceRepository.findTop4ByDisplayStatusAndDeleteStatusAndServiceNameIgnoreCaseContaining("1",2,value);
	}
	public Services findServiceByUuidAndDeleteStatus(String serviceUuid, int i) {
		// TODO Auto-generated method stub
		return this.serviceRepository.findByUuidAndDeleteStatus(serviceUuid, i);
	}
	public List<Services> findAllProductServicesByDeleteStatus(int dStatus,String displayStatus) {
		return this.serviceRepository.findAllProductServicesByDeleteStatus(dStatus,displayStatus);
	}

    public List<Services> getAllServicesNotInCity(int deletStatus) {
		return this.serviceRepository.findAllServicesNotInCity(deletStatus);
    }
}
