package com.corpseed.serviceImpl.hrmserviceimpl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.corpseed.entity.hrmentity.InterviewSchedule;
import com.corpseed.repository.hrmrepo.InterviewScheduleRepository;

@Service
public class InterviewScheduleService {

	@Autowired
	private InterviewScheduleRepository interviewScheduleRepository;

	public InterviewSchedule scheduleInterview(InterviewSchedule interviewSchedule) {
		return this.interviewScheduleRepository.save(interviewSchedule);
	}

	public void saveScheduledInterview(List<InterviewSchedule> interviewSchedule) {
		this.interviewScheduleRepository.saveAll(interviewSchedule);
	}
	
	
}
