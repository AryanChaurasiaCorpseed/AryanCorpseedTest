package com.corpseed.repository.servicerepo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.corpseed.entity.serviceentity.ServiceBrands;
import com.corpseed.entity.serviceentity.Services;
import org.springframework.stereotype.Repository;

@Repository
public interface ServiceBrandRepo extends JpaRepository<ServiceBrands, Long> {

	ServiceBrands findByUuidAndDeleteStatus(String uuid,int dStatus);

	ServiceBrands findByIdAndDeleteStatus(long typeId, int dStatus);

	List<ServiceBrands> findByServicesAndDeleteStatus(Services service, int dStatus);

}
