package com.surge.vms.repositories;



import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.surge.vms.model.Document;
import com.surge.vms.model.VendorCompany;
 
 
@Repository
public interface DocumentRepository extends CrudRepository<Document, Long> {
	
	public List<Document> getDocumentByDocOwnerIdAndDocDomainType(String ownerId, String docType);
	
	public List<Document> getDocumentByDocOwnerId(String ownerId);
 
}


