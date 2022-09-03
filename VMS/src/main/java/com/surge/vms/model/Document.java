package com.surge.vms.model;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;


@Entity

public class Document {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column
	private Long docId;

	@Column
	private String docName;

	@Column
	private String docPath;
	
	@Column
	private String docOwnerId;

	@Column
	private String docDomainType;
	
	@Column
	private Long docProcessInstanceId;
	
	@Column
	private Long docProcessDefnId;
	
	
		
    @CreationTimestamp
    private LocalDateTime createDateTime;
 
    @UpdateTimestamp
    private LocalDateTime updateDateTime;

	public Long getDocId() {
		return docId;
	}

	public void setDocId(Long docId) {
		this.docId = docId;
	}

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




	public Long getDocProcessInstanceId() {
		return docProcessInstanceId;
	}

	public void setDocProcessInstanceId(Long docProcessInstanceId) {
		this.docProcessInstanceId = docProcessInstanceId;
	}

	public Long getDocProcessDefnId() {
		return docProcessDefnId;
	}

	public void setDocProcessDefnId(Long docProcessDefnId) {
		this.docProcessDefnId = docProcessDefnId;
	}

	public String getDocDomainType() {
		return docDomainType;
	}

	public void setDocDomainType(String docDomainType) {
		this.docDomainType = docDomainType;
	}

	public LocalDateTime getCreateDateTime() {
		return createDateTime;
	}

	public void setCreateDateTime(LocalDateTime createDateTime) {
		this.createDateTime = createDateTime;
	}

	public LocalDateTime getUpdateDateTime() {
		return updateDateTime;
	}

	public void setUpdateDateTime(LocalDateTime updateDateTime) {
		this.updateDateTime = updateDateTime;
	}

	public String getDocOwnerId() {
		return docOwnerId;
	}

	public void setDocOwnerId(String docOwnerId) {
		this.docOwnerId = docOwnerId;
	}

	

	

}