package com.surge.vms.businessimpl;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.camunda.bpm.engine.rest.dto.identity.GroupDto;
import org.camunda.bpm.engine.rest.dto.identity.UserDto;
import org.camunda.bpm.engine.rest.dto.identity.UserProfileDto;
import org.camunda.bpm.engine.rest.dto.task.TaskDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import com.surge.vms.business.UserService;
import com.surge.vms.exception.ResourceNotFoundException;
import com.surge.vms.model.Credentials;
import com.surge.vms.model.Profile;
import com.surge.vms.model.User;
import com.surge.vms.model.UserDomain;
import com.surge.vms.repositories.UserRepository;

@Service
public class UserServiceImplement implements UserService {

	@Autowired
	private UserRepository userRepository;
	@Value("${camunda.api.basepath}")
	private String restPortUrl;
	private final RestTemplate rest;

	public UserServiceImplement(RestTemplateBuilder builder) {
		this.rest = builder.build();
	}

	@Override
	public List<UserDomain> getAllUser() {
		return userRepository.findAll();
	}

	public Boolean userExists(String userId) {
		System.out.println("Checking if User exists...");
		System.out.println("Calling UserUrl..." + restPortUrl + "/user");
		Boolean userExist = false;
		ResponseEntity<UserProfileDto[]> usercheck = rest.getForEntity(restPortUrl + "user", UserProfileDto[].class);
		UserProfileDto[] users = usercheck.getBody();
		for (UserProfileDto user : users) {
			if (user.getId().equals(userId)) {

				userExist = true;
				break;
			}
		}
		return userExist;
	}
	public ResponseEntity<User> createUser(User newUser) {

		String userCreateUrl =  restPortUrl + "user/create";
		
		System.out.println("userCreateUrl...:" + userCreateUrl);

		URI uri = null;
		try {
			uri = new URI(userCreateUrl);
		} catch (URISyntaxException e) {
			e.printStackTrace();
		}

		HttpHeaders headers = new HttpHeaders();
		headers.set("X-COM-PERSIST", "true");
		headers.set("X-COM-LOCATION", "USA");

		HttpEntity<User> request = new HttpEntity<>(newUser, headers);

		ResponseEntity<User> result = rest.postForEntity(uri, request, User.class);


		return result;

	}
	public List<User> getUsers() {

		List<User> response = rest.getForObject(restPortUrl + "user", List.class);

		return response;

	}

	public ResponseEntity<String> updateUserProfileCamunda(String userId, User user) {

		System.out.println(" createUserDup restCreateUserUrl...:" + restPortUrl + "user/" + userId + "/profile" + "/n"
				+ restPortUrl + "user/" + userId + "/credentials");

		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		ResponseEntity<String> res = null;
		try {
			String url = restPortUrl + "user/" + userId + "/profile";
			String url2 = restPortUrl + "user/" + userId + "/credentials";
			HttpEntity<Profile> request = new HttpEntity<>(user.getProfile(), headers);

			HttpEntity<Credentials> request2 = new HttpEntity<>(user.getCredentials(), headers);
			rest.put(url, request);

			rest.put(url2, request2);

			res = ResponseEntity.status(HttpStatus.OK).body("User Updated Successfully");

		} catch (Exception e) {
			e.printStackTrace();
			res = ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Something went wrong. Could not update user.");

		}

		return res;

	}

	public ResponseEntity deleteUser(String userId) {
		System.out.println("Calling deleteUserURL..." + restPortUrl + "user/" + userId);

		try {

			rest.delete(restPortUrl + "user/" + userId);

			System.out.println("The user was deleted");

			return ResponseEntity.status(HttpStatus.OK).body("The user was deleted");

		} catch (RestClientException r) {
			System.out.println(r);
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("User could not be deleted.");

		}

	}

	@Override
	public UserDomain addUser(UserDomain user) {

		return userRepository.save(user);

	}

