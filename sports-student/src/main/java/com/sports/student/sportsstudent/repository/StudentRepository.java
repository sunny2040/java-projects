package com.sports.student.sportsstudent.repository;

import org.springframework.data.repository.CrudRepository;

import com.sports.student.sportsstudent.model.Student;

public interface StudentRepository extends CrudRepository<Student, Integer> {
}
