package com.corpseed.repository.footerrepo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.corpseed.entity.footerentity.FooterCategory;
import org.springframework.stereotype.Repository;

@Repository
public interface FooterRepository extends JpaRepository<FooterCategory, Long> {

	FooterCategory findByCategoryName(String categoryName);

	FooterCategory findByUuidAndDeleteStatus(String uuid,int dStatus);

	FooterCategory findByCategoryNameAndUuidNot(String categoryName,String uuid);

	List<FooterCategory> findByDisplayStatusAndDeleteStatus(String status,int dStatus);

	List<FooterCategory> findByDeleteStatus(int i);

	FooterCategory findByIdAndDeleteStatus(long typeId, int dStatus);

}
