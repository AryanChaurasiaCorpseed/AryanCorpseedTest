package com.corpseed.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.corpseed.entity.Position;
import com.corpseed.entity.User;
import org.springframework.stereotype.Repository;

@Repository
public interface PositionRepository extends JpaRepository<Position, Long> {

	Position findByUser(User user);

	Position findByUuidAndDeleteStatus(String uuid,int dStatus);

	Position findByUserAndUuidNot(User user,String uuid);

	List<Position> findByDisplayStatusAndDeleteStatus(String status,int dStatus);

	List<Position> findByDeleteStatusOrderByIdDesc(int dStatus);

	Position findByIdAndDeleteStatus(long typeId, int i);

}
