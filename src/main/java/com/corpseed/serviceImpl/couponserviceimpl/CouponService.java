package com.corpseed.serviceImpl.couponserviceimpl;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import com.corpseed.entity.couponentity.Coupon;
import com.corpseed.repository.couponrepo.CouponRepository;

@Service
public class CouponService {

	@Autowired
	private CouponRepository couponRepository;

	public Coupon findByTitle(String title) {
		return this.couponRepository.findByTitle(title);
		
	}

	public List<Coupon> saveAll(List<Coupon> listCoupon) {
		return this.couponRepository.saveAll(listCoupon);	
	}

	public List<Coupon> getAllCoupons() {
		return this.couponRepository.findByDeleteStatusOrderByIdDesc(2);
	}

	public Coupon getCouponByUuid(String couponUUID) {
		return this.couponRepository.findByUuidAndDeleteStatus(couponUUID,2);
	}

	@CacheEvict(value = "couponByDate",allEntries = true)
	public void deleteCoupon(long id) {
		this.couponRepository.deleteById(id);
	}

	public Coupon findByTitleAndUuidNot(String title,String uuid) {
		return this.couponRepository.findByTitleAndUuidNot(title,uuid);
	}

	@CacheEvict(value = "couponByDate",allEntries = true)
	public Coupon saveCoupon(@Valid Coupon coupon) {
		return this.couponRepository.save(coupon);
	}

	public Coupon findByTitleAndDisplayStatus(String coupon,String string) {
		return this.couponRepository.findByTitleAndDisplayStatusAndDeleteStatus(coupon,string,2);
	}

	@Cacheable("couponByDate")
	public Coupon findByTitleAndDisplayStatusAndStartDateBeforeAndEndDateAfter(String coupon, String string,
			String today, String today2) {
		return this.couponRepository.findByTitleAndDisplayStatusAndStartDateBeforeAndEndDateAfterAndDeleteStatus(coupon, string, today, today2,2);
	}

	public List<Coupon> findByCouponPushStatus(int i) {
		return this.couponRepository.findByCouponPushStatusAndDeleteStatus(i,2);
	}

	public Coupon findByIdAndDeleteStatus(long typeId, int dStatus) {
		// TODO Auto-generated method stub
		return this.couponRepository.findByIdAndDeleteStatus(typeId,dStatus);
	}
	
}
