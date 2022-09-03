package com.surge.vms.dto;

import javax.persistence.Column;

public class DocumentDTO {
	
	private String docName;

	private String docPath;
	
	private String docOwnerId;

	private String docDomainType;
	
	private Long docProcessInstanceId;
	
	private String docEncodedContent;
	
	private String docMimeType;

	public String getDocName() {
		return docName;
	}

	public void setDocName(String docName) {
		this.docName = docName;
	}

	public String getDocPath() {
		return docPath;
	}

	public void setDocPath(String docPath) {
		this.docPath = docPath;
	}

	public String getDocOwnerId() {
		return docOwnerId;
	}

	public void setDocOwnerId(String docOwnerId) {
		this.docOwnerId = docOwnerId;
	}

	public String getDocDomainType() {
		return docDomainType;
	}

	public void setDocDomainType(String docDomainType) {
		this.docDomainType = docDomainType;
	}

	public Long getDocProcessInstanceId() {
		return docProcessInstanceId;
	}

	public void setDocProcessInstanceId(Long docProcessInstanceId) {
		this.docProcessInstanceId = docProcessInstanceId;
	}

	public String getDocEncodedContent() {
		return docEncodedContent;
	}

	public void setDocEncodedContent(String docEncodedContent) {
		this.docEncodedContent = docEncodedContent;
	}

	public String getDocMimeType() {
		return docMimeType;
	}

	public void setDocMimeType(String docMimeType) {
		this.docMimeType = docMimeType;
	}
	

	
	

}
