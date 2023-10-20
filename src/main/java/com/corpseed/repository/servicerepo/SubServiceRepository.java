package com.corpseed.repository.servicerepo;

import com.corpseed.entity.serviceentity.Services;
import com.corpseed.entity.serviceentity.SubService;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SubServiceRepository extends JpaRepository<SubService,Long> {
    List<SubService> findByParentServiceUuidAndDeleteStatus(String uuid,int deleteStatus);

    SubService findByParentServiceUuidAndServicesAndIdNot(String uuid, Services serviceById, long id);

    SubService findByParentServiceUuidAndId(String uuid, long id);

    SubService findByIdAndDeleteStatus(long typeId, int i);

    List<SubService> findByParentServiceUuidAndDeleteStatusAndDisplayStatus(String uuid, int d, int s);

    SubService findByParentServiceUuidAndServices(String uuid, Services addService);

    SubService findByParentServiceUuidAndServicesAndDeleteStatusAndDisplayStatus(String parentServiceUuid, Services service, int i, int i1);
}
