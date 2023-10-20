package com.corpseed.repository.enqrepo;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.corpseed.entity.enquiryentity.Enquiry;
import org.springframework.stereotype.Repository;

@Repository
public interface EnquiryRepository extends JpaRepository<Enquiry, Long> {

	Enquiry findByUuidAndDeleteStatus(String uuid,int dStatus);
	
	@Query("select e from Enquiry e where e.type= :type and e.displayStatus= :status and e.categoryId= :id and e.deleteStatus=2 GROUP BY e.email")
	List<Enquiry> findByTypeAndDisplayStatusAndCategoryId(@Param("type") String type, @Param("status") String status, @Param("id") String id);

	List<Enquiry> findByTypeAndDisplayStatusAndServiceIdAndDeleteStatus(String type, String status, String id,int dStatus);

	List<Enquiry> findByTypeAndDisplayStatusAndIndustryIdAndDeleteStatus(String type, String status, String id,int dStatus);

	@Query("select count(e) from Enquiry e where (e.email!=null and e.email!='NA' and e.email= :email) or e.mobile LIKE %:mobile")
	int findByEmailOrMobile(@Param("email") String email, @Param("mobile") String mobile);

	List<Enquiry> findByDeleteStatusOrderByIdDesc(int dStatus);

	@Query("select count(e) from Enquiry e where e.mobile= :mobile")
	int findByMobile(@Param("mobile") String mobile);

	Enquiry findByIdAndDeleteStatus(long typeId, int dStatus);

	List<Enquiry> findByDisplayStatusAndDeleteStatusAndBitrixStatus(String string, int i, int j);

	Page<Enquiry> findByDisplayStatusAndDeleteStatus(String string, int i, Pageable pageable);

	Page<Enquiry> findByDisplayStatusAndDeleteStatusAndPostDateBetween(String string, int i, String fromDate,
			String toDate, Pageable pageable);

}
