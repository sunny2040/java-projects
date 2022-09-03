package com.surge.vms.dto;

import java.util.Map;

import com.surge.vms.model.Address;
import com.surge.vms.model.BankAccount;

public class VendorCompanyAddrDTO {

	private Map<String, Address> addladdr;

	public Map<String, Address> getAddladdr() {
		return addladdr;
	}

	public void setAddladdr(Map<String, Address> addladdr) {
		this.addladdr = addladdr;
	}
	

}
