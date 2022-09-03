package com.surge.vms.repositories;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.surge.vms.model.ZipCode;

@Repository
public interface ZipCodeRepository extends CrudRepository<ZipCode, Long> {

	List<ZipCode> findByCityCodeEquals(String cityCode);

}
