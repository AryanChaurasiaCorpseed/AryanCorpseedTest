package com.corpseed.repository.industryrepo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.corpseed.entity.industryentity.Industry;
import com.corpseed.entity.industryentity.IndustryDetails;
import org.springframework.stereotype.Repository;

@Repository
public interface IndustryDetailsRepo extends JpaRepository<IndustryDetails, Long> {

	List<IndustryDetails> findByIndustryAndDeleteStatus(Industry industry,int dStatus);

	IndustryDetails findByUuidAndDeleteStatus(String detailsUUID,int dStatus);

	IndustryDetails findByTabNameAndIndustryAndUuidNot(String tabName, 
			Industry industry,String detailsUUID);

	IndustryDetails findByDisplayOrderAndIndustryAndUuidNot(String displayOrder, 
			Industry industry,String detailsUUID);

	IndustryDetails findByTabNameAndIndustry(String tabName, Industry industry);

	IndustryDetails findByDisplayOrderAndIndustry(String displayOrder, 
			Industry industry);

	IndustryDetails findByIdAndDeleteStatus(long typeId, int dStatus);

}
