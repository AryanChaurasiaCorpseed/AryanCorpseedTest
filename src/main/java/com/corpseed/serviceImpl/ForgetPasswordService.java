package com.corpseed.serviceImpl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;

import com.corpseed.entity.ForgetPassword;
import com.corpseed.entity.User;
import com.corpseed.repository.ForgetPasswordRepository;

@Service
public class ForgetPasswordService {

	@Autowired
	private ForgetPasswordRepository forgetPasswordRepository;
	
	@Caching(evict = {
		    @CacheEvict(value = "forgetPasswordByUser", allEntries = true),
		    @CacheEvict(value = "forgetPassword", allEntries = true)		    
		})
	public void saveForgetPasswordLink(ForgetPassword forgetPassword) {
		this.forgetPasswordRepository.save(forgetPassword);
	}

	@Cacheable(value="forgetPassword",key="#user.id")
	public ForgetPassword findByUser(User user) {
		return this.forgetPasswordRepository.findByUserAndDeleteStatus(user,2);
	}

	@Caching(evict = {
		    @CacheEvict(value = "forgetPasswordByUser", allEntries = true),
		    @CacheEvict(value = "forgetPassword", allEntries = true)		    
		})
	public void deactivatePastLink(long id) {
		this.forgetPasswordRepository.deleteById(id);
	}

	@Cacheable(value = "forgetPasswordByUser",key="#user.id")
	public ForgetPassword findByUserAndDisplayStatus(User user, String status) {
		return this.forgetPasswordRepository.findByUserAndDisplayStatusAndDeleteStatus(user,status,2);
	}

	public List<ForgetPassword> findByPostDateBefore(String dateBeforeDays) {
		return this.forgetPasswordRepository.findByPostDateBeforeAndDeleteStatus(dateBeforeDays,2);
	}
	@Caching(evict = {
		    @CacheEvict(value = "forgetPasswordByUser", allEntries = true),
		    @CacheEvict(value = "forgetPassword", allEntries = true)		    
		})
	public void deleteAll(List<ForgetPassword> forgetPassList) {
		this.forgetPasswordRepository.deleteInBatch(forgetPassList);
	}
	@Caching(evict = {
		    @CacheEvict(value = "forgetPasswordByUser", allEntries = true),
		    @CacheEvict(value = "forgetPassword", allEntries = true)		    
		})
	public void saveForgetPasswordLinks(List<ForgetPassword> forgetPassword) {
		this.forgetPasswordRepository.saveAll(forgetPassword);
	}
}
