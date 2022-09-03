package com.sports.student.sportsstudent.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sports.student.sportsstudent.model.User;
import com.sports.student.sportsstudent.repository.UserRepository;

@Service
public class UserService {

	@Autowired
	UserRepository userRepository;

	// saving user record
	public void saveOrUpdate(User user) {
		/*
		 * UUID uuid = UUID.randomUUID(); sports.setSports_id(uuid.toString());
		 */
		userRepository.save(user);
	}
}
