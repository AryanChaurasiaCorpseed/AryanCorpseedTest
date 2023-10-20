package com.corpseed.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.corpseed.entity.Visitors;
import org.springframework.stereotype.Repository;

@Repository
public interface VisitorsRepository extends JpaRepository<Visitors, Long> {

	Visitors findByIpAddressAndBlogServiceUuidAndVisitedDateAndDeleteStatus(String ipAddress, String uuid,
			String today,int dStatus);

	List<Visitors> findByVisitedDateAfterAndDeleteStatus(String dateBeforeDays,int dStatus);

	List<Visitors> findByVisitedDateBetweenAndDeleteStatus(String startDate, String lastDate,int dStatus);

	List<Visitors> findByDeleteStatus(int dStatus);

	Page<Visitors> findByDeleteStatus(int dStatus, Pageable pageable);

}
