package com.corpseed.service;

import java.util.List;

import com.corpseed.entity.schedulerentity.EmailScheduler;

public interface EmailSchedulerService {

	public EmailScheduler saveEmail(EmailScheduler emailScheduler);

	public void saveAllEmail(List<EmailScheduler> emailScheduler);

	public List<EmailScheduler> findByStatus(int i);

	public void cleanTrash(List<EmailScheduler> emailTrash);

	public void updateEmails(List<EmailScheduler> emailTrash);
}
