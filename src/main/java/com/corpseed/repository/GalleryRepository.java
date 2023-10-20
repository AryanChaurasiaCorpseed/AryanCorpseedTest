package com.corpseed.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.corpseed.entity.lifeentity.Gallery;
import org.springframework.stereotype.Repository;

@Repository
public interface GalleryRepository extends JpaRepository<Gallery, Long> {

	Gallery findByUuidAndDeleteStatus(String uuid,int dStatus);

	List<Gallery> findByDisplayStatusAndDeleteStatus(String status,int dStatus);

	List<Gallery> findByDeleteStatusOrderByIdDesc(int dStatus);

	Gallery findByIdAndDeleteStatus(long typeId, int i);

}
