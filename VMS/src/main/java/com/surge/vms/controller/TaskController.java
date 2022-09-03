package com.surge.vms.controller;

import java.awt.Image;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import com.surge.vms.business.UserService;
import com.surge.vms.business.VendorService;
import com.surge.vms.model.Vendor;
import com.surge.vms.process.integrator.CamundaProcessInstanceService;
import com.surge.vms.process.integrator.RuntimeService;

import org.camunda.bpm.engine.rest.dto.runtime.ActivityInstanceDto;
import org.camunda.bpm.engine.rest.dto.runtime.ProcessInstanceDto;
import org.camunda.bpm.engine.rest.dto.task.TaskDto;
import org.json.XML;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@CrossOrigin(origins = "*", allowedHeaders = "*")
@RestController

public class TaskController {
	private static final Logger logger = LoggerFactory.getLogger(TaskController.class);

	@Autowired
	private VendorService vendorService;

	@Autowired
	private RuntimeService runtimeService;

	@Autowired
	private UserService userService;
	
	@Autowired
	private CamundaProcessInstanceService camundaProcessInstanceService;

	/*
	 * @GetMapping(value = "/vendors") public List<Vendor> getAllVendors() {
	 * 
	 * System.out.println("getAllVendors...."); List<Vendor> tempVendorList =
	 * vendorService.getAllVendors();
	 * System.out.println("tempVendorList...:"+tempVendorList); return
	 * tempVendorList; }
	 */

	/*
	 * @GetMapping(value = "/employees") public List<Employee> getAllEmployees() {
	 * 
	 * System.out.println("getAllEmployees...."); List<Employee> tempEmployeeList =
	 * vendorService.getAllEmployees();
	 * System.out.println("tempVendorList...:"+tempEmployeeList);
	 * 
	 * rabbitMQSender.send(tempEmployeeList.get(0)); return tempEmployeeList; }
	 */

	/*
	 * @PostMapping("/addEmployee") public Employee addEmployee(@RequestBody
	 * Employee newEmployee) { System.out.println("addEmployee...."); Employee
	 * retEmployee = vendorService.addEmployee(newEmployee);
	 * //runtimeService.getProcessDefinitions();
	 * 
	 * 
	 * 
	 * return retEmployee; }
	 */

	@GetMapping(value = "/tasks")
	public List<TaskDto> getAllTasks() {

		System.out.println("getAllTasks....");
		List<TaskDto> taskDtos = runtimeService.getTasks();
		System.out.println("temptaskDtosList...:" + taskDtos);
		return taskDtos;
	}

	@GetMapping(value = "/task/{taskId}")
	public TaskDto getTaskById(@PathVariable String taskId) {

		System.out.println("getAllTasks....");
		TaskDto taskDto = runtimeService.getTaskById(taskId);
		System.out.println("taskDto...:" + taskDto);
		return taskDto;
	}

	@GetMapping(value = "/tasksvariables/{taskId}")
	public Map<String, Map<String, String>> getTasksVariables(@PathVariable String taskId) {

		System.out.println("getTasksVariables....");
		Map<String, Map<String, String>> retMap = runtimeService.getTasksVariablesByTaskId(taskId);
		System.out.println("getTasksVariables...:" + retMap);

		return retMap;
	}

	@GetMapping(value = "/tasksAssignee")
	public List<TaskDto> getAllTasksByAssignee(@RequestParam(value = "assignee", required = true) String assigneeName) {

		System.out.println("getAllTasksByAssignee...." + assigneeName);
		List<TaskDto> taskDtos = runtimeService.getTasksByAssignee(assigneeName);
		System.out.println("temptaskDtosList...:" + taskDtos);
		return taskDtos;
	}

	@GetMapping(value = "/getBPMNXML/{PDID}")
	public String getBPMNXML(@PathVariable String PDID) {
		String url = "process-definition/" + PDID + "/xml";
		String bpmnXML = runtimeService.getBPMNXML(url);
		return bpmnXML;
	}

