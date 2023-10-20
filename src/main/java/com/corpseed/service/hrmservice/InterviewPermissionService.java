package com.corpseed.service.hrmservice;

import java.util.List;

import com.corpseed.entity.hrmentity.InterviewPermissions;
import com.corpseed.entity.User;

public interface InterviewPermissionService {
	InterviewPermissions findByDepartmentAndUser(String department,User user);

	List<InterviewPermissions> saveAllInterviewDepartment(List<InterviewPermissions> newList);

	List<InterviewPermissions> findByUser(User saveUser);

	void deleteAll(List<InterviewPermissions> allIP);

	List<InterviewPermissions> findByDepartment(String dept);
}
