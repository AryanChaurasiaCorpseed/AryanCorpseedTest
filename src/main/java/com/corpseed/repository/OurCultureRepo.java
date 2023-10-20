package com.corpseed.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.corpseed.entity.lifeentity.OurCulture;
import org.springframework.stereotype.Repository;

@Repository
public interface OurCultureRepo extends JpaRepository<OurCulture, Long> {

	List<OurCulture> findByDeleteStatus(int i);

	OurCulture findByTitle(String title);

	OurCulture findByUuid(String uuid);

	OurCulture findByTitleAndUuidNot(String title, String uuid);

	OurCulture findByDisplayStatusAndDeleteStatusAndTitle(int i, int j, String title);

	List<OurCulture> findByDisplayStatusAndDeleteStatus(int i, int j);

	OurCulture findByIdAndDeleteStatus(long typeId, int i);

}