	@GetMapping(value = "/getActivityId/{PID}")
	public ActivityInstanceDto getActivityIdByPID(@PathVariable String PID) {
		String url = "process-instance/" + PID + "/activity-instances";
		ActivityInstanceDto activityInstanceDto = runtimeService.geActivityIdFromPID(url);
		return activityInstanceDto;
	}

	@PostMapping(value = "/taskComplete/{taskId}")
	public String completeTask(@RequestBody Vendor dataVendor, @PathVariable String taskId) {

		System.out.println("completeTask...." + taskId);
		// dataVendor.setVendorReply("");
		// dataVendor.setApproverQuery("");

		String completeTaskStatus = runtimeService.completeTask(taskId, dataVendor);

		System.out.println("completeTaskStatus...:" + completeTaskStatus);

		return completeTaskStatus;
	}

	@GetMapping("/gettasksbyUserId/{userId}")
	public ResponseEntity getTasksByUserId(@PathVariable(name = "userId", required = true) final String userId) {

		if (userService.userExists(userId) == true) {
			
			TaskDto[] taskDtoS = runtimeService.getTasksByUserId(userId);
			
			for(TaskDto taskDto:taskDtoS) {
				ProcessInstanceDto processInstanceDto = camundaProcessInstanceService.getProcessInstanceDto(taskDto.getProcessInstanceId());
				taskDto.setDescription(processInstanceDto.getBusinessKey());
			}
			

			return ResponseEntity.ok(taskDtoS);
		}

		else {

			logger.warn("User does not exist, please check if user Id you entered is correct");
			return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body("User does not exist");

		}

	}

	@GetMapping("/taskattachment/{taskId}")
	public ResponseEntity taskAttachment(@PathVariable(value = "taskId", required = true) final String taskId) {
		if (runtimeService.taskExist(taskId)) {
			logger.warn("Task with Id found");
			return ResponseEntity.status(HttpStatus.OK).body(runtimeService.getAttachments(taskId));
		} else {
			logger.warn("Task with Id not found");
			return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body("Task with this Id does not exist");
		}

	}

	@DeleteMapping("/deleteTaskAttachment/{taskId}/{attachmentId}")
	public ResponseEntity deleteTaskAttachment(@PathVariable(value = "taskId", required = true) String taskId,
			@PathVariable(value = "attachmentId", required = true) String attachmentId) {
		if (runtimeService.taskExist(taskId)) {
			if (runtimeService.attachmentExist(taskId, attachmentId)) {
				runtimeService.deleteAttachment(taskId, attachmentId);
				return ResponseEntity.status(HttpStatus.OK).body("Attachent was successfully deleted");
			} else {
				return ResponseEntity.status(HttpStatus.OK).body("Attachent with this Id does not exist.");

			}
		}

		else {
			return ResponseEntity.status(HttpStatus.OK).body("Task with this Id does not exist.");

		}

	}

	@GetMapping("/gettaskIdentityLinks/{taskId}")
	public ResponseEntity getTasksIdentityLinks(@PathVariable(name = "taskId", required = true) final String taskId) {
		if (runtimeService.taskExist(taskId)) {
			logger.warn("Task with Id found");
			return ResponseEntity.status(HttpStatus.OK).body(runtimeService.getIdentityLinks(taskId));
		} else {
			logger.warn("Task with Id not found");
			return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body("Task with this Id does not exist");
		}

	}

