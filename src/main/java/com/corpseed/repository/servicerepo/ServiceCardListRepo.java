package com.corpseed.repository.servicerepo;

import java.util.List;

import javax.validation.Valid;

import org.springframework.data.jpa.repository.JpaRepository;

import com.corpseed.entity.serviceentity.ServiceCardList;
import com.corpseed.entity.serviceentity.ServiceDetails;
import com.corpseed.entity.serviceentity.Services;
import org.springframework.stereotype.Repository;

@Repository
public interface ServiceCardListRepo extends JpaRepository<ServiceCardList, Long> {

	List<ServiceCardList> findByServiceDetails(@Valid ServiceDetails details);

	ServiceCardList findByServiceDetailsAndService(ServiceDetails saveServiceDetails,
			Services eservice);

}
