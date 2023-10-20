package com.corpseed.serviceImpl.hrmserviceimpl;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.corpseed.entity.documententity.DocumentVerification;
import com.corpseed.entity.hrmentity.HrAndTechnical;
import com.corpseed.entity.hrmentity.HrScreening;
import com.corpseed.entity.hrmentity.JobApplication;
import com.corpseed.entity.hrmentity.OfferLetter;
import com.corpseed.entity.hrmentity.TechnicalRound;
import com.corpseed.entity.hrmentity.TrackApplication;
import com.corpseed.entity.User;
import com.corpseed.repository.docrepo.DocumentVerificationRepository;
import com.corpseed.repository.hrmrepo.HrAndTechnicalRepository;
import com.corpseed.repository.hrmrepo.HrScreeningRepository;
import com.corpseed.repository.hrmrepo.OfferLetterRepository;
import com.corpseed.repository.hrmrepo.TechnicalRoundRepository;
import com.corpseed.repository.hrmrepo.TrackAppRepository;

@Service
public class TrackAppService {

	@Autowired
	private TrackAppRepository trackAppRepository;
	
	@Autowired
	private HrScreeningRepository hrScreeningRepository;
	
	@Autowired
	private HrAndTechnicalRepository hrAndTechnicalRepository;
	
	@Autowired
	private DocumentVerificationRepository documentVerificationRepository;
	
	@Autowired
	private TechnicalRoundRepository technicalRoundRepository;
	
	@Autowired
	private OfferLetterRepository offerLetterRepository;

	public TrackApplication findByJobApplication(JobApplication jobApp) {
		return this.trackAppRepository.findByJobApplicationAndDeleteStatus(jobApp,2);
	}

	public void saveTrackApplication(TrackApplication trackApp) {
		this.trackAppRepository.save(trackApp);
	}
	public TrackApplication findByUuid(String uuid) {
		return this.trackAppRepository.findByUuidAndDeleteStatus(uuid,2);
	}

	public HrScreening saveHrScreening(@Valid HrScreening hrScreening) {
		return this.hrScreeningRepository.save(hrScreening);
	}

	public HrScreening findHrScreeningByTrackApplication(TrackApplication trackApp) {
		return this.hrScreeningRepository.findByTrackApplicationAndDeleteStatus(trackApp,2);
	}

	public HrAndTechnical findHrTechnicalByTrackApplication(TrackApplication trackApp) {
		return this.hrAndTechnicalRepository.findByTrackApplicationAndDeleteStatus(trackApp,2);
	}

	public TechnicalRound findTechnicalRoundByTrackApplication(TrackApplication trackApp) {
		return this.technicalRoundRepository.findByTrackApplicationAndDeleteStatus(trackApp,2);
	}

	public DocumentVerification findDocumentVerificationByTrackApplication(TrackApplication trackApp) {
		return this.documentVerificationRepository.findByTrackApplicationAndDeleteStatus(trackApp,2);
	}

	public OfferLetter findOfferLetterByTrackApplication(TrackApplication trackApp) {
		return this.offerLetterRepository.findByTrackApplicationAndDeleteStatus(trackApp,2);
	}

	public HrAndTechnical saveHrTechnical(@Valid HrAndTechnical hrTechnical) {
		return this.hrAndTechnicalRepository.save(hrTechnical);
	}

	public TechnicalRound saveTechnicalRound(@Valid TechnicalRound technicalRound) {
		return this.technicalRoundRepository.save(technicalRound);
	}

	public DocumentVerification saveDocumentVerification(@Valid DocumentVerification docVerification) {
		return this.documentVerificationRepository.save(docVerification);
	}

	public OfferLetter saveOfferLetter(@Valid OfferLetter offerLetter) {
		return this.offerLetterRepository.save(offerLetter);
	}

	public Page<TrackApplication> findByStatusOrderByIdDesc(String status,Pageable pageable) {
		return this.trackAppRepository.findByStatusAndDeleteStatus(status,2,pageable);
	}

	public Page<TrackApplication> findByAddedByUuidAndStatus(String uuid, String status,Pageable pageable) {
		return this.trackAppRepository.findByAddedByUuidAndStatusAndDeleteStatus(uuid, status,2,pageable);
	}

	public Page<TrackApplication> findByAddedByUuidOrderByIdDesc(String uuid,Pageable pageable) {
		return this.trackAppRepository.findByAddedByUuidAndDeleteStatusAndStatusNotOrderByIdDesc(uuid,2,"3",pageable);
	}

	public Page<TrackApplication> findByOrderByIdDesc(Pageable pageable) {
		return this.trackAppRepository.findByDeleteStatusAndStatusNotOrderByIdDesc(2,"3",pageable);
	}

