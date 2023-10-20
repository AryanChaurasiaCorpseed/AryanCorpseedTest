package com.corpseed.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.corpseed.entity.LawUpdate;
import org.springframework.stereotype.Repository;

@Repository
public interface LawUpdateRepository extends JpaRepository<LawUpdate, Long> {

	LawUpdate findByUuidAndDeleteStatus(String lawuuid,int dStatus);

	LawUpdate findBySlugAndDeleteStatus(String slug,int dStatus);

	List<LawUpdate> findByDeleteStatusOrderByIdDesc(int dStatus);

	Page<LawUpdate> findByDisplayStatusAndDeleteStatus(String status,int dStatus
			, Pageable pageable);

	Page<LawUpdate> findByDepartmentAndPostDateBetweenAndDisplayStatusAndDeleteStatus(
			String department, String fromDate,String toDate, String status,int dStatus
			, Pageable pageable);

	LawUpdate findByIdAndDeleteStatus(long typeId, int i);

	Page<LawUpdate> findByDepartmentAndDisplayStatusAndDeleteStatusAndPostDateBetween(String department,String status,int dStatus,String fromDate, String toDate,
			Pageable pageable);

}
