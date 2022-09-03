package com.surge.vms.repositories;



import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.surge.vms.model.Address;

 
@Repository
public interface AddressRepository extends CrudRepository<Address, Long> {
 
}


