package com.corpseed.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.corpseed.entity.hrmentity.CandidateDocuments;
import org.springframework.stereotype.Repository;

@Repository
public interface CandidateDocumentRepository extends JpaRepository<CandidateDocuments, Long> {
	CandidateDocuments findByUuidAndDeleteStatus(String uuid,int dStatus);

}