	@Override
	public UserDomain getUserById(long id) throws ResourceNotFoundException {
		UserDomain user = userRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("User not found for this id :: " + id));
		return user;
	}

	@Override
	public User getUserByUserId(String id) throws ResourceNotFoundException {
		return null;
	}

	@Override
	public Map<String, Boolean> deleteUserById(long id) throws ResourceNotFoundException {
		UserDomain user = userRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("User not found for this id :: " + id));

		userRepository.delete(user);
		Map<String, Boolean> response = new HashMap<>();
		response.put("deleted", Boolean.TRUE);
		return response;
	}

	@Override
	public User updateUserById(long id, User userDetails) throws ResourceNotFoundException {
		return null;
	}

	@Override
	public User updateUserByUserId(String id, User userDetails) throws ResourceNotFoundException {
		return null;
	}

	@Override
	public UserDomain getUserByUserLN(String userLN) throws ResourceNotFoundException {
		UserDomain user = userRepository.findByUserLn(userLN);
		if (user == null)
			throw new ResourceNotFoundException("User not found for this userLN :: " + userLN);
		return user;
	}

	@Override
	public UserDomain getUserByUserEmail(String userEmail) throws ResourceNotFoundException {
		UserDomain user = userRepository.findByUserEmail(userEmail);
		if (user == null)
			throw new ResourceNotFoundException("User not found for this email :: " + userEmail);
		return user;
	}

	@Override
	public UserDomain getUserByUserFN(String userFN) throws ResourceNotFoundException {
		UserDomain user = userRepository.findByUserFn(userFN);
		if (user == null)
			throw new ResourceNotFoundException("User not found for this FN :: " + userFN);
		return user;
	}

	@Override
	public UserDomain getUserByUserPwd(String userPwd) throws ResourceNotFoundException {
		UserDomain user = userRepository.findByUserPwd(userPwd);
		if (user == null)
			throw new ResourceNotFoundException("User not found for this password :: " + userPwd);
		return user;
	}

	public UserDomain updateUserDomainById(UserDomain userDetails) throws ResourceNotFoundException {

		Optional<UserDomain> user = userRepository.findById(userDetails.getId());

		user.get().setUserEmail(userDetails.getUserEmail());
		user.get().setUserFn(userDetails.getUserFn());
		user.get().setUserLn(userDetails.getUserLn());
//		user.setUpdateDate(new Date());

		return userRepository.save(user.get());
	}

	public ResponseEntity<String> addUsertoMulGroups(String userId, String groupId) {
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		System.out.println(groupId);
		HttpEntity<String> request = new HttpEntity<>(null, headers);
		ResponseEntity<String> res = null;
		try {

			if (isUserinGroup(groupId, userId).equals(false)) {

				rest.put(restPortUrl + "group/" + groupId + "/members/" + userId, request);
				System.out.println("user added to group");
				res = ResponseEntity.status(HttpStatus.OK)
						.body("User was successfully added to the group with Id " + groupId);
			} else {
				System.out.println("User is already a member of the group ");
				res = ResponseEntity.status(HttpStatus.OK)
						.body("User is already a member of the group with Id " + groupId);

			}

			return res;

		}

		catch (Error e) {
			return ResponseEntity.status(HttpStatus.OK).body("Something went wrong try again");
		}
	}

	public ResponseEntity<GroupDto[]> getGroupById(String groupId) {

		try {

			return ResponseEntity.status(HttpStatus.OK)
					.body(rest.getForEntity(restPortUrl + "/group?id=" + groupId, GroupDto[].class).getBody());
		}

		catch (Error e) {
			return ResponseEntity.status(HttpStatus.OK).body(null);
		}
	}

	public Boolean isUserinGroup(String groupId, String userId) {

		String url = restPortUrl + "user?memberOfGroup=" + groupId;
		System.out.println(url);
		Boolean ret = false;
		ResponseEntity<Profile[]> response = rest.getForEntity(url, Profile[].class);
		Profile[] profiles = response.getBody();

		for (Profile profile : profiles) {

			if (profile.getId().equals(userId)) {
				ret = true;
				break;
			}
		}
		return ret;
	}

	public ResponseEntity<GroupDto[]> memberOf(String userId) {

		String url = restPortUrl + "group?member=" + userId;
		System.out.println(url);
		try {

			return rest.getForEntity(url, GroupDto[].class);
		} catch (Error e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
		}

	}

}