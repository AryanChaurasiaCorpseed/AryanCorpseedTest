package com.corpseed.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.corpseed.entity.serviceentity.Services;
import com.corpseed.entity.Testimonial;
import org.springframework.stereotype.Repository;

@Repository
public interface TestimonialRepo extends JpaRepository<Testimonial, Long> {

	List<Testimonial> findByServicesAndDeleteStatus(Services service, int dStatus);

	Testimonial findById(long id);

}
