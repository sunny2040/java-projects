package com.sports.student.sportsstudent.repository;

import org.springframework.data.repository.CrudRepository;

import com.sports.student.sportsstudent.model.User;

public interface UserRepository extends CrudRepository<User, String> {

}
