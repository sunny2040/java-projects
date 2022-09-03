package com.surge.vms.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.surge.vms.model.CompanyCompliance;

@Repository
public interface CompanyComplianceRepository extends CrudRepository<CompanyCompliance, Long> {

}
