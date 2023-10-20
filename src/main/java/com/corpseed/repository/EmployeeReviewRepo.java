package com.corpseed.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.corpseed.entity.EmployeeReview;
import com.corpseed.entity.User;
import org.springframework.stereotype.Repository;

@Repository
public interface EmployeeReviewRepo extends JpaRepository<EmployeeReview, Long> {

	List<EmployeeReview> findByDeleteStatus(int i);

	EmployeeReview findByUser(User user);

	EmployeeReview findByUserAndUuidNot(User user, String uuid);

	EmployeeReview findByUuid(String uuid);

	List<EmployeeReview> findTop3ByDisplayStatusAndDeleteStatus(int i, int j);

	EmployeeReview findByIdAndDeleteStatus(long typeId, int i);

}
