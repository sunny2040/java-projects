package com.surge.vms.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Transient;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Entity
public class CompanyLegal {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column
	private Long legalId;

	@Column
	private String legalEntity;

	@Column
	private String dunsNumber;

	@Column
	private String serviceArea;

	@JsonInclude()
	@Transient

	Map<String,String> fileNameDomainTypeMap = new HashMap<String, String>();

	@CreationTimestamp
	private LocalDateTime createDateTime;

	@UpdateTimestamp
	private LocalDateTime updateDateTime;

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

	public Long getLegalId() {
		return legalId;
	}

	public void setLegalId(Long legalId) {
		this.legalId = legalId;
	}

	public String getLegalEntity() {
		return legalEntity;
	}

	public void setLegalEntity(String legalEntity) {
		this.legalEntity = legalEntity;
	}

	public String getDunsNumber() {
		return dunsNumber;
	}

	public void setDunsNumber(String dunsNumber) {
		this.dunsNumber = dunsNumber;
	}

	public String getServiceArea() {
		return serviceArea;
	}

	public void setServiceArea(String serviceArea) {
		this.serviceArea = serviceArea;
	}

	public Map<String, String> getFileNameDomainTypeMap() {
		return fileNameDomainTypeMap;
	}

	public void setFileNameDomainTypeMap(Map<String, String> fileNameDomainTypeMap) {
		this.fileNameDomainTypeMap = fileNameDomainTypeMap;
	}



}
