package com.surge.vms.process.integrator;

import java.io.IOException;

import org.camunda.bpm.engine.rest.dto.repository.ProcessDefinitionDto;
import org.camunda.bpm.engine.rest.dto.runtime.ProcessInstanceDto;

import com.surge.vms.model.Vendor;
import com.surge.vms.model.VendorCompany;

public interface CamundaProcessInstanceService {

	public ProcessInstanceDto startProcessDefinitions(String processName, String businessKey);

	public int postCamundaVariable(String pId, String variableName,
			String variableValue);

	public int postMessageListenerTriggee(String msgListenerName,
			String bussinessKey);

	public int postVendorDataToWorkflow(String pId, Vendor vendor,
			VendorCompany vendorCompany);

	public int postDocToWorkflow(String pId, VendorCompany vendorCompany)
			throws IOException;

	public void callCamundaCustomRestApi();

	public ProcessInstanceDto startProcessInstance(String processName,
			String businessKey);
	
	public ProcessInstanceDto getProcessInstanceDto(String processInstanceId);

	public ProcessDefinitionDto[] getAllProcessDefinitionName();

}
