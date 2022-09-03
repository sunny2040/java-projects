package com.surge.vms.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.surge.vms.business.TasklistFilterService;
import com.surge.vms.model.TasklistFilter;

@RestController
@Validated
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class TasklistFilterController {

	@Autowired
	private TasklistFilterService filterService;

	@PostMapping(value = "/dashBoardTaskFilter")
	public TasklistFilter createFilter(@RequestBody TasklistFilter filter) {
		TasklistFilter newFilter = filterService.addFilter(filter);
		return newFilter;
	}

	@GetMapping(value = "/dashBoardTaskFilter")
	public List<TasklistFilter> getAllFilter() {
		List<TasklistFilter> list = filterService.getAllFilter();
		return list;
	}

}
