package com.corpseed.service;

import com.corpseed.entity.OTP;

import java.util.List;

public interface OTPService {
    void saveOtp(OTP otp);

    OTP findByMobileContainingAndOtpCodeAndStatus(String mobile, String otp,boolean status);

    OTP findOtpByMobile(String mobile);

    boolean isValidOtp(String mobile, String otp);

    List<OTP> fetchMissedOtp(String today, String time);
}
