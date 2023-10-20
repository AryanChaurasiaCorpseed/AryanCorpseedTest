package com.corpseed.repository.hrmrepo;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.corpseed.entity.hrmentity.JobApplication;
import com.corpseed.entity.hrmentity.TrackApplication;
import com.corpseed.entity.User;
import org.springframework.stereotype.Repository;

@Repository
public interface TrackAppRepository extends JpaRepository<TrackApplication, Long> {

	TrackApplication findByJobApplicationAndDeleteStatus(JobApplication jobApp,int dStatus);

	TrackApplication findByUuidAndDeleteStatus(String uuid,int dStatus);

	List<TrackApplication> findByDeleteStatusOrderByIdDesc(int dStatus);

	List<TrackApplication> findByUserAndDeleteStatus(User loginedUser, int i);

	Page<TrackApplication> findByStatusAndDeleteStatus(String status, int i, Pageable pageable);

	Page<TrackApplication> findByAddedByUuidAndStatusAndDeleteStatus(String uuid, String status, int i,
			Pageable pageable);

	Page<TrackApplication> findByDeleteStatusAndStatusNotOrderByIdDesc(int i, String string, Pageable pageable);

	Page<TrackApplication> findByAddedByUuidAndDeleteStatusAndStatusNotOrderByIdDesc(String uuid, int i, String string,
			Pageable pageable);

	@Query("select tr from TrackApplication tr LEFT JOIN TechnicalRound te ON tr=te.trackApplication "
			+ "WHERE te.status=?2 and exists(select j from JobApplication j where "
			+ "tr.jobApplication=j and exists(select jb from Jobs jb where jb.jobTitle=?1 "
			+ "and j.jobs=jb)) group by tr")
	Page<TrackApplication> findByStatusAndDepartmentAndRoundOne(String department, String status, Pageable pageable);
	
	@Query("select tr from TrackApplication tr LEFT JOIN HrScreening h ON tr=h.trackApplication "
			+ "WHERE h.status=?2 and exists(select j from JobApplication j "
			+ "where tr.jobApplication=j and exists(select jb from Jobs jb where jb.jobTitle=?1 "
			+ "and j.jobs=jb)) group by tr")
	Page<TrackApplication> findByStatusAndDepartmentAndRoundSecond(String department, String status, Pageable pageable);
	
	@Query("select tr from TrackApplication tr LEFT JOIN HrAndTechnical h ON tr=h.trackApplication "
			+ "WHERE h.status=?2 and exists(select j from JobApplication j where tr.jobApplication=j "
			+ "and exists(select jb from Jobs jb where jb.jobTitle=?1 and j.jobs=jb)) group by tr")
	Page<TrackApplication> findByStatusAndDepartmentAndRoundThird(String department, String status, Pageable pageable);
	
	@Query("select tr from TrackApplication tr LEFT JOIN DocumentVerification d ON "
			+ "tr=d.trackApplication WHERE d.status=?2 and exists(select j from JobApplication j "
			+ "where tr.jobApplication=j and exists(select jb from Jobs jb where jb.jobTitle=?1 "
			+ "and j.jobs=jb)) group by tr")
	Page<TrackApplication> findByStatusAndDepartmentAndRoundFourth(String department, String status, Pageable pageable);

	@Query("select tr from TrackApplication tr LEFT JOIN TechnicalRound te ON "
			+ "tr=te.trackApplication WHERE te.status=?1 group by tr")
	Page<TrackApplication> findByStatusAndRoundOne(String status, Pageable pageable);
	
	@Query("select tr from TrackApplication tr LEFT JOIN HrScreening h ON tr=h.trackApplication "
			+ "WHERE h.status=?1 group by tr")
	Page<TrackApplication> findByStatusAndRoundSecond(String status, Pageable pageable);
	
	@Query("select tr from TrackApplication tr LEFT JOIN HrAndTechnical h ON tr=h.trackApplication "
			+ "WHERE h.status=?1 group by tr")
	Page<TrackApplication> findByStatusAndRoundThird(String status, Pageable pageable);
	
	@Query("select tr from TrackApplication tr LEFT JOIN DocumentVerification d ON "
			+ "tr=d.trackApplication WHERE d.status=?1 group by tr")
	Page<TrackApplication> findByStatusAndRoundFourth(String status, Pageable pageable);
	
	@Query("select tr from TrackApplication tr WHERE tr.status=?2 and exists(select j from "
			+ "JobApplication j where tr.jobApplication=j and exists(select jb from Jobs jb where "
			+ "jb.jobTitle=?1 and j.jobs=jb)) group by tr")
	Page<TrackApplication> findByStatusAndDepartment(String department,String status, Pageable pageable);
		
