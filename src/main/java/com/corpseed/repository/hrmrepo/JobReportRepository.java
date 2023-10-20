package com.corpseed.repository.hrmrepo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.corpseed.entity.hrmentity.JobReport;
import com.corpseed.entity.hrmentity.OfferLetter;
import org.springframework.stereotype.Repository;

@Repository
public interface JobReportRepository extends JpaRepository<JobReport, Long> {

	JobReport findByOfferLetterAndDeleteStatus(OfferLetter saveOfferLetter,int dStatus);

	List<JobReport> findByDeleteStatus(int sStatus);

}
