package com.corpseed.repository.enqrepo;

import com.corpseed.entity.enquiryentity.EnquiryBlocker;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EnquiryBlockerRepo extends JpaRepository<EnquiryBlocker,Long> {

    EnquiryBlocker findByMobileContainingOrEmail(String mobile, String email);
}
