package com.surge.vms.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.surge.vms.model.TasklistFilter;

@Repository
public interface TasklistRepository
		extends CrudRepository<TasklistFilter, Long> {

}
