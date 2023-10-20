package com.corpseed.serviceImpl.hrmserviceimpl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.corpseed.entity.hrmentity.InterviewPermissions;
import com.corpseed.entity.User;
import com.corpseed.repository.hrmrepo.InterviewPermissionsRepo;
import com.corpseed.service.hrmservice.InterviewPermissionService;

@Service
public class InterviewPermissionServiceImpl implements InterviewPermissionService {

	@Autowired
	private InterviewPermissionsRepo interviewPermissionRepo;

	@Override
	public InterviewPermissions findByDepartmentAndUser(String department, User user) {
		// TODO Auto-generated method stub
		return this.interviewPermissionRepo.findByDepartmentAndUser(department,user);
	}

	@Override
	public List<InterviewPermissions> saveAllInterviewDepartment(List<InterviewPermissions> newList) {
		// TODO Auto-generated method stub
		return this.interviewPermissionRepo.saveAll(newList);
	}

	@Override
	public List<InterviewPermissions> findByUser(User saveUser) {
		// TODO Auto-generated method stub
		return this.interviewPermissionRepo.findByUser(saveUser);
	}

	@Override
	public void deleteAll(List<InterviewPermissions> allIP) {
		this.interviewPermissionRepo.deleteInBatch(allIP);
	}

	@Override
	public List<InterviewPermissions> findByDepartment(String dept) {
		// TODO Auto-generated method stub
		return this.interviewPermissionRepo.findByDepartment(dept);
	}
	
	

}
