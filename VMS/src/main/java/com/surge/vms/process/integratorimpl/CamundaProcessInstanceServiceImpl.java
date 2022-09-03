package com.surge.vms.process.integratorimpl;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.camunda.bpm.engine.rest.dto.repository.ProcessDefinitionDto;
import org.camunda.bpm.engine.rest.dto.runtime.ProcessInstanceDto;
import org.camunda.bpm.engine.rest.dto.task.TaskDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.surge.vms.business.DocumentService;
import com.surge.vms.dto.DocumentDTO;
import com.surge.vms.model.Document;
import com.surge.vms.model.Vendor;
import com.surge.vms.model.VendorCompany;
import com.surge.vms.process.integrator.CamundaProcessInstanceService;
import com.surge.vms.util.FileUtil;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class CamundaProcessInstanceServiceImpl
		implements CamundaProcessInstanceService {

	private RestTemplate restTemplate;

	@Autowired
	private DocumentService documentService;

	private String reststartPIurl = "process-definition/key/";

	private String restPostPIVariableurl = "process-instance/";

	@Value("${camunda.api.basepath}")
	private String restApiBasePath;

	public CamundaProcessInstanceServiceImpl() {

		this.restTemplate = new RestTemplate();

		// Add the Jackson and String message converters
		restTemplate.getMessageConverters()
				.add(new MappingJackson2HttpMessageConverter());
		restTemplate.getMessageConverters()
				.add(new StringHttpMessageConverter());

	}

	public ProcessInstanceDto startProcessDefinitions(String processName, String businessKey) {

		String requestUrl = restApiBasePath + reststartPIurl + processName
				+ "/start";
		System.out.println("requestUrl...:" + requestUrl);

// Set the Content-Type header
		HttpHeaders requestHeaders = new HttpHeaders();
		requestHeaders.setContentType(new MediaType("application", "json"));

		String jsonString = "{\"businessKey\" : \""+ businessKey +"\"}";

		HttpEntity requestEntity = new HttpEntity(jsonString, requestHeaders);

		// ResponseEntity<ProcessInstanceDto> responseEntity =
		// restTemplate.exchange(requestUrl, HttpMethod.POST, requestEntity,
		// ProcessInstanceDto.class);

		ResponseEntity<ProcessInstanceDto> response = restTemplate
				.postForEntity(requestUrl, requestEntity,
						ProcessInstanceDto.class);

		ProcessInstanceDto pid = response.getBody();
		System.out.println("Process InsId:" + pid.getId());

		return pid;
	}

	// Get ALL process definitions lastest version
	public ProcessDefinitionDto[] getAllProcessDefinitionName() {
		String requestUrl = restApiBasePath
				+ "process-definition?latestVersion=true";
		System.out.println("requestURL ....: " + requestUrl);

		// Set the Content-Type header
		HttpHeaders requestHeaders = new HttpHeaders();
		requestHeaders.setContentType(new MediaType("application", "json"));

		HttpEntity requestEntity = new HttpEntity(requestHeaders);
		ResponseEntity<ProcessDefinitionDto[]> response = restTemplate
				.getForEntity(requestUrl, ProcessDefinitionDto[].class);
		ProcessDefinitionDto[] processDef = response.getBody();

		return processDef;
	}

	// Start process Instance with or without business key
	public ProcessInstanceDto startProcessInstance(String processName,
			String businessKey) {

		String requestUrl = restApiBasePath + reststartPIurl + processName
				+ "/start";
		System.out.println("requestUrl...:" + requestUrl);

		// Set the Content-Type header
		HttpHeaders requestHeaders = new HttpHeaders();
		requestHeaders.setContentType(new MediaType("application", "json"));

		if (businessKey == null) {
			String jsonString = "{\"businessKey\" : \"" + businessKey + "\"}";
			HttpEntity requestEntity = new HttpEntity(jsonString,
					requestHeaders);
			ResponseEntity<ProcessInstanceDto> response = restTemplate
					.postForEntity(requestUrl, requestEntity,
							ProcessInstanceDto.class);
			ProcessInstanceDto pid = response.getBody();
			System.out.println("Process InsId:" + pid.getId());
			return pid;
		} else {
			String jsonString = "{\"businessKey\" : \"" + businessKey + "\"}";
			HttpEntity requestEntity = new HttpEntity(jsonString,
					requestHeaders);
			ResponseEntity<ProcessInstanceDto> response = restTemplate
					.postForEntity(requestUrl, requestEntity,
							ProcessInstanceDto.class);

			ProcessInstanceDto pid = response.getBody();
			System.out.println("Process InsId:" + pid.getId());
			return pid;
		}
	}

	public int postCamundaVariable(String pId, String variableName,
			String variableValue) {

		// String url =
		// "http://localhost:8080/engine-rest/process-instance/"+pId+"/variables";

		String url = restApiBasePath + restPostPIVariableurl + pId
				+ "/variables";
		System.out.println("restPostPIVariableurl...:" + url);

		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);

		ObjectMapper mapper = new ObjectMapper();

		String requestJson = "{\n" + "  \"modifications\": {\n" + "    \""
				+ variableName + "\": { \"value\": \"" + variableValue
				+ "\", \"type\": \"String\" }\n" + "  }\n" + "}";

		Map<String, Map<String, Map<String, String>>> jsonMap = new HashMap<String, Map<String, Map<String, String>>>();

		try {

			// convert JSON string to Map
			jsonMap = mapper.readValue(requestJson, Map.class);

			System.out.println("******************");
			System.out.println(jsonMap);

		} catch (Exception e) {
			e.printStackTrace();
		}
		ResponseEntity<String> response = restTemplate.postForEntity(url,
				jsonMap, String.class);
		System.out.println(response);
		int statusCode = response.getStatusCodeValue();
		return statusCode;
	}

	public int postVendorDataToWorkflow(String pId, Vendor vendor,
			VendorCompany vendorCompany) {

		// String url =
		// "http://localhost:8080/engine-rest/process-instance/"+pId+"/variables";

		String url = restApiBasePath + restPostPIVariableurl + pId
				+ "/variables";
		System.out.println("restPostPIVariableurl...:" + url);

		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		vendor.setVendorReply("");
		vendor.setApproverQuery("");

		String vendorCompanyName = "vendorCompanyName";
		String vendorCompanyWebsite = "vendorCompanyWebsite";
		String vendorCompanyPhone = "vendorCompanyPhone";
		String vendorCompanyTurnOver = "vendorCompanyTurnOver";
		String taxIdNumber = "taxIdNumber";
		String dunsNumber = "dunsNumber";
		String legalEntity = "legalEntity";
		String firstName = "firstName";
		String lastName = "lastName";
		String email = "email";
		String serviceArea = "serviceArea";
		String vendorApprovalStatus = "vendorStatus";
		String vendorReply = "vendorReply";
		String approverQuery = "approverQuery";
		String emptyStr = "";

		ObjectMapper mapper = new ObjectMapper();

		String requestJson = "{\n" + "  \"modifications\": {\n" +

				"    \"" + vendorCompanyName + "\": { \"value\": \""
				+ vendorCompany.getCompanyName()
				+ "\", \"type\": \"String\" },\n" + "    \""
				+ vendorCompanyWebsite + "\": { \"value\": \""
				+ vendorCompany.getCompanyWebsite()
				+ "\", \"type\": \"String\" },\n" + "    \""
				+ vendorCompanyPhone + "\": { \"value\": \""
				+ vendorCompany.getCompanyPhone()
				+ "\", \"type\": \"String\" },\n" + "    \""
				+ vendorCompanyTurnOver + "\": { \"value\": \""
				+ vendorCompany.getCompanyTurnOver()
				+ "\", \"type\": \"String\" },\n" + "    \"" + taxIdNumber
				+ "\": { \"value\": \"" + vendorCompany.getTaxIdNumber()
				+ "\", \"type\": \"String\" },\n" + "    \"" + dunsNumber
				+ "\": { \"value\": \"" + vendorCompany.getCompanyLegal()
						.stream().findFirst().get().getDunsNumber()
				+ "\", \"type\": \"String\" },\n" + "    \"" + legalEntity
				+ "\": { \"value\": \"" + vendorCompany.getCompanyLegal()
						.stream().findFirst().get().getLegalEntity()
				+ "\", \"type\": \"String\" },\n" +

				"    \"" + firstName + "\": { \"value\": \""
				+ vendor.getFirstName() + "\", \"type\": \"String\" },\n"
				+ "    \"" + lastName + "\": { \"value\": \""
				+ vendor.getLastName() + "\", \"type\": \"String\" },\n"
				+ "    \"" + email + "\": { \"value\": \"" + vendor.getEmail()
				+ "\", \"type\": \"String\" },\n" + "    \""
				+ vendorApprovalStatus + "\": { \"value\": \"" + emptyStr
				+ "\", \"type\": \"String\" },\n" + "    \"" + approverQuery
				+ "\": { \"value\": \"" + vendor.getApproverQuery()
				+ "\", \"type\": \"String\" },\n" + "    \"" + vendorReply
				+ "\": { \"value\": \"" + vendor.getVendorReply()
				+ "\", \"type\": \"String\" },\n" + "    \"" + serviceArea
				+ "\": { \"value\": \"" + "serviceareareplaced"
				+ "\", \"type\": \"String\" }\n" + "  }\n" + "}";

		System.out.println("requestJson...:" + requestJson);

		Map<String, Map<String, Map<String, String>>> jsonMap = new HashMap<String, Map<String, Map<String, String>>>();

		try {

			// convert JSON string to Map
			jsonMap = mapper.readValue(requestJson, Map.class);

			System.out.println("******************");
			System.out.println(jsonMap);

		} catch (Exception e) {
			e.printStackTrace();
		}
		ResponseEntity<String> response = restTemplate.postForEntity(url,
				jsonMap, String.class);
		System.out.println(response);
		int statusCode = response.getStatusCodeValue();
		return statusCode;
	}

	public int postMessageListenerTriggee(String msgListenerName,
			String bussinessKey) {

		// String url =
		// "http://localhost:8080/engine-rest/process-instance/"+pId+"/variables";

		String url = restApiBasePath + "/message";
		System.out.println("postMessageListenerTriggee...:" + url);

		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);

		Map<String, String> reqJsonMap = new HashMap<String, String>();
		reqJsonMap.put("messageName", msgListenerName);
		reqJsonMap.put("businessKey", bussinessKey);
		System.out.println(reqJsonMap);
		ResponseEntity<String> response = restTemplate.postForEntity(url,
				reqJsonMap, String.class);
		System.out.println(response);
		int statusCode = response.getStatusCodeValue();
		return statusCode;
	}

	public int postDocToWorkflow(String pId, VendorCompany vendorCompany)
			throws IOException {

		List<Document> docList = documentService
				.getDocumentByOwnerId(vendorCompany.getTaxIdNumber());
		int statusCode = -1;
		if (null != docList && docList.size() > 0) {

			for (Document doc : docList) {
				String filePath = doc.getDocPath();
				DocumentDTO documentDTO = new FileUtil()
						.encodeFileFromPath(filePath);
				statusCode = postFilesToWorkflow(pId, documentDTO);

			}

		}

		return statusCode;
	}

	public int postFilesToWorkflow(String pId, DocumentDTO documentDTO) {

		String url = restApiBasePath + restPostPIVariableurl + pId
				+ "/variables";
		System.out.println("restPostPIVariableurl...:" + url);

		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);

		ObjectMapper mapper = new ObjectMapper();

		String requestJson = "{\n" + "  \"modifications\": {\n" + "    \""
				+ documentDTO.getDocName() + "\": {\n"
				+ "      \"type\": \"File\",\n" + "      \"valueInfo\": {\n"
				+ "        \"filename\": \"" + documentDTO.getDocName()
				+ "\",\n" + "        \"mimetype\": \""
				+ documentDTO.getDocMimeType() + "\",\n"
				+ "        \"encoding\": \"Base64\"\n" + "      },\n"
				+ "      \"value\": \"" + documentDTO.getDocEncodedContent()
				+ "\"\n" + "    }\n" + "  }\n" + "}";

		System.out.println(requestJson);

		Map<String, Map<String, Map<String, String>>> jsonMap = new HashMap<String, Map<String, Map<String, String>>>();

		try {

			// convert JSON string to Map
			jsonMap = mapper.readValue(requestJson, Map.class);

			System.out.println("******************");
			System.out.println(jsonMap);

		} catch (Exception e) {
			e.printStackTrace();
		}
		ResponseEntity<String> response = restTemplate.postForEntity(url,
				jsonMap, String.class);
		System.out.println(response);
		int statusCode = response.getStatusCodeValue();
		return statusCode;
	}

	public void callCamundaCustomRestApi() {
		System.out.println("callCamundaCustomRestApi");
		String url = restApiBasePath + "/task/candidategroups/skilled";
		ResponseEntity<TaskDto[]> response = restTemplate.getForEntity(url,
				TaskDto[].class);
		System.out.println("callCamundaCustomRestApi");
	}
	
	public ProcessInstanceDto getProcessInstanceDto(String processInstanceId) {
		
		String url = restApiBasePath + "/process-instance/" + processInstanceId;

		System.out.println("restGetProcessInstanceDtoURL...:" + url);
		
		ResponseEntity<ProcessInstanceDto> response = restTemplate.getForEntity(url, ProcessInstanceDto.class);
		
		return response.getBody();
		
	}

}
