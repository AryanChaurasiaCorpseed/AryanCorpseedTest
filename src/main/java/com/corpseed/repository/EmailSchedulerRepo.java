package com.corpseed.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.corpseed.entity.schedulerentity.EmailScheduler;
import org.springframework.stereotype.Repository;

@Repository
public interface EmailSchedulerRepo extends JpaRepository<EmailScheduler, Long> {

	List<EmailScheduler> findByStatus(int i);

}
