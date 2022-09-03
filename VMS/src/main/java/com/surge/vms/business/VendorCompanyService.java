package com.surge.vms.business;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import com.surge.vms.model.Address;
import com.surge.vms.model.BankAccount;
import com.surge.vms.model.Vendor;
import com.surge.vms.model.VendorCompany;
import com.surge.vms.model.CompanyCompliance;
import com.surge.vms.model.CompanyLegal;

public interface VendorCompanyService {
	
	
	public List<VendorCompany> getAllVendorCompany();
	
	public Optional<VendorCompany> getVendorCompanyByID(Long companyId);
	
	public VendorCompany addVendorCompany(VendorCompany newCompany);
	
	public Optional<VendorCompany> updateVendorCompanyBasic(VendorCompany updCompanyVendor,Long vendorCompId);
	
	public Optional<VendorCompany> updateVendorCompanyVendor(Long vendorCompId, Vendor vendor);
	
	public void deleteVendorCompanyById(Long companyId);
	
	public List<VendorCompany> getAllVendorCompanyByStatus(String status);
	
	public VendorCompany getVendorCompanyByVendorRepEmail(String email);
	
	public Optional<VendorCompany> updateVendorCompanyBankAccount(BankAccount bankAccount, Address remitAddress, Long vendorCompId);
	
	public Optional<VendorCompany> updateVendorCompanyCompliance(CompanyCompliance updCompanyCompliance, Long vendorCompId);
	
	public Optional<VendorCompany> updateVendorCompanyLegal(CompanyLegal updCompanyLegalSet, Long vendorCompId);
	
	public Optional<VendorCompany> updateVendorCompanyAddlAddr(Map<String, Address> addlAddrMap, Long vendorCompId);
	
	public VendorCompany getVendorCompanyByTaxIdNumber(String taxId);
	
	public VendorCompany updateVendorRepPwdByEmail(String email, String pwd);
	
	public void updateVendorReplyByVendorCompTaxId(String vendorCompTaxId, String vendorNewStatus, String vendorReply);
	
	
	

	
	
	
}
