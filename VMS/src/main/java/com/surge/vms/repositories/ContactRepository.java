package com.surge.vms.repositories;



import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.surge.vms.model.Contact;

 
@Repository
public interface ContactRepository extends CrudRepository<Contact, Long> {
 
}


