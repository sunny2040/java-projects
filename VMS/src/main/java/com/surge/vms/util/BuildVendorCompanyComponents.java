package com.surge.vms.util;

import java.util.HashSet;
import java.util.Set;

import com.surge.vms.model.Address;
import com.surge.vms.model.BankAccount;
import com.surge.vms.model.CompanyCompliance;
import com.surge.vms.model.CompanyLegal;
import com.surge.vms.model.VendorCompany;

public class BuildVendorCompanyComponents {

	private static final String emptyString = "";

	public static Set<BankAccount> getBankAccountBuildUp() {

		BankAccount bankAccount = new BankAccount();
		bankAccount.setAccountName(emptyString);
		bankAccount.setAccountType(emptyString);
		bankAccount.setAccountNumber(000l);
		bankAccount.setRoutingNumber(0l);

		Set<BankAccount> bankAccountSet = new HashSet<BankAccount>();
		bankAccountSet.add(bankAccount);

		return bankAccountSet;

	}

	public static Set<CompanyCompliance> getCompanyComplianceBuildUp() {

		CompanyCompliance companyCompliance = new CompanyCompliance();
		companyCompliance.setInsurance(emptyString);
		companyCompliance.setOtherEntity(emptyString);
		companyCompliance.setSupplierIncl(emptyString);

		Set<CompanyCompliance> companyComplianceSet = new HashSet<CompanyCompliance>();
		companyComplianceSet.add(companyCompliance);

		return companyComplianceSet;

	}

	public static Set<CompanyLegal> getCompanyLegalBuildUp() {

		CompanyLegal companyLegal = new CompanyLegal();

		companyLegal.setDunsNumber(emptyString);
		companyLegal.setLegalEntity(emptyString);
		companyLegal.setServiceArea(emptyString);

		Set<CompanyLegal> companyLegalSet = new HashSet<CompanyLegal>();
		companyLegalSet.add(companyLegal);

		return companyLegalSet;

	}

	public static Address getAddressBuildUp(String addrType) {

		Address addr = new Address();

		addr.setAddrFirstLine(emptyString);
		addr.setAddrSecondLine(emptyString);
		addr.setCountry(emptyString);
		addr.setState(emptyString);
		addr.setCity(emptyString);
		addr.setZipCode(0);
		addr.setAddrType(addrType);
		return addr;

	}

	public static VendorCompany getVendorCompanyBuildUp(VendorCompany vendorCompany) {

		vendorCompany.getAddress().add(getAddressBuildUp("poaddr"));
		vendorCompany.getAddress().add(getAddressBuildUp("claimaddr"));
		vendorCompany.getAddress().add(getAddressBuildUp("returnaddr"));
		vendorCompany.getAddress().add(getAddressBuildUp("remitaddr"));

		vendorCompany.setBankAccount(getBankAccountBuildUp());
		vendorCompany.setCompanyCompliance(getCompanyComplianceBuildUp());
		vendorCompany.setCompanyLegal(getCompanyLegalBuildUp());

		return vendorCompany;

	}

	public static void main(String args[]) {
		BuildVendorCompanyComponents util = new BuildVendorCompanyComponents();

	}
}