	@PostMapping(value = { "/deletetaskIdentityLinks/{taskId}/{userId}/{type}",
			"/deletetaskIdentityLinks/{taskId}/{userId}" })
	public ResponseEntity deleteTaskIdentityLinks(@PathVariable(value = "taskId", required = true) String taskId,
			@PathVariable(value = "userId", required = true) String userId,
			@PathVariable(value = "type", required = false) String type) {
		if (type == null) {
			type = "";
		}

		if (runtimeService.taskExist(taskId)) {

			if (userService.userExists(userId)) {
				if (runtimeService.userIsIdentityLink(taskId, userId)) {

					runtimeService.deleteIdentityLink(taskId, userId, type);
					return ResponseEntity.status(HttpStatus.OK).body("IdentityLink was deleted successfully");
				} else {
					logger.error("User with entered ID is not an Identity Link of the task");
					return ResponseEntity.status(HttpStatus.BAD_REQUEST)
							.body("User with entered ID is not an Identity Link of the task");
				}

			}

			else {
				logger.error("User with entered ID does not exist");
				return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("User with entered ID does not exist");

			}
		}

		else {
			return ResponseEntity.status(HttpStatus.OK).body("Task with this Id does not exist.");

		}

	}

	@GetMapping("/getvardata/{taskId}/{varname}")
	public ResponseEntity<Resource> taskAttachment(@PathVariable(value = "taskId", required = true) final String taskId,
			@PathVariable(value = "varname", required = true) final String varname) throws IOException {

		if (runtimeService.taskExist(taskId)) {
			logger.warn("Task with Id found");
			return runtimeService.getTaskVarData(taskId, varname);
		} else {
			logger.warn("Task with Id not found");
			return null;
		}

	}

	@DeleteMapping("/deletetaskVars/{taskId}/{varName}")
	public ResponseEntity<String> deleteTaskVars(@PathVariable(name = "taskId", required = true) final String taskId,
			@PathVariable(name = "varName", required = true) final String varName) {
		if (runtimeService.taskExist(taskId)) {
			logger.warn("Task with Id found");
			if (runtimeService.VariableExist(taskId, varName)) {
				runtimeService.deleteTaskVars(taskId, varName);

				return ResponseEntity.status(HttpStatus.OK)
						.body("The variable with name" + varName + " was deleted successfully");
			} else {
				return ResponseEntity.status(HttpStatus.OK)
						.body("The variable with name " + varName + " does not exist");
			}
		} else {
			logger.warn("Task with Id not found");
			return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body("Task with this Id does not exist");
		}

	}

	@GetMapping("/gettaskLocalVars/{taskId}")
	public ResponseEntity getLocalTaskVars(@PathVariable(name = "taskId", required = true) final String taskId) {
		if (runtimeService.taskExist(taskId)) {
			logger.warn("Task with Id found");
			return ResponseEntity.status(HttpStatus.OK).body(runtimeService.getTaskLocalVars(taskId));
		} else {
			logger.warn("Task with Id not found");
			return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body("Task with this Id does not exist");
		}

	}

	@DeleteMapping("/deletetaskLocalVars/{taskId}/{varName}")
	public ResponseEntity<String> deleteLocalTaskVars(
			@PathVariable(name = "taskId", required = true) final String taskId,
			@PathVariable(name = "varName", required = true) final String varName) {
		if (runtimeService.taskExist(taskId)) {
			logger.warn("Task with Id found");
			if (runtimeService.LocalVariableExist(taskId, varName)) {
				runtimeService.deleteTaskLocalVars(taskId, varName);

				return ResponseEntity.status(HttpStatus.OK)
						.body("The local variable with name " + varName + " was deleted successfully");
			} else {
				return ResponseEntity.status(HttpStatus.OK)
						.body("The local variable with name" + varName + " does not exist");
			}
		} else {
			logger.warn("Task with Id not found");
			return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body("Task with this Id does not exist");
		}

	}

	@PostMapping(value = { "/claimTask/{taskId}/{userId}" })
	public ResponseEntity claimTask(@PathVariable(value = "taskId", required = true) String taskId,
			@PathVariable(value = "userId", required = true) String userId) {

		if (runtimeService.taskExist(taskId)) {

			if (userService.userExists(userId)) {

				return runtimeService.claimTask(taskId, userId);

			}

			else {
				logger.error("User with entered ID does not exist");
				return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("User with entered ID does not exist");

			}
		}

		else {
			return ResponseEntity.status(HttpStatus.OK).body("Task with this Id does not exist.");

		}

	}

