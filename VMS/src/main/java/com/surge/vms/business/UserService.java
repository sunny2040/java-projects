package com.surge.vms.business;

import java.util.List;
import java.util.Map;

import org.camunda.bpm.engine.rest.dto.identity.GroupDto;
import org.springframework.http.ResponseEntity;

import com.surge.vms.exception.ResourceNotFoundException;
import com.surge.vms.model.User;
//import com.surge.vms.model.UserDomain;
import com.surge.vms.model.UserDomain;

public interface UserService {
	/*Check if user exists in Camunda*/
	public Boolean userExists(String userId);
    
	/*get All users from database repo*/
	public List<UserDomain> getAllUser();
	
	/*get all users from Camunda*/
	public  List<User> getUsers();
	
	/*Update user profile in Camunda*/
	public ResponseEntity<String> updateUserProfileCamunda(String userId,User user) ;
	
	/*delete User from Camunda*/
	public ResponseEntity deleteUser(String userId);
    
	/*add user to data base repo*/
	public UserDomain addUser(UserDomain userDomain);
    
	/*line 33-52 services manipulating user data in database repo, not in camunda*/
	public UserDomain getUserById(long id) throws ResourceNotFoundException;

	public User getUserByUserId(String id) throws ResourceNotFoundException;

	public Map<String, Boolean> deleteUserById(long id) throws ResourceNotFoundException;

	User updateUserById(long id, User userDetails) throws ResourceNotFoundException;

	User updateUserByUserId(String id, User userDetails) throws ResourceNotFoundException;

	public UserDomain getUserByUserLN(String userLN) throws ResourceNotFoundException;

	public UserDomain getUserByUserEmail(String userEmail) throws ResourceNotFoundException;

	public UserDomain getUserByUserFN(String userFn) throws ResourceNotFoundException;

	public UserDomain getUserByUserPwd(String userPwd) throws ResourceNotFoundException;
	
	public UserDomain updateUserDomainById(UserDomain userDetails) throws ResourceNotFoundException;
	
	public ResponseEntity<User> createUser(User newUser);

	
	/*get Group by Id Camunda*/
	public ResponseEntity<GroupDto[]> getGroupById(String groupId);
	
	/*Checking if user in group Camunda*/
	public Boolean isUserinGroup(String groupId, String userId);
	
	/*Getting group list where user is already a member*/
	public ResponseEntity<GroupDto[]> memberOf(String userId);
	
	/*adding user to a group*/
	public ResponseEntity<String> addUsertoMulGroups(String userId, String groupId);
}
