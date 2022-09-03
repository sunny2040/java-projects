package com.sports.student.sportsstudent.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.sports.student.sportsstudent.model.Student;
import com.sports.student.sportsstudent.service.StudentService;

@RestController
public class StudentController {
	@Autowired
	StudentService studentService;

	@GetMapping("/student")
	private List<Student> getAllStudent() {
		return studentService.getAllStudent();
	}

	@GetMapping("/student/{id}")
	private Student getStudent(@PathVariable("id") int id) {
		return studentService.getStudentById(id);
	}

	@DeleteMapping("/stud/{id}")
	private void deleteStudent(@PathVariable("id") int id) {
		studentService.delete(id);
	}

	@PostMapping("/studs")
	private int saveStudent(@RequestBody Student student) {
		studentService.saveOrUpdate(student);
		return student.getStudent_id();
	}

	@PutMapping("/studsss/{id}")
	private void update(@PathVariable(value = "id") int id, @RequestBody Student student) {
		studentService.update(id, student);
	}
}
