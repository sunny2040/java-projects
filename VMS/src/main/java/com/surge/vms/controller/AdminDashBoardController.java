package com.surge.vms.controller;

import java.util.ArrayList;
import java.util.List;

import org.camunda.bpm.engine.rest.dto.identity.GroupDto;
import org.camunda.bpm.engine.rest.dto.repository.ProcessDefinitionDto;
import org.camunda.bpm.engine.rest.dto.runtime.ProcessInstanceDto;
import org.camunda.bpm.engine.rest.dto.task.TaskDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import com.surge.vms.business.UserService;
import com.surge.vms.process.integrator.CamundaCustomAPIService;
import com.surge.vms.process.integrator.CamundaProcessInstanceService;

@RestController

@CrossOrigin(origins = "*", allowedHeaders = "*")
public class AdminDashBoardController {

	@Autowired
	private UserService userService;

	@Autowired
	private CamundaCustomAPIService camundaCustomAPIService;

	@Autowired
	private CamundaProcessInstanceService camundaProcessInstanceService;

	@Value("${dmsPath}")
	private String dmsBasePath;

	@GetMapping("/dashboard/{userId}")
	ResponseEntity<TaskDto[]> getCandidateGroupTaskFromCamunda(
			@PathVariable String userId) {

		/*
		 * GroupDto[] groupDtoS = userService.memberOf(userId).getBody();
		 * List<String> groupIds = new ArrayList<String>();
		 * 
		 * for(GroupDto groupDto:groupDtoS) { groupIds.add(groupDto.getId()); }
		 */

		TaskDto[] taskDtoS = camundaCustomAPIService
				.getTaskListByCandidateGroup("skilled");
		return new ResponseEntity<TaskDto[]>(taskDtoS, HttpStatus.OK);

	}

	@GetMapping("/dashboard/teamtask/{userId}")
	ResponseEntity<TaskDto[]> getTaskFromCamundaCandidateGroupList(
			@PathVariable String userId) throws Exception {

		GroupDto[] groupDtoS = userService.memberOf(userId).getBody();
		List<String> groupIds = new ArrayList<String>();

		for (GroupDto groupDto : groupDtoS) {
			groupIds.add(groupDto.getId());
		}

		TaskDto[] taskDtoS = camundaCustomAPIService
				.getTaskListByCandidateGroupList(groupIds);
		
		for(TaskDto taskDto:taskDtoS) {
			ProcessInstanceDto processInstanceDto = camundaProcessInstanceService.getProcessInstanceDto(taskDto.getProcessInstanceId());
			taskDto.setDescription(processInstanceDto.getBusinessKey());
		}
		return new ResponseEntity<TaskDto[]>(taskDtoS, HttpStatus.OK);

	}

	// Create a new Process Instance with / without Businesskey
	@PostMapping(value = { "/process-definition/key/{processName}/businessKey",
			"/process-definition/key/{processName}/businessKey/{businessKey}" })
	public ProcessInstanceDto startProcessInstance(
			@PathVariable("processName") String processName,
			@PathVariable(required = false) String businessKey) {
		return camundaProcessInstanceService.startProcessInstance(processName,
				businessKey);
	}

	// Get All ProcessDefinition
	@GetMapping(value = { "/process-definition" })
	public ProcessDefinitionDto[] getProcessDefinition() {
		return camundaProcessInstanceService.getAllProcessDefinitionName();
	}

	// Get All ProcessDefinition Name
	@GetMapping(value = { "/process-definition/name" })
	public ArrayList<String> getProcessDefinitionName() {
		ProcessDefinitionDto[] processArray = camundaProcessInstanceService
				.getAllProcessDefinitionName();
		ArrayList<String> processName = new ArrayList<String>();
		for (ProcessDefinitionDto process : processArray) {
			processName.add(process.getName());
		}
		return processName;
	}

}