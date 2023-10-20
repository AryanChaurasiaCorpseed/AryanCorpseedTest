package com.corpseed.repository.industryrepo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.corpseed.entity.industryentity.Industry;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface IndustryRepository extends JpaRepository<Industry, Long> {

	Industry findByUuidAndDeleteStatus(String uuid,int dStatus);

	List<Industry> findByDisplayStatusAndDeleteStatus(String status,int dStatus);

	Industry findBySlug(String slug);

	Industry findBySlugAndUuidNot(String slug,String uuid);

	List<Industry> findByDeleteStatusOrderByIdDesc(int dStatus);

	Industry findByIdAndDeleteStatus(long industryId,int dStatus);

	@Query(nativeQuery = true,value="SELECT * FROM Industry where " +
			"display_status= ?1 and delete_status= ?2 and " +
			"(industry_name like %?3% or meta_keyword like %?3% or meta_description like %?3% or " +
			"meta_title like %?3% or title like %?3% or slug like %?3% or summary like %?3%) order by visited desc limit 2")
	List<Industry> findTop1ByDisplayStatusAndDeleteStatusAndIndustryNameIgnoreCaseContaining(String displayStatus,int deleteStatus,String value);

	@Query(nativeQuery = true,value="SELECT * FROM Industry where " +
			"display_status= ?1 and delete_status= ?2 order by visited desc limit 1")
	Industry findTop1ByDisplayStatusAndDeleteStatusOrderByVisitedDesc(String status, int i);

}