	public HrScreening findHrScreeningByUuid(String uuid) {
		// TODO Auto-generated method stub
		return this.hrScreeningRepository.findByUuidAndDeleteStatus(uuid,2);
	}
	
	public void saveTrackApplications(List<TrackApplication> trackApp) {
		this.trackAppRepository.saveAll(trackApp);
	}

	public List<TechnicalRound> findByUserAndDeleteStatus(User loginedUser, int i) {
		// TODO Auto-generated method stub
		return this.technicalRoundRepository.findByUserAndDeleteStatus(loginedUser,i);
	}

	public List<HrScreening> findBusinessRoundByUserAndDeleteStatus(User loginedUser, int i) {
		// TODO Auto-generated method stub
		return this.hrScreeningRepository.findByUserAndDeleteStatus(loginedUser,i);
	}

	public List<TrackApplication> findAppByUserAndDeleteStatus(User loginedUser, int i) {
		// TODO Auto-generated method stub
		return this.trackAppRepository.findByUserAndDeleteStatus(loginedUser,i);
	}

	public Page<TrackApplication> findByStatusAndDepartmentAndRoundOne(String department, String status,Pageable pageable) {
		// TODO Auto-generated method stub
		return this.trackAppRepository.findByStatusAndDepartmentAndRoundOne(department,status,pageable);
	}

	public Page<TrackApplication> findByStatusAndDepartmentAndRoundSecond(String department, String status,
			Pageable pageable) {
		// TODO Auto-generated method stub
		return this.trackAppRepository.findByStatusAndDepartmentAndRoundSecond(department,status,pageable);
	}

	public Page<TrackApplication> findByStatusAndDepartmentAndRoundThird(String department, String status,
			Pageable pageable) {
		// TODO Auto-generated method stub
		return this.trackAppRepository.findByStatusAndDepartmentAndRoundThird(department,status,pageable);
	}

	public Page<TrackApplication> findByStatusAndDepartmentAndRoundFourth(String department, String status,
			Pageable pageable) {
		// TODO Auto-generated method stub
		return this.trackAppRepository.findByStatusAndDepartmentAndRoundFourth(department,status,pageable);
	}

	public Page<TrackApplication> findByStatusAndRoundOne(String status, Pageable pageable) {
		// TODO Auto-generated method stub
		return this.trackAppRepository.findByStatusAndRoundOne(status, pageable);
	}

	public Page<TrackApplication> findByStatusAndRoundSecond(String status, Pageable pageable) {
		// TODO Auto-generated method stub
		return this.trackAppRepository.findByStatusAndRoundSecond(status, pageable);
	}

	public Page<TrackApplication> findByStatusAndRoundThird(String status, Pageable pageable) {
		// TODO Auto-generated method stub
		return this.trackAppRepository.findByStatusAndRoundThird(status, pageable);
	}

	public Page<TrackApplication> findByStatusAndRoundFourth(String status, Pageable pageable) {
		// TODO Auto-generated method stub
		return this.trackAppRepository.findByStatusAndRoundFourth(status, pageable);
	}

	public Page<TrackApplication> findByStatusAndDepartment(String department, String status, Pageable pageable) {
		// TODO Auto-generated method stub
		return this.trackAppRepository.findByStatusAndDepartment(department, status, pageable);
	}

	public Page<TrackApplication> findByStatusAndDepartmentAndRoundOne(String department, String status,String uuid,Pageable pageable) {
		// TODO Auto-generated method stub
		return this.trackAppRepository.findByStatusAndDepartmentAndRoundOne(department,status,uuid,pageable);
	}

	public Page<TrackApplication> findByStatusAndDepartmentAndRoundSecond(String department, String status,String uuid,
			Pageable pageable) {
		// TODO Auto-generated method stub
		return this.trackAppRepository.findByStatusAndDepartmentAndRoundSecond(department,status,uuid,pageable);
	}

	public Page<TrackApplication> findByStatusAndDepartmentAndRoundThird(String department, String status,String uuid,
			Pageable pageable) {
		// TODO Auto-generated method stub
		return this.trackAppRepository.findByStatusAndDepartmentAndRoundThird(department,status,uuid,pageable);
	}

	public Page<TrackApplication> findByStatusAndDepartmentAndRoundFourth(String department, String status,String uuid,
			Pageable pageable) {
		// TODO Auto-generated method stub
		return this.trackAppRepository.findByStatusAndDepartmentAndRoundFourth(department,status,uuid,pageable);
	}

	public Page<TrackApplication> findByStatusAndRoundOne(String status,String uuid, Pageable pageable) {
		// TODO Auto-generated method stub
		return this.trackAppRepository.findByStatusAndRoundOne(status,uuid, pageable);
	}

