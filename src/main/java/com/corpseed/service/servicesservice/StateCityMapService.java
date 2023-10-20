package com.corpseed.service.servicesservice;

import com.corpseed.entity.serviceentity.Services;
import com.corpseed.entity.serviceentity.StateCityService;

import java.util.List;

public interface StateCityMapService {
    List<StateCityService> findByServices(Services service);
}
