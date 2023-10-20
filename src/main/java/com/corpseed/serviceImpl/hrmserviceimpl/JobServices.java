package com.corpseed.serviceImpl.hrmserviceimpl;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;

import com.corpseed.entity.hrmentity.Jobs;
import com.corpseed.repository.hrmrepo.JobsRepository;

@Service
public class JobServices {
	
	@Autowired
	private JobsRepository jobsRepository;

	@Caching(evict = {
		    @CacheEvict(value = "jobById", allEntries = true),
		    @CacheEvict(value = "jobBySlugAndStatus", allEntries = true)
		})
	public Jobs saveJobs(@Valid Jobs jobs) {
		return this.jobsRepository.save(jobs);
	}
	
	public List<Jobs> getAllJobs(){
		return this.jobsRepository.findByDisplayStatusAndDeleteStatusOrderByIdDesc("1",2);
	}

	@Cacheable(value = "jobById",key = "#jobId")
	public Jobs getJobById(String jobId) {
		return this.jobsRepository.getOne(Long.parseLong(jobId));
	}

	public Jobs getjobByUuid(String jobsUUID) {
		return this.jobsRepository.findByUuidAndDeleteStatus(jobsUUID,2);
	}

	public void deleteJob(long id) {
		this.jobsRepository.deleteById(id);
	}

	public List<Jobs> getJobByStatus(String status) {
		return this.jobsRepository.findByDisplayStatusAndDeleteStatus(status,2);
	}

	@Cacheable(value = "jobBySlugAndStatus",key = "#slug")
	public Jobs getJobBySlugAndStatus(String slug,String status) {
		return this.jobsRepository.findBySlugAndDisplayStatusAndDeleteStatus(slug,status,2);
	}

	public List<Jobs> findByJobTitle(String jobType) {
		return this.jobsRepository.findByJobTitleAndDeleteStatus(jobType,2);
	}

	public Jobs findJobByUuidAndStatus(String uuid, String status) {
		return this.jobsRepository.findByUuidAndDisplayStatusAndDeleteStatus(uuid,status,2);
	}

	public List<Jobs> findAllJobsByStatusNot(String status) {
		return this.jobsRepository.findByDisplayStatusNotAndDeleteStatus(status,2);
	}

	public Jobs findBySlugAndDisplayStatusNot(String jobSlug, String status) {
		return this.jobsRepository.findBySlugAndDisplayStatus(jobSlug,status);
	}

	public Jobs findBySlugAndUuidNotAndDisplayStatusNot(String title, String status, String uuid) {
		return this.jobsRepository.findBySlugAndUuidNotAndDisplayStatusNot(title, uuid,status);
	}

	public Jobs findByIdAndDeleteStatus(long typeId, int dStatus) {
		// TODO Auto-generated method stub
		return this.jobsRepository.findByIdAndDeleteStatus(typeId, dStatus);
	}

	public List<Jobs> findByJobTitleInAndDeleteStatus(List<String> jobType, int i) {
		// TODO Auto-generated method stub
		return this.jobsRepository.findByJobTitleInAndDeleteStatus(jobType, i);
	}

}
