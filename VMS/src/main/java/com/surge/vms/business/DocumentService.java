package com.surge.vms.business;


import java.util.List;

import com.surge.vms.model.Document;

public interface DocumentService {
	
	public Document addDocument(Document doc);
	public List<Document> getDocumentByOwnerIdAndDocType(String ownerId, String docDomaintype);
	public List<Document> getDocumentByOwnerId(String ownerId);

}
