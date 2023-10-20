package com.corpseed.serviceImpl;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import com.corpseed.entity.StaticSeo;
import com.corpseed.repository.StaticSeoRepository;

@Service
public class StaticSeoService {

	@Autowired
	private StaticSeoRepository staticSeoRepository;

	public List<StaticSeo> findAll() {
		// TODO Auto-generated method stub
		return this.staticSeoRepository.findByDeleteStatus(2);
	}

	@CacheEvict(value = "staticSeo",allEntries = true)
	public void save(@Valid StaticSeo staticSeo) {
		this.staticSeoRepository.save(staticSeo);
	}

	@Cacheable(value = "staticSeo",key = "#page")
	public StaticSeo findByPage(String page) {
		// TODO Auto-generated method stub
		return this.staticSeoRepository.findByPage(page);
	}

	public StaticSeo findByUuid(String uuid) {
		// TODO Auto-generated method stub
		return this.staticSeoRepository.findByUuidAndDeleteStatus(uuid,2);
	}
	
	public StaticSeo findByPageAndUuidNot(String page, String uuid) {
		// TODO Auto-generated method stub
		return this.staticSeoRepository.findByPageAndUuidNot(page,uuid);
	}
}
