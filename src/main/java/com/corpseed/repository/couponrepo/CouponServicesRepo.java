package com.corpseed.repository.couponrepo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.corpseed.entity.couponentity.Coupon;
import com.corpseed.entity.couponentity.CouponServices;
import com.corpseed.entity.serviceentity.Services;
import org.springframework.stereotype.Repository;

@Repository
public interface CouponServicesRepo extends JpaRepository<CouponServices, Long> {

	CouponServices findByCouponAndServicesAndDeleteStatus(Coupon findCoupon, Services services,int dStatus);

	List<CouponServices> findByCouponAndDeleteStatus(Coupon saveCoupon,int dStatus);

}
