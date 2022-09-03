package com.surge.vms.dto;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.Transient;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.surge.vms.model.Address;
import com.surge.vms.model.BankAccount;

public class VendorCompanyBankAccountDTO {

	private Address address;
	private BankAccount bankAccount;
	Map<String, String> fileNameDomainTypeMap = new HashMap<String, String>();
	

	public Address getAddress() {
		return address;
	}

	public void setAddress(Address address) {
		this.address = address;
	}

	public BankAccount getBankAccount() {
		return bankAccount;
	}

	public void setBankAccount(BankAccount bankAccount) {
		this.bankAccount = bankAccount;
	}

	public Map<String, String> getFileNameDomainTypeMap() {
		return fileNameDomainTypeMap;
	}

	public void setFileNameDomainTypeMap(Map<String, String> fileNameDomainTypeMap) {
		this.fileNameDomainTypeMap = fileNameDomainTypeMap;
	}




	

}
