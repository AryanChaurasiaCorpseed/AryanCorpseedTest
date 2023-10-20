package com.corpseed.serviceImpl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import com.corpseed.entity.enquiryentity.Enquiry;
import com.corpseed.entity.Subscribers;
import com.corpseed.entity.TriggersList;
import com.corpseed.repository.TriggersRepository;

@Service
public class TriggerService {

	@Autowired
	private TriggersRepository triggersRepository;
	
	@Autowired
	private CommonServices commonService;

	@Autowired
	private Environment env;
	
	@Autowired
	private SubscriberService subscriberService;
	
	public List<TriggersList> getAllTriggers() {
		return this.triggersRepository.findByDeleteStatusOrderByIdDesc(2);
	}

	public void saveTriggers(List<Enquiry> enquiryList, String slug,String url1,String updateName) {
		List<TriggersList> triggerList=new ArrayList<>();
		String url=env.getProperty("domain.name")+url1+slug;
		for (Enquiry enqList : enquiryList) {
			triggerList.add(new TriggersList(0, this.commonService.getUUID(),enqList.getEmail(), url,updateName,this.commonService.getToday(), "1"));
		}
		List<Subscribers> subscribers=this.subscriberService.getAllSubscriberByStatus("1");
		for (Subscribers subscriber : subscribers) {
			triggerList.add(new TriggersList(0, this.commonService.getUUID(), subscriber.getEmail(), url, updateName, this.commonService.getToday(), "1"));
		}
		
		if(!triggerList.isEmpty()) {
			triggersRepository.saveAll(triggerList);
		}
	}

	public TriggersList findByUuid(String uuid) {
		return this.triggersRepository.findByUuidAndDeleteStatus(uuid,2);
	}

	public void deleteById(long id) {
		this.triggersRepository.deleteById(id);
	}

	public List<TriggersList> getTriggerByStatus(String status) {
		return this.triggersRepository.findByDisplayStatusAndDeleteStatus(status,2);
	}

	public void saveTrigger(TriggersList trigger) {
		this.triggersRepository.save(trigger);
	}

	public void updateAllTriggers(List<TriggersList> trgList) {
		this.triggersRepository.saveAll(trgList);
	}

	public void deleteByList(List<TriggersList> trgList) {
		this.triggersRepository.deleteInBatch(trgList);
	}

	public TriggersList findByIdAndDeleteStatus(long typeId, int i) {
		// TODO Auto-generated method stub
		return this.triggersRepository.findByIdAndDeleteStatus(typeId,i);
	}
}