	public Page<TrackApplication> findByStatusAndRoundSecond(String status,String uuid, Pageable pageable) {
		// TODO Auto-generated method stub
		return this.trackAppRepository.findByStatusAndRoundSecond(status,uuid, pageable);
	}

	public Page<TrackApplication> findByStatusAndRoundThird(String status,String uuid, Pageable pageable) {
		// TODO Auto-generated method stub
		return this.trackAppRepository.findByStatusAndRoundThird(status,uuid, pageable);
	}

	public Page<TrackApplication> findByStatusAndRoundFourth(String status,String uuid, Pageable pageable) {
		// TODO Auto-generated method stub
		return this.trackAppRepository.findByStatusAndRoundFourth(status,uuid, pageable);
	}

	public Page<TrackApplication> findByStatusAndDepartment(String department, String status,String uuid, Pageable pageable) {
		// TODO Auto-generated method stub
		return this.trackAppRepository.findByStatusAndDepartment(department, status,uuid, pageable);
	}

	public List<TrackApplication> findTrackApplicationByUser(User loginedUser) {
		// TODO Auto-generated method stub
		return this.trackAppRepository.findTrackApplicationByUser(loginedUser);
	}
	public List<TrackApplication> findSelectedTrackApplicationByUser(User loginedUser) {
		// TODO Auto-generated method stub
		return this.trackAppRepository.findSelectedTrackApplicationByUser(loginedUser);
	}
	public List<TrackApplication> findHoldTrackApplicationByUser(User loginedUser) {
		// TODO Auto-generated method stub
		return this.trackAppRepository.findHoldTrackApplicationByUser(loginedUser);
	}
	public List<TrackApplication> findRejectedTrackApplicationByUser(User loginedUser) {
		// TODO Auto-generated method stub
		return this.trackAppRepository.findRejectedTrackApplicationByUser(loginedUser);
	}

	public List<TrackApplication> findByStatusAndDepartmentAndRoundOne(String department, String status, User user) {
		// TODO Auto-generated method stub
		return this.trackAppRepository.findByStatusAndDepartmentAndRoundOne(department,status,user);
	}

	public List<TrackApplication> findByStatusAndDepartmentAndRoundSecond(String department, String status, User user) {
		// TODO Auto-generated method stub
		return this.trackAppRepository.findByStatusAndDepartmentAndRoundSecond(department, status, user);
	}

	public List<TrackApplication> findByStatusAndDepartmentAndRoundThird(String department, String status, User user) {
		// TODO Auto-generated method stub
		return this.trackAppRepository.findByStatusAndDepartmentAndRoundThird(department, status, user);
	}

	public List<TrackApplication> findByStatusAndDepartment(String department, String status, User user) {
		// TODO Auto-generated method stub
		return this.trackAppRepository.findByStatusAndDepartment(department,status,user);
	}

	public List<TrackApplication> findByStatusAndRoundOne(String status, User user) {
		// TODO Auto-generated method stub
		return this.trackAppRepository.findByStatusAndRoundOne(status,user);
	}
	
	public List<TrackApplication> findByStatusAndRoundSecond(String status, User user) {
		// TODO Auto-generated method stub
		return this.trackAppRepository.findByStatusAndRoundSecond(status,user);
	}
	
	public List<TrackApplication> findByStatusAndRoundThird(String status, User user) {
		// TODO Auto-generated method stub
		return this.trackAppRepository.findByStatusAndRoundThird(status,user);
	}

	public List<TrackApplication> findByStatusAndUser(String status, User user) {
		// TODO Auto-generated method stub
		return this.trackAppRepository.findByStatusAndUser(status,user);
	}

	public List<TrackApplication> findTrackAppByDateAndHrStatus(String addedByUuid,String today, int i) {
		// TODO Auto-generated method stub
		return this.trackAppRepository.findByAddedByUuidAndPostDateAndHrEmailStatus(addedByUuid,today,i);
	}

	public List<HrScreening> findHrScreeningByDateAndHrStatus(String addedByUuid,String today, int i) {
		// TODO Auto-generated method stub
		return this.hrScreeningRepository.findByAddedByUuidAndPostDateAndHrEmailStatus(addedByUuid,today,i);
	}

	public List<TechnicalRound> findTechincalByDateAndHrStatus(String addedByUuid,String today, int i) {
		// TODO Auto-generated method stub
		return this.technicalRoundRepository.findByAddedByUuidAndPostDateAndHrEmailStatus(addedByUuid,today,i);
	}

	public void saveAllHrScreening(List<HrScreening> screening) {
		this.hrScreeningRepository.saveAll(screening);
	}

	public void saveAllTechnicalRound(List<TechnicalRound> technical) {
		this.technicalRoundRepository.saveAll(technical);
	}	
}
