package com.surge.vms.process.integratorimpl;

import lombok.extern.slf4j.Slf4j;
import net.minidev.json.JSONObject;
import net.minidev.json.parser.JSONParser;
import net.minidev.json.parser.ParseException;

import org.camunda.bpm.engine.rest.dto.repository.ProcessDefinitionDto;
import org.camunda.bpm.engine.rest.dto.runtime.ActivityInstanceDto;
import org.camunda.bpm.engine.rest.dto.task.AttachmentDto;
import org.camunda.bpm.engine.rest.dto.task.IdentityLinkDto;
import org.camunda.bpm.engine.rest.dto.task.TaskDto;
import org.camunda.bpm.engine.rest.exception.InvalidRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.JsonObject;
import com.google.gson.stream.JsonReader;
import com.surge.vms.model.User;
import com.surge.vms.model.Vendor;
import com.surge.vms.process.integrator.RuntimeService;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.Map;

import org.camunda.bpm.engine.rest.dto.VariableValueDto;
import org.camunda.bpm.engine.rest.dto.authorization.AuthorizationCheckResultDto;
import org.camunda.bpm.engine.rest.dto.authorization.AuthorizationDto;
import org.camunda.bpm.engine.rest.dto.identity.UserProfileDto;
import org.apache.commons.io.IOUtils;

//import org.apache.tika.Tika;

import org.camunda.bpm.engine.rest.dto.VariableQueryParameterDto;
import org.springframework.http.HttpStatus;

@Service
@Slf4j
public class RuntimeServiceImpl implements RuntimeService {

	private final RestTemplate rest;
	// @Value("${camunda.server.rest.url}")

	private String resttempurl = "http://localhost:8080//engine-rest/process-definition/";
	private String restGetTaskurl = "http://localhost:8080//rest/task";
	private String restCreateUserUrl = "http://localhost:8088/rest/user/create";
	private String restGetTaskByAssigneeurl = "http://localhost:8080/engine-rest/task?assignee=";

	@Value("${camunda.api.basepath}")
	private String restApiBasePath;

	public RuntimeServiceImpl(RestTemplateBuilder builder) {
		this.rest = builder.build();
	}

	public ProcessDefinitionDto[] getProcessDefinitions() {

		System.out.println("resttempurl...:" + resttempurl);

		ResponseEntity<ProcessDefinitionDto[]> response = rest.getForEntity(resttempurl, ProcessDefinitionDto[].class);
		ProcessDefinitionDto[] processes = response.getBody();

		Arrays.stream(processes)
				.forEach(pd -> System.out.println("process name: " + pd.getName() + "   process id: " + pd.getId()));

		return processes;
	}

	public ResponseEntity createUser(User newUser) {

		System.out.println("restCreateUserUrl...:" + restCreateUserUrl);

		URI uri = null;
		try {
			uri = new URI(restCreateUserUrl);
		} catch (URISyntaxException e) {
			e.printStackTrace();
		}

		HttpHeaders headers = new HttpHeaders();
		headers.set("X-COM-PERSIST", "true");
		headers.set("X-COM-LOCATION", "USA");

		HttpEntity<User> request = new HttpEntity<>(newUser, headers);

		ResponseEntity<String> result = rest.postForEntity(uri, request, String.class);

		System.out.println(result);

		return result;

	}

	public void createUserDup(User newUser) {

		System.out.println(" createUserDup restCreateUserUrl...:" + restCreateUserUrl);

		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);

