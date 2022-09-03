package com.surge.vms.repositories;



import java.util.Optional;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.surge.vms.model.Country;

 
@Repository
public interface CountryRepository extends CrudRepository<Country, Long> {
	
	Optional<Country> findByCountryCode(String countryCode);
 
}


