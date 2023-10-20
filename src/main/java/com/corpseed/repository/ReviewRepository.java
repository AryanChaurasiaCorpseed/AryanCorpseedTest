package com.corpseed.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.corpseed.entity.Review;
import org.springframework.stereotype.Repository;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {

	Review findByUuidAndDeleteStatus(String uuid,int dStatus);

	List<Review> findByShowHomeStatusAndDisplayStatusAndDeleteStatus(String status1, String status2
			,int dStatus);

	List<Review> findByDeleteStatusOrderByIdDesc(int dStatus);

	Review findByIdAndDeleteStatus(long typeId, int dStatus);

}
