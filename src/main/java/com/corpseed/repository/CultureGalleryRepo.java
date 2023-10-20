package com.corpseed.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.corpseed.entity.lifeentity.CultureGallery;
import com.corpseed.entity.lifeentity.OurCulture;
import org.springframework.stereotype.Repository;

@Repository
public interface CultureGalleryRepo extends JpaRepository<CultureGallery, Long> {

	List<CultureGallery> findByDeleteStatus(int i);

	CultureGallery findByUuid(String uuid);

	CultureGallery findByIdAndDeleteStatus(long typeId, int i);

	List<CultureGallery> findByOurCultureAndDeleteStatus(OurCulture findByUuid,int dStatus);

}
