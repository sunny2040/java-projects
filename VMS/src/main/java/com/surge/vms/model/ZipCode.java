package com.surge.vms.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity

public class ZipCode {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column
	private Long zipCodeId;

	@Column
	private String zipCode;
	
	@Column
	private String zipCodeName;
	
	@Column
	private String cityCode;

	public Long getZipCodeId() {
		return zipCodeId;
	}

	public void setZipCodeId(Long zipCodeId) {
		this.zipCodeId = zipCodeId;
	}

	public String getZipCode() {
		return zipCode;
	}

	public void setZipCode(String zipCode) {
		this.zipCode = zipCode;
	}

	public String getZipCodeName() {
		return zipCodeName;
	}

	public void setZipCodeName(String zipCodeName) {
		this.zipCodeName = zipCodeName;
	}

	public String getCityCode() {
		return cityCode;
	}

	public void setCityCode(String cityCode) {
		this.cityCode = cityCode;
	}

	
}
