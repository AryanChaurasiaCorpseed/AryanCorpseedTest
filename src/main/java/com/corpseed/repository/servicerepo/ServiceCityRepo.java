package com.corpseed.repository.servicerepo;

import com.corpseed.entity.serviceentity.ServiceCity;
import com.corpseed.entity.serviceentity.Services;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ServiceCityRepo extends JpaRepository<ServiceCity,Long> {
    ServiceCity findByCityNameAndServices(String stateCity, Services service);

    List<ServiceCity> findByServiceStateNameAndStateServiceId(String stateCity, long id);
}
