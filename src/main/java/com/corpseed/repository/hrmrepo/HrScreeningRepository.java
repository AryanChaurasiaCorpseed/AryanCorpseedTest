package com.corpseed.repository.hrmrepo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.corpseed.entity.hrmentity.HrScreening;
import com.corpseed.entity.hrmentity.TrackApplication;
import com.corpseed.entity.User;
import org.springframework.stereotype.Repository;

@Repository
public interface HrScreeningRepository extends JpaRepository<HrScreening, Long> {

	HrScreening findByUuidAndDeleteStatus(String uuid,int dStatus);

	HrScreening findByTrackApplicationAndDeleteStatus(TrackApplication trackApp,int dStatus);

	List<HrScreening> findByUserAndDeleteStatus(User loginedUser, int i);

	List<HrScreening> findByAddedByUuidAndPostDateAndHrEmailStatus(String addedByUuid,String today, int i);


}
