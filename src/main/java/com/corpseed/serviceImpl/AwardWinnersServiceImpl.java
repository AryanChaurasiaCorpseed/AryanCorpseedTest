package com.corpseed.serviceImpl;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.corpseed.entity.lifeentity.AwardWinners;
import com.corpseed.repository.AwardWinnersRepo;
import com.corpseed.service.AwardWinnersService;
@Service
public class AwardWinnersServiceImpl implements AwardWinnersService {

	@Autowired
	private AwardWinnersRepo awardWinners;
	
	@Override
	public List<AwardWinners> allAwardWinners() {
		// TODO Auto-generated method stub
		return this.awardWinners.findByDeleteStatus(2);
	}

	@Override
	public AwardWinners saveAward(@Valid AwardWinners awardWinner) {
		// TODO Auto-generated method stub
		return this.awardWinners.save(awardWinner);
	}

	@Override
	public AwardWinners findByUuid(String uuid) {
		// TODO Auto-generated method stub
		return this.awardWinners.findByUuid(uuid);
	}

	@Override
	public List<AwardWinners> findByDisplayStatusAndDeleteStatus(String dststus, int i) {
		// TODO Auto-generated method stub
		return this.awardWinners.findByDisplayStatusAndDeleteStatus(dststus,i);
	}

	@Override
	public AwardWinners findByIdAndStatus(long typeId, int i) {
		// TODO Auto-generated method stub
		return this.awardWinners.findByIdAndDeleteStatus(typeId,i);
	}

	@Override
	public void deleteAwardWinner(AwardWinners awardWinner) {
		this.awardWinners.delete(awardWinner);
	}

}
