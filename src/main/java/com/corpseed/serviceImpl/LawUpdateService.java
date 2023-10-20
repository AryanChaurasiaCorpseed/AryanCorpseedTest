package com.corpseed.serviceImpl;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.corpseed.entity.LawUpdate;
import com.corpseed.repository.LawUpdateRepository;

@Service
public class LawUpdateService {

	@Autowired
	private LawUpdateRepository lawUpdateRepository;
	
	@Caching(evict = {
		    @CacheEvict(value = "allLawUpdates", allEntries = true),
		    @CacheEvict(value = "lawUpdateByDept", allEntries = true),
		    @CacheEvict(value = "allLawUpdatesByStatus", allEntries = true),
		    @CacheEvict(value = "lawUpdateBySlug", allEntries = true)
		})
	public LawUpdate saveLawUpdate(@Valid LawUpdate lawUpdate) {
		return this.lawUpdateRepository.save(lawUpdate);
	}

	public List<LawUpdate> getAllLawUpdates() {
		return this.lawUpdateRepository.findByDeleteStatusOrderByIdDesc(2);
	}
	
	public LawUpdate getLawUpdateByUuid(String lawuuid) {
		return this.lawUpdateRepository.findByUuidAndDeleteStatus(lawuuid,2);
	}
	@Caching(evict = {
		    @CacheEvict(value = "allLawUpdates", allEntries = true),
		    @CacheEvict(value = "lawUpdateByDept", allEntries = true),
		    @CacheEvict(value = "allLawUpdatesByStatus", allEntries = true),
		    @CacheEvict(value = "lawUpdateBySlug", allEntries = true)
		})
	public void deleteLawUpdate(long id) {
		this.lawUpdateRepository.deleteById(id);
	}

	@Cacheable(value="allLawUpdatesByStatus")
	public Page<LawUpdate> getLawUpdateByStatus(String status,Pageable pageable) {
		return this.lawUpdateRepository.findByDisplayStatusAndDeleteStatus(status,2,pageable);
	}

	@Cacheable(value = "lawUpdateBySlug",key="#slug")
	public LawUpdate getLawUpdateBySlug(String slug) {
		return this.lawUpdateRepository.findBySlugAndDeleteStatus(slug,2);
	}

	@Cacheable(value = "lawUpdateByDept")
	public Page<LawUpdate> getLawUpdateByDateAndDepartment(String fromDate, String toDate, String department,Pageable pageable) {
		return this.lawUpdateRepository.findByDepartmentAndPostDateBetweenAndDisplayStatusAndDeleteStatus(department,fromDate,toDate,"1",2,pageable);

	}
	@Caching(evict = {
		    @CacheEvict(value = "allLawUpdates", allEntries = true),
		    @CacheEvict(value = "lawUpdateByDept", allEntries = true),
		    @CacheEvict(value = "allLawUpdatesByStatus", allEntries = true),
		    @CacheEvict(value = "lawUpdateBySlug", allEntries = true)
		})
	public void saveAllLawUpdate(List<LawUpdate> lawUpdate) {
		this.lawUpdateRepository.saveAll(lawUpdate);
	}

	public LawUpdate findByIdAndDeleteStatus(long typeId, int i) {
		// TODO Auto-generated method stub
		return this.lawUpdateRepository.findByIdAndDeleteStatus(typeId,i);
	}

	public Page<LawUpdate> getLawUpdateBySearch(String status, String fromDate, String toDate,String department,Pageable pageable) {
		// TODO Auto-generated method stub
		return this.lawUpdateRepository.findByDepartmentAndDisplayStatusAndDeleteStatusAndPostDateBetween(department,status,2,fromDate,toDate,pageable);
	}

}