	@PostMapping(value = { "/unclaimTask/{taskId}" })
	public ResponseEntity unclaimTask(@PathVariable(value = "taskId", required = true) String taskId) {

		if (runtimeService.taskExist(taskId)) {

			return runtimeService.unclaimTask(taskId);

		}

		else {
			return ResponseEntity.status(HttpStatus.OK).body("Task with this Id does not exist.");

		}

	}

	@PostMapping(value = { "/resolveTask/{taskId}" })
	public ResponseEntity resolveTask(@PathVariable(value = "taskId", required = true) String taskId) {

		if (runtimeService.taskExist(taskId)) {

			return runtimeService.resolveTask(taskId, runtimeService.getTasksVariablesByTaskId(taskId));

		}

		else {
			return ResponseEntity.status(HttpStatus.OK).body("Task with this Id does not exist.");

		}

	}

	@PostMapping(value = { "/delegateTask/{taskId}/{userId}" })
	public ResponseEntity delegateTask(@PathVariable(value = "taskId", required = true) String taskId,
			@PathVariable(value = "userId", required = true) String userId) {

		if (runtimeService.taskExist(taskId)) {

			if (userService.userExists(userId)) {

				return runtimeService.delegateTask(taskId, userId);

			}

			else {
				logger.error("User with entered ID does not exist");
				return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("User with entered ID does not exist");

			}
		}

		else {
			return ResponseEntity.status(HttpStatus.OK).body("Task with this Id does not exist.");

		}

	}

	@PostMapping(value = { "/assignTask/{taskId}/{userId}/{assignerUserId}" })
	public ResponseEntity assignTask(@PathVariable(value = "taskId", required = true) String taskId,
			@PathVariable(value = "userId", required = true) String userId,
			@PathVariable(value = "assignerUserId", required = true) String assignerUserId) {

		if (runtimeService.taskExist(taskId)) {

			if (userService.userExists(userId)) {
				if (runtimeService.checkforAuthTask(assignerUserId)) {

					return runtimeService.assignTask(taskId, userId, assignerUserId);

				} else {
					logger.error("User is not authorized");
					return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("User is not authorized");
				}

			}

			else {
				logger.error("User with entered ID does not exist");
				return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("User with entered ID does not exist");

			}
		}

		else {
			return ResponseEntity.status(HttpStatus.OK).body("Task with this Id does not exist.");

		}
	}

	@DeleteMapping(value = { "/deleteTask/{taskId}" })
	public ResponseEntity deleteTask(@PathVariable(value = "taskId", required = true) String taskId) {

		if (runtimeService.taskExist(taskId)) {

			return runtimeService.deleteTask(taskId);

		}

		else {
			return ResponseEntity.status(HttpStatus.OK).body("Task with this Id does not exist.");

		}

	}

	@GetMapping(value = { "/getTaskFormVars/{taskId}" })
	public ResponseEntity getTaskFormVars(@PathVariable(value = "taskId", required = true) String taskId) {

		if (runtimeService.taskExist(taskId)) {

			return runtimeService.getTaskFormVars(taskId);

		}

		else {
			return ResponseEntity.status(HttpStatus.OK).body("Task with this Id does not exist.");

		}

	}

	@PostMapping("/postvardata/{taskId}/{varname}")
	public ResponseEntity<String> postVarData(@RequestParam("file") MultipartFile file,
			@PathVariable(value = "taskId", required = true) final String taskId,
			@PathVariable(value = "varname", required = true) final String varname) throws IOException {

		return runtimeService.postData(file.getBytes(), varname, taskId);

	}

	@GetMapping(value = { "/getAuthorization/{userId}" })
	public Boolean checkAuth(@PathVariable(value = "userId", required = true) String userId) {
		return runtimeService.checkforAuthTask(userId);

	}

}