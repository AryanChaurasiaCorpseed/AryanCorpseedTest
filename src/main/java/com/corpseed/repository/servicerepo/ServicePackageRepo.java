package com.corpseed.repository.servicerepo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.corpseed.entity.serviceentity.ServicePackage;
import com.corpseed.entity.serviceentity.Services;
import org.springframework.stereotype.Repository;

@Repository
public interface ServicePackageRepo extends JpaRepository<ServicePackage, Long> {

	ServicePackage findByPackageNameAndServicesAndDeleteStatus(String packageName, Services service
			,int dStatus);

	List<ServicePackage> findByServicesAndDeleteStatus(Services services,int dStatus);

	ServicePackage findByUuidAndDeleteStatus(String packageUUID,int dStatus);

	ServicePackage findByPackageNameAndServicesAndUuidNot(String packageName, Services service,String packageUUID);

	List<ServicePackage> findByDeleteStatus(int dStatus);

	ServicePackage findByIdAndDeleteStatus(long typeId, int dStatus);

	List<ServicePackage> findByServicesAndDeleteStatusAndDisplayStatus(Services services, int i, String string);

}
