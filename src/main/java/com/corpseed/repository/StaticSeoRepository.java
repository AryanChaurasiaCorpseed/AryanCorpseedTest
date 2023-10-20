package com.corpseed.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.corpseed.entity.StaticSeo;
import org.springframework.stereotype.Repository;

@Repository
public interface StaticSeoRepository extends JpaRepository<StaticSeo, Long> {

	StaticSeo findByPage(String page);

	StaticSeo findByUuidAndDeleteStatus(String uuid,int dStatus);

	StaticSeo findByPageAndUuidNot(String page,String uuid);

	List<StaticSeo> findByDeleteStatus(int dStatus);

}
