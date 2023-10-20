package com.corpseed.serviceImpl;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.corpseed.entity.documententity.Documents;
import com.corpseed.repository.docrepo.DocumentRepository;

@Service
public class DocumentService {

	@Autowired
	private DocumentRepository documentRepository;
	
	public Documents saveDocument(@Valid Documents documents) {
		return this.documentRepository.save(documents);
	}

	public List<Documents> getAllDocuments() {
		return this.documentRepository.findByDeleteStatusOrderByIdDesc(2);
	}

	public Documents getDocumentByUuid(String uuid) {
		return this.documentRepository.findByUuidAndDeleteStatus(uuid,2);
	}

	public void deleteDocument(long id) {
		this.documentRepository.deleteById(id);
	}

	public Documents findByIdAndDeleteStatus(long typeId, int dStatus) {
		return this.documentRepository.findByIdAndDeleteStatus(typeId,dStatus);
	}

}