	@Query("select tr from TrackApplication tr LEFT JOIN TechnicalRound te ON tr=te.trackApplication "
			+ "WHERE te.status=?2 and tr.addedByUuid=?3 and exists(select j from JobApplication j "
			+ "where tr.jobApplication=j and exists(select jb from Jobs jb where jb.jobTitle=?1 "
			+ "and j.jobs=jb)) group by tr")
	Page<TrackApplication> findByStatusAndDepartmentAndRoundOne(String department, String status,String uuid, Pageable pageable);
	
	@Query("select tr from TrackApplication tr LEFT JOIN HrScreening h  ON tr=h.trackApplication "
			+ "WHERE h.status=?2 and tr.addedByUuid=?3 and exists(select j from JobApplication j "
			+ "where tr.jobApplication=j and exists(select jb from Jobs jb where jb.jobTitle=?1 "
			+ "and j.jobs=jb)) group by tr")
	Page<TrackApplication> findByStatusAndDepartmentAndRoundSecond(String department, String status,String uuid, Pageable pageable);
	
	@Query("select tr from TrackApplication tr LEFT JOIN HrAndTechnical h ON tr=h.trackApplication "
			+ "WHERE h.status=?2 and tr.addedByUuid=?3 and exists(select j from JobApplication j "
			+ "where tr.jobApplication=j and exists(select jb from Jobs jb where jb.jobTitle=?1 "
			+ "and j.jobs=jb)) group by tr")
	Page<TrackApplication> findByStatusAndDepartmentAndRoundThird(String department, String status,String uuid, Pageable pageable);
	
	@Query("select tr from TrackApplication tr LEFT JOIN DocumentVerification d ON "
			+ "tr=d.trackApplication WHERE d.status=?2 and tr.addedByUuid=?3 and "
			+ "exists(select j from JobApplication j where tr.jobApplication=j and "
			+ "exists(select jb from Jobs jb where jb.jobTitle=?1 and j.jobs=jb)) group by tr")
	Page<TrackApplication> findByStatusAndDepartmentAndRoundFourth(String department, String status,String uuid, Pageable pageable);

	@Query("select tr from TrackApplication tr LEFT JOIN TechnicalRound te ON tr=te.trackApplication "
			+ "WHERE te.status=?1 and tr.addedByUuid=?2 group by tr")
	Page<TrackApplication> findByStatusAndRoundOne(String status,String uuid, Pageable pageable);
	
	@Query("select tr from TrackApplication tr LEFT JOIN HrScreening h ON tr=h.trackApplication "
			+ "WHERE h.status=?1 and tr.addedByUuid=?2 group by tr")
	Page<TrackApplication> findByStatusAndRoundSecond(String status,String uuid, Pageable pageable);
	
	@Query("select tr from TrackApplication tr LEFT JOIN HrAndTechnical h ON tr=h.trackApplication "
			+ "WHERE h.status=?1 and tr.addedByUuid=?2 group by tr")
	Page<TrackApplication> findByStatusAndRoundThird(String status,String uuid, Pageable pageable);
	
	@Query("select tr from TrackApplication tr LEFT JOIN DocumentVerification d ON "
			+ "tr=d.trackApplication WHERE d.status=?1 and tr.addedByUuid=?2 group by tr")
	Page<TrackApplication> findByStatusAndRoundFourth(String status,String uuid, Pageable pageable);
	
	@Query("select tr from TrackApplication tr WHERE tr.status=?2 and tr.addedByUuid=?3 "
			+ "and exists(select j from JobApplication j where tr.jobApplication=j and "
			+ "exists(select jb from Jobs jb where jb.jobTitle=?1 and j.jobs=jb)) group by tr")
	Page<TrackApplication> findByStatusAndDepartment(String department,String status,String uuid, Pageable pageable);

//	@Query("select t from TrackApplication t LEFT JOIN TechnicalRound te ON t=te.trackApplication LEFT JOIN"
//			+ " HrScreening h ON t=h.trackApplication where "
//			+ "(te.user=?1 or h.user=?1 or t.user=?1) group by t")
//	List<TrackApplication> findTrackApplicationByUser(User loginedUser);
	
	@Query("select t from TrackApplication t LEFT JOIN TechnicalRound te ON t=te.trackApplication LEFT JOIN"
			+ " HrScreening h ON t=h.trackApplication LEFT JOIN HrAndTechnical ht on t=ht.trackApplication where "
			+ "(t.user=?1 and te=null) or "
			+ "(te.user=?1 and h=null) or "
			+ "(h.user=?1 and ht=null) group by t")	
	List<TrackApplication> findTrackApplicationByUser(User loginedUser);
	
	@Query("select t from TrackApplication t LEFT JOIN TechnicalRound te ON t=te.trackApplication LEFT JOIN"
			+ " HrScreening h ON t=h.trackApplication LEFT JOIN HrAndTechnical ht ON t=ht.trackApplication where "
			+ "(t.user=?1 and te.status='1' and (h=null or h.user!=?1)) or "
			+ "(te.user=?1 and h.status='1' and ht=null) or "
			+ "(h.user=?1 and ht.status='1') group by t")	
	List<TrackApplication> findSelectedTrackApplicationByUser(User loginedUser);
	
