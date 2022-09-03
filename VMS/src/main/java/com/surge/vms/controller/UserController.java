package com.surge.vms.controller;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.surge.vms.business.UserService;
import com.surge.vms.business.VendorService;
import com.surge.vms.exception.ResourceNotFoundException;
import com.surge.vms.model.User;
import com.surge.vms.model.UserDomain;
import com.surge.vms.process.integrator.RuntimeService;

@CrossOrigin(origins = "*", allowedHeaders = "*")
@RestController

public class UserController {

	@Autowired
	private VendorService vendorService;

	@Autowired
	private RuntimeService runtimeService;

	@Autowired
	private UserService userService;

	@PostMapping("/user/create")
	public ResponseEntity<String> addUser(@RequestBody User newUser) {
		System.out.println("addUser....");
		System.out.println(newUser.getProfile().getEmail());
		System.out.println(newUser.getCredentials().getPassword());
		
		if (!userService.userExists(newUser.getProfile().getId())) {

			userService.createUser(newUser);
			return ResponseEntity.status(HttpStatus.OK).body("User created successfully");
		} else {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("User with provided Id already exists");
		}

	}

	@GetMapping("/users")
	public List<User> getAllUsers() {

		return userService.getUsers();

	}

	@PutMapping("/update/user/{userId}")
	public ResponseEntity<String> updateUser(@PathVariable String userId, @RequestBody User user)
			throws ResourceNotFoundException {

		if (userService.userExists(userId)) {

			// userService.updateUserByUserId(user.getProfile().getId(), user);
			return userService.updateUserProfileCamunda(userId, user);

		} else {

			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("User does not exist");
		}

	}

	@DeleteMapping("/delete/user/{userId}")

	public ResponseEntity deleteUser(@PathVariable String userId) {
		return userService.deleteUser(userId);
	}

	@PutMapping("/addUsertoMultipleGroups/{userId}/{groupId}")

	public ResponseEntity deleteUser(@PathVariable String userId, @PathVariable String groupId) {

		return userService.addUsertoMulGroups(userId, groupId);
	}

	@GetMapping("/groupbyId/{groupId}")

	public ResponseEntity getGroup(@PathVariable String groupId) {

		return userService.getGroupById(groupId);
	}

	@GetMapping("/memberships/{userId}")

	public ResponseEntity memberOf(@PathVariable String userId) {

		return userService.memberOf(userId);
	}

}