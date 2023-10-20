package com.corpseed.repository;

import com.corpseed.entity.OTP;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OtpRepository extends JpaRepository<OTP,Long> {

    OTP findByMobile(String mobile);

    OTP findTop1ByMobileContainingAndOtpCodeAndStatus(String mobile, String otp,boolean status);

    @Query("select o from OTP o where o.status= true and o.deliveryStatus= 2 and SUBSTRING(o.dateTime, 1, 10)= :today and SUBSTRING(o.dateTime, 12)< :time")
    List<OTP> fetchMissedOtp(String today, String time);
}
