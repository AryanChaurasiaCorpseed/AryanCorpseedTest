package com.corpseed.service.servicesservice;

import com.corpseed.entity.serviceentity.Services;
import com.corpseed.entity.serviceentity.SubService;

import java.util.List;

public interface SubServiceService {
    List<SubService> fetchAllSubServices(String uuid);

    void saveAllSubServices(List<SubService> subServiceList);

    SubService findSubServiceByUuidAndServicesAndIdNot(String uuid, Services serviceById, long id);

    void updateSubService(SubService subService);

    SubService findSubServiceByUuidAndId(String uuid, long id);

    SubService findSubServiceByIdAndDeleteStatus(long typeId, int i);

    void deleteSubService(SubService subService);

    List<SubService> findByParentServiceUuidAndDeleteStatusAndDisplayStatus(String uuid, int i,int s);

    void saveSubService(SubService subService);

    SubService findSubServiceByParentServiceUuidAndService(String uuid, Services addService);

    boolean isSubServiceOfService(String parentServiceUuid, Services service);
}
