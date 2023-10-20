package com.corpseed.serviceImpl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.corpseed.entity.SiteMapUrl;
import com.corpseed.repository.SiteMapRepository;

@Service
public class SiteMapService {

	@Autowired
	private SiteMapRepository siteMapRepository;

	public void saveUrl(SiteMapUrl siteMapUrl) {
		this.siteMapRepository.save(siteMapUrl);
	}

	public SiteMapUrl findByTypeAndUid(String type, long uid) {
		// TODO Auto-generated method stub
		return this.siteMapRepository.findByTypeAndUid(type, uid);
	}

	public List<SiteMapUrl> getByStatus(int status) {
		// TODO Auto-generated method stub
		return this.siteMapRepository.findByStatus(status);
	}

	public void deleteUrl(String type,long id) {
		SiteMapUrl smu = this.siteMapRepository.findByTypeAndUid(type,id);
		if(smu!=null)
		this.siteMapRepository.deleteById(smu.getId());
	}

	public void saveSiteMap(List<SiteMapUrl> siteMap) {
		this.siteMapRepository.saveAll(siteMap);
	}

	public Optional<SiteMapUrl> findById(long l) {
		// TODO Auto-generated method stub
		return this.siteMapRepository.findById(l);
	}

	public void delete(SiteMapUrl siteMapUrl) {
		// TODO Auto-generated method stub
		this.siteMapRepository.delete(siteMapUrl);
	}
}
