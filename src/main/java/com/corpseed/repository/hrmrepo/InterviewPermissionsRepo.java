package com.corpseed.repository.hrmrepo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.corpseed.entity.hrmentity.InterviewPermissions;
import com.corpseed.entity.User;
import org.springframework.stereotype.Repository;

@Repository
public interface InterviewPermissionsRepo
		extends JpaRepository<InterviewPermissions, java.lang.Integer> {

	InterviewPermissions findByDepartmentAndUser(String department, User user);

	List<InterviewPermissions> findByUser(User saveUser);

	List<InterviewPermissions> findByDepartment(String dept);


}
