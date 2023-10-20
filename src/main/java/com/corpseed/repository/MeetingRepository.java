package com.corpseed.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.corpseed.entity.Meeting;
import org.springframework.stereotype.Repository;

@Repository
public interface MeetingRepository extends JpaRepository<Meeting, Long> {

	Meeting findByUuidAndDeleteStatus(String uuid,int dStatus);

	List<Meeting> findByDeleteStatusOrderByIdDesc(int dStatus);

	@Query("select count(m) from Meeting m where m.email= :email or m.mobile= :mobile")
	int findByEmailOrMobile(@Param("email") String email,@Param("mobile") String mobile);

	Meeting findByIdAndDeleteStatus(long typeId, int dStatus);

}
