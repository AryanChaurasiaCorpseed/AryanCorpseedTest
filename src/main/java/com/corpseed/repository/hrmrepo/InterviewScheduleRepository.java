package com.corpseed.repository.hrmrepo;

import org.springframework.data.jpa.repository.JpaRepository;

import com.corpseed.entity.hrmentity.InterviewSchedule;
import org.springframework.stereotype.Repository;

@Repository
public interface InterviewScheduleRepository extends JpaRepository<InterviewSchedule, Long> {

}
