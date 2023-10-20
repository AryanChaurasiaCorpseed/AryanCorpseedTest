package com.corpseed.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.corpseed.entity.Subscribers;
import org.springframework.stereotype.Repository;

@Repository
public interface SubscriberRepository extends JpaRepository<Subscribers, Long> {

	Subscribers findByEmail(String email);

	Subscribers findByUuidAndDeleteStatus(String uuid,int dStatus);

	Subscribers findByEmailAndUuidNot(String email,String uuid);

	List<Subscribers> findByDisplayStatusAndDeleteStatus(String status,int dStatus);

	List<Subscribers> findByDeleteStatusOrderByIdDesc(int dStatus);

	Subscribers findByIdAndDeleteStatus(long typeId, int i);

}
