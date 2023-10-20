package com.corpseed.repository.hrmrepo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.corpseed.entity.hrmentity.HrPermissions;
import com.corpseed.entity.User;
import org.springframework.stereotype.Repository;

@Repository
public interface HrPermissionRepository extends JpaRepository<HrPermissions, Long> {

	HrPermissions findByUserAndJobTypeAndDeleteStatus(User saveUser, String jobType,int dStatus);

	List<HrPermissions> findByUserAndDeleteStatus(User saveUser,int dStatus);

}
