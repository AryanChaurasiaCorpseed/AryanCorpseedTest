package com.corpseed.service;

import java.util.List;

import javax.validation.Valid;

import com.corpseed.entity.EmployeeReview;
import com.corpseed.entity.User;

public interface EmployeeReviewService {

	List<EmployeeReview> allReview();

	void save(@Valid EmployeeReview review);

	EmployeeReview findByUser(User user);

	EmployeeReview findByUserAndUuidNot(User user,String uuid);

	EmployeeReview findByUuid(String uuid);

	List<EmployeeReview> findTop3ByDisplayStatusAndDeleteStatus(int i, int j);

	EmployeeReview findByIdAndDeleteStatus(long typeId, int i);

	void deleteReview(EmployeeReview empReview);
}
