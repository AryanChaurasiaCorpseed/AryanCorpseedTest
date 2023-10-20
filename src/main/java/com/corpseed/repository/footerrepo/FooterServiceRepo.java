package com.corpseed.repository.footerrepo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.corpseed.entity.footerentity.FooterCategory;
import com.corpseed.entity.footerentity.FooterService;
import com.corpseed.entity.serviceentity.Services;
import org.springframework.stereotype.Repository;

@Repository
public interface FooterServiceRepo extends JpaRepository<FooterService, Long> {

	FooterService findByFooterCategoryAndServicesAndDeleteStatus(FooterCategory saveFooter, Services service,int dStatus);

	List<FooterService> findByFooterCategoryAndDeleteStatus(FooterCategory saveFooter,int dStatus);

}
