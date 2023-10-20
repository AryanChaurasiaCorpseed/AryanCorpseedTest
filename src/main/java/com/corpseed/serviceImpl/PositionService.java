package com.corpseed.serviceImpl;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import com.corpseed.entity.Position;
import com.corpseed.entity.User;
import com.corpseed.repository.PositionRepository;

@Service
public class PositionService {
	
	@Autowired
	private PositionRepository positionRepository;

	public List<Position> getAllPositions() {
		return this.positionRepository.findByDeleteStatusOrderByIdDesc(2);
	}

	public long countAllPositions() {
		return this.positionRepository.count();
	}

	@CacheEvict(value = "lifeAtPosition",allEntries = true)
	public Position savePosition(@Valid Position position) {
		return this.positionRepository.save(position);
	}

	public Position findByUser(User user) {
		return this.positionRepository.findByUser(user);
	}

	public Position findByUuid(String uuid) {
		return this.positionRepository.findByUuidAndDeleteStatus(uuid,2);
	}

	@CacheEvict(value = "lifeAtPosition",allEntries = true)
	public void deletePosition(long id) {
		this.positionRepository.deleteById(id);
	}

	public Position findByUserAndUuidNot(User user, String uuid) {
		return this.positionRepository.findByUserAndUuidNot(user,uuid);
	}

	@Cacheable("lifeAtPosition")
	public List<Position> getAllPositionsByStatus(String status) {
		return this.positionRepository.findByDisplayStatusAndDeleteStatus(status,2);
	}

	public Position findByIdAndDeleteStatus(long typeId, int i) {
		// TODO Auto-generated method stub
		return this.positionRepository.findByIdAndDeleteStatus(typeId,i);
	}
	
	
}
