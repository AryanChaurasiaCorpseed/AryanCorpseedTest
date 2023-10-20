package com.corpseed.serviceImpl.servicesserviceimpl;

import com.corpseed.entity.serviceentity.ServiceContact;
import com.corpseed.entity.serviceentity.Services;
import com.corpseed.entity.User;
import com.corpseed.repository.servicerepo.ServiceContactRepository;
import com.corpseed.repository.servicerepo.ServiceRepository;
import com.corpseed.service.servicesservice.ServiceContactService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;

import java.util.List;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;

@Service
public class ServiceContactServiceImpl implements ServiceContactService {

    @Autowired
    private ServiceContactRepository serviceContactRepository;

    @Autowired
    private ServiceRepository serviceRepository;

    @Override
    public List<ServiceContact> fetchAllServiceContacts(String uuid) {
        Services service=this.serviceRepository.findByUuidAndDeleteStatus(uuid,2);

        return this.serviceContactRepository.findByServicesAndDeleteStatus(service,2);
    }

    @Override
    public ServiceContact findServiceContactByServiceAndId(String uuid, long id) {
        Services service=this.serviceRepository.findByUuidAndDeleteStatus(uuid,2);
        if(service==null)return null;

        ServiceContact serviceContact=this.serviceContactRepository.findByServicesAndId(service,id);

        return serviceContact;
    }

    @Cacheable(value = "serviceContactByService")
    @Override
    public ServiceContact findServiceContactByServiceAndUser(Services service, User user) {
        return this.serviceContactRepository.findByServicesAndUser(service,user);
    }
    @Caching(evict = {
            @CacheEvict(value = "serviceContact", allEntries = true),
            @CacheEvict(value = "serviceContactByService", allEntries = true),
            @CacheEvict(value = "findByUserSlug",allEntries = true)
    })
    @Override
    public void saveServiceContact(ServiceContact serviceContact) {
        this.serviceContactRepository.save(serviceContact);
    }

    @Override
    public ServiceContact findServiceContactByServiceAndUserAndIdNot(Services service, User user, long id) {
        return this.serviceContactRepository.findByServicesAndUserAndIdNot(service,user,id);
    }

    @Override
    public ServiceContact findServiceContactByIdAndDeleteStatus(long typeId, int i) {
        return this.serviceContactRepository.findByIdAndDeleteStatus(typeId,i);
    }

    @Caching(evict = {
            @CacheEvict(value = "serviceContact", allEntries = true),
            @CacheEvict(value = "serviceContactByService", allEntries = true),
            @CacheEvict(value = "findByUserSlug",allEntries = true)
    })
    @Override
    public void deleteServiceContact(ServiceContact serviceContact) {
        this.serviceContactRepository.delete(serviceContact);
    }

    @Cacheable(value = "serviceContact")
    @Override
    public List<ServiceContact> findByServiceAndDeleteStatusAndDisplayStatus(Services service, int i, int j) {
//        System.out.println("Service contact called.....................");
        return this.serviceContactRepository.findByServicesAndDeleteStatusAndDisplayStatus(service,i,j);
    }
}
