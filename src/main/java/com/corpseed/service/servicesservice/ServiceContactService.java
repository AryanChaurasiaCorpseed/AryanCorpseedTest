package com.corpseed.service.servicesservice;

import com.corpseed.entity.serviceentity.ServiceContact;
import com.corpseed.entity.serviceentity.Services;
import com.corpseed.entity.User;

import java.util.List;

public interface ServiceContactService {
    List<ServiceContact> fetchAllServiceContacts(String uuid);

    ServiceContact findServiceContactByServiceAndId(String uuid, long id);

    ServiceContact findServiceContactByServiceAndUser(Services service, User user);

    void saveServiceContact(ServiceContact serviceContact);

    ServiceContact findServiceContactByServiceAndUserAndIdNot(Services service, User user, long id);

    ServiceContact findServiceContactByIdAndDeleteStatus(long typeId, int i);

    void deleteServiceContact(ServiceContact serviceContact);

    List<ServiceContact> findByServiceAndDeleteStatusAndDisplayStatus(Services service, int i, int j);
}
