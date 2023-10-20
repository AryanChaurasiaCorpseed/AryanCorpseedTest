package com.corpseed.serviceImpl.enqserviceimpl;

import com.corpseed.entity.enquiryentity.EnquiryBlocker;
import com.corpseed.repository.enqrepo.EnquiryBlockerRepo;
import com.corpseed.service.EnquiryBlockerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class EnquiryBlockerServiceImpl implements EnquiryBlockerService {

    @Autowired
    private EnquiryBlockerRepo enquiryBlockerRepo;
    @Override
    public EnquiryBlocker findByMobileOrEmail(String mobile, String email) {
        return this.enquiryBlockerRepo.findByMobileContainingOrEmail(mobile,email);
    }
}
