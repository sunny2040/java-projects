package com.surge.vms.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Transient;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.surge.vms.dto.UploadFileResponse;

@Entity

public class VendorCompany {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "company_id")
	private Long companyId;

	@Column
	private String companyName;

	@Column
	private String companyWebsite;

	@Column
	private Long companyTurnOver;

	@Column
	private Long companyPhone;

	@Column
	private Long companyFax;

	@Column
	private String category;

	@Column
	private String taxIdNumber;

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	@JoinColumn(name = "company_id")
	private Set<Address> address;

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	@JoinColumn(name = "company_id")
	private Set<BankAccount> bankAccount;

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	@JoinColumn(name = "company_id")
	private Set<CompanyLegal> companyLegal;

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	@JoinColumn(name = "company_id")
	private Set<CompanyCompliance> companyCompliance;

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	@JoinColumn(name = "company_id")
	private Set<Vendor> vendor;

	/*
	 * @OneToMany(targetEntity=Vendor.class,cascade = CascadeType.ALL, fetch =
	 * FetchType.LAZY, orphanRemoval = true)
	 */

	/*
	 * @OneToMany(fetch = FetchType.EAGER, mappedBy="company", cascade =
	 * CascadeType.ALL)
	 */
	// @JsonIgnoreProperties("book")

	/*
	 * @OneToMany(mappedBy = "company", cascade = { CascadeType.PERSIST,
	 * CascadeType.MERGE, CascadeType.DETACH, CascadeType.REFRESH }) private
	 * List<Vendor> vendors = new ArrayList<Vendor>();
	 */
	@JsonInclude()
	@Transient

	List<UploadFileResponse> uploadFileListDetails = new ArrayList<UploadFileResponse>();

	@JsonInclude()
	@Transient

	List<UploadFileResponse> vendorCompanyDocList = new ArrayList<UploadFileResponse>();

	@JsonInclude()
	@Transient

	Map<String, String> fileNameDomainTypeMap = new HashMap<String, String>();

	@CreationTimestamp
	private LocalDateTime createDateTime;

	@UpdateTimestamp
	private LocalDateTime updateDateTime;

	public Long getCompanyId() {
		return companyId;
	}

	public void setCompanyId(Long companyId) {
		this.companyId = companyId;
	}

	public String getCompanyName() {
		return companyName;
	}

	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}

	public String getCompanyWebsite() {
		return companyWebsite;
	}

	public void setCompanyWebsite(String companyWebsite) {
		this.companyWebsite = companyWebsite;
	}

	public Long getCompanyTurnOver() {
		return companyTurnOver;
	}

	public void setCompanyTurnOver(Long companyTurnOver) {
		this.companyTurnOver = companyTurnOver;
	}

	public Long getCompanyPhone() {
		return companyPhone;
	}

	public void setCompanyPhone(Long companyPhone) {
		this.companyPhone = companyPhone;
	}

	public Long getCompanyFax() {
		return companyFax;
	}

	public void setCompanyFax(Long companyFax) {
		this.companyFax = companyFax;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public String getTaxIdNumber() {
		return taxIdNumber;
	}

	public void setTaxIdNumber(String taxIdNumber) {
		this.taxIdNumber = taxIdNumber;
	}

	public Set<Address> getAddress() {
		return address;
	}

	public void setAddress(Set<Address> address) {
		this.address = address;
	}

	public Set<BankAccount> getBankAccount() {
		return bankAccount;
	}

	public void setBankAccount(Set<BankAccount> bankAccount) {
		this.bankAccount = bankAccount;
	}

	public Set<CompanyLegal> getCompanyLegal() {
		return companyLegal;
	}

	public void setCompanyLegal(Set<CompanyLegal> companyLegal) {
		this.companyLegal = companyLegal;
	}

	public Set<CompanyCompliance> getCompanyCompliance() {
		return companyCompliance;
	}

	public void setCompanyCompliance(Set<CompanyCompliance> companyCompliance) {
		this.companyCompliance = companyCompliance;
	}

	public Set<Vendor> getVendor() {
		return vendor;
	}

	public void setVendor(Set<Vendor> vendor) {
		this.vendor = vendor;
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

	public List<UploadFileResponse> getUploadFileListDetails() {
		return uploadFileListDetails;
	}

	public void setUploadFileListDetails(List<UploadFileResponse> uploadFileListDetails) {
		this.uploadFileListDetails = uploadFileListDetails;
	}

	public List<UploadFileResponse> getVendorCompanyDocList() {
		return vendorCompanyDocList;
	}

	public void setVendorCompanyDocList(List<UploadFileResponse> vendorCompanyDocList) {
		this.vendorCompanyDocList = vendorCompanyDocList;
	}

}