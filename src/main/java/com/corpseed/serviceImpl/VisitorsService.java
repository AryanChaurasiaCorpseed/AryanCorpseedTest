package com.corpseed.serviceImpl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.corpseed.entity.Visitors;
import com.corpseed.repository.VisitorsRepository;

@Service
public class VisitorsService {

	@Autowired
	private VisitorsRepository visitorsRepository;
	
	public List<Visitors> findAll() {
		return this.visitorsRepository.findByDeleteStatus(2);
	}

	@CacheEvict(value = "findVisitorByDate", allEntries = true)		
	public Visitors saveVisitors(Visitors visitors) {
		return this.visitorsRepository.save(visitors);
	}

	@Cacheable("findVisitorByDate")
	public Visitors findByIpAddressAndBlogServiceUuidAndVisitedDate(String ipAddress, String uuid,
			String today) {
		return this.visitorsRepository.findByIpAddressAndBlogServiceUuidAndVisitedDateAndDeleteStatus(ipAddress,
				uuid, today,2);
	}

	public List<Visitors> findVisitorsByDate(String dateBeforeDays) {
		return this.visitorsRepository.findByVisitedDateAfterAndDeleteStatus(dateBeforeDays,2);
	}

	public List<Visitors> findVisitorsByFromToDate(String startDate, String lastDate) {
		return this.visitorsRepository.findByVisitedDateBetweenAndDeleteStatus(startDate,lastDate,2);
	}

	public Page<Visitors> getVisitorsByDeleteStatus(int dStatus, Pageable pageable) {
		// TODO Auto-generated method stub
		return this.visitorsRepository.findByDeleteStatus(dStatus, pageable);
	}
	
}
