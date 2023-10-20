package com.corpseed.service.servicesservice;

import com.corpseed.entity.serviceentity.ServiceCity;
import com.corpseed.entity.serviceentity.ServiceState;
import com.corpseed.entity.serviceentity.Services;
import com.corpseed.entity.serviceentity.StateCityService;

import java.util.List;

public interface ServiceCityService {
    void saveServiceCity(ServiceCity mapToServiceCity);

    ServiceCity findByCityNameAndServices(String s, Services service);

    List<ServiceCity> findByServiceStateNameAndStateServiceId(String stateCity, long id);
}
