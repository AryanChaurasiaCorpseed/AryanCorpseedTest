package com.corpseed.repository.couponrepo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.corpseed.entity.couponentity.Coupon;
import org.springframework.stereotype.Repository;

@Repository
public interface CouponRepository extends JpaRepository<Coupon, Long> {

	Coupon findByTitle(String title);

	Coupon findByUuidAndDeleteStatus(String couponUUID,int dStatus);

	Coupon findByTitleAndUuidNot(String title,String uuid);

	List<Coupon> findByDeleteStatusOrderByIdDesc(int dStatus);

	Coupon findByTitleAndDisplayStatusAndDeleteStatus(String coupon,String string,int dStatus);

	Coupon findByTitleAndDisplayStatusAndStartDateBeforeAndEndDateAfterAndDeleteStatus(String coupon, String string, String today,
			String today2,int dStatus);

	List<Coupon> findByCouponPushStatusAndDeleteStatus(int i,int dStatus);

	Coupon findByIdAndDeleteStatus(long typeId, int dStatus);

}
