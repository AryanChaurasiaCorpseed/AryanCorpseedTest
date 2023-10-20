package com.corpseed.serviceImpl.servicesserviceimpl;

import com.corpseed.entity.serviceentity.ServiceCity;
import com.corpseed.entity.serviceentity.ServiceState;
import com.corpseed.entity.serviceentity.Services;
import com.corpseed.repository.servicerepo.ServiceCityRepo;
import com.corpseed.service.servicesservice.ServiceCityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ServiceCityServiceImpl implements ServiceCityService {

    @Autowired
    private ServiceCityRepo serviceCityRepo;
    @Override
    public void saveServiceCity(ServiceCity serviceCity) {
        this.serviceCityRepo.save(serviceCity);
    }

    @Override
    public ServiceCity findByCityNameAndServices(String stateCity, Services service) {
        return this.serviceCityRepo.findByCityNameAndServices(stateCity,service);
    }

    @Override
    public List<ServiceCity> findByServiceStateNameAndStateServiceId(String stateCity, long id) {
        return this.serviceCityRepo.findByServiceStateNameAndStateServiceId(stateCity,id);
    }

}
