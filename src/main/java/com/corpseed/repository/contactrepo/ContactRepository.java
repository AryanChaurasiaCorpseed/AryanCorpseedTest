package com.corpseed.repository.contactrepo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.corpseed.entity.contactentity.Contact;
import org.springframework.stereotype.Repository;

@Repository
public interface ContactRepository extends JpaRepository<Contact, Long> {

	Contact findByDisplayStatusAndDeleteStatus(String status,int dStatus);

	Contact findByMobileOrEmail(String mobile, String email);

	Contact findByUuidAndDeleteStatus(String contactuuid,int dStatus);

	Contact findByDisplayStatusAndDeleteStatusAndUuidNot(String status,int dStatus, String uuid);

	Contact findByMobileAndDeleteStatusAndUuidNot(String mobile,int dStatus, String uuid);

	Contact findByEmailAndDeleteStatusAndUuidNot(String email,int dStatus, String uuid);

	List<Contact> findByDeleteStatusOrderByIdDesc(int dStatus);

	Contact findByIdAndDeleteStatus(long typeId, int dStatus);

}
