package com.corpseed.repository.hrmrepo;

import org.springframework.data.jpa.repository.JpaRepository;

import com.corpseed.entity.hrmentity.HrAndTechnical;
import com.corpseed.entity.hrmentity.TrackApplication;
import org.springframework.stereotype.Repository;

@Repository
public interface HrAndTechnicalRepository extends JpaRepository<HrAndTechnical, Long> {

	HrAndTechnical findByUuidAndDeleteStatus(String uuid,int dStatus);

	HrAndTechnical findByTrackApplicationAndDeleteStatus(TrackApplication trackApp,int dStatus);

}
