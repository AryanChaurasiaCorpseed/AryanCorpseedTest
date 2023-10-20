package com.corpseed.serviceImpl.hrmserviceimpl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.corpseed.entity.hrmentity.JobApplication;
import com.corpseed.entity.hrmentity.Jobs;
import com.corpseed.repository.hrmrepo.JobApplicationRepository;

@Service
public class JobApplicationServices {

	@Autowired
	private JobApplicationRepository jobApplicationRepository;

	public JobApplication isAlredayApplied(String email, Jobs jobs) {
		return this.jobApplicationRepository.findByEmailAndJobsAndDeleteStatus(email,jobs,2);
	}

	@CacheEvict(value = "findByEmailAndJobsAndPostDateAfter",allEntries = true)
	public JobApplication saveJobApplication(JobApplication jobApp) {
		return this.jobApplicationRepository.save(jobApp);
	}

	public Page<JobApplication> findJobApplicationByApplicationStatus(String astatus, Pageable pageable) {
		return this.jobApplicationRepository.findByDeleteStatusAndApplicationStatus(2,astatus,pageable);
	}

	public JobApplication getJobAppByUuid(String jobsAppUUID) {
		return this.jobApplicationRepository.findByUuidAndDeleteStatus(jobsAppUUID,2);
	}

	public JobApplication findByEmailAndJobsAndUuidNot(String email, Jobs jobs, String uuid) {
		return this.jobApplicationRepository.findByEmailAndJobsAndDeleteStatusAndUuidNot(email,jobs,2,uuid);
	}

	@CacheEvict(value = "findByEmailAndJobsAndPostDateAfter",allEntries = true)
	public void deleteJobApplication(long id) {
		this.jobApplicationRepository.deleteById(id);
	}

	public List<JobApplication> findByApplicationStatus(String status) {
		return this.jobApplicationRepository.findByApplicationStatusAndDeleteStatus(status,2);
	}

	public List<JobApplication> findByEmailAndDisplayStatus(String email, String status) {
		return this.jobApplicationRepository.findByEmailAndDisplayStatusAndDeleteStatus(email, status,2);
	}

	public List<JobApplication> findByjobs(Jobs job) {
		return this.jobApplicationRepository.findByjobsAndDeleteStatus(job,2);
	}

	@Cacheable("findByEmailAndJobsAndPostDateAfter")
	public JobApplication findByEmailAndJobsAndPostDateAfter(String email, Jobs jobs, String dateBeforeDays) {
		return this.jobApplicationRepository.findByEmailAndJobsAndPostDateAfter(email, 
				jobs, dateBeforeDays);
	}

	public JobApplication findByEmailAndJobsAndUuidNotAndPostDateAfter(String email, Jobs jobs, String uuid,
			String dateBeforeDays) {
		return this.jobApplicationRepository.findByEmailAndJobsAndUuidNotAndPostDateAfter(
				email, jobs, uuid, dateBeforeDays);
	}

	public JobApplication findTop1ByEmailOrderByIdDesc(String email) {
		return this.jobApplicationRepository.findTop1ByEmailAndDeleteStatusOrderByIdDesc(email,2);
	}

	public List<JobApplication> findByEmail(String email) {
		// TODO Auto-generated method stub
		return this.jobApplicationRepository.findByEmailAndDeleteStatus(email,2);
	}

	@CacheEvict(value = "findByEmailAndJobsAndPostDateAfter",allEntries = true)
	public void deleteAllApplication(List<JobApplication> jobApp) {	
		this.jobApplicationRepository.deleteInBatch(jobApp);
	}
	@CacheEvict(value = "findByEmailAndJobsAndPostDateAfter",allEntries = true)
	public void saveJobApplications(List<JobApplication> jobApplication) {
		this.jobApplicationRepository.saveAll(jobApplication);
	}

	public JobApplication findByIdAndDeleteStatus(long typeId, int dStatus) {
		// TODO Auto-generated method stub
		return this.jobApplicationRepository.findByIdAndDeleteStatus(typeId,dStatus);
	}

	public Page<JobApplication> getJobApplicationByApplicationStatusAndJobType(List<String> jobType,
			Pageable pageable) {
		// TODO Auto-generated method stub
		return this.jobApplicationRepository.findJobApplicationByApplicationStatusAndJobType(jobType,pageable);
	}

	public List<JobApplication> getJobApplicationByApplicationStatus(String astatus) {
		// TODO Auto-generated method stub
		return this.jobApplicationRepository.findByDeleteStatusAndApplicationStatusOrderByIdDesc(2,astatus);
	}

	public List<JobApplication> getJobApplicationByApplicationStatusAndJobType(List<String> jobType,String appStatus) {
		// TODO Auto-generated method stub
		return this.jobApplicationRepository.findJobApplicationByApplicationStatusAndJobType(jobType,appStatus);
	}

	public List<JobApplication> findJobApplicationByApplicationStatusAndBySearch(String search) {
		// TODO Auto-generated method stub
		return this.jobApplicationRepository.findJobApplicationByApplicationStatusAndSearch(search);
	}

	public List<JobApplication> getJobApplicationByStatusAndJobTypeAndSearch(List<String> jobType, String search) {
		// TODO Auto-generated method stub
		return this.jobApplicationRepository.getJobApplicationByStatusAndJobTypeAndSearch(jobType,search);
	}

	public Page<JobApplication> findJobAppByStatusAndSearch(String searchText, Pageable pageable) {
		// TODO Auto-generated method stub
		return this.jobApplicationRepository.findJobAppByStatusAndSearch(searchText,pageable);
	}

	public Page<JobApplication> getJobAppByStatusAndJobTypeAndSearch(List<String> jobType, String searchText,
			Pageable pageable) {
		// TODO Auto-generated method stub
		return this.jobApplicationRepository.getJobAppByStatusAndJobTypeAndSearch(jobType,searchText,pageable);
	}	
}
