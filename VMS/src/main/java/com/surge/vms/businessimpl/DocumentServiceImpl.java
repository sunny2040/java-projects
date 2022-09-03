package com.surge.vms.businessimpl;



import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.surge.vms.business.DocumentService;
import com.surge.vms.model.Document;
import com.surge.vms.repositories.DocumentRepository;

@Service
public class DocumentServiceImpl implements DocumentService{
	
	@Autowired
	private DocumentRepository documentRepository;

	@Override
	public Document addDocument(Document document){
		
		Document retdocument = documentRepository.save(document);
		return retdocument;
		
	}
	
	@Override
	public List<Document> getDocumentByOwnerIdAndDocType(String ownerId, String docDomaintype){
		
		List<Document> retDocList = documentRepository.getDocumentByDocOwnerIdAndDocDomainType(ownerId, docDomaintype);
		return retDocList;
		
	}
	
	@Override
	public List<Document> getDocumentByOwnerId(String ownerId){
		List<Document> retDocList = documentRepository.getDocumentByDocOwnerId(ownerId);
		return retDocList;
		
	}

}
