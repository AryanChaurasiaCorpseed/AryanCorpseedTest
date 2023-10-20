package com.corpseed.serviceImpl;

import com.corpseed.entity.OTP;
import com.corpseed.repository.OtpRepository;
import com.corpseed.service.OTPService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OTPServiceImpl implements OTPService {

    @Autowired
    private OtpRepository otpRepository;

    @Override
    public void saveOtp(OTP otp) {
        this.otpRepository.save(otp);
    }

    @Override
    public OTP findByMobileContainingAndOtpCodeAndStatus(String mobile, String otp,boolean status) {
        if(mobile.length()>10)mobile=mobile.substring(mobile.length()-10);
        return this.otpRepository.findTop1ByMobileContainingAndOtpCodeAndStatus(mobile,otp,status);
    }

    @Override
    public OTP findOtpByMobile(String mobile) {
        return this.otpRepository.findByMobile(mobile);
    }

    @Override
    public boolean isValidOtp(String mobile, String otp) {
    	if(mobile.length()>10)mobile=mobile.substring(mobile.length()-10);
        OTP otpResult = this.otpRepository.findTop1ByMobileContainingAndOtpCodeAndStatus(mobile, otp,true);
        if(otpResult==null)
            return false;

        otpResult.setDeliveryStatus(1);
        otpResult.setStatus(false);
        this.otpRepository.save(otpResult);
        return true;
    }

    @Override
    public List<OTP> fetchMissedOtp(String today, String time) {
        return this.otpRepository.fetchMissedOtp(today,time);
    }
}