		try {
			URI uri = new URI(restCreateUserUrl);

			HttpEntity<User> request = new HttpEntity<>(newUser, headers);

			User createdUser = rest.postForObject(uri, request, User.class);

			// ResponseEntity createdUser = rest.exchange(restCreateUserUrl,
			// HttpMethod.POST, request, User.class);

			System.out.println(createdUser);

		} catch (URISyntaxException e) {
			e.printStackTrace();
		}

	}

	public List<TaskDto> getTasks() {

		String url = restApiBasePath + "task";

		System.out.println("restGetTaskurl...:" + url);

		ResponseEntity<TaskDto[]> response = rest.getForEntity(url, TaskDto[].class);
		TaskDto[] tasks = response.getBody();
		List<TaskDto> taskList = new ArrayList<TaskDto>();

		for (TaskDto taskDto : tasks) {

			System.out.println("task getAssignee: " + taskDto.getAssignee());
			System.out.println("task name: " + taskDto.getName());
			System.out.println("task getId: " + taskDto.getId());
			System.out.println("task getcreateddate: " + taskDto.getCreated());
			taskList.add(taskDto);
		}
		return taskList;
	}

	public TaskDto getTaskById(String taskId) {

		String url = restApiBasePath + "task/" + taskId;

		System.out.println("restGetTaskurl...:" + url);

		ResponseEntity<TaskDto> response = rest.getForEntity(url, TaskDto.class);
		TaskDto taskDto = response.getBody();

		return taskDto;
	}

	public List<TaskDto> getTasksByAssignee(String assigneeName) {

		String url = restApiBasePath + "task?assignee=" + assigneeName;

		System.out.println("restGetTaskByAssigneeurl...:" + url);

		ResponseEntity<TaskDto[]> response = rest.getForEntity(url, TaskDto[].class);
		TaskDto[] tasks = response.getBody();
		List<TaskDto> taskList = new ArrayList<TaskDto>();

		for (TaskDto taskDto : tasks) {

			System.out.println("task getAssignee: " + taskDto.getAssignee());
			System.out.println("task name: " + taskDto.getName());
			System.out.println("task getId: " + taskDto.getId());
			System.out.println("task getcreateddate: " + taskDto.getCreated());
			taskList.add(taskDto);
		}

		return taskList;

	}

	public String getBPMNXML(String feedURL) {

		String url = restApiBasePath + feedURL;
		String bpmn20XmlString = "";
		ResponseEntity<String> response = rest.getForEntity(url, String.class);
		String respString = response.getBody();
		ObjectMapper mapper = new ObjectMapper();
		Map<String, String> jsonMap = new HashMap<String, String>();

		try {

			jsonMap = mapper.readValue(respString, Map.class);
			bpmn20XmlString = (String) jsonMap.get("bpmn20Xml");

		} catch (Exception e) {
			e.printStackTrace();
		}

		return bpmn20XmlString;

	}

	public ActivityInstanceDto geActivityIdFromPID(String feedURL) {
		String url = restApiBasePath + feedURL;
		ResponseEntity<ActivityInstanceDto> response = rest.getForEntity(url, ActivityInstanceDto.class);
		ActivityInstanceDto activityInstanceDto = response.getBody();

		return activityInstanceDto;

	}

	public Map<String, Map<String, String>> getTasksVariablesByTaskId(String taskId) {

		String url = restApiBasePath + "task/" + taskId + "/variables";

		System.out.println("getTasksVariablesByTaskId url...:" + url);

		ObjectMapper mapper = new ObjectMapper();

		ResponseEntity<String> response = rest.getForEntity(url, String.class);

		String variableList = response.getBody();

		Map<String, Map<String, String>> jsonMap = new HashMap<String, Map<String, String>>();

		try {

			// convert JSON string to Map
			jsonMap = mapper.readValue(variableList, Map.class);

			System.out.println("******************");
			System.out.println(jsonMap);

		} catch (Exception e) {
			e.printStackTrace();
		}

		for (String name : jsonMap.keySet()) {

			Map childMap = (Map) jsonMap.get(name);
			childMap.remove("valueInfo");
		}

		System.out.println(jsonMap);
		return jsonMap;
	}

	public String completeTask(String taskId, Vendor dataVendor) {

		String variablesSetTaskurl = restApiBasePath + "task/" + taskId + "/variables";
		System.out.println("variablesSetTaskurl...:" + variablesSetTaskurl);
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);

		String vendorApprovalStatus = "vendorStatus";
		String vendorReply = "vendorReply";
		String approverQuery = "query";
		String emptyStr = "";

		ObjectMapper mapper = new ObjectMapper();

		String requestJson = "{\n" + "  \"modifications\": {\n" +

				"    \"" + vendorApprovalStatus + "\": { \"value\": \"" + dataVendor.getVendorStatus()
				+ "\", \"type\": \"String\" },\n" + "    \"" + approverQuery + "\": { \"value\": \""
				+ dataVendor.getApproverQuery() + "\", \"type\": \"String\" },\n" + "    \"" + vendorReply
				+ "\": { \"value\": \"" + emptyStr + "\", \"type\": \"String\" }\n" +

				"  }\n" + "}";

		Map<String, Map<String, String>> reqJsonMap = new HashMap<String, Map<String, String>>();

		try {

			// convert JSON string to Map
			reqJsonMap = mapper.readValue(requestJson, Map.class);

			System.out.println("******************");
			System.out.println(reqJsonMap);

		} catch (Exception e) {
			e.printStackTrace();
		}

		ResponseEntity<String> response = rest.postForEntity(variablesSetTaskurl, reqJsonMap, String.class);
		System.out.println(response);
		int responseStatusCode = response.getStatusCodeValue();
		String responseStatus = "";

		if (responseStatusCode == 204) {

			String completeTaskurl = restApiBasePath + "task/" + taskId + "/complete";

			System.out.println("completeTaskurl...:" + completeTaskurl);

			response = rest.postForEntity(completeTaskurl, reqJsonMap, String.class);

			int responseStatusCodeCompleteTask = response.getStatusCodeValue();

			System.out.println("completeTask responseStatus...:" + responseStatusCodeCompleteTask);

		}

		return responseStatus;
	}

	public Boolean userIsIdentityLink(String taskId, String userId) {
		ResponseEntity<IdentityLinkDto[]> response = rest
				.getForEntity(restApiBasePath + "task" + "/" + taskId + "/identity-links", IdentityLinkDto[].class);
		IdentityLinkDto[] links = response.getBody();
		Boolean userisLink = false;
		for (IdentityLinkDto link : links) {

			if (link.getUserId().equals(userId)) {
				userisLink = true;
				break;
			}

		}
		return userisLink;

	}

	public Boolean VariableExist(String taskId, String VarName) {
		ResponseEntity<Map> response = rest.getForEntity(restApiBasePath + "task/" + taskId + "/variables", Map.class);
		Map vars = response.getBody();

		List<String> list1 = new ArrayList<String>(vars.keySet());

		if (list1.contains(VarName)) {
			return true;
		} else {
			return false;
		}

	}

	public Boolean LocalVariableExist(String taskId, String VarName) {
		ResponseEntity<Map> response = rest.getForEntity(restApiBasePath + "/task/" + taskId + "/localVariables",
				Map.class);
		Map vars = response.getBody();

		List<String> list1 = new ArrayList<String>(vars.keySet());

		if (list1.contains(VarName)) {
			return true;
		} else {
			return false;
		}

	}

	public Boolean taskExist(String taskId) {

		System.out.println("Checking if task exists");

		ResponseEntity<TaskDto[]> response = rest.getForEntity(restApiBasePath + "task", TaskDto[].class);
		TaskDto[] tasks = response.getBody();
		Boolean taskexist = false;
		for (TaskDto taskDto : tasks) {

			if (taskDto.getId().equals(taskId)) {
				taskexist = true;

				break;

			}

		}

		return taskexist;
	}

	public TaskDto[] getTasksByUserId(String userId) {

		System.out.println("restGetTaskByUserId Url...:" + restApiBasePath + "task" + "?assignee=" + userId);

		ResponseEntity<TaskDto[]> response = rest.getForEntity(restApiBasePath + "task" + "?assignee=" + userId,
				TaskDto[].class);
		TaskDto[] tasks = response.getBody();

		return tasks;
	}

	public AttachmentDto[] getAttachments(String taskID) {

		System.out.println("rest get attachment url...:" + restApiBasePath + "task" + "/" + taskID + "/attachment");

		ResponseEntity<AttachmentDto[]> response = rest
				.getForEntity(restApiBasePath + "task" + "/" + taskID + "/attachment", AttachmentDto[].class);
		AttachmentDto[] attachments = response.getBody();

		return attachments;
	}

	public Boolean attachmentExist(String taskID, String attachmentId) {
		ResponseEntity<AttachmentDto[]> response = rest
				.getForEntity(restApiBasePath + "task" + "/" + taskID + "/attachment", AttachmentDto[].class);
		AttachmentDto[] attachments = response.getBody();
		Boolean attachmentexist = false;
		for (AttachmentDto attachment : attachments) {
			if (attachment.getId().contains(attachmentId)) {
				attachmentexist = true;
				break;
			}

		}
		return attachmentexist;

	}

	public void deleteAttachment(String taskId, String attachmentId) {
		try {

			rest.delete(restApiBasePath + "task/" + taskId + "/attachment/" + attachmentId);
			System.out.println("The attachment was deleted");

		} catch (RestClientException r) {
			System.out.println(r);

		}

	}

	public IdentityLinkDto[] getIdentityLinks(String taskId) {

		System.out.println("restIdentityLinksUrl...:" + restApiBasePath + "task" + "/" + taskId + "/identity-links");

		ResponseEntity<IdentityLinkDto[]> response = rest
				.getForEntity(restApiBasePath + "task" + "/" + taskId + "/identity-links", IdentityLinkDto[].class);
		IdentityLinkDto[] links = response.getBody();

		for (IdentityLinkDto link : links) {

			System.out.println("identity link userId: " + link.getUserId());
			System.out.println("identity link groudId: " + link.getGroupId());
			System.out.println("identity link type : " + link.getType());

		}

		Arrays.stream(links).forEach(link -> System.out.println("link usedId: " + link.getUserId()
				+ "   link group id: " + link.getGroupId() + " link type....:" + link.getType()));

		return links;
	}

	public void deleteIdentityLink(String taskId, String userId, String type) {
		System.out.println(
				"Calling deleteIdentityLinksUrl..." + restApiBasePath + "task/" + taskId + "/identity-links/delete");

		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		JSONObject body = new JSONObject();
		body.put("userId", userId);
		body.put("type", type);

		try {

			HttpEntity<String> request = new HttpEntity<String>(body.toString(), headers);

			rest.postForEntity(restApiBasePath + "task/" + taskId + "/identity-links/delete", request, String.class);

			System.out.println("The identity link was deleted");

		} catch (RestClientException r) {
			System.out.println(r);

		} catch (InvalidRequestException e) {
			System.out.println("Please enter type of Identity Link");
		}

	}

	public ResponseEntity<Resource> getTaskVarData(String taskId, String varName) throws IOException {

		System.out.println(
				"restGetTaskVarsUrl...:" + restApiBasePath + "task/" + taskId + "/variables/" + varName + "/data");

		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Arrays.asList(MediaType.APPLICATION_OCTET_STREAM));
			HttpEntity<String> entity = new HttpEntity<>(headers);

			ResponseEntity<Resource> response = rest.getForEntity(
					restApiBasePath + "task/" + taskId + "/variables/" + varName + "/data", Resource.class);
			Resource resource = response.getBody();
			System.out.println(resource.getFilename());
			// String type = new Tika().detect(resource.getInputStream());
			String type = "";

			// String mimeType = Magic.getMagicMatch(file, false).getMimeType();
			return ResponseEntity.status(HttpStatus.ACCEPTED).contentType(MediaType.parseMediaType(type))

					.body(resource);
		} catch (Exception e) {
			System.out.println(e);
			return ResponseEntity.status(HttpStatus.BAD_REQUEST)

					.body(null);
		}

	}

	public byte[] convertResourcetoByte(ResponseEntity<Resource> res) throws IOException {
		Resource r = res.getBody();
		InputStream in = r.getInputStream();
		byte[] byteArr = IOUtils.toByteArray(in);

		return byteArr;
	}

	public void deleteTaskVars(String taskId, String varname) {

		System.out
				.println("Calling delteTaskVarsUrl..." + restApiBasePath + "task/" + taskId + "/variables/" + varname);
		try {

			rest.delete(restApiBasePath + "task/" + taskId + "/variables/" + varname);
			System.out.println("The variable with name " + varname + " was deleted");
		} catch (RestClientException r) {
			System.out.println(r);

		}

	}

	public List getTaskLocalVars(String taskId) {

		System.out.println("restGetTaskLocalVarsUrl...:" + restApiBasePath + "task/" + taskId + "/localVariables");

		ResponseEntity<Map> response = rest.getForEntity(restApiBasePath + "task/" + taskId + "/localVariables",
				Map.class);
		Map vars = response.getBody();
		List<Map> list = new ArrayList<Map>(vars.values());

		List<String> list1 = new ArrayList<String>(vars.keySet());

		for (int i = 0; i < list.size(); i++) {
			list.get(i).put("VarName", list1.get(i));

		}

		System.out.println(list);

		return list;
	}

	public void deleteTaskLocalVars(String taskId, String varname) {
		System.out
				.println("Calling DeleteTaskLocalVarsUrl..." + restApiBasePath + "task/" + taskId + "/localVariables");
		try {

			rest.delete(restApiBasePath + "task/" + taskId + "/localVariables/" + varname);
			System.out.println("The local variable with name " + varname + " was deleted");
		} catch (RestClientException r) {
			System.out.println(r);

		}

	}

	public ResponseEntity<String> postData(byte[] f, String VarName, String taskId) {
		HttpHeaders headers = new HttpHeaders();

		headers.setContentType(MediaType.MULTIPART_FORM_DATA);
		MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
		body.add("data", f);

		HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);
		MappingJackson2HttpMessageConverter mappingJackson2HttpMessageConverter = new MappingJackson2HttpMessageConverter();
		mappingJackson2HttpMessageConverter.setSupportedMediaTypes(
				Arrays.asList(MediaType.APPLICATION_JSON, MediaType.APPLICATION_OCTET_STREAM, MediaType.IMAGE_PNG));
		rest.getMessageConverters().add(mappingJackson2HttpMessageConverter);

		String serverUrl = restApiBasePath + taskId + "/variables/" + VarName + "/data";
		ResponseEntity<String> response = rest.postForEntity(serverUrl, requestEntity, String.class);

		return response;

	}

	public ResponseEntity claimTask(String taskId, String userId) {
		System.out.println("Calling deleteIdentityLinksUrl..." + restApiBasePath + "task/" + taskId + "/claim");

		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		JSONObject body = new JSONObject();
		body.put("userId", userId);

		try {

			HttpEntity<String> request = new HttpEntity<String>(body.toString(), headers);

			rest.postForEntity(restApiBasePath + "task/" + taskId + "/claim", request, String.class);

			System.out.println("The task was claimed");

			return ResponseEntity.status(HttpStatus.OK).body("The task was claimed successfully");

		} catch (RestClientException r) {
			System.out.println(r);
			return ResponseEntity.status(HttpStatus.BAD_REQUEST)
					.body("The task is claimed by someone else, please unclaim first");

		} catch (InvalidRequestException e) {
			System.out.println("Please enter usedID to claim task");
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Please provide userID");

		}

	}

	public ResponseEntity unclaimTask(String taskId) {
		System.out.println("Calling unclaimTaskURL..." + restApiBasePath + "task/" + taskId + "/unclaim");

		try {

			rest.postForEntity(restApiBasePath + "task/" + taskId + "/unclaim", null, String.class);

			System.out.println("The task was unclaimed");

			return ResponseEntity.status(HttpStatus.OK).body("The task was unclaimed");

		} catch (RestClientException r) {
			System.out.println(r);
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("r");

		}

	}

	public ResponseEntity resolveTask(String taskId, Map taskVars) {
		System.out.println("Calling deleteIdentityLinksUrl..." + restApiBasePath + "task/" + taskId + "/resolve");

		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		JSONObject body = new JSONObject();
		body.put("variables", taskVars);

		try {

			HttpEntity<String> request = new HttpEntity<String>(body.toString(), headers);

			rest.postForEntity(restApiBasePath + "task/" + taskId + "/resolve", request, String.class);

			System.out.println("The task was resolved");

			return ResponseEntity.status(HttpStatus.OK).body("The task was resolved");

		} catch (RestClientException r) {
			System.out.println(r);
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Cannot resolve task");

		} catch (InvalidRequestException e) {
			System.out.println("Problem with fetching task variables");
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Problem with fetching task variables");

		}

	}

	public ResponseEntity delegateTask(String taskId, String userId) {
		System.out.println("Calling deleteIdentityLinksUrl..." + restApiBasePath + "task/" + taskId + "/delegate");

		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		JSONObject body = new JSONObject();
		body.put("userId", userId);

		try {

			HttpEntity<String> request = new HttpEntity<String>(body.toString(), headers);

			rest.postForEntity(restApiBasePath + "task/" + taskId + "/delegate", request, String.class);

			System.out.println("The task was delegated");

			return ResponseEntity.status(HttpStatus.OK).body("The task was delegated successfully");

		} catch (RestClientException r) {
			System.out.println(r);
			return ResponseEntity.status(HttpStatus.BAD_REQUEST)
					.body("Cannot delegate task, either you're not in the list of users"
							+ " for that task or you do not have admin powers");

		} catch (InvalidRequestException e) {
			System.out.println("Please enter usedID to delegate task");
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Please provide userID");

		}

	}

	public ResponseEntity assignTask(String taskId, String userId, String assignerUserId) {
		System.out.println("Calling deleteIdentityLinksUrl..." + restApiBasePath + "task/" + taskId + "/assignee");

		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		JSONObject body = new JSONObject();
		body.put("userId", userId);

		try {

			HttpEntity<String> request = new HttpEntity<String>(body.toString(), headers);

			rest.postForEntity(restApiBasePath + "task/" + taskId + "/assignee", request, String.class);

			System.out.println("The task was assigned");

			return ResponseEntity.status(HttpStatus.OK).body("The task was assigned successfully");

		} catch (RestClientException r) {
			System.out.println(r);
			return ResponseEntity.status(HttpStatus.BAD_REQUEST)
					.body("Cannot assign task, either you're not in the list of users"
							+ " for that task or you do not have admin powers");

		} catch (InvalidRequestException e) {
			System.out.println("Please enter usedID to assign task");
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Please provide userID");

		}

	}

	public ResponseEntity deleteTask(String taskId) {
		System.out.println("Calling deleteTaskURL..." + restApiBasePath + "task/" + taskId + "/delete");

		try {

			rest.delete(restApiBasePath + "task/" + taskId);

			System.out.println("The task was deleted");

			return ResponseEntity.status(HttpStatus.OK).body("The task was deleted");

		} catch (RestClientException r) {
			System.out.println(r);
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("r");

		}

	}

	public ResponseEntity<Map> getTaskFormVars(String taskId) {
		System.out.println("Calling getTaskFormVarsURL..." + restApiBasePath + "task/" + taskId + "/unclaim");

		try {

			ResponseEntity<Map> response = rest.getForEntity(restApiBasePath + "task/" + taskId + "/form-variables",
					Map.class);

			System.out.println("The task form variables were fetched successfully");

			return ResponseEntity.status(HttpStatus.OK).body(response.getBody());

		} catch (RestClientException r) {
			System.out.println(r);
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);

		}

	}

