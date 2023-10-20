package com.corpseed.serviceImpl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import com.corpseed.entity.TrandingSearch;
import com.corpseed.repository.TrandingSearchRepo;

@Service
public class TrandingSearchService {

	@Autowired
	private TrandingSearchRepo trandingSearchRepo;

	@Cacheable("trandingSearch")
	public TrandingSearch findBySearchName(String key) {
		return this.trandingSearchRepo.findBySearchName(key);
	}

	@CacheEvict(value = "trandingSearch",allEntries = true)
	public void saveTrandingSearch(TrandingSearch search) {
		this.trandingSearchRepo.save(search);
	}

	public List<TrandingSearch> findTop5ByOrderBySearchedDesc() {
		return this.trandingSearchRepo.findTop5ByOrderBySearchedDesc();
	}
	
	
}
