package com.corpseed.serviceImpl;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;

import com.corpseed.entity.industryentity.Industry;
import com.corpseed.entity.industryentity.IndustryDetails;
import com.corpseed.repository.industryrepo.IndustryDetailsRepo;
import com.corpseed.repository.industryrepo.IndustryRepository;

@Service
public class IndustryService {

	@Autowired
	private IndustryRepository industryRepository;
	
	@Autowired
	private IndustryDetailsRepo industryDetailsRepo;
	
	@Caching(evict = {
		    @CacheEvict(value = "industryByUuid", allEntries = true),
		    @CacheEvict(value = "industries", allEntries = true),
		    @CacheEvict(value = "industryBySlug", allEntries = true),
		    @CacheEvict(value = "findByIndustryIdAndStatusNot", allEntries = true),
			@CacheEvict(value = "industryByName", allEntries = true),
			@CacheEvict(value = "topIndustryByVisited",allEntries = true)
		})
	public Industry saveIndustry(@Valid Industry industry) {
		return this.industryRepository.save(industry);
	}

	public List<Industry> getAllIndustry() {
		return this.industryRepository.findByDeleteStatusOrderByIdDesc(2);
	}

	@Cacheable(value = "industryByUuid",key = "#uuid")
	public Industry findByUuid(String uuid) {
		return this.industryRepository.findByUuidAndDeleteStatus(uuid,2);
	}
	@Caching(evict = {
		    @CacheEvict(value = "industryByUuid", allEntries = true),
		    @CacheEvict(value = "industries", allEntries = true),
		    @CacheEvict(value = "industryBySlug", allEntries = true),
		    @CacheEvict(value = "findByIndustryIdAndStatusNot", allEntries = true),
			@CacheEvict(value = "industryByName", allEntries = true),
			@CacheEvict(value = "topIndustryByVisited",allEntries = true)
		})
	public void deleteIndustry(Industry industry) {
		this.industryRepository.delete(industry);
	}

	@Cacheable("industries")
	public List<Industry> getIndustryByDisplsyStatus(String status) {
		return this.industryRepository.findByDisplayStatusAndDeleteStatus(status,2);
	}

	@Cacheable(value = "industryBySlug",key = "#slug")
	public Industry findBySlug(String slug) {
		return this.industryRepository.findBySlug(slug);
	}

	public Industry findBySlugAndUuidNot(String slug, String uuid) {
		return this.industryRepository.findBySlugAndUuidNot(slug,uuid);
	}
	
	@Cacheable(value = "industryBySlug",key = "#slug")
	public Industry getIndustryBySlug(String slug) {
		return this.industryRepository.findBySlug(slug);
	}

	public List<IndustryDetails> getAllIndustryDetails(Industry industry) {
		return this.industryDetailsRepo.findByIndustryAndDeleteStatus(industry,2);
	}

	public IndustryDetails getIndustryDetailsByUuid(String detailsUUID) {
		return this.industryDetailsRepo.findByUuidAndDeleteStatus(detailsUUID,2);
	}

	@Caching(evict = {
		    @CacheEvict(value = "industryByUuid", allEntries = true),
		    @CacheEvict(value = "industries", allEntries = true),
		    @CacheEvict(value = "industryBySlug", allEntries = true),
		    @CacheEvict(value = "findByIndustryIdAndStatusNot", allEntries = true),
			@CacheEvict(value = "industryByName", allEntries = true),
			@CacheEvict(value = "topIndustryByVisited",allEntries = true)
		})
	public void deleteIndustryDetails(IndustryDetails industryDetails) {
		this.industryDetailsRepo.delete(industryDetails);
	}

	public IndustryDetails findByTabNameAndIndustryAndUuidNot(String tabName, Industry industry, String detailsUUID) {
		return this.industryDetailsRepo.findByTabNameAndIndustryAndUuidNot(tabName,industry,detailsUUID);
	}

	public IndustryDetails findByDisplayOrderAndIndustryAndUuidNot(String displayOrder, Industry industry,
			String detailsUUID) {
		return this.industryDetailsRepo.findByDisplayOrderAndIndustryAndUuidNot(displayOrder, industry,detailsUUID);
	}

	@Caching(evict = {
		    @CacheEvict(value = "industryByUuid", allEntries = true),
		    @CacheEvict(value = "industries", allEntries = true),
		    @CacheEvict(value = "industryBySlug", allEntries = true),
		    @CacheEvict(value = "findByIndustryIdAndStatusNot", allEntries = true),
			@CacheEvict(value = "industryByName", allEntries = true),
			@CacheEvict(value = "topIndustryByVisited",allEntries = true)
		})
	public IndustryDetails saveIndustryDetails(@Valid IndustryDetails details) {
		return this.industryDetailsRepo.save(details);
	}

	public IndustryDetails isIndustryDetailsExist(String tabName, Industry industry) {
		return this.industryDetailsRepo.findByTabNameAndIndustry(tabName,industry);
	}

	public IndustryDetails findByDisplayOrderAndIndustry(String displayOrder, Industry industry) {
		return this.industryDetailsRepo.findByDisplayOrderAndIndustry(displayOrder, industry);
	}

	@Cacheable(value = "findByIndustryIdAndStatusNot",key = "#industryId")
	public Industry findByIndustryIdAndStatusNot(long industryId, String status) {
		// TODO Auto-generated method stub
		return this.industryRepository.findByIdAndDeleteStatus(industryId,2);
	}

	@Caching(evict = {
		    @CacheEvict(value = "industryByUuid", allEntries = true),
		    @CacheEvict(value = "industries", allEntries = true),
		    @CacheEvict(value = "industryBySlug", allEntries = true),
		    @CacheEvict(value = "findByIndustryIdAndStatusNot", allEntries = true),
			@CacheEvict(value = "industryByName", allEntries = true),
			@CacheEvict(value = "topIndustryByVisited",allEntries = true)
		})
	public void saveAllIndustryDetails(List<IndustryDetails> industryDetails) {
		this.industryDetailsRepo.saveAll(industryDetails);
	}

	public Industry findByIdAndDeleteStatus(long typeId, int dStatus) {
		// TODO Auto-generated method stub
		return this.industryRepository.findByIdAndDeleteStatus(typeId, dStatus);
	}

	public IndustryDetails findDetailsByIdAndDeleteStatus(long typeId, int dStatus) {
		// TODO Auto-generated method stub
		return this.industryDetailsRepo.findByIdAndDeleteStatus(typeId,dStatus);
	}

	@Cacheable("industryByName")
	public List<Industry> findTop1ByIndustryName(String value) {
		// TODO Auto-generated method stub
		return this.industryRepository.findTop1ByDisplayStatusAndDeleteStatusAndIndustryNameIgnoreCaseContaining("1",2,value);
	}

	@Cacheable("topIndustryByVisited")
	public Industry findTop1ByDisplayStatusAndDeleteStatusOrderByVisitedDesc(String status, int i) {
		// TODO Auto-generated method stub
		return this.industryRepository.findTop1ByDisplayStatusAndDeleteStatusOrderByVisitedDesc(status,i);
	}
	
}
