package com.corpseed.service.servicesservice;

import com.corpseed.entity.serviceentity.ServiceState;
import com.corpseed.entity.serviceentity.Services;

import java.util.Optional;

public interface ServiceStateService {
    void saveServiceState(ServiceState mapToServiceState);

    ServiceState findByStateNameAndServices(String stateCity, Services service);
}
