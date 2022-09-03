package com.surge.vms.process.integratorimpl;

import lombok.extern.slf4j.Slf4j;
import net.minidev.json.JSONObject;

import org.camunda.bpm.engine.rest.dto.runtime.ProcessInstanceDto;
import org.camunda.bpm.engine.rest.dto.task.TaskDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.surge.vms.business.DocumentService;
import com.surge.vms.dto.DocumentDTO;
import com.surge.vms.model.Document;
import com.surge.vms.model.Vendor;
import com.surge.vms.model.VendorCompany;
import com.surge.vms.process.integrator.CamundaCustomAPIService;
import com.surge.vms.process.integrator.CamundaProcessInstanceService;
import com.surge.vms.util.FileUtil;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class CamundaCustomAPIServiceImpl implements CamundaCustomAPIService {

	private RestTemplate restTemplate;



	@Value("${camunda.custom.api.basepath}")
	private String restCustomApiBasePath;

	public CamundaCustomAPIServiceImpl() {

		this.restTemplate = new RestTemplate();

		restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
		restTemplate.getMessageConverters().add(new StringHttpMessageConverter());

	}


	public void callCamundaCustomRestApi() {
		System.out.println("callCamundaCustomRestApi");
		String url = restCustomApiBasePath + "/task/candidategroups/skilled";
		ResponseEntity<TaskDto[]> response = restTemplate.getForEntity(url, TaskDto[].class);
		System.out.println("callCamundaCustomRestApi");
	}
	
	public TaskDto[] getTaskListByCandidateGroup(String candidateGroup) {

		List<String> candidateGrpList = new ArrayList<String>();
		
		String url = restCustomApiBasePath + "/task/candidategroup/"+candidateGroup;

		candidateGrpList.add(candidateGroup);

		ResponseEntity<TaskDto[]> response = restTemplate.getForEntity(url, TaskDto[].class);

		return response.getBody();

	}
	
	public TaskDto[] getTaskListByCandidateGroupList(List candidateGroupIds) throws Exception {

		ObjectMapper mapper = new ObjectMapper();
		String jsondata = mapper.writeValueAsString(candidateGroupIds);
		
		String url = restCustomApiBasePath + "task/candidategroupList/";

	    HttpHeaders headers  = new HttpHeaders();
	    headers.setContentType(MediaType.APPLICATION_JSON);

	    Map<String, List> params = new HashMap<String, List>();
	    params.put("candidateGroupIds",candidateGroupIds);
	    
        UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromHttpUrl(url)
                .queryParam("candidateGroupIds", candidateGroupIds);
	    
	    HttpEntity<String> httpEntity = 
			      new HttpEntity<String>(headers);
	   	

		ResponseEntity<TaskDto[]> response = restTemplate.exchange(uriBuilder.toUriString(), HttpMethod.GET, httpEntity,TaskDto[].class);

		return response.getBody();

	}
	

}
