package com.corpseed.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.corpseed.entity.lifeentity.AwardWinners;
import org.springframework.stereotype.Repository;

@Repository
public interface AwardWinnersRepo extends JpaRepository<AwardWinners, Long> {

	List<AwardWinners> findByDeleteStatus(int i);

	AwardWinners findByUuid(String uuid);

	List<AwardWinners> findByDisplayStatusAndDeleteStatus(String dststus, int i);

	AwardWinners findByIdAndDeleteStatus(long typeId, int i);

}
