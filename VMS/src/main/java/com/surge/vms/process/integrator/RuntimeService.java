package com.surge.vms.process.integrator;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.core.io.Resource;

import org.camunda.bpm.engine.rest.dto.repository.ProcessDefinitionDto;
import org.camunda.bpm.engine.rest.dto.runtime.ActivityInstanceDto;
import org.camunda.bpm.engine.rest.dto.task.AttachmentDto;
import org.camunda.bpm.engine.rest.dto.task.IdentityLinkDto;
import org.camunda.bpm.engine.rest.dto.task.TaskDto;
import org.springframework.http.ResponseEntity;

import com.surge.vms.model.User;
import com.surge.vms.model.Vendor;

public interface RuntimeService {
    ProcessDefinitionDto[] getProcessDefinitions();
    List<TaskDto> getTasks();
    public TaskDto getTaskById(String taskId);
    List<TaskDto> getTasksByAssignee(String assigneeName);
    
    ResponseEntity createUser(User newUser);
    void createUserDup(User newUser);
    
    public Map<String, Map<String,String>> getTasksVariablesByTaskId(String taskId);
    
    public String completeTask(String taskId, Vendor dataVendor);
    
    public String getBPMNXML(String url);
    
    public ActivityInstanceDto geActivityIdFromPID(String url);
    
   //Services done by Naz
    Boolean taskExist(String taskId);
    Boolean userIsIdentityLink(String taskId,String userId);
    Boolean attachmentExist(String taskID,String attachmentId);
    Boolean VariableExist(String taskId,String VarName);
    Boolean LocalVariableExist(String taskId,String VarName);
    AttachmentDto[] getAttachments(String taskID);
    void deleteAttachment(String taskId, String attachmentId);
    IdentityLinkDto[] getIdentityLinks(String taskId);
    void deleteIdentityLink(String taskId, String userId, String type);
    TaskDto[] getTasksByUserId(String userId);
    ResponseEntity<Resource> getTaskVarData(String taskId, String varName) throws IOException ;
    void deleteTaskVars(String taskId,String varname);
    List getTaskLocalVars(String taskId);
    void deleteTaskLocalVars(String taskId,String varname);
    byte[] convertResourcetoByte(ResponseEntity<Resource> res) throws IOException;
    ResponseEntity<String> postData(byte [] f, String VarName, String taskId);
    ResponseEntity claimTask(String taskId, String userId);
	ResponseEntity unclaimTask(String taskId);
	ResponseEntity resolveTask(String taskId, Map taskVars);
    ResponseEntity delegateTask(String taskId, String userId);
    ResponseEntity assignTask(String taskId, String userId, String assignerId);
    ResponseEntity deleteTask(String taskId);
    ResponseEntity<Map> getTaskFormVars(String taskId);
    
    /*check if user is authorized to assign task*/
    Boolean checkforAuthTask(String userId);
    
}
