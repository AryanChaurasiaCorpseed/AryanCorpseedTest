package com.corpseed.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.corpseed.entity.SiteMapUrl;
import org.springframework.stereotype.Repository;

@Repository
public interface SiteMapRepository extends JpaRepository<SiteMapUrl, Long> {

	SiteMapUrl findByTypeAndUid(String type, long uid);

	List<SiteMapUrl> findByStatus(int status);

	void deleteByTypeAndUid(String type, long id);


}
