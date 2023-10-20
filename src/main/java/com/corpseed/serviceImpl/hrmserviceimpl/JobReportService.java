package com.corpseed.serviceImpl.hrmserviceimpl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.corpseed.entity.hrmentity.JobReport;
import com.corpseed.entity.hrmentity.OfferLetter;
import com.corpseed.repository.hrmrepo.JobReportRepository;

@Service
public class JobReportService {

	@Autowired
	private JobReportRepository jobReportRepository;

	public List<JobReport> findAll() {
		return this.jobReportRepository.findByDeleteStatus(2);
	}

	public JobReport findByOfferLetter(OfferLetter saveOfferLetter) {
		return this.jobReportRepository.findByOfferLetterAndDeleteStatus(saveOfferLetter,2);
	}

	public JobReport saveReport(JobReport jobReport) {
		return this.jobReportRepository.save(jobReport);
	}
	
	
}
