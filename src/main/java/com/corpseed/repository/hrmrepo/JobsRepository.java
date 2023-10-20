package com.corpseed.repository.hrmrepo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.corpseed.entity.hrmentity.Jobs;
import org.springframework.stereotype.Repository;

@Repository
public interface JobsRepository extends JpaRepository<Jobs, Long> {

	Jobs findByUuidAndDeleteStatus(String jobsUUID,int dStatus);

	List<Jobs> findByDisplayStatusAndDeleteStatus(String status,int dStatus);

	Jobs findBySlugAndDisplayStatusAndDeleteStatus(String slug, String status,int dStatus);

	List<Jobs> findByDisplayStatusAndDeleteStatusOrderByIdDesc(String status,int dStatus);

	List<Jobs> findByJobTitleAndDeleteStatus(String jobType,int dStatus);

	Jobs findByUuidAndDisplayStatusAndDeleteStatus(String uuid, String status,int dStatus);

	List<Jobs> findByDisplayStatusNotAndDeleteStatus(String status,int dStatus);

	Jobs findBySlugAndDisplayStatus(String slug, String status);

	Jobs findBySlugAndUuidNotAndDisplayStatusNot(String slug, String uuid, 
			String status);

	Jobs findByIdAndDeleteStatus(long typeId, int dStatus);

	List<Jobs> findByJobTitleInAndDeleteStatus(List<String> jobType, int i);

}