//	public Boolean checkforAdmin(String userId) {
//		System.out.println(restApiBasePath+"authorization/check?permissionName=ALL,permissionValue=2147483647,"
//				+ "resourceName="+"USER"
//				+ "resourceType=7,resourceId="+userId);
//		AuthorizationCheckResultDto response= rest.getForObject(restApiBasePath+"authorization/check?permissionName=ALL&permissionValue=2147483647&"
//				+ "resourceName="+"USER"
//				+ "&resourceType=7&resourceId="+userId, AuthorizationCheckResultDto.class);
//		
//		
//		if(response.isAuthorized()) {
//			return true;
//			
//		}
//		return false;
//		
//	}

	public Boolean checkforAuthTask(String userId) {
		System.out.println(restApiBasePath + "authorization?userId=" + userId);
		ResponseEntity<List> response = rest.getForEntity(restApiBasePath + "authorization", List.class);
		List<Map> res = response.getBody();
		System.out.println(res);
		Boolean ret = false;

		for (Map m : res) {
			System.out.println(m.values());
			String val = String.valueOf(m.get("permissions"));
			String user = String.valueOf(m.get("userId"));
			if ((val.equals("[ALL]") || val.equals("[TASK_ASSIGN]")) && user.equals(userId)) {
				ret = true;
			}

		}
		return ret;

	}

}
