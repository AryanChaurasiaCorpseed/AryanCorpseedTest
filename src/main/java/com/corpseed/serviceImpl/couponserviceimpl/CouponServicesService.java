package com.corpseed.serviceImpl.couponserviceimpl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import com.corpseed.entity.couponentity.Coupon;
import com.corpseed.entity.couponentity.CouponServices;
import com.corpseed.entity.serviceentity.Services;
import com.corpseed.repository.couponrepo.CouponServicesRepo;

@Service
public class CouponServicesService {

	@Autowired
	private CouponServicesRepo couponServicesRepo;

	@CacheEvict(value = "couponServiceByCouponAndService",allEntries = true)
	public void saveAllServices(List<CouponServices> couponServiceList) {
		this.couponServicesRepo.saveAll(couponServiceList);
	}

	@Cacheable("couponServiceByCouponAndService")
	public CouponServices findByCouponAndServices(Coupon findCoupon, Services services) {
		return this.couponServicesRepo.findByCouponAndServicesAndDeleteStatus(findCoupon, services,2);
	}

	public List<CouponServices> findByCoupon(Coupon saveCoupon) {
		return this.couponServicesRepo.findByCouponAndDeleteStatus(saveCoupon,2);
	}

	@CacheEvict(value = "couponServiceByCouponAndService",allEntries = true)
	public void deleteAll(List<CouponServices> allCouponServiceList) {
		this.couponServicesRepo.deleteInBatch(allCouponServiceList);
	}
}
