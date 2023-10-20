package com.corpseed.serviceImpl;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import com.corpseed.entity.Meeting;
import com.corpseed.repository.MeetingRepository;

@Service
public class MeetingService {

	@Autowired
	private MeetingRepository meetingRepository;

	@CacheEvict(value = "findByEmailOrMobile",allEntries = true)
	public Meeting bookMeeting(@Valid Meeting meeting) {
		return this.meetingRepository.save(meeting);
	}

	public List<Meeting> getAllMettings() {
		return this.meetingRepository.findByDeleteStatusOrderByIdDesc(2);
	}

	public Meeting getMeetingByUuid(String uuid) {
		return this.meetingRepository.findByUuidAndDeleteStatus(uuid,2);
	}

	@CacheEvict(value = "findByEmailOrMobile",allEntries = true)
	public void deleteMeeting(long id) {
		this.meetingRepository.deleteById(id);
	}

	@Cacheable(value = "findByEmailOrMobile")
	public int findByEmailOrMobile(String email, String mobile) {
		// TODO Auto-generated method stub
		return this.meetingRepository.findByEmailOrMobile(email,mobile);
	}
	
	@CacheEvict(value = "findByEmailOrMobile",allEntries = true)
	public void saveMeeting(Meeting meeting) {
		this.meetingRepository.save(meeting);
	}

	public Meeting findByIdAndDeleteStatus(long typeId, int dStatus) {
		// TODO Auto-generated method stub
		return this.meetingRepository.findByIdAndDeleteStatus(typeId,dStatus);
	}
	
	
}
