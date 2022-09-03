package com.surge.vms.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.surge.vms.model.BankAccount;

@Repository
public interface BankAccountRepository extends CrudRepository<BankAccount, Long> {

}
