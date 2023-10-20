package com.corpseed.serviceImpl;

import com.corpseed.entity.Subscribers;
import com.corpseed.repository.SubscriberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import javax.validation.Valid;
import java.util.List;

@Service
public class SubscriberService {

	@Autowired
	private SubscriberRepository subscriberRepository;

	@CacheEvict(value = "subscriberByEmail",allEntries = true)
	public Subscribers addSubscriber(Subscribers subscribed) {
		return this.subscriberRepository.save(subscribed);
	}

	@Cacheable("subscriberByEmail")
	public Subscribers findByEmail(String email) {
		return this.subscriberRepository.findByEmail(email);
	}

	public List<Subscribers> getAllSubscriber() {
		return this.subscriberRepository.findByDeleteStatusOrderByIdDesc(2);
	}

	@CacheEvict(value = "subscriberByEmail",allEntries = true)
	public Subscribers saveSubscriber(@Valid Subscribers subscriber) {
		return this.subscriberRepository.save(subscriber);
	}

	public Subscribers getSubscriberByUuid(String uuid) {
		return this.subscriberRepository.findByUuidAndDeleteStatus(uuid,2);
	}

	public Subscribers findByEmailAndUuidNot(String email, String uuid) {
		return this.subscriberRepository.findByEmailAndUuidNot(email,uuid);
	}

	@CacheEvict(value = "subscriberByEmail",allEntries = true)
	public void deleteById(long id) {
		this.subscriberRepository.deleteById(id);
	}

	public List<Subscribers> getAllSubscriberByStatus(String status) {
		return this.subscriberRepository.findByDisplayStatusAndDeleteStatus(status,2);
	}

	public Subscribers findByIdAndDeleteStatus(long typeId, int i) {
		// TODO Auto-generated method stub
		return this.subscriberRepository.findByIdAndDeleteStatus(typeId,i);
	}

}
