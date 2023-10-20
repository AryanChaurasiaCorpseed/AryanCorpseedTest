package com.corpseed.serviceImpl;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import com.corpseed.entity.HotTags;
import com.corpseed.entity.serviceentity.Services;
import com.corpseed.repository.HotTagRepository;

@Service
public class HotTagService {

	@Autowired
	private HotTagRepository hotTagRepository;

	public List<HotTags> findAll() {
		return this.hotTagRepository.findByDeleteStatus(2);
	}

	public HotTags findByUuid(String uuid) {
		return this.hotTagRepository.findByUuidAndDeleteStatus(uuid,2);
	}

	@CacheEvict(value = "hotTags4",allEntries = true)
	public HotTags saveTag(@Valid HotTags tags) {
		return this.hotTagRepository.save(tags);
	}
	
	@CacheEvict(value = "hotTags4",allEntries = true)
	public void deleteTag(long id) {
		this.hotTagRepository.deleteById(id);
	}

	public HotTags findByName(String name) {
		return this.hotTagRepository.findByName(name);
	}

	public HotTags findByNameAndUuidNot(String name, String uuid) {
		return this.hotTagRepository.findByNameAndUuidNot(name,uuid);
	}

	public HotTags findByServices(Services service) {
		return this.hotTagRepository.findByServices(service);
	}

	public HotTags findByServicesAndUuidNot(Services service, String uuid) {
		return this.hotTagRepository.findByServicesAndUuidNot(service,uuid);
	}

	@Cacheable("hotTags4")
	public List<HotTags> findTop4ByDisplayStatus(String status) {
		return this.hotTagRepository.findTop4ByDisplayStatusAndDeleteStatus(status,2);
	}
	
	@CacheEvict(value = "hotTags4",allEntries = true)
	public void saveTags(List<HotTags> hotTags) {
		this.hotTagRepository.saveAll(hotTags);
	}

	public HotTags findHotTagByIdAndStatus(long typeId, int i) {
		// TODO Auto-generated method stub
		return this.hotTagRepository.findByIdAndDeleteStatus(typeId,i);
	}
}
