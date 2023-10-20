package com.corpseed.serviceImpl.emailserviceimpl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.corpseed.entity.schedulerentity.EmailScheduler;
import com.corpseed.repository.EmailSchedulerRepo;
import com.corpseed.service.EmailSchedulerService;

@Service
public class EmailSchedulerImpl implements EmailSchedulerService {

	@Autowired
	private EmailSchedulerRepo emailSchedulerrepo;
	
	@Override
	public EmailScheduler saveEmail(EmailScheduler emailScheduler) {
		// TODO Auto-generated method stub
		return this.emailSchedulerrepo.save(emailScheduler);
	}

	@Override
	public void saveAllEmail(List<EmailScheduler> emailScheduler) {
		this.emailSchedulerrepo.saveAll(emailScheduler);
	}

	@Override
	public List<EmailScheduler> findByStatus(int i) {
		// TODO Auto-generated method stub
		return this.emailSchedulerrepo.findByStatus(i);
	}

	@Override
	public void cleanTrash(List<EmailScheduler> emailTrash) {
		this.emailSchedulerrepo.deleteInBatch(emailTrash);
	}

	@Override
	public void updateEmails(List<EmailScheduler> emailTrash) {
		this.emailSchedulerrepo.saveAll(emailTrash);
	}

}
