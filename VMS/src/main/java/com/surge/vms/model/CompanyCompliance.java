package com.surge.vms.model;


import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Transient;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.surge.vms.exception.validator.Author;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Entity
public class CompanyCompliance {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column
	private Long complianceId;

	@Column
	private String otherEntity;

	@Column
	private String supplierIncl;
	
	@Column
	private String insurance;
	
	@JsonInclude()
	@Transient
	
	Map<String, String> fileNameDomainTypeMap = new HashMap<String, String>();
	
	@CreationTimestamp
	private LocalDateTime createDateTime;

	@UpdateTimestamp
	private LocalDateTime updateDateTime;

	public Long getComplianceId() {
		return complianceId;
	}

	public void setComplianceId(Long complianceId) {
		this.complianceId = complianceId;
	}

	public String getOtherEntity() {
		return otherEntity;
	}

	public void setOtherEntity(String otherEntity) {
		this.otherEntity = otherEntity;
	}

	public String getSupplierIncl() {
		return supplierIncl;
	}

	public void setSupplierIncl(String supplierIncl) {
		this.supplierIncl = supplierIncl;
	}

	public String getInsurance() {
		return insurance;
	}

	public void setInsurance(String insurance) {
		this.insurance = insurance;
	}



	public Map<String, String> getFileNameDomainTypeMap() {
		return fileNameDomainTypeMap;
	}

	public void setFileNameDomainTypeMap(Map<String, String> fileNameDomainTypeMap) {
		this.fileNameDomainTypeMap = fileNameDomainTypeMap;
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
	
	
}
