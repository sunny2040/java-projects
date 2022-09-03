package com.surge.vms.repositories;



import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.surge.vms.model.State;

 
@Repository
public interface StateRepository extends CrudRepository<State, Long> {
	
	List<State> findByCountryCodeEquals(String countryCode);
 
}


