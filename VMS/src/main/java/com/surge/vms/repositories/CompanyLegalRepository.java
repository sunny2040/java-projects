package com.surge.vms.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.surge.vms.model.CompanyLegal;

@Repository
public interface CompanyLegalRepository extends CrudRepository<CompanyLegal, Long> {

}
