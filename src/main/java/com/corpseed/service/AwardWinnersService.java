package com.corpseed.service;

import java.util.List;

import javax.validation.Valid;

import com.corpseed.entity.lifeentity.AwardWinners;

public interface AwardWinnersService {

	List<AwardWinners> allAwardWinners();

	AwardWinners saveAward(@Valid AwardWinners awardWinner);

	AwardWinners findByUuid(String uuid);

	List<AwardWinners> findByDisplayStatusAndDeleteStatus(String string, int i);

	AwardWinners findByIdAndStatus(long typeId, int i);

	void deleteAwardWinner(AwardWinners awardWinner);
}
