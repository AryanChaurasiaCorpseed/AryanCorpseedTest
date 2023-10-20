package com.corpseed.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.corpseed.entity.History;
import org.springframework.stereotype.Repository;

@Repository
public interface HistoryRepository extends JpaRepository<History, Long> {

	List<History> findByOrderByIdDesc();

	History findByUuid(String uuid);

	List<History> findByDateBefore(String today);

}
