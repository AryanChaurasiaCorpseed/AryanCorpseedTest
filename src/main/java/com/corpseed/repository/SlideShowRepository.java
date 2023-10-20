package com.corpseed.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.corpseed.entity.lifeentity.Slideshow;
import org.springframework.stereotype.Repository;

@Repository
public interface SlideShowRepository extends JpaRepository<Slideshow, Long> {

	Slideshow findByUuidAndDeleteStatus(String uuid,int dStatus);

	List<Slideshow> findByDisplayStatusAndDeleteStatus(String string,int dStatus);

	List<Slideshow> findByDeleteStatusOrderByIdDesc(int dStatus);

	Slideshow findByIdAndDeleteStatus(long typeId, int i);

}
