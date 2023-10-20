package com.corpseed.serviceImpl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.corpseed.entity.hrmentity.CandidateDocuments;
import com.corpseed.repository.CandidateDocumentRepository;

@Service
public class CandidateDocumentService {

	@Autowired
	private CandidateDocumentRepository candidateDocumentRepository;

	public void saveDocuments(List<CandidateDocuments> docList) {
		this.candidateDocumentRepository.saveAll(docList);
	}

	public CandidateDocuments findByUuid(String uuid) {
		return this.candidateDocumentRepository.findByUuidAndDeleteStatus(uuid,2);
	}

	public void saveDocument(CandidateDocuments candDoc) {
		this.candidateDocumentRepository.save(candDoc);
	}

}
