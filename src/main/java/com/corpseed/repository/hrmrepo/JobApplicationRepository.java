package com.corpseed.repository.hrmrepo;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.corpseed.entity.hrmentity.JobApplication;
import com.corpseed.entity.hrmentity.Jobs;
import org.springframework.stereotype.Repository;

@Repository
public interface JobApplicationRepository extends JpaRepository<JobApplication, Long> {

	JobApplication findByEmailAndJobsAndDeleteStatus(String email, Jobs jobs,int dStatus);

	JobApplication findByUuidAndDeleteStatus(String jobsAppUUID,int dStatus);

	JobApplication findByEmailAndJobsAndDeleteStatusAndUuidNot(String email, Jobs jobs,int dStatus
			, String uuid);


	List<JobApplication> findByApplicationStatusAndDeleteStatus(String status,int dStatus);

	List<JobApplication> findByEmailAndDisplayStatusAndDeleteStatus(String email, String status
			,int dStatus);

	List<JobApplication> findByjobsAndDeleteStatus(Jobs job,int dStatus);

	JobApplication findByEmailAndJobsAndPostDateAfter(String email, Jobs jobs, 
			String dateBeforeDays);

	JobApplication findByEmailAndJobsAndUuidNotAndPostDateAfter(String email, Jobs jobs, String uuid,
			String dateBeforeDays);

	JobApplication findTop1ByEmailAndDeleteStatusOrderByIdDesc(String email,int dStatus);

	List<JobApplication> findByEmailAndDeleteStatus(String email,int dStatus);

	JobApplication findByIdAndDeleteStatus(long typeId, int dStatus);

	Page<JobApplication> findByDeleteStatusAndApplicationStatus(int i, String astatus, Pageable pageable);
	
	List<JobApplication> findByDeleteStatusAndApplicationStatusOrderByIdDesc(int i, String astatus); 
	
	@Query("select ja from JobApplication ja, Jobs j WHERE ja.jobs=j and ja.deleteStatus=2 and ja.applicationStatus=3 and j.jobTitle IN (?1)")
	Page<JobApplication> findJobApplicationByApplicationStatusAndJobType(List<String> jobType, Pageable pageable);

	@Query("select ja from JobApplication ja, Jobs j WHERE ja.jobs=j and ja.deleteStatus=2 and ja.applicationStatus=?2 and j.jobTitle IN (?1) order by ja.id desc")
	List<JobApplication> findJobApplicationByApplicationStatusAndJobType(List<String> jobType,String appStatus);

	@Query(value="select j.* from job_application j, jobs jb WHERE j.job_id=jb.id and j.delete_status=2 and j.application_status=3 and (j.name like %:search% or j.email like %:search% or j.post_date like %:search% or jb.position like %:search%) order by j.id desc LIMIT 5",nativeQuery = true)
	List<JobApplication> findJobApplicationByApplicationStatusAndSearch(@Param("search") String search);

	@Query(value="select ja.* from job_application ja, jobs j WHERE ja.job_id=j.id and ja.delete_status=2 and ja.application_status=3 and j.job_title IN (:jobType) and (ja.name like %:search% or ja.email like %:search% or ja.post_date like %:search% or j.position like %:search%) order by ja.id desc LIMIT 5",nativeQuery = true)
	List<JobApplication> getJobApplicationByStatusAndJobTypeAndSearch(@Param("jobType") List<String> jobType,@Param("search") String search);

	@Query("select ja from JobApplication ja, Jobs j WHERE ja.jobs=j and  ja.deleteStatus=2 and ja.applicationStatus=3 and (ja.name like %:search% or ja.email like %:search% or ja.postDate like %:search% or j.position like %:search%) order by ja.id desc")
	Page<JobApplication> findJobAppByStatusAndSearch(@Param("search") String search, Pageable pageable);

	@Query("select ja from JobApplication ja, Jobs j WHERE ja.jobs=j and ja.deleteStatus=2 and ja.applicationStatus=3 and j.jobTitle IN (:jobType) and (ja.name like %:search% or ja.email like %:search% or ja.postDate like %:search% or j.position like %:search%) order by ja.id desc")
	Page<JobApplication> getJobAppByStatusAndJobTypeAndSearch(@Param("jobType") List<String> jobType,@Param("search") String search,
			Pageable pageable);
	

	
 
//	List<JobApplication> findByDeleteStatusAndApplicationStatusOrderByIdDesc(int i, String astatus);

}
