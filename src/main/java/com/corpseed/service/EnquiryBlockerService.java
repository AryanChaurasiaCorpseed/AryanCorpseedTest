package com.corpseed.service;

import com.corpseed.entity.enquiryentity.EnquiryBlocker;

public interface EnquiryBlockerService {
    EnquiryBlocker findByMobileOrEmail(String mobile, String email);
}
