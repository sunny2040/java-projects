package com.surge.vms.process.integrator;

import java.util.List;

import org.camunda.bpm.engine.rest.dto.task.TaskDto;

public interface CamundaCustomAPIService {

	public void callCamundaCustomRestApi();
	
	public TaskDto[] getTaskListByCandidateGroup(String candidateGroup);
	
	public TaskDto[] getTaskListByCandidateGroupList(List candidateGroupIds) throws Exception ;

	
	
}


