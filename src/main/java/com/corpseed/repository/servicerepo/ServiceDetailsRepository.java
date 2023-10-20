package com.corpseed.repository.servicerepo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.corpseed.entity.serviceentity.ServiceDetails;
import com.corpseed.entity.serviceentity.Services;
import org.springframework.stereotype.Repository;

@Repository
public interface ServiceDetailsRepository extends JpaRepository<ServiceDetails, Long> {

	ServiceDetails findByTabNameAndServices(String tabName, Services service);

	ServiceDetails findByDisplayOrderAndServices(String displayOrder,
			Services service);

	ServiceDetails findByUuidAndDeleteStatus(String detailsUUID,int dStatus);

	ServiceDetails findByTabNameAndServicesAndUuidNot(String tabName, 
			Services service,String detailsUUID);

	ServiceDetails findByDisplayOrderAndServicesAndUuidNot(String displayOrder, 
			Services service,String detailsUUID);

	List<ServiceDetails> findByServicesAndDeleteStatus(Services service,int dStatus);

	ServiceDetails findByIdAndDeleteStatus(long typeId, int dStatus);

	List<ServiceDetails> findByServicesAndDeleteStatusAndDisplayStatus(Services service, int i, String dStatus);

}
