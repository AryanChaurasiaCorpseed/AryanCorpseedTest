package com.corpseed.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.corpseed.entity.HotTags;
import com.corpseed.entity.serviceentity.Services;
import org.springframework.stereotype.Repository;

@Repository
public interface HotTagRepository extends JpaRepository<HotTags, Long> {

	HotTags findByUuidAndDeleteStatus(String uuid,int dStatus);

	HotTags findByName(String name);

	HotTags findByNameAndUuidNot(String name,String uuid);

	HotTags findByServices(Services service);

	HotTags findByServicesAndUuidNot(Services service,String uuid);

	List<HotTags> findTop4ByDisplayStatusAndDeleteStatus(String status,int dStatus);

	List<HotTags> findByDeleteStatus(int dStatus);

	HotTags findByIdAndDeleteStatus(long typeId, int i);

}
