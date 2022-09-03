package com.sports.student.sportsstudent.model;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table
public class Student {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column
	private int student_id;
	@Column
	private String firstName;
	@Column
	private String lastName;
	@Column
	private String address;
	@Column
	private String schoolName;
	@Column
	private Timestamp dob;
	@Column
	private String grade;
	@Column
	private String gender;
	@Column
	private Integer mobile;
	@Column
	private Integer height;
	@Column
	private Integer weight;
	@Column
	private String sports_id;
	@Column
	private String batch_id;
	@Column
	private String coach_id;
	@Column
	private String parents_id;
	@Column
	private Timestamp created_dt;
	@Column
	private String created_by;
	@Column
	private Timestamp last_updated_dt;
	@Column
	private String last_updated_by;

	@OneToOne()
	@JoinColumn(name = "user_id", referencedColumnName = "user_id")
	private User user;

	public int getStudent_id() {
		return student_id;
	}

	public void setStudent_id(int student_id) {
		this.student_id = student_id;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getSchoolName() {
		return schoolName;
	}

	public void setSchoolName(String schoolName) {
		this.schoolName = schoolName;
	}

	public Timestamp getDob() {
		return dob;
	}

	public void setDob(Timestamp dob) {
		this.dob = dob;
	}

	public String getGrade() {
		return grade;
	}

	public void setGrade(String grade) {
		this.grade = grade;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public Integer getMobile() {
		return mobile;
	}

	public void setMobile(Integer mobile) {
		this.mobile = mobile;
	}

	public Integer getHeight() {
		return height;
	}

	public void setHeight(Integer height) {
		this.height = height;
	}

	public Integer getWeight() {
		return weight;
	}

	public void setWeight(Integer weight) {
		this.weight = weight;
	}

	public String getSports_id() {
		return sports_id;
	}

	public void setSports_id(String sports_id) {
		this.sports_id = sports_id;
	}

	public String getBatch_id() {
		return batch_id;
	}

	public void setBatch_id(String batch_id) {
		this.batch_id = batch_id;
	}

	public String getCoach_id() {
		return coach_id;
	}

	public void setCoach_id(String coach_id) {
		this.coach_id = coach_id;
	}

	public String getParents_id() {
		return parents_id;
	}

	public void setParents_id(String parents_id) {
		this.parents_id = parents_id;
	}

	public Timestamp getCreated_dt() {
		return created_dt;
	}

	public void setCreated_dt(Timestamp created_dt) {
		this.created_dt = created_dt;
	}

	public String getCreated_by() {
		return created_by;
	}

	public void setCreated_by(String created_by) {
		this.created_by = created_by;
	}

	public Timestamp getLast_updated_dt() {
		return last_updated_dt;
	}

	public void setLast_updated_dt(Timestamp last_updated_dt) {
		this.last_updated_dt = last_updated_dt;
	}

	public String getLast_updated_by() {
		return last_updated_by;
	}

	public void setLast_updated_by(String last_updated_by) {
		this.last_updated_by = last_updated_by;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

}