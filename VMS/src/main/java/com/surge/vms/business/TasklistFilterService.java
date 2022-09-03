package com.surge.vms.business;

import java.util.List;
import java.util.Optional;

import com.surge.vms.model.TasklistFilter;

public interface TasklistFilterService {

	// Create new filter
	public TasklistFilter addFilter(TasklistFilter tasklistFilter);

	// Get All Filter
	public List<TasklistFilter> getAllFilter();

	// Update Filter
	public Optional<TasklistFilter> updateFilter(TasklistFilter tasklistFilter,
			Long filterId);

	// Delete Filter
	public void deleteFilter(Long filterId);

}
