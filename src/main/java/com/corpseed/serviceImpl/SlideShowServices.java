package com.corpseed.serviceImpl;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import com.corpseed.entity.lifeentity.Slideshow;
import com.corpseed.repository.SlideShowRepository;

@Service
public class SlideShowServices {
	
	@Autowired
	private SlideShowRepository slideShowRepository;

	@CacheEvict(value = "lifeAtSlider",allEntries = true)
	public Slideshow saveSlide(@Valid Slideshow slideshow) {
		return this.slideShowRepository.save(slideshow);
	}

	public List<Slideshow> getAllSlideShow() {
		return this.slideShowRepository.findByDeleteStatusOrderByIdDesc(2);
	}

	public Slideshow getSlideShowByUuid(String uuid) {
		return this.slideShowRepository.findByUuidAndDeleteStatus(uuid,2);
	}

	@CacheEvict(value = "lifeAtSlider",allEntries = true)
	public void deleteSlideShow(long id) {
		this.slideShowRepository.deleteById(id);
	}

	@Cacheable("lifeAtSlider")
	public List<Slideshow> getSliderByStatus(String string) {
		return this.slideShowRepository.findByDisplayStatusAndDeleteStatus("1",2);
	}

	public Slideshow findByIdAndDeleteStatus(long typeId, int i) {
		// TODO Auto-generated method stub
		return this.slideShowRepository.findByIdAndDeleteStatus(typeId,i);
	}

	
}
