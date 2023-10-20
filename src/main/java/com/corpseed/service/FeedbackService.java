package com.corpseed.service;

import java.util.List;

import com.corpseed.entity.Feedback;

public interface FeedbackService {

	List<Feedback> findAll();

	List<Feedback> findByType(String slug);

	Feedback saveFeedback(Feedback feedback);

	Feedback findByIpAndUrl(String ipAddress, String string);

	Feedback findById(long id);

	void deleteFeedback(Feedback feedback);

}
