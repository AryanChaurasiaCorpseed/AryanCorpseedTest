package com.corpseed.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.corpseed.entity.TriggersList;
import org.springframework.stereotype.Repository;

@Repository
public interface TriggersRepository extends JpaRepository<TriggersList, Long> {

	TriggersList findByUuidAndDeleteStatus(String uuid,int dStatus);

	List<TriggersList> findByDisplayStatusAndDeleteStatus(String status,int dStatus);

	List<TriggersList> findByDeleteStatusOrderByIdDesc(int dStatus);

	TriggersList findByIdAndDeleteStatus(long typeId, int i);

}
