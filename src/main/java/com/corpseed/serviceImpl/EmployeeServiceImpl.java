package com.corpseed.serviceImpl;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.corpseed.entity.EmployeeReview;
import com.corpseed.entity.User;
import com.corpseed.repository.EmployeeReviewRepo;
import com.corpseed.service.EmployeeReviewService;
@Service
public class EmployeeServiceImpl implements EmployeeReviewService {

	@Autowired
	private EmployeeReviewRepo empReviewRepo;
	
	@Override
	public List<EmployeeReview> allReview() {
		// TODO Auto-generated method stub
		return this.empReviewRepo.findByDeleteStatus(2);
	}

	@Override
	public void save(@Valid EmployeeReview review) {
		this.empReviewRepo.save(review);		
	}

	@Override
	public EmployeeReview findByUser(User user) {
		// TODO Auto-generated method stub
		return this.empReviewRepo.findByUser(user);
	}

	@Override
	public EmployeeReview findByUserAndUuidNot(User user,String uuid) {
		// TODO Auto-generated method stub
		return this.empReviewRepo.findByUserAndUuidNot(user,uuid);
	}

	@Override
	public EmployeeReview findByUuid(String uuid) {
		// TODO Auto-generated method stub
		return this.empReviewRepo.findByUuid(uuid);
	}

	@Override
	public List<EmployeeReview> findTop3ByDisplayStatusAndDeleteStatus(int i, int j) {
		// TODO Auto-generated method stub
		return this.empReviewRepo.findTop3ByDisplayStatusAndDeleteStatus(i,j);
	}

	@Override
	public EmployeeReview findByIdAndDeleteStatus(long typeId, int i) {
		// TODO Auto-generated method stub
		return this.empReviewRepo.findByIdAndDeleteStatus(typeId,i);
	}

	@Override
	public void deleteReview(EmployeeReview empReview) {
		// TODO Auto-generated method stub
		this.empReviewRepo.delete(empReview);
	}

}
