package com.corpseed.serviceImpl.servicesserviceimpl;

import com.corpseed.entity.serviceentity.Services;
import com.corpseed.entity.serviceentity.StateCityService;
import com.corpseed.repository.StateCityMapServiceRepo;
import com.corpseed.service.servicesservice.StateCityMapService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StateCityMapServiceImpl implements StateCityMapService {

    @Autowired
    private StateCityMapServiceRepo stateCityMapServiceRepo;

    @Override
    public List<StateCityService> findByServices(Services service) {
        return this.stateCityMapServiceRepo.findByServices(service);
    }
}
