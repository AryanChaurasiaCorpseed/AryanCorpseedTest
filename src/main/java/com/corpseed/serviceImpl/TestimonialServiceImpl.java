package com.corpseed.serviceImpl;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import com.corpseed.entity.serviceentity.Services;
import com.corpseed.entity.Testimonial;
import com.corpseed.repository.TestimonialRepo;
import com.corpseed.service.TestimonialService;

@Service
public class TestimonialServiceImpl implements TestimonialService {

	@Autowired
	private TestimonialRepo testmonialRepo;
	
	@Override
	public List<Testimonial> findByServicesAndDeleteStatus(Services service, int dStatus) {
		// TODO Auto-generated method stub
		return this.testmonialRepo.findByServicesAndDeleteStatus(service,dStatus);
	}

	@CacheEvict(value = "testimonials",allEntries = true)
	@Override
	public Testimonial saveTestimonial(@Valid Testimonial testimonial) {
		// TODO Auto-generated method stub
		return this.testmonialRepo.save(testimonial);
	}

	@Cacheable("testimonials")
	@Override
	public List<Testimonial> findTestimonialByServiceAndDeleteStatus(Services service, int i) {
		// TODO Auto-generated method stub
		return this.testmonialRepo.findByServicesAndDeleteStatus(service, i);
	}

	@Override
	public Testimonial findTestimonialById(long id) {
		// TODO Auto-generated method stub
		return this.testmonialRepo.findById(id);
	}

	@Override
	public void deleteTestimonial(Testimonial testimonial) {
		// TODO Auto-generated method stub
		this.testmonialRepo.delete(testimonial);
	}

}
