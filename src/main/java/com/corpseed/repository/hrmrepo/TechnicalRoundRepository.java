package com.corpseed.repository.hrmrepo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.corpseed.entity.hrmentity.TechnicalRound;
import com.corpseed.entity.hrmentity.TrackApplication;
import com.corpseed.entity.User;
import org.springframework.stereotype.Repository;

@Repository
public interface TechnicalRoundRepository extends JpaRepository<TechnicalRound, Long> {

	TechnicalRound findByUuidAndDeleteStatus(String uuid,int dStatus);

	TechnicalRound findByTrackApplicationAndDeleteStatus(TrackApplication trackApp,int dStatus);

	List<TechnicalRound> findByUserAndDeleteStatus(User loginedUser, int i);

	List<TechnicalRound> findByAddedByUuidAndPostDateAndHrEmailStatus(String addedByUuid,String today, int i);

}
