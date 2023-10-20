package com.corpseed.serviceImpl.servicesserviceimpl;

import com.corpseed.entity.serviceentity.ServiceState;
import com.corpseed.entity.serviceentity.Services;
import com.corpseed.repository.servicerepo.ServiceStateRepo;
import com.corpseed.service.servicesservice.ServiceStateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ServiceStateServiceImpl implements ServiceStateService {

    @Autowired
    private ServiceStateRepo serviceStateRepo;
    @Override
    public void saveServiceState(ServiceState serviceState) {
        this.serviceStateRepo.save(serviceState);
    }

    @Override
    public ServiceState findByStateNameAndServices(String stateCity, Services service) {
        return this.serviceStateRepo.findByStateNameAndServices(stateCity,service);
    }
}
