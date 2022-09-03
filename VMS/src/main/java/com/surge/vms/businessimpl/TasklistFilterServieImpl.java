package com.surge.vms.businessimpl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.surge.vms.business.TasklistFilterService;
import com.surge.vms.model.TasklistFilter;
import com.surge.vms.repositories.TasklistRepository;

@Service
public class TasklistFilterServieImpl implements TasklistFilterService {

	@Autowired
	private TasklistRepository filterRepo;

	// Create a filter
	@Override
	public TasklistFilter addFilter(TasklistFilter filter) {
		TasklistFilter newfilter = filterRepo.save(filter);
		return newfilter;
	}

	// Get list of filters
	@Override
	public List<TasklistFilter> getAllFilter() {
		List<TasklistFilter> list = new ArrayList<>();
		filterRepo.findAll().forEach(e -> list.add(e));
		return list;
	}

	// Update filter by Id
	@Override
	public Optional<TasklistFilter> updateFilter(TasklistFilter tasklistFilter,
			Long filterId) {
		Optional<TasklistFilter> taskFilter = filterRepo.findById(filterId)
				.map(x -> {
					x.setFilterName(tasklistFilter.getFilterName());
					x.setFilterDescription(
							tasklistFilter.getFilterDescription());
					x.setCriteria(tasklistFilter.getCriteria());
					return filterRepo.save(x);
				});

		return taskFilter;
	}

	// Delete filter By Id
	@Override
	public void deleteFilter(Long filterId) {
		filterRepo.deleteById(filterId);
	}

}
