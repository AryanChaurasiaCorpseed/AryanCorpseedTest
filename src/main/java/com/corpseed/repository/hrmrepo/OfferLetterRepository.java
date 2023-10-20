package com.corpseed.repository.hrmrepo;

import org.springframework.data.jpa.repository.JpaRepository;

import com.corpseed.entity.hrmentity.OfferLetter;
import com.corpseed.entity.hrmentity.TrackApplication;
import org.springframework.stereotype.Repository;

@Repository
public interface OfferLetterRepository extends JpaRepository<OfferLetter, Long> {

	OfferLetter findByUuidAndDeleteStatus(String uuid,int dStatus);

	OfferLetter findByTrackApplicationAndDeleteStatus(TrackApplication trackApp,int dStatus);

}
