package com.corpseed.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.corpseed.entity.Feedback;
import org.springframework.stereotype.Repository;

@Repository
public interface FeedbackRepository extends JpaRepository<Feedback, Long> {

	Feedback findTop1ByIpAddressAndFeedbackUrl(String ipAddress, String url);

	List<Feedback> findByOrderByIdDesc();

	List<Feedback> findByTypeOrderByIdDesc(String slug);

}
