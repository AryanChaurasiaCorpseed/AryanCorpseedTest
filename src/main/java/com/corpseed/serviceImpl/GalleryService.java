package com.corpseed.serviceImpl;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import com.corpseed.entity.lifeentity.Gallery;
import com.corpseed.repository.GalleryRepository;

@Service
public class GalleryService {

	@Autowired
	private GalleryRepository galleryRepository;

	public List<Gallery> getAllGallery() {
		return this.galleryRepository.findByDeleteStatusOrderByIdDesc(2);
	}

	public Gallery saveGallery(@Valid Gallery gallery) {
		return this.galleryRepository.save(gallery);
	}

	public Gallery getGalleryByUuid(String uuid) {
		return this.galleryRepository.findByUuidAndDeleteStatus(uuid,2);
	}

	@CacheEvict(value = "lifeAtGallary",allEntries = true)
	public void deleteGallery(long id) {
		this.galleryRepository.deleteById(id);
	}

	@Cacheable("lifeAtGallary")
	public List<Gallery> getAllGalleryByStatus(String status) {
		return this.galleryRepository.findByDisplayStatusAndDeleteStatus(status,2);
	}

	public Gallery findByIdAndDeleteStatus(long typeId, int i) {
		// TODO Auto-generated method stub
		return this.galleryRepository.findByIdAndDeleteStatus(typeId,i);
	}
}
