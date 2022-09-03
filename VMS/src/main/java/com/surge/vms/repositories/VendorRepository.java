package com.surge.vms.repositories;



import java.util.List;
import java.util.Optional;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.surge.vms.model.Vendor;
 
 
@Repository
public interface VendorRepository extends CrudRepository<Vendor, Long> {
	
	List<Vendor> findByVendorStatusEquals(String vendorStatus);
	
	Optional<Vendor> findByProcessInstanceIdEquals(String processInsId);
	
	Optional<Vendor> findByIdAndProcessInstanceId(Long vendorId, String pId);
	
	Optional<Vendor> findByProcessInstanceId(String pId);
	
	Optional<Vendor> findByEmail(String email);
 
}


