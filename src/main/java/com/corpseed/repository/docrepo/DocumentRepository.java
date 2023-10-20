package com.corpseed.repository.docrepo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.corpseed.entity.documententity.Documents;
import org.springframework.stereotype.Repository;

@Repository
public interface DocumentRepository extends JpaRepository<Documents, Long> {

	Documents findByUuidAndDeleteStatus(String uuid,int dStatus);

	List<Documents> findByDeleteStatusOrderByIdDesc(int dStatus);

	Documents findByIdAndDeleteStatus(long typeId, int dStatus);

}