	@Query("select t from TrackApplication t LEFT JOIN TechnicalRound te ON t=te.trackApplication LEFT JOIN"
			+ " HrScreening h ON t=h.trackApplication LEFT JOIN HrAndTechnical ht ON t=ht.trackApplication where "
			+ "(t.user=?1 and te.status='3' and (h=null or h.user!=?1)) or "
			+ "(te.user=?1 and h.status='3' and ht=null) or "
			+ "(h.user=?1 and ht.status='3') group by t")	
	List<TrackApplication> findHoldTrackApplicationByUser(User loginedUser);
	
	@Query("select t from TrackApplication t LEFT JOIN TechnicalRound te ON t=te.trackApplication LEFT JOIN"
			+ " HrScreening h ON t=h.trackApplication LEFT JOIN HrAndTechnical ht ON t=ht.trackApplication where "
			+ "(t.user=?1 and te.status='2' and (h=null or h.user!=?1)) or "
			+ "(te.user=?1 and h.status='2' and ht=null) or "
			+ "(h.user=?1 and ht.status='2') group by t")	
	List<TrackApplication> findRejectedTrackApplicationByUser(User loginedUser);
	
	@Query("select tr from TrackApplication tr LEFT JOIN TechnicalRound te ON tr=te.trackApplication "
			+ "WHERE tr.user=?3 and te.status=?2 and exists(select j from JobApplication j where "
			+ "tr.jobApplication=j and exists(select jb from Jobs jb where jb.jobTitle=?1 and "
			+ "j.jobs=jb)) group by tr")
	List<TrackApplication> findByStatusAndDepartmentAndRoundOne(String department, String status, User user);
	
	@Query("select tr from TrackApplication tr LEFT JOIN TechnicalRound te ON tr=te.trackApplication"
			+ " LEFT JOIN HrScreening h ON tr=h.trackApplication"
			+ " WHERE te.user=?3 "
			+ "and h.status=?2 and exists(select j from JobApplication j where tr.jobApplication=j and "
			+ "exists(select jb from Jobs jb where jb.jobTitle=?1 and j.jobs=jb)) group by tr")
	List<TrackApplication> findByStatusAndDepartmentAndRoundSecond(String department, String status, User user);

	@Query("select tr from TrackApplication tr LEFT JOIN HrScreening h ON "
			+ "tr=h.trackApplication LEFT JOIN HrAndTechnical hr ON tr=hr.trackApplication "
			+ "WHERE h.user=?3 and hr.status=?2 and exists(select j from JobApplication j where "
			+ "tr.jobApplication=j and exists(select jb from Jobs jb where jb.jobTitle=?1 and "
			+ "j.jobs=jb)) group by tr")
	List<TrackApplication> findByStatusAndDepartmentAndRoundThird(String department, String status, User user);

	@Query("select tr from TrackApplication tr WHERE tr.status=?2 and tr.user=?3 and "
			+ "exists(select j from JobApplication j where tr.jobApplication=j and "
			+ "exists(select jb from Jobs jb where jb.jobTitle=?1 and j.jobs=jb)) group by tr")
	List<TrackApplication> findByStatusAndDepartment(String department, String status, User user);

	@Query("select tr from TrackApplication tr LEFT JOIN TechnicalRound te ON tr=te.trackApplication "
			+ "WHERE tr.user=?2 and te.status=?1 group by tr")
	List<TrackApplication> findByStatusAndRoundOne(String status, User user);
	
	@Query("select tr from TrackApplication tr LEFT JOIN TechnicalRound te ON tr=te.trackApplication"
			+ " LEFT JOIN HrScreening h ON tr=h.trackApplication WHERE te.user=?2 and h.status=?1 group by tr")
	List<TrackApplication> findByStatusAndRoundSecond(String status, User user);
	
	@Query("select tr from TrackApplication tr LEFT JOIN HrScreening h ON "
			+ "tr=h.trackApplication LEFT JOIN HrAndTechnical hr ON tr=hr.trackApplication "
			+ "WHERE h.user=?2 and hr.status=?1 group by tr")
	List<TrackApplication> findByStatusAndRoundThird(String status, User user);

	@Query("select t from TrackApplication t LEFT JOIN TechnicalRound te ON t=te.trackApplication LEFT JOIN"
			+ " HrScreening h ON t=h.trackApplication LEFT JOIN HrAndTechnical ht ON t=ht.trackApplication where "
			+ "(t.user=?2 and te.status=?1 and (h=null or h.user!=?2)) or "
			+ "(te.user=?2 and h.status=?1 and ht=null) or "
			+ "(h.user=?2 and ht.status=?1) group by t")
	List<TrackApplication> findByStatusAndUser(String status, User user);

	List<TrackApplication> findByAddedByUuidAndPostDateAndHrEmailStatus(String addedByUuid,String today, int i);
}
