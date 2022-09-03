package com.sports.student.sportsstudent.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sports.student.sportsstudent.model.Student;
import com.sports.student.sportsstudent.repository.StudentRepository;

@Service
public class StudentService {
	@Autowired
	StudentRepository studentRepository;

	public List<Student> getAllStudent() {
		List<Student> student = new ArrayList<Student>();
		studentRepository.findAll().forEach(stud -> student.add(stud));
		return student;
	}

	public Student getStudentById(int id) {
		return studentRepository.findById(id).get();
	}

	public void saveOrUpdate(Student student) {
		studentRepository.save(student);
	}

	public void delete(int id) {
		studentRepository.deleteById(id);
	}

	public void update(int id, Student student) {
		Optional<Student> std = studentRepository.findById(id);
		if (!std.isPresent()) {
			System.out.println(id);
		}
		Student student1 = std.get();
		student1.setFirstName(student.getFirstName());
		student1.setLastName(student.getLastName());
		student1.setSchoolName(student.getSchoolName());
		student1.setDob(student.getDob());
		student1.setGrade(student.getGrade());
		student1.setGender(student.getGender());
		student1.setHeight(student.getHeight());
		student1.setWeight(student.getWeight());
		student1.setMobile(student.getMobile());
		student1.setAddress(student.getAddress());
		student1.setSports_id(student.getSports_id());
		student1.setBatch_id(student.getBatch_id());
		student1.setCoach_id(student.getBatch_id());
		student1.setParents_id(student.getParents_id());
		student1.setCreated_dt(student.getCreated_dt());
		student1.setCreated_by(student.getCreated_by());
		student1.setLast_updated_dt(student.getLast_updated_dt());
		student1.setLast_updated_by(student.getLast_updated_by());
		studentRepository.save(student1);
	}
}