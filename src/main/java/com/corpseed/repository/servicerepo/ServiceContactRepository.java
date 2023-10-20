package com.corpseed.repository.servicerepo;

import com.corpseed.entity.serviceentity.ServiceContact;
import com.corpseed.entity.serviceentity.Services;
import com.corpseed.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ServiceContactRepository extends JpaRepository<ServiceContact,Long> {
    ServiceContact findByServicesAndId(Services service, long id);

    List<ServiceContact> findByServicesAndDeleteStatus(Services service, int deleteStatus);

    ServiceContact findByServicesAndUser(Services service, User user);

    ServiceContact findByServicesAndUserAndIdNot(Services service, User user, long id);

    ServiceContact findByIdAndDeleteStatus(long typeId, int i);

    List<ServiceContact> findByServicesAndDeleteStatusAndDisplayStatus(Services service, int i, int j);
}
