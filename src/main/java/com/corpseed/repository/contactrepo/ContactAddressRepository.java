package com.corpseed.repository.contactrepo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.corpseed.entity.contactentity.ContactAddress;
import org.springframework.stereotype.Repository;

@Repository
public interface ContactAddressRepository extends JpaRepository<ContactAddress, Long> {

	ContactAddress findByUuidAndDeleteStatus(String addressuuid,int dStatus);

	List<ContactAddress> findByDeleteStatusOrderByIdDesc(int dStatus);

	List<ContactAddress> findByDisplayStatusAndDeleteStatus(String status,int dStatus);

	ContactAddress findByIdAndDeleteStatus(long typeId, int dStatus);

}
