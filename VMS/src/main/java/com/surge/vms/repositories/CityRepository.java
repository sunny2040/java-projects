package com.surge.vms.repositories;



import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.surge.vms.model.City;

 
@Repository
public interface CityRepository extends CrudRepository<City, Long> {
	
	List<City> findByStateCodeEquals(String stateCode);
 
}


