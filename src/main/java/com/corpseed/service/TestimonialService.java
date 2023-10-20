package com.corpseed.service;

import java.util.List;

import javax.validation.Valid;

import com.corpseed.entity.serviceentity.Services;
import com.corpseed.entity.Testimonial;

public interface TestimonialService {

	List<Testimonial> findByServicesAndDeleteStatus(Services service, int dStatus);

	Testimonial saveTestimonial(@Valid Testimonial testimonial);

	List<Testimonial> findTestimonialByServiceAndDeleteStatus(Services service, int i);

	Testimonial findTestimonialById(long id);

	void deleteTestimonial(Testimonial testimonial);

}
