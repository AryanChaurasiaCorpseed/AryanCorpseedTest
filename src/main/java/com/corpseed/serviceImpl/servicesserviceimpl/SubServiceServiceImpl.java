package com.corpseed.serviceImpl.servicesserviceimpl;

import com.corpseed.entity.serviceentity.Services;
import com.corpseed.entity.serviceentity.SubService;
import com.corpseed.repository.servicerepo.SubServiceRepository;
import com.corpseed.service.servicesservice.SubServiceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SubServiceServiceImpl implements SubServiceService {

    @Autowired
    private SubServiceRepository subServiceRepository;

    @Override
    public List<SubService> fetchAllSubServices(String uuid) {
        return this.subServiceRepository.findByParentServiceUuidAndDeleteStatus(uuid,2);
    }

    @CacheEvict(value = "subService",allEntries = true)
    @Override
    public void saveAllSubServices(List<SubService> subServiceList) {
        this.subServiceRepository.saveAll(subServiceList);
    }

    @Override
    public SubService findSubServiceByUuidAndServicesAndIdNot(String uuid, Services serviceById, long id) {
        return this.subServiceRepository.findByParentServiceUuidAndServicesAndIdNot(uuid,serviceById,id);
    }

    @CacheEvict(value = "subService",allEntries = true)
    @Override
    public void updateSubService(SubService subService) {
        this.subServiceRepository.save(subService);
    }

    @Override
    public SubService findSubServiceByUuidAndId(String uuid, long id) {
        return this.subServiceRepository.findByParentServiceUuidAndId(uuid,id);
    }

    @Override
    public SubService findSubServiceByIdAndDeleteStatus(long typeId, int i) {
        return this.subServiceRepository.findByIdAndDeleteStatus(typeId,i);
    }

    @CacheEvict(value = "subService",allEntries = true)
    @Override
    public void deleteSubService(SubService subService) {
        this.subServiceRepository.delete(subService);
    }

    @Cacheable(value = "subService",key = "#uuid")
    @Override
    public List<SubService> findByParentServiceUuidAndDeleteStatusAndDisplayStatus(String uuid, int d,int s) {
        return this.subServiceRepository.findByParentServiceUuidAndDeleteStatusAndDisplayStatus(uuid,d,s);
    }

    @CacheEvict(value = "subService",allEntries = true)
    @Override
    public void saveSubService(SubService subService) {
        this.subServiceRepository.save(subService);
    }

    @Override
    public SubService findSubServiceByParentServiceUuidAndService(String uuid, Services addService) {
        return this.subServiceRepository.findByParentServiceUuidAndServices(uuid,addService);
    }

    @Override
    public boolean isSubServiceOfService(String parentServiceUuid, Services service) {
        SubService subService=this.subServiceRepository.findByParentServiceUuidAndServicesAndDeleteStatusAndDisplayStatus(parentServiceUuid,service,2,1);
        if(subService==null)
            return false;
        return true;

    }
}
