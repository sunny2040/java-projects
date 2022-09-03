package com.sports.student.sportsstudent.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.sports.student.sportsstudent.model.User;
import com.sports.student.sportsstudent.service.UserService;

@RestController
public class UserController {

	@Autowired
	UserService userService;

	@PostMapping("/user")
	private User addUser(@RequestBody User user) {
		userService.saveOrUpdate(user);
		return user;
	}
}
