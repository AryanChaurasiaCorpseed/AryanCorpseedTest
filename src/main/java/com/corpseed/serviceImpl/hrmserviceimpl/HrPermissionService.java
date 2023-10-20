package com.corpseed.serviceImpl.hrmserviceimpl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.corpseed.entity.hrmentity.HrPermissions;
import com.corpseed.entity.User;
import com.corpseed.repository.hrmrepo.HrPermissionRepository;

@Service
public class HrPermissionService {

	@Autowired
	private HrPermissionRepository hrPermissionRepository;

	public List<HrPermissions> saveHrPermissions(List<HrPermissions> permissionList) {
		return this.hrPermissionRepository.saveAll(permissionList);
	}

	public void deleteHrPermission(long id) {
		this.hrPermissionRepository.deleteById(id);
	}

	public HrPermissions findByUserAndJobType(User saveUser, String jobType) {
		return this.hrPermissionRepository.findByUserAndJobTypeAndDeleteStatus(saveUser, jobType,2);
	}

	public List<HrPermissions> findByUser(User saveUser) {
		return this.hrPermissionRepository.findByUserAndDeleteStatus(saveUser,2);
	}

	public void deleteAlls(List<HrPermissions> allPermissionList) {
		this.hrPermissionRepository.deleteInBatch(allPermissionList);
	}

}
