package com.corpseed.repository.servicerepo;

import com.corpseed.entity.serviceentity.ServiceState;
import com.corpseed.entity.serviceentity.Services;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ServiceStateRepo extends JpaRepository<ServiceState,Long> {
    ServiceState findByStateNameAndServices(String stateCity, Services service);
}
