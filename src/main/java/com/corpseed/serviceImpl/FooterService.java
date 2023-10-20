package com.corpseed.serviceImpl;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import com.corpseed.entity.footerentity.FooterCategory;
import com.corpseed.entity.serviceentity.Services;
import com.corpseed.repository.footerrepo.FooterRepository;
import com.corpseed.repository.footerrepo.FooterServiceRepo;

@Service
public class FooterService {

	@Autowired
	private FooterRepository footerRepository;
	
	@Autowired
	private FooterServiceRepo footerServiceRepo;

	public List<FooterCategory> findAll() {
		// TODO Auto-generated method stub
		return this.footerRepository.findByDeleteStatus(2);
	}

	@CacheEvict(value = "footer",allEntries = true)
	public FooterCategory saveFooter(@Valid FooterCategory footer) {
		// TODO Auto-generated method stub
		return this.footerRepository.save(footer); 
	}
	@CacheEvict(value = "footer",allEntries = true)
	public List<com.corpseed.entity.footerentity.FooterService> saveFooterService(List<com.corpseed.entity.footerentity.FooterService> footerService) {
		return this.footerServiceRepo.saveAll(footerService);
	}

	public FooterCategory findByCategoryName(String categoryName) {
		// TODO Auto-generated method stub
		return this.footerRepository.findByCategoryName(categoryName);
	}

	public FooterCategory findByUuid(String uuid) {
		// TODO Auto-generated method stub
		return this.footerRepository.findByUuidAndDeleteStatus(uuid,2);
	}

	@CacheEvict(value = "footer",allEntries = true)
	public void deleteFooter(long id) {
		this.footerRepository.deleteById(id);
	}

	public FooterCategory findByCategoryNameAndUuidNot(String categoryName, String uuid) {
		// TODO Auto-generated method stub
		return this.footerRepository.findByCategoryNameAndUuidNot(categoryName,uuid);
	}

	public com.corpseed.entity.footerentity.FooterService findByFooterCategoryAndServices(FooterCategory saveFooter,
                                                                                          Services service) {
		// TODO Auto-generated method stub
		return this.footerServiceRepo.findByFooterCategoryAndServicesAndDeleteStatus(saveFooter,service,2);
	}

	public List<com.corpseed.entity.footerentity.FooterService> findByFooterCategory(FooterCategory saveFooter) {
		// TODO Auto-generated method stub
		return this.footerServiceRepo.findByFooterCategoryAndDeleteStatus(saveFooter,2);
	}

	@CacheEvict(value = "footer",allEntries = true)
	public void removeAll(List<com.corpseed.entity.footerentity.FooterService> allFooterService) {
		this.footerServiceRepo.deleteInBatch(allFooterService);
	}

	@Cacheable(value="footer")
	public List<FooterCategory> findByDisplayStatus(String status) {
		// TODO Auto-generated method stub
		return this.footerRepository.findByDisplayStatusAndDeleteStatus(status,2);
	} 

	@CacheEvict(value = "footer",allEntries = true)
	public void saveFooterService(com.corpseed.entity.footerentity.FooterService footerService) {
		this.footerServiceRepo.save(footerService);
	}

	public FooterCategory findByIdAndDeleteStatus(long typeId, int dStatus) {
		// TODO Auto-generated method stub
		return this.footerRepository.findByIdAndDeleteStatus(typeId,dStatus);
	}

}
