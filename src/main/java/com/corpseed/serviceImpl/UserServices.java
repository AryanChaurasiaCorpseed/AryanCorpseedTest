package com.corpseed.serviceImpl;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;

import com.corpseed.entity.User;
import com.corpseed.repository.UserRepository;

@Service
public class UserServices {

	@Autowired
	private UserRepository userRepository;
	
	public User isUserExist(String email) {
		return this.userRepository.findByEmail(email);
	}

	@Caching(evict = {
		    @CacheEvict(value = "getUserByUuid", allEntries = true),
		    @CacheEvict(value = "userByEmailAndStatus", allEntries = true),
		    @CacheEvict(value = "userByEmailAndStatus", allEntries = true),
		    @CacheEvict(value = "lifeAtPosition", allEntries = true),
		    @CacheEvict(value = "awardWinners", allEntries = true),
			@CacheEvict(value = "serviceContact",allEntries = true),
			@CacheEvict(value = "findByUserSlug",allEntries = true)
		})
	public User saveUser(@Valid User user) {		
		return userRepository.save(user);
	}

	public List<User> getAllUsers() {
		return this.userRepository.findByDeleteStatusOrderByIdDesc(2);
	}

	@Cacheable(value = "getUserByUuid",key = "#useruuid")
	public User getUserByUuid(String useruuid) {
		return this.userRepository.findByUuidAndDeleteStatus(useruuid,2);
	}

	public User findByEmailAndUuidNot(String email, String useruuid) {
		return this.userRepository.findByEmailAndUuidNot(email,useruuid);
	}
	
	@Caching(evict = {
			@CacheEvict(value = "getUserByUuid", allEntries = true),
		    @CacheEvict(value = "userByEmailAndStatus", allEntries = true),
		    @CacheEvict(value = "userByEmailAndStatus", allEntries = true),
		    @CacheEvict(value = "lifeAtPosition", allEntries = true),
		    @CacheEvict(value = "awardWinners", allEntries = true),
			@CacheEvict(value = "serviceContact",allEntries = true),
			@CacheEvict(value = "findByUserSlug",allEntries = true)
		})
	public void deleteUser(long id) {
		this.userRepository.deleteById(id);
	}

	public long countAllUsers() {
		return this.userRepository.count();
	}

	public List<User> findByRoleNotIn(List<String> roles) {
		return this.userRepository.findByRoleNotInAndDeleteStatus(roles,2);
	}

	public User findByEmailAndRole(String email, String role) {
		return this.userRepository.findByEmailAndRoleAndDeleteStatus(email, role,2);
	}

	@Cacheable("userByEmailAndStatus")
	public User findByEmailAndDisplayStatus(String email, String status) {
		return this.userRepository.findByEmailAndDisplayStatusAndDeleteStatus(email,status,2);
	}

	public List<User> findByDisplayStatus(String status) {
		// TODO Auto-generated method stub
		return this.userRepository.findByDisplayStatusAndDeleteStatus(status,2);
	}
	public List<User> findAllNotUserAndAccountStatus() {
		// TODO Auto-generated method stub
		return this.userRepository.findByRoleNotAndDeleteStatusAndAccountStatus("ROLE_USER",2,1);
	}
	public List<User> findAllNotUser() {
		// TODO Auto-generated method stub
		return this.userRepository.findByRoleNotAndDeleteStatus("ROLE_USER",2);
	}

	public List<User> findByRole(String role) {
		// TODO Auto-generated method stub
		return this.userRepository.findByRoleAndDeleteStatus(role,2);
	}

	public User findByIdAndDeleteStatus(long typeId, int i) {
		// TODO Auto-generated method stub
		return this.userRepository.findByIdAndDeleteStatus(typeId,i);
	}

	public List<User> findByRoleNotInAndDepartmentAndDeleteStatus(List<String> roles, String dept, int dStatus) {
		// TODO Auto-generated method stub
		return this.userRepository.findByRoleNotInAndDepartmentAndDeleteStatus(roles,dept,dStatus);
	}

	public User findByEmailAndRoleNot(String email, String role) {
		// TODO Auto-generated method stub
		return this.userRepository.findByEmailAndRoleNot(email, role);
	}

	public User findByEmailAndDeleteStatus(String email, int i) {
		// TODO Auto-generated method stub
		return this.userRepository.findByEmailAndDeleteStatus(email,i);
	}

    public List<User> findUserByAccountStatusAndDeleteStatusAndDisplayStatusAndRoleNot(int accountStatus, int deleteStatus, String displayStatus,String role_user) {
		return this.userRepository.findUserByAccountStatusAndDeleteStatusAndDisplayStatusAndRoleNot(accountStatus, deleteStatus, displayStatus,role_user);
    }

	public User findBySlugRoleNot(String slug, String role_user) {
		return this.userRepository.findBySlugAndRoleNot(slug,role_user);
	}

	public User findBySlugRoleNotAndIdNot(String slug, String role_user, long id) {
		return this.userRepository.findBySlugAndRoleNotAndIdNot(slug,role_user,id);
	}

	@Cacheable(value = "findByUserSlug")
	public User findUserBySlug(String userSlug) {
		return this.userRepository.findBySlug(userSlug);
	}
}
