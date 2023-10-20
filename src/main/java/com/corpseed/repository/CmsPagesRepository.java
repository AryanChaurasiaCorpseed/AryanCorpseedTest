package com.corpseed.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.corpseed.entity.CmsPages;
import org.springframework.stereotype.Repository;

@Repository
public interface CmsPagesRepository extends JpaRepository<CmsPages, Long> {

	CmsPages findByUuidAndDeleteStatus(String pageuuid,int dStatus);

	List<CmsPages> findByDisplayStatusAndDeleteStatus(String status,int dStatus);

	List<CmsPages> findTop6ByDeleteStatusOrderByIdDesc(int dStatus);

	List<CmsPages> findByDeleteStatusOrderByIdDesc(int dStatus);

	CmsPages findBySlugAndDeleteStatus(String slug,int dStatus);

	CmsPages findBySlugAndUuidNot(String slug,String uuid);

	CmsPages findByIdAndDeleteStatus(long typeId, int dStatus);

}
