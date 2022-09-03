package com.surge.vms.repositories;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.surge.vms.model.VendorCompany;

@Repository
public interface VendorCompanyRepository extends CrudRepository<VendorCompany, Long> {

	// Get company by vendor rep email

	@Query(value = "select * from vendor_company vc, vendor v where vc.company_id=v.company_id and v.email= ?1", nativeQuery = true)
	public VendorCompany getCompanyByVendorRepEmail(String string);
	
	
	public VendorCompany getCompanyByTaxIdNumber(String taxId);
	
	

}
