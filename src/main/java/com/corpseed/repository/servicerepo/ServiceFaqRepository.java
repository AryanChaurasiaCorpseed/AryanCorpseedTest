package com.corpseed.repository.servicerepo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.corpseed.entity.serviceentity.ServiceFaq;
import com.corpseed.entity.serviceentity.Services;
import org.springframework.stereotype.Repository;

@Repository
public interface ServiceFaqRepository extends JpaRepository<ServiceFaq, Long> {

	ServiceFaq findByTitleAndServices(String title, Services service);

	ServiceFaq findByUuidAndDeleteStatus(String faqUUID,int dStatus);

	ServiceFaq findByTitleAndServicesAndUuidNot(String title, Services service 
			,String faqUUID);

	ServiceFaq findByIdAndDeleteStatus(long typeId, int dStatus);

	List<ServiceFaq> findByServicesAndDeleteStatusAndDisplayStatus(Services service, int i, String dStatus);

	List<ServiceFaq> findByServicesAndDeleteStatus(Services services, int i);

}
