package com.corpseed.repository.docrepo;

import org.springframework.data.jpa.repository.JpaRepository;

import com.corpseed.entity.documententity.DocumentVerification;
import com.corpseed.entity.hrmentity.TrackApplication;
import org.springframework.stereotype.Repository;

@Repository
public interface DocumentVerificationRepository extends JpaRepository<DocumentVerification, Long> {

	DocumentVerification findByUuidAndDeleteStatus(String uuid,int dStatus);

	DocumentVerification findByTrackApplicationAndDeleteStatus(TrackApplication trackApp,int dStatus);

}
