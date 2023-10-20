package com.corpseed.serviceImpl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import com.corpseed.entity.Feedback;
import com.corpseed.repository.FeedbackRepository;
import com.corpseed.service.FeedbackService;

@Service
public class FeedbackServiceImpl implements FeedbackService {

	@Autowired
	private FeedbackRepository feedbackRepository;
	
	@Override
	public List<Feedback> findAll() {
		// TODO Auto-generated method stub
		return this.feedbackRepository.findByOrderByIdDesc();
	}

	@Override
	public List<Feedback> findByType(String slug) {
		// TODO Auto-generated method stub
		return this.feedbackRepository.findByTypeOrderByIdDesc(slug);
	}

	@CacheEvict(value = "blogFeedbackByIp",allEntries = true)
	@Override
	public Feedback saveFeedback(Feedback feedback) {
		// TODO Auto-generated method stub
		return this.feedbackRepository.save(feedback);
	}

	@Cacheable(value = "blogFeedbackByIp")
	@Override
	public Feedback findByIpAndUrl(String ipAddress, String url) {
		// TODO Auto-generated method stub
		return this.feedbackRepository.findTop1ByIpAddressAndFeedbackUrl(ipAddress,url);
	}

	@Override
	public Feedback findById(long id) {
		// TODO Auto-generated method stub
		Optional<Feedback> findById = this.feedbackRepository.findById(id);
		if(findById.isPresent())
			return findById.get();
		else
			return null;
	}

	@CacheEvict(value = "blogFeedbackByIp",allEntries = true)
	@Override
	public void deleteFeedback(Feedback feedback) {
		// TODO Auto-generated method stub
		this.feedbackRepository.delete(feedback);
	}

}
